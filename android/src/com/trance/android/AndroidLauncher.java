package com.trance.android;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.trance.android.font.AndroidFreeFont;
import com.trance.android.util.GetDeviceId;
import com.trance.empire.modules.player.model.Player;
import com.trance.event.BsuEvent;
import com.trance.view.TranceGame;
import com.trance.view.screens.GameScreen;
import com.trance.view.screens.LoginScreen;
import com.trance.view.screens.ReplayScreen;
import com.trance.view.screens.WorldScreen;
import com.trance.view.screens.type.BattleFinishType;
import com.trance.view.utils.MsgUtil;
import com.trance.view.utils.SocketUtil;

import java.lang.ref.WeakReference;
import java.util.Locale;



public class AndroidLauncher extends AndroidApplication {
	public TranceGame tranceGame;
	private boolean isInit;

	// Admob Google test banner
//	private static final String AD_UNITID ="ca-app-pub-3940256099942544/6300978111";

	//Admob 横屏广告ID(自己的)
	private static final String AD_UNITID ="ca-app-pub-5713066340300541/1056902518";

	private static AdView adView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);

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

        initialize(tranceGame);
//        View gameView = initializeForView(tranceGame,config);
//		showAD(gameView);

		init();
	}

	/**
	 * 显示Admob banner
	 */
	private void showAD(final View gameView){
		// Create the layout
		RelativeLayout layout = new RelativeLayout(this);

        // Create a banner ad
		adView = new AdView(this);
		adView.setAdSize(AdSize.BANNER);
		adView.setAdUnitId(AD_UNITID);

		AdRequest adRequest = new AdRequest.Builder().build();

		// Add the libgdx view
		layout.addView(gameView);

		// Add the AdMob view
		RelativeLayout.LayoutParams adParams =
				new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
						RelativeLayout.LayoutParams.WRAP_CONTENT);
		adParams.addRule(RelativeLayout.CENTER_HORIZONTAL | RelativeLayout.ALIGN_PARENT_TOP);
		layout.addView(adView, adParams);

		adView.loadAd(adRequest);
		setContentView(layout);

		adView.setAdListener(new AdListener() {
			@Override
			public void onAdLoaded() {
				adView.setVisibility(View.GONE);
				adView.setVisibility(View.VISIBLE);
			}

            @Override
            public void onAdFailedToLoad(int i) {
                Log.e("trance", "加载admob AD 失败！！！！" + i);
            }
        });
	}

	@Override
	protected void onPause() {
//		adView.pause();
		super.onPause();
	}

	@Override
	protected void onResume() {
		super.onResume();
//		adView.resume();
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

		GetDeviceId getDeviceId  = new GetDeviceId();
		Player.userName =  getDeviceId.getUniquePsuedoID();
//		Player.userName =  "test10";
//		Player.userName =  "ffffffffc61540e7ffffffffa1150133";

		isInit = true;
	}

	private static class MyHandler extends Handler{

		private WeakReference<Context> reference;
		private ProgressDialog dialog;

		public MyHandler(Context context, ProgressDialog dialog){
			this.reference = new WeakReference<>(context);
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
//					adView.setVisibility(View.GONE);
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
			if(screen.getClass() != WorldScreen.class && screen.getClass() !=
                    LoginScreen.class){
				return;
			}
		}
		long now = System.currentTimeMillis();
		if(time <= 0 || (now - time) > 2000){
			this.time = now;
			Toast.makeText(this, MsgUtil.getInstance().getLocalMsg("Press again to exit the game"), Toast.LENGTH_SHORT)
					.show();
			return;
		}
		super.onBackPressed();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
//		adView.destroy();
		tranceGame.dispose();
		SocketUtil.destroy();
		Gdx.app.exit();
		System.exit(0);
	}
}
