package cn.gietv.mlive.modules.category.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.siyamed.shapeimageview.RoundedImageView;

import java.util.ArrayList;
import java.util.List;

import cn.gietv.mlive.R;
import cn.gietv.mlive.constants.CommConstants;
import cn.gietv.mlive.db.DBUtils;
import cn.gietv.mlive.http.DefaultLiveHttpCallBack;
import cn.gietv.mlive.modules.album.activity.AlbumActivity;
import cn.gietv.mlive.modules.author.activity.AuthorActivity;
import cn.gietv.mlive.modules.follow.model.FollowModel;
import cn.gietv.mlive.modules.game.bean.GameInfoBean;
import cn.gietv.mlive.modules.login.activity.LoginActivity;
import cn.gietv.mlive.modules.program.bean.ProgramBean;
import cn.gietv.mlive.modules.usercenter.bean.UserCenterBean;
import cn.gietv.mlive.modules.video.activity.VideoPlayListActivity3;
import cn.gietv.mlive.utils.DensityUtil;
import cn.gietv.mlive.utils.ImageLoaderUtils;
import cn.gietv.mlive.utils.IntentUtils;
import cn.gietv.mlive.utils.NumUtils;
import cn.gietv.mlive.utils.ToastUtils;
import cn.gietv.mlive.utils.UserUtils;
import cn.gietv.mlive.utils.ViewHolder;
import cn.gietv.mlive.views.FlowLayout;
import cn.gietv.mlive.views.SquareImageView2;
import cn.kalading.android.retrofit.utils.RetrofitUtils;

/**
 * Created by houde on 2016/5/27.
 */
