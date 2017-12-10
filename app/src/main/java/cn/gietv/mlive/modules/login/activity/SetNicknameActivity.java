package cn.gietv.mlive.modules.login.activity;

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
import cn.gietv.mlive.http.DefaultLiveHttpCallBack;
import cn.gietv.mlive.modules.login.bean.UserInfo;
import cn.gietv.mlive.modules.login.model.RegistModel;
import cn.gietv.mlive.modules.xmpp.XmppUtils;
import cn.gietv.mlive.utils.CacheUtils;
import cn.gietv.mlive.utils.HeadViewController;
import cn.gietv.mlive.utils.StringUtils;
import cn.gietv.mlive.utils.ToastUtils;
import cn.kalading.android.retrofit.utils.RetrofitUtils;

public class SetNicknameActivity extends AbsBaseActivity implements View.OnClickListener {
    private EditText mNicknameText;
    private Button mSubmit;
    private String mTelephoneNumber,mPassword;
    private String mNickname;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_nickname);
        MainApplication.getInstance().addActivityList(this);
        HeadViewController.initHeadWithoutSearch(this, "设置昵称");
        mTelephoneNumber = getIntent().getExtras().getString("telephone");
        mPassword = getIntent().getExtras().getString("password");
        mNicknameText = (EditText) findViewById(R.id.nickname);
        mSubmit = (Button) findViewById(R.id.submit);
        mSubmit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.submit){
                mNickname = mNicknameText.getText().toString();
                if(TextUtils.isEmpty(mNickname)){
                    ToastUtils.showToast(this,"昵称不能为空");
                    return;
                }else{
                    if(StringUtils.length(mNickname) > 16){
                        ToastUtils.showToast(this,"昵称最多8个汉字或者16英文及数字");
                        return;
                    }
                    startRegister();
                    //IntentUtils.openActivity(this, HomeActivity.class);
                }
        }
    }

    private void startRegister() {
        RegistModel model = RetrofitUtils.create(RegistModel.class);
        model.smsRegister(mTelephoneNumber, mPassword, mTelephoneNumber, mNickname, new DefaultLiveHttpCallBack<UserInfo>() {
            @Override
            public void success(UserInfo userInfo) {
                if (isNotFinish()) {
                    CacheUtils.saveUserInfo(userInfo.userinfo);
                    CacheUtils.getCache().put(CacheConstants.CACHE_TOKEN, userInfo.token);
                    CacheUtils.getCache().put(CacheConstants.CACHE_USERID, userInfo.userinfo._id);
                    RetrofitUtils.addHeader(HttpConstants.HEAD_TOKEN, userInfo.token);
                    RetrofitUtils.addHeader(HttpConstants.HEAD_USER_ID, userInfo.userinfo._id);
                    ToastUtils.showToast(SetNicknameActivity.this, "注册成功");
                    setResult(RESULT_OK);
                    XmppUtils.getInstance().reconnection(SetNicknameActivity.this);
                   /* MqttService mqttService = (MqttService) MainApplication.getInstance().getObjcet();
                    if (mqttService != null) {
                        MqttController mqttController = mqttService.getMqttController();
                        if (mqttController != null) {
                            MqttClient client = mqttController.getMqttClient();
                            if (client != null) {
                                try {
                                    client.subscribe(ConfigUtils.PRIVATE_CHAT + CacheUtils.getCacheUserInfo()._id, 1);
                                } catch (MqttException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }*/
                }
                MainApplication.getInstance().destroyActivity();
            }

            @Override
            public void failure(String message) {
                if (isNotFinish()) {
                    ToastUtils.showToast(SetNicknameActivity.this, message);
                }
            }
        });
    }
}
