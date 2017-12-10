package cn.gietv.mlive.modules.subscribe.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import cn.gietv.mlive.R;
import cn.gietv.mlive.modules.game.bean.GameInfoBean;
import cn.gietv.mlive.utils.ImageLoaderUtils;

/**
 * Created by houde on 2016/9/14.
 */
public class SubscribeAdapter extends RecyclerView.Adapter<SubscribeAdapter.SubscribeViewHolder> {
    private Context mContext;
    private List<GameInfoBean.GameInfoEntity> mGameList;
    private ImageLoader imageLoader;
    private List<SubscribeViewHolder> mHolders;
    private boolean mCheckAll = true;
    private List<GameInfoBean.GameInfoEntity> mCheckGameList;
    private boolean isFirst = true;
    public SubscribeAdapter(Context context, List<GameInfoBean.GameInfoEntity> games){
        this.mContext = context;
        this.mGameList = games;
        imageLoader = ImageLoaderUtils.getDefaultImageLoader(mContext);
        mHolders = new ArrayList<>();
        mCheckGameList = new ArrayList<>();
    }
    @Override
    public SubscribeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        SubscribeViewHolder holder = new SubscribeViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_subscribe,null));
        holder.setIsRecyclable(false);
        return holder;
    }

    @Override
    public void onBindViewHolder(final SubscribeViewHolder holder, int position) {
        final GameInfoBean.GameInfoEntity game = mGameList.get(position);
        if(game == null)
            return;
        holder.checkBox.setText(game.name);
        imageLoader.displayImage(game.img,holder.spic);
        holder.checkBox.setChecked(mCheckAll);
        if(isFirst){
            mCheckGameList.add(game);
        }
        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    mCheckGameList.add(game);
                    Log.e("ceshi", "list.size() =  " + mCheckGameList.size());
                }else{
                    mCheckGameList.remove(game);
                }
            }
        });
    }
    public List<GameInfoBean.GameInfoEntity> getCheckGameList(){
        return mCheckGameList;
    }

    @Override
    public int getItemCount() {
        return mGameList.size();
    }
    public class SubscribeViewHolder extends RecyclerView.ViewHolder{
        ImageView spic;
        CheckBox checkBox;
        public SubscribeViewHolder(View itemView) {
            super(itemView);
            spic = (ImageView) itemView.findViewById(R.id.game_iv_image);
            checkBox = (CheckBox) itemView.findViewById(R.id.checkbox);
        }

    }
    public void setCheckAll(boolean isCheck){
        mCheckAll = isCheck;
        notifyDataSetChanged();
        isFirst = false;
        if(isCheck) {
            for(int i = 0; i < mGameList.size() ; i++){
                if(mCheckGameList == null ){
                    break;
                }
                mCheckGameList.add(mGameList.get(i));
            }
        }else{
            if(mCheckGameList != null && mCheckGameList.size() > 0){
                mCheckGameList.clear();
            }
        }
    }
}
