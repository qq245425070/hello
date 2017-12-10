package cn.gietv.mlive.modules.category.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
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
import cn.gietv.mlive.modules.category.adapter.CategoryPagerAdapter2;
import cn.gietv.mlive.modules.follow.fragment.FollowContentFragment;
import cn.gietv.mlive.modules.follow.model.FollowModel;
import cn.gietv.mlive.modules.game.bean.GameInfoBean;
import cn.gietv.mlive.modules.game.bean.GameProgramBean;
import cn.gietv.mlive.modules.game.model.GameModel;
import cn.gietv.mlive.modules.login.activity.LoginActivity;
import cn.gietv.mlive.modules.recommend.bean.RecommendBean;
import cn.gietv.mlive.modules.recommend.model.RecommendModel;
import cn.gietv.mlive.parallaxheaderviewpager.ParallaxViewPagerBaseActivity;
import cn.gietv.mlive.utils.ImageLoaderUtils;
import cn.gietv.mlive.utils.IntentUtils;
import cn.gietv.mlive.utils.NotificationTitleBarUtils;
import cn.gietv.mlive.utils.ToastUtils;
import cn.gietv.mlive.utils.UserUtils;
import cn.gietv.mlive.views.slidingTab.SlidingTabLayout;
import cn.kalading.android.retrofit.utils.RetrofitUtils;

/**
 * Created by houde on 2016/5/27.
 */
public class CategoryActivity2 extends ParallaxViewPagerBaseActivity {
    private ImageView mImage;
    private RecommendModel mModel;
    private List<Object> list;
    private String mID;
    private String mName;
    private SlidingTabLayout mSliding;
    private TextView mTitle;
    private ImageView mExitImage;
    private ImageLoader mImageLoader;
    private ImageView mAttentionImage;
    private String mImageUrl ;
    int location[] = new int[2];
    private FollowModel model;
    private GameModel gModel;
    private GameInfoBean.GameInfoEntity gameInfoEntity;
    public static void openActivity(Context context,String id,String name,int type,String imageUrl){
        Bundle bundle = new Bundle();
        bundle.putString("id", id);
        bundle.putString("name", name);
        bundle.putInt("type", type);
        bundle.putString("imageUrl", imageUrl);
        IntentUtils.openActivity(context, CategoryActivity2.class, bundle);
    }
    public static void openActivity(Context context,GameInfoBean.GameInfoEntity gameInfoEntity){
        Bundle bundle = new Bundle();
        bundle.putSerializable("gameEntity", gameInfoEntity);
        IntentUtils.openActivity(context, CategoryActivity2.class, bundle);
    }

    @Override
    protected void initValues() {
        int tabHeight = getResources().getDimensionPixelSize(R.dimen.tab_height);
        mMinHeaderHeight = getResources().getDimensionPixelSize(R.dimen.min_header_height);
        mHeaderHeight = getResources().getDimensionPixelSize(R.dimen.header_height);
        mMinHeaderTranslation = -mMinHeaderHeight + tabHeight + tabHeight;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e("ceshi","CategoryActivity2:onCreate");
        NotificationTitleBarUtils.setTranspanrentNotification(this);
        setContentView(R.layout.activity_album);
        initValues();
        model = RetrofitUtils.create(FollowModel.class);
        gModel = RetrofitUtils.create(GameModel.class);
        mModel = RetrofitUtils.create(RecommendModel.class);
        list = new ArrayList<>();
        gameInfoEntity = (GameInfoBean.GameInfoEntity) getIntent().getSerializableExtra("gameEntity");
        mID = gameInfoEntity == null ? getIntent().getStringExtra("id") : gameInfoEntity._id;
        mName = gameInfoEntity == null ? getIntent().getStringExtra("name") : gameInfoEntity.name;
        mImageUrl = gameInfoEntity == null ? getIntent().getStringExtra("imageUrl") : gameInfoEntity.spic;
        mTitle = (TextView) findViewById(R.id.head_tv_title);
        mAttentionImage = (ImageView)findViewById(R.id.attention_text);
        Log.e("ceshi","gameInfoEntity._id="+gameInfoEntity._id);
        gModel.getArea(gameInfoEntity._id, 1, 1, 1, new DefaultLiveHttpCallBack<GameProgramBean>() {
            @Override
            public void success(GameProgramBean gameProgramBean) {
                mAttentionImage.setVisibility(View.VISIBLE);
                Log.e("ceshi","getGameInfo:success");
                if(gameProgramBean!=null){

                    mAttentionImage.setVisibility(View.VISIBLE);
                    if(gameProgramBean.game.isfollow == CommConstants.FOLLOW_TRUE){
                        mAttentionImage.setImageResource(R.mipmap.subscribe_over);
                    }else {
                        mAttentionImage.setImageResource(R.mipmap.subscribe_no);
                    }
                    gameInfoEntity=gameProgramBean.game;
                    initImageOnclick();

                }

            }

            @Override
            public void failure(String message) {
                Log.e("ceshi","getGameInfo:failure");
            }
        });

        mTitle.setText(mName);
        mExitImage = (ImageView) findViewById(R.id.head_ib_exit);
        mImage = (ImageView) findViewById(R.id.image);
        mImageLoader = ImageLoaderUtils.getDefaultImageLoader(this);
        mImageLoader.displayImage(mImageUrl,mImage);
        mViewPager = (ViewPager) findViewById(R.id.view_pager);
        mExitImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mSliding = (SlidingTabLayout) findViewById(R.id.navig_tab);
        mHeader = findViewById(R.id.header);
        if (savedInstanceState != null) {
            mImage.setTranslationY(savedInstanceState.getFloat(IMAGE_TRANSLATION_Y));
            mHeader.setTranslationY(savedInstanceState.getFloat(HEADER_TRANSLATION_Y));
        }
        getData();
    }


