package cn.gietv.mlive.modules.video.activity;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import cn.gietv.mlive.R;
import cn.gietv.mlive.base.AbsBaseActivity;
import cn.gietv.mlive.http.DefaultLiveHttpCallBack;
import cn.gietv.mlive.modules.login.activity.LoginActivity;
import cn.gietv.mlive.modules.video.bean.MessageBean;
import cn.gietv.mlive.modules.video.model.MessageModel;
import cn.gietv.mlive.utils.CacheUtils;
import cn.gietv.mlive.utils.IntentUtils;
import cn.gietv.mlive.utils.ToastUtils;
import cn.gietv.mlive.utils.UserUtils;
import cn.kalading.android.retrofit.utils.RetrofitUtils;

/**
 * Created by houde on 2016/6/27.
 * 回复评论的Activity
 */
public class ReplyCommentActivity extends AbsBaseActivity implements View.OnClickListener{
    private TextView mCancelText,mReplyText,mTitleText;
    private EditText mCommentContent;
    private String mNickname;
    public static void openReplyCommentActivity(Context context,String nickname,String proid,String msgID,String msgUid){
        Bundle bundle = new Bundle();
        bundle.putString("nickname",nickname);
        bundle.putString("proid",proid);
        bundle.putString("msgID",msgID);
        bundle.putString("msgUid",msgUid);
        IntentUtils.openActivity(context,ReplyCommentActivity.class,bundle);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reply_comment);
        mCancelText = (TextView) findViewById(R.id.cancel);
        mReplyText = (TextView) findViewById(R.id.reply);
        mTitleText = (TextView) findViewById(R.id.title);
        mCommentContent = (EditText) findViewById(R.id.comment_content);
        mCommentContent.requestFocus();
        mNickname = getIntent().getStringExtra("nickname");
        mTitleText.setText("回复" + mNickname + "的评论");
        mCancelText.setOnClickListener(this);
        mReplyText.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.cancel){
            this.finish();
        }else if(v.getId() == R.id.reply){
            mReplyText.setClickable(false);
            if(UserUtils.isNotLogin()){
                IntentUtils.openActivity(this, LoginActivity.class);
                mReplyText.setClickable(true);
                return;
            }
            String content = mCommentContent.getText().toString();
            if(TextUtils.isEmpty(content)){
                ToastUtils.showToastShort(this,"回复能让不能为空");
                mReplyText.setClickable(true);
                return;
            }

            MessageModel messageModel = RetrofitUtils.create(MessageModel.class);
            String myID = CacheUtils.getCacheUserInfo()._id;
            String msgUid = getIntent().getStringExtra("msgUid");
            if(myID != null && myID.equals(msgUid)){
                ToastUtils.showToastShort(this, "不能回复自己的评论");
                mReplyText.setClickable(true);
                return;
            }
            messageModel.sendMessage(getIntent().getStringExtra("proid"), content, 2, "yes", getIntent().getStringExtra("msgID"), msgUid, new DefaultLiveHttpCallBack<MessageBean.MessagesEntity>() {
                @Override
                public void success(MessageBean.MessagesEntity messagesEntity) {
                    if (isNotFinish()) {
                        ToastUtils.showToastShort(ReplyCommentActivity.this, "回复评论成功");
                        finish();
                    }
                }

                @Override
                public void failure(String message) {
                    if(isNotFinish()){
                        mReplyText.setClickable(true);
                        ToastUtils.showToastShort(ReplyCommentActivity.this,message);
                    }
                }
            });

        }
    }


}
