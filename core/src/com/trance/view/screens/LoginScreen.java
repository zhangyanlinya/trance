package com.trance.view.screens;


import com.alibaba.fastjson.JSON;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.trance.common.basedb.BasedbService;
import com.trance.common.socket.model.Request;
import com.trance.common.socket.model.Response;
import com.trance.common.socket.model.ResponseStatus;
import com.trance.empire.config.Module;
import com.trance.empire.model.Result;
import com.trance.empire.modules.army.model.ArmyDto;
import com.trance.empire.modules.building.model.BuildingDto;
import com.trance.empire.modules.building.model.basedb.CityElement;
import com.trance.empire.modules.coolqueue.model.CoolQueueDto;
import com.trance.empire.modules.player.handler.PlayerCmd;
import com.trance.empire.modules.player.model.Player;
import com.trance.empire.modules.player.model.PlayerDto;
import com.trance.view.TranceGame;
import com.trance.view.mapdata.MapData;
import com.trance.view.screens.base.BaseScreen;
import com.trance.view.utils.MsgUtil;
import com.trance.view.utils.ResUtil;
import com.trance.view.utils.SocketUtil;
import com.trance.view.utils.TimeUtil;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import var3d.net.freefont.FreeBitmapFont;
import var3d.net.freefont.FreeFont;


public class LoginScreen extends BaseScreen {
	
	private Texture background;
	private Stage stage;
	private Image start;
	private SpriteBatch spriteBatch;
	private boolean init;
	private ResUtil resUtil;
	private FreeBitmapFont font;
	//画笔
  	public ShapeRenderer renderer;
  	
  	public static boolean loginSuccess;

	public LoginScreen(TranceGame tranceGame) {
		super(tranceGame);
	}

	public void init(){
		renderer = new ShapeRenderer();
		resUtil = ResUtil.getInstance();
		resUtil.init();
		stage = new Stage(new FillViewport(width,height));

		spriteBatch = new SpriteBatch();
		font = FreeFont.getBitmapFont("login");
		font.appendText("[点击图片开始游戏]");
		//GO
		background = new Texture(Gdx.files.internal("ui/loginbg.png"));
		TextureRegionDrawable startDrawable = new TextureRegionDrawable(new TextureRegion(
				background));
		start = new Image(startDrawable);
		start.addListener(new ClickListener(){
			
			@Override
			public void clicked(InputEvent event, float x, float y) {
				   if(!finish){
					   return;
				   }
				   login();
			}
		});
		
		start.setWidth(start.getWidth() * 5);
		start.setHeight(start.getHeight() * 5);
		float x = Gdx.graphics.getWidth()/2 - start.getWidth()/2;
		float y = Gdx.graphics.getHeight()/2 - start.getHeight()/2;
		start.setX(x);
		start.setY(y);
		stage.addActor(start);
		
		InputMultiplexer inputMultiplexer = new InputMultiplexer();
		inputMultiplexer.addProcessor(stage);
		Gdx.input.setInputProcessor(inputMultiplexer);


	}

	@Override
	public void show() {
		if(!init){
			init();
			init = true;
		}
		loginSuccess = false;
	}
	
