package cn.gietv.mlive.modules.usercenter.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
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
import cn.gietv.mlive.modules.follow.model.FollowModel;
import cn.gietv.mlive.modules.usercenter.bean.UserCenterBean;
import cn.gietv.mlive.utils.CacheUtils;
import cn.gietv.mlive.utils.ImageLoaderUtils;
import cn.gietv.mlive.utils.ToastUtils;
import cn.kalading.android.retrofit.utils.RetrofitUtils;

/**
 * Created by houde on 2016/10/14.
 */
public class MyFriendsAddapter extends RecyclerView.Adapter<MyFriendsAddapter.FriendsHolder>{
    private Context mContext;
    private List<UserCenterBean.UserinfoEntity> userList;
    private ImageLoader mImageLoader;
    private int isMine;//区分自己的关注还是别人的关注
    private FollowModel follow;
    public MyFriendsAddapter(Context countext, List<UserCenterBean.UserinfoEntity> users,int isMine){
        this.mContext = countext;
        this.userList = users;
        this.mImageLoader = ImageLoaderUtils.getDefaultImageLoader(mContext);
        this.isMine = isMine;
        follow = RetrofitUtils.create(FollowModel.class);
    }
    @Override
    public FriendsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new FriendsHolder(LayoutInflater.from(mContext).inflate(R.layout.item_myfriend,null));
    }

    @Override
    public void onBindViewHolder(final FriendsHolder holder, final int position) {
        final UserCenterBean.UserinfoEntity user = userList.get(position);
        if(user == null)
            return;
        holder.followCount.setText(user.follows + "个粉丝");
        holder.nickname.setText(user.nickname);
        holder.desc.setText(user.desc);
        mImageLoader.displayImage(user.avatar,holder.avatar);
        if(user.isfollow == CommConstants.FOLLOW_TRUE){
           holder.followImage.setImageResource(R.mipmap.attention_over);
        }else{
            holder.followImage.setImageResource(R.mipmap.attention_no);
        }
        UserCenterBean.UserinfoEntity userinfoEntity = CacheUtils.getCacheUserInfo();
        if(userinfoEntity != null){
            if(userinfoEntity._id.equals(user._id)){
                holder.followImage.setVisibility(View.INVISIBLE);
            }
        }
        holder.followImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(user.isfollow == CommConstants.FOLLOW_TRUE){
                    follow.followByUser(user._id, CommConstants.FOLLOW_FALSE, new DefaultLiveHttpCallBack<String>() {
                        @Override
                        public void success(String s) {
                            user.isfollow = CommConstants.FOLLOW_ACTION_OFF;
                            ToastUtils.showToastShort(mContext,"取消关注成功");
                            holder.followImage.setImageResource(R.mipmap.attention_no);
                        }

                        @Override
                        public void failure(String message) {
                            ToastUtils.showToastShort(mContext,message);
                        }
                    });
                }else{
                    follow.followByUser(user._id, CommConstants.FOLLOW_TRUE, new DefaultLiveHttpCallBack<String>() {
                        @Override
                        public void success(String s) {
                            user.isfollow = CommConstants.FOLLOW_ACTION_ON;
                            ToastUtils.showToastShort(mContext,"关注成功");
                            holder.followImage.setImageResource(R.mipmap.attention_over);
                        }

                        @Override
                        public void failure(String message) {
                            ToastUtils.showToastShort(mContext,message);
                        }
                    });
                }
            }
        });
        if(isMine == 1){
            holder.followImage.setVisibility(View.VISIBLE);
        }else{
            holder.followImage.setVisibility(View.INVISIBLE);
        }
        holder.avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CompereActivity.openCompereActivity(mContext,user._id);
            }
        });
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }
    public class FriendsHolder extends RecyclerView.ViewHolder{
        ImageView avatar,followImage;
        TextView nickname,followCount,desc;
        public FriendsHolder(View itemView) {
            super(itemView);
            avatar = (ImageView) itemView.findViewById(R.id.user_image);
            nickname = (TextView) itemView.findViewById(R.id.nickname);
            followCount = (TextView) itemView.findViewById(R.id.follow_count);
            desc = (TextView) itemView.findViewById(R.id.desc);
            followImage = (ImageView) itemView.findViewById(R.id.follow_image);
        }
    }
}
