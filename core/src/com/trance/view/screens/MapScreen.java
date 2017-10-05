package com.trance.view.screens;

import com.alibaba.fastjson.JSON;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.TextInputListener;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.StringBuilder;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.trance.common.socket.model.Request;
import com.trance.common.socket.model.Response;
import com.trance.common.socket.model.ResponseStatus;
import com.trance.empire.config.Module;
import com.trance.empire.model.Result;
import com.trance.empire.modules.army.model.ArmyDto;
import com.trance.empire.modules.battle.handler.BattleCmd;
import com.trance.empire.modules.building.handler.BuildingCmd;
import com.trance.empire.modules.building.model.BuildingDto;
import com.trance.empire.modules.building.model.BuildingType;
import com.trance.empire.modules.mapdata.handler.MapDataCmd;
import com.trance.empire.modules.player.model.Player;
import com.trance.empire.modules.player.model.PlayerDto;
import com.trance.empire.modules.reward.result.ValueResultSet;
import com.trance.empire.modules.reward.service.RewardService;
import com.trance.empire.modules.world.handler.WorldCmd;
import com.trance.view.TranceGame;
import com.trance.view.actors.Building;
import com.trance.view.actors.MapImage;
import com.trance.view.actors.ResImage;
import com.trance.view.constant.ControlType;
import com.trance.view.constant.UiType;
import com.trance.view.controller.GestureController;
import com.trance.view.dialog.DialogArmyStage;
import com.trance.view.dialog.DialogAttackInfoStage;
import com.trance.view.dialog.DialogBuildingStage;
import com.trance.view.dialog.DialogRankUpStage;
import com.trance.view.freefont.FreeBitmapFont;
import com.trance.view.freefont.FreeFont;
import com.trance.view.mapdata.MapData;
import com.trance.view.model.RangeInfo;
import com.trance.view.screens.base.BaseScreen;
import com.trance.view.textinput.RenameInputListener;
import com.trance.view.utils.FontUtil;
import com.trance.view.utils.FormulaUtil;
import com.trance.view.utils.MsgUtil;
import com.trance.view.utils.RandomUtil;
import com.trance.view.utils.ResUtil;
import com.trance.view.utils.SocketUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;


public class MapScreen extends BaseScreen implements InputProcessor {

	private float menu_width = 0;
	/** 控制区域高度 */
	private int width;
	private int height;
	/** 数组宽数量 */
	private final int ARR_WIDTH_SIZE = 16;
	/** 数组高数量 */
	private final int ARR_HEIGHT_SIZE = 20;
	
	/** 中间游戏区域的百分比 */
	private final  double percent = 0.9;
	/** 每格的边长 */
	private  float length = 45;
	/** 游戏区域宽 */
	private float game_width = 720;
	/** 游戏区域高 */
	private float game_height = 900;
	/** 菜单区域宽度 */
	private float control_height = 300;
	private SpriteBatch spriteBatch;
	private Image attack;
	private Image toWorld;
	private Image toChange;
	private Image toTrain;
	private Image toUpBuilding;
	private Image toRankUp;
	private Image toAttackInfo;
	private Image rename;

	private Label label_world;
	private Label label_rename;
	private Label label_train;
	private Label label_upgrade;
	private Label label_ranking;
	private Label label_attack;
	private Label label_change;
	private Label label_info;

	private boolean init;
	private TextInputListener listener;
	private PlayerDto playerDto;
	private OrthographicCamera camera;
	private Image bg;
	private GestureController controller;
	private GestureDetector gestureHandler;

	private Stage stage;
	private FreeBitmapFont font;
	
	private ShapeRenderer shapeRenderer;
	
	private InputMultiplexer inputMultiplexer;
    public DialogArmyStage dialogArmyStage;
    public DialogBuildingStage dialogBuildingStage;
    public DialogRankUpStage dialogRankUpStage;
    public DialogAttackInfoStage dialogAttackInfoStage;

	private String msg;
	
