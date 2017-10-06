

package com.trance.android;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.trance.android.util.UpdateManager;


public class AdmobActivity extends AppCompatActivity {

    private InterstitialAd mInterstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        UpdateManager update = new UpdateManager(this , new Handler.Callback(){
            @Override
            public boolean handleMessage(Message message) {
//                int delay = message.what;
//                new TimeThread(delay).start();
                return false;
            }
        });
        update.checkUpdate();

        mInterstitialAd = newInterstitialAd();
        loadInterstitial();

        // 5秒后跳转
        new TimeThread(6).start();
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
        } else {
           Toast.makeText(this, "Ad did not load", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadInterstitial() {
        mInterstitialAd.loadAd(new AdRequest.Builder().build());
    }

    private boolean start;

    private synchronized void startGame(){
        if(start){
            return;
        }
        start = true;
        Intent intent = new Intent(AdmobActivity.this, AndroidLauncher.class);
        startActivity(intent);
        this.finish();

    }

    public Handler handler = new Handler(){

        public void handleMessage(android.os.Message msg) {
            int count = msg.what;
            Toast.makeText(AdmobActivity.this, count + "s", Toast.LENGTH_SHORT).show();
            if(count < 1){
                startGame();
            }
        }
    };

   private class TimeThread extends Thread{
        private int delay;
        private TimeThread(int delay){
            this.delay = delay;
        }
        public void run(){
            while(true){
                try {
                    handler.sendEmptyMessage(delay);
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                delay--;
                if(delay < 0){
                    break;
                }
            }
        }
    }
}


