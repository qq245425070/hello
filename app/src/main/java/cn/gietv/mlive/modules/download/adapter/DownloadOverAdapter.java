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
import cn.gietv.mlive.modules.download.bean.M3U8Bean;
import cn.gietv.mlive.modules.video.activity.VideoPlayerActivity2;
import cn.gietv.mlive.utils.ImageLoaderUtils;

/**
 * Created by houde on 2016/3/3.
 */
public class DownloadOverAdapter extends AbsArrayAdapter<M3U8Bean> {
    private Context context;
    private ImageLoader imageLoader;
    private final int DOWNLOAD_STATUS_RESUME = 0;
    private final int DOWNLOAD_STATUS_PAUSE = 0;
    private int currentStatus;
    public DownloadOverAdapter(Context context, List<M3U8Bean> objects) {
        super(context, objects);
        this.context = context;
        imageLoader = ImageLoaderUtils.getDefaultImageLoader(context);
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
        imageLoader.displayImage(bean.getImage(), viewHolder.imageView);
        viewHolder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Bundle bundle = new Bundle();
//                bundle.putString("path", bean.getNativePath());
//                bundle.putInt("model", 2);
//                IntentUtils.openActivity(getContext(), VRVideoPlayActivity.class, bundle);
                VideoPlayerActivity2.openVideoPlayerActivity(getContext(), bean.getNativePath(), bean.getName(), 0, bean.getName(), 2);
            }
        });
        viewHolder.videoInfo.setText("已完成");
        viewHolder.anchor.setText(bean.getAnchor());
        viewHolder.videoTitle.setText(bean.getName());
        return convertView;
    }
    class ViewHolder{
        CheckBox checkBox;
        ImageView imageView;
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