	public MapScreen(TranceGame tranceGame){
		super(tranceGame);
	}
	
	public void init(){
		width = Gdx.graphics.getWidth(); // 720
		height = Gdx.graphics.getHeight(); // 1200
		length = (int) (width * percent / ARR_WIDTH_SIZE);
		game_width   = length * ARR_WIDTH_SIZE;
		game_height  = length * ARR_HEIGHT_SIZE;
		menu_width  = (width - game_width)/2;
		control_height = height - game_height-length * 2;//再减2格

		font = FontUtil.getFont();
		font.appendText(playerDto.getPlayerName());
		msg = MsgUtil.getInstance().getLocalMsg("Drag building placement");
		font.appendText(msg);
		final String newNameMsg = MsgUtil.getInstance().getLocalMsg("input new name");
		font.appendText(newNameMsg);

		camera = new OrthographicCamera(width, height);
		camera.setToOrtho(false, width, height);
		stage = new Stage(new StretchViewport(width,height,camera));

		spriteBatch = new SpriteBatch();
		shapeRenderer = new ShapeRenderer();
		
		dialogArmyStage = new DialogArmyStage(tranceGame);
		dialogBuildingStage = new DialogBuildingStage(tranceGame);
		dialogRankUpStage = new DialogRankUpStage(tranceGame);
		dialogAttackInfoStage = new DialogAttackInfoStage(tranceGame);

		bg = new MapImage(ResUtil.getInstance().get("world/bg.jpg",Texture.class));
		
		float side = width/8;

		label_world = FreeFont.getLabel(MsgUtil.getInstance().getLocalMsg("world"));
		label_world.setPosition(10 + side/2, side, Align.center);

		toWorld = new Image(ResUtil.getInstance().getControlTextureRegion(ControlType.WORLD));
		toWorld.setBounds(10, 0, side, side);
		toWorld.addListener(new ClickListener(){
			
			@Override
			public void clicked(InputEvent event, float x, float y) {
				toWorld();
			}
		});

		label_rename = FreeFont.getLabel(MsgUtil.getInstance().getLocalMsg("rename"));
		label_rename.setPosition(side * 1 + side/2, side, Align.center);
		//Rename
		listener = new RenameInputListener();
		rename = new Image(ResUtil.getInstance().getControlTextureRegion(ControlType.RENAME));
		rename.setBounds(side * 1, 0, side, side);
		rename.addListener(new ClickListener(){

			@Override
			public void clicked(InputEvent event, float x, float y) {
				Gdx.input.getTextInput(listener, newNameMsg, Player.player.getPlayerName(),"");
			}
		});


		label_train = FreeFont.getLabel(MsgUtil.getInstance().getLocalMsg("train"));
		label_train.setPosition(side * 2 + side/2, side, Align.center);

		toTrain = new Image(ResUtil.getInstance().getUi(UiType.TRAIN));
		toTrain.setBounds(side * 2, 0, side, side);
		toTrain.addListener(new ClickListener(){
			
			@Override
			public void clicked(InputEvent event, float x, float y) {
				train();
			}
		});


		label_upgrade = FreeFont.getLabel(MsgUtil.getInstance().getLocalMsg("upgrade"));
		label_upgrade.setPosition(side * 3 + side/2, side, Align.center);

		toUpBuilding = new Image(ResUtil.getInstance().getUi(UiType.UPBUILDING));
		toUpBuilding.setBounds(side * 3, 0, side, side);
		toUpBuilding.addListener(new ClickListener(){
			
			@Override
			public void clicked(InputEvent event, float x, float y) {
				upBuilding();
			}
		});



		label_ranking= FreeFont.getLabel(MsgUtil.getInstance().getLocalMsg("ranking"));
		label_ranking.setPosition(side * 4 + side/2, side, Align.center);

		toRankUp = new Image(ResUtil.getInstance().getUi(UiType.LEVEL));
		toRankUp.setBounds(side * 4, 0, side, side);
		toRankUp.addListener(new ClickListener(){
			
			@Override
			public void clicked(InputEvent event, float x, float y) {
				rankUp();
			}
		});

		label_change = FreeFont.getLabel(MsgUtil.getInstance().getLocalMsg("change"));
		label_change.setPosition(side * 5 + side/2, side, Align.center);

		toChange = new Image(ResUtil.getInstance().getUi(UiType.CHANGE));
		toChange.setBounds(side * 5, 0, side, side);
		toChange.addListener(new ClickListener(){

			@Override
			public void clicked(InputEvent event, float x, float y) {
				change();
			}
		});

		label_attack = FreeFont.getLabel(MsgUtil.getInstance().getLocalMsg("attack"));
		label_attack.setPosition(side * 6 + side/2, side, Align.center);

		attack = new Image(ResUtil.getInstance().getControlTextureRegion(ControlType.ATTACK));
		attack.setBounds(side * 6, 0, side, side);
		attack.addListener(new ClickListener(){

			@Override
			public void clicked(InputEvent event, float x, float y) {
				attack();
			}
		});


		label_info= FreeFont.getLabel(MsgUtil.getInstance().getLocalMsg("info"));
		label_info.setPosition(side * 7 + side/2, side, Align.center);

		toAttackInfo = new Image(ResUtil.getInstance().getUi(UiType.LEVEL));
		toAttackInfo.setBounds(side * 7, 0, side, side);
		toAttackInfo.addListener(new ClickListener(){

			@Override
			public void clicked(InputEvent event, float x, float y) {
				attackInfo();
			}
		});
	}
	
