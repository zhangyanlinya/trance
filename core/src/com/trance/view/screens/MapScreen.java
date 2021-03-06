package com.trance.view.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.TextInputListener;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
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
import com.trance.common.basedb.BasedbService;
import com.trance.common.socket.model.Request;
import com.trance.common.socket.model.Response;
import com.trance.common.socket.model.ResponseStatus;
import com.trance.common.util.ProtostuffUtil;
import com.trance.empire.config.Module;
import com.trance.empire.model.Result;
import com.trance.empire.modules.army.model.ArmyDto;
import com.trance.empire.modules.battle.handler.BattleCmd;
import com.trance.empire.modules.battle.model.ReqBattle;
import com.trance.empire.modules.building.handler.BuildingCmd;
import com.trance.empire.modules.building.model.BuildingDto;
import com.trance.empire.modules.building.model.BuildingType;
import com.trance.empire.modules.building.model.ReqHarvist;
import com.trance.empire.modules.building.model.basedb.CityElement;
import com.trance.empire.modules.mapdata.handler.MapDataCmd;
import com.trance.empire.modules.mapdata.model.ReqChangeMapData;
import com.trance.empire.modules.player.model.Player;
import com.trance.empire.modules.player.model.PlayerDto;
import com.trance.empire.modules.reward.result.ValueResultSet;
import com.trance.empire.modules.reward.service.RewardService;
import com.trance.empire.modules.world.handler.WorldCmd;
import com.trance.empire.modules.world.model.ReqChangePlayer;
import com.trance.empire.modules.world.model.ResChangePlayer;
import com.trance.view.TranceGame;
import com.trance.view.actors.Building;
import com.trance.view.actors.MapImage;
import com.trance.view.actors.ResImage;
import com.trance.view.constant.ControlType;
import com.trance.view.constant.UiType;
import com.trance.view.controller.GestureController;
import com.trance.view.dialog.DialogArmyStage;
import com.trance.view.dialog.DialogAttackInfoStage;
import com.trance.view.dialog.DialogOperateStage;
import com.trance.view.dialog.DialogRankUpStage;
import com.trance.view.freefont.FreeBitmapFont;
import com.trance.view.freefont.FreeFont;
import com.trance.view.mapdata.MapData;
import com.trance.view.model.Gird;
import com.trance.view.model.RangeInfo;
import com.trance.view.screens.base.BaseScreen;
import com.trance.view.screens.type.ButtonType;
import com.trance.view.textinput.RenameInputListener;
import com.trance.view.utils.FontUtil;
import com.trance.view.utils.FormulaUtil;
import com.trance.view.utils.MsgUtil;
import com.trance.view.utils.RandomUtil;
import com.trance.view.utils.ResUtil;
import com.trance.view.utils.SocketUtil;
import com.trance.view.utils.TimeUtil;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.Stack;
import java.util.concurrent.ArrayBlockingQueue;


public class MapScreen extends BaseScreen implements InputProcessor {

    private static final int BASE_NUMBER = 100;

    private float menu_width = 0;
	/** 控制区域高度 */
	private int width;
	private int height;
	/** 数组宽数量 */
	private final int ARR_WIDTH_SIZE = 16;
	/** 数组高数量 */
	private final int ARR_HEIGHT_SIZE = 20;
	
	/** 中间游戏区域的百分比 */
	private final  double percent = 0.8;
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
	private Sprite toWorld;
	private Image toChange;
	private Sprite toTrain;
//	private Sprite toUpBuilding;
	private Sprite toRankUp;
	private Sprite toAttackInfo;
	private Sprite rename;

	private Label label_world;
	private Label label_rename;
	private Label label_train;
//	private Label label_upgrade;
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
    public DialogRankUpStage dialogRankUpStage;
    public DialogAttackInfoStage dialogAttackInfoStage;
    public DialogOperateStage dialogOperateStage;
    
    public Stack<Stage> dialogs = new Stack<Stage>();

	private String msg;

    private String newNameMsg;

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
		newNameMsg = MsgUtil.getInstance().getLocalMsg("input new name");
		font.appendText(newNameMsg);

		camera = new OrthographicCamera(width, height);
		camera.setToOrtho(false, width, height);

		stage = new Stage(new StretchViewport(width,height,camera));

