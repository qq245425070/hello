package cn.gietv.mlive.modules.download.activity;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.gietv.mlive.R;
import cn.gietv.mlive.base.AbsBaseActivity;
import cn.gietv.mlive.db.DBUtils;
import cn.gietv.mlive.modules.download.adapter.DownloadOverAdapter;
import cn.gietv.mlive.modules.download.bean.M3U8Bean;
import cn.gietv.mlive.utils.DownLoadVideo;
import cn.gietv.mlive.utils.HeadViewController;
import cn.gietv.mlive.utils.IntentUtils;
import cn.gietv.mlive.utils.SDCardUtils;

public class DownloadOverAcitvity extends AbsBaseActivity implements OnClickListener {

    private TextView noOverTitle,sdCardInfo;
    private ListView listView;
    private DownloadOverAdapter adapter ;
    private List<M3U8Bean> noOverVideo;
    private LinearLayout noOverParent;
    private ImageButton mSearchButton;
    private TextView mSubmit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download_over_acitvity);
        HeadViewController.initHeadWithoutSearch(this, "全部缓存");
        noOverTitle = (TextView) findViewById(R.id.no_over_download);
        sdCardInfo = (TextView) findViewById(R.id.sdcard_info);
        listView = (ListView) findViewById(R.id.listView);
        noOverParent = (LinearLayout) findViewById(R.id.no_over_parent);
        noOverParent.setOnClickListener(this);
        mSearchButton = (ImageButton)findViewById(R.id.head_ib_search);
        mSearchButton.setVisibility(View.VISIBLE);
        mSearchButton.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.delete_black));
        mSubmit = (TextView) findViewById(R.id.submit);
        mSearchButton.setOnClickListener(this);
        mSubmit.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        sdCardInfo.setText("已用 ：" + SDCardUtils.getUseMemory(this) + "可用 ：" + SDCardUtils.getAvailablesMomery(this));
        noOverTitle.setText( "缓存未完成  " + "("+DBUtils.getInstance(this).getAllNoOverBeanCount()+")");
        noOverVideo = DBUtils.getInstance(this).getAllSuccessBean();
        adapter = new DownloadOverAdapter(this,noOverVideo);
        listView.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.no_over_parent) {
            IntentUtils.openActivity(this, DownloadNoOverActivity.class);
        }else if(v.getId() == R.id.head_ib_search){
                if(adapter.getType() == 0){
                    adapter.setType(1);
                    adapter.notifyDataSetChanged();
                    mSearchButton.setVisibility(View.INVISIBLE);
                    mSubmit.setVisibility(View.VISIBLE);
                }
        }else if(v.getId() == R.id.submit) {
            mSearchButton.setVisibility(View.VISIBLE);
            mSubmit.setVisibility(View.GONE);
                    List<M3U8Bean> beans = new ArrayList<>();
                    for (int i = 0; i < noOverVideo.size(); i++) {
                        if (noOverVideo.get(i).isCheck()) {
                            beans.add(noOverVideo.get(i));
                        }
                    }
                    DownLoadVideo.getInstance(this).deleteFile(beans, this);
                    for (int i = 0; i < beans.size(); i++) {
                        DBUtils.getInstance(DownloadOverAcitvity.this).deleteM3u8(beans.get(i).getName());
                    }
                    adapter.setType(0);
                    adapter = new DownloadOverAdapter(this, DBUtils.getInstance(this).getAllSuccessBean());
                    listView.setAdapter(adapter);
                    mSearchButton.setVisibility(View.VISIBLE);
                    mSubmit.setVisibility(View.GONE);
                }
        }
}
