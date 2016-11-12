package com.trance.view.screens;

import com.alibaba.fastjson.JSON;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactFilter;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.ui.Window.WindowStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.trance.common.socket.model.Request;
import com.trance.common.socket.model.Response;
import com.trance.common.socket.model.ResponseStatus;
import com.trance.empire.config.Module;
import com.trance.empire.model.Result;
import com.trance.empire.modules.army.model.ArmyDto;
import com.trance.empire.modules.army.model.ArmyType;
import com.trance.empire.modules.army.model.ArmyVo;
import com.trance.empire.modules.battle.handler.BattleCmd;
import com.trance.empire.modules.building.model.BuildingType;
import com.trance.empire.modules.player.handler.PlayerCmd;
import com.trance.empire.modules.player.model.Player;
import com.trance.empire.modules.player.model.PlayerDto;
import com.trance.empire.modules.reward.result.ValueResultSet;
import com.trance.empire.modules.reward.service.RewardService;
import com.trance.view.TranceGame;
import com.trance.view.actors.Army;
import com.trance.view.actors.Building;
import com.trance.view.actors.Bullet;
import com.trance.view.actors.GameActor;
import com.trance.view.actors.MapImage;
import com.trance.view.constant.ControlType;
import com.trance.view.controller.GestureController;
import com.trance.view.mapdata.MapData;
import com.trance.view.utils.FontUtil;
import com.trance.view.utils.MsgUtil;
import com.trance.view.utils.RandomUtil;
import com.trance.view.utils.ResUtil;
import com.trance.view.utils.SocketUtil;
import com.trance.view.utils.WorldUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class GameScreen extends InputAdapter implements Screen,ContactListener {
	
	private TranceGame tranceGame;
	public static int width;
	public static int height;
	private Stage stage;
	private Image toWorld;
	private Window window;
	private SpriteBatch spriteBatch;
	private BitmapFont font;
	public static PlayerDto playerDto;
	
	
	/** 数组宽数量 */
	public final static int ARR_WIDTH_SIZE = 16;
	/** 数组高数量 */
	public final static int ARR_HEIGHT_SIZE = 20;
	
	/** 中间游戏区域的百分比 */
	public static double percent = 0.9;
	/** 每格的边长 */
	public static float length = 32;
	/** 游戏区域宽 */
	public static float game_width = 512;
	/** 游戏区域高 */
	public static float game_height = 832;
	/** 菜单区域宽度 */
	public static float menu_width = 208;
	/** 控制区域高度 */
	public static float control_height = 368;
	
	
    private World world;
    private ShapeRenderer shapeRenderer;
    private final float TIME_STEP = 1 / 50f;;
    
    public static final float WORLD_TO_BOX = 0.05f;
    public static final float BOX_TO_WORLD = 20f;
    
//    private Box2DDebugRenderer debugRenderer;
	
	public final static Array<GameActor> buildings = new Array<GameActor>();
	
	public final static Array<GameActor> armys = new Array<GameActor>();
	
	public final static Array<GameActor> connons = new Array<GameActor>();
	
	private final Array<Body> bodies = new Array<Body>();
	
	private OrthographicCamera camera;
	private Image bg;

	/**
	 * 一局所用总时间
	 */
	private final static int TOTAL_TIME = 2 * 60;
	private Action[] sAction;
	
	/**
	 * 当前时间
	 */
	private int currTime = TOTAL_TIME;
	private boolean init;
	
	private static boolean finishBattle;
	
	public GameScreen(TranceGame tranceGame) {
		this.tranceGame = tranceGame;
	}
	
	@Override
	public void show() {
		if(!init){
			init();
			init = true;
		}
		MapData.gamerunning = true;
		finishBattle = false;
		gobattle = false;
		camera.position.set(width/2 , height/2 , 0);
		currTime = TOTAL_TIME;//初始化时间 
		stage.clear();
		initClock();
		initWorld();
		initMap();
		initArmy();
		InputMultiplexer inputMultiplexer = new InputMultiplexer();
		GestureController controller = new GestureController(camera, 0, width * 2, 0, height * 2);
		GestureDetector gestureHandler = new GestureDetector(controller);
		inputMultiplexer.addProcessor(gestureHandler);
		inputMultiplexer.addProcessor(stage);
		inputMultiplexer.addProcessor(this);
		Gdx.input.setInputProcessor(inputMultiplexer);
	}
	
	private void init(){
		spriteBatch = new SpriteBatch();
		font = FontUtil.getFont();
		width = Gdx.graphics.getWidth(); // 720
		height = Gdx.graphics.getHeight(); // 1200
		stage = new Stage(new FillViewport(width * 2, height * 2));
		
		CELL_LENGHT = width / 10;
        camera = new OrthographicCamera();
        camera.setToOrtho(false, width, height);
//		debugRenderer = new Box2DDebugRenderer(); 
		stage.getViewport().setCamera(camera);
		
		
		length = (int) (width * percent / ARR_WIDTH_SIZE);
		game_width   = length * ARR_WIDTH_SIZE;
		game_height  = length * ARR_HEIGHT_SIZE;
		menu_width     = (width - game_width)/2;
		control_height = height - game_height -length * 2;//再减少2格
		shapeRenderer = new ShapeRenderer();
		
		//返回家
		toWorld = new Image(ResUtil.getInstance().getControlTextureRegion(ControlType.WORLD));
		toWorld.setBounds(10, 10, toWorld.getWidth() + toWorld.getWidth()/2, toWorld.getHeight() + toWorld.getHeight()/2);
		toWorld.addListener(new ClickListener(){
			
			@Override
			public void clicked(InputEvent event, float x, float y) {
				tranceGame.setScreen(tranceGame.worldScreen);
			}
		});
		
		//提示框
		TextureRegionDrawable tips = new TextureRegionDrawable( new TextureRegion(
				ResUtil.getInstance().get("world/tips.png",Texture.class)));
		Drawable background = new TextureRegionDrawable(tips);
		WindowStyle style = new WindowStyle(font, Color.MAGENTA, background);
		window = new Window("点赞",style);
		window.setPosition(width/2 - window.getWidth()/2, height/2 - window.getHeight()/2);
		window.addListener(new ClickListener(){

			@Override
			public void clicked(InputEvent event, float x, float y) {
				if(playerDto != null){
					Map<String,Object> params = new HashMap<String,Object>();
					params.put("targetId", playerDto.getId());
					SocketUtil.sendAsync(Request.valueOf(Module.PLAYER, PlayerCmd.UP, params));
				}
			}
		});
    	world = WorldUtils.createWorld();
	}
	
	private static float CELL_LENGHT;
	private int chooseArmyId;
	private void initArmy(){
		armys.clear();
		Map<Integer,ArmyDto> amrys_map = Player.player.getArmys();
		if(amrys_map == null || amrys_map.isEmpty()){
			return;
		}
		
		int i = 0;
		for(ArmyDto dto : amrys_map.values()){
			if(dto.getAmout() <= 0){
				continue;
			}
			
			if(i == 0){
				chooseArmyId = dto.getId();
			}
			
			dto.setGo(false);
			dto.setRegion(ResUtil.getInstance().getArmyTextureRegion(dto.getId()));
			Rectangle rect = new Rectangle(i * CELL_LENGHT, 0, CELL_LENGHT, CELL_LENGHT);
			dto.setRect(rect);
			i++;
		}
	}
	
	public static void finishBattle(boolean win){
		if(finishBattle || !gobattle){
			System.out.println("无派出 ：已结算");
			return;
		}
		
		Sound sound = ResUtil.getInstance().getSound(5);
		sound.play();
		
		Map<Integer,ArmyDto> myArmys = Player.player.getArmys();
		for(ArmyDto dto : myArmys.values()){//原来的
			if(dto.isGo()){
				dto.setAmout(0);
			}
			dto.setGo(false);
		}
		for(GameActor actor : armys){
			Army army = (Army)actor;
			int type = army.armyType.id;
			ArmyDto a = myArmys.get(type);
			if(a != null){
				a.setAmout(a.getAmout() + 1);
			}
		}
		
		//转换
		List<ArmyVo> vos = new ArrayList<ArmyVo>();
		for(ArmyDto armyDto : myArmys.values()){
			vos.add(ArmyVo.valueOf(armyDto));
		}
		
		HashMap<String,Object> params = new HashMap<String,Object>();
		params.put("armys", vos);
		params.put("x", playerDto.getX());
		params.put("y", playerDto.getY());
		params.put("state", win ? 0 : 1);
		params.put("sign", "");//TODO
		Request request = Request.valueOf(Module.BATTLE, BattleCmd.FINISH_BATTLE, params);
		Response response = SocketUtil.send(request, true);
		if(response == null){
			finishBattle = true;
			return;
		}
		
		ResponseStatus status = response.getStatus();
		if (status != ResponseStatus.SUCCESS) {
			finishBattle = true;
			return;
		}
		
		byte[] bytes = response.getValueBytes();
		String text = new String(bytes);
		@SuppressWarnings("unchecked")
		HashMap<String, Object> result = JSON.parseObject(text, HashMap.class);
		Object codeObject = result.get("result");
		int code = Integer.valueOf(String.valueOf(codeObject));
		if(code != Result.SUCCESS){
			MsgUtil.showMsg(Module.BATTLE, code);
			finishBattle = true;
			return;
		}
		
		Object o = result.get("content");
		if(o != null){
			ValueResultSet valueResultSet =  JSON.parseObject(o.toString(), ValueResultSet.class);
			RewardService.executeRewards(valueResultSet);
		}
		finishBattle = true;
		//refresh world
		if(win){
			WorldScreen.remove(playerDto.getX(), playerDto.getY());
		}
	}

	//DestoryBody
	private void destoryBody(Body body) {
		GameActor ga = (GameActor) body.getUserData();
		if(ga == null){
			return;
		}
		if(!ga.alive){
		   world.destroyBody(body);
		}
	}
	
    private void initWorld() {
    	world.clearForces();
        world.getBodies(bodies);
        for(int i = 0 ; i < bodies.size ; i++){
        	world.destroyBody(bodies.get(i));
        }
        bodies.clear();
        
        world.setContactListener(this);
        world.setContactFilter( new ContactFilter() {
			
			@Override
			public boolean shouldCollide(Fixture fixtureA, Fixture fixtureB) {
		        Body bodyA = fixtureA.getBody();
		        Body bodyB = fixtureB.getBody();
		        GameActor a =(GameActor) bodyA.getUserData();
		        GameActor b =(GameActor) bodyB.getUserData();
		        if(a != null){
		        	if(a.role == 1){
		        		if(b!= null && b.camp == a.camp){
		        			return false;
		        		}
		        	}
		        }
		        if(b != null ){
		        	if(b.role == 1){
		        		if(a!= null && a.camp == b.camp){
		        			return false;
		        		}
		        	}
		        }
				return true;
			}
		});
        
//        WorldUtils.createBorder(world,menu_width, control_height, game_width+menu_width, height - length);
    }
	
	private void initClock() {
		sAction = new Action[TOTAL_TIME];// 一共执行120次
		// 使用action实现定时器
		for (int i = 0; i < sAction.length; i++) {
			Action delayedAction = Actions.run(new Runnable() {

				@Override
				public void run() {
					currTime--;
					if(currTime <= 0){
						MapData.gamerunning = false;
					}
				}
			});
			// 延迟1s后执行delayedAction
			Action action = Actions.delay(1f, delayedAction);
			sAction[i] = action;
		}
		stage.addAction(Actions.sequence(sAction));
	}
	
	//
	private void initMap() {
		buildings.clear();
		connons.clear();
		if(playerDto == null){
			return;
		}
		int[][] map = playerDto.getMap();
		if(map == null){
			return;
		}
		
		bg = new MapImage(ResUtil.getInstance().get("world/bg.jpg",Texture.class));
		float w = bg.getWidth();
		float h = bg.getHeight();
		for(float x = -w ; x < stage.getWidth(); x += w){//background;
			for(float y = -h * 4 ; y < stage.getHeight() + h * 4 ; y += h){
				bg = new MapImage(ResUtil.getInstance().get("world/bg.jpg",Texture.class));
				bg.setPosition(x, y);
				stage.addActor(bg);
			}
		}
		
		for(int i = 0 ; i < 5; i ++){
			int index = RandomUtil.nextInt(4) + 1;
			int x = RandomUtil.nextInt((int)width);
			int y = RandomUtil.nextInt((int)height);
			Image grass = new MapImage(ResUtil.getInstance().get("world/soil" + index +".png", Texture.class));
			grass.setPosition(x, y);
			stage.addActor(grass);
		}
		
		
		
		for (int i = 0; i < map.length; i++) {
			float n = map.length - 1 - i;
			for (int j = 0; j < map[i].length; j++) {
				int type = map[i][j];
				float x = menu_width + j * length;
				float y = control_height + n * length;
				
				if(i == 0 ){
					int index = RandomUtil.nextInt(5) + 1;
					Image grass = new MapImage(ResUtil.getInstance().get("world/tree" + index +".png", Texture.class));
					grass.setPosition(x, y + length);
					stage.addActor(grass);
				}else if(i == map.length - 1){
//					int index = RandomUtil.nextInt(5) + 1;
//					Image grass = new MapImage(ResUtil.getInstance().get("world/tree" + index +".png", Texture.class));
//					grass.setPosition(x, y - length * 2);
//					stage.addActor(grass);
				}
				
				if(j == 0){
					int index = RandomUtil.nextInt(5) + 1;
					Image grass = new MapImage(ResUtil.getInstance().get("world/tree" + index +".png", Texture.class));
					grass.setPosition(x - length, y);
					stage.addActor(grass);
				}else if(j == map[i].length -1){
					int index = RandomUtil.nextInt(5) + 1;
					Image grass = new MapImage(ResUtil.getInstance().get("world/tree" + index +".png", Texture.class));
					grass.setPosition(x + length, y);
					stage.addActor(grass);
				}
				
				if (type > 0){
					Building block = Building.buildingPool.obtain();
					if(type >= BuildingType.CANNON){
						connons.add(block);
					}
					block.init(world,type, x, y, length,length,null);
					buildings.add(block);
					stage.addActor(block);
				}
			}
		}
	}
	
	private void renderKeepArmys(SpriteBatch batch){
		int i = 0;
		Map<Integer,ArmyDto> myArmys = Player.player.getArmys();
		for(ArmyDto dto : myArmys.values()){
			if(dto.getAmout() == 0){
				continue;
			}
			batch.draw(dto.getRegion(), dto.getRect().x * i, dto.getRect().y, dto.getRect().width,dto.getRect().height);
//			font.setColor(Color.BLUE);
			font.draw(batch, dto.getAmout()+"", dto.getRect().x * i + dto.getRect().width/2, dto.getRect().y + dto.getRect().height/2);
			i ++;
		}
	}
	
	@Override
	public void render(float delta) {
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		Gdx.gl.glClearColor(0, 0, 0, 0);
		if(!MapData.gamerunning){
			stage.addActor(window);
		}
		
		//debug---
//		camera.update();
//		debugRenderer.render(world, camera.combined);
		//debug---
		
		scan();
		stage.draw();
		stage.act(delta);
		
		spriteBatch.begin();
		renderKeepArmys(spriteBatch);
		font.draw(spriteBatch,"count down:" + currTime, 10 ,height);
		spriteBatch.end();
		
		shapeRenderer.setProjectionMatrix(camera.combined);
		checkGameOver();
		
		//box2d
        world.step(TIME_STEP, 6, 2);
        
        world.getBodies(bodies);
        for(int i = 0 ; i < bodies.size ; i++){
        	destoryBody(bodies.get(i));
        }
	}
	
	private void checkGameOver() {
		if(armys.size == 0){
			Map<Integer,ArmyDto> myArmys = Player.player.getArmys();
			for(ArmyDto dto : myArmys.values()){
				if(dto.isGo()){
					continue;
				}
				return;
			}
			MapData.gamerunning = false;
			finishBattle(false);
		}
	}

	private void scan() {
		for(GameActor block : connons){
			block.scan(armys);
		}
		
		for(GameActor army : armys){
			army.scan(buildings);
		}
	}
	

	@Override
    public void beginContact(Contact contact) {
        Fixture fa = contact.getFixtureA();
        Fixture fb = contact.getFixtureB();
        Body bodyA = fa.getBody();
        Body bodyB = fb.getBody();
        GameActor a =(GameActor) bodyA.getUserData();
        GameActor b =(GameActor) bodyB.getUserData();
        if(a == null &&  b== null){
        	return;
        }
        if(a != null && b == null){
           if(a.role == 1){
        	   a.dead();
           }
           return;
        }
        if(a == null && b != null){
        	if(b.role == 1){
        		b.dead();
        	}
        	return;
        }

        if(a.role != b.role){//角色不一样
			if (a.camp != b.camp) {//敌对的
				if (a.role == 1) {
					b.byAttack(a);
				} else {
					a.byAttack(b);
				}
			}
        }
        
        if(a.role == 1 && b.role == 1){
        	return;
        }
        
        if(a.role == 1){
     	   a.dead();
        }
        if(b.role == 1){
    		b.dead();
    	}
    }
	
	
    @Override
    public void endContact(Contact contact) {
    	
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
    
    private Integer hitKeepArmy(float x, float y){
    	Map<Integer,ArmyDto> myArmys = Player.player.getArmys();
    	for(ArmyDto dto : myArmys.values()){
    		if(dto.getAmout() == 0){
    			continue;
    		}
    		if(dto.getRect().contains(x, y)){
    			return dto.getId();
    		}
    	}
    	return null;
    }
    
    public static boolean gobattle;
    
	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		Vector3 vector3 = new Vector3(screenX, screenY, 0);
		camera.unproject(vector3); // coordinate convert
		float x = vector3.x;
		float y = vector3.y;
//		if(x > -length * 2  && x < width + length * 2 
//				&& y > control_height - length * 2  && y < height + length * 2){
//			return false;
//	    }
		if(y > control_height - length * 2){
			return false;
		}
		
		screenY = height - screenY;//y top to down
		Integer type = hitKeepArmy(screenX, screenY);
		if(type != null){
			chooseArmyId = type;	
			return false;
		}
		
		Actor actor = stage.hit(x, y, true);
		if(actor != null){
			return false;
		}
		Map<Integer,ArmyDto> myArmys = Player.player.getArmys();
		for(ArmyDto army : myArmys.values()){
			if(army.isGo()){
				continue;
			}
			if(army.getId() != chooseArmyId){
				continue;
			}
			for(int i = 0 ; i < army.getAmout(); i++){
				Army block = Army.armyPool.obtain();
				block.init(world, ArmyType.valueOf(army.getId()), 10 + x + i * length , 10 + y, length,length,shapeRenderer);
				armys.add(block);
				stage.addActor(block);
			}
//			Point p = new Point(x, y);
//			readytogoArmy.put(p, army);
			
			army.setGo(true);
			gobattle = true;
			
		}
		
		//for the next choose type;
		for(ArmyDto army : myArmys.values()){
			if(army.isGo()){
				continue;
			}
			chooseArmyId = army.getId();
			break;
		}
		
		return true;
	}
	
	class Point{
		public float x;
		public float y;
		public Point (float x, float y) {
			this.x = x;
			this.y = y;
		}
	}
	
	@Override
	public void resize(int width, int height) {
		
	}

	@Override
	public void hide() {
		MapData.gamerunning = false;
		finishBattle(false);
	}

	@Override
	public void pause() {
		System.out.println("gameScreen pause!");
//		TIME_STEP = 0;
	}

	@Override
	public void resume() {
		System.out.println("gameScreen resume!");
//		TIME_STEP = 1 / 50f;
	}

	@Override
	public void dispose() {
		if(!init){
			return;
		}
		init = false;
		if (stage != null){
			stage.dispose();
		}
		
		if(spriteBatch != null){
			spriteBatch.dispose();
		}
		
//		music.dispose();
		
		if(font != null){
			font.dispose();
		}
		
		shapeRenderer.dispose();
//		debugRenderer.dispose();
		
		if(world != null){
			world.dispose();
		}
		bodies.clear();
		
		buildings.clear();
		armys.clear();
		connons.clear();
		Bullet.bulletPool.clear();
		Building.buildingPool.clear();
		
	}


}
