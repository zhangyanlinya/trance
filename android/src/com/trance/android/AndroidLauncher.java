package com.trance.android;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.trance.android.font.AndroidFreeFont;
import com.trance.android.util.GetDeviceId;
import com.trance.empire.modules.player.model.Player;
import com.trance.event.BsuEvent;
import com.trance.view.TranceGame;
import com.trance.view.screens.LoginScreen;
import com.trance.view.screens.WorldScreen;
import com.trance.view.screens.base.BaseScreen;
import com.trance.view.utils.SocketUtil;

import java.lang.ref.WeakReference;
import java.util.Locale;



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
		ProgressDialog dialog = new ProgressDialog(this);
		dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);// 设置水平进度条
		dialog.setCanceledOnTouchOutside(false);// 设置在点击Dialog外是否取消Dialog进度条
		dialog.setCancelable(false);
		dialog.setIndeterminate(true);
		dialog.setMessage("Loading...");
		WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
		lp.alpha = 0.8f;

		handler = new MyHandler(this,dialog);

		tranceGame = new TranceGame( new BsuEvent(){
			@Override
			public void notify(int what, Object obj) {
				Message msg = Message.obtain();
				msg.what = what;
				handler.sendMessage(msg);
			}
		}, lang);

		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		config.useAccelerometer = false;  //禁用加速计
		config.useCompass = false;		  //禁用罗盘
//        config.useGL20 = true;			  //就可以随便任何分辨率图片不必是2的N次方了
		AndroidFreeFont.Strat();//初始化文本
		initialize(tranceGame, config);
		init();
	}

		public static Handler handler;

	/**
	 * 初始化
	 */
	private synchronized void init() {
		if(isInit){
			return;
		}

//		UpdateManager update = new UpdateManager(this);
//		update.checkUpdate();

		GetDeviceId getDeviceId  = new GetDeviceId(this);
		Player.userName = getDeviceId.getCombinedId();

		//

		isInit = true;
	}

	static class MyHandler extends Handler{

		private WeakReference<Context> reference;
		private ProgressDialog dialog;

		public MyHandler(Context context, ProgressDialog dialog){
			this.reference = new WeakReference<Context>(context);
			this.dialog = dialog;
		}

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
				case 1:
					dialog.show();
					break;
				case 2:
					dialog.dismiss();
					break;
				default:
					Toast.makeText(reference.get(), msg.obj+"",
							Toast.LENGTH_SHORT).show();
					break;
			}
		}
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
						if(((BaseScreen)tranceGame.getScreen()).showloading){
							return;
						}
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