		spriteBatch = new SpriteBatch();
		// Create a full screen sprite renderer and use the above camera
		spriteBatch.setProjectionMatrix(camera.combined);

		shapeRenderer = new ShapeRenderer();
		
		dialogArmyStage = new DialogArmyStage(tranceGame);
		dialogRankUpStage = new DialogRankUpStage(tranceGame);
		dialogAttackInfoStage = new DialogAttackInfoStage(tranceGame);
        dialogOperateStage = new DialogOperateStage(tranceGame);

		bg = new MapImage(ResUtil.getInstance().get("world/bg.jpg",Texture.class));
		
		float side = width/8f;

		label_world = FreeFont.getLabel(MsgUtil.getInstance().getLocalMsg("world"));
		label_world.setPosition(10 + side/2, side, Align.center);

		toWorld = new Sprite(ResUtil.getInstance().getControlTextureRegion(ControlType.WORLD));
		toWorld.setBounds(10, 0, side, side);

		label_rename = FreeFont.getLabel(MsgUtil.getInstance().getLocalMsg("rename"));
		label_rename.setPosition(side * 1 + side/2, side, Align.center);
		//Rename
		listener = new RenameInputListener();
		rename = new Sprite(ResUtil.getInstance().getControlTextureRegion(ControlType.RENAME));
		rename.setBounds(side * 1, 0, side, side);

		label_train = FreeFont.getLabel(MsgUtil.getInstance().getLocalMsg("train"));
		label_train.setPosition(side * 2 + side/2, side, Align.center);

		toTrain = new Sprite(ResUtil.getInstance().getUi(UiType.TRAIN));
		toTrain.setBounds(side * 2, 0, side, side);

//		label_upgrade = FreeFont.getLabel(MsgUtil.getInstance().getLocalMsg("upgrade"));
//		label_upgrade.setPosition(side * 3 + side/2, side, Align.center);
//
//		toUpBuilding = new Sprite(ResUtil.getInstance().getUi(UiType.UPBUILDING));
//		toUpBuilding.setBounds(side * 3, 0, side, side);


		label_ranking= FreeFont.getLabel(MsgUtil.getInstance().getLocalMsg("ranking"));
		label_ranking.setPosition(side * 3 + side/2, side, Align.center);

		toRankUp = new Sprite(ResUtil.getInstance().getUi(UiType.LEVEL));
		toRankUp.setBounds(side * 3, 0, side, side);

		label_change = FreeFont.getLabel(MsgUtil.getInstance().getLocalMsg("change"));
		label_change.setPosition(side * 4 + side/2, side, Align.center);

		toChange = new Image(ResUtil.getInstance().getUi(UiType.CHANGE));
		toChange.setBounds(side * 4, 0, side, side);
		toChange.addListener(new ClickListener(){

			@Override
			public void clicked(InputEvent event, float x, float y) {
				change();
			}
		});

		label_attack = FreeFont.getLabel(MsgUtil.getInstance().getLocalMsg("attack"));
		label_attack.setPosition(side * 5 + side/2, side, Align.center);

		attack = new Image(ResUtil.getInstance().getControlTextureRegion(ControlType.ATTACK));
		attack.setBounds(side * 5, 0, side, side);
		attack.addListener(new ClickListener(){

			@Override
			public void clicked(InputEvent event, float x, float y) {
				attack();
			}
		});


		label_info= FreeFont.getLabel(MsgUtil.getInstance().getLocalMsg("info"));
		label_info.setPosition(side * 6 + side/2, side, Align.center);

		toAttackInfo = new Sprite(ResUtil.getInstance().getUi(UiType.LEVEL));
		toAttackInfo.setBounds(side * 6, 0, side, side);
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
//			stage.addActor(rename);
//			stage.addActor(toTrain);
//			stage.addActor(toUpBuilding);
//			stage.addActor(toRankUp);
//			stage.addActor(toAttackInfo);


			stage.addActor(label_rename);
			stage.addActor(label_train);
//			stage.addActor(label_upgrade);
			stage.addActor(label_ranking);
			stage.addActor(label_info);
		}else{
			stage.addActor(toChange);
			stage.addActor(attack);

			stage.addActor(label_change);
			stage.addActor(label_attack);
		}
		initPlayerInfo();
