package cn.gietv.mlive.modules.author.activity;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import cn.gietv.mlive.R;
import cn.gietv.mlive.constants.CommConstants;
import cn.gietv.mlive.http.DefaultLiveHttpCallBack;
import cn.gietv.mlive.modules.author.Fragment.AlbumFragment;
import cn.gietv.mlive.modules.author.Fragment.DescriptionFragment;
import cn.gietv.mlive.modules.compere.bean.AlbumBean;
import cn.gietv.mlive.modules.compere.model.CompereModel;
import cn.gietv.mlive.modules.follow.model.FollowModel;
import cn.gietv.mlive.modules.login.activity.LoginActivity;
import cn.gietv.mlive.modules.usercenter.bean.UserCenterBean;
import cn.gietv.mlive.parallaxheaderviewpager.ParallaxFragmentPagerAdapter;
import cn.gietv.mlive.parallaxheaderviewpager.ParallaxViewPagerBaseActivity;
import cn.gietv.mlive.utils.ConfigUtils;
import cn.gietv.mlive.utils.ImageLoaderUtils;
import cn.gietv.mlive.utils.IntentUtils;
import cn.gietv.mlive.utils.NotificationTitleBarUtils;
import cn.gietv.mlive.utils.SharedPreferenceUtils;
import cn.gietv.mlive.utils.ToastUtils;
import cn.gietv.mlive.utils.UserUtils;
import cn.gietv.mlive.views.AuthorGuidePopuWindow;
import cn.gietv.mlive.views.slidingTab.SlidingTabLayout;
import cn.kalading.android.retrofit.utils.RetrofitUtils;

/**
 * Created by houde on 2016/5/29.
 */
public class AuthorActivity extends ParallaxViewPagerBaseActivity {

