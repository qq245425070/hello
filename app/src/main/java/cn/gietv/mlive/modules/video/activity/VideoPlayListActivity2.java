package cn.gietv.mlive.modules.video.activity;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import cn.gietv.mlive.R;
import cn.gietv.mlive.base.AbsBaseActivity;
import cn.gietv.mlive.db.DBUtils;
import cn.gietv.mlive.http.DefaultLiveHttpCallBack;
import cn.gietv.mlive.modules.program.bean.ProgramBean;
import cn.gietv.mlive.modules.statistics.StatisticsMode;
import cn.gietv.mlive.modules.video.adapter.VideoPlayListPagerAdapter;
import cn.gietv.mlive.modules.video.bean.VideoPlayBean;
import cn.gietv.mlive.modules.video.fragment.CommentFragment;
import cn.gietv.mlive.modules.video.fragment.VideoPlayFragment2;
import cn.gietv.mlive.modules.video.fragment.VideoPlayFragment4;
import cn.gietv.mlive.modules.video.fragment.VideoPlayFragment4.ReportListener;
import cn.gietv.mlive.modules.video.model.VideoModel;
import cn.gietv.mlive.utils.AndroidBug5497Workaround;
import cn.gietv.mlive.utils.DensityUtil;
import cn.gietv.mlive.utils.IntentUtils;
import cn.gietv.mlive.utils.NotificationTitleBarUtils;
import cn.gietv.mlive.utils.ToastUtils;
import cn.gietv.mlive.views.PagerTab;
import cn.gietv.mlive.views.ReportPopuWindow;
import cn.kalading.android.retrofit.utils.RetrofitUtils;

/**
 * Created by houde on 2016/5/29.
 */
public class VideoPlayListActivity2 extends AbsBaseActivity {
    public ViewPager mPager;
    private PagerTab mIndicator;
    public RelativeLayout mRelativeLayout;
    public View mLine;
    public ProgramBean.ProgramEntity mProgram;
    private VideoModel model;
    private VideoPlayFragment4 mInstance;
    private StatisticsMode statisticsMode;
    private long mCurrentDate;
    private long mStartTime;
    private long mTotalTime;
    public TextView mSignText;
    private VideoPlayListPagerAdapter pagerAdapter;
    public ImageView mSign;
    public String resourceid;
    public int resourceType;
    private Context mContext;
    public static void openVideoPlayListActivity2(Context context,ProgramBean.ProgramEntity bean){
        Bundle bundle = new Bundle();
        bundle.putSerializable("program", bean);
        IntentUtils.openActivity(context, VideoPlayListActivity2.class, bundle);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //设置透明状态栏
        NotificationTitleBarUtils.setTranspanrentNotification(this);
        setContentView(R.layout.activity_video_play_list2);
        mRelativeLayout = (RelativeLayout) findViewById(R.id.relativelayout);
        mLine = findViewById(R.id.line);
        AndroidBug5497Workaround.assistActivity(this);
        mContext = this;
        mProgram = (ProgramBean.ProgramEntity) getIntent().getSerializableExtra("program");
        if(mProgram == null){
            ToastUtils.showToast(this,"视频错误，请退出重试");
            return;
        }
        mStartTime = System.currentTimeMillis();
        statisticsMode = RetrofitUtils.create(StatisticsMode.class);
        mPager = (ViewPager) findViewById(R.id.pager);
        mIndicator = (PagerTab) findViewById(R.id.tab_indicator);
        mSignText = (TextView) findViewById(R.id.sign);
        mSign = (ImageView) findViewById(R.id.sign_image);


        model = RetrofitUtils.create(VideoModel.class);
        WindowManager windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        mHeight = windowManager.getDefaultDisplay().getHeight();
        model.queryProByProid(mProgram._id, new DefaultLiveHttpCallBack<VideoPlayBean>() {
            @Override
            public void success(VideoPlayBean videoPlayBean) {
                if (isNotFinish()) {
                    if (videoPlayBean == null)
                        return;
                    if (videoPlayBean.program == null) {
                        return;
                    }
                    mProgram = videoPlayBean.program;
                    String[] titles = new String[]{"简介", "评论" };
                    CommentFragment fragment = CommentFragment.getInstance(mProgram._id,mProgram.urlfrom);
                    fragment.setShowReportWindowListener(listener);
                    pagerAdapter = new VideoPlayListPagerAdapter(getSupportFragmentManager(), titles, mProgram._id,fragment);
                    mPager.setAdapter(pagerAdapter);
                    mIndicator.setViewPager(mPager);
                    mIndicator.setCurrentItem(0);
                    setPagerAdapter();
                    openFragment();
                }
            }

            @Override
            public void failure(String message) {
                if (isNotFinish()) {
                    ToastUtils.showToast(VideoPlayListActivity2.this, message);
                }
            }
        });

        mIndicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 1) {
                    if (mSignText.getVisibility() == View.VISIBLE) {
                        mSignText.setTextColor(Color.parseColor("#4fc396"));
                    }
                } else {
                    mSignText.setTextColor(Color.parseColor("#bbbdbf"));
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        mTotalTime += System.currentTimeMillis() - mCurrentDate;
    }