//		stage.addActor(toWorld);
//		toWorld.draw(spriteBatch);
		stage.addActor(label_world);

		controller = new GestureController(camera, 0, width * 2, 0, height * 2);
		camera.position.set(width/2f, height/2f, 0);
		controller.setCanPan(false);

		inputMultiplexer = new InputMultiplexer();
		gestureHandler = new GestureDetector(controller);
		initInputProcessor();
	}

	private void initInputProcessor(){
//		inputMultiplexer.addProcessor(gestureHandler);
		inputMultiplexer.addProcessor(stage);
		inputMultiplexer.addProcessor(this);
		Gdx.input.setInputProcessor(inputMultiplexer);
	}


	@Override
	public void render(float delta) {
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		Gdx.gl.glClearColor(0, 0, 0, 0);
		stage.draw();

		renderRange(delta);

		spriteBatch.begin();

        toWorld.draw(spriteBatch);
		if(playerDto.isMyself()){
			font.draw(spriteBatch,msg,length,control_height -length * 2);
            font.draw(spriteBatch, Player.player.getPlayerName(),length,height - length);
            font.draw(spriteBatch, Player.player.getExperience()+"/"+levelExp ,length,height - length * 2);

            rename.draw(spriteBatch);
            toTrain.draw(spriteBatch);
//            toUpBuilding.draw(spriteBatch);
            toRankUp.draw(spriteBatch);
            toAttackInfo.draw(spriteBatch);
		}else{
			font.draw(spriteBatch, playerDto.getPlayerName(),length,height - length);
		}

		spriteBatch.end();
		
		for(Stage s : dialogs){
			s.act();
			s.draw();
		}
		
		if (Gdx.input.isKeyPressed(Input.Keys.BACK)) {
			onBackPressed();
		}
		
		super.render(delta);
	}

	private int clickcount;

	private void onBackPressed() {
		boolean hasAction = hideDialog();
		if(hasAction){
		    return;
        }

		if(clickcount < 6){
		    clickcount++;
		    return;
        }

        clickcount = 0;
        tranceGame.setScreen(tranceGame.worldScreen);
	}
	
	
	private long levelExp;
