package cn.gietv.mlive.modules.setting.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.EditText;
import android.widget.TextView;

import cn.gietv.mlive.R;
import cn.gietv.mlive.base.AbsBaseActivity;
import cn.gietv.mlive.http.DefaultLiveHttpCallBack;
import cn.gietv.mlive.modules.setting.activity.model.SettingModel;
import cn.gietv.mlive.utils.HeadViewController;
import cn.gietv.mlive.utils.InputUtils;
import cn.gietv.mlive.utils.ToastUtils;
import cn.kalading.android.retrofit.utils.RetrofitUtils;

public class TuCaoActivity extends AbsBaseActivity implements View.OnClickListener {

    private EditText mContent;
    private TextView mSubmit,mBabyText;
    private SettingModel mSettingModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tu_cao);
        HeadViewController.initHeadWithoutSearch(this, "吐槽有理");
        mContent = (EditText) findViewById(R.id.tucao_et);
        mSubmit = (TextView) findViewById(R.id.tucao_submit);
        mBabyText = (TextView) findViewById(R.id.baby_text);
        mSubmit.setOnClickListener(this);
        mSettingModel = RetrofitUtils.create(SettingModel.class);
    }

    @Override
    public void onClick(View v) {
                String content = mContent.getText().toString();
                if(TextUtils.isEmpty(content)){
                    ToastUtils.showToast(this,"请先输入吐槽的内容,再提交");
                    return;
                }
                mSettingModel.submitTucao(content, new DefaultLiveHttpCallBack<String>() {
                    @Override
                    public void success(String s) {
                        InputUtils.closeInputKeyBoard(mContent);
                        mContent.setText("");
                    }

                    @Override
                    public void failure(String message) {
                        ToastUtils.showToast(TuCaoActivity.this,message);
                    }
                });
                mBabyText.setVisibility(View.VISIBLE);
                Animation animation = new AlphaAnimation(1f,0f);
                animation.setDuration(2000);
                animation.setFillAfter(true);
                mBabyText.setAnimation(animation);
                mBabyText.startAnimation(animation);
                animation.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        mBabyText.setVisibility(View.GONE);
                        finish();
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
    }
}
