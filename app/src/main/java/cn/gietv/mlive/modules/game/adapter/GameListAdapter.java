package cn.gietv.mlive.modules.game.adapter;

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
import cn.gietv.mlive.modules.category.activity.CategoryActivity2;
import cn.gietv.mlive.modules.game.bean.GameInfoBean;
import cn.gietv.mlive.utils.ImageLoaderUtils;

/**
 * author：steven
 * datetime：15/10/4 10:41
 */
public class GameListAdapter extends RecyclerView.Adapter<GameListAdapter.ViewHolder> {
    private ImageLoader mImageloader;
    private List<GameInfoBean.GameInfoEntity> mData;
    private Context mContext;
    public GameListAdapter(Context context, List<GameInfoBean.GameInfoEntity> objects) {
        mImageloader = ImageLoaderUtils.getDefaultImageLoader(context);
        mData = objects;
        this.mContext = context;
    }

    public void getView(int position, ViewHolder viewHolder) {
        final GameInfoBean.GameInfoEntity bean = getItem(position);
        if(bean == null)
            return;
        mImageloader.displayImage( bean.img, viewHolder.image);
        viewHolder.name.setText(bean.name);
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                CategoryActivity2.openActivity(mContext, bean._id, bean.name, bean.type,bean.spic);
                CategoryActivity2.openActivity(mContext,bean);
            }
        });
    }

    private GameInfoBean.GameInfoEntity getItem(int position) {
        return mData.get(position);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.game_album_adapter, null));
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
        public ViewHolder(View convertView) {
            super(convertView);
            image = cn.gietv.mlive.utils.ViewHolder.get(convertView, R.id.game_iv_image);
            name = cn.gietv.mlive.utils.ViewHolder.get(convertView, R.id.game_tv_name);
        }
    }
}
