package com.trance.view;

import com.badlogic.gdx.Game;
import com.trance.common.basedb.BasedbService;
import com.trance.empire.modules.player.model.Player;
import com.trance.event.BsuEvent;
import com.trance.view.screens.GameScreen;
import com.trance.view.screens.LoginScreen;
import com.trance.view.screens.MapScreen;
import com.trance.view.screens.WorldScreen;
import com.trance.view.screens.base.BaseScreen;
import com.trance.view.utils.FontUtil;
import com.trance.view.utils.MsgUtil;
import com.trance.view.utils.ResUtil;
import com.trance.view.utils.SocketUtil;

public class TranceGame extends Game {
	public BaseScreen loginScreen;//
	public WorldScreen worldScreen;//
	public MapScreen mapScreen;    //
	public GameScreen gameScreen;  //
	private String lang;

	private BsuEvent event;
	public TranceGame(BsuEvent event, String lang){
		this.event = event;
		this.lang = lang;
	}

	@Override
	public void create() {
		MsgUtil.getInstance().init(this, lang);
		new Thread(){
			public void run(){
				SocketUtil.init();
			}
		}.start();
		BasedbService.init();

		loginScreen = new LoginScreen(this);
		worldScreen = new WorldScreen(this);
		mapScreen = new MapScreen(this);
		gameScreen = new GameScreen(this);
		this.setScreen(loginScreen);
	}

	/**
	 * start
	 */
	public void startGame(){
		mapScreen.setPlayerDto(Player.player);
		this.setScreen(mapScreen);
	}

	public void showMsg(String msg){
		((BaseScreen)this.getScreen()).showMsg(msg);
	}


	@Override
	public void dispose() {
		loginScreen.dispose();
		worldScreen.dispose();
		mapScreen.dispose();
		gameScreen.dispose();
		ResUtil.getInstance().dispose();
		super.dispose();
		FontUtil.dispose();
	}

	public BsuEvent getEvent() {
		return event;
	}

	public void setEvent(BsuEvent event) {
		this.event = event;
	}
}
