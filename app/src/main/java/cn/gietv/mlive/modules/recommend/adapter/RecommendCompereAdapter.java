package cn.gietv.mlive.modules.recommend.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

import cn.gietv.mlive.R;
import cn.gietv.mlive.modules.common.OnItemClick;
import cn.gietv.mlive.modules.compere.activity.CompereActivity;
import cn.gietv.mlive.modules.recommend.bean.RecommendBean;
import cn.gietv.mlive.modules.usercenter.bean.UserCenterBean;
import cn.gietv.mlive.utils.ImageLoaderUtils;

/**
 * Created by houde on 2016/4/23.
 */
public class RecommendCompereAdapter extends RecyclerView.Adapter<RecommendCompereAdapter.ViewHolder> {

    private ImageLoader mImageLoader;
    private Context mContext;
    private List<RecommendBean.RecommendAnchorEntity> list;
    private OnItemClick<UserCenterBean.UserinfoEntity> mOnItemClick;

    public void setOnItemClick(OnItemClick<UserCenterBean.UserinfoEntity> onItemClick) {
        mOnItemClick = onItemClick;
    }

    public RecommendCompereAdapter(Context context,List<RecommendBean.RecommendAnchorEntity> objects){
        this.mContext = context;
        this.list = objects;
        mImageLoader = ImageLoaderUtils.getDefaultImageLoader(context);
        WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(outMetrics);
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.recommend_hlistview_item, viewGroup,false));
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        final RecommendBean.RecommendAnchorEntity anchor = list.get(i);
        mImageLoader.displayImage(anchor.userinfo.avatar, viewHolder.gameImage);
        viewHolder.nickName.setText(anchor.userinfo.nickname);
        viewHolder.gameName.setText(anchor.gamename);
        viewHolder.gameImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClick != null) {
                    mOnItemClick.onClick(anchor.userinfo);
                } else {
                    CompereActivity.openCompereActivity(mContext, anchor.userinfo._id);
//                    FastEntryAuthorActivity.openAnchorActivity(mContext,anchor.userinfo._id,anchor.userinfo.nickname);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView gameImage;
        TextView gameName;
        TextView nickName;
        public ViewHolder(View itemView) {
            super(itemView);
            this.gameImage = (ImageView) itemView.findViewById(R.id.recommend_iv_compere);
            this.gameName = (TextView) itemView.findViewById(R.id.recommend_tv_compere_game);
            this.nickName = (TextView) itemView.findViewById(R.id.recommend_tv_compere_name);
        }
    }
}

