package cn.gietv.mlive.modules.vrgame.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import cn.gietv.mlive.R;
import cn.gietv.mlive.base.AbsBaseActivity;
import cn.gietv.mlive.modules.vrgame.fragment.VRGameFragment;

/**
 * 作者：houde on 2016/12/9 17:29
 * 邮箱：yangzhonghao@gietv.com
 */
public class VRAllGameActivity extends AbsBaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vrallgame);
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.fragment,new VRGameFragment()).commitAllowingStateLoss();
    }
}
