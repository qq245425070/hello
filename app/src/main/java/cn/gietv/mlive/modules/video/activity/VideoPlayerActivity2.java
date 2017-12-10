package cn.gietv.mlive.modules.video.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import cn.gietv.mlive.R;
import cn.gietv.mlive.base.AbsBaseActivity;
import cn.gietv.mlive.http.DefaultLiveHttpCallBack;
import cn.gietv.mlive.modules.statistics.StatisticsMode;
import cn.gietv.mlive.modules.video.fragment.VideoPlayFragment4;
import cn.gietv.mlive.utils.AndroidBug5497Workaround;
import cn.gietv.mlive.utils.IntentUtils;
import cn.gietv.mlive.utils.NotificationTitleBarUtils;
import cn.kalading.android.retrofit.utils.RetrofitUtils;

/**
 * author：steven
 * datetime：15/10/8 16:42
 */
public class VideoPlayerActivity2 extends AbsBaseActivity {
    private String mVideoPath,mProId;
    private VideoPlayFragment4 mInstance;
    private StatisticsMode statisticsMode;
    private long mCurrentDate;
    private int mType;
    public static void openVideoPlayerActivity(Context context,String videoPath,String proId,int position,String name,int type){
        Bundle bundle = new Bundle();
        bundle.putString("videoPath",videoPath);
        bundle.putString("proId", proId);
        bundle.putInt("position", position);
        bundle.putString("name", name);
        bundle.putInt("type",type);
        IntentUtils.openActivity(context, VideoPlayerActivity2.class, bundle);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        NotificationTitleBarUtils.setTranspanrentNotification(this);
        setContentView(R.layout.activity_video_play);
        AndroidBug5497Workaround.assistActivity(this);
        mVideoPath = getIntent().getStringExtra("videoPath") ;
        mProId = getIntent().getStringExtra("proId");
        mType = getIntent().getIntExtra("type",2);
        statisticsMode = RetrofitUtils.create(StatisticsMode.class);
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        mInstance = VideoPlayFragment4.getInstence(mVideoPath,mProId,VideoPlayFragment4.FULL_SCREEN_MODEL,getIntent().getIntExtra("position",0),getIntent().getStringExtra("name"),getIntent().getIntExtra("type", 2));
        ft.replace(R.id.video_play_fragment, mInstance);
        ft.commit();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mCurrentDate = System.currentTimeMillis();
        System.out.println(getIntent().getStringExtra("name"));
    }
    public void exitActivity(int position){
        Intent intent = new Intent();
        intent.putExtra("position", position);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        if(mType != 2) {
            statisticsMode.sendProgramTime(mProId, System.currentTimeMillis() - mCurrentDate, getIntent().getStringExtra("name"), getIntent().getIntExtra("type", 2), mCurrentDate, System.currentTimeMillis(), new DefaultLiveHttpCallBack<String>() {
                @Override
                public void success(String s) {
                }

                @Override
                public void failure(String message) {
                }
            });
        }
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(mInstance != null){
           if (mInstance.mVideoView != null){
               exitActivity(mInstance.mVideoView.getCurrentPosition());
           }
        }
    }
}
