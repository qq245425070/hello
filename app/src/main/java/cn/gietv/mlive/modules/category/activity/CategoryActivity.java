package cn.gietv.mlive.modules.category.activity;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.gietv.mlive.R;
import cn.gietv.mlive.base.AbsBaseActivity;
import cn.gietv.mlive.constants.CommConstants;
import cn.gietv.mlive.http.DefaultLiveHttpCallBack;
import cn.gietv.mlive.modules.category.adapter.CategoryPagerAdapter;
import cn.gietv.mlive.modules.follow.model.FollowModel;
import cn.gietv.mlive.modules.login.activity.LoginActivity;
import cn.gietv.mlive.utils.ConfigUtils;
import cn.gietv.mlive.utils.IntentUtils;
import cn.gietv.mlive.utils.NotificationTitleBarUtils;
import cn.gietv.mlive.utils.SharedPreferenceUtils;
import cn.gietv.mlive.utils.ToastUtils;
import cn.gietv.mlive.utils.UserUtils;
import cn.gietv.mlive.views.TagGuidePopuWindow;
import cn.gietv.mlive.views.slidingTab.SlidingTabLayout;
import cn.kalading.android.retrofit.utils.RetrofitUtils;

/**
 * Created by houde on 2016/5/27.
 */
public class CategoryActivity extends AbsBaseActivity {
    private String mID;
    private String mName;
    private ViewPager mPager;
    private SlidingTabLayout indicator;
    private CategoryPagerAdapter mAdapter;
    private FollowModel followModel;
    private ImageView mAttentionText;
    private int isFollow ;
    private TagGuidePopuWindow mPopuWindow;
    public static void openActivity(Context context,String id,String name,int type){
        Bundle bundle = new Bundle();
        bundle.putString("id", id);
        bundle.putString("name", name);
        bundle.putInt("type", type);
        IntentUtils.openActivity(context, CategoryActivity.class, bundle);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        NotificationTitleBarUtils.setGreenNotification(this);
        setContentView(R.layout.activity_category);
        initView();

    }

    @Override
    protected void onResume() {
        super.onResume();
        if(SharedPreferenceUtils.getBoolean(ConfigUtils.FIRST_VERSION + ConfigUtils.FIRST_IN_TAG_PAGE,true)) {
            SharedPreferenceUtils.saveProp(ConfigUtils.FIRST_VERSION + ConfigUtils.FIRST_IN_TAG_PAGE,false);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    showPopuWindow();
                }
            }, 100);
        }
    }

    private void showPopuWindow() {
        SharedPreferenceUtils.saveProp(ConfigUtils.FIRST_VERSION + ConfigUtils.FIRST_IN_TAG_PAGE,false);
        mPopuWindow = new TagGuidePopuWindow(this);
        mPopuWindow.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        mPopuWindow.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        mPopuWindow.setFocusable(true);
        mPopuWindow.setOutsideTouchable(true);
        mPopuWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        View view = LayoutInflater.from(this).inflate(R.layout.activity_category,null);
        mPopuWindow.showAtLocation(view, Gravity.NO_GRAVITY,0,0);
    }

    private void initView() {
        mID = getIntent().getStringExtra("id");
        mName = getIntent().getStringExtra("name");
        getView();
        List<String> titles = new ArrayList<>();
        titles.add("视频");
        titles.add("专辑");
        titles.add("作者");
        mAdapter = new CategoryPagerAdapter(getSupportFragmentManager(),titles,mID,1);
        mPager.setAdapter(mAdapter);
        indicator.setViewPager(mPager);
        mPager.setCurrentItem(0);
    }

    public void getView() {
//        HeadViewController.initSearchHead(this, mName);
        ImageButton exitButton = (ImageButton) findViewById(R.id.head_ib_exit);
        TextView titleText = (TextView) findViewById(R.id.head_tv_title);
        titleText.setText(mName);
        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        indicator = (SlidingTabLayout) findViewById(R.id.tab_indicator);
        mPager = (ViewPager) findViewById(R.id.pager);
        mAttentionText = (ImageView) findViewById(R.id.subscribe_image);
        followModel = RetrofitUtils.create(FollowModel.class);
        mAttentionText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(UserUtils.isNotLogin()){
                    IntentUtils.openActivity(CategoryActivity.this, LoginActivity.class);
                    return;
                }
                if(isFollow == CommConstants.FOLLOW_TRUE) {
                        mAttentionText.setClickable(false);
                        followModel.follow(mName, CommConstants.FOLLOW_FALSE, CommConstants.TYPE_TAG, new DefaultLiveHttpCallBack<String>() {
                            @Override
                            public void success(String s) {
                                if (isNotFinish()) {
                                    isFollow = CommConstants.FOLLOW_FALSE;
                                    mAttentionText.setClickable(true);
                                    mAttentionText.setImageResource(R.mipmap.subscribe_no);
                                    ToastUtils.showToastShort(CategoryActivity.this , "取消订阅成功");
                                }
                            }

                            @Override
                            public void failure(String message) {
                                if (isNotFinish()) {
                                    ToastUtils.showToastShort(CategoryActivity.this, message);
                                }
                            }
                        });
                }else{
                        mAttentionText.setClickable(false);
                        followModel.follow(mName, CommConstants.FOLLOW_TRUE, CommConstants.TYPE_TAG, new DefaultLiveHttpCallBack<String>() {
                            @Override
                            public void success(String s) {
                                if (isNotFinish()) {
                                    isFollow = CommConstants.FOLLOW_TRUE;
                                    mAttentionText.setClickable(true);
                                    ToastUtils.showToastShort(CategoryActivity.this , "订阅成功");
                                    mAttentionText.setImageResource(R.mipmap.subscribe_over);
                                }
                            }

                            @Override
                            public void failure(String message) {
                                if (isNotFinish()) {
                                    ToastUtils.showToastShort(CategoryActivity.this, message);
                                }
                            }
                        });
                }
            }
        });

    }
    public void setIsFollow(int isFollow){
        this.isFollow = isFollow;
        if(isFollow == CommConstants.FOLLOW_TRUE){
            mAttentionText.setImageResource(R.mipmap.subscribe_over);
        }else{
            mAttentionText.setImageResource(R.mipmap.subscribe_no);
        }
    }
}
