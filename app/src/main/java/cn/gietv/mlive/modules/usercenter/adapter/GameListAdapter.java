package cn.gietv.mlive.modules.usercenter.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import cn.gietv.mlive.R;
import cn.gietv.mlive.constants.CommConstants;
import cn.gietv.mlive.modules.album.activity.AlbumActivity;
import cn.gietv.mlive.modules.category.activity.CategoryActivity;
import cn.gietv.mlive.modules.category.activity.CategoryActivity2;
import cn.gietv.mlive.modules.game.bean.GameInfoBean;
import cn.gietv.mlive.utils.ImageLoaderUtils;

/**
 * author：steven
 * datetime：15/10/4 10:41
 */
public class GameListAdapter extends RecyclerView.Adapter<GameListAdapter.ViewHolder> {
    private ImageLoaderUtils mImageloader;
    private List<GameInfoBean.GameInfoEntity> mData;
    private Context mContext;
    public GameListAdapter(Context context, List<GameInfoBean.GameInfoEntity> objects) {
        mImageloader = ImageLoaderUtils.getInstance();
        mData = objects;
        this.mContext = context;
    }

    public void getView(int position, ViewHolder viewHolder) {
        final GameInfoBean.GameInfoEntity bean = getItem(position);
        if(bean == null)
            return;
        mImageloader.downLoadImage(mContext,viewHolder.image,bean.spic,false);
        viewHolder.name.setText(bean.name);
        viewHolder.desc.setText(bean.programnums + " 个视频");
        if(bean.type == CommConstants.CAROUSEL_TYPE_AREA){
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CategoryActivity2.openActivity(mContext,bean);
                }
            });
        }else if(bean.type == CommConstants.CAROUSEL_TYPE_ALBUM) {
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlbumActivity.openAlbumActivity(bean.name, bean._id, mContext);
                }
            });
        }else{
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    CategoryActivity.openActivity(mContext, bean._id, bean.name, bean.type);
                }
            });
        }
    }

    private GameInfoBean.GameInfoEntity getItem(int position) {
        return mData.get(position);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.game_list_adapter, null));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        getView(position,holder);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView name;
        TextView desc;
        public ViewHolder(View itemView) {
            super(itemView);
            image = cn.gietv.mlive.utils.ViewHolder.get(itemView, R.id.game_iv_image);
            name = cn.gietv.mlive.utils.ViewHolder.get(itemView, R.id.game_tv_name);
            desc = cn.gietv.mlive.utils.ViewHolder.get(itemView, R.id.game_tv_desc);
        }
    }
}
