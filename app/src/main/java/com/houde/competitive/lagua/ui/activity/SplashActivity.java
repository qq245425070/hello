package com.houde.competitive.lagua.ui.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;

import com.houde.competitive.lagua.R;
import com.zyw.horrarndoo.sdk.utils.StatusBarUtils;


public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        StatusBarUtils.setTransparent(this);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(!isFinishing()){
                    startActivity(new Intent(SplashActivity.this,Main2Activity.class));
                    finish();
                }
            }
        },3000);

    }

}
