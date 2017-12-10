package cn.gietv.mlive.modules.login.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import cn.gietv.mlive.MainApplication;
import cn.gietv.mlive.R;
import cn.gietv.mlive.base.AbsBaseActivity;
import cn.gietv.mlive.http.DefaultLiveHttpCallBack;
import cn.gietv.mlive.modules.login.model.LoginModel;
import cn.gietv.mlive.modules.usercenter.model.UserCenterModel;
import cn.gietv.mlive.utils.HeadViewController;
import cn.gietv.mlive.utils.IntentUtils;
import cn.gietv.mlive.utils.ToastUtils;
import cn.kalading.android.retrofit.utils.RetrofitUtils;

public class RegisterCodeActivity extends AbsBaseActivity implements View.OnClickListener {
    private TextView mTelephone,mTime,mTimes,mResend;
    private EditText mCode;
    private Button mSubmit;
    private String telephoneNumber;
    private String userid;
    private int type;
    private boolean flag;
    private int count;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    int i = (int)msg.obj;
                    if(i<10){
                        mTime.setText("00:" + "0" + i + "后");
                    }else {
                        mTime.setText("00:" + i + "后");
                    }
                    if(i == 0){
                        mResend.setTextColor(getResources().getColor(R.color.theme_green));
                        mResend.setClickable(true);
                    }
                    break;
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification_code);
        MainApplication.getInstance().addActivityList(this);
        HeadViewController.initHeadWithoutSearch(this, "手机注册");
        telephoneNumber = getIntent().getExtras().getString("telephone");
        userid = getIntent().getExtras().getString("userid");
        type = getIntent().getExtras().getInt("type");
        mTelephone = (TextView) findViewById(R.id.telephone);
        mTime = (TextView) findViewById(R.id.time);
        mTimes = (TextView) findViewById(R.id.times);
        mResend = (TextView) findViewById(R.id.resend);
        mSubmit = (Button) findViewById(R.id.submit);
        mCode = ( EditText) findViewById(R.id.code);
        mSubmit.setText("下一步");
        mSubmit.setOnClickListener(this);
        mResend.setOnClickListener(this);
        mTelephone.setText("你的手机号：+86" + telephoneNumber);
        mResend.setText("重新发送");
        mTimes.setText("(第1次)");
        mResend.setClickable(false);
        mResend.setTextColor(getResources().getColor(R.color.attention));
        flag = false;
        count = 1;
        startThread();
    }

    private void startThread() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                for(int i = 59;i >= 0;i--){
                    if(flag){
                        break;
                    }
                    Message message = Message.obtain();
                    message.what = 1;
                    message.obj = i;
                    handler.sendMessage(message);
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.resend) {
            mResend.setClickable(false);
            mResend.setTextColor(getResources().getColor(R.color.attention));
            count++;
            mTimes.setText("(第" + count + "次)");
            startThread();
            sendSMS();
        }else if(v.getId() == R.id.submit){
                   verifySMS();
        }
    }

    private void verifySMS(){
        String code = mCode.getText().toString();
        if(TextUtils.isEmpty(code)){
            ToastUtils.showToast(this,"请输入短信验证码");
            return;
        }
        LoginModel model = RetrofitUtils.create(LoginModel.class);
        model.verifysms(telephoneNumber, telephoneNumber, code, new DefaultLiveHttpCallBack<String>() {
            @Override
            public void success(String s) {
                if(isNotFinish()) {
                    if (s != null) {
                        if (s.equals("success")) {
                            Bundle bundle = new Bundle();
                            bundle.putString("telephone", telephoneNumber);
                            bundle.putInt("type", type);
                            IntentUtils.openActivity(RegisterCodeActivity.this, SetPasswordActivity.class, bundle);
                            finish();
                        } else {
                            ToastUtils.showToast(RegisterCodeActivity.this, "短信验证码错误");
                        }
                    }
                }
            }

            @Override
            public void failure(String message) {
                if(isNotFinish())
                ToastUtils.showToast(RegisterCodeActivity.this,message);
            }
        });
    }
    private void sendSMS() {
        UserCenterModel model = RetrofitUtils.create(UserCenterModel.class);
        model.sendSMS(telephoneNumber, telephoneNumber, new DefaultLiveHttpCallBack<String>() {
            @Override
            public void success(String smsResult) {

            }

            @Override
            public void failure(String message) {

            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        flag = true;
    }
}
