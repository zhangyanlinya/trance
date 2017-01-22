package com.trance.view.screens;

import com.alibaba.fastjson.JSON;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.StringBuilder;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.trance.common.basedb.BasedbService;
import com.trance.common.socket.model.Request;
import com.trance.common.socket.model.Response;
import com.trance.common.socket.model.ResponseStatus;
import com.trance.empire.config.Module;
import com.trance.empire.model.Result;
import com.trance.empire.modules.building.model.BuildingDto;
import com.trance.empire.modules.building.model.basedb.CityElement;
import com.trance.empire.modules.dailyreward.handler.DailyRewardCmd;
import com.trance.empire.modules.player.model.Player;
import com.trance.empire.modules.player.model.PlayerDto;
import com.trance.empire.modules.reward.result.ValueResultSet;
import com.trance.empire.modules.reward.service.RewardService;
import com.trance.empire.modules.world.handler.WorldCmd;
import com.trance.view.TranceGame;
import com.trance.view.actors.WorldImage;
import com.trance.view.constant.ControlType;
import com.trance.view.constant.UiType;
import com.trance.view.controller.GestureController;
import com.trance.view.freefont.FreeBitmapFont;
import com.trance.view.freefont.FreeFont;
import com.trance.view.mapdata.MapData;
import com.trance.view.screens.base.BaseScreen;
import com.trance.view.utils.MsgUtil;
import com.trance.view.utils.RandomUtil;
import com.trance.view.utils.ResUtil;
import com.trance.view.utils.SocketUtil;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



public class WorldScreen extends BaseScreen implements InputProcessor {
	
	private final static int BASE = 10;
	private Stage stage;
	private FreeBitmapFont font;
	private float width;
	private float height;
	private OrthographicCamera camera;
	private SpriteBatch spriteBatch;
	private boolean init;
	private Image home;
	private Image fixed;
	private Image dailyReward;
	private float sw = 480 * BASE;
	private float sh = 800 * BASE;
	private final static Map<String,WorldImage> locations = new HashMap<String,WorldImage>();
	public final static Map<String,PlayerDto> playerDtos = new HashMap<String,PlayerDto>();
	private float side;
	private InputMultiplexer inputMultiplexer;
	private GestureDetector gestureHandler;
	private String tips = "";


	public WorldScreen(TranceGame tranceGame) {
		super(tranceGame);
	}
	
	public static PlayerDto getWorldPlayerDto(int x, int y) {
		String key = createKey(x, y);
		return playerDtos.get(key);
	}
	
	public static void setWorldPlayerDto(int x, int y, PlayerDto newPlayerDto) {
		String key = createKey(x, y);
		playerDtos.put(key, newPlayerDto);
		locations.get(key).setPlayerDto(newPlayerDto);
	}
	
	public static void remove(int x, int y){
		String key = createKey(x, y);
		playerDtos.remove(key);
		locations.get(key).setPlayerDto(null);
	}
	
	public static String createKey(int x ,int y){
		return new StringBuilder().append(x).append("_").append(y).toString();
	}

	@Override
	public void show() {
		if(!init){
			init();
			init = true;
		}

		inputMultiplexer = new InputMultiplexer();
		GestureController controller = new GestureController(camera, 0, sw, 0, sh);
		gestureHandler = new GestureDetector(controller);
		initInputProcessor();
	}

	private void initInputProcessor(){
		inputMultiplexer.addProcessor(gestureHandler);
		inputMultiplexer.addProcessor(this);
		inputMultiplexer.addProcessor(stage);
		Gdx.input.setInputProcessor(inputMultiplexer);

	}

