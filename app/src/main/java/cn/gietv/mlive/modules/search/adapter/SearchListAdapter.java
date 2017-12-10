package cn.gietv.mlive.modules.search.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.siyamed.shapeimageview.RoundedImageView;

import java.util.List;

import cn.gietv.mlive.R;
import cn.gietv.mlive.constants.CommConstants;
import cn.gietv.mlive.db.DBUtils;
import cn.gietv.mlive.http.DefaultLiveHttpCallBack;
import cn.gietv.mlive.modules.album.activity.AlbumActivity;
import cn.gietv.mlive.modules.author.activity.AuthorActivity;
import cn.gietv.mlive.modules.follow.model.FollowModel;
import cn.gietv.mlive.modules.login.activity.LoginActivity;
import cn.gietv.mlive.modules.program.bean.ProgramBean;
import cn.gietv.mlive.modules.search.bean.SearchBean;
import cn.gietv.mlive.modules.video.activity.VideoPlayListActivity3;
import cn.gietv.mlive.utils.ImageLoaderUtils;
import cn.gietv.mlive.utils.IntentUtils;
import cn.gietv.mlive.utils.NumUtils;
import cn.gietv.mlive.utils.ToastUtils;
import cn.gietv.mlive.utils.UserUtils;
import cn.gietv.mlive.utils.ViewHolder;
import cn.gietv.mlive.views.SquareImageView2;
import cn.kalading.android.retrofit.utils.RetrofitUtils;

/**
 * author：steven
 * datetime：15/10/4 10:41
 */
