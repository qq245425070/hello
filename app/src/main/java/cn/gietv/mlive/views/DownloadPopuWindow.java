package cn.gietv.mlive.views;

import android.content.Context;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import java.util.List;

import cn.gietv.mlive.R;
import cn.gietv.mlive.modules.download.activity.DownloadOverAcitvity;
import cn.gietv.mlive.modules.download.bean.DownLoadBean;
import cn.gietv.mlive.modules.video.adapter.DownloadPopuAdapter;
import cn.gietv.mlive.utils.DownLoadVideo;
import cn.gietv.mlive.utils.IntentUtils;
import cn.gietv.mlive.utils.SDCardUtils;

/**
 * Created by houde on 2016/3/2.
 */
public class DownloadPopuWindow extends PopupWindow implements View.OnClickListener{
    private Context mContext;
    private View rootView;
    private CheckBox checkBox;
    private TextView mAllDownloadButton,mUseTitle,mDownloadManager;
    private ImageView mCloseButton;
    private ListView mListView;
    private DownloadPopuAdapter mAdapter;
    private List<DownLoadBean> beans;
    public DownloadPopuWindow(Context context,List<DownLoadBean> beans){
        this.mContext = context;
        this.beans = beans;
        LayoutInflater inflater = LayoutInflater.from(context);
        rootView = inflater.inflate(R.layout.popu_download, null);
        checkBox = (CheckBox) rootView.findViewById(R.id.checkbox);
        mAllDownloadButton = (TextView)rootView.findViewById(R.id.all_download);
        mUseTitle = (TextView)rootView.findViewById(R.id.use_title);
        mDownloadManager = (TextView) rootView.findViewById(R.id.download_manager);
        mListView = (ListView)rootView.findViewById(R.id.listView);
        mCloseButton = (ImageView) rootView.findViewById(R.id.close_button);
        mCloseButton.setOnClickListener(this);
        mAllDownloadButton.setOnClickListener(this);
        mDownloadManager.setOnClickListener(this);
        mUseTitle.setText("已用 ：" + SDCardUtils.getUseMemory(context) + "可用 ：" + SDCardUtils.getAvailablesMomery(context));
        mAdapter = new DownloadPopuAdapter(context,beans);
        mListView.setAdapter(mAdapter);
        this.setContentView(rootView);
    }
    DownLoadBean downLoadBean;
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.all_download) {
            for (int i = 0; i < beans.size(); i++) {
                downLoadBean = beans.get(i);
                new Thread() {
                    @Override
                    public void run() {
                        downLoadBean.setCheck(true);
                        DownLoadVideo.getInstance(mContext).getNetwork(Environment.getExternalStoragePublicDirectory("download") + "/" + downLoadBean.getTitle(), mContext, downLoadBean.getUrl(), downLoadBean.getTitle(), downLoadBean.getImagePath(), downLoadBean.getAnchor());
                    }
                }.start();
            }
            mAdapter.notifyDataSetChanged();
        }else if(v.getId() == R.id.download_manager) {
            IntentUtils.openActivity(mContext, DownloadOverAcitvity.class);
        }else if(v.getId() == R.id.close_button){
                this.dismiss();
        }
    }
}
