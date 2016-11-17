package com.trance.android;

import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.trance.android.util.GetDeviceId;
import com.trance.android.util.UpdateManager;
import com.trance.empire.modules.player.model.Player;
import com.trance.view.TranceGame;
import com.trance.view.screens.LoginScreen;
import com.trance.view.screens.WorldScreen;
import com.trance.view.utils.SocketUtil;

import java.util.Locale;

import var3d.net.freefont.android.AndroidFreeFont;

public class AndroidLauncher extends AndroidApplication {
	public TranceGame tranceGame;
	private boolean isInit;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		String lang = Locale.getDefault().getLanguage();
		tranceGame = new TranceGame();
		tranceGame.setLang(lang);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		config.useAccelerometer = false;  //禁用加速计
		config.useCompass = false;		  //禁用罗盘
//        config.useGL20 = true;			  //就可以随便任何分辨率图片不必是2的N次方了
		AndroidFreeFont.Strat();//初始化文本
		initialize(tranceGame, config);
		init();
	}


	/**
	 * 初始化
	 */
	private synchronized void init() {
		if(isInit){
			return;
		}

		UpdateManager update = new UpdateManager(this);
		update.checkUpdate();

		GetDeviceId getDeviceId  = new GetDeviceId(this);
		Player.userName = getDeviceId.getCombinedId();

		//

		isInit = true;
	}


	private long time;


	@Override
	public void onBackPressed() {
		Screen screen = tranceGame.getScreen();
		if(screen != null){
			if(screen.getClass() != WorldScreen.class && screen.getClass() != LoginScreen.class){
				Gdx.app.postRunnable(new Runnable() {

					@Override
					public void run() {
						if(tranceGame.mapScreen.dialogArmyStage.isVisible()){
							tranceGame.mapScreen.setArmyDailog(false);
						}else if(tranceGame.mapScreen.dialogBuildingStage.isVisible()){
							tranceGame.mapScreen.setBuildingDailog(false);
						}else if(tranceGame.mapScreen.dialogRankUpStage.isVisible()){
							tranceGame.mapScreen.setRankUpDailog(false);
						}else{
							tranceGame.setScreen(tranceGame.worldScreen);
						}
					}
				});
				return;
			}
		}
		long now = System.currentTimeMillis();
		if(time <= 0 || (now - time) > 2000){
			this.time = now;
			Toast.makeText(this, "再按一次退出游戏", Toast.LENGTH_SHORT)
					.show();
			return;
		}
		super.onBackPressed();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		tranceGame.dispose();
		SocketUtil.destroy();
		Gdx.app.exit();
		System.exit(0);
	}
}
