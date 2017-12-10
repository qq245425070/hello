package cn.gietv.mlive.modules.compere.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import cn.gietv.mlive.base.AbsBaseActivity;
import cn.gietv.mlive.modules.compere.fragment.CompereInfoFragment;
import cn.gietv.mlive.utils.IntentUtils;

/**
 * author：steven
 * datetime：15/10/12 13:00
 *
 */
public class CompereActivity extends AbsBaseActivity {
    public static final String EXTRA_USER_ID = "extra_userid";

    public static void openCompereActivity(Context context, String userid) {
        Bundle bundle = new Bundle();
        bundle.putString("extra_userid", userid);
        IntentUtils.openActivity(context, CompereActivity.class, bundle);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(cn.gietv.mlive.R.layout.common_fragment_layout);
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.add(cn.gietv.mlive.R.id.fragment, CompereInfoFragment.getInstence(getIntent().getExtras().getString(EXTRA_USER_ID)));
        ft.commit();
    }

    @Override
    public void finish() {
        super.finish();
    }
}
