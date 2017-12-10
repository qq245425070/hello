package cn.gietv.mlive.modules.download.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

import cn.gietv.mlive.R;
import cn.gietv.mlive.base.AbsArrayAdapter;
import cn.gietv.mlive.modules.download.activity.DownloadNoOverActivity;
import cn.gietv.mlive.modules.download.bean.M3U8Bean;
import cn.gietv.mlive.utils.ConfigUtils;
import cn.gietv.mlive.utils.DownLoadVideo;
import cn.gietv.mlive.utils.ImageLoaderUtils;

/**
 * Created by houde on 2016/3/3.
 */
public class DownloadNoOverAdapter extends AbsArrayAdapter<M3U8Bean> {
    private Context context;
    private ImageLoader imageLoader;
    private DownloadNoOverActivity downloadNoOverActivity;
    public DownloadNoOverAdapter(Context context, List<M3U8Bean> objects) {
        super(context, objects);
        this.context = context;
        imageLoader = ImageLoaderUtils.getDefaultImageLoader(context);
        downloadNoOverActivity = (DownloadNoOverActivity)context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final M3U8Bean bean = getItem(position);
        ViewHolder viewHolder = null;
        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.download_item,null);
            viewHolder = new ViewHolder();
            viewHolder.checkBox = (CheckBox) convertView.findViewById(R.id.download_check);
            viewHolder.imageView = (ImageView) convertView.findViewById(R.id.download_video_image);
            viewHolder.videoTitle = (TextView) convertView.findViewById(R.id.download_video_name);
            viewHolder.anchor = (TextView)convertView.findViewById(R.id.download_anchor);
            viewHolder.videoInfo = (TextView)convertView.findViewById(R.id.download_info);
            viewHolder.downloadStatus = (ImageView) convertView.findViewById(R.id.download_status);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        if(type == 0){
            viewHolder.checkBox.setVisibility(View.GONE);
        }else{
            viewHolder.checkBox.setVisibility(View.VISIBLE);
            final ViewHolder finalViewHolder = viewHolder;
            viewHolder.checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    bean.setCheck(finalViewHolder.checkBox.isChecked());
                }
            });
        }
        if(viewHolder.imageView.getTag() != null && bean.getImage() != null && (bean.getImage()).equals((String)viewHolder.imageView.getTag())){

        }else {
            imageLoader.displayImage(bean.getImage(), viewHolder.imageView);
            viewHolder.imageView.setTag(bean.getImage());
        }
        viewHolder.downloadStatus.setVisibility(View.VISIBLE);
        if(ConfigUtils.STATUS_PAUSE.equals(bean.getStatus())){
            viewHolder.downloadStatus.setBackgroundResource(R.mipmap.live_icon_usercenter);
            final ViewHolder finalViewHolder2 = viewHolder;
            viewHolder.downloadStatus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    bean.setStatus(ConfigUtils.STATUS_RUNNING);
                    finalViewHolder2.downloadStatus.setBackgroundResource(R.mipmap.download_resume);
                    new Thread(){
                        @Override
                        public void run() {
                            DownLoadVideo.getInstance(getContext()).resumeDownload(getContext(), bean.getName());
                        }
                    }.start();
                    notifyDataSetChanged();
                }
            });
        }else if(ConfigUtils.STATUS_RUNNING.equals(bean.getStatus())){
            viewHolder.downloadStatus.setBackgroundResource(R.mipmap.download_resume);
            final ViewHolder finalViewHolder1 = viewHolder;
            viewHolder.downloadStatus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    bean.setStatus(ConfigUtils.STATUS_PAUSE);
                    finalViewHolder1.downloadStatus.setBackgroundResource(R.mipmap.live_icon_usercenter);
                    new Thread(){
                        @Override
                        public void run() {
                            DownLoadVideo.getInstance(getContext()).pauseDownload(getContext(), bean.getName());
                        }
                    }.start();
                    notifyDataSetChanged();
                }
            });
        }
        viewHolder.videoInfo.setText("完成" + (bean.getProgress() * 100/ bean.getTotal()) + "%");
        viewHolder.anchor.setText(bean.getAnchor());
        viewHolder.videoTitle.setText(bean.getName());
        return convertView;
    }

    class ViewHolder{
        CheckBox checkBox;
        ImageView imageView,downloadStatus;
        TextView videoTitle,anchor,videoInfo;
    }
    private int type ;
    public void setType(int type){
        this.type = type;
    }
    public int getType(){
        return this.type;
    }
}
