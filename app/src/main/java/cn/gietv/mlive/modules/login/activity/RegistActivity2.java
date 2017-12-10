package cn.gietv.mlive.modules.login.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.gietv.mlive.MainApplication;
import cn.gietv.mlive.R;
import cn.gietv.mlive.base.AbsBaseActivity;
import cn.gietv.mlive.http.DefaultLiveHttpCallBack;
import cn.gietv.mlive.modules.usercenter.activity.RegisterAgreementActivity;
import cn.gietv.mlive.modules.usercenter.model.UserCenterModel;
import cn.gietv.mlive.utils.HeadViewController;
import cn.gietv.mlive.utils.IntentUtils;
import cn.gietv.mlive.utils.ToastUtils;
import cn.kalading.android.retrofit.utils.RetrofitUtils;

/**
 * author：steven
 * datetime：15/10/9 18:30
 */
public class RegistActivity2 extends AbsBaseActivity implements View.OnClickListener {

    private TextView mRegisterAgreement;
    private Button submit;
    private EditText mTelephone;
    private UserCenterModel model;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_binding_phone);
        MainApplication.getInstance().addActivityList(this);
        model = RetrofitUtils.create(UserCenterModel.class);
        initViews();
    }

    private void initViews() {
        HeadViewController.initHeadWithoutSearch(this, "手机注册");
        mRegisterAgreement = (TextView) findViewById(R.id.register_agreement);
        mRegisterAgreement.setOnClickListener(this);
        submit = (Button) findViewById(R.id.submit);
        mTelephone = (EditText) findViewById(R.id.telephone);
        submit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.register_agreement) {
            IntentUtils.openActivity(this, RegisterAgreementActivity.class);
        }else if(v.getId() == R.id.submit){
                submit.setClickable(false);
                final String telephoneNumber = mTelephone.getText().toString();
                if(TextUtils.isEmpty(telephoneNumber)){
                    ToastUtils.showToast(this, "手机号码不能为空");
                    return ;
                }else {
                    if (telephoneNumber.length() < 11) {
                        ToastUtils.showToast(this, "请输入完整的手机号");
                        return;
                    }
                    if (!isMobileNO(telephoneNumber)) {
                        ToastUtils.showToast(this, "请输入正确的手机号");
                        return;
                    }
                }
                model.getBindPhoneStatus(telephoneNumber, new DefaultLiveHttpCallBack<String>() {
                    @Override
                    public void success(String s) {
                        if(isNotFinish()){
                            getSMSCode(telephoneNumber);
                            submit.setClickable(true);
                        }
                    }

                    @Override
                    public void failure(String message) {
                        if(isNotFinish()){
                            ToastUtils.showToastShort(RegistActivity2.this,message);
                        }
                    }
                });

        }
    }

    private void getSMSCode(final String telephoneNumber) {
        model.sendSMS(telephoneNumber, telephoneNumber, new DefaultLiveHttpCallBack<String>() {
            @Override
            public void success(String smsResult) {
                if(isNotFinish()) {
                    Bundle bundle = new Bundle();
                    bundle.putString("telephone", telephoneNumber);
                    bundle.putInt("type", 1);
                    IntentUtils.openActivity(RegistActivity2.this, RegisterCodeActivity.class, bundle);
                    finish();
                }
            }

            @Override
            public void failure(String message) {
                if(isNotFinish())
                 submit.setClickable(true);
            }
        });
    }

    public static boolean isMobileNO(String mobiles) {
        Pattern p = Pattern.compile("[1][3578]\\d{9}");
        Matcher m = p.matcher(mobiles);
        System.out.println(m.matches() + "---");
        return m.matches();
    }
}
