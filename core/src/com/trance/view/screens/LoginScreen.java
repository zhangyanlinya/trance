package com.trance.view.screens;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
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
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.trance.common.socket.model.Request;
import com.trance.common.socket.model.Response;
import com.trance.common.socket.model.ResponseStatus;
import com.trance.common.util.ProtostuffUtil;
import com.trance.empire.config.Module;
import com.trance.empire.model.Result;
import com.trance.empire.modules.army.model.ArmyDto;
import com.trance.empire.modules.building.model.BuildingDto;
import com.trance.empire.modules.coolqueue.model.CoolQueueDto;
import com.trance.empire.modules.player.handler.PlayerCmd;
import com.trance.empire.modules.player.model.Player;
import com.trance.empire.modules.player.model.PlayerDto;
import com.trance.empire.modules.player.model.ReqLogin;
import com.trance.empire.modules.player.model.ResLogin;
import com.trance.empire.modules.tech.model.TechDto;
import com.trance.view.TranceGame;
import com.trance.view.freefont.FreeBitmapFont;
import com.trance.view.freefont.FreeFont;
import com.trance.view.mapdata.MapData;
import com.trance.view.screens.base.BaseScreen;
import com.trance.view.screens.callback.LoginCallback;
import com.trance.view.textinput.RegisterInputListener;
import com.trance.view.utils.CharUtil;
import com.trance.view.utils.MsgUtil;
import com.trance.view.utils.ResUtil;
import com.trance.view.utils.SocketUtil;
import com.trance.view.utils.TimeUtil;

import java.util.List;
import java.util.Map;


public class LoginScreen extends BaseScreen {
	
	private Texture background;
	private Stage stage;
	private float width;
	private SpriteBatch spriteBatch;
	private boolean init;
	private ResUtil resUtil;
	private FreeBitmapFont font;
	//画笔
	private ShapeRenderer renderer;

	private String msg;

	public LoginScreen(TranceGame tranceGame) {
		super(tranceGame);
	}

	public void init(){
		renderer = new ShapeRenderer();
		resUtil = ResUtil.getInstance();
		resUtil.init();
		width = Gdx.graphics.getWidth();
		float height = Gdx.graphics.getHeight();
		stage = new Stage(new StretchViewport(width, height));
		spriteBatch = new SpriteBatch();
		font = FreeFont.getBitmapFont("login");
		msg = MsgUtil.getInstance().getLocalMsg("Click the picture to start the game");
		font.appendText("[" + msg + "]");
		//GO
		background = new Texture(Gdx.files.internal("ui/loginbg.png"));
		TextureRegionDrawable startDrawable = new TextureRegionDrawable(new TextureRegion(
				background));
		Image start = new Image(startDrawable);
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
		float x = Gdx.graphics.getWidth()/2f - start.getWidth()/2;
		float y = Gdx.graphics.getHeight()/2f - start.getHeight()/2;
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
	}

	private void showRegisterInputDialog(){
		String newNameMsg = MsgUtil.getInstance().getLocalMsg("input new name");
		String newName = CharUtil.getRandomName();
		Input.TextInputListener listener = new RegisterInputListener(new LoginCallback(){
			@Override
			public void handleMessage(Result<ResLogin> result) {
				loginAction(result.getContent());
			}
		});

		Gdx.input.getTextInput(listener, newNameMsg, newName,"");
	}
	
	private void login() {
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

		ReqLogin reqLogin = new ReqLogin();
		reqLogin.setServerId(1);
		reqLogin.setUserName(Player.userName);
//		params.put("loginKey", loginMD5); //TODO 暂时不校验
		byte module = Module.PLAYER;
		byte cmd = PlayerCmd.LOGIN;
		Response response = SocketUtil.send(Request.valueOf(module, cmd, reqLogin),true);
		if(response == null){
			return;
		}
		
		byte status = response.getStatus();
		if (status == 0) {
			byte[] bytes = response.getValueBytes();
//			String text = new String(bytes);
			Result<ResLogin> result = ProtostuffUtil.parseObject(bytes, Result.class);
			if (result == null) {
				return;
			}

			int code = result.getCode();
			if(code == -4){ //角色不存在
				showRegisterInputDialog();
				return;
			}

			if(code != 0){
				MsgUtil.getInstance().showMsg(Module.PLAYER, code);
				return;
			}

			loginAction(result.getContent());
		}
	}

	private void loginAction(ResLogin result){
		long serverTime = result.getServerTime();
		if(serverTime > 0){
			TimeUtil.init(serverTime);
		}else{
			Gdx.app.log("trance","同步服务器时间错误");
		}

		PlayerDto playerDto = result.getPlayerDto();				
		playerDto.setMyself(true);


		int[][] map = result.getMapdata();
		if (map == null) {
			map = MapData.clonemap();
		}
		playerDto.setMap(map);
		
		Map<String,PlayerDto> playerDtos = result.getWorldPlayers();
		if (playerDtos != null){
			WorldScreen.playerDtos = playerDtos;
		}

		List<ArmyDto> armys = result.getArmys();
		if(armys != null){
			for(ArmyDto dto : armys){
				playerDto.addAmry(dto);
			}
		}

		List<CoolQueueDto> coolQueues = result.getCoolQueues();
		if(coolQueues != null){
			for(CoolQueueDto dto : coolQueues){
				playerDto.addCoolQueue(dto);
			}
		}

		List<BuildingDto> buildings = result.getBuildings();
		if(buildings != null){
			for(BuildingDto dto : buildings){
				playerDto.addBuilding(dto);
			}
		}

//      playerDto.refreshWaitBudiing()

		//Tech
//		Object tobj = result.get("techs");
//		if(tobj != null){
//			List<TechDto> techDtos = JSON.parseArray(tobj.toString(), TechDto.class);
//			for(TechDto dto : techDtos){
//				playerDto.addTech(dto);
//			}
//		}
		
		//TEST
		TechDto techDto = new TechDto();
		techDto.setId(1);
		techDto.setAmout(3);
		techDto.setLevel(1);
		playerDto.addTech(techDto);

		TechDto lampDto = new TechDto();
		lampDto.setId(2);
		lampDto.setAmout(3);
		lampDto.setLevel(5);
		playerDto.addTech(lampDto);

		Player.player = playerDto;

		SocketUtil.heartbeat = true;
		Gdx.app.postRunnable(new Runnable() {

			@Override
			public void run() {
				tranceGame.startGame();
			}
		});
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
			font.draw(spriteBatch,"[" +msg+ "]",width/2 - 120  ,240);
			spriteBatch.end();
			finish = true;
		}

		//draw progress
		float percent = resUtil.getProgress(); 
		renderer.setColor(Color.RED);
		renderer.begin(ShapeType.Line);
		renderer.rect(Gdx.graphics.getWidth() / 4f , 100, Gdx.graphics.getWidth() / 2f, 40);
		renderer.end();
		if(percent < 0.2){
			renderer.setColor(Color.RED);
		}else if(percent < 0.5){
			renderer.setColor(Color.YELLOW);
		}else{
			renderer.setColor(Color.GREEN);
		}
		renderer.begin(ShapeType.Filled);
		renderer.rect(Gdx.graphics.getWidth() / 4f + 2, 104, percent * Gdx.graphics.getWidth()/2 - 6, 34);
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