	private void init(){
		width = Gdx.graphics.getWidth();
		height = Gdx.graphics.getHeight();
		side = width / 8;
		spriteBatch = new SpriteBatch();
		StringBuilder sb = new StringBuilder();
		sb.append(Player.player.getPlayerName());
		if(!playerDtos.isEmpty()){
			for(PlayerDto dto : playerDtos.values() ){
				String name = dto.getPlayerName();
				sb.append(name);
			}
		}

		font = FreeFont.getBitmapFont("world");
		font.appendText(sb.toString());

		tips = MsgUtil.getInstance().getLocalMsg("Click on the building to search for enemies");
		font.appendText(tips);

		camera = new OrthographicCamera(width, height);
		stage = new Stage(new StretchViewport(sw, sh));
		camera.setToOrtho(false, width, height);
		camera.translate(sw / 2 - 480, sh / 2 - 800);
		stage.getViewport().setCamera(camera);
		
		Image bg = new Image(ResUtil.getInstance().get("world/bg.jpg",Texture.class));
		float w = bg.getWidth();
		float h = bg.getHeight();
		for(float x = -w * 2 ; x <= sw + w; x += w){//background;
			for(float y = -h * 4 ; y <= sh + h * 4 ; y += h){
				bg = new Image(ResUtil.getInstance().get("world/bg.jpg",Texture.class));
				bg.setPosition(x, y);
				stage.addActor(bg);
			}
		}
		
		//grass
		for(int i = 0; i < 100; i++){
			int index = RandomUtil.nextInt(5) + 1;
			int x = RandomUtil.nextInt((int)sw);
			int y = RandomUtil.nextInt((int)sh);
			Image grass = new Image(ResUtil.getInstance().get("world/grass" + index +".png", Texture.class));
			grass.setPosition(x,y);
			stage.addActor(grass);
		}
		for(int i = 0; i < 100; i++){
			int index = RandomUtil.nextInt(4) + 1;
			int x = RandomUtil.nextInt((int)sw);
			int y = RandomUtil.nextInt((int)sh);
			Image grass = new Image(ResUtil.getInstance().get("world/soil" + index +".png", Texture.class));
			grass.setPosition(x,y);
			stage.addActor(grass);
		}
		
		for(int i = 0; i < 50; i++){
			int index = RandomUtil.nextInt(2) + 1;
			int x = RandomUtil.nextInt((int)sw);
			int y = RandomUtil.nextInt((int)sh);
			Image grass = new Image(ResUtil.getInstance().get("world/stone" + index +".png", Texture.class));
			grass.setPosition(x,y);
			stage.addActor(grass);
		}
		
		for(int i = 0; i < 500; i++){
			int index = RandomUtil.nextInt(5) + 1;
			int x = RandomUtil.nextInt((int)sw);
			int y = RandomUtil.nextInt((int)sh);
			Image grass = new Image(ResUtil.getInstance().get("world/tree" + index +".png", Texture.class));
			grass.setPosition(x,y);
			stage.addActor(grass);
		}
	
		
		for(int x = 0; x < BASE; x ++){
			for(int y = 0 ; y < BASE; y ++){
				PlayerDto dto ;
				if(x == BASE/2 && y == BASE/2){
					dto = Player.player;
				}else{
					dto = getWorldPlayerDto(x, y);
				}
				
				String filename = "world/me1.png";
				if(dto != null){
					if(dto.getLevel() > 10){
						filename ="world/me2.png";
					}else if(dto.getLevel() > 30){
						filename ="world/me3.png";
					}
				}
				
				final WorldImage location = new WorldImage(ResUtil.getInstance().get(filename, Texture.class), font, dto);
				float opx =  x * 480 +(x ^ y) * 20;
				float opy =  y * 800 + ((BASE - x) ^ (BASE - y)) * 40;
				location.setPosition(opx , opy);
				
				if(x == BASE/2 && y == BASE/2){
					location.setColor(Color.WHITE);
				}
				
				String key = createKey(x, y);
				locations.put(key, location);
				
				stage.addActor(location);
				
				final int ox = x;
				final int oy = y;
				location.addListener(new ClickListener() {
					@Override
					public void clicked(InputEvent event, float x, float y) {
						PlayerDto dto = location.getPlayerDto();
						if(dto != null ){
							if( ox == 5 && oy == 5){
								dto.setMyself(true);
								location.setColor(255, 0, 0, 1);
								gotoHome();
							}else{//spy get the map
								HashMap<String,Object> params = new HashMap<String,Object>();
								params.put("x", ox);
								params.put("y", oy);
								Response response = SocketUtil.send(Request.valueOf(Module.WORLD, WorldCmd.SPY, params),true);
								if(response == null || response.getStatus() != ResponseStatus.SUCCESS){
									return;
								}
								byte[] bytes = response.getValueBytes();
								String text = new String(bytes);
								@SuppressWarnings("unchecked")
								HashMap<String, Object> result = JSON.parseObject(text,HashMap.class);
								int code = (Integer) result.get("result");
								if(code != Result.SUCCESS){
									MsgUtil.getInstance().showMsg(Module.WORLD, code);
									return;
								}
								Object mobj = result.get("content");
								if (mobj != null) {
									int[][] map = JSON.parseObject(	mobj.toString(),int[][].class);
									dto.setMap(map);
								}else{
									dto.setMap(MapData.clonemap());
								}

								Object bobj = result.get("buildings");
								if(bobj == null){//defalut;
									Collection<CityElement> citys = BasedbService.listAll(CityElement.class);
									for(CityElement city : citys){
										if(city.getOpenLevel()  == 1){
											BuildingDto bto = new BuildingDto();
											bto.setId(city.getId());
											bto.setAmount(1);
											bto.setLevel(1);
											bto.setBuildAmount(1);
											dto.addBuilding(bto);
										}
									}
								}else{
									List<BuildingDto> buildings = JSON.parseArray(bobj.toString(), BuildingDto.class);
									for(BuildingDto bto : buildings){
										dto.addBuilding(bto);
									}
								}


								dto.setX(ox);
								dto.setY(oy);
								location.setPlayerDto(dto);
								font.appendText(dto.getPlayerName());
								tranceGame.mapScreen.setPlayerDto(dto);
								tranceGame.setScreen(tranceGame.mapScreen);
							}
						}else{
							HashMap<String,Object> params = new HashMap<String,Object>();
							params.put("x", ox);
							params.put("y", oy);
							Response response = SocketUtil.send(Request.valueOf(Module.WORLD, WorldCmd.ALLOCATION, params),true);
							if(response == null || response.getStatus() != ResponseStatus.SUCCESS){
								return;
							}
							byte[] bytes = response.getValueBytes();
							String text = new String(bytes);
							@SuppressWarnings("unchecked")
							HashMap<String, Object> result = JSON.parseObject(text,HashMap.class);
							int code = (Integer) result.get("result");
							if(code != Result.SUCCESS){
								MsgUtil.getInstance().showMsg(Module.WORLD, code);
								return;
							}
							
							Object pobj = result.get("content");
							dto = JSON.parseObject(pobj.toString(), PlayerDto.class);
							dto.setX(ox);
							dto.setY(oy);

							Object mobj = result.get("map");
							if (mobj != null) {
								int[][] map = JSON.parseObject(	mobj.toString(),int[][].class);
								dto.setMap(map);
							}else{
								dto.setMap(MapData.clonemap());
							}

							Object bobj = result.get("buildings");
							if(bobj == null){//defalut;
								Collection<CityElement> citys = BasedbService.listAll(CityElement.class);
								for(CityElement city : citys){
									if(city.getOpenLevel()  == 1){
										BuildingDto bto = new BuildingDto();
										bto.setId(city.getId());
										bto.setAmount(1);
										bto.setLevel(1);
										bto.setBuildAmount(1);
										dto.addBuilding(bto);
									}
								}
							}else{
								List<BuildingDto> buildings = JSON.parseArray(bobj.toString(), BuildingDto.class);
								for(BuildingDto bto : buildings){
									dto.addBuilding(bto);
								}
							}

							location.setPlayerDto(dto);
							font.appendText(dto.getPlayerName());
							tranceGame.mapScreen.setPlayerDto(dto);
							tranceGame.setScreen(tranceGame.mapScreen);
					   }
					}
				});
			}
		}
	
		//Home
		home = new Image(ResUtil.getInstance().getControlTextureRegion(ControlType.HOME));
		home.setBounds(20, 10, side, side);
		
		fixed = new Image(ResUtil.getInstance().getUi(UiType.FIXED));
		fixed.setBounds(width - side - 20 , 10, side, side);
		
		//itembox
		dailyReward = new Image(ResUtil.getInstance().getUi(UiType.ITEMBOX));
		int x = RandomUtil.betweenValue(20, (int)sw -20);
		int y = RandomUtil.betweenValue(20, (int)sh -20);
		dailyReward.setBounds(x, y, dailyReward.getWidth() + dailyReward.getWidth()/2, dailyReward.getHeight() + dailyReward.getHeight()/2);
		stage.addActor(dailyReward);
		
		dailyReward.addListener(new ClickListener(){

			@Override
			public void clicked(InputEvent event, float x, float y) {
				Response response = SocketUtil.send(Request.valueOf(Module.DAILY_REWARD, DailyRewardCmd.GET_DAILY_REWARD, null),true);
				if(response == null || response.getStatus() != ResponseStatus.SUCCESS){
					return;
				}
				
				byte[] bytes = response.getValueBytes();
				String text = new String(bytes);
				@SuppressWarnings("unchecked")
				HashMap<String, Object> result = JSON.parseObject(text,HashMap.class);
				int code = (Integer) result.get("result");
				if(code != Result.SUCCESS){
					MsgUtil.getInstance().showMsg(Module.DAILY_REWARD, code);
					return;
				}
				
				Object reward = result.get("content");
				if(reward != null){
					ValueResultSet valueResultSet = JSON.parseObject(reward.toString(), ValueResultSet.class);
					RewardService.executeRewards(valueResultSet);
				}
			}
			
		});
		
	}
	
	private void gotoHome(){
		tranceGame.mapScreen.setPlayerDto(Player.player);
		tranceGame.setScreen(tranceGame.mapScreen);
	}
	
	private void fixedToLacation() {
		camera.position.set(sw / 2, sh / 2, 0);
	}

	@Override
	public void render(float delatime) {
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		Gdx.gl.glClearColor(0, 0, 0, 0);
		stage.draw();
		spriteBatch.begin();
		home.draw(spriteBatch, 1);
		fixed.draw(spriteBatch, 1);
		font.draw(spriteBatch, tips, side, height - 20f);
		spriteBatch.end();
		super.render(delatime);
	}

	
	@Override
	public void dispose() {
		if(!init){
			return;
		}
		init = false;
		stage.dispose();
		font.dispose();
		if(spriteBatch != null)
			spriteBatch.dispose();
		playerDtos.clear();
	}

	@Override
	public boolean keyDown(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		if(screenX < side + 20  && screenY > height - side ){
			gotoHome();
		}else if(screenX > width - side -20  && screenY > height - side ){
			fixedToLacation();
		}
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		// TODO Auto-generated method stub
		return false;
	}

}
