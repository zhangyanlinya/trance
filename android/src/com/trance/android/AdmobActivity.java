

package com.trance.android;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.trance.android.util.UpdateManager;


public class AdmobActivity extends AppCompatActivity {

    private InterstitialAd mInterstitialAd;

    /**
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        UpdateManager update = new UpdateManager(this , new Handler.Callback(){
            @Override
            public boolean handleMessage(Message message) {
                int delay = message.what;
                new TimeThread(delay).start();
                return false;
            }
        });
        update.checkUpdate();

        mInterstitialAd = newInterstitialAd();
        loadInterstitial();

    }


    private InterstitialAd newInterstitialAd() {
        InterstitialAd interstitialAd = new InterstitialAd(this);
        interstitialAd.setAdUnitId("ca-app-pub-3940256099942544/1033173712");
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
     //       Toast.makeText(this, "Ad did not load", Toast.LENGTH_SHORT).show();
        }
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

    public Handler handler = new Handler(){

        public void handleMessage(android.os.Message msg) {
            int count = msg.what;
            if(count < 1){
                startGame();
            }
        }
    };

    class TimeThread extends Thread{
        private int delay;
        public TimeThread(int delay){
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
                if(delay <= 1){
                    break;
                }
            }
        }
    }
}