	public void setPlayerDto(PlayerDto playerDto){
		this.playerDto = playerDto;
	}

	@Override
	public void show() {
		if(!init){
			init();
			init = true;
		}
		
		MapData.gamerunning = false;
		//文字 

		stage.clear();
		float w = bg.getWidth();
		float h = bg.getHeight();
		for(float x = -w ; x < stage.getWidth(); x += w){//background;
			for(float y = -h * 5 ; y < stage.getHeight()  + h* 5; y += h){
				bg = new MapImage(ResUtil.getInstance().get("world/bg.jpg",Texture.class));
				bg.setPosition(x, y);
				stage.addActor(bg);
			}
		}
		
		for(int i = 0 ; i < 5; i ++){
			int index = RandomUtil.nextInt(4) + 1;
			int x = RandomUtil.nextInt(width);
			int y = RandomUtil.nextInt(height);
			Image grass = new MapImage(ResUtil.getInstance().get("world/soil" + index +".png", Texture.class));
			grass.setPosition(x, y);
			stage.addActor(grass);
		}
		
		initMap();//初始化地图
		if(isEdit()){
			refreshPlayerDtoData();
			refreshLeftBuiding();
			stage.addActor(rename);
			stage.addActor(toTrain);
			stage.addActor(toUpBuilding);
			stage.addActor(toRankUp);
			stage.addActor(toAttackInfo);


			stage.addActor(label_rename);
			stage.addActor(label_train);
			stage.addActor(label_upgrade);
			stage.addActor(label_ranking);
			stage.addActor(label_info);
		}else{
			stage.addActor(toChange);
			stage.addActor(attack);

			stage.addActor(label_change);
			stage.addActor(label_attack);
		}
		initPlayerInfo();
		stage.addActor(toWorld);
		stage.addActor(label_world);

		controller = new GestureController(camera, 0, width * 2, 0, height * 2);
		camera.position.set(width/2, height/2, 0);
		controller.setCanPan(false);

		inputMultiplexer = new InputMultiplexer();
		gestureHandler = new GestureDetector(controller);
		initInputProcessor();
	}

	private void initInputProcessor(){
		inputMultiplexer.addProcessor(gestureHandler);
		inputMultiplexer.addProcessor(stage);
		inputMultiplexer.addProcessor(this);
		Gdx.input.setInputProcessor(inputMultiplexer);
	}


