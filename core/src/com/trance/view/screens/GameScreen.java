package com.trance.view.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
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
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.trance.common.socket.model.Request;
import com.trance.common.socket.model.Response;
import com.trance.common.socket.model.ResponseStatus;
import com.trance.common.util.ProtostuffUtil;
import com.trance.empire.config.Module;
import com.trance.empire.model.Result;
import com.trance.empire.modules.army.model.ArmyDto;
import com.trance.empire.modules.army.model.ArmyType;
import com.trance.empire.modules.army.model.ArmyVo;
import com.trance.empire.modules.battle.handler.BattleCmd;
import com.trance.empire.modules.battle.model.ReqFinishBattle;
import com.trance.empire.modules.building.model.BuildingDto;
import com.trance.empire.modules.building.model.BuildingType;
import com.trance.empire.modules.player.handler.PlayerCmd;
import com.trance.empire.modules.player.model.Player;
import com.trance.empire.modules.player.model.PlayerDto;
import com.trance.empire.modules.player.model.ReqUp;
import com.trance.empire.modules.replay.model.Click;
import com.trance.empire.modules.reward.result.ValueResultSet;
import com.trance.empire.modules.reward.service.RewardService;
import com.trance.empire.modules.tech.model.TechDto;
import com.trance.view.TranceGame;
import com.trance.view.actors.Army;
import com.trance.view.actors.Building;
import com.trance.view.actors.Bullet;
import com.trance.view.actors.Explode;
import com.trance.view.actors.GameActor;
import com.trance.view.actors.MapImage;
import com.trance.view.constant.ControlType;
import com.trance.view.constant.ExplodeType;
import com.trance.view.freefont.FreeBitmapFont;
import com.trance.view.mapdata.MapData;
import com.trance.view.particle.ParticleService;
import com.trance.view.screens.base.BaseScreen;
import com.trance.view.screens.type.BattleFinishType;
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
import java.util.Timer;
import java.util.TimerTask;


public class GameScreen extends BaseScreen implements ContactListener, InputProcessor {

    private Window window;
    private Batch spriteBatch;
    public static PlayerDto playerDto;
    private Stage stage;
    private FreeBitmapFont font;
    private float width;
    private float height;

    /**
     * 数组宽数量
     */
    private final static int ARR_WIDTH_SIZE = 16;
    /**
     * 数组高数量
     */
    private final static int ARR_HEIGHT_SIZE = 20;

    /**
     * 中间游戏区域的百分比
     */
    private static double percent = 0.8;
    /**
     * 每格的边长
     */
    private static float length = 32;
    /**
     * 游戏区域宽
     */
    private static float game_width = 512;
    /**
     * 游戏区域高
     */
    private static float game_height = 832;
    /**
     * 菜单区域宽度
     */
    private static float menu_width = 208;
    /**
     * 控制区域高度
     */
    private static float control_height = 368;


    private World world;
    private ShapeRenderer shapeRenderer;
    private final float TIME_STEP = 1 / 50f;


//    private Box2DDebugRenderer debugRenderer;

    public final static Array<GameActor> buildings = new Array<GameActor>();

    private final static Array<GameActor> armys = new Array<GameActor>();

    private final static Array<GameActor> connons = new Array<GameActor>();

    private final Array<Body> bodies = new Array<Body>();

    private OrthographicCamera camera;
    private Image bg;

    /**
     * 一局所用总时间
     */
    private final static int TOTAL_TIME = 2 * 60;

    /**
     * 当前时间
     */
    private int currTime = TOTAL_TIME;
    private boolean init;

    private static boolean finishBattle;

    private static long startTime;

    private InputMultiplexer inputMultiplexer;
//	private GestureDetector gestureHandler;

    private static List<Click> clicks = new ArrayList<Click>();

    public GameScreen(TranceGame tranceGame) {
        super(tranceGame);
    }

