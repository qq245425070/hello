package cn.gietv.mlive.modules.author.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.siyamed.shapeimageview.RoundedImageView;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

import cn.gietv.mlive.R;
import cn.gietv.mlive.base.AbsArrayAdapter;
import cn.gietv.mlive.modules.album.activity.AlbumActivity;
import cn.gietv.mlive.modules.game.bean.GameInfoBean;
import cn.gietv.mlive.utils.ImageLoaderUtils;
import cn.gietv.mlive.utils.NumUtils;
import cn.gietv.mlive.utils.ViewHolder;

/**
 * Created by houde on 2016/5/29.
 */
public class AlbumAdapter extends AbsArrayAdapter<GameInfoBean.GameInfoEntity> {
    private ImageLoader mImageLoader;
    public AlbumAdapter(Context context, List<GameInfoBean.GameInfoEntity> objects) {
        super(context, objects);
        this.mImageLoader = ImageLoaderUtils.getDefaultImageLoader(getContext());
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.category_album_item, null);
        }
        handleAlbum(getItem(position),convertView);
        return convertView;
    }
    private void handleAlbum(final GameInfoBean.GameInfoEntity bean, View convertView) {
        if (bean == null) {
            return;
        }
        TextView videocount = ViewHolder.get(convertView,R.id.video_count);
        RoundedImageView image = ViewHolder.get(convertView, R.id.game_info_iv_image);
        TextView name = ViewHolder.get(convertView, R.id.album_name);
        TextView anchor = ViewHolder.get(convertView, R.id.album_anchor);
        TextView count = ViewHolder.get(convertView,R.id.play_count);
        View line = ViewHolder.get(convertView,R.id.line3);
        line.setVisibility(View.VISIBLE);
        if(bean.author == null){
            return;
        }
        anchor.setText(bean.author.nickname);
        count.setText(NumUtils.w(bean.onlines));
        videocount.setText(bean.programnums + "个视频");
        mImageLoader.displayImage(bean.spic, image);
        name.setText(bean.name);
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlbumActivity.openAlbumActivity(bean.name, bean._id, getContext());
            }
        });
    }

}
