package cn.gietv.mlive.modules.vrgame.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;

import cn.gietv.mlive.R;
import cn.gietv.mlive.base.AbsBaseActivity;
import cn.gietv.mlive.http.DefaultLiveHttpCallBack;
import cn.gietv.mlive.modules.login.activity.LoginActivity;
import cn.gietv.mlive.modules.video.bean.MessageBean;
import cn.gietv.mlive.modules.vrgame.model.VRGameModel;
import cn.gietv.mlive.utils.HeadViewController;
import cn.gietv.mlive.utils.IntentUtils;
import cn.gietv.mlive.utils.ToastUtils;
import cn.gietv.mlive.utils.UserUtils;
import cn.kalading.android.retrofit.utils.RetrofitUtils;

/**
 * Created by houde on 2016/6/24.
 */
public class WriteCommentActivity extends AbsBaseActivity {
    private RatingBar mRatingBar;
    private TextView mSubmit;
    private EditText mCommentContent;
    private Context mContext;
    private String mID;
    private VRGameModel model;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_comment);
        mContext = this;
        mID = getIntent().getStringExtra("id");
        model = RetrofitUtils.create(VRGameModel.class);
        HeadViewController.initHeadWithoutSearch(this, "撰写评论");
        mRatingBar = (RatingBar) findViewById(R.id.rating_bar);
        mSubmit = (TextView) findViewById(R.id.submit);
        mCommentContent = (EditText) findViewById(R.id.comment_content);
        mSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSubmit.setClickable(false);
                String content = mCommentContent.getText().toString();
                if (TextUtils.isEmpty(content)) {
                    ToastUtils.showToastShort(mContext, "输入评论内容，再提交");
                    return;
                }
                int score = (int) mRatingBar.getRating();
                if(UserUtils.isNotLogin()){
                    IntentUtils.openActivity(mContext, LoginActivity.class);
                    return;
                }
                model.commitComment(mID, content, 3, score, new DefaultLiveHttpCallBack<MessageBean.MessagesEntity>() {
                    @Override
                    public void success(MessageBean.MessagesEntity messagesEntity) {
                        Intent  intent = new Intent();
                        intent.putExtra("message",messagesEntity);
                        setResult(RESULT_OK, intent);
                        finish();
                    }

                    @Override
                    public void failure(String message) {
                        if (isNotFinish()) {
                            ToastUtils.showToastShort(mContext, message);
                        }
                    }
                });
            }
        });
    }
}