    @Override
    public void show() {
        if (!init) {
            init();
            init = true;
        }
        MapData.gamerunning = true;
        finishBattle = false;
        gobattle = false;
        chooseTechId = 0;
        camera.position.set(width / 2, height / 2, 0);
        currTime = TOTAL_TIME;//初始化时间
        stage.clear();
        initClock();
        initWorld();
        initMap();
        initArmy();
        clicks.clear();
        inputMultiplexer = new InputMultiplexer();
//		GestureController controller = new GestureController(camera, 0, width * 2, 0, height * 2);
//		gestureHandler = new GestureDetector(controller);
        initInputProcessor();

        startTime = System.currentTimeMillis();
    }

    private void initInputProcessor() {
//		inputMultiplexer.addProcessor(gestureHandler);
        inputMultiplexer.addProcessor(stage);
        inputMultiplexer.addProcessor(this);
        Gdx.input.setInputProcessor(inputMultiplexer);
    }


    private void init() {
        spriteBatch = new SpriteBatch();
        width = Gdx.graphics.getWidth(); // 720
        height = Gdx.graphics.getHeight(); // 1200
        font = FontUtil.getFont();
        font.appendText(MsgUtil.getInstance().getLocalMsg("laud"));
        font.appendText(MsgUtil.getInstance().getLocalMsg("Click on the green area to send soldiers or other side"));
        stage = new Stage(new StretchViewport(width * 2, height * 2));

        CELL_LENGHT = width / 10;
        camera = new OrthographicCamera();
        camera.setToOrtho(false, width, height);
        spriteBatch.setProjectionMatrix(camera.combined);
//		debugRenderer = new Box2DDebugRenderer();
        stage.getViewport().setCamera(camera);

        length = (int) (width * percent / ARR_WIDTH_SIZE);
        game_width = length * ARR_WIDTH_SIZE;
        game_height = length * ARR_HEIGHT_SIZE;
        menu_width = (width - game_width) / 2;
        control_height = height - game_height - length * 2;//再减少2格
        shapeRenderer = new ShapeRenderer();

        //返回家
        Image toWorld = new Image(ResUtil.getInstance().getControlTextureRegion(ControlType.WORLD));
        toWorld.setBounds(10, 10, toWorld.getWidth() + toWorld.getWidth() / 2, toWorld.getHeight() + toWorld.getHeight() / 2);
        toWorld.addListener(new ClickListener() {

            @Override
            public void clicked(InputEvent event, float x, float y) {
                tranceGame.setScreen(tranceGame.worldScreen);
            }
        });

        //提示框
        TextureRegionDrawable tips = new TextureRegionDrawable(new TextureRegion(
                ResUtil.getInstance().get("world/tips.png", Texture.class)));
        Drawable background = new TextureRegionDrawable(tips);
        WindowStyle style = new WindowStyle(font, Color.MAGENTA, background);
        window = new Window(MsgUtil.getInstance().getLocalMsg("laud"), style);
        window.setPosition(width / 2 - window.getWidth() / 2, height / 2 - window.getHeight() / 2);
        window.addListener(new ClickListener() {

            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (playerDto != null) {
                    ReqUp req = new ReqUp();
                    req.setTargetId(playerDto.getId());
                    SocketUtil.sendAsync(Request.valueOf(Module.PLAYER, PlayerCmd.UP, req));
                }
            }
        });
        world = WorldUtils.createWorld();
    }

    private static float CELL_LENGHT;
    private int chooseArmyId;
    private int chooseTechId;

    private void initArmy() {
        armys.clear();
        Map<Integer, ArmyDto> amrys_map = Player.player.getArmys();
        if (amrys_map == null || amrys_map.isEmpty()) {
            return;
        }

        int i = 0;
        for (ArmyDto dto : amrys_map.values()) {
            if (dto.getAmout() == 0) {
                continue;
            }
            dto.setGo(false);

            if (chooseArmyId == 0) {
                chooseArmyId = dto.getId();
            }
            dto.setGo(false);
            dto.setRegion(ResUtil.getInstance().getArmyTextureRegion(dto.getId()));
            Rectangle rect = new Rectangle(i * CELL_LENGHT, 0, CELL_LENGHT, CELL_LENGHT);
            dto.setRect(rect);
            i++;
        }

        int j = 0;
        for (TechDto techDto : Player.player.getTechs().values()) {
            if (chooseTechId == 0) {
                chooseTechId = techDto.getId();
            }
            techDto.resetAmount();
            techDto.setRegion(ResUtil.getInstance().getExplodeTextureRegion(techDto.getId()));
            Rectangle rect = new Rectangle(j * CELL_LENGHT, CELL_LENGHT, CELL_LENGHT, CELL_LENGHT);
            techDto.setRect(rect);
            j++;
        }
    }

    public static synchronized void finishBattle(BattleFinishType finishType) {
        MapData.gamerunning = false;
        if (finishBattle) {
            return;
        }
        
        if(!gobattle){
        	finishType = BattleFinishType.CANCEL;
        }
        
        Sound sound = ResUtil.getInstance().getSound(5);
        sound.play();
        
        ReqFinishBattle req = new ReqFinishBattle();
        req.setState(finishType.ordinal());
        req.setX(playerDto.getX());
        req.setY(playerDto.getY());
        if (finishType == BattleFinishType.CANCEL) {
        	Request request = Request.valueOf(Module.BATTLE, BattleCmd.FINISH_BATTLE, req);
            SocketUtil.send(request, true);
            return;
        }

        Click click =  new Click();
        click.setT((int) (System.currentTimeMillis() - startTime));
        click.setX(-1);
        click.setY(-1);
        clicks.add(click); //结束标识

        Map<Integer, ArmyDto> myArmys = Player.player.getArmys();
        for (GameActor actor : armys) {
            Army army = (Army) actor;
            int type = army.armyType.id;
            ArmyDto a = myArmys.get(type);
            if (a != null) {
                a.setAmout(a.getAmout() + 1);
            }
        }

        //转换
        List<ArmyVo> vos = new ArrayList<ArmyVo>();
        for (ArmyDto armyDto : myArmys.values()) {
            vos.add(ArmyVo.valueOf(armyDto));
        }

        finishBattle = true;
        req.setArmys(vos);
        req.setClicks(clicks);
        Request request = Request.valueOf(Module.BATTLE, BattleCmd.FINISH_BATTLE, req);
        Response response = SocketUtil.send(request, true);
        if (response == null) {
            return;
        }

        ResponseStatus status = response.getStatus();
        if (status != ResponseStatus.SUCCESS) {
            return;
        }

        byte[] bytes = response.getValueBytes();
        Result<ValueResultSet> result = ProtostuffUtil.parseObject(bytes, Result.class);
        int code = result.getCode();
        if (code != Result.SUCCESS) {
            MsgUtil.getInstance().showMsg(Module.BATTLE, code);
            return;
        }

        ValueResultSet valueResultSet= result.getContent();
        if (valueResultSet != null) {
            RewardService.executeRewards(valueResultSet);
        }

        //refresh world
        if (finishType == BattleFinishType.WIN) {
            WorldScreen.remove(playerDto.getX(), playerDto.getY());
        }
    }

    //DestoryBody
    private void destoryBody(Body body) {
        GameActor ga = (GameActor) body.getUserData();
        if (ga == null) {
            return;
        }
        if (!ga.alive) {
            world.destroyBody(body);
        }
    }

    private void initWorld() {
        world.clearForces();
        world.getBodies(bodies);
        for (int i = 0; i < bodies.size; i++) {
            world.destroyBody(bodies.get(i));
        }
        bodies.clear();

        world.setContactListener(this);
        world.setContactFilter(new ContactFilter() {

            @Override
            public boolean shouldCollide(Fixture fixtureA, Fixture fixtureB) {
                Body bodyA = fixtureA.getBody();
                Body bodyB = fixtureB.getBody();
                GameActor a = (GameActor) bodyA.getUserData();
                GameActor b = (GameActor) bodyB.getUserData();
                if (a != null) {
                    if (a.role == 1) {
                        if (b != null && b.camp == a.camp) {
                            return false;
                        }
                    }
                }
                if (b != null) {
                    if (b.role == 1) {
                        if (a != null && a.camp == b.camp) {
                            return false;
                        }
                    }
                }
                return true;
            }
        });

        WorldUtils.createBorder(world, menu_width, control_height, game_width + menu_width, height - length);
    }

    private void initClock() {
        final Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            public void run() {
                currTime--;
                if (currTime <= 0) {
                    timer.cancel();
                    finishBattle(BattleFinishType.TIMEOUT);
                }
            }
        }, 0 , 1000);

