package cn.gietv.mlive.modules.welcome.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import java.util.Timer;
import java.util.TimerTask;

import cn.gietv.mlive.R;
import cn.gietv.mlive.base.AbsBaseActivity;
import cn.gietv.mlive.modules.home.activity.HomeActivity;
import cn.gietv.mlive.modules.subscribe.activity.SubscribeActivity;
import cn.gietv.mlive.utils.IntentUtils;
import cn.gietv.mlive.utils.SharedPreferenceUtils;

/**
 * Created by houde on 2016/9/12.
 */
public class SplashActivity extends AbsBaseActivity {
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        ImageView image = new ImageView(this);
        image.setImageResource(R.mipmap.helloworld);
        image.setScaleType(ImageView.ScaleType.CENTER);
        setContentView(image);
    }

    @Override
    protected void onResume() {
        super.onResume();
        final Intent localIntent=new Intent(this,WelcomeActivity.class);

        Timer timer=new Timer();
        TimerTask tast=new TimerTask()
        {
            @Override
            public void run(){
                Log.e("ceshi","0.9.0 = " + SharedPreferenceUtils.getBoolean("0.9.0",true)+"");
                if(SharedPreferenceUtils.getBoolean("0.9.0",true)) {
                    SharedPreferenceUtils.saveProp("0.9.0",false);
                    startActivity(localIntent);
                    finish();
                }else{
                    if(SharedPreferenceUtils.getBoolean("is_first_subscribe",true)) {
                        IntentUtils.openActivity(SplashActivity.this, SubscribeActivity.class);
                        SharedPreferenceUtils.saveProp("is_first_subscribe",false);
                        finish();
                    }else{
                        IntentUtils.openActivity(SplashActivity.this, HomeActivity.class);
                        finish();
                    }
                }
            }
        };
        timer.schedule(tast,1000);
    }
}
