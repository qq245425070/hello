package cn.gietv.mlive.modules.usercenter.activity;

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
import cn.gietv.mlive.modules.usercenter.model.UserCenterModel;
import cn.gietv.mlive.utils.CacheUtils;
import cn.gietv.mlive.utils.ConfigUtils;
import cn.gietv.mlive.utils.HeadViewController;
import cn.gietv.mlive.utils.IntentUtils;
import cn.gietv.mlive.utils.NoviceTaskUtils;
import cn.gietv.mlive.utils.ToastUtils;
import cn.kalading.android.retrofit.utils.RetrofitUtils;

public class VerificationCodeActivity extends AbsBaseActivity implements View.OnClickListener{
    private TextView mTelephone,mTime,mTimes,mResend;
    private EditText mCode;
    private Button mSubmit;
   // private SMSResult mSmsResult;
    private String telephoneNumber;
    private String userid;
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
        HeadViewController.initHeadWithoutSearch(this, "绑定手机");
        telephoneNumber = getIntent().getExtras().getString("telephone");
        userid = getIntent().getExtras().getString("userid");
        mTelephone = (TextView) findViewById(R.id.telephone);
        mTime = (TextView) findViewById(R.id.time);
        mTimes = (TextView) findViewById(R.id.times);
        mResend = (TextView) findViewById(R.id.resend);
        mSubmit = (Button) findViewById(R.id.submit);
        mSubmit.setText("完成");
        mCode = (EditText) findViewById(R.id.code);
        mSubmit.setOnClickListener(this);
        mResend.setOnClickListener(this);
        mTelephone.setText("你的手机号：+86" + telephoneNumber);
        mResend.setText("重新发送");
        mTimes.setText("(第1次)");
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
        }else if (R.id.submit == v.getId()){
                String code = mCode.getText().toString();
                if(TextUtils.isEmpty(code)){
                    ToastUtils.showToast(this,"请输入短信验证码");
                    return;
                }
                model.bindPhone(userid, telephoneNumber, code, new DefaultLiveHttpCallBack<String>() {
                    @Override
                    public void success(String s) {
                        IntentUtils.openActivity(VerificationCodeActivity.this, TaskActivity.class);
                        //绑定手机号成功，保存信息到数据库
                        if(NoviceTaskUtils.getNewTaskData(ConfigUtils.TASK_NEW_PHONE,VerificationCodeActivity.this) == -1)
                            NoviceTaskUtils.editTaskCount(ConfigUtils.TASK_NEW_PHONE,VerificationCodeActivity.this,0,0);
                        finish();
                    }

                    @Override
                    public void failure(String message) {
                        ToastUtils.showToast(VerificationCodeActivity.this,message);
                    }
                });
        }
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        flag = true;
    }
    private UserCenterModel model = RetrofitUtils.create(UserCenterModel.class);
    private void sendSMS() {
        model.sendSMS(CacheUtils.getCacheUserInfo()._id, telephoneNumber, new DefaultLiveHttpCallBack<String>() {
            @Override
            public void success(String smsResult) {

            }

            @Override
            public void failure(String message) {

            }
        });
    }
}