//        Action[] sAction = new Action[TOTAL_TIME];// 一共执行120次
//        // 使用action实现定时器
//        for (int i = 0; i < sAction.length; i++) {
//            Action delayedAction = Actions.run(new Runnable() {
//
//                @Override
//                public void run() {
//                    currTime--;
//                    if (currTime <= 0) {
//                        finishBattle(BattleFinishType.TIMEOUT);
//                    }
//                }
//            });
//            // 延迟1s后执行delayedAction
//            Action action = Actions.delay(1f, delayedAction);
//            sAction[i] = action;
//        }
//        stage.addAction(Actions.sequence(sAction));
    }

    //
    private void initMap() {
        buildings.clear();
        connons.clear();
        if (playerDto == null) {
            return;
        }
        int[][] map = playerDto.getMap();
        if (map == null) {
            return;
        }

        bg = new MapImage(ResUtil.getInstance().get("world/bg.jpg", Texture.class));
        float w = bg.getWidth();
        float h = bg.getHeight();
        for (float x = -w; x < stage.getWidth(); x += w) {//background;
            for (float y = -h * 4; y < stage.getHeight() + h * 4; y += h) {
                bg = new MapImage(ResUtil.getInstance().get("world/bg.jpg", Texture.class));
                bg.setPosition(x, y);
                stage.addActor(bg);
            }
        }

        for (int i = 0; i < 5; i++) {
            int index = RandomUtil.nextInt(4) + 1;
            int x = RandomUtil.nextInt((int) width);
            int y = RandomUtil.nextInt((int) height);
            Image grass = new MapImage(ResUtil.getInstance().get("world/soil" + index + ".png", Texture.class));
            grass.setPosition(x, y);
            stage.addActor(grass);
        }


        for (int i = 0; i < map.length; i++) {
            float n = map.length - 1 - i;
            for (int j = 0; j < map[i].length; j++) {
//				int type = map[i][j];
                float x = menu_width + j * length;
                float y = control_height + n * length;

                if (i == 0) {
//					int index = RandomUtil.nextInt(5) + 1;
//					Image grass = new MapImage(ResUtil.getInstance().get("world/tree" + index +".png", Texture.class));
                    Image grass = new MapImage(ResUtil.getInstance().get("world/wall.png", Texture.class));
                    grass.setPosition(x, y + length);
                    stage.addActor(grass);
                } else if (i == map.length - 1) {
                    if (x < length * 4 || x > width - length * 5) { //形成一个口
//						int index = RandomUtil.nextInt(5) + 1;
//						Image grass = new MapImage(ResUtil.getInstance().get("world/tree" + index +".png", Texture.class));
                        Image grass = new MapImage(ResUtil.getInstance().get("world/wall.png", Texture.class));
                        grass.setPosition(x, y - length * 2);
                        stage.addActor(grass);
                    }
                }

                if (j == 0) {
//					int index = RandomUtil.nextInt(5) + 1;
//					Image grass = new MapImage(ResUtil.getInstance().get("world/tree" + index +".png", Texture.class));
                    Image grass = new MapImage(ResUtil.getInstance().get("world/wall.png", Texture.class));
                    grass.setPosition(x - length, y);
                    stage.addActor(grass);
                } else if (j == map[i].length - 1) {
//					int index = RandomUtil.nextInt(5) + 1;
//					Image grass = new MapImage(ResUtil.getInstance().get("world/tree" + index +".png", Texture.class));
                    Image grass = new MapImage(ResUtil.getInstance().get("world/wall.png", Texture.class));
                    grass.setPosition(x + length, y);
                    stage.addActor(grass);
                }

            }
        }

        // add building
        for (BuildingDto dto : playerDto.getBuildings().values()) {
            int i = dto.getX();
            int j = dto.getY();
            Building building = new Building();
//            int n = map.length - 1 - i;
//            float px = menu_width + j * length;
//            float py = control_height + n * length;
            float px = menu_width + j * length;
            float py = control_height + i * length;

            building.setIndex(i, j);
            building.init(world, dto.getMid(), px, py, length, length, shapeRenderer, dto);
            building.setInScreenType(1);

            if (dto.getMid() >= BuildingType.CANNON.getId() && dto.getMid() <= BuildingType.MORTAR.getId()) {
                connons.add(building);
            }

            buildings.add(building);
            stage.addActor(building);
        }
    }

    private void renderKeeps(Batch batch) {
        Map<Integer, ArmyDto> myArmys = Player.player.getArmys();
        for (ArmyDto dto : myArmys.values()) {
            if (dto.getAmout() == 0) {
                continue;
            }
            if (dto.getId() == chooseArmyId) {
                batch.setColor(Color.RED);
            } else {
                batch.setColor(Color.WHITE);
            }
            batch.draw(dto.getRegion(), dto.getRect().x, dto.getRect().y, dto.getRect().width, dto.getRect().height);
            font.draw(batch, dto.getAmout() + "", dto.getRect().x + dto.getRect().width / 2, dto.getRect().y + dto.getRect().height / 2);
        }

        for (TechDto dto : Player.player.getTechs().values()) {
            if (dto.getId() == chooseTechId) {
                batch.setColor(Color.RED);
            } else {
                batch.setColor(Color.WHITE);
            }
            batch.draw(dto.getRegion(), dto.getRect().x, dto.getRect().y, dto.getRect().width, dto.getRect().height);
            font.draw(batch, dto.getUseAmount() + "", dto.getRect().x + dto.getRect().width / 2, dto.getRect().y + dto.getRect().height / 2);
        }
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        Gdx.gl.glClearColor(0, 0, 0, 0);
        if (!MapData.gamerunning) {
            stage.addActor(window);
        }

        //debug---
//		debugRenderer.render(world, camera.combined);
        //debug---

        scan();
        stage.draw();
        stage.act(delta);

        spriteBatch.begin();
        renderKeeps(spriteBatch);
        font.draw(spriteBatch, "count down:" + currTime, 10, height);
        font.draw(spriteBatch, MsgUtil.getInstance().getLocalMsg("Click on the green area to send soldiers or other side"), 24, control_height - length * 2 - 10);
        spriteBatch.end();

        shapeRenderer.setProjectionMatrix(camera.combined);

        shapeRenderer.setColor(Color.GREEN);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.rect(length * 4, length * 2, width - length * 8, control_height - length * 4);
//		shapeRenderer.rect(20, height + length * 2, width - 40, control_height - length * 4);

        shapeRenderer.end();

        checkArmyStatus(delta);

        //box2d
        world.step(TIME_STEP, 6, 2);

        world.getBodies(bodies);
        for (int i = 0; i < bodies.size; i++) {
            destoryBody(bodies.get(i));
        }
        
        if (Gdx.input.isKeyPressed(Input.Keys.BACK)){
            onBackPressed();
        }

        super.render(delta);
    }
    
    public void onBackPressed(){
    	if (MapData.gamerunning) {
    		finishBattle(BattleFinishType.LOSE);
    		return;
    	} 
    	dispose();
    	tranceGame.setScreen(tranceGame.worldScreen);
    }
    
    private float sendDelta = 0;

    private void checkArmyStatus(float delta) {
        if (armys.size == 0) { // 是否已经派完
            Map<Integer, ArmyDto> myArmys = Player.player.getArmys();
            for (ArmyDto dto : myArmys.values()) {
                if (dto.getAmout() > 0) {
                    if (!dto.isGo()) {
                        return;
                    }
                }
            }
            finishBattle(BattleFinishType.LOSE);
        } else {//监听是否有要派出
            for (GameActor ga : armys) {
                Army a = (Army) ga;
                if (a.isSend()) {
                    continue;
                }

                float speed = 5 - a.getSpeed();//5可能是最大速度
                if (sendDelta == 0 || sendDelta > speed) { //
                    stage.addActor(a);
                    a.setSend(true);
                    sendDelta = 0;
                }
                sendDelta += delta;
            }
        }
    }

    private void scan() {
        for (GameActor connon : connons) {
            connon.scan(armys);
        }

        for (GameActor army : armys) {
            army.scan(buildings);
        }
    }


    @Override
    public void beginContact(Contact contact) {
        Fixture fa = contact.getFixtureA();
        Fixture fb = contact.getFixtureB();
        Body bodyA = fa.getBody();
        Body bodyB = fb.getBody();
        GameActor a = (GameActor) bodyA.getUserData();
        GameActor b = (GameActor) bodyB.getUserData();
        if (a == null && b == null) {
            return;
        }
        if (a != null && b == null) {
            if (a.role == 1) {
                a.dead();
            }
            return;
        }
        if (a == null) {
            if (b.role == 1) {
                b.dead();
            }
            return;
        }

        if (a.role != b.role) {//角色不一样
            if (a.camp != b.camp) {//敌对的
                if (a.role == 1) {
                    b.byAttack(a);
                } else {
                    a.byAttack(b);
                }
            }
        }

        if (a.role == 1 && b.role == 1) {
            return;
        }

        if (a.role == 1) {
            a.dead();
        }
        if (b.role == 1) {
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

    private Integer hitKeepArmy(float x, float y) {
        Map<Integer, ArmyDto> myArmys = Player.player.getArmys();
        for (ArmyDto dto : myArmys.values()) {
            if (dto.getAmout() == 0) {
                continue;
            }
            if (dto.getRect().contains(x, y)) {
                return dto.getId();
            }
        }

        return null;
    }

    private Integer hitKeepTech(float x, float y) {
        for (TechDto dto : Player.player.getTechs().values()) {
            if (dto.getUseAmount() == 0) {
                continue;
            }
            if (dto.getRect().contains(x, y)) {
                return dto.getId();
            }
        }

        return null;
    }

    private static boolean gobattle;

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        boolean validClick; //是否有效点击
        int Y = (int) height - screenY; //y top to down
        if (Y < (width / 10) * 2) {
            validClick = chooseArea(screenX, Y); //选择区域
        } else {
            validClick = executeArea(screenX, screenY); //执行区域
        }
        if (validClick) {
            Click click = new Click();
            click.setT((int) (System.currentTimeMillis() - startTime));
            click.setX(screenX);
            click.setY(screenY);
            clicks.add(click);
        }

        return false;
    }

    /**
     * 选择区域事件
     */
    private boolean chooseArea(int screenX, int screenY) {
        boolean validClick = false; //是否有效点击
        Integer armyType = hitKeepArmy(screenX, screenY);
        if (armyType != null) {
            chooseArmyId = armyType;
            validClick = true;
        }

        Integer techType = hitKeepTech(screenX, screenY);
        if (techType != null) {
            chooseTechId = techType;
            validClick = true;
        }

        return validClick;
    }

    /**
     * 执行区域事件
     */
    private boolean executeArea(int screenX, int screenY) {
        boolean validClick = false; //是否有效点击
        Vector3 vector3 = new Vector3(screenX, screenY, 0);
        camera.unproject(vector3); // coordinate convert
        float x = vector3.x;
        float y = vector3.y;
//		if(x > -length * 2  && x < width + length * 2
//				&& y > control_height - length * 2  && y < height + length * 2){//四面可以进攻
//
//			if(chooseTechId > 0) {
//				TechDto tech = Player.player.getTechs().get(chooseTechId);
//				if(tech != null && tech.getUseAmount() > 0) {
//					sendExplode(x, y, tech);
//				}
//			}
//			return false;
//	    }

        if (y > control_height - length * 2 || x < length * 4 || x > width - length * 4) { // 只有下面一个区域可能进攻
            if (chooseTechId > 0) {
                validClick = sendTechEffect(x, y);
            }
            return validClick;
        }


        Actor actor = stage.hit(x, y, true);
        if (actor != null) {
            return false;
        }
        Map<Integer, ArmyDto> myArmys = Player.player.getArmys();
        for (ArmyDto army : myArmys.values()) {
            if (army.isGo() || army.getAmout() == 0) {
                continue;
            }

            if (army.getId() != chooseArmyId) {
                continue;
            }

            for (int i = 0; i < army.getAmout(); i++) {
                Army block = Army.armyPool.obtain();
                block.init(world, ArmyType.valueOf(army.getId()), x, y, length, length, shapeRenderer);
                armys.add(block);
            }
            army.setAmout(0);
            army.setGo(true);
            gobattle = true;
            validClick = true;
        }

        //for the next choose type;
        for (ArmyDto army : myArmys.values()) {
            if (army.isGo() || army.getAmout() == 0) {
                continue;
            }
            chooseArmyId = army.getId();
            break;
        }

        return validClick;
    }

    /**
     * 执行科技效果s
     */
    private boolean sendTechEffect(float x, float y) {
        TechDto tech = Player.player.getTechs().get(chooseTechId);
        if (tech == null || tech.getUseAmount() <= 0) {
            return false;
        }

        if (tech.getId() == 1) {
            sendExplode(x, y, tech);
            return true;
        } else if (tech.getId() == 2) {
            sendLamp(x, y, tech);
            return true;
        }
        return false;
    }

    /**
     * 执行EXPLODE
     */
    private void sendExplode(float x, float y, TechDto tech) {
        Explode explode = Explode.pool.obtain();
        explode.init(world, ExplodeType.valueOf(tech.getId()), x, y);
        stage.addActor(explode);
        tech.setUseAmount(tech.getUseAmount() - 1);
        gobattle = true;
    }


    /**
     * 执行LAMP
     */
    private void sendLamp(float x, float y, TechDto tech) {
        Explode explode = Explode.pool.obtain();
        explode.init(world, ExplodeType.valueOf(tech.getId()), x, y);
        stage.addActor(explode);
        for (GameActor army : armys) {
            army.lampExpireTime = System.currentTimeMillis() + tech.getLevel() * 1000;
            army.moveTo(x, y);

        }
        tech.setUseAmount(tech.getUseAmount() - 1);
        gobattle = true;
    }

    public boolean keyUp(int keycode) {
        return false;
    }

    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    public boolean scrolled(int amount) {
        return false;
    }

    public boolean keyDown(int keycode) {
        return false;
    }

    public boolean keyTyped(char character) {
        return false;
    }

    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public void dispose() {
        if (!init) {
            return;
        }
        init = false;
        stage.dispose();

        if (spriteBatch != null) {
            spriteBatch.dispose();
        }

        shapeRenderer.dispose();
//		debugRenderer.dispose();

        if (world != null) {
            world.dispose();
        }
        bodies.clear();

        buildings.clear();
        armys.clear();
        connons.clear();
        Bullet.bulletPool.clear();
    }
}
