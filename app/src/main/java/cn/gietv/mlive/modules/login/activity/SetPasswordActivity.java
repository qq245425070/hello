package cn.gietv.mlive.modules.login.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import cn.gietv.mlive.MainApplication;
import cn.gietv.mlive.R;
import cn.gietv.mlive.base.AbsBaseActivity;
import cn.gietv.mlive.constants.CacheConstants;
import cn.gietv.mlive.constants.HttpConstants;
import cn.gietv.mlive.db.DBUtils;
import cn.gietv.mlive.http.DefaultLiveHttpCallBack;
import cn.gietv.mlive.modules.login.bean.UserInfo;
import cn.gietv.mlive.modules.login.model.LoginModel;
import cn.gietv.mlive.modules.xmpp.XmppConnection;
import cn.gietv.mlive.service.XmppService;
import cn.gietv.mlive.utils.CacheUtils;
import cn.gietv.mlive.utils.HeadViewController;
import cn.gietv.mlive.utils.IntentUtils;
import cn.gietv.mlive.utils.ToastUtils;
import cn.kalading.android.retrofit.utils.RetrofitUtils;

public class SetPasswordActivity extends AbsBaseActivity implements View.OnClickListener {
    private EditText mPasswordText;
    private Button mSubmit;
    private String mTelephoneNumber;
    private String mPassword;
    private int type;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_password);
        MainApplication.getInstance().addActivityList(this);
        HeadViewController.initHeadWithoutSearch(this, "设置密码");
        mTelephoneNumber = getIntent().getExtras().getString("telephone");
        type = getIntent().getExtras().getInt("type");
        mPasswordText = (EditText) findViewById(R.id.password);
        mSubmit = (Button) findViewById(R.id.submit);
        if(type == 1){
            mSubmit.setText("下一步");
        }else if (type == 2){
            mSubmit.setText("完成");
        }
        mSubmit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.submit){
                mPassword = mPasswordText.getText().toString();
                if(TextUtils.isEmpty(mPassword)){
                    ToastUtils.showToast(this,"密码不能为空");
                    return;
                }else{
                    if(mPassword.length() < 6 || mPassword.length() > 20){
                        ToastUtils.showToast(this,"请输入6-20位的密码");
                        return;
                    }
                    if(type == 1) {
                        Bundle bundle = new Bundle();
                        bundle.putString("telephone", mTelephoneNumber);
                        bundle.putString("password", mPassword);
                        IntentUtils.openActivity(this, SetNicknameActivity.class, bundle);
                        finish();
                    }else if(type == 2){
                        LoginModel model = RetrofitUtils.create(LoginModel.class);
                        model.smsmodifypsw(mTelephoneNumber,mPassword,new DefaultLiveHttpCallBack<UserInfo>(){

                            @Override
                            public void success(UserInfo userInfo) {
                                cacheSaveUserinfo(userInfo);
                                getMqttService();
                                MainApplication.getInstance().destroyActivity();
                                if(isNotFinish())
                                    finish();
                            }

                            @Override
                            public void failure(String message) {
                                if(isNotFinish())
                                    ToastUtils.showToastShort(SetPasswordActivity.this,message);
                            }
                        });
                    }
                }
        }
    }

    private void cacheSaveUserinfo(UserInfo userInfo) {
        System.out.println("old     " + CacheUtils.getCache().getAsString(CacheConstants.CACHE_TOKEN));
        System.out.println("new     " + userInfo.token);
        DBUtils.getInstance(this).saveUser(userInfo);
        CacheUtils.saveUserInfo(userInfo.userinfo);
        CacheUtils.getCache().put(CacheConstants.CACHE_USERID, userInfo.userinfo._id);
        CacheUtils.getCache().put(CacheConstants.CACHE_TOKEN, userInfo.token);
        RetrofitUtils.addHeader(HttpConstants.HEAD_TOKEN, userInfo.token);
        RetrofitUtils.addHeader(HttpConstants.HEAD_USER_ID, userInfo.userinfo._id);
        ToastUtils.showToast(SetPasswordActivity.this, "登录成功");
    }

    private void getMqttService() {
        XmppConnection.getInstance().closeConnection();
        Intent service = new Intent();
        service.setClass(this,XmppService.class);
        stopService(service);
        startService(service);
        /*MqttService mqttService = (MqttService) MainApplication.getInstance().getObjcet();
        if(mqttService != null) {
            MqttController mqttController = mqttService.getMqttController();
            if(mqttController != null) {
                MqttClient client = mqttController.getMqttClient();
                if(client != null) {
                    try {
                        client.subscribe(ConfigUtils.PRIVATE_CHAT + CacheUtils.getCacheUserInfo()._id, 1);
                    } catch (MqttException e) {
                        e.printStackTrace();
                    }
                }
            }
        }*/
    }
}
