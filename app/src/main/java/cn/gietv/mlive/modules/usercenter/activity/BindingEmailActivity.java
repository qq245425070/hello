package cn.gietv.mlive.modules.usercenter.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.gietv.mlive.R;
import cn.gietv.mlive.base.AbsBaseActivity;
import cn.gietv.mlive.http.DefaultLiveHttpCallBack;
import cn.gietv.mlive.modules.usercenter.bean.UserCenterBean;
import cn.gietv.mlive.modules.usercenter.model.UserCenterModel;
import cn.gietv.mlive.utils.CacheUtils;
import cn.gietv.mlive.utils.HeadViewController;
import cn.gietv.mlive.utils.ToastUtils;
import cn.kalading.android.retrofit.utils.RetrofitUtils;

public class BindingEmailActivity extends AbsBaseActivity implements View.OnClickListener{
    private EditText mEmail;
    private Button mSubmit;
    private UserCenterModel mModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_binding_email);
        mModel = RetrofitUtils.create(UserCenterModel.class);
        HeadViewController.initHeadWithoutSearch(this, "绑定邮箱");
        mEmail = (EditText) findViewById(R.id.email);
        mSubmit = (Button) findViewById(R.id.submit);
        mSubmit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
                updateNickname();
    }
    private void updateNickname() {
        UserCenterBean.UserinfoEntity mUserinfo = CacheUtils.getCacheUserInfo();
        String email = mEmail.getText().toString();
        if(TextUtils.isEmpty(email)){
            ToastUtils.showToast(this, "昵称不能为空");
            return;
        }else{
            String check = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
            Pattern regex = Pattern.compile(check);
            Matcher matcher = regex.matcher(email);
            boolean isMatched = matcher.matches();
            if(!isMatched){
                ToastUtils.showToast(this,"请输入正确的邮箱");
                return;
            }
            mModel.updateUserInfo(mUserinfo.nickname, mUserinfo.desc, mUserinfo.gender, email, new DefaultLiveHttpCallBack<UserCenterBean>() {
                @Override
                public void success(UserCenterBean userinfoEntity) {
                    CacheUtils.saveUserInfo(userinfoEntity.userinfo);
                    if(isNotFinish()) {
                        ToastUtils.showToast(BindingEmailActivity.this, "邮箱修改成功");
                        finish();
                    }
                }

                @Override
                public void failure(String message) {
                    ToastUtils.showToast(BindingEmailActivity.this,message);
                }
            });
        }
    }
}
