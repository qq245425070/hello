package cn.gietv.mlive.modules.author.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import cn.gietv.mlive.R;
import cn.gietv.mlive.base.AbsArrayAdapter;
import cn.gietv.mlive.constants.CommConstants;
import cn.gietv.mlive.http.DefaultLiveHttpCallBack;
import cn.gietv.mlive.modules.author.activity.AuthorActivity;
import cn.gietv.mlive.modules.category.activity.CategoryActivity;
import cn.gietv.mlive.modules.follow.model.FollowModel;
import cn.gietv.mlive.modules.login.activity.LoginActivity;
import cn.gietv.mlive.modules.usercenter.bean.UserCenterBean;
import cn.gietv.mlive.utils.DensityUtil;
import cn.gietv.mlive.utils.ImageLoaderUtils;
import cn.gietv.mlive.utils.IntentUtils;
import cn.gietv.mlive.utils.NumUtils;
import cn.gietv.mlive.utils.ToastUtils;
import cn.gietv.mlive.utils.UserUtils;
import cn.gietv.mlive.utils.ViewHolder;
import cn.gietv.mlive.views.FlowLayout;
import cn.kalading.android.retrofit.utils.RetrofitUtils;

/**
 * Created by houde on 2016/5/29.
 */
public class DescriptionAdapter extends AbsArrayAdapter<Object> {
    private final int TYPE_INFO = 0;
    private final int TYPE_TAG = 1;
    private final int TYPE_ANCHOR = 2;
    private ImageLoader mImageLoader;
    private FollowModel model;
    private int type;
    private int flag = -1;
    private Context mContext;
    public DescriptionAdapter(Context context, List<Object> objects) {
        super(context, objects);
        mContext = context;
        mImageLoader = ImageLoaderUtils.getDefaultImageLoader(context);
        model = RetrofitUtils.create(FollowModel.class);
    }

    @Override
    public int getItemViewType(int position) {
        Object o = getItem(position);
        if (position == 0) {
            return TYPE_INFO;
        } else if (o instanceof UserCenterBean.UserinfoEntity) {
            return TYPE_ANCHOR;
        } else {
            return TYPE_TAG;
        }
    }

