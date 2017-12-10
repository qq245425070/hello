package cn.gietv.mlive.modules.video.adapter;

import android.content.Context;
import android.graphics.Color;
import android.os.Environment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import cn.gietv.mlive.modules.category.activity.CategoryActivity;
import cn.gietv.mlive.modules.follow.model.FollowModel;
import cn.gietv.mlive.modules.game.bean.GameInfoBean;
import cn.gietv.mlive.modules.login.activity.LoginActivity;
import cn.gietv.mlive.modules.program.bean.ProgramBean;
import cn.gietv.mlive.modules.statistics.StatisticsMode;
import cn.gietv.mlive.modules.video.activity.VideoPlayListActivity3;
import cn.gietv.mlive.modules.video.model.VideoModel;
import cn.gietv.mlive.utils.DensityUtil;
import cn.gietv.mlive.utils.DownLoadVideo;
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
public class VideoPlayDescAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final int TYPE_INFO = 0;
    private final int TYPE_TAG = 1;
    private final int TYPE_ALBUM_PRO = 2;
    private final int TYPE_ALBUM = 3;
    private final int TYPE_PRO = 4;
    private ImageLoaderUtils mImageLoader;
    private int mShowAttentionPosition = -1;
    private int albumTag = -1;
    private int mTag;
    private VideoModel videoModel;
    private Context mContext;
    private TextView temText;
    private List<Object> mData;
    private StatisticsMode mModel;
    class VideoInfoViewHolder extends RecyclerView.ViewHolder {
        public VideoInfoViewHolder(View itemView) {
            super(itemView);
        }
    }
    class TagViewHolder extends RecyclerView.ViewHolder {
        public TagViewHolder(View itemView) {
            super(itemView);
        }
    }
    class AlbumViewHolder extends RecyclerView.ViewHolder {
        public AlbumViewHolder(View itemView) {
            super(itemView);
        }
    }
    class VideoViewHolder extends RecyclerView.ViewHolder {
        public VideoViewHolder(View itemView) {
            super(itemView);
        }
    }

    public VideoPlayDescAdapter(Context context, List<Object> objects,int tag) {
        mImageLoader = ImageLoaderUtils.getInstance();
        this.mTag = tag;
        videoModel = RetrofitUtils.create(VideoModel.class);
        mContext = context;
        this.mData = objects;
        mModel = RetrofitUtils.create(StatisticsMode.class);
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View convertView;
        switch (viewType) {
            case TYPE_INFO:
                convertView = LayoutInflater.from(mContext).inflate(R.layout.video_desc_info_item,null);
                return new VideoInfoViewHolder(convertView);
            case TYPE_TAG:
                convertView = LayoutInflater.from(mContext).inflate(R.layout.game_flow_layout,null);
                return new TagViewHolder(convertView);
            case TYPE_ALBUM_PRO:
                convertView = LayoutInflater.from(mContext).inflate(R.layout.game_info_item_layout2, null);
                return new VideoViewHolder(convertView);
            case TYPE_ALBUM:
                convertView = LayoutInflater.from(mContext).inflate(R.layout.category_album_item, null);
                return new AlbumViewHolder(convertView);
            case TYPE_PRO:
                convertView = LayoutInflater.from(mContext).inflate(R.layout.game_info_item_layout2,null);
                return new VideoViewHolder(convertView);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Object o = getItem(position);
        if(holder instanceof VideoInfoViewHolder){
            handleInfo((ProgramBean.ProgramEntity) o, holder.itemView);
        }else if(holder instanceof VideoViewHolder){
            handleVideo((ProgramBean.ProgramEntity) o, holder.itemView, position);
        }else if(holder instanceof AlbumViewHolder){
            handleAlbum((GameInfoBean.GameInfoEntity) o, holder.itemView, position);
        }else if(holder instanceof TagViewHolder){
            handleLabel((ArrayList<String>) o, holder.itemView);
        }
    }

    @Override
    public int getItemViewType(int position) {
        Object o = getItem(position);
        if(position == 0){
            return TYPE_INFO;
        }else if(o instanceof ProgramBean.ProgramEntity){
            if(position < mTag){
                return TYPE_ALBUM_PRO;
            }else {
                return TYPE_PRO;
            }
        }else if(o instanceof GameInfoBean.GameInfoEntity){
            return TYPE_ALBUM;
        }else {
            return TYPE_TAG;
        }
    }

    private Object getItem(int position) {
        return mData.get(position);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }
    private void handleAlbum(final GameInfoBean.GameInfoEntity bean, View convertView,int position) {
        if (bean == null) {
            return;
        }
        TextView myTag = ViewHolder.get(convertView,R.id.tag);
        RoundedImageView image = ViewHolder.get(convertView, R.id.game_info_iv_image);
        TextView name = ViewHolder.get(convertView, R.id.album_name);
        TextView anchor = ViewHolder.get(convertView, R.id.album_anchor);
        TextView count = ViewHolder.get(convertView,R.id.play_count);
        TextView videoCount = ViewHolder.get(convertView,R.id.video_count);
        View line = ViewHolder.get(convertView,R.id.line);
        View line1 = ViewHolder.get(convertView,R.id.line1);
        View line2 = ViewHolder.get(convertView,R.id.line2);
        line1.setVisibility(View.GONE);
        line2.setVisibility(View.VISIBLE);
        if(bean.author == null){
            return;
        }
        anchor.setText(bean.author.nickname);
        count.setText(NumUtils.w(bean.onlines));
        videoCount.setText(bean.programnums + "个视频");
        mImageLoader.downLoadImage(mContext, image, bean.img,false);
        name.setText(bean.name);
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlbumActivity.openAlbumActivity(bean.name, bean._id, mContext);
            }
        });
        myTag.setTextColor(mContext.getResources().getColor(R.color.text_color_101010));
        if(albumTag == -1){
            myTag.setVisibility(View.VISIBLE);
            albumTag = position;
            line.setVisibility(View.VISIBLE);
            }else if(albumTag == position){
            myTag.setVisibility(View.VISIBLE);
            line.setVisibility(View.VISIBLE);
        }else{
            myTag.setVisibility(View.GONE);
            line.setVisibility(View.GONE);
        }
    }
    private void handleVideo(final ProgramBean.ProgramEntity bean, View convertView,int position) {
        if (bean == null) {
            return;
        }
        View line = ViewHolder.get(convertView,R.id.line);
        line.setVisibility(View.VISIBLE);
        RoundedImageView image = ViewHolder.get(convertView, R.id.game_info_iv_image);
        final TextView name = ViewHolder.get(convertView, R.id.album_name);
        TextView anchor = ViewHolder.get(convertView, R.id.album_anchor);
        TextView count = ViewHolder.get(convertView,R.id.play_count);
        View line1 = ViewHolder.get(convertView,R.id.line1);
//        anchor.setText(bean.userinfo.nickname);
        count.setText(NumUtils.w(bean.onlines));
        mImageLoader.downLoadImage(mContext, image, bean.spic,false);
        name.setText(bean.name);
        if(DBUtils.getInstance(mContext).getVideoById(bean._id) != null){
            name.setTextColor(mContext.getResources().getColor(R.color.text_929292));
        }
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VideoPlayListActivity3.openVideoPlayListActivity2(mContext,bean);
                name.setTextColor(mContext.getResources().getColor(R.color.text_929292));
            }
        });
        TextView myAttention = ViewHolder.get(convertView,R.id.attention);
        if(position < mTag){
            myAttention.setText("同专辑热播");
            myAttention.setTextColor(mContext.getResources().getColor(R.color.text_color_101010));
        }else{
            myAttention.setText("相关视频");
            myAttention.setTextColor(mContext.getResources().getColor(R.color.text_color_101010));
        }
        if (mShowAttentionPosition == -1) {
            myAttention.setVisibility(View.VISIBLE);
            mShowAttentionPosition = position;
        } else if (mShowAttentionPosition == position) {
            myAttention.setVisibility(View.VISIBLE);
        } else {
            if(mTag == position){
                myAttention.setVisibility(View.VISIBLE);
                line1.setVisibility(View.VISIBLE);
            }else {
                myAttention.setVisibility(View.GONE);
            }
        }
    }
    private void handleLabel(final ArrayList<String> labels,View convertView) {
        if(labels == null){
            return;
        }
        ViewHolder.get(convertView,R.id.tag).setVisibility(View.VISIBLE);
        ViewHolder.get(convertView,R.id.line).setVisibility(View.INVISIBLE);
        FlowLayout rootView = ViewHolder.get(convertView,R.id.flow_layout);
        rootView.removeAllViews();
        ViewGroup.MarginLayoutParams lp = new ViewGroup.MarginLayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.leftMargin = DensityUtil.dip2px(mContext,8);
        lp.topMargin = DensityUtil.dip2px(mContext,6);
        lp.bottomMargin = DensityUtil.dip2px(mContext,6);
        for(int i = 0;i<labels.size();i++) {
            final TextView textView = (TextView) LayoutInflater.from(mContext).inflate(R.layout.flowlayout_text, null);
            textView.setText(labels.get(i));
            textView.setTextColor(mContext.getResources().getColor(R.color.text_color_101010));
            textView.setBackgroundResource(R.drawable.commen_button_theme_color5);
            rootView.addView(textView, lp);
            final int finalI = i;

            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(temText != null){
                        temText.setBackgroundResource(R.drawable.commen_button_theme_color5);
                        temText.setTextColor(mContext.getResources().getColor(R.color.text_color_101010));
                    }
                    textView.setBackgroundResource(R.drawable.commen_button_light_green2);
                    textView.setTextColor(Color.parseColor("#4fc396"));
                    temText = textView;
                    CategoryActivity.openActivity(mContext, labels.get(finalI), labels.get(finalI), 0);
                }
            });
        }
    }
    private void handleInfo(final ProgramBean.ProgramEntity bean, View convertView) {
        if (bean == null) {
            return;
        }
        TextView albumName = ViewHolder.get(convertView, R.id.video_name);
        TextView videoCount = ViewHolder.get(convertView, R.id.video_count);
        albumName.setText(bean.name);
        videoCount.setText(NumUtils.w(bean.onlines));
        final ImageView downloadImage = ViewHolder.get(convertView,R.id.download_image);
        ImageView shareImage = ViewHolder.get(convertView,R.id.share_image);
        final ImageView shoucangImage = ViewHolder.get(convertView,R.id.shoucang_image);
        final FollowModel model = RetrofitUtils.create(FollowModel.class);
        if(bean.type != 2){
            shareImage.setVisibility(View.INVISIBLE);
        }
        downloadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(null != DBUtils.getInstance(mContext).getBean(bean.name)){
                    ToastUtils.showToastShort(mContext,"视频已经下载");
                    return;
                }
                if("youku".equals(bean.urlfrom) || "优酷".equals(bean.urlfrom)){
                    ToastUtils.showToastShort(mContext,"亲，此视频不支持下载");
                    return;
                }
                //向服务器发送下载数据
                mModel.sendDownloadData(bean._id, bean.type, new DefaultLiveHttpCallBack<String>() {
                    @Override
                    public void success(String s) {

                    }

                    @Override
                    public void failure(String message) {

                    }
                });
              videoModel.getPlayUrl(bean._id, new DefaultLiveHttpCallBack<String>() {
                  @Override
                  public void success(final String s) {
                      new Thread(){
                          @Override
                          public void run() {
                              DownLoadVideo.getInstance(mContext).getNetwork(Environment.getExternalStoragePublicDirectory("download") + "/" + bean.name, mContext, s, bean.name, bean.spic, bean.userinfo.nickname);
                          }
                      }.start();

                  }

                  @Override
                  public void failure(String message) {

                  }
              });
//
            }
        });
        shareImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null){
                    listener.shareListener(bean);
                }
            }
        });
        if (bean.isfollow == CommConstants.FOLLOW_TRUE) {
            shoucangImage.setImageResource(R.mipmap.shoucang_iamge);
            shoucangImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (UserUtils.isNotLogin()) {
                        IntentUtils.openActivity(mContext, LoginActivity.class);
                        return;
                    }
                    model.follow(bean._id, CommConstants.FOLLOW_FALSE, bean.type, new DefaultLiveHttpCallBack<String>() {
                        @Override
                        public void success(String s) {
                            bean.isfollow = CommConstants.FOLLOW_FALSE;
                            ToastUtils.showToast(mContext, "取消收藏成功");
                            notifyDataSetChanged();
                        }

                        @Override
                        public void failure(String message) {
                            ToastUtils.showToast(mContext, message);
                        }
                    });
                }
            });
        } else {
            shoucangImage.setImageResource(R.mipmap.shoucang_iamge_no);
            shoucangImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (UserUtils.isNotLogin()) {
                        IntentUtils.openActivity(mContext, LoginActivity.class);
                        return;
                    }
                    model.follow(bean._id, CommConstants.FOLLOW_TRUE, bean.type, new DefaultLiveHttpCallBack<String>() {
                        @Override
                        public void success(String s) {
                            bean.isfollow = CommConstants.FOLLOW_TRUE;
                            ToastUtils.showToast(mContext, "收藏成功");
                            notifyDataSetChanged();
                        }

                        @Override
                        public void failure(String message) {
                            ToastUtils.showToast(mContext, message);
                        }
                    });
                }
            });
        }
    }

    public void setShareOnClick(AdapterListener listener){
        this.listener = listener;
    }
    private AdapterListener  listener;
    public interface AdapterListener{
        void shareListener(ProgramBean.ProgramEntity mProgram);
    }
}
