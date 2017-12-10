package cn.gietv.mlive.modules.usercenter.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.gietv.mlive.R;
import cn.gietv.mlive.base.AbsBaseActivity;
import cn.gietv.mlive.http.DefaultLiveHttpCallBack;
import cn.gietv.mlive.modules.usercenter.model.UserCenterModel;
import cn.gietv.mlive.utils.CacheUtils;
import cn.gietv.mlive.utils.HeadViewController;
import cn.gietv.mlive.utils.IntentUtils;
import cn.gietv.mlive.utils.ToastUtils;
import cn.kalading.android.retrofit.utils.RetrofitUtils;

public class BindingPhoneActivity extends AbsBaseActivity implements View.OnClickListener{
    private TextView mRegisterAgreement;
    private Button submit;
    private EditText mTelephone;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_binding_phone);
        HeadViewController.initHeadWithoutSearch(this, "绑定手机");
        mRegisterAgreement = (TextView) findViewById(R.id.register_agreement);
        mRegisterAgreement.setOnClickListener(this);
        submit = (Button) findViewById(R.id.submit);
        mTelephone = (EditText) findViewById(R.id.telephone);
        submit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.register_agreement) {
            IntentUtils.openActivity(this, RegisterAgreementActivity.class);
        }else if(R.id.submit == v.getId()){
                final String telephoneNumber = mTelephone.getText().toString();
                if(TextUtils.isEmpty(telephoneNumber)){
                    ToastUtils.showToast(this,"手机号码不能为空");
                    return ;
                }else{
                    if(telephoneNumber.length()< 11){
                        ToastUtils.showToast(this,"请输入完整的手机号");
                        return;
                    }
                    if(!isMobileNO(telephoneNumber)){
                        ToastUtils.showToast(this,"请输入正确的手机号");
                        return;
                    }
                }
                UserCenterModel model = RetrofitUtils.create(UserCenterModel.class);
                model.sendSMS(CacheUtils.getCacheUserInfo()._id, telephoneNumber, new DefaultLiveHttpCallBack<String>() {
                    @Override
                    public void success(String result) {
                        Bundle bundle = new Bundle();
                        bundle.putString("telephone",telephoneNumber);
                        bundle.putString("userid",CacheUtils.getCacheUserInfo()._id);
                        IntentUtils.openActivity(BindingPhoneActivity.this, VerificationCodeActivity.class, bundle);
                        finish();
                    }

                    @Override
                    public void failure(String message) {
                        ToastUtils.showToast(BindingPhoneActivity.this,message);
                    }
                });
        }
    }
    public boolean isMobileNO(String mobiles) {
        Pattern p = Pattern.compile("[1][3578]\\d{9}");
        Matcher m = p.matcher(mobiles);
        System.out.println(m.matches() + "---");
        return m.matches();
    }
}
