package cn.gietv.mlive.modules.album.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import cn.gietv.mlive.MainApplication;
import cn.gietv.mlive.R;
import cn.gietv.mlive.constants.CommConstants;
import cn.gietv.mlive.http.DefaultLiveHttpCallBack;
import cn.gietv.mlive.modules.album.fragment.DescriptionFragment;
import cn.gietv.mlive.modules.album.fragment.VideoFragment;
import cn.gietv.mlive.modules.follow.fragment.FollowContentFragment;
import cn.gietv.mlive.modules.follow.model.FollowModel;
import cn.gietv.mlive.modules.game.bean.GameInfoBean;
import cn.gietv.mlive.modules.game.model.GameModel;
import cn.gietv.mlive.modules.login.activity.LoginActivity;
import cn.gietv.mlive.modules.program.bean.ProgramBean;
import cn.gietv.mlive.parallaxheaderviewpager.ParallaxFragmentPagerAdapter;
import cn.gietv.mlive.parallaxheaderviewpager.ParallaxViewPagerBaseActivity;
import cn.gietv.mlive.utils.ImageLoaderUtils;
import cn.gietv.mlive.utils.IntentUtils;
import cn.gietv.mlive.utils.NotificationTitleBarUtils;
import cn.gietv.mlive.utils.ToastUtils;
import cn.gietv.mlive.utils.UserUtils;
import cn.gietv.mlive.views.slidingTab.SlidingTabLayout;
import cn.kalading.android.retrofit.utils.RetrofitUtils;

public class AlbumActivity extends ParallaxViewPagerBaseActivity {

