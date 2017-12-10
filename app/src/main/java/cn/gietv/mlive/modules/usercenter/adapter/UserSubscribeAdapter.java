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
import cn.gietv.mlive.modules.album.activity.AlbumActivity;
import cn.gietv.mlive.modules.author.activity.AuthorActivity;
import cn.gietv.mlive.modules.category.activity.CategoryActivity;
import cn.gietv.mlive.modules.category.activity.CategoryActivity2;
import cn.gietv.mlive.modules.follow.model.FollowModel;
import cn.gietv.mlive.modules.game.bean.GameInfoBean;
import cn.gietv.mlive.modules.login.activity.LoginActivity;
import cn.gietv.mlive.modules.usercenter.bean.UserCenterBean;
import cn.gietv.mlive.utils.ImageLoaderUtils;
import cn.gietv.mlive.utils.IntentUtils;
import cn.gietv.mlive.utils.ToastUtils;
import cn.gietv.mlive.utils.UserUtils;
import cn.gietv.mlive.utils.ViewHolder;
import cn.kalading.android.retrofit.utils.RetrofitUtils;

/**
 * Created by houde on 2016/9/26.
 */
public class UserSubscribeAdapter extends RecyclerView.Adapter<UserSubscribeAdapter.ParentHolder> {
    private List<Object> mData;
    private Context mContext;
    private ImageLoader imageLoader;
    private FollowModel followModel;
    public UserSubscribeAdapter(Context context, List<Object> data){
        this.mContext = context;
        mData = data;
        imageLoader = ImageLoaderUtils.getDefaultImageLoader(context);
        followModel = RetrofitUtils.create(FollowModel.class);
    }