//
//	private static int hitCount = 0;
//	private static long hitTime = System.currentTimeMillis();
	
	private void initPlayerInfo(){
		float side = width / 5f;
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
			int n = map.length - 1 - i;
			for (int j = 0; j < map[i].length; j++) {
//				final int type = map[i][j];
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
					grass.setPosition(x - grass.getWidth()/2 - length, y);
					stage.addActor(grass);
				}else if(j == map[i].length -1){
//					int index = RandomUtil.nextInt(5) + 1;
//					Image grass = new MapImage(ResUtil.getInstance().get("world/tree" + index +".png", Texture.class));
					Image grass = new MapImage(ResUtil.getInstance().get("world/wall.png", Texture.class));
					grass.setPosition(x + length, y);
					stage.addActor(grass);
				}
			}
		}

		// add building
        for(BuildingDto dto : playerDto.getBuildings().values()) {
            addBuildingActor(dto);
        }
	}

	private void addBuildingActor( BuildingDto dto){
        int i = dto.getX();
        int j = dto.getY();
        Building block = new Building();
        float px = menu_width + j * length;
        float py = control_height + i * length;


//		int n = ARR_HEIGHT_SIZE - 1 - i;
//		float px = menu_width + j * length;
//		float py = control_height + n * length;

        block.setIndex(i, j);
        block.init(null, dto.getMid(), px, py, length, length, shapeRenderer, dto);
        stage.addActor(block);
    }


    private Gird calculateIndex(float x, float y) {
		x -= menu_width;
		y -= control_height;

		if (x < 0 || y < 0) {
			return null;
		}

		int j = (int) (x / length);
		if (j >= ARR_WIDTH_SIZE) {
			return null;
		}

		int i = (int) (y / length);
		if (i >= ARR_HEIGHT_SIZE) {
			return null;
		}

		int id = playerDto.getMap()[i][j];

		if (id > BASE_NUMBER) { //表示占了不止一个格子
			Gird temp = parseCode(id);
			i = temp.i;
			j = temp.j;
		}

		BuildingDto dto = playerDto.getBuildings().get(PlayerDto.getKey(i, j));
		if (dto != null) {
			id = dto.getMid();
		}

		float cx = menu_width + j * length;
		float cy = control_height + i * length;

		return new Gird(id, i, j, cx, cy);
	}

    private int toOccupyCode(int i, int j){
        return  (i+1) * BASE_NUMBER + (j+1);
    }

    private Gird parseCode(int code){
        int i = code / BASE_NUMBER;
        int j = code % BASE_NUMBER;
        return new Gird(i-1, j-1);
    }

    public void refreshLeftBuiding() {
		float side = width/10f;
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

        int officeLvl = playerDto.getOfficeLevel();
        Map<Integer, Integer> hasMap = playerDto.getHasBuildingSize();
        Collection<CityElement> list = BasedbService.listAll(CityElement.class);
        for(CityElement element : list){
            if(element.getOpenLevel() <= officeLvl){
                Integer hasBuildNum = hasMap.get(element.getId());
                if(hasBuildNum == null){
                    hasBuildNum = 0;
                }
                int leftNum = element.getAmount() - hasBuildNum;
                if(leftNum> 0){
                    Building buiding = new Building();
                    int rate  = i % 5;
                    float x = rate * side + length;
                    int rate2 = i/5 + 1;
                    float y = control_height - (length * 2 + rate2 * length * 2 );
                    buiding.init(null, element.getId(), x, y, length,length, null, leftNum);
                    stage.addActor(buiding);
                    i++;
                }
            }
        }
	}

    private void toastOperator(BuildingDto dto, float x, float y){
        dialogOperateStage.setBuildingDto(dto);
        setOperateStageDailog(true, x ,y);
    }

	private Building a ;
	private float oldx;
	private float oldy;
	private int oldi;
	private int oldj;
	private int oldType;
    private boolean isNew;

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
//		MsgUtil.getInstance().showLog("screenY=" + screenY);
        isNew = false;
		tochTaskButton(screenX, screenY,pointer, button);

        if(!isEdit()){
            return false;
        }

		Vector3 vector3 = new Vector3(screenX, screenY, 0);
		camera.unproject(vector3); // 坐标转化  
		float x = vector3.x;
		float y = vector3.y;
//		MsgUtil.getInstance().showLog("y=" + y);

		if(y < 0){
			return false;
		}

		Actor actor = stage.hit(x, y, true);
		if(actor == null || !(actor instanceof Building)){
			return false;
		}

		Building b = (Building) actor;
        if(b.getDto() == null && b.getLeftNum() == 0){
            return false;
        }

        if(b.getDto() != null) {
            handleBuildingOnClick(b, x, y);
        }

//        if (b.getWdto() != null) {
//            if (b.getWdto().getAmount() <= 0) {//不够建造物
//                return false;
//            }
//        }

        a = b;
        oldx = b.getX();
        oldy = b.getY();
        if(b.getDto() != null) {
            oldi = b.getDto().getX();
            oldj = b.getDto().getY();
        }else {
            oldi = -1;
            oldj = -1;
        }
        oldType = b.type;
        isNew =true;

		return false;
	}


	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		if(a == null || !isNew){
			return false;
		}
		Vector3 vector3 = new Vector3(screenX, screenY, 0);
		camera.unproject(vector3); // 坐标转化
		float x = vector3.x;
		float y = vector3.y;

		if(y < 0){
			a.setPosition(oldx, oldy);
            a.setTouchable(Touchable.enabled);//比较后就可以点了
			MsgUtil.getInstance().showLog(1 +" = "+ x + " - " + y);
			return false;
		}
		
		Gird gird = calculateIndex(x,y);
		if(gird == null){//移除
			System.out.println("gird = null");
			a.setPosition(oldx, oldy);//暂时不做移除
            a.setTouchable(Touchable.enabled);//比较后就可以点了
			MsgUtil.getInstance().showLog(2  +" = "+ x + " - " + y);
			return false;
		}