	/**
	 *  地图是否可编辑
	 * @return
	 */
	private boolean isEdit(){
		return(playerDto.isMyself());//
	}
	
	
	/**
	 * attack other player
	 */
	private void attack(){
		Map<Integer,ArmyDto> armys = Player.player.getArmys();
		if(armys == null || armys.isEmpty()){
			return;
		}

		boolean deadAll = true;
		for(ArmyDto dto : armys.values()){
			if(dto.getAmout() > 0){
				deadAll = false;
				break;
			}
		}
		if(deadAll){
			MsgUtil.getInstance().showMsg(Module.BATTLE,-10003);
			return;
		}
		
		HashMap<String,Object> params = new HashMap<String,Object>();
		params.put("x", playerDto.getX());
		params.put("y", playerDto.getY());
		Request request = Request.valueOf(Module.BATTLE, BattleCmd.START_BATTLE, params);
		Response response = SocketUtil.send(request, true);
		if(response == null || response.getStatus() != ResponseStatus.SUCCESS){
			return;
		}
		
		byte[] bytes = response.getValueBytes();
		String text = new String(bytes);
		@SuppressWarnings("unchecked")
		HashMap<String, Object> result = JSON.parseObject(text, HashMap.class);
		Object codeObject = result.get("result");
		int code = Integer.valueOf(String.valueOf(codeObject));
		if(code != Result.SUCCESS){
			MsgUtil.getInstance().showMsg(Module.BATTLE, code);
			return;
		}
		
		Object o = result.get("content");
		if(o != null){
			ValueResultSet valueResultSet =  JSON.parseObject(o.toString(), ValueResultSet.class);
			RewardService.executeRewards(valueResultSet);
		}
		
		GameScreen.playerDto = playerDto;
		tranceGame.setScreen(tranceGame.gameScreen);
	}
	
	/**
	 * change player
	 */
	private void change(){
		HashMap<String,Object> params = new HashMap<String,Object>();
		params.put("x", playerDto.getX());
		params.put("y", playerDto.getY());
		Request request = Request.valueOf(Module.WORLD, WorldCmd.CHANGE_PLAYER, params);
		Response response = SocketUtil.send(request, true);
		if(response == null || response.getStatus() != ResponseStatus.SUCCESS){
			return;
		}
		byte[] bytes = response.getValueBytes();
		String text = new String(bytes);
		@SuppressWarnings("unchecked")
		HashMap<String, Object> result = JSON.parseObject(text, HashMap.class);
		Object codeObject = result.get("result");
		int code = Integer.valueOf(String.valueOf(codeObject));
		if(code != Result.SUCCESS){
			MsgUtil.getInstance().showMsg(Module.WORLD, code);
			return;
		}
		
		Object pobj = result.get("content");
		if(pobj != null){
			PlayerDto newPlayerDto = JSON.parseObject(pobj.toString(), PlayerDto.class);
			newPlayerDto.setX(playerDto.getX());
			newPlayerDto.setY(playerDto.getY());
			WorldScreen.setWorldPlayerDto(playerDto.getX(), playerDto.getY(), newPlayerDto);
			playerDto = newPlayerDto;
			int[][] map;
			Object mobj = result.get("mapdata");
			if (mobj == null) {
				map = MapData.clonemap();
			}else{
				map = JSON.parseObject(mobj.toString(), int[][].class);
			}
			playerDto.setMap(map);
			show();
			Sound sound = ResUtil.getInstance().getSound(8);
			sound.play();
		}
	}
	
	private void train(){
		setArmyDailog(true);
	}
	
	private void upBuilding(){
		setBuildingDailog(true);
	}
	private void attackInfo(){
		setAttackInfoDailog(true);
	}

	private void rankUp(){
		setRankUpDailog(true);
	}
	
