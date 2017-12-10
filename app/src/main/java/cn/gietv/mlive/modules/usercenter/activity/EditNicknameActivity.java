package cn.gietv.mlive.modules.usercenter.activity;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import cn.gietv.mlive.R;
import cn.gietv.mlive.base.AbsBaseActivity;
import cn.gietv.mlive.http.DefaultLiveHttpCallBack;
import cn.gietv.mlive.modules.usercenter.bean.UserCenterBean;
import cn.gietv.mlive.modules.usercenter.model.UserCenterModel;
import cn.gietv.mlive.utils.CacheUtils;
import cn.gietv.mlive.utils.HeadViewController;
import cn.gietv.mlive.utils.IntentUtils;
import cn.gietv.mlive.utils.StringUtils;
import cn.gietv.mlive.utils.ToastUtils;
import cn.kalading.android.retrofit.utils.RetrofitUtils;

/**
 * Created by houde on 2016/1/19.
 */
public class EditNicknameActivity extends AbsBaseActivity implements View.OnClickListener {

    private EditText mNickname;
    private Button mSubmit;
    private UserCenterBean.UserinfoEntity mUserinfo;
    private UserCenterModel mModel;
    public static void getInstance(Context context,UserCenterBean.UserinfoEntity userinfo){
        Bundle bundle = new Bundle();
        bundle.putSerializable("userinfo",userinfo);
        IntentUtils.openActivity(context,EditNicknameActivity.class,bundle);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_nickname);
        HeadViewController.initHeadWithoutSearch(this, "修改昵称");
        mUserinfo = (UserCenterBean.UserinfoEntity) getIntent().getExtras().getSerializable("userinfo");
        mNickname = (EditText)findViewById(R.id.nickname);
        mSubmit = (Button) findViewById(R.id.submit);
        mSubmit.setOnClickListener(this);
        mModel = RetrofitUtils.create(UserCenterModel.class);
    }

    @Override
    public void onClick(View v) {
        updateNickname();
    }

    private void updateNickname() {
        String nickname = mNickname.getText().toString();
        if(TextUtils.isEmpty(nickname)){
            ToastUtils.showToast(this,"昵称不能为空");
            return;
        }else{
            int length = StringUtils.length(nickname);
            if(length > 16){
                ToastUtils.showToast(this,"最多8个汉字或者16个英文及数字");
                return;
            }
            mModel.updateUserInfo(nickname, mUserinfo.desc, mUserinfo.gender, mUserinfo.email, new DefaultLiveHttpCallBack<UserCenterBean>() {
                @Override
                public void success(UserCenterBean userinfoEntity) {
                    CacheUtils.saveUserInfo(userinfoEntity.userinfo);
                    if(isNotFinish()) {
                        ToastUtils.showToast(EditNicknameActivity.this, "昵称修改成功");
                        finish();
                    }
                }

                @Override
                public void failure(String message) {
                    ToastUtils.showToast(EditNicknameActivity.this,message);
                }
            });
        }
    }
}
