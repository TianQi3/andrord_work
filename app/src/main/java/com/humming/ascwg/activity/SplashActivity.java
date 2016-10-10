package com.humming.ascwg.activity;

import android.content.Intent;
import android.os.Bundle;

import com.humming.ascwg.Application;
import com.humming.ascwg.R;

/**
 * Created by Zhtq on 2016/8/17.
 * 登陆
 */
public class SplashActivity extends AbstractActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                    Intent intent = new Intent(Application.getInstance().getCurrentActivity(),LoginActivity.class);
                    startActivity(intent);
                    finish();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
