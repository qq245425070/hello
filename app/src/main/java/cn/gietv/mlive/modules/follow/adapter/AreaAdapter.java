package cn.gietv.mlive.modules.follow.adapter;

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
import cn.gietv.mlive.modules.game.bean.GameInfoBean;
import cn.gietv.mlive.utils.ImageLoaderUtils;

/**
 * Created by houde on 2016/9/1.
 */
public class AreaAdapter extends RecyclerView.Adapter<AreaAdapter.AreaViewHolder>{

    private Context mContext;
    private List<GameInfoBean.GameInfoEntity> mData;
    private ImageLoader mImageLoader;
    public AreaAdapter(Context context, List<GameInfoBean.GameInfoEntity> data){
        this.mContext = context;
        this.mData = data;
        mImageLoader = ImageLoaderUtils.getDefaultImageLoader(context);
    }

    @Override
    public AreaViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new AreaViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_area_follow,null));
    }

    @Override
    public void onBindViewHolder(AreaViewHolder holder, int position) {
        GameInfoBean.GameInfoEntity bean = mData.get(position);
        if(bean == null)
            return;
        mImageLoader.displayImage(bean.spic,holder.imageView);
        holder.areaName.setText(bean.name);
        holder.videoNum.setText("今日更新" + bean.onlines + "部视频");
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }
    public class AreaViewHolder extends RecyclerView.ViewHolder{
        ImageView imageView ;
        TextView areaName,videoNum,followText;
        public AreaViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.compere_list_iv_image);
            areaName = (TextView) itemView.findViewById(R.id.compere_list_tv_name);
            videoNum = (TextView) itemView.findViewById(R.id.compere_list_tv_attentionCount);
            followText = (TextView) itemView.findViewById(R.id.compere_list_adapter_iv_guanzhu);
        }
    }
}