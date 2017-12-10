package cn.gietv.mlive.modules.download.activity;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.gietv.mlive.R;
import cn.gietv.mlive.base.AbsBaseActivity;
import cn.gietv.mlive.db.DBUtils;
import cn.gietv.mlive.modules.download.adapter.DownloadNoOverAdapter;
import cn.gietv.mlive.modules.download.bean.M3U8Bean;
import cn.gietv.mlive.utils.ConfigUtils;
import cn.gietv.mlive.utils.DownLoadVideo;
import cn.gietv.mlive.utils.HeadViewController;
import cn.gietv.mlive.utils.SDCardUtils;

public class DownloadNoOverActivity extends AbsBaseActivity implements View.OnClickListener{
    private ListView listView;
    private TextView sdcardInfo,allDownload;
    private DownloadNoOverAdapter adapter;
    private List<M3U8Bean> noOverVideo;
    private List<M3U8Bean> tempList;
    private Map<String ,M3U8Bean> mMap ;
    private boolean flag = true;
    private ImageButton mSearchButton;
    private TextView mSubmit;
    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            tempList = DBUtils.getInstance(DownloadNoOverActivity.this).getAllNoOverBean();
            if(tempList != null && tempList.size() > 0){
                for(int i = 0;i<tempList.size();i++){
                    mMap.get(tempList.get(i).getName()).setProgress(tempList.get(i).getProgress());
                    System.out.println(tempList.get(i).getProgress());
                }
            }
            if(noOverVideo.size() != tempList.size()){
                noOverVideo = tempList;
            }
            adapter.notifyDataSetChanged();
            mHandler.removeCallbacks(mRunnable);
            mHandler.postDelayed(mRunnable,3000);
        }
    };
    private Handler mHandler = new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download_no_over);
        mMap = new HashMap<>();
        sdcardInfo = (TextView) findViewById(R.id.sdcard_info);
        allDownload = (TextView) findViewById(R.id.all_download);
        sdcardInfo.setText("已用 ：" + SDCardUtils.getUseMemory(this) + "可用 ：" + SDCardUtils.getAvailablesMomery(this));
        allDownload.setOnClickListener(this);
        listView = (ListView) findViewById(R.id.listView);
        noOverVideo = DBUtils.getInstance(this).getAllNoOverBean();
        adapter = new DownloadNoOverAdapter(this, noOverVideo);
        listView.setAdapter(adapter);
        HeadViewController.initHeadWithoutSearch(this, "未完成");
        mSearchButton = (ImageButton)findViewById(R.id.head_ib_search);
        mSubmit = (TextView) findViewById(R.id.submit);
        mSearchButton.setVisibility(View.VISIBLE);
        mSearchButton.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.delete_black));
        mSearchButton.setOnClickListener(this);
        mSubmit.setOnClickListener(this);
        for(int i= 0;i<noOverVideo.size();i++){
            mMap.put(noOverVideo.get(i).getName(),noOverVideo.get(i));
            if(noOverVideo.get(i).getStatus().equals(ConfigUtils.STATUS_RUNNING)){
                flag = false;
            }
        }
        if(!flag){
            allDownload.setText("全部暂停");
        }else{
            allDownload.setText("全部开始");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mHandler.post(mRunnable);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mHandler.removeCallbacks(mRunnable);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.all_download) {
            if (flag) {
                for (int i = 0; i < noOverVideo.size(); i++) {
                    if (noOverVideo.get(i).getStatus().equals(ConfigUtils.STATUS_PAUSE)) {
                        DownLoadVideo.getInstance(this).resumeDownload(this, noOverVideo.get(i).getName());
                        noOverVideo.get(i).setStatus(ConfigUtils.STATUS_RUNNING);
                    }
                }
                flag = false;
                allDownload.setText("全部暂停");
                adapter.notifyDataSetChanged();
            } else {
                for (int i = 0; i < noOverVideo.size(); i++) {
                    if (noOverVideo.get(i).getStatus().equals(ConfigUtils.STATUS_RUNNING)) {
                        DownLoadVideo.getInstance(this).pauseDownload(this, noOverVideo.get(i).getName());
                        noOverVideo.get(i).setStatus(ConfigUtils.STATUS_PAUSE);
                    }
                }
                flag = true;
                allDownload.setText("全部开始");
                adapter.notifyDataSetChanged();
            }
        }else if(v.getId() == R.id.head_ib_search){
                if(adapter.getType() ==0){
                    adapter.setType(1);
                    adapter.notifyDataSetChanged();
                    mSearchButton.setVisibility(View.INVISIBLE);
                    mSubmit.setVisibility(View.VISIBLE);
                }
        }else if(v.getId() == R.id.submit){
            List<M3U8Bean> beans = new ArrayList<>();
            for (int i = 0; i < noOverVideo.size(); i++) {
                if (noOverVideo.get(i).isCheck()) {
                    beans.add(noOverVideo.get(i));
                }
            }
            DownLoadVideo.getInstance(this).deleteFile(beans, this);
            for (int i = 0; i < beans.size(); i++) {
                DBUtils.getInstance(this).deleteM3u8(beans.get(i).getName());
            }
            adapter.setType(0);
            adapter = new DownloadNoOverAdapter(this, DBUtils.getInstance(this).getAllNoOverBean());
            listView.setAdapter(adapter);
            mSearchButton.setVisibility(View.VISIBLE);
            mSubmit.setVisibility(View.GONE);
        }
    }
}