//		MsgUtil.getInstance().showLog(gird.toString());

		if(!checkXy(gird.i, gird.j, oldType)){
            a.setPosition(oldx, oldy);
            a.setTouchable(Touchable.enabled);//比较后就可以点了
			MsgUtil.getInstance().showLog(3 +" = "+ x + " - " + y);
            return false;
        }

        int[][] map = playerDto.getMap();
        if(!isBlank(map, oldi, oldj, gird.i, gird.j, oldType)){
            a.setPosition(oldx, oldy);
            a.setTouchable(Touchable.enabled);//比较后就可以点了
//			MsgUtil.getInstance().showLog(4 +" = "+ x + " - " + y);
            return false;
        }

		if(oldy <= control_height - length){//增加
//			System.out.println("开始增加...");
			if(gird.id != 0){
				a.setPosition(oldx, oldy);//不覆盖已经占坑的
                a.setTouchable(Touchable.enabled);//比较后就可以点了
				MsgUtil.getInstance().showLog(5 +" = "+ x + " - " + y);
				return false;
			}

			if(a.getLeftNum() <= 0){
				a.setPosition(oldx, oldy);
                a.setTouchable(Touchable.enabled);//比较后就可以点了
				MsgUtil.getInstance().showLog(6 +" = "+ x + " - " + y);
				return false;
			}

			//增加
			a.setPosition(gird.x, gird.y);
			a.setIndex(gird.i, gird.j);
            map[gird.i][gird.j] = oldType;

            BuildingDto dto = new BuildingDto();
            dto.setMid(oldType);
            dto.setX(gird.i);
            dto.setY(gird.j);

            a.setDto(dto);
            playerDto.addBuilding(dto);

            int leftNum = a.getLeftNum() - 1;
			if(leftNum > 0){
				Building block = new Building();
				block.init(null,oldType, oldx, oldy, length, length, null, leftNum);
				stage.addActor(block);
			}

            a.setLeftNum(0);

            addToBlank(map,  gird.i, gird.j, oldType);

			StringBuilder to = new StringBuilder();
			to.append(gird.i).append("|").append(gird.j).append("|").append(oldType);
			saveMaptoServer(null,to.toString());
            a.setTouchable(Touchable.enabled);//比较后就可以点了

			return false;
		}
		if(oldi >= 0) {
            if (gird.i == oldi && gird.j == oldj) { //没有移动
                a.setPosition(oldx, oldy);
                a.setTouchable(Touchable.enabled);//比较后就可以点了
                return false;
            }
        }

		////替换
//        int targetType = 0;
//        Building b = compute(x, y);
//        if(b != null){
//            if(oldi == -1){
//                a.setPosition(oldx, oldy);
//                a.setTouchable(Touchable.enabled);//比较后就可以点了
//                return false;
//            }
//            targetType = b.type;
//        }
//
//        if(targetType == 0){//add
//            if(!isBlank( map, -1, -1, gird.i, gird.j, targetType)){
//                a.setPosition(oldx, oldy);
//                a.setTouchable(Touchable.enabled);//比较后就可以点了
//                return false;
//            }
//        }else {
//            if (!canChange(oldType, targetType)) {
//                a.setPosition(oldx, oldy);
//                a.setTouchable(Touchable.enabled);//比较后就可以点了
//                return false;
//            }
//        }
//
//        if(b != null){
//            b.setPosition(oldx, oldy);
//            b.setIndex(oldi, oldj);
//        }

//        if(targetType == 0){//add
            moveToBlank(map, oldi, oldj, gird.i, gird.j, oldType);
//        }else{
//            map[oldi][oldj] = targetType;
//            map[gird.i][gird.j] = oldType;
//        }

//        playerDto.getMap()[oldi][oldj] = targetType;

		a.setPosition(gird.x, gird.y);
		a.setIndex(gird.i, gird.j);
//		playerDto.getMap()[gird.i][gird.j] = oldType;


