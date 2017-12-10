package cn.gietv.mlive.modules.author.adapter;

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
import cn.gietv.mlive.modules.author.activity.AuthorActivity;
import cn.gietv.mlive.modules.follow.model.FollowModel;
import cn.gietv.mlive.modules.login.activity.LoginActivity;
import cn.gietv.mlive.modules.usercenter.bean.UserCenterBean;
import cn.gietv.mlive.utils.ImageLoaderUtils;
import cn.gietv.mlive.utils.IntentUtils;
import cn.gietv.mlive.utils.ToastUtils;
import cn.gietv.mlive.utils.UserUtils;
import cn.gietv.mlive.utils.ViewHolder;
import cn.kalading.android.retrofit.utils.RetrofitUtils;

/**
 * 快速入口作者页的Adapter
 * 作者：houde on 2016/11/22 15:36
 * 邮箱：yangzhonghao@gietv.com
 */
public class AuthorAdapter extends RecyclerView.Adapter<AuthorAdapter.AuthorHolder> {
    private Context mContext;
    private ImageLoader mImageLoader;
    private List<UserCenterBean.UserinfoEntity> mUsers;
    private FollowModel model;
    public AuthorAdapter(Context context, List<UserCenterBean.UserinfoEntity> users){
        this.mContext = context;
        mUsers = users;
        mImageLoader = ImageLoaderUtils.getDefaultImageLoader(context);
        model = RetrofitUtils.create(FollowModel.class);
    }
    @Override
    public AuthorHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new AuthorHolder(LayoutInflater.from(mContext).inflate(R.layout.game_info_item_anchor,null));
    }

    @Override
    public void onBindViewHolder(final AuthorHolder holder, int position) {
        final UserCenterBean.UserinfoEntity bean = mUsers.get(position);
        if (bean == null)
            return;
        holder.authorName.setText(bean.nickname);
        holder.authorFollow.setText(bean.follows+"");
        holder.authorDesc.setText(bean.desc);
        if(bean.avatar != null && bean.avatar.equals(holder.authorImage.getTag())){

        }else {
            mImageLoader.displayImage(bean.avatar, holder.authorImage);
            holder.authorImage.setTag(bean.avatar);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AuthorActivity.openAnchorActivity(bean.nickname, bean._id, mContext,bean.isfollow);
            }
        });
        if(bean.isfollow == CommConstants.TRUE){
            holder.followImage.setImageResource(R.mipmap.subscribe_over);
        }else {
            holder.followImage.setImageResource(R.mipmap.subscribe_no);
        }
        holder.followImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (UserUtils.isNotLogin()) {
                    IntentUtils.openActivity(mContext, LoginActivity.class);
                    return;
                }
                holder.followImage.setClickable(false);
                if(bean.isfollow == CommConstants.TRUE){
                    model.follow(bean._id, CommConstants.FOLLOW_ACTION_OFF, bean.type, new DefaultLiveHttpCallBack<String>() {
                        @Override
                        public void success(String s) {
                            holder.authorFollow.setText(--bean.follows+"个粉丝");
                            ToastUtils.showToast(mContext, "取消订阅成功");
                            holder.followImage.setImageResource(R.mipmap.subscribe_no);
                            bean.isfollow = CommConstants.FALSE;
                            holder.followImage.setClickable(true);
                        }

                        @Override
                        public void failure(String message) {

                            ToastUtils.showToast(mContext, message);
                        }
                    });
                }else {
                    holder.followImage.setClickable(false);
                    model.follow(bean._id, CommConstants.FOLLOW_ACTION_ON, bean.type, new DefaultLiveHttpCallBack<String>() {
                        @Override
                        public void success(String s) {
                            holder.authorFollow.setText(++bean.follows+"个粉丝");
                            ToastUtils.showToast(mContext, "订阅成功");
                            holder.followImage.setImageResource(R.mipmap.subscribe_over);
                            bean.isfollow = CommConstants.TRUE;
                            holder.followImage.setClickable(true);
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
    public int getItemCount() {
        return mUsers.size();
    }

    class AuthorHolder extends RecyclerView.ViewHolder {
        ImageView authorImage;
        TextView authorName;
        TextView authorDesc;
        TextView authorFollow;
        ImageView followImage;
        public AuthorHolder(View convertView) {
            super(convertView);
            authorImage = ViewHolder.get(convertView, R.id.anchor_image);
            authorName = ViewHolder.get(convertView,R.id.anchor_name);
            authorDesc = ViewHolder.get(convertView,R.id.anchor_desc);
            authorFollow = ViewHolder.get(convertView,R.id.anchor_follow);
            followImage = ViewHolder.get(convertView,R.id.follow_image);
            followImage.setVisibility(View.INVISIBLE);
        }
    }
}