	private void toWorld(){
		this.tranceGame.setScreen(tranceGame.worldScreen);
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		Gdx.gl.glClearColor(0, 0, 0, 0);
		stage.draw();

		renderRange(delta);

		spriteBatch.begin();
		if(playerDto.isMyself()){
			font.draw(spriteBatch,msg,length,control_height -length * 2);
		}
		if(playerDto.isMyself()){
			font.draw(spriteBatch, Player.player.getPlayerName(),length,height - length);
			font.draw(spriteBatch, Player.player.getExperience()+"/"+levelExp ,length,height - length * 2);
		}else{
			font.draw(spriteBatch, playerDto.getPlayerName(),length,height - length);
		}
		spriteBatch.end();
		
		if(dialogArmyStage.isVisible()){
			dialogArmyStage.act();
			dialogArmyStage.draw();
		}
		if(dialogBuildingStage.isVisible()){
			dialogBuildingStage.act();
			dialogBuildingStage.draw();
		}
		if(dialogRankUpStage.isVisible()){
			dialogRankUpStage.act();
			dialogRankUpStage.draw();
		}
		if(dialogAttackInfoStage.isVisible()){
			dialogAttackInfoStage.act();
			dialogAttackInfoStage.draw();
		}
		super.render(delta);
	}

	private Queue<RangeInfo> rangeQueue = new ArrayBlockingQueue<RangeInfo>(10);

	private void renderRange(float delta){
		for (RangeInfo info :rangeQueue) {
			shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
			shapeRenderer.setColor(Color.GREEN);
			shapeRenderer.circle(info.getX(), info.getY(), info.getRange());
			shapeRenderer.end();
			info.setShowTime(info.getShowTime() + delta);
			if(info.getShowTime() > 2){
				rangeQueue.poll();
			}
		}
	}
	
	public void setRankUpDailog(boolean visible) {
		if(visible){
			dialogRankUpStage.show();
			inputMultiplexer.addProcessor(dialogRankUpStage);
			inputMultiplexer.removeProcessor(stage);
			inputMultiplexer.removeProcessor(this);
		}else{
			dialogRankUpStage.hide();
			inputMultiplexer.addProcessor(stage);
			inputMultiplexer.addProcessor(this);
			inputMultiplexer.removeProcessor(dialogRankUpStage);
		}
	}
	
	public void setArmyDailog(boolean visible) {
		if(visible){
			dialogArmyStage.show();
			inputMultiplexer.addProcessor(dialogArmyStage);
			inputMultiplexer.removeProcessor(stage);
			inputMultiplexer.removeProcessor(this);
		}else{
			dialogArmyStage.hide();
			inputMultiplexer.addProcessor(stage);
			inputMultiplexer.addProcessor(this);
			inputMultiplexer.removeProcessor(dialogArmyStage);
		}
	}
	
	public void setBuildingDailog(boolean visible) {
		if(visible){
			dialogBuildingStage.show();
			inputMultiplexer.addProcessor(dialogBuildingStage);
			inputMultiplexer.removeProcessor(stage);
			inputMultiplexer.removeProcessor(this);
		}else{
			dialogBuildingStage.hide();
			inputMultiplexer.addProcessor(stage);
			inputMultiplexer.addProcessor(this);
			inputMultiplexer.removeProcessor(dialogBuildingStage);
		}
	}

	public void setAttackInfoDailog(boolean visible) {
		if(visible){
			dialogAttackInfoStage.show();
			inputMultiplexer.addProcessor(dialogAttackInfoStage);
			inputMultiplexer.removeProcessor(stage);
			inputMultiplexer.removeProcessor(this);
		}else{
			dialogAttackInfoStage.hide();
			inputMultiplexer.addProcessor(stage);
			inputMultiplexer.addProcessor(this);
			inputMultiplexer.removeProcessor(dialogAttackInfoStage);
		}
	}

	private long huoseTime; //收割临时时间
	private long barracksTime; //收割临时时间

