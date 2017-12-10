package cn.gietv.mlive.modules.login.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Map;

import cn.gietv.mlive.MainApplication;
import cn.gietv.mlive.R;
import cn.gietv.mlive.base.AbsBaseActivity;
import cn.gietv.mlive.constants.CacheConstants;
import cn.gietv.mlive.constants.CommConstants;
import cn.gietv.mlive.constants.HttpConstants;
import cn.gietv.mlive.db.DBUtils;
import cn.gietv.mlive.http.DefaultLiveHttpCallBack;
import cn.gietv.mlive.modules.compere.bean.CompereListBean;
import cn.gietv.mlive.modules.compere.model.CompereModel;
import cn.gietv.mlive.modules.follow.fragment.FollowContentFragment;
import cn.gietv.mlive.modules.follow.model.FollowModel;
import cn.gietv.mlive.modules.game.bean.GameInfoBean;
import cn.gietv.mlive.modules.login.bean.UserInfo;
import cn.gietv.mlive.modules.login.model.LoginModel;
import cn.gietv.mlive.modules.statistics.StatisticsMode;
import cn.gietv.mlive.modules.xmpp.XmppUtils;
import cn.gietv.mlive.utils.CacheUtils;
import cn.gietv.mlive.utils.ConfigUtils;
import cn.gietv.mlive.utils.DensityUtil;
import cn.gietv.mlive.utils.HeadViewController;
import cn.gietv.mlive.utils.IntentUtils;
import cn.gietv.mlive.utils.NotificationTitleBarUtils;
import cn.gietv.mlive.utils.SharedPreferenceUtils;
import cn.gietv.mlive.utils.ToastUtils;
import cn.gietv.mlive.views.ShowUidPopuWindow;
import cn.kalading.android.retrofit.utils.RetrofitUtils;

/**
 * author：steven
 * datetime：15/9/17 11:13
 */
