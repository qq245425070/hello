package cn.gietv.mlive.modules.album.adapter;

import android.content.Context;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import cn.gietv.mlive.R;
import cn.gietv.mlive.base.AbsArrayAdapter;
import cn.gietv.mlive.db.DBUtils;
import cn.gietv.mlive.http.DefaultLiveHttpCallBack;
import cn.gietv.mlive.modules.program.bean.ProgramBean;
import cn.gietv.mlive.modules.video.activity.VideoPlayListActivity3;
import cn.gietv.mlive.modules.video.model.VideoModel;
import cn.gietv.mlive.utils.DownLoadVideo;
import cn.gietv.mlive.utils.NumUtils;
import cn.gietv.mlive.utils.ToastUtils;
import cn.kalading.android.retrofit.utils.RetrofitUtils;

/**
 * Created by houde on 2016/5/28.
 */
public class VideoAdapter extends AbsArrayAdapter<ProgramBean.ProgramEntity> {
    private Context mContext;
    private VideoModel model;
    public VideoAdapter(Context context, List<ProgramBean.ProgramEntity> objects) {
        super(context, objects);
        this.mContext = context;
        model = RetrofitUtils.create(VideoModel.class);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ProgramBean.ProgramEntity bean = getItem(position);
        final ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.video_item, null);
            viewHolder = new ViewHolder();
            viewHolder.playCount = (TextView) convertView.findViewById(R.id.play_count);
            viewHolder.videoName = (TextView) convertView.findViewById(R.id.video_name);
            viewHolder.videoDownLoad = (ImageView) convertView.findViewById(R.id.video_download);
            viewHolder.videoPlay = (ImageView) convertView.findViewById(R.id.video_play);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.videoName.setText(bean.name);
        viewHolder.playCount.setIncludeFontPadding(false);
        viewHolder.playCount.setText(NumUtils.w(bean.onlines));
        if(null != DBUtils.getInstance(mContext).getVideoById(bean._id)){
            viewHolder.videoName.setTextColor(mContext.getResources().getColor(R.color.text_929292));
        }
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VideoPlayListActivity3.openVideoPlayListActivity2(getContext(),bean);
                viewHolder.videoName.setTextColor(mContext.getResources().getColor(R.color.text_929292));
            }
        });
        if(bean.userinfo == null){
            return convertView;
        }
        viewHolder.videoDownLoad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                model.getPlayUrl(bean._id, new DefaultLiveHttpCallBack<String>() {
                    @Override
                    public void success(final String s) {
                        ToastUtils.showToast(getContext(), "开始下载");
                        new Thread(){
                            @Override
                            public void run() {
                                if(DownLoadVideo.getInstance(mContext).getNetwork(Environment.getExternalStoragePublicDirectory("download") + "/" + bean.name, mContext, s, bean.name, bean.spic, bean.userinfo.nickname)){
                                    if(downloadListener != null){
                                        downloadListener.setDownloadResult(true);
                                    }
                                }else {
                                    if(downloadListener != null){
                                        downloadListener.setDownloadResult(false);
                                    }
                                }
                            }
                        }.start();

                    }

                    @Override
                    public void failure(String message) {

                    }
                });

            }
        });
        return convertView;
    }
    class ViewHolder {
        TextView videoName;
        TextView playCount;
        ImageView videoPlay;
        ImageView videoDownLoad;
    }
    public void setDownloadListener(DownloadListener listener){
        this.downloadListener = listener;
    }
    private DownloadListener downloadListener;
    public interface DownloadListener{
        void setDownloadResult(boolean result);
    }
}
