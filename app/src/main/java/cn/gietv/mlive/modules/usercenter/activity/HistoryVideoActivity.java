package cn.gietv.mlive.modules.usercenter.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.ImageView;

import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import java.util.ArrayList;
import java.util.List;

import cn.gietv.mlive.R;
import cn.gietv.mlive.base.AbsBaseActivity;
import cn.gietv.mlive.constants.CommConstants;
import cn.gietv.mlive.http.DefaultLiveHttpCallBack;
import cn.gietv.mlive.modules.program.bean.ProgramBean;
import cn.gietv.mlive.modules.usercenter.adapter.ShouCangAdapter;
import cn.gietv.mlive.modules.usercenter.model.UserCenterModel;
import cn.gietv.mlive.utils.HeadViewController;
import cn.gietv.mlive.utils.NumUtils;
import cn.gietv.mlive.utils.ToastUtils;
import cn.kalading.android.retrofit.utils.RetrofitUtils;

/**
 * Created by houde on 2016/7/8.
 */
public class HistoryVideoActivity extends AbsBaseActivity {
    private XRecyclerView mListView;
    private ShouCangAdapter mAdapter;
    private List<ProgramBean.ProgramEntity> mVideoProgramList;
    private UserCenterModel model;
    private int mCurrentPage;
    private ImageView mDeleteImage ;
    private Dialog mDialog;
    private Context mContext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_video);
        mListView = (XRecyclerView) findViewById(R.id.recyclerview);
        mContext = this;
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mListView.setLayoutManager(layoutManager);
        mDeleteImage = (ImageView) findViewById(R.id.head_ib_search);
        mListView.setRefreshProgressStyle(ProgressStyle.SysProgress);
        mListView.setLaodingMoreProgressStyle(ProgressStyle.SysProgress);
        mListView.setArrowImageView(R.drawable.iconfont_downgrey);
        HeadViewController.initHeadWithoutSearch(this, "历史记录");
        mDeleteImage.setVisibility(View.VISIBLE);
        mVideoProgramList = new ArrayList<>();
        model = RetrofitUtils.create(UserCenterModel.class);
        mCurrentPage = 1;
        getData();
        mListView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                getData();
            }

            @Override
            public void onLoadMore() {
                getData();
            }
        });
        mDeleteImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mDialog == null){
                    AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                    builder.setTitle("提示");
                    builder.setMessage("确定清除历史记录吗？");
                    builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            model.clearHistory(new DefaultLiveHttpCallBack<String>() {
                                @Override
                                public void success(String s) {
                                    if(mVideoProgramList != null){
                                        mVideoProgramList.clear();
                                        mAdapter.notifyDataSetChanged();
                                    }
                                }

                                @Override
                                public void failure(String message) {

                                }
                            });
                            dialog.dismiss();
                        }
                    });
                    builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    mDialog = builder.create();
                }else{
                    mDialog.show();
                }
            }
        });
    }

    private void getData() {
        model.getHistoryVideo(99999, mCurrentPage, CommConstants.DEFAULT_SORT_ONLINE_PERSON, new DefaultLiveHttpCallBack<ProgramBean>() {
            @Override
            public void success(ProgramBean programBean) {
                if(isNotFinish()){
                    mListView.refreshComplete();
                    if(programBean == null || programBean.programs == null){
                        return;
                    }
                    if(NumUtils.getPage(programBean.cnt,99999) == mCurrentPage){
                        mListView.setLoadingMoreEnabled(false);
                    }else if(NumUtils.getPage(programBean.cnt,99999) > mCurrentPage){
                        mListView.setLoadingMoreEnabled(true);
                        mCurrentPage ++;
                    }
                    if(mAdapter == null){
                        mVideoProgramList.clear();
                        mVideoProgramList.addAll(programBean.programs);
                        mAdapter = new ShouCangAdapter(HistoryVideoActivity.this,mVideoProgramList);
                        mListView.setAdapter(mAdapter);
                    }else{
                        mVideoProgramList.addAll(programBean.programs);
                        mAdapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void failure(String message) {
                if(isNotFinish()){
                    ToastUtils.showToastShort(HistoryVideoActivity.this,message);
                    mListView.refreshComplete();
                }
            }
        });
    }
}
