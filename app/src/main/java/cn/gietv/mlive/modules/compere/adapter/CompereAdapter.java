package cn.gietv.mlive.modules.compere.adapter;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.HashMap;
import java.util.List;

import cn.gietv.mlive.R;
import cn.gietv.mlive.constants.CommConstants;
import cn.gietv.mlive.http.DefaultLiveHttpCallBack;
import cn.gietv.mlive.modules.album.activity.AlbumActivity;
import cn.gietv.mlive.modules.follow.model.FollowModel;
import cn.gietv.mlive.modules.game.bean.GameInfoBean;
import cn.gietv.mlive.modules.login.activity.LoginActivity;
import cn.gietv.mlive.modules.news.activity.NewsInfoActivity;
import cn.gietv.mlive.modules.photo.activity.PhotoActivity;
import cn.gietv.mlive.modules.program.bean.ProgramBean;
import cn.gietv.mlive.modules.usercenter.activity.MyConcernActivity;
import cn.gietv.mlive.modules.usercenter.activity.ShouCangActivity;
import cn.gietv.mlive.modules.usercenter.bean.UserCenterBean;
import cn.gietv.mlive.modules.usercenter.fragment.MyConcernFragment;
import cn.gietv.mlive.modules.video.activity.LivePlayListActivity;
import cn.gietv.mlive.modules.video.activity.VideoPlayListActivity3;
import cn.gietv.mlive.utils.ImageLoaderUtils;
import cn.gietv.mlive.utils.IntentUtils;
import cn.gietv.mlive.utils.NumUtils;
import cn.gietv.mlive.utils.ToastUtils;
import cn.gietv.mlive.utils.UserUtils;
import cn.gietv.mlive.utils.ViewHolder;
import cn.gietv.mlive.views.SquareImageView;
import cn.gietv.mlive.views.SquareImageView2;
import cn.kalading.android.retrofit.utils.RetrofitUtils;

/**
 * author：steven
 * datetime：15/10/12 13:02
 */
