package cn.gietv.mlive.modules.setting.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.umeng.message.IUmengCallback;
import com.umeng.message.PushAgent;

import cn.gietv.mlive.MainApplication;
import cn.gietv.mlive.R;
import cn.gietv.mlive.base.AbsBaseActivity;
import cn.gietv.mlive.constants.CacheConstants;
import cn.gietv.mlive.constants.HttpConstants;
import cn.gietv.mlive.db.DBUtils;
import cn.gietv.mlive.modules.aboutus.activity.WebActivity;
import cn.gietv.mlive.modules.follow.fragment.FollowContentFragment;
import cn.gietv.mlive.modules.usercenter.bean.UserCenterBean;
import cn.gietv.mlive.modules.xmpp.XmppUtils;
import cn.gietv.mlive.utils.CacheController;
import cn.gietv.mlive.utils.CacheUtils;
import cn.gietv.mlive.utils.HeadViewController;
import cn.gietv.mlive.utils.IntentUtils;
import cn.gietv.mlive.utils.SharedPreferenceUtils;
import cn.gietv.mlive.utils.ToastUtils;
import cn.gietv.mlive.utils.UpdataController;
import cn.gietv.mlive.utils.UserUtils;
import cn.kalading.android.retrofit.utils.RetrofitUtils;

/**
 * author：steven
 * datetime：15/10/11 21:42
 */
public class SettingActivity extends AbsBaseActivity implements View.OnClickListener {
    private TextView mExitText;
    private TextView mUpdateText;
    private TextView mAboutusText;
    private TextView mDataText;
    private TextView mTuCaoText;
    private CheckBox mConcernMe,mMessage,mNotification,mHaveGood;
    private UpdataController mUpdataController;
    private RelativeLayout mClearDataParent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_layout);
        initView();
    }

    private void initView() {
        HeadViewController.initHeadWithoutSearch(this, "系统设置");
        mExitText = (TextView) findViewById(R.id.setting_tv_exit);
        mAboutusText = (TextView) findViewById(R.id.setting_tv_aboutus);
        mAboutusText.setOnClickListener(this);
        mClearDataParent = (RelativeLayout) findViewById(R.id.clear_data_parent);
        mClearDataParent.setOnClickListener(this);
        mDataText = (TextView) findViewById(R.id.data_text);
        mTuCaoText = (TextView) findViewById(R.id.tucao_tv_update);
        mTuCaoText.setOnClickListener(this);
       // mDataText.setText(CacheController.getCacheSize(this, new File("file:///data/data/cn.gietv.mlive/cache/")));
        mDataText.setText(CacheController.getCacheSize(this, this.getExternalCacheDir()));
        mUpdateText = (TextView) findViewById(R.id.setting_tv_update);
        mConcernMe = (CheckBox) findViewById(R.id.setting_tv_have_attention);
        mMessage = (CheckBox) findViewById(R.id.setting_tv_have_message);
        mNotification = (CheckBox) findViewById(R.id.setting_tv_have_notification);
        mHaveGood = (CheckBox) findViewById(R.id.setting_tv_have_good);
        mConcernMe.setChecked(true);
        mMessage.setChecked(true);
        mNotification.setChecked(true);
        final PushAgent mPushAgent = PushAgent.getInstance(this);
        mHaveGood.setChecked(SharedPreferenceUtils.getBoolean("system_message_swith",true));
        mHaveGood.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    mPushAgent.enable(new IUmengCallback() {
                        @Override
                        public void onSuccess() {
                            SharedPreferenceUtils.saveProp("system_message_swith",true);
                        }

                        @Override
                        public void onFailure(String s, String s1) {

                        }
                    });

                }else{
                    mPushAgent.disable(new IUmengCallback() {
                        @Override
                        public void onSuccess() {
                            SharedPreferenceUtils.saveProp("system_message_swith",false);
                        }

                        @Override
                        public void onFailure(String s, String s1) {

                        }
                    });

                }
            }
        });
        if (UserUtils.isNotLogin()) {
            mExitText.setVisibility(View.GONE);
        }
        mExitText.setOnClickListener(this);
        mUpdateText.setOnClickListener(this);
        mUpdataController = new UpdataController(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.setting_tv_exit) {
            showDialog();
            DBUtils.getInstance(this).deleteUser(CacheUtils.getCache().getAsString(CacheConstants.CACHE_USERID));
            CacheUtils.getCache().put(CacheConstants.CACHE_USERID, HttpConstants.USER_ID_NONE);
            CacheUtils.getCache().put(CacheConstants.CACHE_TOKEN, HttpConstants.TOKEN_NONE);
            RetrofitUtils.addHeader(HttpConstants.HEAD_USER_ID, HttpConstants.USER_ID_NONE);
            RetrofitUtils.addHeader(HttpConstants.HEAD_TOKEN, HttpConstants.TOKEN_NONE);
            ToastUtils.showToast(this, "您已退出登录");
            if (mDialog.isShowing()) {
                mDialog.dismiss();
            }
            CacheUtils.saveUserInfo(null);
            XmppUtils.getInstance().reconnection(this);
            changeFollowPager();
            finish();
        }else if(view.getId()== R.id.setting_tv_update) {
            mUpdataController.checkUpdate();
        }else if(view.getId() == R.id.setting_tv_aboutus) {
            IntentUtils.openActivity(this, WebActivity.class);
        }else if(view.getId() == R.id.clear_data_parent) {
            UserCenterBean.UserinfoEntity userinfo = CacheUtils.getCacheUserInfo();
            CacheController.deleteFilesByDirectory(this.getExternalCacheDir());
            CacheUtils.saveUserInfo(userinfo);
            mDataText.setText("0 KB");
        }else if(R.id.tucao_tv_update == view.getId()){
                IntentUtils.openActivity(this,TuCaoActivity.class);
        }
    }
    private void changeFollowPager(){
        FollowContentFragment followContentFragment =  (FollowContentFragment) MainApplication.getInstance().getHomeActivity().getSupportFragmentManager().findFragmentByTag("订阅");
        if(followContentFragment != null)
            followContentFragment.getData();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mUpdataController.destory();
    }
    private Dialog mDialog;
    private void showDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("正在注销请稍后");
        builder.setView(new ProgressBar(this));
        mDialog = builder.create();
        mDialog.show();
    }
}
