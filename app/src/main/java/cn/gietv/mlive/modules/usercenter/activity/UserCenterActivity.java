package cn.gietv.mlive.modules.usercenter.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import cn.gietv.mlive.base.AbsBaseActivity;
import cn.gietv.mlive.modules.usercenter.fragment.UserCenterFragment;
import cn.gietv.mlive.utils.HeadViewController;
import cn.gietv.mlive.utils.IntentUtils;

/**
 * author：steven
 * datetime：15/10/9 21:03
 *
 */
public class UserCenterActivity extends AbsBaseActivity {

    public static final String EXTRA_TITLE = "extra_title";
    UserCenterFragment fragment;

    public static void openUserCenterActivity(Context context, String userId, String title) {
        Bundle bundle = new Bundle();
        bundle.putString(UserCenterFragment.EXTRA_USER_ID, userId);
        bundle.putString(EXTRA_TITLE, title);
        IntentUtils.openActivity(context, UserCenterActivity.class, bundle);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(cn.gietv.mlive.R.layout.commcon_fragment_head_layout);
        String title = getIntent().getExtras().getString(EXTRA_TITLE);
        HeadViewController.initSearchHead(this, title);
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        fragment = UserCenterFragment.getInstence(getIntent().getExtras().getString(UserCenterFragment.EXTRA_USER_ID));
        ft.add(cn.gietv.mlive.R.id.fragment, fragment);
        ft.commit();
    }

    @Override
    protected void onResume() {
        super.onResume();
//        fragment.hideHead();
    }
}
