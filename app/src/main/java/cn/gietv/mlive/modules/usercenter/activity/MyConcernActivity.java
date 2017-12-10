package cn.gietv.mlive.modules.usercenter.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import cn.gietv.mlive.R;
import cn.gietv.mlive.base.AbsBaseActivity;
import cn.gietv.mlive.constants.CommConstants;
import cn.gietv.mlive.modules.usercenter.bean.UserCenterBean;
import cn.gietv.mlive.modules.usercenter.fragment.MyConcernFragment;
import cn.gietv.mlive.utils.CacheUtils;
import cn.gietv.mlive.utils.HeadViewController;
import cn.gietv.mlive.utils.IntentUtils;

/**
 * Created by houde on 2016/7/5.
 */
public class MyConcernActivity extends AbsBaseActivity {
//    private PagerTab2 mPagerTab;
//    private ViewPager mViewPager;
    private String mID;
    private int isMine ;
//    private MyConcernPagerAdapter mAdapter;
//    private final String[] TITLE = {"好友","游戏","专辑","标签"};
    public static void openMyConcernActivity(Context context,String id,String nickname,int flag){
        Bundle bundle = new Bundle();
        bundle.putString("id",id);
        bundle.putString("nickname",nickname);
        bundle.putInt("flag",flag);
        IntentUtils.openActivity(context,MyConcernActivity.class,bundle);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_concern);
        String nickname = getIntent().getStringExtra("nickname");
        UserCenterBean.UserinfoEntity user = CacheUtils.getCacheUserInfo();
        if(user != null && nickname.equals(user.nickname)){
            nickname = "我的";
            isMine = 1;
        }
        int flag = getIntent().getIntExtra("flag",0);
        String follow ;
        if(flag == MyConcernFragment.FLAG_FOLLOW){
            follow = "关注";
        }else{
            follow = "粉丝";
        }
        HeadViewController.initHeadWithoutSearch(this,nickname  + follow);
//        mPagerTab = (PagerTab2) findViewById(R.id.pager_tab);
//        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        mID = getIntent().getStringExtra("id");
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.fragment, MyConcernFragment.getInstance(mID, CommConstants.CAROUSEL_TYPE_COMPERE,getIntent().getIntExtra("flag",0),isMine));
        ft.commitAllowingStateLoss();
//        mAdapter = new MyConcernPagerAdapter(getSupportFragmentManager(),TITLE,mID);
//        mViewPager.setAdapter(mAdapter);
//        mPagerTab.setTextColor(R.color.tab_text_color_category);
//        mPagerTab.setViewPager(mViewPager);
//        mPagerTab.setCurrentPosition(0);
    }

}