    private ImageView mImage;
    private SlidingTabLayout mSliding;
    private String mID;
    private String mName;
    private GameModel gameModel;
    private ImageLoader mImageLoader;
    private VideoFragment videoFragment;
    private DescriptionFragment descriptionFragment;
    private TextView mTitle;
    private ImageView mExitImage;
    private ImageView attentionImage;
    private GameInfoBean.GameInfoEntity albuminfo;
    private FollowModel model;
    public static void openAlbumActivity(String name,String id,Context context){
        Bundle bundle = new Bundle();
        bundle.putString("name",name);
        bundle.putString("id",id);
        IntentUtils.openActivity(context,AlbumActivity.class,bundle);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        NotificationTitleBarUtils.setTranspanrentNotification(this);
        setContentView(R.layout.activity_album);
        initValues();
        mTitle = (TextView) findViewById(R.id.head_tv_title);
        mExitImage = (ImageView) findViewById(R.id.head_ib_exit);
        mImageLoader = ImageLoaderUtils.getDefaultImageLoader(this);
        gameModel = RetrofitUtils.create(GameModel.class);
        mID = getIntent().getStringExtra("id");
        mName = getIntent().getStringExtra("mName");
        mTitle.setText(mName);
        model = RetrofitUtils.create(FollowModel.class);
        mExitImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mViewPager = (ViewPager) findViewById(R.id.view_pager);
        attentionImage = (ImageView) findViewById(R.id.attention_text);
        attentionImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(UserUtils.isNotLogin()) {
                    IntentUtils.openActivity(AlbumActivity.this, LoginActivity.class);
                    return;
                }
                attentionImage.setClickable(false);
                if(albuminfo.isfollow == CommConstants.FOLLOW_TRUE){
                    model.follow(albuminfo._id, CommConstants.FOLLOW_FALSE, albuminfo.type, new DefaultLiveHttpCallBack<String>() {
                        @Override
                        public void success(String s) {
                            if(isNotFinish()) {
                                albuminfo.isfollow = 0;
                                attentionImage.setClickable(true);
                                attentionImage.setImageResource(R.mipmap.subscribe_no);
                                ToastUtils.showToastShort(AlbumActivity.this,"取消订阅成功");
                            }
                        }

                        @Override
                        public void failure(String message) {
                            if(isNotFinish()) {
                                attentionImage.setClickable(true);
                                ToastUtils.showToastShort(AlbumActivity.this,message);
                            }
                        }
                    });
                }else{
                    model.follow(albuminfo._id, CommConstants.FOLLOW_TRUE, albuminfo.type, new DefaultLiveHttpCallBack<String>() {
                        @Override
                        public void success(String s) {
                            if(isNotFinish()) {
                                albuminfo.isfollow = CommConstants.FOLLOW_TRUE;
                                attentionImage.setClickable(true);
                                attentionImage.setImageResource(R.mipmap.subscribe_over);
                                ToastUtils.showToastShort(AlbumActivity.this,"订阅成功");
                            }
                        }

                        @Override
                        public void failure(String message) {
                            if(isNotFinish()) {
                                attentionImage.setClickable(true);
                                ToastUtils.showToastShort(AlbumActivity.this,message);
                            }
                        }
                    });
                }

            }
        });
        mImage = (ImageView) findViewById(R.id.image);
        mSliding = (SlidingTabLayout) findViewById(R.id.navig_tab);
        mHeader = findViewById(R.id.header);
        if (savedInstanceState != null) {
            mImage.setTranslationY(savedInstanceState.getFloat(IMAGE_TRANSLATION_Y));
            mHeader.setTranslationY(savedInstanceState.getFloat(HEADER_TRANSLATION_Y));
        }
        videoFragment = VideoFragment.getInstance(0);
        descriptionFragment = DescriptionFragment.getInstance(1);
        gameModel.getAlbumLists(mID, CommConstants.COMMON_PAGE_COUNT, 1, CommConstants.DEFAULT_SORT_ONLINE_PERSON, new DefaultLiveHttpCallBack<ProgramBean>() {
            @Override
            public void success(ProgramBean programBean) {
                if(isNotFinish()) {
                    if (programBean == null) {
                        return;
                    }
                    if(programBean.albuminfo != null){
                        attentionImage.setVisibility(View.VISIBLE);
                        albuminfo = programBean.albuminfo;
                        if(albuminfo.isfollow == CommConstants.FOLLOW_TRUE){
                            attentionImage.setImageResource(R.mipmap.subscribe_over);
                        }else{
                            attentionImage.setImageResource(R.mipmap.subscribe_no);
                        }
                    }
                    if (programBean.programs != null && programBean.programs.size() > 0)
                        videoFragment.setData(programBean.programs);
                    List<Object> datas = new ArrayList<>();
                    if (programBean.albuminfo != null) {
                        mImageLoader.displayImage(programBean.albuminfo.spic, mImage);
                        datas.add(programBean.albuminfo);
                        if (programBean.albuminfo.tags != null && programBean.albuminfo.tags.size() > 0) {
                            datas.add(programBean.albuminfo.tags);
                        }
                    }
                    if (programBean.taglist != null && programBean.taglist.size() > 0)
                        datas.add(programBean.taglist);
                    if (programBean.albumlist_taglist != null && programBean.albumlist_taglist.size() > 0)
                        datas.addAll(programBean.albumlist_taglist);
                    descriptionFragment.setData(datas);
                }
            }

            @Override
            public void failure(String message) {
                ToastUtils.showToastShort(AlbumActivity.this,message);
            }
        });
        setupAdapter();
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
        mImage.setTranslationY(-translationY / 3);
    }

    @Override
    protected void setupAdapter() {
        if (mAdapter == null) {
            mAdapter = new ViewPagerAdapter(getSupportFragmentManager(), mNumFragments);
        }
        mViewPager.setAdapter(mAdapter);
        mViewPager.setCurrentItem(0);
        mViewPager.setOffscreenPageLimit(mNumFragments);
        mSliding.setViewPager(mViewPager);
        mSliding.setOnPageChangeListener(getViewPagerChangeListener());
    }

    @Override
    protected void onResume() {
        super.onResume();
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
            switch (position) {
                case 0:
                    return "视频";

                case 1:
                    return "简介";

                default:
                    throw new IllegalArgumentException("wrong position for the fragment in vehicle page");
            }
        }
    }

    @Override
    protected void onDestroy() {
        changeFollowPager();
        super.onDestroy();
    }
    private void changeFollowPager(){//改变订阅页的内容
        FollowContentFragment followContentFragment =  (FollowContentFragment) MainApplication.getInstance().getHomeActivity().getSupportFragmentManager().findFragmentByTag("订阅");
        if(followContentFragment != null)
            followContentFragment.getData();
    }
}
