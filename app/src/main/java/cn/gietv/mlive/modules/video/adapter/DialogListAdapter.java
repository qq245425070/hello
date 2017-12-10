package cn.gietv.mlive.modules.video.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

import cn.gietv.mlive.R;
import cn.gietv.mlive.modules.compere.activity.CompereActivity;
import cn.gietv.mlive.modules.video.activity.DialogListActivity;
import cn.gietv.mlive.modules.video.activity.ReplyCommentActivity;
import cn.gietv.mlive.modules.video.bean.MessageBean;
import cn.gietv.mlive.utils.ImageLoaderUtils;
import cn.gietv.mlive.utils.TimeUtil;

/**
 * Created by houde on 2016/6/27.
 */
public class DialogListAdapter extends RecyclerView.Adapter<DialogListAdapter.MessageViewHolder> {
    private ImageLoader mImageLoader;
    private String mProid;
    private Context mContext;
    private List<MessageBean.MessagesEntity> mData;
    public DialogListAdapter(Context context, List<MessageBean.MessagesEntity> objects,String proid) {
        mImageLoader = ImageLoaderUtils.getDefaultImageLoader(context);
        this.mProid = proid;
        mContext = context;
        mData = objects;
    }

    public void getView(int position,MessageViewHolder messageViewHolder) {
        final MessageBean.MessagesEntity bean = getItem(position);
        messageViewHolder.commentContant.setText(bean.message);
        messageViewHolder.dateText.setText(TimeUtil.format(bean.date));
        messageViewHolder.nicknameText.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        messageViewHolder.nicknameText.setText(bean.nickname);
        messageViewHolder.dianzan.setVisibility(View.INVISIBLE);
        messageViewHolder.dialogList.setVisibility(View.INVISIBLE);
        mImageLoader.displayImage(bean.avatar, messageViewHolder.userAvatar);
        messageViewHolder.userAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CompereActivity.openCompereActivity(mContext, bean.uid);
            }
        });
        if("yes".equals(bean.isreply)){
            messageViewHolder.nicknameText.setText(bean.nickname+"  回复  " + bean.replyuser.nickname);
            messageViewHolder.dialogList.setVisibility(View.VISIBLE);
        }else{
            messageViewHolder.nicknameText.setText(bean.nickname);
            messageViewHolder.dialogList.setVisibility(View.INVISIBLE);
        }

        messageViewHolder.userAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CompereActivity.openCompereActivity(mContext, bean.uid);
            }
        });
        messageViewHolder.dialogList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogListActivity.openDialogListActivity(mContext, mProid, bean._id);
            }
        });
        messageViewHolder.reply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ReplyCommentActivity.openReplyCommentActivity(mContext, bean.nickname, mProid, bean._id, bean.uid);
            }
        });
    }

    private MessageBean.MessagesEntity getItem(int position) {
        return mData.get(position);
    }

    @Override
    public MessageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MessageViewHolder(LayoutInflater.from(mContext).inflate(R.layout.comment_message_item,null));
    }

    @Override
    public void onBindViewHolder(MessageViewHolder holder, int position) {
        getView(position,holder);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

   public class MessageViewHolder extends RecyclerView.ViewHolder {
        TextView nicknameText;
        TextView commentContant;
        TextView dateText;
        ImageView userAvatar;
        ImageView dianzan;
        TextView dialogList;
        TextView likeCount;
        TextView reply;
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
        }
    }
}
