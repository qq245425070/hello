package cn.gietv.mlive.modules.usercenter.activity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;


import cn.gietv.mlive.R;
import cn.gietv.mlive.base.AbsBaseActivity;
import cn.gietv.mlive.http.DefaultLiveHttpCallBack;
import cn.gietv.mlive.modules.game.activity.GameActivity;
import cn.gietv.mlive.modules.home.model.NoviceTaskModel;
import cn.gietv.mlive.modules.login.activity.LoginActivity;
import cn.gietv.mlive.modules.usercenter.bean.TasksBean;
import cn.gietv.mlive.utils.CacheUtils;
import cn.gietv.mlive.utils.ConfigUtils;
import cn.gietv.mlive.utils.HeadViewController;
import cn.gietv.mlive.utils.IntentUtils;
import cn.gietv.mlive.utils.NoviceTaskUtils;
import cn.gietv.mlive.utils.UserUtils;
import cn.kalading.android.retrofit.utils.RetrofitUtils;

public class TaskActivity extends AbsBaseActivity implements View.OnClickListener{

    private Button mSign,mGive,mBarrage,mLiveShared,mGameDownload,mGiveGift,mRegist,mAnchor,mBinding,mSettingAvatar;
    private ScrollView mTaskParent;
    private TasksBean mTasksBean;
    private Context mContext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);
        HeadViewController.initTaskHeader(this, "金币任务");
        mContext = this;
        init();
    }

    private void init() {
        mSign = (Button) findViewById(R.id.sign);
        mGive = (Button) findViewById(R.id.give);
        mBarrage = (Button) findViewById(R.id.barrage);
        mLiveShared = (Button) findViewById(R.id.live_shared);
        mGameDownload = (Button) findViewById(R.id.game_download);
        mGiveGift = (Button) findViewById(R.id.give_gift);
        mRegist = (Button) findViewById(R.id.regist);
        mAnchor = (Button) findViewById(R.id.anchor);
        mBinding = (Button) findViewById(R.id.binding);
        mSettingAvatar = (Button) findViewById(R.id.setting_avatar);
        mTaskParent = (ScrollView) findViewById(R.id.task_parent);
        NoviceTaskModel model = RetrofitUtils.create(NoviceTaskModel.class);
        model.queryTask(new DefaultLiveHttpCallBack<TasksBean>() {
            @Override
            public void success(TasksBean s) {
                mTasksBean = s;
                initListener();
            }

            @Override
            public void failure(String message) {
                initListener();
            }
        });

    }
    private void isLogin(){
        if(UserUtils.isNotLogin()){
            IntentUtils.openActivity(this, LoginActivity.class);
        }
    }
    private void changeButtonGreen(Button button,String text){
        button.setBackgroundResource(R.drawable.commen_button_light_green_selected2);
        button.setTextColor(getResources().getColor(R.color.white));
        button.setText(text);
    }
    private void changeButtonWhite(Button button,String text){
        button.setBackgroundResource(R.drawable.commen_button_theme_color_greed);
        button.setTextColor(getResources().getColor(R.color.theme_green));
        button.setText(text);
    }
    private void initListener() {
        changeButtonGreen(mSign, "领取");
        mSign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(UserUtils.isNotLogin()){
                    IntentUtils.openActivity(mContext, LoginActivity.class);
                    return;
                }
                changeButtonGreen(mSign, "已领取");
                NoviceTaskUtils.doDayTask(ConfigUtils.TASK_DAY_LOGIN, TaskActivity.this);
            }
        });
        changeButtonWhite(mGive, "0/66");
        mGive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(UserUtils.isNotLogin()){
                    IntentUtils.openActivity(mContext, LoginActivity.class);
                    return;
                }
            }
        });
        changeButtonWhite(mBarrage, "0/5");
        mBarrage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isLogin();
            }
        });
        changeButtonGreen(mLiveShared, "未完成");
        mLiveShared.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isLogin();
            }
        });
        changeButtonWhite(mGameDownload, "详情");
        mGameDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(UserUtils.isNotLogin()){
                    IntentUtils.openActivity(mContext, LoginActivity.class);
                    return;
                }
                IntentUtils.openActivity(TaskActivity.this, GameActivity.class);
            }
        });
        changeButtonGreen(mGiveGift, "未完成");
        mGiveGift.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isLogin();
               /* Bundle bundle = new Bundle();
                bundle.putInt("position", 2);
                IntentUtils.openActivity(TaskActivity.this, HomeActivity.class, bundle);*/
            }
        });
        changeButtonGreen(mAnchor, "未完成");
        mAnchor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isLogin();
            }
        });
        changeButtonWhite(mBinding, "绑定");
        mBinding.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(UserUtils.isNotLogin()){
                    IntentUtils.openActivity(mContext, LoginActivity.class);
                    return;
                }
                IntentUtils.openActivity(TaskActivity.this, BindingPhoneActivity.class);
            }
        });
        if(!UserUtils.isNotLogin() && CacheUtils.getCacheUserInfo().mobilephone != null && !"".equals(CacheUtils.getCacheUserInfo().mobilephone)){
            changeButtonGreen(mBinding, "领取");
            mBinding.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    changeButtonGreen(mBinding,"已领取");
                    NoviceTaskUtils.doNoviceTask(ConfigUtils.TASK_NEW_PHONE, TaskActivity.this);
                    mBinding.setClickable(false);
                }
            });
        }
        changeButtonWhite(mSettingAvatar, "设置");
        mSettingAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(UserUtils.isNotLogin()){
                    IntentUtils.openActivity(mContext, LoginActivity.class);
                    return;
                }
                Bundle bundle = new Bundle();
                bundle.putSerializable(UserUpdateActivity.EXTRA_USER_INFO, CacheUtils.getCacheUserInfo());
                IntentUtils.openActivity(TaskActivity.this, UserUpdateActivity.class, bundle);
            }
        });
        if(!UserUtils.isNotLogin() && CacheUtils.getCacheUserInfo().avatar != null && !CacheUtils.getCacheUserInfo().avatar.contains("/huyu/app/user/userhead/22.jpg")){
            changeButtonGreen(mSettingAvatar,"领取");
            mSettingAvatar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    changeButtonGreen(mSettingAvatar,"已领取");
                    mSettingAvatar.setClickable(false);
                    NoviceTaskUtils.doNoviceTask(ConfigUtils.TASK_NEW_IMG,TaskActivity.this);
                }
            });
        }
        TasksBean.TaskBean bean = null;
        if(mTasksBean == null){
            return;
        }
        for(int i = 0;i<mTasksBean.taskqualifications.size();i++) {
            bean = mTasksBean.taskqualifications.get(i);
            switch (bean.taskcode) {
                case ConfigUtils.TASK_DAY_LOGIN:
                    if (bean.status == 1) {//表示有资格  1 可领取   0 已领取  -1没有资格领取
                        changeButtonGreen(mSign, "领取");
                        mSign.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                changeButtonGreen(mSign, "已领取");
                                NoviceTaskUtils.doDayTask(ConfigUtils.TASK_DAY_LOGIN, TaskActivity.this);
                                mSign.setClickable(false);
                            }
                        });
                    } else if(bean.status < 0){
                        changeButtonGreen(mSign, "已领取");
                    }
                    break;
                case ConfigUtils.TASK_DAY_FOLLOW_LIVE:
                    if(bean.status >= 1 ){
                        changeButtonGreen(mAnchor, "领取");
                        mAnchor.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                changeButtonGreen(mAnchor, "已领取");
                                NoviceTaskUtils.doNoviceTask(ConfigUtils.TASK_DAY_FOLLOW_LIVE, TaskActivity.this);
                                mAnchor.setClickable(false);
                            }
                        });
                    }else if(bean.status < 0){
                        changeButtonGreen(mAnchor, "已领取");
                        mAnchor.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                            }
                        });
                    }else if(bean.status == 0){
                        changeButtonWhite(mAnchor, "未完成");
                        mAnchor.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                            }
                        });
                    }
                    break;
                case ConfigUtils.TASK_DAY_GIVE_COIN_66:
                    if(bean.status >=1){
                        changeButtonGreen(mGive,"领取");
                        mGive.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                changeButtonGreen(mGive,"已领取");
                                NoviceTaskUtils.doDayTask(ConfigUtils.TASK_DAY_GIVE_COIN_66, TaskActivity.this);
                                mGive.setClickable(false);
                            }
                        });
                    }else if(bean.status == 0){
                        changeButtonGreen(mGive, "已领取");
                        mGive.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                            }
                        });
                    }else if(bean.status < 0){
                        changeButtonWhite(mGive, "0/66");
                        mGive.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                            }
                        });
                    }
                    break;
                case  ConfigUtils.TASK_JINJIAO://"GIVEJINJIAO":
                    if(bean.status > 0){
                        changeButtonGreen(mGiveGift,"领取");
                        mGiveGift.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                changeButtonGreen(mGiveGift,"已领取");
                                NoviceTaskUtils.doTask("GIVEJINJIAO",TaskActivity.this,0);
                                mGiveGift.setClickable(false);
                            }
                        });
                    }else{
                        changeButtonWhite(mGiveGift,"未赠送");
                        mGiveGift.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                            }
                        });
                    }
                    break;
                case ConfigUtils.TASK_DOWNLOAD_GAME:
                    if(bean.status >= 1){
                        changeButtonGreen(mGameDownload,"领取");
                        mGameDownload.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                changeButtonGreen(mGameDownload,"已领取");
                                NoviceTaskUtils.doTask(ConfigUtils.TASK_DOWNLOAD_GAME, TaskActivity.this, 0);
                                mGameDownload.setClickable(false);
                            }
                        });
                    }else{
                        changeButtonWhite(mGameDownload,"详情");
                        mGameDownload.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                IntentUtils.openActivity(TaskActivity.this, GameActivity.class);
                            }
                        });
                    }
                    break;
                case ConfigUtils.TASK_DAY_SEND_MSG:
                    if(bean.status >= 1){
                        changeButtonGreen(mBarrage,"领取");
                        mBarrage.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                changeButtonGreen(mBarrage,"已领取");
                                NoviceTaskUtils.doDayTask(ConfigUtils.TASK_DAY_SEND_MSG,TaskActivity.this);
                                mBarrage.setClickable(false);
                            }
                        });
                    }else if(bean.status == 0){
                        changeButtonGreen(mBarrage,"已领取");
                        mBarrage.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                            }
                        });
                    }else if(bean.status < 0){
                        changeButtonWhite(mBarrage,"0/5");
                        mBarrage.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                            }
                        });
                    }
                    break;
                case ConfigUtils.TASK_NEW_IMG:
                    if(bean.status >= 1){
                        changeButtonGreen(mSettingAvatar,"领取");
                        mSettingAvatar.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                changeButtonGreen(mSettingAvatar,"已领取");
                                NoviceTaskUtils.doNoviceTask(ConfigUtils.TASK_NEW_IMG,TaskActivity.this);
                                mSettingAvatar.setClickable(false);
                            }
                        });
                    }else if(bean.status < 0){
                        changeButtonGreen(mSettingAvatar,"已领取");
                        mSettingAvatar.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                            }
                        });
                    }
                    break;
                case ConfigUtils.TASK_NEW_PHONE:
                    if(bean.status >= 1){
                        changeButtonGreen(mBinding,"领取");
                        mBinding.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                changeButtonGreen(mBinding,"已领取");
                                mBinding.setClickable(false);
                                NoviceTaskUtils.doNoviceTask(ConfigUtils.TASK_NEW_PHONE,TaskActivity.this);
                            }
                        });
                    }else if(bean.status < 0){
                        changeButtonGreen(mBinding,"已领取");
                        mBinding.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                            }
                        });
                    }

                    break;
            }
        }

    }

    @Override
    public void onClick(View v) {
    }
      /* 任务系统改版
       int result =  NoviceTaskUtils.getTaskResult(ConfigUtils.TASK_DAY_LOGIN, TaskActivity.this);
        if(result == 1){
            mSign.setBackgroundResource(R.drawable.commen_button_light_green_selected2);
            mSign.setTextColor(getResources().getColor(R.color.white));
            mSign.setText("已领取");
        }else {
            mSign.setBackgroundResource(R.drawable.commen_button_light_green_selected2);
            mSign.setTextColor(getResources().getColor(R.color.white));
            mSign.setText("领取");
            mSign.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (UserUtils.isNotLogin()) {
                        IntentUtils.openActivity(TaskActivity.this, LoginActivity.class);
                    } else {
                        mSign.setBackgroundResource(R.drawable.commen_button_light_green_selected2);
                        mSign.setTextColor(getResources().getColor(R.color.white));
                        mSign.setText("已领取");
                        NoviceTaskUtils.doDayTask(ConfigUtils.TASK_DAY_LOGIN, TaskActivity.this);
                        mSign.setClickable(false);
                    }
                }
            });
        }
        result = NoviceTaskUtils.getTaskResult(ConfigUtils.TASK_DAY_SEND_MSG,TaskActivity.this);
        if (result == 1) {
            mBarrage.setBackgroundResource(R.drawable.commen_button_light_green_selected2);
            mBarrage.setTextColor(getResources().getColor(R.color.white));
            mBarrage.setText("已领取");
        }else{
            long count = NoviceTaskUtils.getDbData(ConfigUtils.TASK_DAY_SEND_MSG,TaskActivity.this);
            if(count >= 5){
                mBarrage.setBackgroundResource(R.drawable.commen_button_light_green_selected2);
                mBarrage.setTextColor(getResources().getColor(R.color.white));
                mBarrage.setText("领取");
                mBarrage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (UserUtils.isNotLogin()) {
                            IntentUtils.openActivity(TaskActivity.this, LoginActivity.class);
                        } else {
                            mBarrage.setBackgroundResource(R.drawable.commen_button_light_green_selected2);
                            mBarrage.setTextColor(getResources().getColor(R.color.white));
                            mBarrage.setText("已领取");
                            NoviceTaskUtils.doDayTask(ConfigUtils.TASK_DAY_SEND_MSG, TaskActivity.this);
                            mBarrage.setClickable(false);
                        }
                    }
                });
            }else {
                mBarrage.setBackgroundResource(R.drawable.commen_button_theme_color_greed);
                mBarrage.setTextColor(getResources().getColor(R.color.theme_green));
                mBarrage.setText(count + "/5");
            }

        }
        result = NoviceTaskUtils.getTaskResult(ConfigUtils.TASK_DAY_GIVE_COIN_66, TaskActivity.this);
        if (result == 1) {
            mGive.setBackgroundResource(R.drawable.commen_button_light_green_selected2);
            mGive.setTextColor(getResources().getColor(R.color.white));
            mGive.setText("已领取");
        }else{
            long count = NoviceTaskUtils.getDbData(ConfigUtils.TASK_DAY_GIVE_COIN_66,TaskActivity.this);
            if(count >= 66){
                mGive.setBackgroundResource(R.drawable.commen_button_light_green_selected2);
                mGive.setTextColor(getResources().getColor(R.color.white));
                mGive.setText("领取");
                mGive.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (UserUtils.isNotLogin()) {
                            IntentUtils.openActivity(TaskActivity.this, LoginActivity.class);
                        } else {
                            mGive.setBackgroundResource(R.drawable.commen_button_light_green_selected2);
                            mGive.setTextColor(getResources().getColor(R.color.white));
                            mGive.setText("已领取");
                            NoviceTaskUtils.doDayTask(ConfigUtils.TASK_DAY_GIVE_COIN_66, TaskActivity.this);
                            mGive.setClickable(false);
                        }
                    }
                });
            }else {
                mGive.setBackgroundResource(R.drawable.commen_button_theme_color_greed);
                mGive.setTextColor(getResources().getColor(R.color.theme_green));
                mGive.setText(count + "/66");
            }
        }
        result = NoviceTaskUtils.getTaskResult(ConfigUtils.TASK_SHARE_LIVE, TaskActivity.this);
        if (result == 1 || result == -1) {
            mLiveShared.setBackgroundResource(R.drawable.commen_button_light_green_selected2);
            mLiveShared.setTextColor(getResources().getColor(R.color.white));
            mLiveShared.setText("已领取");
        }else{
            mLiveShared.setBackgroundResource(R.drawable.commen_button_light_green_selected2);
            mLiveShared.setTextColor(getResources().getColor(R.color.white));
            mLiveShared.setText("领取");
            mLiveShared.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (UserUtils.isNotLogin()) {
                        IntentUtils.openActivity(TaskActivity.this, LoginActivity.class);
                    } else {
                        int count = NoviceTaskUtils.getTaskCount(ConfigUtils.TASK_SHARE_LIVE, TaskActivity.this);
                        if(--count > 0){
                            mLiveShared.setBackgroundResource(R.drawable.commen_button_light_green_selected2);
                            mLiveShared.setTextColor(getResources().getColor(R.color.white));
                            mLiveShared.setText("领取");
                            NoviceTaskUtils.doTask(ConfigUtils.TASK_SHARE_LIVE,TaskActivity.this,count);
                        }else {
                            mLiveShared.setBackgroundResource(R.drawable.commen_button_light_green_selected2);
                            mLiveShared.setTextColor(getResources().getColor(R.color.white));
                            mLiveShared.setText("已领取");
                        }
                    }
                }
            });
        }
        result = NoviceTaskUtils.getTaskCount(ConfigUtils.TASK_DOWNLOAD_GAME, TaskActivity.this);
        if (result == 0 || result ==-1) {
            mGameDownload.setBackgroundResource(R.drawable.commen_button_theme_color_greed);
            mGameDownload.setTextColor(getResources().getColor(R.color.theme_green));
            mGameDownload.setText("详情");
            mGameDownload.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle = new Bundle();
                    bundle.putInt("position", 1);
                    IntentUtils.openActivity(TaskActivity.this, HomeActivity.class, bundle);
                }
            });
        }else if(result > 0){
            mGameDownload.setBackgroundResource(R.drawable.commen_button_light_green_selected2);
            mGameDownload.setTextColor(getResources().getColor(R.color.white));
            mGameDownload.setText("领取");
            mGameDownload.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (UserUtils.isNotLogin()) {
                        IntentUtils.openActivity(TaskActivity.this, LoginActivity.class);
                    } else {
                        int count = NoviceTaskUtils.getTaskCount(ConfigUtils.TASK_DOWNLOAD_GAME, TaskActivity.this);
                        --count;
                        if(count > 0){
                            mGameDownload.setBackgroundResource(R.drawable.commen_button_light_green_selected2);
                            mGameDownload.setTextColor(getResources().getColor(R.color.white));
                            mGameDownload.setText("领取");
                        }else {
                            mGameDownload.setBackgroundResource(R.drawable.commen_button_light_green_selected2);
                            mGameDownload.setTextColor(getResources().getColor(R.color.white));
                            mGameDownload.setText("已领取");
                            mGameDownload.setClickable(false);
                        }
                        System.out.println(count);
                        NoviceTaskUtils.doTask(ConfigUtils.TASK_DOWNLOAD_GAME,TaskActivity.this,count);
                    }
                }
            });
        }
        result = NoviceTaskUtils.getTaskResult(ConfigUtils.TASK_DAY_FOLLOW_LIVE, TaskActivity.this);
        if(result == 1 || result == -1){
            mAnchor.setBackgroundResource(R.drawable.commen_button_light_green_selected2);
            mAnchor.setTextColor(getResources().getColor(R.color.white));
            mAnchor.setText("已领取");
        }else {
            mAnchor.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (UserUtils.isNotLogin()) {
                        IntentUtils.openActivity(TaskActivity.this, LoginActivity.class);
                    } else {
                        mAnchor.setBackgroundResource(R.drawable.commen_button_light_green_selected2);
                        mAnchor.setTextColor(getResources().getColor(R.color.white));
                        mAnchor.setText("已领取");
                        NoviceTaskUtils.doDayTask(ConfigUtils.TASK_DAY_FOLLOW_LIVE, TaskActivity.this);
                    }
                }
            });
        }
        result = NoviceTaskUtils.getNewTaskData(ConfigUtils.TASK_NEW_PHONE, TaskActivity.this);
        if(result == 1){
            mBinding.setBackgroundResource(R.drawable.commen_button_light_green_selected2);
            mBinding.setTextColor(getResources().getColor(R.color.white));
            mBinding.setText("已领取");
        }else {
            result = NoviceTaskUtils.getNewTaskData(ConfigUtils.TASK_NEW_PHONE, TaskActivity.this);
            if (result == -1) {
                if(!UserUtils.isNotLogin() && CacheUtils.getCacheUserInfo().mobilephone != null && !"".equals(CacheUtils.getCacheUserInfo().mobilephone)){
                    mBinding.setBackgroundResource(R.drawable.commen_button_light_green_selected2);
                    mBinding.setTextColor(getResources().getColor(R.color.white));
                    mBinding.setText("领取");
                    mBinding.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (UserUtils.isNotLogin()) {
                                IntentUtils.openActivity(TaskActivity.this, LoginActivity.class);
                            } else {
                                NoviceTaskUtils.doNoviceTask(ConfigUtils.TASK_NEW_PHONE, TaskActivity.this);
                                mBinding.setBackgroundResource(R.drawable.commen_button_light_green_selected2);
                                mBinding.setTextColor(getResources().getColor(R.color.white));
                                mBinding.setText("已领取");
                            }
                        }
                    });
                }else{
                mBinding.setBackgroundResource(R.drawable.commen_button_theme_color_greed);
                mBinding.setTextColor(getResources().getColor(R.color.theme_green));
                mBinding.setText("绑定");
                mBinding.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (UserUtils.isNotLogin()) {
                            IntentUtils.openActivity(TaskActivity.this, LoginActivity.class);
                        } else {
                            IntentUtils.openActivity(TaskActivity.this, BindingPhoneActivity.class);
                            finish();
                        }
                    }
                });
            }} else {
                mBinding.setBackgroundResource(R.drawable.commen_button_light_green_selected2);
                mBinding.setTextColor(getResources().getColor(R.color.white));
                mBinding.setText("领取");
                mBinding.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (UserUtils.isNotLogin()) {
                            IntentUtils.openActivity(TaskActivity.this, LoginActivity.class);
                        } else {
                            NoviceTaskUtils.doNoviceTask(ConfigUtils.TASK_NEW_PHONE, TaskActivity.this);
                            mBinding.setBackgroundResource(R.drawable.commen_button_light_green_selected2);
                            mBinding.setTextColor(getResources().getColor(R.color.white));
                            mBinding.setText("已领取");
                        }
                    }
                });
            }
        }
        result = NoviceTaskUtils.getNewTaskData(ConfigUtils.TASK_NEW_IMG, TaskActivity.this);
        if(result == 1){
            mSettingAvatar.setBackgroundResource(R.drawable.commen_button_light_green_selected2);
            mSettingAvatar.setTextColor(getResources().getColor(R.color.white));
            mSettingAvatar.setText("已领取");
        }else if (result == -1) {
            if(CacheUtils.getCacheUserInfo() != null && CacheUtils.getCacheUserInfo().avatar.contains("/huyu/app/user/userhead/22.jpg")) {
                mSettingAvatar.setBackgroundResource(R.drawable.commen_button_theme_color_greed);
                mSettingAvatar.setTextColor(getResources().getColor(R.color.theme_green));
                mSettingAvatar.setText("设置");
                mSettingAvatar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (UserUtils.isNotLogin()) {
                            IntentUtils.openActivity(TaskActivity.this, LoginActivity.class);
                        } else {
                            Bundle bundle = new Bundle();
                            bundle.putSerializable(UserUpdateActivity.EXTRA_USER_INFO, CacheUtils.getCacheUserInfo());
                            IntentUtils.openActivity(TaskActivity.this, UserUpdateActivity.class, bundle);
                            finish();
                        }
                    }
                });
            }else{
                mSettingAvatar.setBackgroundResource(R.drawable.commen_button_light_green_selected2);
                mSettingAvatar.setTextColor(getResources().getColor(R.color.white));
                mSettingAvatar.setText("领取");
                mSettingAvatar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (UserUtils.isNotLogin()) {
                            IntentUtils.openActivity(TaskActivity.this, LoginActivity.class);
                        } else {
                            NoviceTaskUtils.doNoviceTask(ConfigUtils.TASK_NEW_IMG, TaskActivity.this);
                            mSettingAvatar.setBackgroundResource(R.drawable.commen_button_light_green_selected2);
                            mSettingAvatar.setTextColor(getResources().getColor(R.color.white));
                            mSettingAvatar.setText("已领取");
                        }
                    }
                });
            }
            } else {
                mSettingAvatar.setBackgroundResource(R.drawable.commen_button_light_green_selected2);
                mSettingAvatar.setTextColor(getResources().getColor(R.color.white));
                mSettingAvatar.setText("领取");
                mSettingAvatar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (UserUtils.isNotLogin()) {
                            IntentUtils.openActivity(TaskActivity.this, LoginActivity.class);
                        } else {
                            NoviceTaskUtils.doNoviceTask(ConfigUtils.TASK_NEW_IMG, TaskActivity.this);
                            mSettingAvatar.setBackgroundResource(R.drawable.commen_button_light_green_selected2);
                            mSettingAvatar.setTextColor(getResources().getColor(R.color.white));
                            mSettingAvatar.setText("已领取");
                        }
                    }
                });
            }
       if(NoviceTaskUtils.getTaskCount(ConfigUtils.TASK_DAY_GIVE_JINJIAO_199,this) > 0 ||
               NoviceTaskUtils.getTaskCount(ConfigUtils.TASK_DAY_GIVE_JINJIAO_99,this) > 0 ||
               NoviceTaskUtils.getTaskCount(ConfigUtils.TASK_DAY_GIVE_JINJIAO_49,this) > 0 ||
               NoviceTaskUtils.getTaskCount(ConfigUtils.TASK_DAY_GIVE_JINJIAO_5,this) > 0){
               mGiveGift.setBackgroundResource(R.drawable.commen_button_light_green_selected2);
               mGiveGift.setTextColor(getResources().getColor(R.color.white));
               mGiveGift.setText("领取");
               mGiveGift.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View v) {
                       if (UserUtils.isNotLogin()) {
                           IntentUtils.openActivity(TaskActivity.this, LoginActivity.class);
                       } else {
                           int count = NoviceTaskUtils.getTaskCount(ConfigUtils.TASK_DAY_GIVE_JINJIAO_199, TaskActivity.this);
                           if (count > 0) {
                               --count;
                               NoviceTaskUtils.doTask(ConfigUtils.TASK_DAY_GIVE_JINJIAO_199, TaskActivity.this, count);
                               if(count > 0){
                                   mGiveGift.setText("领取");
                               }else{
                                   mGiveGift.setText("已领取");
                                   if(NoviceTaskUtils.getTaskCount(ConfigUtils.TASK_DAY_GIVE_JINJIAO_99,TaskActivity.this) <= 0 &&
                                           NoviceTaskUtils.getTaskCount(ConfigUtils.TASK_DAY_GIVE_JINJIAO_49,TaskActivity.this) <= 0 &&
                                           NoviceTaskUtils.getTaskCount(ConfigUtils.TASK_DAY_GIVE_JINJIAO_5,TaskActivity.this) <= 0){
                                       mGiveGift.setClickable(false);
                                   }else{
                                       mGiveGift.setText("领取");
                                       return;
                                   }
                               }
                           } else {
                               mGiveGift.setText("已领取");
                           }
                           count = NoviceTaskUtils.getTaskCount(ConfigUtils.TASK_DAY_GIVE_JINJIAO_99, TaskActivity.this);
                           if (count > 0) {
                               --count;
                               NoviceTaskUtils.doTask(ConfigUtils.TASK_DAY_GIVE_JINJIAO_99, TaskActivity.this, count);
                               if(count > 0){
                                   mGiveGift.setText("领取");
                               }else{
                                   mGiveGift.setText("已领取");
                                   if(NoviceTaskUtils.getTaskCount(ConfigUtils.TASK_DAY_GIVE_JINJIAO_199,TaskActivity.this) <= 0 &&
                                           NoviceTaskUtils.getTaskCount(ConfigUtils.TASK_DAY_GIVE_JINJIAO_49,TaskActivity.this) <= 0 &&
                                           NoviceTaskUtils.getTaskCount(ConfigUtils.TASK_DAY_GIVE_JINJIAO_5,TaskActivity.this) <= 0){
                                       mGiveGift.setClickable(false);
                                   }else{
                                       mGiveGift.setText("领取");
                                       return;
                                   }
                               }
                           } else {
                               mGiveGift.setText("已领取");
                           }
                           count = NoviceTaskUtils.getTaskCount(ConfigUtils.TASK_DAY_GIVE_JINJIAO_49, TaskActivity.this);
                           if (count > 0) {
                               --count;
                               NoviceTaskUtils.doTask(ConfigUtils.TASK_DAY_GIVE_JINJIAO_49, TaskActivity.this, count);
                               if(count > 0){
                                   mGiveGift.setText("领取");
                               }else{
                                   mGiveGift.setText("已领取");
                                   if(NoviceTaskUtils.getTaskCount(ConfigUtils.TASK_DAY_GIVE_JINJIAO_199,TaskActivity.this) <= 0 &&
                                           NoviceTaskUtils.getTaskCount(ConfigUtils.TASK_DAY_GIVE_JINJIAO_99,TaskActivity.this) <= 0 &&
                                           NoviceTaskUtils.getTaskCount(ConfigUtils.TASK_DAY_GIVE_JINJIAO_5,TaskActivity.this) <= 0){
                                       mGiveGift.setClickable(false);
                                   }else{
                                       mGiveGift.setText("领取");
                                       return;
                                   }
                               }
                           } else {
                               mGiveGift.setText("已领取");
                           }
                           count = NoviceTaskUtils.getTaskCount(ConfigUtils.TASK_DAY_GIVE_JINJIAO_5, TaskActivity.this);
                           if (count > 0) {
                               --count;
                               NoviceTaskUtils.doTask(ConfigUtils.TASK_DAY_GIVE_JINJIAO_5, TaskActivity.this, count);
                               if(count > 0){
                                   mGiveGift.setText("领取");
                               }else{
                                   mGiveGift.setText("已领取");
                                   if(NoviceTaskUtils.getTaskCount(ConfigUtils.TASK_DAY_GIVE_JINJIAO_99,TaskActivity.this) <= 0 &&
                                           NoviceTaskUtils.getTaskCount(ConfigUtils.TASK_DAY_GIVE_JINJIAO_49,TaskActivity.this) <= 0 &&
                                           NoviceTaskUtils.getTaskCount(ConfigUtils.TASK_DAY_GIVE_JINJIAO_199,TaskActivity.this) <= 0){
                                       mGiveGift.setClickable(false);
                                   }else{
                                       mGiveGift.setText("领取");
                                       return;
                                   }
                               }
                           } else {
                               mGiveGift.setText("已领取");
                           }
                       }
                   }
               });
       }else{
           mGiveGift.setBackgroundResource(R.drawable.commen_button_light_green_selected2);
           mGiveGift.setTextColor(getResources().getColor(R.color.white));
           mGiveGift.setText("未赠送");
       }*/
}
