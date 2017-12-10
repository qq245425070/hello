package cn.gietv.mlive.views;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import cn.gietv.mlive.R;
import cn.gietv.mlive.modules.game.bean.GameInfoBean;
import cn.gietv.mlive.utils.ImageLoaderUtils;

/**
 * Created by houde on 2016/9/28.
 */
public class FollowTitlePopuWindow extends PopupWindow {
    private  Context mContext;
    private List<GameInfoBean.GameInfoEntity> mGames ;
    private View mRootView;
    private XRecyclerView mListView;
    private GameListAdapter mAdapter;
    public  FollowTitlePopuWindow(Context context, List<GameInfoBean.GameInfoEntity> gameInfoEntities){
        this.mContext = context;
        mGames = new ArrayList<>();
        mGames.addAll(gameInfoEntities);
        mGames.remove(0);
        mRootView =  LayoutInflater.from(context).inflate(R.layout.fragment_category2 ,null);
        mListView = (XRecyclerView) mRootView.findViewById(R.id.recyclerview);
        GridLayoutManager layoutManager1 = new GridLayoutManager(mContext,3);
        mListView.setLayoutManager(layoutManager1);
        mListView.setRefreshProgressStyle(ProgressStyle.SysProgress);
        mListView.setLaodingMoreProgressStyle(ProgressStyle.SysProgress);
        mListView.setArrowImageView(R.drawable.iconfont_downgrey);
        mListView.setPullRefreshEnabled(false);
        mListView.setLoadingMoreEnabled(false);
        mAdapter = new GameListAdapter(mContext,mGames);
        mListView.setAdapter(mAdapter);
        setContentView(mRootView);
    }
    public class GameListAdapter extends RecyclerView.Adapter<GameListAdapter.ViewHolder> {
        private ImageLoader mImageloader;
        private List<GameInfoBean.GameInfoEntity> mData;
        private Context mContext;

        public GameListAdapter(Context context, List<GameInfoBean.GameInfoEntity> objects) {
            mImageloader = ImageLoaderUtils.getDefaultImageLoader(context);
            mData = objects;
            this.mContext = context;
        }

        public void getView(final int position, ViewHolder viewHolder) {
            final GameInfoBean.GameInfoEntity bean = getItem(position);
            if (bean == null)
                return;
            mImageloader.displayImage(bean.img , viewHolder.image);
            viewHolder.name.setText(bean.name);
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    CategoryActivity2.openActivity(mContext, bean);
                    if(itemClickListener != null){
                        itemClickListener.onItemClickListener(position);
                        dismiss();
                    }
                }
            });
        }

        private GameInfoBean.GameInfoEntity getItem(int position) {
            return mData.get(position);
        }

        @Override
        public GameListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.game_album_adapter, null));
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            getView(position, holder);
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
    private ItemClickListener itemClickListener;
    public void setOnItemClickListener(ItemClickListener listener){
        this.itemClickListener = listener;
    }
    public interface ItemClickListener{
        void onItemClickListener(int position);
    }
}