    public void initImageOnclick(){
        mAttentionImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(UserUtils.isNotLogin()){
                    IntentUtils.openActivity(CategoryActivity2.this, LoginActivity.class);
                    return;
                }
                mAttentionImage.setClickable(false);
                if(gameInfoEntity.isfollow == CommConstants.FOLLOW_TRUE){
                    model.follow(gameInfoEntity._id, CommConstants.FOLLOW_FALSE, gameInfoEntity.type, new DefaultLiveHttpCallBack<String>() {
                        @Override
                        public void success(String s) {
                            if(isNotFinish()){
                                mAttentionImage.setClickable(true);
                                gameInfoEntity.isfollow = CommConstants.FOLLOW_FALSE;
                                mAttentionImage.setImageResource(R.mipmap.subscribe_no);
                                ToastUtils.showToastShort(CategoryActivity2.this, "取消订阅成功");
                                updateFollowPage();
                            }
                        }

                        @Override
                        public void failure(String message) {
                            if(isNotFinish()){
                                mAttentionImage.setClickable(true);
                                ToastUtils.showToastShort(CategoryActivity2.this,message);
                            }
                        }
                    });
                }else{
                    model.follow(gameInfoEntity._id, CommConstants.FOLLOW_TRUE, gameInfoEntity.type, new DefaultLiveHttpCallBack<String>() {
                        @Override
                        public void success(String s) {
                            if(isNotFinish()){
                                mAttentionImage.setClickable(true);
                                gameInfoEntity.isfollow = CommConstants.FOLLOW_TRUE;
                                mAttentionImage.setImageResource(R.mipmap.subscribe_over);
                                ToastUtils.showToastShort(CategoryActivity2.this, "订阅成功");

                               updateFollowPage();
                            }
                        }

                        @Override
                        public void failure(String message) {
                            if(isNotFinish()){
                                mAttentionImage.setClickable(true);
                                ToastUtils.showToastShort(CategoryActivity2.this,message);
                            }
                        }
                    });
                }
            }
        });
    }
    public int[] getLocation(){
        mSliding.getLocationOnScreen(location);
        return location;
    }
    //更新关注页内容
    private void updateFollowPage(){
        FollowContentFragment followContentFragment =  (FollowContentFragment) MainApplication.getInstance().getHomeActivity().getSupportFragmentManager().findFragmentByTag("关注");
        if(followContentFragment != null)
            followContentFragment.getData();
    }
    private void getData() {
        mModel.getCategoryRecommendData(mID, new DefaultLiveHttpCallBack<RecommendBean>() {
            @Override
            public void success(RecommendBean recommendBean) {
                if (isNotFinish()) {
                    loadDataSuccess(recommendBean);
                }
            }

            @Override
            public void failure(String message) {
                if (isNotFinish()) {
                    ToastUtils.showToastShort(CategoryActivity2.this, message);
                }
            }
        });
    }
    private void loadDataSuccess(RecommendBean recommendBean) {
        if (isNotFinish()) {
            if (recommendBean == null) {
                List<String> titles = new ArrayList<>();
                titles.add("视频");
                titles.add("专辑");
                titles.add("作者");
                mNumFragments = titles.size();
                mAdapter = new CategoryPagerAdapter2(getSupportFragmentManager(), titles,mID);
            }else{
                if (recommendBean.carousels != null && recommendBean.carousels.size() > 0) {
                    list.add(recommendBean.carousels);
                }
                if (recommendBean.programsbygameid != null && recommendBean.programsbygameid.size() != 0) {
                    list.addAll(recommendBean.programsbygameid);
                }
//                if (recommendBean.follow != null && recommendBean.follow.size() != 0) {
//                    list.addAll(recommendBean.follow);
//                }
//                mImageLoader.displayImage(recommendBean.carousels.get(0).carouselimg, mImage);
                List<String> titles = new ArrayList<>();
                titles.add("推荐");
                titles.add("视频");
                titles.add("专辑");
                titles.add("作者");
                mNumFragments = titles.size();
                mAdapter = new CategoryPagerAdapter2(getSupportFragmentManager(),titles,mID);
                ((CategoryPagerAdapter2)mAdapter).setData(list);
            }
            mViewPager.setAdapter(mAdapter);
            mViewPager.setCurrentItem(0);
            mViewPager.setOffscreenPageLimit(mNumFragments);
            mSliding.setViewPager(mViewPager);
            mSliding.setOnPageChangeListener(getViewPagerChangeListener());
        }
    }
    @Override
    protected void scrollHeader(int scrollY) {
        float translationY = Math.max(-scrollY, mMinHeaderTranslation);
        mHeader.setTranslationY(translationY);
        mImage.setTranslationY(-translationY / 3);
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putFloat(IMAGE_TRANSLATION_Y, mImage.getTranslationY());
        outState.putFloat(HEADER_TRANSLATION_Y, mHeader.getTranslationY());
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void setupAdapter() {

    }
    public void changeSubscribeBg(int isFollow){
        if(isFollow == CommConstants.FOLLOW_TRUE){
            mAttentionImage.setImageResource(R.mipmap.subscribe_over);
        }else{
            mAttentionImage.setImageResource(R.mipmap.subscribe_no);
        }
    }
}
