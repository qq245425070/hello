package cn.gietv.mlive.modules.video.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

import cn.gietv.mlive.R;
import cn.gietv.mlive.base.AbsArrayAdapter;
import cn.gietv.mlive.modules.compere.activity.CompereActivity;
import cn.gietv.mlive.modules.video.bean.MessageBean;
import cn.gietv.mlive.utils.ImageLoaderUtils;
import cn.gietv.mlive.utils.StringUtils;
import cn.gietv.mlive.utils.ViewHolder;

/**
 * author：steven
 * datetime：15/10/8 22:27
 */
public class MessageAdapter extends AbsArrayAdapter<MessageBean.MessagesEntity> {
    private ImageLoader mImageLoader;
    private boolean flag;
    private String title;
    public MessageAdapter(Context context, List<MessageBean.MessagesEntity> objects,boolean flag,String title) {
        super(context, objects);
        mImageLoader = ImageLoaderUtils.getDefaultImageLoader(getContext());
        this.flag = flag;
        this.title = title;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final MessageBean.MessagesEntity messagesEntity = getItem(position);
        if (convertView == null) {
            if(getItemViewType(position) == 0) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.video_comment_item_layout, null);
            }else{
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.video_comment_item_luobo,null);
            }
        }
        if(getItemViewType(position) == 0){
            initLiveMessage(convertView,position,messagesEntity);
        }else{
            initLuoboMessage(convertView,position,messagesEntity);
        }
        return convertView;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        if(flag)
            return 0;
        else
            return 1;

    }

    private void initLuoboMessage(View convertView, int position, final MessageBean.MessagesEntity messagesEntity) {
        TextView nickName = ViewHolder.get(convertView, R.id.video_tv_name);
        TextView message = ViewHolder.get(convertView, R.id.video_tv_text);
        nickName.setText(messagesEntity.nickname);
        message.setText(getItem(position).message);
        ImageView image = ViewHolder.get(convertView, R.id.video_iv_head_image);
        if (StringUtils.isEmpty(messagesEntity.avatar)) {
            image.setVisibility(View.GONE);
        } else {
            image.setVisibility(View.VISIBLE);
            mImageLoader.displayImage(getItem(position).avatar, image);
        }
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CompereActivity.openCompereActivity(getContext(), messagesEntity.uid);
            }
        });
    }

    private void initLiveMessage(View convertView, int position, final MessageBean.MessagesEntity messagesEntity) {
        LinearLayout received_message_right = (LinearLayout) convertView.findViewById(R.id.received_message_right);
        LinearLayout received_message_left = (LinearLayout) convertView.findViewById( R.id.received_message_left);
    /*    if(CacheUtils.getCacheUserInfo() != null && CacheUtils.getCacheUserInfo().nickname != null && CacheUtils.getCacheUserInfo().nickname.equals(messagesEntity.nickname)){
            received_message_left.setVisibility(View.GONE);
            received_message_right.setVisibility(View.VISIBLE);
            ImageView image = ViewHolder.get(convertView, cn.gietv.mlive.R.id.video_iv_head_image_right);
            if (StringUtils.isEmpty(messagesEntity.avatar)) {
                image.setVisibility(View.GONE);
            } else {
                image.setVisibility(View.VISIBLE);
                mImageLoader.displayImage(getItem(position).avatar, image);
            }
            TextView nickName = ViewHolder.get(convertView, cn.gietv.mlive.R.id.video_tv_name_right);
            TextView message = ViewHolder.get(convertView, cn.gietv.mlive.R.id.video_tv_text_right);
            TextView jinjiaoText = ViewHolder.get(convertView, R.id.jinjiao_title_right);
            TextView coinLogoText = ViewHolder.get(convertView,R.id.acoin_title_right);
            coinLogoText.setText(title);
            ImageView jinjiaoLogo = ViewHolder.get(convertView,R.id.jinjiao_logo_right);
            ImageView coinLogo = ViewHolder.get(convertView,R.id.acoin_logo_right);
            RelativeLayout jinjiaoParent = ViewHolder.get(convertView,R.id.jinjiao_parent_right);
            RelativeLayout acoinParent = ViewHolder.get(convertView,R.id.acoin_parent_right);
            setBackground(messagesEntity.jinjiaoImg,jinjiaoLogo,jinjiaoParent);
            setBackground(messagesEntity.acoinImg,coinLogo,acoinParent);
            nickName.setText(messagesEntity.nickname);
            message.setText(getItem(position).message);
            image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    CompereActivity.openCompereActivity(getContext(), messagesEntity.uid);
                }
            });
           // received_message_right_parent.setBackgroundDrawable(getContext().getResources().getDrawable(cn.gietv.mlive.R.drawable.message_bg));
        }else {*/
            received_message_left.setVisibility(View.VISIBLE);
            received_message_right.setVisibility(View.GONE);
            ImageView image = ViewHolder.get(convertView, R.id.video_iv_head_image);
            if (StringUtils.isEmpty(messagesEntity.avatar)) {
                image.setVisibility(View.GONE);
            } else {
                image.setVisibility(View.VISIBLE);
                mImageLoader.displayImage(getItem(position).avatar, image);
            }
            image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    CompereActivity.openCompereActivity(getContext(), messagesEntity.uid);
                }
            });
                TextView nickName = ViewHolder.get(convertView, R.id.video_tv_name);
                TextView message = ViewHolder.get(convertView, R.id.video_tv_text);
                TextView jinjiaoText = ViewHolder.get(convertView, R.id.jinjiao_title);
                TextView coinLogoText = ViewHolder.get(convertView,R.id.acoin_title);
                coinLogoText.setText(title);
                ImageView propImg = ViewHolder.get(convertView,R.id.propimg);
                if(messagesEntity.propimg != null){
                    mImageLoader.displayImage(messagesEntity.propimg,propImg);
                 }else{
                    propImg.setVisibility(View.INVISIBLE);
                }
                ImageView jinjiaoLogo = ViewHolder.get(convertView,R.id.jinjiao_logo);
                ImageView coinLogo = ViewHolder.get(convertView,R.id.acoin_logo);
                RelativeLayout jinjiaoParent = ViewHolder.get(convertView,R.id.jinjiao_parent);
                RelativeLayout acoinParent = ViewHolder.get(convertView,R.id.acoin_parent);
                setBackground(messagesEntity.jinjiaoImg,jinjiaoLogo,jinjiaoParent);
                setBackground(messagesEntity.acoinImg, coinLogo, acoinParent);
                nickName.setText(messagesEntity.nickname);
                message.setText(getItem(position).message);
                //received_message_parent.setBackgroundDrawable(getContext().getResources().getDrawable(cn.gietv.mlive.R.drawable.message_send_bg));
       // }
    }
    private void setBackground(String url,ImageView imageView,ViewGroup parentView){
        if(url == null || "".equals(url)){
            parentView.setVisibility(View.GONE);
        }else{
            parentView.setVisibility(View.VISIBLE);
            mImageLoader.displayImage(url,imageView);
        }
    }
}
