package cn.gietv.mlive.modules.program.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

import cn.gietv.mlive.R;
import cn.gietv.mlive.base.AbsArrayAdapter;
import cn.gietv.mlive.modules.game.bean.GameInfoBean;
import cn.gietv.mlive.utils.ImageLoaderUtils;

/**
 * Created by houde on 2016/5/4.
 */
public class RankingListAdapter extends AbsArrayAdapter<GameInfoBean.GameInfoEntity> {
    private Context mContext;
    private ImageLoader mImageLoader;
    public RankingListAdapter(Context context, List<GameInfoBean.GameInfoEntity> objects) {
        super(context, objects);
        mImageLoader = ImageLoaderUtils.getDefaultImageLoader(context);
        mContext = context;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final GameInfoBean.GameInfoEntity entity = getItem(position);
        ViewHolder viewHolder;
        if(convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.ranking_list_item, null);
            viewHolder = new ViewHolder();
            viewHolder.index = (TextView)convertView.findViewById(R.id.index);
            viewHolder.gameDesc = (TextView) convertView.findViewById(R.id.game_desc);
            viewHolder.gameLogo = (ImageView) convertView.findViewById(R.id.game_logo);
            viewHolder.gameName = (TextView) convertView.findViewById(R.id.game_name);
            viewHolder.ratingBar = (RatingBar) convertView.findViewById(R.id.rating_bar);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
        mImageLoader.displayImage(entity.spic,viewHolder.gameLogo);
        viewHolder.ratingBar.setNumStars(4);
        viewHolder.gameName.setText(entity.name);
        viewHolder.gameDesc.setText(entity.desc);
        viewHolder.index.setText(String.valueOf(position + 1));
        viewHolder.gameLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                GameInfoActivity.openGameInfoActivity(getContext(), entity._id, entity.name, entity.type);
            }
        });
        return convertView;
    }
    class ViewHolder{
        TextView index;
        ImageView gameLogo;
        TextView gameName;
        RatingBar ratingBar;
        TextView gameDesc;
    }
}
