package cn.gietv.mlive.modules.video.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

import cn.gietv.mlive.R;
import cn.gietv.mlive.constants.CommConstants;
import cn.gietv.mlive.http.DefaultLiveHttpCallBack;
import cn.gietv.mlive.modules.compere.activity.CompereActivity;
import cn.gietv.mlive.modules.login.activity.LoginActivity;
import cn.gietv.mlive.modules.user.activity.YoukuUserActivity;
import cn.gietv.mlive.modules.usercenter.bean.UserCenterBean;
import cn.gietv.mlive.modules.video.activity.DialogListActivity;
import cn.gietv.mlive.modules.video.activity.ReplyCommentActivity;
import cn.gietv.mlive.modules.video.bean.MessageBean;
import cn.gietv.mlive.modules.video.fragment.VideoPlayFragment4;
import cn.gietv.mlive.modules.video.model.MessageModel;
import cn.gietv.mlive.utils.ImageLoaderUtils;
import cn.gietv.mlive.utils.IntentUtils;
import cn.gietv.mlive.utils.TimeUtil;
import cn.gietv.mlive.utils.UserUtils;
import cn.kalading.android.retrofit.utils.RetrofitUtils;

/**
 * Created by houde on 2016/5/30.
 */
public class MessageAdapter2 extends RecyclerView.Adapter<MessageAdapter2.MessageViewHolder> {
    private List<MessageBean.MessagesEntity> messageBeans;
    private Context mContext;
    private ImageLoader mImageLoader;
    private String mProid;
    private MessageModel model ;
    private String temp;
    private int index;
    public MessageAdapter2(Context context,List<MessageBean.MessagesEntity> messageBeans,String id){
        this.messageBeans = messageBeans;
        this.mContext = context;
        mImageLoader = ImageLoaderUtils.getDefaultImageLoader(context);
        this.mProid = id;
        model = RetrofitUtils.create(MessageModel.class);
    }
    public void addMessageEntity(MessageBean.MessagesEntity messagesEntity){
        this.messageBeans.add(0,messagesEntity);
        notifyDataSetChanged();
    }
    @Override
    public MessageViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        return new MessageViewHolder(LayoutInflater.from(mContext).inflate(R.layout.comment_message_item,null));
    }

    @Override
    public void onBindViewHolder(final MessageViewHolder messageViewHolder, int position) {
        final MessageBean.MessagesEntity bean = getItem(position);
        if(bean == null)
            return;
        messageViewHolder.commentContant.setText(bean.message);
        messageViewHolder.dateText.setText(TimeUtil.format(bean.date));
        messageViewHolder.nicknameText.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        if("yes".equals(bean.isreply)){
            temp = bean.nickname+"  回复  " + bean.replyuser.nickname;
            final SpannableString ss = new SpannableString(temp);
            index = temp.indexOf("回复");
            //得到drawable对象，即所要插入的图片
            Drawable d = mContext.getResources().getDrawable(R.mipmap.reply_icon);
            d.setBounds(0, 0, d.getIntrinsicWidth(), d.getIntrinsicHeight());
            //用这个drawable对象代替字符串easy
            ImageSpan span = new ImageSpan(d, ImageSpan.ALIGN_BASELINE);
            //包括0但是不包括"easy".length()即：4。[0,4)。值得注意的是当我们复制这个图片的时候，实际是复制了"easy"这个字符串。
            ss.setSpan(span, index, index + 2, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
            messageViewHolder.nicknameText.setText(ss);
//            messageViewHolder.nicknameText.setText(bean.nickname+"  回复  " + bean.replyuser.nickname);
            messageViewHolder.dialogList.setVisibility(View.VISIBLE);
        }else{
            messageViewHolder.nicknameText.setText(bean.nickname);
            messageViewHolder.dialogList.setVisibility(View.INVISIBLE);
        }
        mImageLoader.displayImage(bean.avatar, messageViewHolder.userAvatar);
        messageViewHolder.userAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(bean.isyouku == 1){
                    UserCenterBean.UserinfoEntity user = new UserCenterBean.UserinfoEntity();
                    user.nickname = bean.nickname;
                    user.avatar = bean.avatar;
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("user",user);
                    IntentUtils.openActivity(mContext,YoukuUserActivity.class,bundle);
                }else {
                    CompereActivity.openCompereActivity(mContext, bean.uid);
                }
            }
        });
        messageViewHolder.dialogList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogListActivity.openDialogListActivity(mContext, mProid, bean._id);
            }
        });
        if(bean.islikes == CommConstants.FOLLOW_ACTION_ON){
            messageViewHolder.dianzan.setImageResource(R.mipmap.commen);
        }else{
            messageViewHolder.dianzan.setImageResource(R.mipmap.commen_nomal);
        }
        messageViewHolder.likeCount.setText(String.valueOf(bean.likes_cnt));
        messageViewHolder.dianzan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (UserUtils.isNotLogin()) {
                    IntentUtils.openActivity(mContext, LoginActivity.class);
                    return;
                }
                messageViewHolder.dianzan.setClickable(false);
                if (bean.islikes == CommConstants.TRUE) {
                    model.likeComment(bean._id, CommConstants.TYPE_COMMENT, CommConstants.FOLLOW_ACTION_OFF, new DefaultLiveHttpCallBack<String>() {
                        @Override
                        public void success(String s) {
                            messageViewHolder.dianzan.setClickable(true);
                            messageViewHolder.dianzan.setImageResource(R.mipmap.commen_nomal);
                            messageViewHolder.likeCount.setText(String.valueOf(--bean.likes_cnt));
                            bean.islikes = CommConstants.FALSE;
                        }

                        @Override
                        public void failure(String message) {
                            messageViewHolder.dianzan.setClickable(true);
                        }
                    });
                } else {
                    model.likeComment(bean._id, CommConstants.TYPE_COMMENT, CommConstants.FOLLOW_ACTION_ON, new DefaultLiveHttpCallBack<String>() {
                        @Override
                        public void success(String s) {
                            messageViewHolder.dianzan.setClickable(true);
                            messageViewHolder.dianzan.setImageResource(R.mipmap.commen);
                            messageViewHolder.likeCount.setText(String.valueOf(++bean.likes_cnt));
                            bean.islikes = CommConstants.TRUE;
                        }

                        @Override
                        public void failure(String message) {
                            messageViewHolder.dianzan.setClickable(true);
                        }
                    });
                }
            }
        });
        messageViewHolder.reply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                IntentUtils.openActivity(mContext,);
                ReplyCommentActivity.openReplyCommentActivity(mContext, bean.nickname, mProid, bean._id, bean.uid);
            }
        });
        messageViewHolder.report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listener != null)
                    listener.showReportPopuWindow(10,bean._id);
            }
        });
    }
    private VideoPlayFragment4.ReportListener listener;
    public void setReportListener(VideoPlayFragment4.ReportListener reportListener){
        if(listener == null)
            listener = reportListener;
    }

    private MessageBean.MessagesEntity getItem(int position) {
        return this.messageBeans.get(position);
    }

    @Override
    public int getItemCount() {
        return messageBeans.size();
    }

     public class MessageViewHolder extends RecyclerView.ViewHolder{
         TextView nicknameText;
         TextView commentContant;
         TextView dateText;
         ImageView userAvatar;
         ImageView dianzan;
         TextView dialogList;
         TextView likeCount;
         TextView reply;
         ImageView report;
         public MessageViewHolder(View itemView) {
             super(itemView);
             nicknameText = (TextView) itemView.findViewById(R.id.nickname);
             commentContant = (TextView) itemView.findViewById(R.id.comment_content);
             dateText = (TextView) itemView.findViewById(R.id.create_date);
             userAvatar = (ImageView) itemView.findViewById(R.id.user_icon);
             dianzan = (ImageView) itemView.findViewById(R.id.dianzan);
             dialogList = (TextView) itemView.findViewById(R.id.dialog_list);
             likeCount = (TextView) itemView.findViewById(R.id.like_count);
             reply = (TextView) itemView.findViewById(R.id.reply);
             report = (ImageView) itemView.findViewById(R.id.report);
         }
     }
}