    @Override
    public int getViewTypeCount() {
        return 3;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Object o = getItem(position);
        switch (getItemViewType(position)) {
            case TYPE_INFO:
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.des_info_item, null);
                break;
            case TYPE_TAG:
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.game_flow_layout, null);
                break;
            case TYPE_ANCHOR:
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.game_info_item_anchor, null);
                break;
        }
        switch (getItemViewType(position)) {
            case TYPE_INFO:
                handleInfo((UserCenterBean.UserinfoEntity) o, convertView);
                type = ((UserCenterBean.UserinfoEntity) o).type;
                break;
            case TYPE_TAG:
                handleLabel((ArrayList<String>) o, convertView);
                break;
            case TYPE_ANCHOR:
                handleAnchor((UserCenterBean.UserinfoEntity) o, convertView,position);
                break;
        }
        return convertView;
    }

    private void handleAnchor(final UserCenterBean.UserinfoEntity bean, View convertView,int position) {
        if(bean == null){
            return;
        }
        ImageView anchorImage = ViewHolder.get(convertView,R.id.anchor_image);
        TextView anchorName = ViewHolder.get(convertView,R.id.anchor_name);
        TextView anchorDesc = ViewHolder.get(convertView,R.id.anchor_desc);
        final TextView anchorFollow = ViewHolder.get(convertView,R.id.anchor_follow);
        final ImageView followImage = ViewHolder.get(convertView,R.id.follow_image);
        TextView tag = ViewHolder.get(convertView,R.id.tag);
        anchorName.setText(bean.nickname);
        anchorFollow.setText(bean.follows + "个粉丝");
        anchorDesc.setText(bean.desc);
        mImageLoader.displayImage(bean.avatar, anchorImage);
        if(flag == -1){
            tag.setVisibility(View.VISIBLE);
            flag = position;
        }else if(position == flag){
            tag.setVisibility(View.VISIBLE);
        }else{
            tag.setVisibility(View.GONE);
        }
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                FastEntryAuthorActivity.openAnchorActivity(getContext(), bean._id, bean.nickname);
                AuthorActivity.openAnchorActivity(bean.nickname, bean._id, getContext(),bean.isfollow);
            }
        });
        if(bean.isfollow == CommConstants.TRUE){
            followImage.setImageResource(R.mipmap.subscribe_over);
        }else {
            followImage.setImageResource(R.mipmap.subscribe_no);
        }
        followImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (UserUtils.isNotLogin()) {
                    IntentUtils.openActivity(getContext(), LoginActivity.class);
                    return;
                }
                if(bean.isfollow == CommConstants.TRUE){
                    model.follow(bean._id, CommConstants.FOLLOW_ACTION_OFF, bean.type, new DefaultLiveHttpCallBack<String>() {
                        @Override
                        public void success(String s) {
                            anchorFollow.setText(String.valueOf(--bean.follows));
                            ToastUtils.showToast(getContext(), "取消关注成功");
                            followImage.setImageResource(R.mipmap.subscribe_no);
                            bean.isfollow = CommConstants.FALSE;
                        }

                        @Override
                        public void failure(String message) {
                            ToastUtils.showToast(getContext(), message);
                        }
                    });
                }else {
                    model.follow(bean._id, CommConstants.FOLLOW_ACTION_ON, bean.type, new DefaultLiveHttpCallBack<String>() {
                        @Override
                        public void success(String s) {
                            anchorFollow.setText(String.valueOf(++bean.follows));
                            ToastUtils.showToast(getContext(), "关注成功");
                            followImage.setImageResource(R.mipmap.subscribe_over);
                            bean.isfollow = CommConstants.TRUE;
                        }

                        @Override
                        public void failure(String message) {
                            ToastUtils.showToast(getContext(), message);
                        }
                    });


                }
            }
        });
    }

    private void handleLabel(final ArrayList<String> labels, View convertView) {
        System.out.println(labels);
        if(labels == null){
            return;
        }
        ViewHolder.get(convertView,R.id.tag).setVisibility(View.VISIBLE);
        FlowLayout rootView = ViewHolder.get(convertView,R.id.flow_layout);
        ViewGroup.MarginLayoutParams lp = new ViewGroup.MarginLayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.leftMargin = DensityUtil.dip2px(mContext,8);
        lp.topMargin = DensityUtil.dip2px(mContext,6);
        lp.bottomMargin = DensityUtil.dip2px(mContext,6);
        for(int i = 0;i<labels.size();i++){
            TextView textView = (TextView) LayoutInflater.from(getContext()).inflate(R.layout.flowlayout_text,null);
            if(labels.get(i)  == null){
                continue;
            }
            textView.setText(labels.get(i));
            textView.setTextColor(getContext().getResources().getColor(R.color.text_color_101010));
            textView.setBackgroundResource(R.drawable.commen_button_theme_color5);
            rootView.addView(textView, lp);
            final int finalI = i;
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    TagSearchActivity.openTagSearchActivity(getContext(),labels.get(finalI),type);
                    CategoryActivity.openActivity(getContext(),labels.get(finalI),labels.get(finalI),0);
                }
            });
        }
    }

    private void handleInfo(UserCenterBean.UserinfoEntity bean, View convertView) {
        if (bean == null) {
            return;
        }
        TextView albumName = ViewHolder.get(convertView, R.id.video_name);
        TextView videoCount = ViewHolder.get(convertView, R.id.video_count);
        TextView playCount = ViewHolder.get(convertView, R.id.play_count);
        albumName.setText(bean.nickname);
        videoCount.setText(bean.follows + "个视频");
        playCount.setText(NumUtils.w(bean.onlines));
    }
}
