package cn.gietv.mlive.modules.usercenter.activity;


import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import cn.gietv.mlive.R;
import cn.gietv.mlive.base.AbsBaseActivity;
import cn.gietv.mlive.http.DefaultLiveHttpCallBack;
import cn.gietv.mlive.modules.usercenter.bean.UserCenterBean;
import cn.gietv.mlive.modules.usercenter.model.UserCenterModel;
import cn.gietv.mlive.utils.CacheUtils;
import cn.gietv.mlive.utils.HeadViewController;
import cn.gietv.mlive.utils.ToastUtils;
import cn.kalading.android.retrofit.utils.RetrofitUtils;

/**
 * Created by houde on 2016/5/12.
 */
public class EditDescActivity extends AbsBaseActivity {
    private EditText mContent;
    private TextView mSubmit;
    private UserCenterModel model;
    private UserCenterBean.UserinfoEntity user ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_desc);
        HeadViewController.initHeadWithoutSearch(this, "修改简介");
        mContent = (EditText) findViewById(R.id.tucao_et);
        mSubmit = (TextView) findViewById(R.id.tucao_submit);
        mSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = mContent.getText().toString();
                if(TextUtils.isEmpty(content)){
                    ToastUtils.showToast(EditDescActivity.this, "请先输入简介的内容,再提交");
                    return;
                }
                mSubmit.setClickable(false);
                model.updateUserInfo(user.nickname, content, user.gender, user.email, new DefaultLiveHttpCallBack<UserCenterBean>() {
                    @Override
                    public void success(UserCenterBean userCenterBean) {
                        CacheUtils.saveUserInfo(userCenterBean.userinfo);
                        ToastUtils.showToast(EditDescActivity.this, "简介修改成功");
                        finish();
                    }

                    @Override
                    public void failure(String message) {
                        ToastUtils.showToast(EditDescActivity.this,message);
                    }
                });
            }
        });
        model = RetrofitUtils.create(UserCenterModel.class);
        user = CacheUtils.getCacheUserInfo();
        if(user != null){
            mContent.setText(user.desc);
        }
    }
}
