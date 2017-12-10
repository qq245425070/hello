package cn.gietv.mlive.base;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Window;

import com.umeng.analytics.MobclickAgent;
import com.umeng.message.PushAgent;

import cn.gietv.mlive.MainApplication;

/**
 * author：steven
 * datetime：15/9/15 15:32
 *
 */
public class AbsBaseActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        PushAgent.getInstance(MainApplication.getInstance()).onAppStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
        MobclickAgent.onPageStart(getClass().getName());
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
        MobclickAgent.onPageEnd(getClass().getName());
    }
    public boolean isNotFinish() {
        return !isFinishing();
    }

}
