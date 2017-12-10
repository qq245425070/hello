package cn.gietv.mlive.modules.login.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.gietv.mlive.R;
import cn.gietv.mlive.base.AbsBaseActivity;
import cn.gietv.mlive.http.DefaultLiveHttpCallBack;
import cn.gietv.mlive.modules.login.bean.UserExist;
import cn.gietv.mlive.modules.login.model.ForgetPasswordModel;
import cn.gietv.mlive.modules.usercenter.model.UserCenterModel;
import cn.gietv.mlive.utils.HeadViewController;
import cn.gietv.mlive.utils.IntentUtils;
import cn.gietv.mlive.utils.ToastUtils;
import cn.kalading.android.retrofit.utils.RetrofitUtils;

/**
 * author：steven
 * datetime：15/10/21 09:59
 */
public class ForgetPasswordActivity extends AbsBaseActivity implements View.OnClickListener {
    private EditText mTelephoneText;
    private TextView mNext;
    private ForgetPasswordModel mPasswordModel;
    private String telephoneNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forget_password_layout);
        HeadViewController.initHeadWithoutSearch(this, "找回密码");
        mPasswordModel = RetrofitUtils.create(ForgetPasswordModel.class);
        mTelephoneText = (EditText) findViewById(R.id.froget_telephone);
        mNext = (TextView) findViewById(R.id.forget_next);
        mNext.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.forget_next) {
                telephoneNumber = mTelephoneText.getText().toString();
                if (TextUtils.isEmpty(telephoneNumber)) {
                    ToastUtils.showToast(this, "请输入手机号，在点击下一步");
                    return;
                } else {
                    if (telephoneNumber.length() < 11) {
                        ToastUtils.showToast(this, "请输入完整的手机号");
                        return;
                    }
                    if(!isMobileNO(telephoneNumber)){
                        ToastUtils.showToast(this,"请输入正确的手机号");
                        return;
                    }
                    existUser();
                }
        }
    }

    private void existUser() {
        ForgetPasswordModel model = RetrofitUtils.create(ForgetPasswordModel.class);
        model.existUser(telephoneNumber, new DefaultLiveHttpCallBack<UserExist>() {
            @Override
            public void success(UserExist userExist) {
                if(userExist.isexist == 0){
                    ToastUtils.showToast(ForgetPasswordActivity.this,"此用户不存在，不能找回密码");
                }else if(userExist.isexist == 1){
                    sendSMS();
                }
            }

            @Override
            public void failure(String message) {

            }
        });
    }

    private void sendSMS() {
        UserCenterModel model = RetrofitUtils.create(UserCenterModel.class);
        model.sendSMS(telephoneNumber, telephoneNumber, new DefaultLiveHttpCallBack<String>() {
            @Override
            public void success(String smsResult) {
                Bundle bundle = new Bundle();
                bundle.putString("telephone",telephoneNumber);
                bundle.putInt("type",2);
                IntentUtils.openActivity(ForgetPasswordActivity.this, RegisterCodeActivity.class, bundle);
                finish();
            }

            @Override
            public void failure(String message) {

            }
        });
    }
    public boolean isMobileNO(String mobiles) {
        Pattern p = Pattern.compile("[1][3578]\\d{9}");
        Matcher m = p.matcher(mobiles);
        System.out.println(m.matches() + "---");
        return m.matches();
    }
}