	@SuppressWarnings("unchecked")
	protected void login() {
//		String src = Player.userName + Player.loginKey;
//		String loginMD5 = null;
//		try {
//			loginMD5 = CryptUtil.md5(src);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
		if(Player.userName == null){
			return;
		}
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("userName", Player.userName);
//		params.put("loginKey", loginMD5); //TODO 暂时不校验
		params.put("server", "1");
		int module = Module.PLAYER;
		int cmd = PlayerCmd.LOGIN;
		Response response = SocketUtil.send(Request.valueOf(module, cmd, params),true);
		if(response == null){
			return;
		}
		
		ResponseStatus status = response.getStatus();
		if (status == ResponseStatus.SUCCESS) {
			byte[] bytes = response.getValueBytes();
			String text = new String(bytes);
			Result<PlayerDto> result = JSON.parseObject(text, Result.class);
			if (result == null) {
				return;
			}
			
			int code = result.getCode();
			if(code != Result.SUCCESS){
				MsgUtil.getInstance().showMsg(Module.PLAYER, code);
				return;
			}
			
			Long serverTime = (Long) result.get("serverTime");
			if(serverTime != null){
				TimeUtil.init(serverTime);
			}else{
				Gdx.app.log("trance","同步服务器时间错误");
			}
			
			Object pobj = result.get("content");
			if (pobj == null) {
				return;
			}
			
			PlayerDto playerDto = JSON.parseObject(pobj.toString(),
					PlayerDto.class);
			playerDto.setMyself(true);
			
			
			int[][] map;
			Object mobj = result.get("mapdata");
			if (mobj == null) {
				map = MapData.clonemap();
			}else{
				map = JSON.parseObject(mobj.toString(), int[][].class);
			}
			playerDto.setMap(map);

			Object wobj = result.get("worldPlayers");
			if (wobj != null) {
				Map<String, Object> players = (Map<String, Object>) wobj;
				for (Entry<String, Object> e : players.entrySet()) {
					String dto = JSON.toJSONString(e.getValue());
					PlayerDto value = JSON.parseObject(dto, PlayerDto.class);
					WorldScreen.playerDtos.put(e.getKey(), value);
				}
			}
			
			Object aobj = result.get("armys");
			if(aobj != null){
				List<ArmyDto> armys = JSON.parseArray(aobj.toString(), ArmyDto.class);
				for(ArmyDto dto : armys){
					playerDto.addAmry(dto);
				}
			}
			
			Object cobj = result.get("coolQueues");
			if(cobj != null){
				List<CoolQueueDto> coolQueues = JSON.parseArray(cobj.toString(), CoolQueueDto.class);
				for(CoolQueueDto dto : coolQueues){
					playerDto.addCoolQueue(dto);
				}
			}
			
			Object bobj = result.get("buildings");
			if(bobj == null){//defalut;
				Collection<CityElement> citys = BasedbService.listAll(CityElement.class);
				for(CityElement city : citys){
					if(city.getOpenLevel()  == 1){
						BuildingDto dto = new BuildingDto();
						dto.setId(city.getId());
						dto.setAmount(1);
						dto.setLevel(1);
						dto.setBuildAmount(1);
						playerDto.addBuilding(dto);
					}
				}
			}else{
				List<BuildingDto> buildings = JSON.parseArray(bobj.toString(), BuildingDto.class);
				for(BuildingDto dto : buildings){
					playerDto.addBuilding(dto);
				}
			}
			
			Player.player = playerDto;
			
			loginSuccess = true;

			SocketUtil.heartbeat = true;
			Gdx.app.postRunnable(new Runnable() {
				
				@Override
				public void run() {
					tranceGame.startGame();
				}
			});
		}
	}

	private boolean finish;


	@Override
	public void render(float delta) {
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		Gdx.gl.glClearColor(0, 0, 0, 0);
		stage.draw();
		if(resUtil.update()){
			spriteBatch.begin();
			font.setColor(Color.GREEN);
			font.draw(spriteBatch,"[点击图片开始游戏]",width/2  ,240);
			spriteBatch.end();
			finish = true;
		}

		//draw progress
		float percent = resUtil.getProgress(); 
		renderer.setColor(Color.RED);
		renderer.begin(ShapeType.Line);
		renderer.rect(Gdx.graphics.getWidth() / 4 , 100, Gdx.graphics.getWidth() / 2, 40);
		renderer.end();
		if(percent < 0.2){
			renderer.setColor(Color.RED);
		}else if(percent < 0.5){
			renderer.setColor(Color.YELLOW);
		}else{
			renderer.setColor(Color.GREEN);
		}
		renderer.begin(ShapeType.Filled);
		renderer.rect(Gdx.graphics.getWidth() / 4 + 2, 104, percent * Gdx.graphics.getWidth()/2 - 6, 34);
		renderer.end();

		super.render(delta);
	}

	@Override
	public void dispose() {
		super.dispose();
		if(!init){
			return;
		}
		init = false;
		stage.dispose();
		font.dispose();
		background.dispose();
		spriteBatch.dispose();
		resUtil.dispose();
		renderer.dispose();
	}
	
}
