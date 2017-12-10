package cn.gietv.mlive.modules.vrgame.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import cn.gietv.mlive.R;
import cn.gietv.mlive.base.AbsBaseActivity;
import cn.gietv.mlive.http.DefaultLiveHttpCallBack;
import cn.gietv.mlive.modules.game.bean.GameInfoBean;
import cn.gietv.mlive.modules.game.bean.GameProgramBean;
import cn.gietv.mlive.modules.game.model.GameModel;
import cn.gietv.mlive.modules.statistics.StatisticsMode;
import cn.gietv.mlive.modules.video.bean.MessageBean;
import cn.gietv.mlive.modules.vrgame.fragment.VRGameCommentFragment;
import cn.gietv.mlive.modules.vrgame.fragment.VRGameInfoFragment;
import cn.gietv.mlive.utils.DownloadController;
import cn.gietv.mlive.utils.HeadViewController;
import cn.gietv.mlive.utils.ImageLoaderUtils;
import cn.gietv.mlive.utils.IntentUtils;
import cn.gietv.mlive.utils.PackageUtils;
import cn.gietv.mlive.views.slidingTab.SlidingTabLayout;
import cn.kalading.android.retrofit.utils.RetrofitUtils;

/**
 * Created by houde on 2016/5/19.
 */
public class VRGameActivity extends AbsBaseActivity {
    private ImageView gameImage;
    private TextView gameName;
    private TextView gameType;
    private RatingBar ratingBar;
    private TextView install;
    private TextView downloadCount;
    private SlidingTabLayout mTab;
    private ViewPager mPager;
    private GameModel mGameModel;
    private String mID;
    private ImageLoader mImageLoader;
    private Context context;
    private StatisticsMode mStatisticsMode;
    private GameInfoBean.GameInfoEntity mGame;
    public static void openVRGameActivity(Context context, GameInfoBean.GameInfoEntity game) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("game",game);
        IntentUtils.openActivity(context, VRGameActivity.class, bundle);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vrgame);
        context = this;
        mGame = (GameInfoBean.GameInfoEntity) getIntent().getSerializableExtra("game");
        if(mGame == null){
            return;
        }
        mStatisticsMode = RetrofitUtils.create(StatisticsMode.class);
        mGameModel = RetrofitUtils.create(GameModel.class);
        gameImage = (ImageView) findViewById(R.id.game_iv_image);
        gameName = (TextView) findViewById(R.id.game_tv_name);
        gameType = (TextView) findViewById(R.id.game_type);
        ratingBar = (RatingBar) findViewById(R.id.rating_bar);
        install = (TextView) findViewById(R.id.install_text);
        downloadCount = (TextView) findViewById(R.id.download_count);
        mID = getIntent().getStringExtra("extra_game_id");
        mImageLoader = ImageLoaderUtils.getDefaultImageLoader(context);
        mPager = (ViewPager) findViewById(R.id.viewpager);
        mTab = (SlidingTabLayout) findViewById(R.id.tab_indicator);
        getData();

    }

    private void getData() {
        mGameModel.getGameInfo(mGame._id, 1, 20, 1, new DefaultLiveHttpCallBack<GameProgramBean>() {
            @Override
            public void success(GameProgramBean gameProgramBean) {
                mGame = gameProgramBean.game;
                mPagerAdapter = new VRGamePagerAdapter(getSupportFragmentManager(), new String[]{"详情", "评价"},mGame._id);
                mPager.setAdapter(mPagerAdapter);
                mPager.setCurrentItem(0);
                mTab.setViewPager(mPager);
                setParam();
            }

            @Override
            public void failure(String message) {

            }
        });

    }

    private void setParam() {
        HeadViewController.initHeadWithoutSearch(VRGameActivity.this,mGame.name);
        downloadCount.setText(String.valueOf(mGame.score_cnt));
        gameType.setText(mGame.gametypename);
        mImageLoader.displayImage(mGame.spic, gameImage);
        gameName.setText(mGame.name);
        if(mGame.package_name.equals("no")){
            install.setVisibility(View.GONE);
        }else{
            if (PackageUtils.hasInstalled(context, mGame.package_name)) {
                install.setText("打开");
            } else {
                install.setText("安装");
            }
        }
        install.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (PackageUtils.hasInstalled(context, mGame.package_name)) {
                    PackageUtils.openApplication(context, mGame.package_name);
                    //向服务器发送游戏启动
                    mStatisticsMode.gameAction(mGame._id, 2, new DefaultLiveHttpCallBack<String>() {
                        @Override
                        public void success(String s) {
                        }

                        @Override
                        public void failure(String message) {
                        }
                    });
                } else {
                    DownloadController controller = new DownloadController(context);
                    controller.startDownload(mGame.name + ".apk", mGame.url_android);
                    mGame.download++;
                    mStatisticsMode.gameAction(mGame._id, 1, new DefaultLiveHttpCallBack<String>() {
                        @Override
                        public void success(String s) {
                        }

                        @Override
                        public void failure(String message) {
                        }
                    });
                }
            }
        });
    }

    private VRGamePagerAdapter mPagerAdapter;
    private VRGameCommentFragment vrGameCommentFragment;
    public class VRGamePagerAdapter extends FragmentPagerAdapter {
        private String[] mTitles;
        private String mId;
        public VRGamePagerAdapter(FragmentManager fm,String[] titles,String id) {
            super(fm);
            this.mTitles = titles;
            this.mId = id;
        }

        @Override
        public Fragment getItem(int position) {
            switch (position){
                case 0:
                    return VRGameInfoFragment.getInstence(mGame);
                case 1:
                    if(vrGameCommentFragment == null)
                        vrGameCommentFragment = VRGameCommentFragment.getInstance(mId);
                    return vrGameCommentFragment;
            }
            return null;
        }

        @Override
        public int getCount() {
            return mTitles.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position){
                case 0:
                    return mTitles[0];
                case 1:
                    return mTitles[1];
                case 2:
                    return mTitles[2];
            }
            return super.getPageTitle(position);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 66 && resultCode == Activity.RESULT_OK && data != null){
            MessageBean.MessagesEntity message = (MessageBean.MessagesEntity) data.getSerializableExtra("message");
           if(vrGameCommentFragment != null)
               vrGameCommentFragment.setMessage(message);
        }
    }
}