    @Override
    public ParentHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType){
            case 0:
                return new GameHolder(LayoutInflater.from(mContext).inflate(R.layout.compere_list_adapter,null));
            case 1:
                return new AnchorHolder(LayoutInflater.from(mContext).inflate(R.layout.compere_list_adapter,null));
            case 2:
                return new TagHolder(LayoutInflater.from(mContext).inflate(R.layout.compere_list_adapter,null));
        }
        return null;
    }

    @Override
    public void onBindViewHolder(final ParentHolder holder, int position) {
        if(holder instanceof GameHolder){
            final GameInfoBean.GameInfoEntity game = (GameInfoBean.GameInfoEntity) mData.get(position);
            if(game == null)
                return;
            game.isfollow = 1;
            holder.name.setText(game.name);
            if(game.type == CommConstants.CAROUSEL_TYPE_AREA) {
                imageLoader.displayImage(game.img, holder.image);
            }else{
                imageLoader.displayImage(game.img, holder.image);
            }
            ((GameHolder) holder).itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(game.type == CommConstants.CAROUSEL_TYPE_AREA) {
                        CategoryActivity2.openActivity(mContext,game);
                    }else{
                        AlbumActivity.openAlbumActivity(game.name,game._id,mContext);
                    }
                }
            });
            changeBackgrount(game.isfollow,holder);
            holder.subscribeImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(UserUtils.isNotLogin()){
                        IntentUtils.openActivity(mContext,LoginActivity.class);
                        return;
                    }
                    if(game.isfollow == CommConstants.FOLLOW_TRUE){
                        followModel.follow(game._id, CommConstants.FOLLOW_FALSE, game.type, new DefaultLiveHttpCallBack<String>() {
                            @Override
                            public void success(String s) {
                                mData.remove(game);
                                notifyDataSetChanged();
                                ToastUtils.showToastShort(mContext,"取消订阅成功");
                                game.isfollow = CommConstants.FOLLOW_FALSE;
                                changeBackgrount(game.isfollow,holder);
                            }

                            @Override
                            public void failure(String message) {
                                ToastUtils.showToastShort(mContext,message);
                            }
                        });
                    }else{
                        followModel.follow(game._id, CommConstants.FOLLOW_TRUE, game.type, new DefaultLiveHttpCallBack<String>() {
                            @Override
                            public void success(String s) {
                                ToastUtils.showToastShort(mContext,"订阅成功");
                                game.isfollow = CommConstants.FOLLOW_TRUE;
                                changeBackgrount(game.isfollow,holder);
                            }

                            @Override
                            public void failure(String message) {
                                ToastUtils.showToastShort(mContext,message);
                            }
                        });
                    }
                }
            });
        }else if(holder instanceof AnchorHolder){
            final UserCenterBean.UserinfoEntity user = (UserCenterBean.UserinfoEntity) mData.get(position);
            if(user == null)
                return;
            user.isfollow = 1;
            holder.name.setText(user.nickname);
            imageLoader.displayImage(user.avatar,holder.image);
            ((AnchorHolder) holder).itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AuthorActivity.openAnchorActivity(user.nickname,user._id,mContext,user.isfollow);
                }
            });
            changeBackgrount(user.isfollow,holder);
            holder.subscribeImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(UserUtils.isNotLogin()){
                        IntentUtils.openActivity(mContext,LoginActivity.class);
                        return;
                    }
                    if(user.isfollow == CommConstants.FOLLOW_TRUE){
                        followModel.follow(user._id, CommConstants.FOLLOW_FALSE, user.type, new DefaultLiveHttpCallBack<String>() {
                            @Override
                            public void success(String s) {
                                mData.remove(user);
                                notifyDataSetChanged();
                                ToastUtils.showToastShort(mContext,"取消订阅成功");
                                user.isfollow = CommConstants.FOLLOW_FALSE;
                                changeBackgrount(user.isfollow,holder);
                            }

                            @Override
                            public void failure(String message) {
                                ToastUtils.showToastShort(mContext,message);
                            }
                        });
                    }else{
                        followModel.follow(user._id, CommConstants.FOLLOW_TRUE, user.type, new DefaultLiveHttpCallBack<String>() {
                            @Override
                            public void success(String s) {
                                ToastUtils.showToastShort(mContext,"订阅成功");
                                user.isfollow = CommConstants.FOLLOW_TRUE;
                                changeBackgrount(user.isfollow,holder);
                            }

                            @Override
                            public void failure(String message) {
                                ToastUtils.showToastShort(mContext,message);
                            }
                        });
                    }
                }
            });
        }else if(holder instanceof TagHolder){
            final int[] isfollow = {1};
            final String tag = (String) mData.get(position);
            if(tag == null)
                return;
            holder.name.setText(tag);
            ((TagHolder) holder).itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CategoryActivity.openActivity(mContext,tag,tag,2);
                }
            });
            holder.subscribeImage.setImageResource(R.mipmap.subscribe_over);
            holder.subscribeImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(UserUtils.isNotLogin()){
                        IntentUtils.openActivity(mContext,LoginActivity.class);
                        return;
                    }
                    if(isfollow[0] == CommConstants.FOLLOW_TRUE){
                        followModel.follow(tag, CommConstants.FOLLOW_FALSE, CommConstants.TYPE_TAG, new DefaultLiveHttpCallBack<String>() {
                            @Override
                            public void success(String s) {
                                mData.remove(tag);
                                notifyDataSetChanged();
                                ToastUtils.showToastShort(mContext,"取消订阅成功");
                                isfollow[0] = CommConstants.FOLLOW_FALSE;
                                changeBackgrount(isfollow[0],holder);
                            }

                            @Override
                            public void failure(String message) {
                                ToastUtils.showToastShort(mContext,message);
                            }
                        });
                    }else{
                        followModel.follow(tag, CommConstants.FOLLOW_TRUE, CommConstants.TYPE_TAG, new DefaultLiveHttpCallBack<String>() {
                            @Override
                            public void success(String s) {
                                ToastUtils.showToastShort(mContext,"订阅成功");
                                isfollow[0] = CommConstants.FOLLOW_TRUE;
                                changeBackgrount(isfollow[0],holder);
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
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }
    private void changeBackgrount(int isFollow,ParentHolder holder){
        if(isFollow == CommConstants.FOLLOW_TRUE){
            holder.subscribeImage.setImageResource(R.mipmap.subscribe_over);
        }else{
            holder.subscribeImage.setImageResource(R.mipmap.subscribe_no);
        }
    }

    @Override
    public int getItemViewType(int position) {
        Object o = mData.get(position);
        if(o instanceof GameInfoBean.GameInfoEntity){
            return 0;
        }else if ( o instanceof UserCenterBean.UserinfoEntity){
            return 1;
        }else if(o instanceof String){
            return 2;
        }
        return super.getItemViewType(position);
    }
    public class ParentHolder extends RecyclerView.ViewHolder{
        ImageView image,subscribeImage;
        TextView name;
        TextView desc;
        public ParentHolder(View itemView) {
            super(itemView);
            image = ViewHolder.get(itemView, R.id.compere_list_iv_image);
            name = ViewHolder.get(itemView, R.id.compere_list_tv_name);
            desc = ViewHolder.get(itemView, R.id.compere_list_tv_attentionCount);
            subscribeImage = ViewHolder.get(itemView,R.id.subscribe_image);
            ViewHolder.get(itemView,R.id.compere_list_adapter_iv_guanzhu).setVisibility(View.INVISIBLE);
            ViewHolder.get(itemView,R.id.concern).setVisibility(View.INVISIBLE);
            ViewHolder.get(itemView,R.id.compere_list_adapter_iv_jiantou).setVisibility(View.INVISIBLE);

        }
    }
    public class GameHolder extends ParentHolder{
        public GameHolder(View itemView) {
            super(itemView);
        }
    }
    public class AnchorHolder extends ParentHolder{
        public AnchorHolder(View itemView) {
            super(itemView);
        }
    }
    public class TagHolder extends ParentHolder{
        public TagHolder(View itemView) {
            super(itemView);
            image.setVisibility(View.GONE);
        }
    }
}
