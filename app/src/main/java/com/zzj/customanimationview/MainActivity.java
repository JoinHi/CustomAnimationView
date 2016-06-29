package com.zzj.customanimationview;

import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.SeekBar;

public class MainActivity extends AppCompatActivity {

    private CustomProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//        setContentView(R.layout.bezier_layout);
//        setContentView(R.layout.wavecircle_layout);
        setContentView(R.layout.progress_layout);
        progressBar = (CustomProgressBar) findViewById(R.id.progress);
    }


    int progress = 0;
    public void start(View view){
        progressBar.start();
        progress = 0;
        new Thread(new Runnable() {
            @Override
            public void run() {
                SystemClock.sleep(1000);
                while (progress<20){
                    SystemClock.sleep(100);
                    progress++;
                    progressBar.setProgressCount(progress);
                }
                SystemClock.sleep(1000);
                while (progress<40){
                    SystemClock.sleep(50);
                    progress++;
                    progressBar.setProgressCount(progress);
                }
                SystemClock.sleep(1000);
                while (progress<50){
                    SystemClock.sleep(100);
                    progress++;
                    progressBar.setProgressCount(progress);
                }
                SystemClock.sleep(1000);
                progressBar.loadingFail();
            }
        }).start();
    }
}