public class LoginActivity extends AbsBaseActivity implements View.OnClickListener {
    private EditText mUsernameEdit, mPasswordEdit;
    private ImageView mWeixinButton, mWeiboButton, mQQButton;
    private TextView mRegistText, mForgetPwd, mLoginButton;
    private final int WEIXIN = 4;
    private final int QQ = 5;
    private final int SINA = 6;
    private UMShareAPI mShareAPI ;
    private int userType;
    private int[] location;
    private ShowUidPopuWindow uidPopuWindow;
    private List<String> mPopuWindowData;
    private LinearLayout mUserParent;
    private int mWidth;
    private void initViews() {
        mShareAPI = UMShareAPI.get(MainApplication.getInstance());
        HeadViewController.initHeadWithoutSearch(this, "登录");
        mUsernameEdit = (EditText) findViewById(R.id.login_et_username);
        mPasswordEdit = (EditText) findViewById(R.id.login_et_password);
        mLoginButton = (TextView) findViewById(R.id.login_btn_login);
        mQQButton = (ImageView) findViewById(R.id.login_btn_qq);
        mWeixinButton = (ImageView) findViewById(R.id.login_btn_weixin);
        mWeiboButton = (ImageView) findViewById(R.id.login_btn_weibo);
        mRegistText = (TextView) findViewById(R.id.login_tv_regist);
        mForgetPwd = (TextView) findViewById(R.id.login_tv_find_password);
        mUserParent = (LinearLayout) findViewById(R.id.username_parent);

        mLoginButton.setOnClickListener(this);
        mWeixinButton.setOnClickListener(this);
        mQQButton.setOnClickListener(this);
        mWeiboButton.setOnClickListener(this);
        mRegistText.setOnClickListener(this);
        mForgetPwd.setOnClickListener(this);
        mForgetPwd.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                location = new int[2];
                mUsernameEdit.getLocationOnScreen(location);
                mWidth = mUsernameEdit.getWidth();
                mForgetPwd.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });
        mUsernameEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mPopuWindowData = DBUtils.getInstance(LoginActivity.this).getUid(s.toString());
                if (mPopuWindowData != null) {
                    if (uidPopuWindow == null) {
                        uidPopuWindow = new ShowUidPopuWindow(LoginActivity.this, mPopuWindowData);
                        uidPopuWindow.setWidth(mWidth);
                        uidPopuWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
                        uidPopuWindow.setOutsideTouchable(true);
                        uidPopuWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        uidPopuWindow.setItemOnClickListener(new ShowUidPopuWindow.ItemOnClickListener() {
                            @Override
                            public void itemOnClick(String uid) {
                                mUsernameEdit.setText(uid);
                                mPasswordEdit.setText(DBUtils.getInstance(LoginActivity.this).getPassword(uid));
                                uidPopuWindow.dismiss();
                            }
                        });
                        //uidPopuWindow.setFocusable(true);
                    } else {
                        uidPopuWindow.setData(mPopuWindowData);
                    }
                    uidPopuWindow.showAtLocation(mUserParent, Gravity.NO_GRAVITY, location[0], location[1] + DensityUtil.dip2px(LoginActivity.this, 40));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        mUsernameEdit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    String uid = mUsernameEdit.getText().toString();
                    if(TextUtils.isEmpty(uid)){
                        return;
                    }
                    mPasswordEdit.setText(DBUtils.getInstance(LoginActivity.this).getPassword(uid));
                }
            }
        });
    }

    //向服务器发送设备信息
    private void sendDevice() {
        StatisticsMode statisticsMode = RetrofitUtils.create(StatisticsMode.class);
        statisticsMode.sendDevice(new DefaultLiveHttpCallBack<String>() {
            @Override
            public void success(String s) {

            }

            @Override
            public void failure(String message) {

            }
        });
    }

    private void login() {
        LoginModel loginModel = RetrofitUtils.create(LoginModel.class);
        if (TextUtils.isEmpty(mUsernameEdit.getText())) {
            mUsernameEdit.setHintTextColor(Color.RED);
            mUsernameEdit.setHint("用户名不能为空");
            return;
        }
        if (TextUtils.isEmpty(mPasswordEdit.getText())) {
            mPasswordEdit.setHintTextColor(Color.RED);
            mPasswordEdit.setHint("密码不能为空");
            return;
        }
        loginModel.login(mUsernameEdit.getText().toString(), mPasswordEdit.getText().toString(), new DefaultLiveHttpCallBack<UserInfo>() {
            @Override
            public void success(UserInfo userInfo) {
                if (isNotFinish()) {
                    if (userInfo == null || userInfo.userinfo == null) {
                        return;
                    }
                    //向数据库中保存登录的用户名
                    DBUtils.getInstance(LoginActivity.this).saveNickname(userInfo.userinfo._id, userInfo.userinfo.nickname, userInfo.token,mPasswordEdit.getText().toString());
                    cacheSaveUserinfo(userInfo);
                    sendDevice();
                    getMqttService();

                    boolean isFirst = SharedPreferenceUtils.getBoolean(ConfigUtils.IS_FIRSTER_LOGIN + userInfo.userinfo._id, true);
                    if (isFirst) {
                        SharedPreferenceUtils.saveProp(ConfigUtils.IS_FIRSTER_LOGIN + userInfo.userinfo._id, false);
//                        saveAttentionAnchorList(userInfo.userinfo._id);
                        //向服务器发送本地未登录关注的专区
                        sendFollowArea();
                    }
                    changeFollowPager();
                    finish();
                }
            }

            @Override
            public void failure(String message) {
                if (isNotFinish()) {
                    ToastUtils.showToast(LoginActivity.this, message);
                }
            }
        });
    }

    private void sendFollowArea() {
        List<GameInfoBean.GameInfoEntity> games = DBUtils.getInstance(this).getAllArea();
        GameInfoBean.GameInfoEntity game;
        if(games != null && games.size() > 0) {
            StringBuilder sb = new StringBuilder();
            for(int i = 0; i <games.size();i++){
                game = games.get(i);
                sb.append(game._id).append("#");
            }
            String ids = sb.toString();
            if(ids.length()>0)
                ids = ids.substring(0,ids.length()-1);
            FollowModel followModel = RetrofitUtils.create(FollowModel.class);
            Log.e("ceshi","发送关注数据");
            followModel.followList(ids , CommConstants.FOLLOW_TYPE_AREA, CommConstants.FOLLOW_TRUE, new DefaultLiveHttpCallBack<String>() {
                @Override
                public void success(String s) {
                    changeFollowPager();
                }

                @Override
                public void failure(String message) {
                    changeFollowPager();
                }
            });
        }
    }

    public void saveAttentionAnchorList(String userid) {
        CompereModel mCompereModel = RetrofitUtils.create(CompereModel.class);
        mCompereModel.getAttentionCompere(userid, 10000, 1, ConfigUtils.QUERY_ATTENTION_COMPERE, 1, new DefaultLiveHttpCallBack<CompereListBean>() {
            @Override
            public void success(final CompereListBean compereListBean) {
                //SharedPreferenceUtils.saveProp(ConfigUtils.IS_FIRSTER_LOGIN,false);
                new Thread() {
                    @Override
                    public void run() {
                        for (int i = 0; i < compereListBean.users.size(); i++) {
                            DBUtils.getInstance(MainApplication.getInstance()).saveAttentionAnchor(compereListBean.users.get(i)._id, 1);
                        }
                    }
                }.start();
            }

            @Override
            public void failure(String message) {

            }
        });
    }

    private void cacheSaveUserinfo(UserInfo userInfo) {
        if(userInfo != null) {
            DBUtils.getInstance(this).saveUser(userInfo);
            CacheUtils.saveUserInfo(userInfo.userinfo);
            CacheUtils.getCache().put(CacheConstants.CACHE_USERID, userInfo.userinfo._id);
            CacheUtils.getCache().put(CacheConstants.CACHE_TOKEN, userInfo.token);
            RetrofitUtils.addHeader(HttpConstants.HEAD_TOKEN, userInfo.token);
            RetrofitUtils.addHeader(HttpConstants.HEAD_USER_ID, userInfo.userinfo._id);
            ToastUtils.showToast(LoginActivity.this, "登录成功");
        }
    }

    private void getMqttService() {
        XmppUtils.getInstance().reconnection(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        NotificationTitleBarUtils.setGreenNotification(this);
        setContentView(R.layout.login_layout);
        MainApplication.getInstance().addActivityList(this);
        initViews();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mShareAPI.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.login_btn_login) {
            login();
        } else if (view.getId() == R.id.login_btn_qq) {
            login(SHARE_MEDIA.QQ);
            userType = QQ;
        } else if (view.getId() == R.id.login_btn_weixin) {
            login(SHARE_MEDIA.WEIXIN);
            userType = WEIXIN;
        } else if (view.getId() == R.id.login_btn_weibo) {
            login(SHARE_MEDIA.SINA);
            userType = SINA;
        } else if (view.getId() == R.id.login_tv_regist) {
            IntentUtils.openActivity(this, RegistActivity2.class);
        } else if (view.getId() == R.id.login_tv_find_password) {
            IntentUtils.openActivity(this, ForgetPasswordActivity.class);
        }
    }

    /**
     * 授权。如果授权成功，则获取用户信息</br>
     */
    private void login(final SHARE_MEDIA platform) {
        mShareAPI.doOauthVerify(this, platform, new UMAuthListener() {
            @Override
            public void onComplete(SHARE_MEDIA share_media, int i, Map<String, String> map) {
                getUserInfo(platform);
            }

            @Override
            public void onError(SHARE_MEDIA share_media, int i, Throwable throwable) {
                Toast.makeText(LoginActivity.this, throwable.getMessage() + platform.toString(), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCancel(SHARE_MEDIA share_media, int i) {
            }
        });
    }

    /**
     * 获取授权平台的用户信息</br>
     */
    private void getUserInfo(SHARE_MEDIA platform) {
        mShareAPI.getPlatformInfo(this, platform, new UMAuthListener() {
            @Override
            public void onComplete(SHARE_MEDIA share_media, int i, Map<String, String> info) {
                Log.e("ceshi",info.toString());

                String avatar = "";
                String nickname = "";
                String userid = "";
                int gender = 0;
                if (info != null) {
                    if (userType == WEIXIN) {
                        userid = info.get("openid");
                        gender = Integer.parseInt(info.get("sex"));
                        nickname =  info.get("nickname");
                        avatar =  info.get("headimgurl");
                    } else if (userType == QQ) {
                        userid = info.get("openid");
                        gender = info.get("gender").equals("男") ? 1:0;
                        nickname =  info.get("screen_name");
                        avatar =  info.get("profile_image_url");
                    } else if (userType == SINA) {
                        String content = info.get("result");
                        try {
                            JSONObject jsonObject = new JSONObject(content);
                            userid = (String) jsonObject.get("idstr");
                            gender = "m".equals(jsonObject.get("gender")) ? 1:0;
                            nickname = (String) jsonObject.get("screen_name");
                            avatar = (String) jsonObject.get("profile_image_url");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    showDialog();
                    LoginModel model = RetrofitUtils.create(LoginModel.class);
                    model.thridLogin(userType, userid, nickname, avatar, gender, new DefaultLiveHttpCallBack<UserInfo>() {
                        @Override
                        public void success(UserInfo userInfo) {
                            if(isNotFinish()){
                                System.out.println(userInfo);
                                if(SharedPreferenceUtils.getBoolean(ConfigUtils.IS_FIRSTER_LOGIN,true)){
                                    SharedPreferenceUtils.saveProp(ConfigUtils.IS_FIRSTER_LOGIN,false);
                                    sendFollowArea();
                                }
                                cacheSaveUserinfo(userInfo);
                                getMqttService();
                                sendDevice();
                                changeFollowPager();
                                if (mDialog != null && mDialog.isShowing()) {
                                    mDialog.dismiss();
                                }
                                finish();
                            }
                        }

                        @Override
                        public void failure(String message) {
                            if(isNotFinish()) {
                                mDialog.dismiss();
                                ToastUtils.showToast(LoginActivity.this, message);
                            }
                        }
                    });
                }
            }

            @Override
            public void onError(SHARE_MEDIA share_media, int i, Throwable throwable) {

            }

            @Override
            public void onCancel(SHARE_MEDIA share_media, int i) {
                mDialog.dismiss();
            }
        });
    }

    private void changeFollowPager(){
        FollowContentFragment followContentFragment =  (FollowContentFragment) MainApplication.getInstance().getHomeActivity().getSupportFragmentManager().findFragmentByTag("订阅");
        if(followContentFragment != null)
            followContentFragment.getData();
    }

    private Dialog mDialog;

    private void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("验证中…");
        builder.setView(new ProgressBar(this));
        mDialog = builder.create();
        if (isNotFinish())
            mDialog.show();
    }
}
