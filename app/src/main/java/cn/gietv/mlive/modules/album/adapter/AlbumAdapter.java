package cn.gietv.mlive.modules.album.adapter;

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
import cn.gietv.mlive.modules.program.bean.ProgramBean;
import cn.gietv.mlive.modules.video.activity.VideoPlayListActivity2;
import cn.gietv.mlive.modules.video.activity.VideoPlayListActivity3;
import cn.gietv.mlive.utils.ImageLoaderUtils;
import cn.gietv.mlive.utils.NumUtils;

/**
 * Created by houde on 2016/5/18.
 */
public class AlbumAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int TYPE_ITEM =0;  //普通Item View
    private static final int TYPE_FOOTER = 1;  //顶部FootView
    private ImageLoader mImageLoader;
    private LayoutInflater mInflater;
    private List<ProgramBean.ProgramEntity> programEntityList;
    private Context mContext;
    public AlbumAdapter(Context context ,List<ProgramBean.ProgramEntity> programEntities){
        mImageLoader = ImageLoaderUtils.getDefaultImageLoader(context);
        this.programEntityList = programEntities;
        mInflater = LayoutInflater.from(context);
        this.mContext = context;
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, final int i) {
            View view=mInflater.inflate(R.layout.album_item,viewGroup,false);
            ItemViewHolder itemViewHolder=new ItemViewHolder(view);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    VideoPlayListActivity2.openVideoPlayListActivity2(mContext,programEntityList.get(i));
                }
            });
            return itemViewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int i) {
        if (holder instanceof ItemViewHolder) {
            final ProgramBean.ProgramEntity programEntity = programEntityList.get(i);
            ((ItemViewHolder) holder).videoName.setText(programEntity.name);
            ((ItemViewHolder) holder).anchorName.setText(programEntity.userinfo.nickname);
            ((ItemViewHolder) holder).videocount.setText(NumUtils.w(programEntity.onlines));
            mImageLoader.displayImage(programEntity.spic, ((ItemViewHolder) holder).imageView);
            ((ItemViewHolder) holder).imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    VideoPlayListActivity3.openVideoPlayListActivity2(mContext,programEntity);
                }
            });
      } //  else if (holder instanceof FootViewHolder) {
//            FootViewHolder footViewHolder = (FootViewHolder) holder;
//            footViewHolder.foot_view_item_tv.setText("上拉加载更多...");
//        }
    }
    @Override
    public int getItemViewType(int position) {
        if (position + 1 == getItemCount()) {
            return TYPE_FOOTER;
        } else {
            return TYPE_ITEM;
        }
    }
    @Override
    public int getItemCount() {
        return programEntityList.size();
    }

    class ItemViewHolder extends RecyclerView.ViewHolder{
        private TextView videoName;
        private TextView anchorName;
        private TextView videocount;
        private ImageView imageView;
        public ItemViewHolder(View itemView) {
            super(itemView);
            videoName = (TextView) itemView.findViewById(R.id.video_name);
            anchorName = (TextView) itemView.findViewById(R.id.anchor_name);
            videocount = (TextView) itemView.findViewById(R.id.video_count);
            imageView = (ImageView) itemView.findViewById(R.id.game_iv_image);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }
    }
}
