package cn.gietv.mlive.modules.album.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.siyamed.shapeimageview.RoundedImageView;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import cn.gietv.mlive.R;
import cn.gietv.mlive.base.AbsArrayAdapter;
import cn.gietv.mlive.modules.album.activity.AlbumActivity;
import cn.gietv.mlive.modules.category.activity.CategoryActivity;
import cn.gietv.mlive.modules.game.bean.GameInfoBean;
import cn.gietv.mlive.utils.DensityUtil;
import cn.gietv.mlive.utils.ImageLoaderUtils;
import cn.gietv.mlive.utils.NumUtils;
import cn.gietv.mlive.utils.ViewHolder;
import cn.gietv.mlive.views.FlowLayout;

/**
 * Created by houde on 2016/5/29.
 */
public class DescriptionAdapter extends AbsArrayAdapter<Object> {
    private final int TYPE_INFO = 0;
    private final int TYPE_TAG = 1;
    private final int TYPE_ALBUM = 2;
    private ImageLoader mImageLoader;
    private TextView temText;
    private int type;
    private int flag = -1;
    private Context mContext;
    public DescriptionAdapter(Context context, List<Object> objects) {
        super(context, objects);
        mContext = context;
        mImageLoader = ImageLoaderUtils.getDefaultImageLoader(context);
    }

    @Override
    public int getItemViewType(int position) {
        Object o = getItem(position);
        if(position == 0){
            return TYPE_INFO;
        }else if(o instanceof GameInfoBean.GameInfoEntity){
            return TYPE_ALBUM;
        }else {
            return TYPE_TAG;
        }
    }

    @Override
    public int getViewTypeCount() {
        return 3;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Object o = getItem(position);
        switch (getItemViewType(position)){
            case TYPE_INFO:
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.des_info_item,null);
                break;
            case TYPE_TAG:
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.game_flow_layout,null);
                break;
            case TYPE_ALBUM:
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.category_album_item,null);
                break;
        }
        switch (getItemViewType(position)){
            case TYPE_INFO:
                handleInfo((GameInfoBean.GameInfoEntity)o,convertView);
                break;
            case TYPE_TAG:
                handleLabel((ArrayList<String>) o,convertView);
                break;
            case TYPE_ALBUM:
                handleAlbum((GameInfoBean.GameInfoEntity)o,convertView,position);
                if(type == 0)
                    type = ((GameInfoBean.GameInfoEntity) o).type;
                break;
        }
        return convertView;
    }
    private void handleAlbum(final GameInfoBean.GameInfoEntity bean, View convertView,int position) {
        if (bean == null) {
            return;
        }
        RoundedImageView image = ViewHolder.get(convertView, R.id.game_info_iv_image);
        TextView name = ViewHolder.get(convertView, R.id.album_name);
        TextView anchor = ViewHolder.get(convertView, R.id.album_anchor);
        TextView count = ViewHolder.get(convertView,R.id.play_count);
        TextView videoCount = ViewHolder.get(convertView,R.id.video_count);
        TextView tag = ViewHolder.get(convertView,R.id.tag);
        View line = ViewHolder.get(convertView,R.id.line3);
        line.setVisibility(View.VISIBLE);
        if(bean.author == null){
            return;
        }
        anchor.setText(bean.author.nickname);
        count.setText(NumUtils.w(bean.onlines));
        mImageLoader.displayImage(bean.img, image);
        name.setText(bean.name);
        videoCount.setText(bean.programnums+"个视频");
        if(flag == -1){
            tag.setVisibility(View.VISIBLE);
            line.setVisibility(View.GONE);
            flag = position;
        }else if (flag == position){
            tag.setVisibility(View.VISIBLE);
            line.setVisibility(View.GONE);
        }else{
            tag.setVisibility(View.GONE);
        }
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlbumActivity.openAlbumActivity(bean.name, bean._id, getContext());
            }
        });
    }
    private void handleLabel(final ArrayList<String> labels,View convertView) {
        System.out.println(labels);
        ViewHolder.get(convertView,R.id.tag).setVisibility(View.VISIBLE);
        FlowLayout rootView = ViewHolder.get(convertView,R.id.flow_layout);
        ViewGroup.MarginLayoutParams lp = new ViewGroup.MarginLayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.leftMargin = DensityUtil.dip2px(mContext,8);
        lp.topMargin = DensityUtil.dip2px(mContext,6);
        lp.bottomMargin = DensityUtil.dip2px(mContext,6);
        for (int i = 0;i < labels.size();i ++){
                final TextView textView = (TextView) LayoutInflater.from(getContext()).inflate(R.layout.flowlayout_text,null);
            textView.setText(labels.get(i));
            textView.setTextColor(getContext().getResources().getColor(R.color.text_color_101010));
            textView.setBackgroundResource(R.drawable.commen_button_theme_color5);
            final int finalI = i;
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(temText != null){
                        temText.setBackgroundResource(R.drawable.commen_button_theme_color5);
                        temText.setTextColor(getContext().getResources().getColor(R.color.text_color_101010));
                    }
                    textView.setBackgroundResource(R.drawable.commen_button_light_green2);
                    textView.setTextColor(Color.parseColor("#4fc396"));
                    CategoryActivity.openActivity(getContext(), labels.get(finalI), labels.get(finalI), 0);
                    temText = textView;
//                    TagSearchActivity.openTagSearchActivity(getContext(), labels.get(finalI), type);
                }
            });
            rootView.addView(textView, lp);
        }
    }

    private void handleInfo(GameInfoBean.GameInfoEntity bean, View convertView) {
        if(bean == null){
            return;
        }
        TextView albumName = ViewHolder.get(convertView,R.id.video_name);
        TextView videoCount = ViewHolder.get(convertView,R.id.video_count);
        TextView playCount = ViewHolder.get(convertView,R.id.play_count);
        albumName.setText(bean.name);
        videoCount.setText(bean.programnums+"个视频");
        playCount.setText(NumUtils.w(bean.download));
    }
}