public class CompereAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int VIEW_TYPE_USERINFO = 0;
    private static final int VIEW_TYPE_ATTENTION = 1;
    private static final int VIEW_TYPE_ALBUM = 2;
    private ImageLoader mImageLoader;
    private List<Object> mData;
    private Context mContext;
    private List<ProgramBean.ProgramEntity> mPrograms;
    private FollowModel followModel;
    public CompereAdapter(Context context, List<Object> objects) {
        mImageLoader = ImageLoaderUtils.getDefaultImageLoader(context);
        mData = objects;
        this.mContext = context;
        followModel = RetrofitUtils.create(FollowModel.class);
    }

    public void setData(List<ProgramBean.ProgramEntity> programEntities){
            mPrograms = programEntities;
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case VIEW_TYPE_ATTENTION:
                return new AttentionViewHolder(LayoutInflater.from(mContext).inflate(R.layout.compere_item_layout, null));
            case VIEW_TYPE_USERINFO:
                return new UserViewHolder(LayoutInflater.from(mContext).inflate(R.layout.compere_head_layout, null));
            case VIEW_TYPE_ALBUM:
                return new AlbumViewHolder(LayoutInflater.from(mContext).inflate(R.layout.game_info_item_layout2,null));
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof UserViewHolder){
            handleUserInfo(holder.itemView, (UserCenterBean.UserinfoEntity) getItem(position));
        }else if(holder instanceof AttentionViewHolder){
            handleAttention(position, holder.itemView, (ProgramBean.ProgramEntity) getItem(position));
        }else if(holder instanceof AlbumViewHolder){
            handleItem(holder.itemView,(GameInfoBean.GameInfoEntity) getItem(position));
        }
    }

    @Override
    public int getItemViewType(int position) {
        Object o = getItem(position);
        if (o instanceof UserCenterBean.UserinfoEntity) {
            return VIEW_TYPE_USERINFO;
        } else if (o instanceof ProgramBean.ProgramEntity) {
            return VIEW_TYPE_ATTENTION;
        }else if( o instanceof GameInfoBean.GameInfoEntity){
            return VIEW_TYPE_ALBUM;
        }
        return VIEW_TYPE_ATTENTION;
    }

    private Object getItem(int position) {
        return mData.get(position);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    private void handleItem( View convertView, final GameInfoBean.GameInfoEntity bean) {
        if (bean == null) {
            return;
        }
        SquareImageView2 image = ViewHolder.get(convertView, R.id.game_info_iv_image);
        TextView name = ViewHolder.get(convertView, R.id.album_name);
        TextView anchor = ViewHolder.get(convertView, R.id.album_anchor);
        mImageLoader.displayImage(bean.spic, image);
        name.setText(bean.name);
        TextView count = ViewHolder.get(convertView,R.id.play_count);
        count.setText(bean.programnums+"个视频");
        // FIXME 点击事件
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlbumActivity.openAlbumActivity(bean.name, bean._id, mContext);
            }
        });
    }
    public void handleUserInfo(View convertView, final UserCenterBean.UserinfoEntity user) {
        if(user == null){
            return;
        }
        ImageView avatarImage = ViewHolder.get(convertView,R.id.user_icon);
        TextView nickName  = ViewHolder.get(convertView,R.id.nickname_user);
        LinearLayout myConcern = ViewHolder.get(convertView,R.id.commen_parent);
        LinearLayout fansParent = ViewHolder.get(convertView,R.id.fans_parent);
        LinearLayout photoParent = ViewHolder.get(convertView,R.id.photo_parent);
        TextView userDesc = ViewHolder.get(convertView,R.id.user_desc);
        TextView followText = ViewHolder.get(convertView,R.id.follow_text);
        TextView fansText = ViewHolder.get(convertView,R.id.fans_text);
        TextView photoText = ViewHolder.get(convertView,R.id.photo_text);
        TextView privateNews = ViewHolder.get(convertView,R.id.private_news);
        final TextView concertText = ViewHolder.get(convertView,R.id.concern);
        LinearLayout commentParent = (LinearLayout) convertView.findViewById(R.id.comment_parent);
        LinearLayout zanPanrent = (LinearLayout)convertView.findViewById(R.id.zan_parent);
        LinearLayout shoucangParent = (LinearLayout)convertView.findViewById(R.id.shoucang_parent);

        mImageLoader.displayImage(user.avatar,avatarImage);
        nickName.setText(user.nickname);
        userDesc.setText(user.desc);
        userDesc.setVisibility(View.INVISIBLE);
        followText.setText(String.valueOf(user.myfollow));
        fansText.setText(String.valueOf(user.follows));
        photoText.setText(String.valueOf(user.photo));
        fansParent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyConcernActivity.openMyConcernActivity(mContext, user._id, user.nickname, MyConcernFragment.FLAG_FANS);
            }
        });
        photoParent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PhotoActivity.openPhotoActivity((Activity) mContext, user._id, user.nickname);
            }
        });
        myConcern.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyConcernActivity.openMyConcernActivity(mContext, user._id, user.nickname, MyConcernFragment.FLAG_FOLLOW);
            }
        });
        privateNews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NewsInfoActivity.openNewsInfoActivity(mContext,user._id,user.nickname,user.avatar);
            }
        });
        shoucangParent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mPrograms == null || mPrograms.size()==0){
                    ToastUtils.showToastShort(mContext,user.nickname+"没有收藏视频");
                }else{
                    ShouCangActivity.openShouCangActivity(mContext,mPrograms,user.nickname);
                }
            }
        });
        if(user.isfollow == CommConstants.FOLLOW_TRUE){
            concertText.setText("已关注");
        }else{
            concertText.setText("+ 关注");
        }
        concertText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (UserUtils.isNotLogin()){
                    IntentUtils.openActivity(mContext, LoginActivity.class);
                    return;
                }
                if (user.isfollow == CommConstants.FOLLOW_TRUE){
                    followModel.followByUser(user._id, CommConstants.FOLLOW_FALSE, new DefaultLiveHttpCallBack<String>() {
                        @Override
                        public void success(String s) {
                            ToastUtils.showToastShort(mContext,"取消关注成功");
                            user.isfollow = CommConstants.FOLLOW_FALSE;
                            concertText.setText("+ 关注");
                        }

                        @Override
                        public void failure(String message) {
                            ToastUtils.showToastShort(mContext,message);
                        }
                    });
                }else{
                    followModel.followByUser(user._id, CommConstants.FOLLOW_TRUE, new DefaultLiveHttpCallBack<String>() {
                        @Override
                        public void success(String s) {
                            ToastUtils.showToastShort(mContext,"关注成功");
                            user.isfollow = CommConstants.FOLLOW_TRUE;
                            concertText.setText("已关注");
                        }

                        @Override
                        public void failure(String message) {
                            ToastUtils.showToastShort(mContext,message);
                        }
                    });
                }
            }
        });
    }

    public void handleAttention(int position, View convertView, final ProgramBean.ProgramEntity program) {
        if(program == null)
            return ;
        SquareImageView image = ViewHolder.get(convertView, R.id.user_center_iv_game_image);
        TextView name = ViewHolder.get(convertView, R.id.user_center_tv_game_name);
        TextView desc = ViewHolder.get(convertView, R.id.user_center_list_tv_game_desc);
        TextView playCount = ViewHolder.get(convertView, R.id.user_center_list_tv_play_count);
        TextView attentionCount = ViewHolder.get(convertView, R.id.user_center_list_tv_attention_count);
        ImageView smal_iv = ViewHolder.get(convertView,R.id.smal_iv);
        attentionCount.setVisibility(View.GONE);
        image.setRatio(1.78F);
        mImageLoader.displayImage(program.spic, image);
        name.setText(program.name);
        if (CommConstants.CAROUSEL_TYPE_LIVE == program.type) {
            ViewHolder.get(convertView, R.id.image_live).setVisibility(View.VISIBLE);
            smal_iv.setBackgroundResource(R.drawable.live_count);
        } else {
            ViewHolder.get(convertView, R.id.image_live).setVisibility(View.GONE);
            smal_iv.setBackgroundResource(R.drawable.sanjiaoxing);
        }
        desc.setText(program.userinfo.nickname );
        playCount.setText(NumUtils.w(program.onlines));
        attentionCount.setText(NumUtils.w(program.follows));

        TextView typeText = ViewHolder.get(convertView, R.id.user_center_tv_type);
        if (mPositionMap.get(position) != null) {
            typeText.setVisibility(View.VISIBLE);
            typeText.setText(mPositionMap.get(position));
        } else {
            typeText.setVisibility(View.GONE);
        }
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (program.type == CommConstants.CAROUSEL_TYPE_LIVE) {
                    Bundle bundle = new Bundle();
                    IntentUtils.openActivity(mContext, LivePlayListActivity.class, bundle);
                } else {
                    VideoPlayListActivity3.openVideoPlayListActivity2(mContext,program);
                }
            }
        });
    }

    private HashMap<Integer, String> mPositionMap = new HashMap<>();
    class UserViewHolder extends RecyclerView.ViewHolder {

        public UserViewHolder(View itemView) {
            super(itemView);
        }
    }
    class AttentionViewHolder extends RecyclerView.ViewHolder {
        public AttentionViewHolder(View itemView) {
            super(itemView);
        }
    }
    class AlbumViewHolder extends RecyclerView.ViewHolder {
        public AlbumViewHolder(View itemView) {
            super(itemView);
        }
    }
//    @Override
//    public void notifyDataSetChanged() {
//        super.notifyDataSetChanged();
//        mPositionMap.clear();
//        int dType = 0;
//        for (int i = 0; i < getItemCount(); i++) {
//            Object o = getItem(i);
//            if (o instanceof ProgramBean.ProgramEntity) {
//                ProgramBean.ProgramEntity p = (ProgramBean.ProgramEntity) o;
//                if (p.type != dType) {
//                    if (p.type == 1) {
//                        mPositionMap.put(i, "在线直播");
//                    } else if (p.type == 2) {
//                        mPositionMap.put(i, "录播视频");
//                    }
//                    dType = p.type;
//                }
//            }
//
//        }
//    }
}