public class CategoryAdapter2 extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private static final int TYPE_LABEL = 0;
    private static final int TYPE_VIDEO = 1;
    private static final int TYPE_ALBUM = 2;
    private static final int TYPE_ANCHOR = 3;
    private static final int TYPE_HEADER = 4;
    private ImageLoaderUtils mImageLoader;
    private List<Object> objects;
    private FollowModel model;
    private Context mContext;
    private TextView tempText;
    private int type;
    private int positionFlag;
    public CategoryAdapter2(Context context, List<Object> objects, int positionFlag) {
        this.mContext = context;
        mImageLoader = ImageLoaderUtils.getInstance();
        this.objects = objects;
        model = RetrofitUtils.create(FollowModel.class);
        this.positionFlag = positionFlag;
    }

    @Override
    public int getItemCount() {
        return objects == null ? 1 : objects.size() + 1;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch ( viewType){
            case TYPE_LABEL:
                return new LableViewHolder(LayoutInflater.from(mContext).inflate(R.layout.game_flow_layout,null));
            case TYPE_VIDEO:
                return new VideoViewHolder(LayoutInflater.from(mContext).inflate(R.layout.game_info_item_layout3,null));
            case TYPE_ALBUM:
                return new AlbumViewHolder(LayoutInflater.from(mContext).inflate(R.layout.category_album_item,null));
            case TYPE_ANCHOR:
                return new AuchorViewHolder(LayoutInflater.from(mContext).inflate(R.layout.game_info_item_anchor,null));
            case TYPE_HEADER:
                View view = LayoutInflater.from(mContext).inflate(R.layout.recycler_header, parent, false);
                return new RecyclerHeaderViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(position > 0) {
            Object object = getItem(position - 1);
            if (holder instanceof LableViewHolder) {
                handleLabel((ArrayList<String>) object, holder.itemView);
            } else if (holder instanceof VideoViewHolder) {
                handleVideo((ProgramBean.ProgramEntity) object, holder.itemView);
                if (type == 0)
                    type = ((ProgramBean.ProgramEntity) object).type;
            } else if (holder instanceof AuchorViewHolder) {
                handleAnchor((UserCenterBean.UserinfoEntity) object, holder.itemView);
                if (type == 0)
                    type = ((UserCenterBean.UserinfoEntity) object).type;
            } else if (holder instanceof AlbumViewHolder) {
                handleAlbum((GameInfoBean.GameInfoEntity) object, holder.itemView);
                if (type == 0)
                    type = ((GameInfoBean.GameInfoEntity) object).type;
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        if(position == 0)
            return TYPE_HEADER;
        Object object = getItem(position-1);
        if(object.getClass().isArray()){
            return TYPE_LABEL;
        }else if(object instanceof ProgramBean.ProgramEntity){
            return TYPE_VIDEO;
        }else if(object instanceof  GameInfoBean.GameInfoEntity){
            return TYPE_ALBUM;
        }else if(object instanceof UserCenterBean.UserinfoEntity){
            return TYPE_ANCHOR;
        }
        return TYPE_LABEL;
    }

    private Object getItem(int position) {
        return objects.get(position);
    }

    private void handleAnchor(final UserCenterBean.UserinfoEntity bean, View convertView) {
        if(bean == null){
            return;
        }
        ImageView anchorImage = ViewHolder.get(convertView,R.id.anchor_image);
        TextView anchorName = ViewHolder.get(convertView,R.id.anchor_name);
        TextView anchorDesc = ViewHolder.get(convertView,R.id.anchor_desc);
        final TextView anchorFollow = ViewHolder.get(convertView,R.id.anchor_follow);
        final ImageView followImage = ViewHolder.get(convertView,R.id.follow_image);
        anchorName.setText(bean.nickname);
        anchorFollow.setText(bean.follows + "个粉丝");
        anchorDesc.setText(bean.desc);
        if(bean.avatar != null && bean.avatar.equals(anchorImage.getTag())){

        }else {
            mImageLoader.downLoadImage(mContext, anchorImage, bean.avatar, false);
            anchorImage.setTag(bean.avatar);
        }
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AuthorActivity.openAnchorActivity(bean.nickname, bean._id, mContext,bean.isfollow);
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
                    IntentUtils.openActivity(mContext, LoginActivity.class);
                    return;
                }
                followImage.setClickable(false);
                if(bean.isfollow == CommConstants.TRUE){
                    model.follow(bean._id, CommConstants.FOLLOW_ACTION_OFF, bean.type, new DefaultLiveHttpCallBack<String>() {
                        @Override
                        public void success(String s) {
                            anchorFollow.setText(--bean.follows+"个粉丝");
                            ToastUtils.showToast(mContext, "取消订阅成功");
                            followImage.setImageResource(R.mipmap.subscribe_no);
                            bean.isfollow = CommConstants.FALSE;
                            followImage.setClickable(true);
                        }

                        @Override
                        public void failure(String message) {
                            ToastUtils.showToast(mContext, message);
                        }
                    });
                }else {
                    followImage.setClickable(false);
                    model.follow(bean._id, CommConstants.FOLLOW_ACTION_ON, bean.type, new DefaultLiveHttpCallBack<String>() {
                        @Override
                        public void success(String s) {
                            anchorFollow.setText(++bean.follows+"个粉丝");
                            ToastUtils.showToast(mContext, "订阅成功");
                            followImage.setImageResource(R.mipmap.subscribe_over);
                            bean.isfollow = CommConstants.TRUE;
                            followImage.setClickable(true);
                        }

                        @Override
                        public void failure(String message) {
                            ToastUtils.showToast(mContext, message);
                        }
                    });


                }
            }
        });
    }

    private void handleAlbum(final GameInfoBean.GameInfoEntity bean, View convertView) {
        if (bean == null) {
            return;
        }
        RoundedImageView image = ViewHolder.get(convertView, R.id.game_info_iv_image);
        TextView name = ViewHolder.get(convertView, R.id.album_name);
        TextView anchor = ViewHolder.get(convertView, R.id.album_anchor);
        TextView count = ViewHolder.get(convertView,R.id.play_count);
        TextView videoCount = ViewHolder.get(convertView,R.id.video_count);
        View line = ViewHolder.get(convertView,R.id.line3);
        line.setVisibility(View.VISIBLE);
        if(bean.author == null){
            return;
        }
        anchor.setText(bean.author.nickname);
        count.setText(String.valueOf(bean.onlines));
        if(bean.img != null && bean.img.equals(image.getTag())){

        }else {
            mImageLoader.downLoadImage(mContext, image, bean.img, false);
            image.setTag(bean.img);
        }
        videoCount.setText(bean.programnums + "个视频");
        name.setText(bean.name);
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlbumActivity.openAlbumActivity(bean.name, bean._id, mContext);
            }
        });
    }

    private void handleVideo(final ProgramBean.ProgramEntity bean, View convertView) {
        if (bean == null) {
            return;
        }
        SquareImageView2 image = ViewHolder.get(convertView, R.id.game_info_iv_image);
        final TextView name = ViewHolder.get(convertView, R.id.album_name);
        TextView anchor = ViewHolder.get(convertView, R.id.album_anchor);
        TextView count = ViewHolder.get(convertView,R.id.play_count);
        if(bean.userinfo == null){
            return;
        }
        anchor.setText(bean.userinfo.nickname);
        count.setText(NumUtils.w(bean.onlines));
        if( bean.spic != null &&bean.spic.equals(image.getTag())){

        }else {
            mImageLoader.downLoadImage(mContext, image, bean.spic, false);
            image.setTag(bean.spic);
        }
        if(bean.name != null && bean.name.length() > 40)
            bean.name = bean.name.substring(0,40)  + "……";
        name.setText(bean.name);
        if(null != DBUtils.getInstance(mContext).getVideoById(bean._id)){
            name.setTextColor(mContext.getResources().getColor(R.color.text_929292));
        }
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VideoPlayListActivity3.openVideoPlayListActivity2(mContext, bean);
                name.setTextColor(mContext.getResources().getColor(R.color.text_929292));
            }
        });
    }
    private FlowLayout mFlowLayout;
    public void setBg(int position){
        if(mFlowLayout != null){
            positionFlag = position;
            if(position >= mFlowLayout.getChildCount() -1){
                if(tempText != null){
                    tempText.setTextColor(Color.parseColor("#101010"));
                    tempText.setBackgroundResource(R.drawable.commen_button_theme_color5);
                }
                return ;
            }
            TextView childAt = (TextView) mFlowLayout.getChildAt(position);
            if(tempText != null){
                tempText.setTextColor(Color.parseColor("#101010"));
                tempText.setBackgroundResource(R.drawable.commen_button_theme_color5);
            }
            childAt.setTextColor(Color.parseColor("#4fc396"));
            childAt.setBackgroundResource(R.drawable.commen_button_light_green2);
            tempText = childAt;

        }
    }
    private void handleLabel(final ArrayList<String> labels, final View convertView) {
        if(labels == null){
            return;
        }
        System.out.println(labels);
        ViewHolder.get(convertView,R.id.line).setVisibility(View.INVISIBLE);
        FlowLayout rootView = ViewHolder.get(convertView,R.id.flow_layout);
        rootView.removeAllViews();
        mFlowLayout = rootView;
        ViewGroup.MarginLayoutParams lp = new ViewGroup.MarginLayoutParams(
                LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
        lp.leftMargin = DensityUtil.dip2px(mContext,8);
        lp.topMargin = DensityUtil.dip2px(mContext,6);
        lp.bottomMargin = DensityUtil.dip2px(mContext,6);
        if(!labels.get(0).equals("全部"))
            labels.add(0,"全部");
        for (int i = 0;i < labels.size();i ++){
            if(i == 10){
                TextView textView = (TextView) LayoutInflater.from(mContext).inflate(R.layout.flowlayout_text,null);
                textView.setText("更多");
                textView.setTextColor(Color.parseColor("#4fc396"));
                textView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (showPopuWindowListener != null) {
                            showPopuWindowListener.showPopuWindow(convertView.getHeight());
                        }
                    }
                });
                rootView.addView(textView, lp);
                break;
            }
            final TextView textView = (TextView) LayoutInflater.from(mContext).inflate(R.layout.flowlayout_text,null);
            textView.setText(labels.get(i));
            textView.setTextColor(mContext.getResources().getColor(R.color.text_color_101010));
            if(positionFlag == i){
                textView.setBackgroundResource(R.drawable.commen_button_light_green2);
                textView.setTextColor(Color.parseColor("#4fc396"));
                tempText = textView;
            }else {
                textView.setBackgroundResource(R.drawable.commen_button_theme_color5);
            }
            final int finalI = i;
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (tempText != null) {
                        tempText.setBackgroundResource(R.drawable.commen_button_theme_color5);
                        tempText.setTextColor(mContext.getResources().getColor(R.color.text_color_101010));
                    }
                    textView.setBackgroundResource(R.drawable.commen_button_light_green2);
                    textView.setTextColor(Color.parseColor("#4fc396"));
                    tempText = textView;
                    positionFlag = finalI;
                    if(adapterListener != null){
                        adapterListener.changeData(labels.get(finalI),finalI);
                    }
                }
            });
            rootView.addView(textView, lp);
        }
    }
    private CategoryAdapter.AdapterListener adapterListener;
    private CategoryAdapter.ShowPopuWindowListener showPopuWindowListener;
    public void setAdapterListener(CategoryAdapter.AdapterListener listener){
        this.adapterListener = listener;
    }
    public void setShowPopuWindowListener(CategoryAdapter.ShowPopuWindowListener listener){
        this.showPopuWindowListener = listener;
    }
    class LableViewHolder extends RecyclerView.ViewHolder{
        public LableViewHolder(View itemView) {
            super(itemView);
        }
    }
    class VideoViewHolder extends RecyclerView.ViewHolder{
        public VideoViewHolder(View itemView) {
            super(itemView);
        }
    }
    class AuchorViewHolder extends RecyclerView.ViewHolder {
        public AuchorViewHolder(View itemView) {
            super(itemView);
        }
    }
    class AlbumViewHolder extends RecyclerView.ViewHolder {
        public AlbumViewHolder(View itemView) {
            super(itemView);
        }
    }
    class RecyclerHeaderViewHolder extends RecyclerView.ViewHolder {

        public RecyclerHeaderViewHolder(View itemView) {
            super(itemView);
        }
    }

}
