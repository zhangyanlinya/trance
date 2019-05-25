

package com.trance.android;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;


public class AdmobActivity extends AppCompatActivity {

    private InterstitialAd mInterstitialAd;

    /**
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

//        UpdateManager update = new UpdateManager(this , new Handler.Callback(){
//            @Override
//            public boolean handleMessage(Message message) {
//                int delay = message.what;
//                new TimeThread(delay).start();
//                return false;
//            }
//        });
//        update.checkUpdate();

        mInterstitialAd = newInterstitialAd();
        loadInterstitial();
        new TimeThread(4).start();

    }


    private InterstitialAd newInterstitialAd() {
        InterstitialAd interstitialAd = new InterstitialAd(this);
//        interstitialAd.setAdUnitId("ca-app-pub-3940256099942544/1033173712");//TEST ID
        interstitialAd.setAdUnitId("ca-app-pub-5713066340300541/3148879311");//
        interstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                if(start){
                    return;
                }
                showInterstitial();
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                startGame();
            }

            @Override
            public void onAdClosed() {
                startGame();
            }
        });
        return interstitialAd;
    }

    private void showInterstitial() {
        if (mInterstitialAd != null && mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        }  //       Toast.makeText(this, "Ad did not load", Toast.LENGTH_SHORT).show();

    }

    private void loadInterstitial() {
        AdRequest adRequest = new AdRequest.Builder()
                .setRequestAgent("android_studio:ad_template").build();
        mInterstitialAd.loadAd(adRequest);
    }

    private boolean start;

    private synchronized void startGame(){
        if(start){
            return;
        }
        start = true;
        Intent intent = new Intent(AdmobActivity.this, AndroidLauncher.class);
        startActivity(intent);
        AdmobActivity.this.finish();
    }

    @SuppressLint("HandlerLeak")
    public Handler handler = new Handler(){

        public void handleMessage(android.os.Message msg) {
            int count = msg.what;
            Toast.makeText(AdmobActivity.this, count + "s", Toast.LENGTH_SHORT).show();
            if(count < 1){
                startGame();
            }
        }
    };

    class TimeThread extends Thread{
        private int delay;
        TimeThread(int delay){
            this.delay = delay;
        }
        public void run(){
            do {
                try {
                    handler.sendEmptyMessage(delay);
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                delay--;
            } while (delay >= 0);
        }
    }
}