    private ImageView mImage;
    private SlidingTabLayout mSliding;
    private String mID;
    private String mName;
    private CompereModel model;
    private ImageLoader mImageLoader;
    private AlbumFragment videoFragment;
    private DescriptionFragment descriptionFragment;
    private TextView mTitle;
    private ImageView mExitImage;
    private ImageView userIcon;
    private ImageView attentionText;
    private FollowModel followModel;
    private UserCenterBean.UserinfoEntity userinfo;
    private int isFollow;
    public static void openAnchorActivity(String name,String id,Context context,int isFollow){
        Bundle bundle = new Bundle();
        bundle.putString("name",name);
        bundle.putString("id",id);
        bundle.putInt("isFollow",isFollow);
        IntentUtils.openActivity(context, AuthorActivity.class, bundle);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        NotificationTitleBarUtils.setTranspanrentNotification(this);
        setContentView(R.layout.activity_author);
        initValues();
        isFollow = getIntent().getIntExtra("isFollow",0);
        mImageLoader = ImageLoaderUtils.getDefaultImageLoader(this);
        userIcon = (ImageView)findViewById(R.id.user_icon);
        attentionText = (ImageView)findViewById(R.id.attention_text);
        model = RetrofitUtils.create(CompereModel.class);
        followModel = RetrofitUtils.create(FollowModel.class);
        mID = getIntent().getStringExtra("id");
        mName = getIntent().getStringExtra("mName");
        mViewPager = (ViewPager) findViewById(R.id.view_pager);
        mImage = (ImageView) findViewById(R.id.user_icon);
        mSliding = (SlidingTabLayout) findViewById(R.id.navig_tab);
        mHeader = findViewById(R.id.header);
        mTitle = (TextView) findViewById(R.id.head_tv_title);
        mExitImage = (ImageView) findViewById(R.id.head_ib_exit);
        mTitle.setText(mName);
        ToastUtils.showToast(this,mName);
        mExitImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        if (savedInstanceState != null) {
            mImage.setTranslationY(savedInstanceState.getFloat(IMAGE_TRANSLATION_Y));
            mHeader.setTranslationY(savedInstanceState.getFloat(HEADER_TRANSLATION_Y));
        }
        attentionText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(userinfo == null){
                    return;
                }
                if(UserUtils.isNotLogin()){
                    IntentUtils.openActivity(AuthorActivity.this, LoginActivity.class);
                    return;
                }
                if(isFollow == CommConstants.FOLLOW_TRUE){
                    followModel.follow(userinfo._id, CommConstants.FOLLOW_FALSE, userinfo.type, new DefaultLiveHttpCallBack<String>() {
                        @Override
                        public void success(String s) {
                            attentionText.setBackgroundResource(R.mipmap.subscribe_no);
                            isFollow = CommConstants.FOLLOW_FALSE;
                            ToastUtils.showToastShort(AuthorActivity.this,"取消订阅成功");
                        }

                        @Override
                        public void failure(String message) {
                            ToastUtils.showToastShort(AuthorActivity.this,message);
                        }
                    });
                }else {
                    followModel.follow(userinfo._id, CommConstants.FOLLOW_TRUE, userinfo.type, new DefaultLiveHttpCallBack<String>() {
                        @Override
                        public void success(String s) {
                            attentionText.setBackgroundResource(R.mipmap.subscribe_over);
                            isFollow = CommConstants.FOLLOW_TRUE;
                            ToastUtils.showToastShort(AuthorActivity.this,"订阅成功");
                        }

                        @Override
                        public void failure(String message) {
                            ToastUtils.showToastShort(AuthorActivity.this,message);
                        }
                    });
                }
            }
        });
        videoFragment = AlbumFragment.getInstance(0);
        descriptionFragment = DescriptionFragment.getInstance(1);
        model.getAlbumList(mID, CommConstants.COMMON_PAGE_COUNT, 1, CommConstants.DEFAULT_SORT_ONLINE_PERSON, new DefaultLiveHttpCallBack<AlbumBean>() {
            @Override
            public void success(final AlbumBean albumBean) {
                if(isNotFinish()) {
                    if(albumBean == null){
                        ToastUtils.showToastShort(AuthorActivity.this,"数据返回错误");
                        return ;
                    }
                    if (albumBean.albumlist != null && albumBean.albumlist.size() > 0)
                        videoFragment.setData(albumBean.albumlist);
                    if(albumBean.authorinfo == null){
                        return;
                    }
                   if(albumBean.authorinfo.isfollow == CommConstants.FOLLOW_TRUE){
                       attentionText.setBackgroundResource(R.mipmap.subscribe_over);
                   }else{
                       attentionText.setBackgroundResource(R.mipmap.subscribe_no);
                   }
                    List<Object> datas = new ArrayList<Object>();
                    if (albumBean.authorinfo != null) {
                        userinfo = albumBean.authorinfo;
                        mImageLoader.displayImage(userinfo.spic, userIcon);
                        datas.add(albumBean.authorinfo);
                        if (albumBean.authorinfo.tags != null && albumBean.authorinfo.tags.size() > 0) {
                            datas.add(albumBean.authorinfo.tags);
                        }
                    }

                    if (albumBean.authorlist_taglist != null && albumBean.authorlist_taglist.size() > 0)
                        datas.addAll(albumBean.authorlist_taglist);
                    descriptionFragment.setData(datas);
                }
            }

            @Override
            public void failure(String message) {
                if(isNotFinish())
                    ToastUtils.showToastShort(AuthorActivity.this,message);
            }
        });
        setupAdapter();
    }

    private void showGuideWindow(){
        SharedPreferenceUtils.saveProp(ConfigUtils.FIRST_VERSION + ConfigUtils.FIRST_IN_AUTHOR_PAGE,false);
        AuthorGuidePopuWindow popuWindow = new AuthorGuidePopuWindow(this);
        popuWindow.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        popuWindow.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        popuWindow.setFocusable(true);
        popuWindow.setOutsideTouchable(true);
        popuWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        View view = LayoutInflater.from(this).inflate(R.layout.activity_author,null);
        popuWindow.showAtLocation(view, Gravity.NO_GRAVITY,0,0);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(SharedPreferenceUtils.getBoolean(ConfigUtils.FIRST_VERSION + ConfigUtils.FIRST_IN_AUTHOR_PAGE,true)) {
            SharedPreferenceUtils.saveProp(ConfigUtils.FIRST_VERSION + ConfigUtils.FIRST_IN_AUTHOR_PAGE,false);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    showGuideWindow();
                }
            }, 100);
        }
    }

    @Override
    protected void initValues() {
        int tabHeight = getResources().getDimensionPixelSize(R.dimen.tab_height);
        mMinHeaderHeight = getResources().getDimensionPixelSize(R.dimen.min_header_height);
        mHeaderHeight = getResources().getDimensionPixelSize(R.dimen.header_height);
        mMinHeaderTranslation = -mMinHeaderHeight + tabHeight + tabHeight;
        mNumFragments = 2;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putFloat(IMAGE_TRANSLATION_Y, mImage.getTranslationY());
        outState.putFloat(HEADER_TRANSLATION_Y, mHeader.getTranslationY());
        super.onSaveInstanceState(outState);
    }
    @Override
    protected void scrollHeader(int scrollY) {
        float translationY = Math.max(-scrollY, mMinHeaderTranslation);
        mHeader.setTranslationY(translationY);
//        mImage.setTranslationY(-translationY / 3);
    }

    @Override
    protected void setupAdapter() {
        Log.e("ceshi", "mViewPager.focus=" + mViewPager.isFocused() + "mSliding.focus=" + mSliding.isFocused());
        if (mAdapter == null) {
            mAdapter = new ViewPagerAdapter(getSupportFragmentManager(), mNumFragments);
        }
        mViewPager.setAdapter(mAdapter);
        mViewPager.setOffscreenPageLimit(mNumFragments);
        mViewPager.setCurrentItem(0);
        mSliding.setOnPageChangeListener(getViewPagerChangeListener());
        mSliding.setViewPager(mViewPager);
    }

    class ViewPagerAdapter extends ParallaxFragmentPagerAdapter {

        public ViewPagerAdapter(FragmentManager fm, int numFragments) {
            super(fm, numFragments);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return videoFragment;
                case 1:
                    return descriptionFragment;
                default:
                    throw new IllegalArgumentException("Wrong page given " + position);
            }
        }

        @Override
        public CharSequence getPageTitle(int position) {
            System.out.println("111111111");
            switch (position) {
                case 0:
                    return "专辑";

                case 1:
                    return "简介";

                default:
                    throw new IllegalArgumentException("wrong position for the fragment in vehicle page");
            }
        }
    }
}