	/**
	 * harvist
	 * @param buildingId
	 */
	private void harvist(int buildingId){
		if(buildingId != BuildingType.HOUSE && buildingId != BuildingType.BARRACKS){
			return;
		}

		long now = System.currentTimeMillis();
		long diffTime = 0;
		if(buildingId == BuildingType.HOUSE){
			diffTime = now - huoseTime;
		}else{
			diffTime = now - barracksTime;
		}

		if(diffTime <= 100000 ){
			MsgUtil.getInstance().showMsg(Module.BUILDING, -10005);
			return;
		}

		Response response = SocketUtil.send(Request.valueOf(Module.BUILDING, BuildingCmd.HARVIST, buildingId),true);
		if(response == null || response.getStatus() != ResponseStatus.SUCCESS){
			return;
		}
		
		byte[] bytes = response.getValueBytes();
		String text = new String(bytes);
		@SuppressWarnings("unchecked")
		HashMap<String,Object> result = JSON.parseObject(text, HashMap.class);
		if(result != null){
			int code = Integer.valueOf(String.valueOf(result.get("result")));
			if(code != Result.SUCCESS){
				MsgUtil.getInstance().showMsg(Module.BUILDING,code);
				return ;
			}
			Object valueResult = result.get("content");
			if(valueResult != null){
				ValueResultSet valueResultSet = JSON.parseObject(JSON.toJSON(valueResult).toString(), ValueResultSet.class);
				RewardService.executeRewards(valueResultSet);
			}
			Sound sound = ResUtil.getInstance().getSound(5);
			sound.play();

			if(buildingId == BuildingType.HOUSE){
				huoseTime = System.currentTimeMillis();
			}else{
				barracksTime = now - barracksTime;
			}
		}
	}
	
	private long levelExp;
	
	private void initPlayerInfo(){
		float side = width / 5;
		ResImage level = new ResImage(ResUtil.getInstance().getUi(UiType.LEVEL),font, playerDto, 1);
		level.setBounds(side, height - length , length, length);
		stage.addActor(level);
		ResImage gold = new ResImage(ResUtil.getInstance().getUi(UiType.GOLD),font, playerDto, 2);
		gold.setBounds(side * 2, height - length, length, length);
		stage.addActor(gold);
		ResImage silver = new ResImage(ResUtil.getInstance().getUi(UiType.SILVER),font, playerDto, 3);
		silver.setBounds(side * 3,  height - length, length, length);
		stage.addActor(silver);
		ResImage foods = new ResImage(ResUtil.getInstance().getUi(UiType.FOODS),font, playerDto, 4);
		foods.setBounds(side * 4, height - length, length, length);
		stage.addActor(foods);
	}
	
	public void refreshPlayerDtoData(){
		levelExp = FormulaUtil.getExpByLevel(playerDto.getLevel() + 1);// 下一级所需经验
	}
	
	// 初始化关卡地图
	private void initMap() {
		if(playerDto == null){
			return;
		}
		int[][] map = playerDto.getMap();
		for (int i = 0; i < map.length; i++) {
			float n = map.length - 1 - i;
			for (int j = 0; j < map[i].length; j++) {
				final int type = map[i][j];
				float x = menu_width + j * length;
				float y = control_height + n * length;
				if(i == 0 ){
//					int index = RandomUtil.nextInt(5) + 1;
//					Image grass = new MapImage(ResUtil.getInstance().get("world/tree" + index +".png", Texture.class));
					Image grass = new MapImage(ResUtil.getInstance().get("world/wall.png", Texture.class));
					grass.setPosition(x, y + length);
					stage.addActor(grass);
				}else if(i == map.length-1){
					if(x < length * 4 || x > width - length * 5) { //形成一个口
//						int index = RandomUtil.nextInt(5) + 1;
//						Image grass = new MapImage(ResUtil.getInstance().get("world/tree" + index +".png", Texture.class));
						Image grass = new MapImage(ResUtil.getInstance().get("world/wall.png", Texture.class));
						grass.setPosition(x, y - length * 2);
						stage.addActor(grass);
					}
				}
				
				if(j == 0){
//					int index = RandomUtil.nextInt(5) + 1;
//					Image grass = new MapImage(ResUtil.getInstance().get("world/tree" + index +".png", Texture.class));
					Image grass = new MapImage(ResUtil.getInstance().get("world/wall.png", Texture.class));
					grass.setPosition(x - length, y);
					stage.addActor(grass);
				}else if(j == map[i].length -1){
//					int index = RandomUtil.nextInt(5) + 1;
//					Image grass = new MapImage(ResUtil.getInstance().get("world/tree" + index +".png", Texture.class));
					Image grass = new MapImage(ResUtil.getInstance().get("world/wall.png", Texture.class));
					grass.setPosition(x + length, y);
					stage.addActor(grass);
				}
				
				Building block = Building.buildingPool.obtain();
				block.setIndex(i, j);
				BuildingDto dto = playerDto.getBuildings().get(type);
				block.init(null,type, x, y, length,length, null, dto);
				stage.addActor(block);
			}
		}
	}
	
