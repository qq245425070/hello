package cn.gietv.mlive.modules.compere.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

import cn.gietv.mlive.R;
import cn.gietv.mlive.constants.CommConstants;
import cn.gietv.mlive.db.DBUtils;
import cn.gietv.mlive.http.DefaultLiveHttpCallBack;
import cn.gietv.mlive.modules.compere.activity.CompereActivity;
import cn.gietv.mlive.modules.follow.model.FollowModel;
import cn.gietv.mlive.modules.news.activity.NewsInfoActivity;
import cn.gietv.mlive.modules.usercenter.bean.UserCenterBean;
import cn.gietv.mlive.utils.CacheUtils;
import cn.gietv.mlive.utils.ImageLoaderUtils;
import cn.gietv.mlive.utils.ToastUtils;
import cn.gietv.mlive.utils.ViewHolder;
import cn.kalading.android.retrofit.utils.RetrofitUtils;

/**
 * author：steven
 * datetime：15/10/16 17:59
 */
public class CompereListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ImageLoader mImageLoader;
    public final static String CONCERN = "concern";
    public final static String CONCERN_OTHER = "concern_other";
    public final static String CHAT = "chat";
    private String mConcernStatus;
    private Context mContext;
    private List<UserCenterBean.UserinfoEntity> mData;
    public CompereListAdapter(Context context, List<UserCenterBean.UserinfoEntity> objects,String concern) {
        mImageLoader = ImageLoaderUtils.getDefaultImageLoader(context);
        this.mContext = context;
        mConcernStatus = concern;
        this.mData = objects;
    }

    public void getView(int position, View convertView, final UserCenterBean.UserinfoEntity entity) {
        ImageView image = ViewHolder.get(convertView, R.id.compere_list_iv_image);
        mImageLoader.displayImage(entity.avatar, image);
        ViewHolder.setText(convertView, R.id.compere_list_tv_name, entity.nickname);
        ViewHolder.setText(convertView, R.id.compere_list_tv_attentionCount, entity.desc);
        ImageView concern = ViewHolder.get(convertView,R.id.concern);
        ImageView concern_other = ViewHolder.get(convertView,R.id.compere_list_adapter_iv_jiantou);
        if(CONCERN.equals(mConcernStatus)){
            concern.setVisibility(View.VISIBLE);
            concern_other.setVisibility(View.GONE);
            if(CacheUtils.getCacheUserInfo() != null && CacheUtils.getCacheUserInfo()._id.equals(entity._id)){
                concern.setVisibility(View.GONE);
                concern_other.setVisibility(View.GONE);
            }
            if(entity.isfollow == CommConstants.TRUE) {
                concern.setImageResource(R.drawable.over_concern);
            }else{
                concern.setImageResource(R.drawable.no_concern);
            }
            concern.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FollowModel model = RetrofitUtils.create(FollowModel.class);
                    if(entity.isfollow == CommConstants.FALSE ){
                        model.followByUser(entity._id, CommConstants.FOLLOW_ACTION_ON, new DefaultLiveHttpCallBack<String>() {
                            @Override
                            public void success(String s) {
                                entity.isfollow = CommConstants.TRUE;
                                ToastUtils.showToast(mContext, "关注成功");
                                notifyDataSetChanged();
                                DBUtils.getInstance(mContext).saveAttentionAnchor(entity._id,1);
                            }

                            @Override
                            public void failure(String message) {
                                ToastUtils.showToast(mContext, message);
                            }
                        });
                    }else{
                        model.followByUser(entity._id, CommConstants.FOLLOW_ACTION_OFF, new DefaultLiveHttpCallBack<String>() {
                            @Override
                            public void success(String s) {
                                entity.isfollow = CommConstants.FALSE;
                                ToastUtils.showToast(mContext, "取消关注成功");
                                notifyDataSetChanged();
                                DBUtils.getInstance(mContext).deleteAttentionAnchor(entity._id);
                            }

                            @Override
                            public void failure(String message) {
                                ToastUtils.showToast(mContext, message);
                            }
                        });
                    }
                }
            });
        } else if(CHAT.equals(mConcernStatus)){
            concern.setVisibility(View.VISIBLE);
            concern_other.setVisibility(View.GONE);
            concern.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    NewsInfoActivity.openNewsInfoActivity(mContext,entity._id,entity.nickname,entity.avatar);
                }
            });
        }else if(CONCERN_OTHER.equals(mConcernStatus) ){
            concern.setVisibility(View.GONE);
            concern_other.setVisibility(View.VISIBLE);
        }
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CompereActivity.openCompereActivity(mContext, entity._id);
            }
        });
    }
    public void setConcernStatus(String status){
        this.mConcernStatus = status;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new CompereViewHolder(LayoutInflater.from(mContext).inflate(R.layout.compere_list_adapter,null));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
      getView(position,holder.itemView,mData.get(position));
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    class CompereViewHolder extends RecyclerView.ViewHolder {
        public CompereViewHolder(View itemView) {
            super(itemView);
        }
    }
}
