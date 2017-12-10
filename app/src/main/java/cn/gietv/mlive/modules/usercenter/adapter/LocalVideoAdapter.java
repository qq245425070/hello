package cn.gietv.mlive.modules.usercenter.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.gietv.mlive.R;
import cn.gietv.mlive.modules.usercenter.bean.LoadedImage;
import cn.gietv.mlive.modules.usercenter.bean.Video;

/**
 * Created by houde on 2016/4/22.
 */
public class LocalVideoAdapter extends BaseAdapter {
    List<Video> listVideos;
    private LayoutInflater mLayoutInflater;
    private ArrayList<LoadedImage> photos = new ArrayList<>();
    public LocalVideoAdapter(Context context, List<Video> listVideos){
        mLayoutInflater = LayoutInflater.from(context);
        this.listVideos = listVideos;
    }
    @Override
    public int getCount() {
        return photos.size();
    }
    public void addPhoto(LoadedImage image){
        photos.add(image);
    }
    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = mLayoutInflater.inflate(R.layout.shoucang_item, null);
            viewHolder.checkBox = (CheckBox) convertView.findViewById(R.id.download_check);
            viewHolder.imageView = (ImageView) convertView.findViewById(R.id.download_video_image);
            viewHolder.videoTitle = (TextView) convertView.findViewById(R.id.download_video_name);
            viewHolder.anchor = (TextView)convertView.findViewById(R.id.download_anchor);
            viewHolder.videoInfo = (TextView)convertView.findViewById(R.id.download_info);
            viewHolder.mCount = (ImageView)convertView.findViewById(R.id.shoucang_count);
            convertView.setTag(viewHolder);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder)convertView.getTag();
        }
        viewHolder.videoTitle.setText(listVideos.get(position).getTitle());//ms
        viewHolder.imageView.setImageBitmap(photos.get(position).getBitmap());
        viewHolder.mCount.setVisibility(View.INVISIBLE);
        System.out.println(listVideos.get(position).getDisplayName());
        return convertView;
    }

    class ViewHolder{
        CheckBox checkBox;
        ImageView imageView,mCount;
        TextView videoTitle,anchor,videoInfo;
    }
}
