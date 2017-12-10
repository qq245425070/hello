package cn.gietv.mlive.modules.video.adapter;

import android.app.ProgressDialog;
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
import cn.gietv.mlive.modules.download.bean.DownLoadBean;
import cn.gietv.mlive.modules.download.bean.M3U8Bean;
import cn.gietv.mlive.utils.DownLoadVideo;
import cn.gietv.mlive.utils.ProgressDialogUtils;
import cn.gietv.mlive.utils.ToastUtils;

/**
 * Created by houde on 2016/3/3.
 */
public class DownloadPopuAdapter extends AbsArrayAdapter<DownLoadBean> {
    private Context mContext;
    public DownloadPopuAdapter(Context context, List<DownLoadBean> objects) {
        super(context, objects);
        this.mContext = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final DownLoadBean downLoadBean = getItem(position);
        ViewHolder viewHolder = null;
        if(convertView == null){
            convertView = LayoutInflater.from(mContext).inflate(R.layout.popu_download_item,null);
            viewHolder = new ViewHolder();
            viewHolder.imageView = (ImageView)convertView.findViewById(R.id.is_check);
            viewHolder.title = (TextView)convertView.findViewById(R.id.title);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder)convertView.getTag();
        }
        viewHolder.title.setText(downLoadBean.getTitle());
        if(downLoadBean.isCheck()){
            viewHolder.imageView.setVisibility(View.VISIBLE);
        }else{
            viewHolder.imageView.setVisibility(View.INVISIBLE);
        }
        M3U8Bean bean = DBUtils.getInstance(getContext()).getBean(downLoadBean.getTitle());
        if(bean != null){
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ToastUtils.showToast(getContext(),"视频已经下载，可以直接观看");
                }
            });
        }else {
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final ProgressDialog dialog = ProgressDialogUtils.createShowDialog(getContext(),"正在添加下载任务，请等候...");
                    dialog.setCancelable(false);
                    downLoadBean.setCheck(true);
                    notifyDataSetChanged();
                    new Thread() {
                        @Override
                        public void run() {
                            DownLoadVideo.getInstance(getContext()).getNetwork(Environment.getExternalStoragePublicDirectory("download") + "/" + downLoadBean.getTitle(), getContext(), downLoadBean.getUrl(), downLoadBean.getTitle(), downLoadBean.getImagePath(), downLoadBean.getAnchor());
                            dialog.dismiss();
                        }
                    }.start();
                }
            });
        }
        return convertView;
    }
    class ViewHolder {
        TextView title;
        ImageView imageView;
    }
}
