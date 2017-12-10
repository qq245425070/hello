package cn.gietv.mlive.modules.usercenter.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.ViewPager;

import cn.gietv.mlive.MainApplication;
import cn.gietv.mlive.R;
import cn.gietv.mlive.base.AbsBaseActivity;
import cn.gietv.mlive.modules.follow.fragment.FollowContentFragment;
import cn.gietv.mlive.modules.usercenter.adapter.UserSubscribePagerAdapter;
import cn.gietv.mlive.modules.usercenter.bean.UserCenterBean;
import cn.gietv.mlive.utils.CacheUtils;
import cn.gietv.mlive.utils.HeadViewController;
import cn.gietv.mlive.utils.IntentUtils;
import cn.gietv.mlive.views.slidingTab.SlidingTabLayout;

/**
 * Created by houde on 2016/9/23.
 */
public class UserSubscribeActivity extends AbsBaseActivity {
    private SlidingTabLayout mTab;
    private ViewPager mViewPager;
    private int isMine;
    public static void openUserSubscribeActivity(Context context, String userid, String nickname){
        Bundle bundle = new Bundle();
        bundle.putString("id",userid);
        bundle.putString("nickname",nickname);
        IntentUtils.openActivity(context,UserSubscribeActivity.class,bundle);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_subscribe);
        String nickname = getIntent().getStringExtra("nickname");
        UserCenterBean.UserinfoEntity user = CacheUtils.getCacheUserInfo();
        if(user != null && nickname.equals(user.nickname)){
            nickname = "我的";
            isMine = 1;
        }
        HeadViewController.initHeadWithoutSearch(this,nickname+"订阅");
        mTab = (SlidingTabLayout) findViewById(R.id.tab_indicator);
        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        mViewPager.setAdapter(new UserSubscribePagerAdapter(getSupportFragmentManager(),getIntent().getStringExtra("id"),isMine));
        mTab.setViewPager(mViewPager);
    }

    @Override
    protected void onDestroy() {
        FollowContentFragment followContentFragment =  (FollowContentFragment) MainApplication.getInstance().getHomeActivity().getSupportFragmentManager().findFragmentByTag("订阅");
        if(followContentFragment != null)
            followContentFragment.getData();
        super.onDestroy();
    }
}