    @Override
    protected void onDestroy() {
        if(mProgram != null && mProgram.type == 2) {
            statisticsMode.sendProgramTime(mProgram._id,mTotalTime, mProgram.name, mProgram.type,mStartTime,System.currentTimeMillis(), new DefaultLiveHttpCallBack<String>() {
                @Override
                public void success(String s) {

                }

                @Override
                public void failure(String message) {

                }
            });
        }
        if(mInstance != null && mInstance.mVideoView != null){
            DBUtils.getInstance(this).saveUserVideo(mProgram._id,mInstance.mVideoView.getCurrentPosition(),"");
        }
        super.onDestroy();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    protected void onResume() {

        super.onResume();
        mCurrentDate = System.currentTimeMillis();
    }
    private int mHeight;
    private void openFragment (){
        if("youku".equals(mProgram.urlfrom) || "优酷".equals(mProgram.urlfrom)){
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            mInstance = VideoPlayFragment4.getInstence(mProgram._id, mProgram._id, mProgram.name, 2, 1,mProgram.urlfrom);
            if (!isNotFinish())
                return;
            ft.add(R.id.video_fragment, mInstance);
            ft.commitAllowingStateLoss();
            setReportListener();
            return;
        }
        model.getPlayUrl(mProgram._id, new DefaultLiveHttpCallBack<String>() {
            @Override
            public void success(String s) {
                if (isNotFinish()) {
                    if(s == null || TextUtils.isEmpty(s)){
                        ToastUtils.showToastShort(VideoPlayListActivity2.this,"返回的播放地址为空，请重新请求");
                        return;
                    }
                    if (mProgram.type == 2) {
                        FragmentManager fm = getSupportFragmentManager();
                        FragmentTransaction ft = fm.beginTransaction();
                        mInstance = VideoPlayFragment4.getInstence(s, mProgram._id, mProgram.name, 2, 1);
                        if (!isNotFinish())
                            return;
                        ft.add(R.id.video_fragment, mInstance);
                        ft.commitAllowingStateLoss();
                        setReportListener();
                    } else {
                        FragmentManager fm = getSupportFragmentManager();
                        FragmentTransaction ft = fm.beginTransaction();
                        ft.add(R.id.video_fragment, VideoPlayFragment2.getInstence(s, mProgram.spic, mProgram._id, mProgram.type, mProgram.name));
                        ft.commitAllowingStateLoss();
                    }
                }
            }

            @Override
            public void failure(String message) {
                if (isNotFinish())
                    ToastUtils.showToastShort(VideoPlayListActivity2.this, message);
            }
        });
    }
    private ReportPopuWindow mReportPopuWindow;
    private void setReportListener() {
        mInstance.setShowReportWindowListener(listener);
    }
    private ReportListener listener= new ReportListener(){
        @Override
        public void showReportPopuWindow(int type, String id){
            int popuWindowHeight = DensityUtil.dip2px(mContext, 81);
            if(mReportPopuWindow == null){
                mReportPopuWindow = new ReportPopuWindow(mContext,id,type);
                mReportPopuWindow.setHeight(popuWindowHeight);
                mReportPopuWindow.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
                mReportPopuWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                mReportPopuWindow.setFocusable(true);
                mReportPopuWindow.setOutsideTouchable(true);
            }else {
                mReportPopuWindow.setTypeAndID(type,id);
            }
            mReportPopuWindow.showAtLocation(mPager, Gravity.NO_GRAVITY,0,mHeight - popuWindowHeight);
        }
    };
    private void setPagerAdapter() {
        if (mProgram.message_cnt  > 0) {
            mSign.setVisibility(View.INVISIBLE);
            mSignText.setText("(" + mProgram.message_cnt + ")");
            mSignText.setTextColor(Color.parseColor("#bbbdbf"));
        }else{
            mSignText.setVisibility(View.INVISIBLE);
        }
    }
}