//		System.out.println(oldType  +" 与 " + b.type +" 进行了交换~ ");
		
		StringBuilder from = new StringBuilder();
		from.append(oldi).append("|").append(oldj).append("|").append(oldType);
		StringBuilder to = new StringBuilder();
		to.append(gird.i).append("|").append(gird.j).append("|").append(0);
		saveMaptoServer(from.toString(),to.toString());
        a.setTouchable(Touchable.enabled);//比较后就可以点了
		return false;
	}

    /**
     * 是否空地
     * @param map
     * @param x
     * @param y
     * @param id //即将占据的id 用来计算范围
     * @return
     */
	/**
	 * 是否空地
	 *
	 * @param map
	 * @param tx
	 * @param ty
	 * @param id
	 *            //即将占据的id
	 * @return
	 */
	private boolean isBlank(int[][] map, int fx, int fy, int tx, int ty, int id) {
		MsgUtil.getInstance().showLog(  fx +"_" + fy +"_"+ tx +"_"+ ty );
		BuildingType buildingType = BuildingType.valueOf(id);
		if (buildingType == null) {
			return false;
		}

		int occupy = buildingType.getOccupy();
		int limitX = tx + occupy;
		if (limitX > ARR_HEIGHT_SIZE) {
			MsgUtil.getInstance().showLog(7  + " limitX = " + limitX);
			return false;
		}

		int limitY = ty + occupy;
		if (limitY > ARR_WIDTH_SIZE) {
			MsgUtil.getInstance().showLog(8  + " limitY = " + limitY);
			return false;
		}

		Set<Integer> codes = new HashSet<Integer>(); //old occupy
		if( fx >= 0 || fy >= 0){
			for (int i = fx; i < fx + occupy; i++) {
				for (int j = fy; j < fy + occupy; j++) {
					codes.add(toOccupyCode(i, j));
				}
			}
		}

//		MsgUtil.getInstance().showLog(9  + " codes = " + codes);
		for (int i = tx; i < limitX; i++) {
			for (int j = ty; j < limitY; j++) {
				if (map[i][j] != 0) {
					int code = toOccupyCode(i, j);
					if(!codes.contains(code)){ // 目标范围里不是完全空地
						MsgUtil.getInstance().showLog(9  + " code = " + code);
						return false;
					}
				}
			}
		}

		return true;
	}


    private boolean checkXy(int x, int y, int id) {
        if (x < 0 || x >= ARR_HEIGHT_SIZE) {
            return false;
        }
        if (y < 0 || y >= ARR_WIDTH_SIZE) {
            return false;
        }

        BuildingType buildingType = BuildingType.valueOf(id);
        if (buildingType == null) {
            return false;
        }

        int occupy = buildingType.getOccupy();
        if (occupy > 1) { // 占据大于1个格子
            if (x > ARR_HEIGHT_SIZE - occupy) {
                return false;
            }
            if (y > ARR_WIDTH_SIZE - occupy) {
                return false;
            }
        }

        return true;
    }

    private void tochTaskButton(int screenX, int screenY, int pointer, int button){
        float side = width/8f;
        float y =  height - screenY;
        if(y > side){
            return;
        }

        int buttionId = (int)(screenX / side);
        ButtonType buttonType = ButtonType.valueOf(buttionId);
        if(buttonType == null){
            return;
        }

        if(!isEdit() && buttonType != ButtonType.WORLD){
            return;
        }

        switch (buttonType){//
            case WORLD:
                toWorld();
                break;
            case RENAME:
                Gdx.input.getTextInput(listener, newNameMsg, Player.player.getPlayerName(),"");
                break;
            case TRAIN:
                train();
                break;
//            case UPGRADE:
//                upBuilding();
//                break;
            case RANKING:
                rankUp();
                break;
            case ATTACKINFO:
                attackInfo();
                break;
        }
    }

    //各种点击事件
    private void handleBuildingOnClick(Building building, float x, float y){
        BuildingType bType = BuildingType.valueOf(building.type);
        if(bType == null){
            return;
        }
        switch (bType){
            case OFFICE:
                break;
            case CANNON:
            case ROCKET:
            case FLAME:
            case GUN:
            case TOWER:
            case MORTAR:
                RangeInfo e = new RangeInfo(x,y,building.range);
                rangeQueue.offer(e);
                break;

            case HOUSE:
            case BARRACKS:
                if(isEdit()){
                    harvist(building.getDto());
                }
                break;
            default:
                break;
        }
        if(building.type > 0) {
            // 弹出操作面板
            toastOperator(building.getDto(), x, y);
        }else {
//            setOperateStageDailog(false, x, y);
        }
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
        	if(dialogs.size() > 0){
        		return;
        	}
            dialogRankUpStage.show();
            dialogs.add(dialogRankUpStage);
            inputMultiplexer.addProcessor(dialogRankUpStage);
            inputMultiplexer.removeProcessor(stage);
//			inputMultiplexer.removeProcessor(this);
        }else{
        	hideDialog();
        }
    }

    public void setArmyDailog(boolean visible) {
        if(visible){
        	if(dialogs.size() > 0){
        		return;
        	}
            dialogArmyStage.show();
            dialogs.add(dialogArmyStage);
            inputMultiplexer.addProcessor(dialogArmyStage);
            inputMultiplexer.removeProcessor(stage);
//			inputMultiplexer.removeProcessor(this);
        }else{
        	hideDialog();
        }
    }

    public void setAttackInfoDailog(boolean visible) {
        if(visible){
        	if(dialogs.size() > 0){
        		return;
        	}
            dialogAttackInfoStage.show();
            dialogs.add(dialogAttackInfoStage);
            inputMultiplexer.addProcessor(dialogAttackInfoStage);
            inputMultiplexer.removeProcessor(stage);
//			inputMultiplexer.removeProcessor(this);
        }else{
        	hideDialog();
        }
    }

    public void setOperateStageDailog(boolean visible, float x, float y) {
        if(visible){
        	if(dialogs.size() > 0){
        		return;
        	}
        	dialogs.add(dialogOperateStage);
            dialogOperateStage.show(x, y);
            inputMultiplexer.addProcessor(dialogOperateStage);
            inputMultiplexer.removeProcessor(stage);
//            inputMultiplexer.removeProcessor(this);
        }else{
        	hideDialog();
        }
    }
    
    public boolean hideDialog(){
    	if(dialogs.size() > 0){
    		Stage s = dialogs.pop();
//    		inputMultiplexer.addProcessor(stage);
//    		inputMultiplexer.addProcessor(this);
    		inputMultiplexer.removeProcessor(s);
    		return true; //有操作
    	}
    	return false;
    }

    /**
     * harvist
     */
    private void harvist(BuildingDto dto){
        if(dto == null){
            return;
        }
        int buildingId = dto.getMid();
        if(buildingId != BuildingType.HOUSE.getId() && buildingId != BuildingType.BARRACKS.getId()){
            return;
        }
        
        long now = TimeUtil.getServerTime();
        long diffTime = now - dto.getHtime();
        if(diffTime <= 100000 ){
            MsgUtil.getInstance().showMsg(Module.BUILDING, -10005);
            return;
        }

		ReqHarvist req = new ReqHarvist();
		req.setX(dto.getX());
		req.setY(dto.getY());
        Response response = SocketUtil.send(Request.valueOf(Module.BUILDING, BuildingCmd.HARVIST, req),true);
        if(response == null || response.getStatus() != 0){
            return;
        }

        byte[] bytes = response.getValueBytes();
		Result<ValueResultSet> result = ProtostuffUtil.parseObject(bytes, Result.class);
        if(result != null){
            int code = result.getCode();
            if(code != Result.SUCCESS){
                MsgUtil.getInstance().showMsg(Module.BUILDING,code);
                return ;
            }
            
			ValueResultSet valueResultSet  = result.getContent();
            if(valueResultSet != null){
                RewardService.executeRewards(valueResultSet);
            }
            
            dto.setHtime(now);
            
            Sound sound = ResUtil.getInstance().getSound(5);
            sound.play();
        }
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

       	ReqBattle req = new ReqBattle();
       	req.setX(playerDto.getX());
       	req.setY(playerDto.getY());
        Request request = Request.valueOf(Module.BATTLE, BattleCmd.START_BATTLE, req);
        Response response = SocketUtil.send(request, true);
        if(response == null || response.getStatus() != 0){
            return;
        }

        byte[] bytes = response.getValueBytes();
        Result<ValueResultSet> result = ProtostuffUtil.parseObject(bytes, Result.class);
        int code = result.getCode();
        if(code != Result.SUCCESS){
            MsgUtil.getInstance().showMsg(Module.BATTLE, code);
            return;
        }

        ValueResultSet valueResultSet = result.getContent();
        if(valueResultSet != null){
            RewardService.executeRewards(valueResultSet);
        }

        GameScreen.playerDto = playerDto;
        tranceGame.setScreen(tranceGame.gameScreen);
    }

    /**
     * change player
     */
    private void change(){
		ReqChangePlayer req = new ReqChangePlayer();
		req.setX(playerDto.getX());
		req.setY(playerDto.getY());
        Request request = Request.valueOf(Module.WORLD, WorldCmd.CHANGE_PLAYER, req);
        Response response = SocketUtil.send(request, true);
        if(response == null || response.getStatus() != 0){
            return;
        }
        byte[] bytes = response.getValueBytes();
		Result<ResChangePlayer> result = ProtostuffUtil.parseObject(bytes, Result.class);
        int code = result.getCode();
        if(result.getCode() != Result.SUCCESS){
            MsgUtil.getInstance().showMsg(Module.WORLD, code);
            return;
        }

		ResChangePlayer res = result.getContent();
        if(res != null){
            PlayerDto newPlayerDto = res.getPlayerDto();
            newPlayerDto.setX(playerDto.getX());
            newPlayerDto.setY(playerDto.getY());
            WorldScreen.setWorldPlayerDto(playerDto.getX(), playerDto.getY(), newPlayerDto);
            playerDto = newPlayerDto;
            int[][] map = res.getMap();
            if (map == null) {
                map = MapData.clonemap();
            }
            playerDto.setMap(map);

			List<BuildingDto> buildings = res.getBuildings();
            if(buildings != null){
                for(BuildingDto dto : buildings){
                    playerDto.addBuilding(dto);
                }
            }

            FontUtil.getFont().appendText(playerDto.getPlayerName());
            show();
            Sound sound = ResUtil.getInstance().getSound(8);
            sound.play();
        }
    }

    private void train(){
        setArmyDailog(true);
    }

    //	private void upBuilding(){