	public void refreshLeftBuiding() {
		float side = width/10;
		int i = 0;
		Array<Actor> actors = stage.getActors();
		for(Actor ac :actors){
			if(ac instanceof Building){
				Building build = (Building)ac;
				if(build.getY() <= control_height - length){
					build.remove();
				}
			}
		}
	
		for(Entry<Integer, BuildingDto> e : Player.player.getBuildings().entrySet()){
			BuildingDto dto = e.getValue();
			if(dto.getLeftAmount() <= 0){
				continue;
			}
			Building buiding = Building.buildingPool.obtain();
			int rate  = i % 5;
			float x = rate * side + length;
			int rate2 = i/5 + 1;
			float y = control_height - (length * 2 + rate2 * length * 2 );
			buiding.init(null,dto.getId(), x, y, length,length,null, dto, true);
			stage.addActor(buiding);
			i++;
		}
	}
	
	private Building a ;
	private float oldx;
	private float oldy;
	private int oldi;
	private int oldj;
	private int oldType;
	
	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		Vector3 vector3 = new Vector3(screenX, screenY, 0);
		camera.unproject(vector3); // 坐标转化  
		float x = vector3.x;
		float y = vector3.y;
		
		if(y < 0){
			return false;
		}
		
		Actor actor = stage.hit(x, y, true);
		if(actor == null || !(actor instanceof Building)){
			return false;
		}
		
		Building b = (Building) actor;
		handleBuildingOnClick(b , x, y);

		if(!isEdit()){
			return false;
		}

		if(actor.getY() <= control_height - length){//增加
			BuildingDto dto = playerDto.getBuildings().get(b.type);
//			updateBuilding(dto);
			if(dto.getLeftAmount() <= 0){//不够建造物
				return false;
			}
		}
		