public class SearchListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final int TYPE_PROGRAM = 0;
    private final int TYPE_ALBUM = 1;
    private final int TYPE＿AUTHOR = 2;
    private ImageLoaderUtils mImageloader;
    private FollowModel model;
    private Context mContext;
    private List<SearchBean.SearchEnitity> searchEnitities;
    public SearchListAdapter(Context context, List<SearchBean.SearchEnitity> objects) {
        mImageloader = ImageLoaderUtils.getInstance();
        model = RetrofitUtils.create(FollowModel.class);
        searchEnitities = objects;
        this.mContext = context;
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        if(i == TYPE_PROGRAM ){
            return new VideoViewHolder(LayoutInflater.from(mContext).inflate(R.layout.game_info_item_layout3,null));
        }else if(i == TYPE_ALBUM ){
            return new AlbumViewHolder(LayoutInflater.from(mContext).inflate(R.layout.category_album_item,null));
        }else if(i == TYPE＿AUTHOR ){
            return new AnchorViewHolder(LayoutInflater.from(mContext).inflate(R.layout.game_info_item_anchor,null));
        }
        return null;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder viewHolder, int position) {
        final SearchBean.SearchEnitity bean = searchEnitities.get(position);
        if(viewHolder instanceof VideoViewHolder){
            handleVideo(position,(VideoViewHolder)viewHolder,searchEnitities.get(position));
        }else if (viewHolder instanceof AlbumViewHolder ){
            handleAlbum(position,(AlbumViewHolder)viewHolder,searchEnitities.get(position));
        }else if(viewHolder instanceof AnchorViewHolder){
           handleAuchor(position,((AnchorViewHolder)viewHolder),searchEnitities.get(position));
        }
    }

    private void handleVideo(int position, VideoViewHolder viewHolder, final SearchBean.SearchEnitity bean) {

        viewHolder.anchor.setText(bean.nickname);
        viewHolder.count.setText(NumUtils.w(bean.onlines));
        mImageloader.downLoadImage(mContext, viewHolder.image ,bean.icon,false);
        viewHolder.name.setText(bean.name);
        if(null != DBUtils.getInstance(mContext).getVideoById(bean._id)){
            viewHolder.name.setTextColor(mContext.getResources().getColor(R.color.text_929292));
        }
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProgramBean.ProgramEntity entity = new ProgramBean.ProgramEntity();
                entity._id = bean._id;
                entity.name = bean.name;
                entity.spic = bean.icon;
                VideoPlayListActivity3.openVideoPlayListActivity2(mContext, entity);
            }
        });
    }

    private void handleAlbum(int position, AlbumViewHolder viewHolder,final SearchBean.SearchEnitity bean) {
        viewHolder.anchor.setText(bean.nickname);
        viewHolder.count.setText(String.valueOf(bean.onlines));
        mImageloader.downLoadImage(mContext, viewHolder.image , bean.icon,false);
        viewHolder.videoCount.setText(bean.programnums + "个视频");
        viewHolder.name.setText(bean.name);
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlbumActivity.openAlbumActivity(bean.name, bean._id, mContext);
            }
        });
    }

    private void handleAuchor(int position, final AnchorViewHolder viewHolder,final SearchBean.SearchEnitity bean) {
        viewHolder.anchorName.setText(bean.name);
        viewHolder.anchorFollow.setText(bean.follows + "个粉丝");
        viewHolder.anchorDesc.setText(bean.desc);
        mImageloader.downLoadImage(mContext, viewHolder.anchorImage , bean.icon, false);
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AuthorActivity.openAnchorActivity(bean.name, bean._id, mContext,bean.isfollow);
            }
        });
        if(bean.isfollow == CommConstants.FOLLOW_FALSE){
            viewHolder.followImage.setImageResource(R.drawable.no_concern);
        }else {
            viewHolder.followImage.setImageResource(R.drawable.over_concern);
        }

        viewHolder.followImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (UserUtils.isNotLogin()) {
                    IntentUtils.openActivity(mContext, LoginActivity.class);
                    return;
                }
                viewHolder.followImage.setClickable(false);
                if(bean.isfollow == CommConstants.TRUE){
                    model.follow(bean._id, CommConstants.FOLLOW_ACTION_OFF, bean.type, new DefaultLiveHttpCallBack<String>() {
                        @Override
                        public void success(String s) {
                            viewHolder.anchorFollow.setText(--bean.follows+"个粉丝");
                            ToastUtils.showToast(mContext, "取消关注成功");
                            viewHolder.followImage.setImageResource(R.drawable.no_concern);
                            bean.isfollow = CommConstants.FALSE;
                            viewHolder.followImage.setClickable(true);
                        }

                        @Override
                        public void failure(String message) {
                            ToastUtils.showToast(mContext, message);
                        }
                    });
                }else {
                    viewHolder.followImage.setClickable(false);
                    model.follow(bean._id, CommConstants.FOLLOW_ACTION_ON, bean.type, new DefaultLiveHttpCallBack<String>() {
                        @Override
                        public void success(String s) {
                            viewHolder.anchorFollow.setText(++bean.follows+"个粉丝");
                            ToastUtils.showToast(mContext, "关注成功");
                            viewHolder.followImage.setImageResource(R.drawable.over_concern);
                            bean.isfollow = CommConstants.TRUE;
                            viewHolder.followImage.setClickable(true);
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

    @Override
    public int getItemViewType(int position) {
        SearchBean.SearchEnitity o = searchEnitities.get(position);
        if(o.type == 2 ){
            return TYPE_PROGRAM;
        }else if(o.type == 7){
            return TYPE_ALBUM;
        }else{
            return TYPE＿AUTHOR;
        }
    }

    @Override
    public int getItemCount() {
        return searchEnitities.size();
    }
    class AnchorViewHolder extends RecyclerView.ViewHolder{
        ImageView anchorImage;
        TextView anchorName;
        TextView anchorDesc;
        TextView anchorFollow;
        ImageView followImage;
        public AnchorViewHolder(View itemView) {
            super(itemView);
            anchorImage = ViewHolder.get(itemView, R.id.anchor_image);
            anchorName = ViewHolder.get(itemView,R.id.anchor_name);
            anchorDesc = ViewHolder.get(itemView,R.id.anchor_desc);
            anchorFollow = ViewHolder.get(itemView,R.id.anchor_follow);
            followImage = ViewHolder.get(itemView,R.id.follow_image);
        }
    }
    class AlbumViewHolder extends RecyclerView.ViewHolder{
        TextView name;
        RoundedImageView image;
        TextView anchor;
        TextView count;
        TextView videoCount;
        public AlbumViewHolder(View itemView) {
            super(itemView);
            image = ViewHolder.get(itemView, R.id.game_info_iv_image);
            name = ViewHolder.get(itemView, R.id.album_name);
            anchor = ViewHolder.get(itemView, R.id.album_anchor);
            count = ViewHolder.get(itemView,R.id.play_count);
            videoCount = ViewHolder.get(itemView,R.id.video_count);
        }
    }
    class VideoViewHolder extends RecyclerView.ViewHolder{
        TextView name;
        SquareImageView2 image;
        TextView anchor;
        TextView count;
        public VideoViewHolder(View itemView) {
            super(itemView);
            image = ViewHolder.get(itemView, R.id.game_info_iv_image);
            name = ViewHolder.get(itemView, R.id.album_name);
            anchor = ViewHolder.get(itemView, R.id.album_anchor);
            count = ViewHolder.get(itemView,R.id.play_count);
        }
    }
//    public SearchListAdapter(Context context, List<SearchBean.SearchEnitity> objects) {
//        super(context, objects);
//        mImageloader = ImageLoaderUtils.getDefaultImageLoader(getContext());
//        model = RetrofitUtils.create(FollowModel.class);
//    }
//
//    @Override
//    public int getViewTypeCount() {
//        return 3;
//    }
//
//
//    @Override
//    public int getItemViewType(int position) {
//        SearchBean.SearchEnitity o = getItem(position);
//        if(o.type == 2 ){
//            return TYPE_PROGRAM;
//        }else if(o.type == 7){
//            return TYPE_ALBUM;
//        }else{
//            return TYPE＿AUTHOR;
//        }
//    }
//
//    @Override
//    public View getView(int position, View convertView, ViewGroup parent) {
//        SearchBean.SearchEnitity o = getItem(position);
//        switch (getItemViewType(position)){
//            case TYPE_PROGRAM:
//                convertView = LayoutInflater.from(getContext()).inflate(R.layout.game_info_item_layout3,null);
//                handleVideo(o,convertView);
//                break;
//            case TYPE_ALBUM:
//                convertView = LayoutInflater.from(getContext()).inflate(R.layout.category_album_item,null);
//                handleAlbum(o,convertView);
//                break;
//            case TYPE＿AUTHOR:
//                convertView = LayoutInflater.from(getContext()).inflate(R.layout.game_info_item_anchor,null);
//                handleAnchor(o,convertView);
//                break;
//        }
////
//        return convertView;
//    }
//    private void handleAnchor(final SearchBean.SearchEnitity bean, View convertView) {
//        if(bean == null){
//            return;
//        }
//        ImageView anchorImage = ViewHolder.get(convertView,R.id.anchor_image);
//        TextView anchorName = ViewHolder.get(convertView,R.id.anchor_name);
//        TextView anchorDesc = ViewHolder.get(convertView,R.id.anchor_desc);
//        final TextView anchorFollow = ViewHolder.get(convertView,R.id.anchor_follow);
//        final ImageView followImage = ViewHolder.get(convertView,R.id.follow_image);
//        anchorName.setText(bean.name);
//        anchorFollow.setText(bean.follows + "个粉丝");
//        anchorDesc.setText(bean.desc);
//        mImageloader.displayImage(bean.icon, anchorImage);
//        convertView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                FastEntryAuthorActivity.openAnchorActivity(getContext(), bean._id, bean.nickname);
//                FastEntryAuthorActivity.openAnchorActivity(bean.name, bean._id, getContext());
//            }
//        });
//        if(bean.isfollow == CommConstants.FOLLOW_FALSE){
//            followImage.setImageResource(R.drawable.no_concern);
//        }else {
//            followImage.setImageResource(R.drawable.over_concern);
//        }
//
//        followImage.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (UserUtils.isNotLogin()) {
//                    IntentUtils.openActivity(getContext(), LoginActivity.class);
//                    return;
//                }
//                followImage.setClickable(false);
//                if(bean.isfollow == CommConstants.TRUE){
//                    model.follow(bean._id, CommConstants.FOLLOW_ACTION_OFF, bean.type, new DefaultLiveHttpCallBack<String>() {
//                        @Override
//                        public void success(String s) {
//                            anchorFollow.setText(--bean.follows+"个粉丝");
//                            ToastUtils.showToast(getContext(), "取消关注成功");
//                            followImage.setImageResource(R.drawable.no_concern);
//                            bean.isfollow = CommConstants.FALSE;
//                            followImage.setClickable(true);
//                        }
//
//                        @Override
//                        public void failure(String message) {
//                            ToastUtils.showToast(getContext(), message);
//                        }
//                    });
//                }else {
//                    followImage.setClickable(false);
//                    model.follow(bean._id, CommConstants.FOLLOW_ACTION_ON, bean.type, new DefaultLiveHttpCallBack<String>() {
//                        @Override
//                        public void success(String s) {
//                            anchorFollow.setText(++bean.follows+"个粉丝");
//                            ToastUtils.showToast(getContext(), "关注成功");
//                            followImage.setImageResource(R.drawable.over_concern);
//                            bean.isfollow = CommConstants.TRUE;
//                            followImage.setClickable(true);
//                        }
//
//                        @Override
//                        public void failure(String message) {
//                            ToastUtils.showToast(getContext(), message);
//                        }
//                    });
//
//
//                }
//            }
//        });
//    }
//
//    private void handleAlbum(final SearchBean.SearchEnitity bean, View convertView) {
//        if (bean == null) {
//            return;
//        }
//        RoundedImageView image = ViewHolder.get(convertView, R.id.game_info_iv_image);
//        TextView name = ViewHolder.get(convertView, R.id.album_name);
//        TextView anchor = ViewHolder.get(convertView, R.id.album_anchor);
//        TextView count = ViewHolder.get(convertView,R.id.play_count);
//        TextView videoCount = ViewHolder.get(convertView,R.id.video_count);
//        anchor.setText(bean.nickname);
//        count.setText(String.valueOf(bean.onlines));
//        mImageloader.displayImage(bean.icon, image);
//        videoCount.setText(bean.programnums + "个视频");
//        name.setText(bean.name);
//        convertView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                AlbumActivity.openAlbumActivity(bean.name, bean._id, getContext());
//            }
//        });
//    }
//    private void handleVideo(final SearchBean.SearchEnitity bean, View convertView) {
//        if (bean == null) {
//            return;
//        }
//        SquareImageView2 image = ViewHolder.get(convertView, R.id.game_info_iv_image);
//        TextView name = ViewHolder.get(convertView, R.id.album_name);
//        TextView anchor = ViewHolder.get(convertView, R.id.album_anchor);
//        TextView count = ViewHolder.get(convertView,R.id.play_count);
//        anchor.setText(bean.nickname);
//        count.setText(NumUtils.w(bean.onlines));
//        mImageloader.displayImage(bean.icon, image);
//        name.setText(bean.name);
//        convertView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                ProgramBean.ProgramEntity entity = new ProgramBean.ProgramEntity();
//                entity._id = bean._id;
//                entity.name = bean.name;
//                entity.spic = bean.icon;
//                VideoPlayListActivity2.openVideoPlayListActivity2(getContext(), entity);
//            }
//        });
//    }
}