//		setBuildingDailog(true);
//	}
    private void attackInfo(){
        setAttackInfoDailog(true);
    }

    private void rankUp(){
        setRankUpDailog(true);
    }

    private void toWorld(){
        this.tranceGame.setScreen(tranceGame.worldScreen);
    }

    /**
     * 是否可以交换（占格子数相等才能交换）
     * @param fv
     * @param tv
     * @return
     */
    private boolean canChange(int fv, int tv){
        BuildingType fType = BuildingType.valueOf(fv);
        if (fType == null || fType == BuildingType.NONE) {
            return false;
        }
        BuildingType tType = BuildingType.valueOf(tv);
        if (tType == null || tType == BuildingType.NONE) {
            return false;
        }

        return fType.getOccupy() == tType.getOccupy();
    }


    /**
     * 向空地移动
     */
    private void moveToBlank(int[][] map, int fx, int fy, int tx, int ty, int id) {
        BuildingType buildingType = BuildingType.valueOf(id);
        if (buildingType == null || buildingType == BuildingType.NONE) {
            return;
        }

        int occupy = buildingType.getOccupy();
        for (int i = fx; i < fx + occupy; i++) {
            for (int j = fy; j < fy + occupy; j++) {
                map[i][j] = 0;
            }
        }

        int code = toOccupyCode(tx, ty);
        for (int i = tx; i < tx + occupy; i++) {
            for (int j = ty; j < ty + occupy; j++) {
                if (i == tx && j == ty) {
                    map[i][j] = id;
                } else {
                    map[i][j] = code;
                }
            }
        }
    }


    /**
     * 向空地增加
     */
    private void addToBlank(int[][] map, int tx, int ty, int id) {
        BuildingType buildingType = BuildingType.valueOf(id);
        if (buildingType == null || buildingType == BuildingType.NONE) {
            return;
        }

        int occupy = buildingType.getOccupy();
        int code  = toOccupyCode(tx, ty);
        for (int i = tx; i < tx + occupy; i++) {
            for (int j = ty; j < ty + occupy; j++) {
                if (i == tx && j == ty) {
                    map[i][j] = id;
                } else {
                    map[i][j] = code;
                }
            }
        }
    }

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		if(a != null&& isNew){
            float dst = a.dst(oldx, oldy);
            if(dst > a.getWidth()/2){
                setOperateStageDailog(false, 0 ,0);
            }
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
		ReqChangeMapData req = new ReqChangeMapData();
		req.setFrom(from);
		req.setTo(to);
		SocketUtil.sendAsync(Request.valueOf(Module.MAP_DATA, MapDataCmd.SAVE_PLAYER_MAP_DATA, req));
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
		if(Building.font != null){
			Building.font.dispose();
		}
	}

}