		a = b;
		oldx = b.getX();
		oldy = b.getY();
		oldi = b.i;
		oldj = b.j;
		oldType = b.type;
		return false;
	}

	//各种点击事件
	private void handleBuildingOnClick(Building building, float x, float y){
		switch (building.type){
			case BuildingType.OFFICE:
				break;
			case BuildingType.CANNON:
			case BuildingType.ROCKET:
			case BuildingType.FLAME:
			case BuildingType.GUN:
			case BuildingType.TOWER:
			case BuildingType.MORTAR:
				RangeInfo e = new RangeInfo(x,y,building.range);
				rangeQueue.offer(e);
				break;

			case BuildingType.HOUSE:
			case BuildingType.BARRACKS:
				if(isEdit()){
					harvist(building.type);
				}
				break;
			default:
				break;
		}
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		if(a == null){
			return false;
		}
		Vector3 vector3 = new Vector3(screenX, screenY, 0);
		camera.unproject(vector3); // 坐标转化  
		float x = vector3.x;
		float y = vector3.y;
		
		if(y < 0){
			a.setPosition(oldx, oldy);
			a = null;
			return false;
		}
		
		Building b = compute(x,y);
		if(b == null){//移除
//			System.out.println("b = null");
			a.setPosition(oldx, oldy);//暂时不做移除
			a = null;
			return false;
		}
		
		if(oldy <= control_height - length){//增加
//			System.out.println("开始增加...");
			if(b.type != 0){
				a.setPosition(oldx, oldy);//不覆盖已经占坑的
				a = null;
				return false;
			}
			
			b.remove();
			Building.buildingPool.free(b);
			
			BuildingDto dto = playerDto.getBuildings().get(oldType);
			if(dto == null || dto.getLeftAmount() <= 0){
				a.setPosition(oldx, oldy);
				a = null;
				return false;
			}
			dto.setBuildAmount(dto.getBuildAmount() + 1);
			//增加
			a.setPosition(b.getX(), b.getY());
			a.setIndex(b.i, b.j);
			playerDto.getMap()[b.i][b.j] = oldType; 
			
			if(dto.getLeftAmount() > 0){
				Building block = Building.buildingPool.obtain();
				block.init(null,oldType, oldx, oldy, length, length, null, dto);
				stage.addActor(block);
			}
			
			StringBuilder to = new StringBuilder();
			to.append(b.i).append("|").append(b.j).append("|").append(oldType);
			saveMaptoServer(null,to.toString());
			a = null;
			return false;
		}
		
		if(oldType == b.type){
			a.setPosition(oldx, oldy);
			a = null;
			return false; //类型一样不用上传
		}
		
		////替换
		a.setPosition(b.getX(), b.getY());
		a.setIndex(b.i, b.j);
		playerDto.getMap()[b.i][b.j] = oldType; 
		
		b.setPosition(oldx, oldy);
		b.setIndex(oldi, oldj);
		playerDto.getMap()[oldi][oldj] = b.type;
		
//		System.out.println(oldType  +" 与 " + b.type +" 进行了交换~ ");
		
		StringBuilder from = new StringBuilder();
		from.append(oldi).append("|").append(oldj).append("|").append(oldType);
		StringBuilder to = new StringBuilder();
		to.append(a.i).append("|").append(a.j).append("|").append(b.type);
		saveMaptoServer(from.toString(),to.toString());
		a = null;
		return false;
	}
	
	
	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		if(a != null){
			Vector3 vector3 = new Vector3(screenX, screenY, 0);
			camera.unproject(vector3); // 坐标转化  
			float x = vector3.x;
			float y = vector3.y;
			if(y < 0){
				return false;
			}
			x = x - a.getWidth()/2;
			y = y - a.getHeight()/2;
			a.setTouchable(Touchable.disabled);//不让点中先
			a.setPosition(x, y);
		}
		return false;
	}
	
	/**
	 * compute new position
	 * @param x
	 * @param y
	 */
	private Building compute(float x, float y) {
		Actor at = stage.hit(x, y, true);
		a.setTouchable(Touchable.enabled);//比较后就可以点了
		if(at == null){
			return null;
		}
		if(at.getX() == oldx && at.getY() == oldy){//自身
			return null;
		}
		if(!(at instanceof Building)){
			return null;
		}
		Building b = (Building)at;
		if(b.getY() <= control_height - length){//与原始的不比较
			return null;
		}
		return b;
	}
	
	/**
	 * save map to server
	 */
	private void saveMaptoServer(String from ,String to){
		HashMap<String,Object> parms = new HashMap<String,Object>();
		parms.put("from", from);
		parms.put("to", to);
		SocketUtil.sendAsync(Request.valueOf(Module.MAP_DATA, MapDataCmd.SAVE_PLAYER_MAP_DATA, parms));
	}
	
	@Override
	public boolean keyDown(int keycode) {
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		return false;
	}
	


	@Override
	public void dispose() {
		if(!init){
			return;
		}
		init = false;

		stage.dispose();

		if(spriteBatch != null){
			spriteBatch.dispose();
		}
		
		if(shapeRenderer!= null){
			shapeRenderer.dispose();
		}
		
		if(dialogArmyStage != null){
			dialogArmyStage.dispose();
		}
		if(dialogBuildingStage != null){
			dialogBuildingStage.dispose();
		}
		if(Building.font != null){
			Building.font.dispose();
		}
	}

}
