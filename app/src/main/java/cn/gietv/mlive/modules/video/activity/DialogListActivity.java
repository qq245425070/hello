package cn.gietv.mlive.modules.video.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;

import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import java.util.ArrayList;
import java.util.List;

import cn.gietv.mlive.R;
import cn.gietv.mlive.base.AbsBaseActivity;
import cn.gietv.mlive.http.DefaultLiveHttpCallBack;
import cn.gietv.mlive.modules.video.adapter.DialogListAdapter;
import cn.gietv.mlive.modules.video.bean.MessageBean;
import cn.gietv.mlive.modules.video.model.MessageModel;
import cn.gietv.mlive.utils.HeadViewController;
import cn.gietv.mlive.utils.IntentUtils;
import cn.gietv.mlive.utils.ToastUtils;
import cn.kalading.android.retrofit.utils.RetrofitUtils;

/**
 * Created by houde on 2016/6/27.
 * 对话列表的Activity
 */
public class DialogListActivity extends AbsBaseActivity{
    private XRecyclerView mListView;
    private List<MessageBean.MessagesEntity> data;
    private DialogListAdapter mAdapter;
    private Context mContext;
    private MessageModel model ;
    private String mProID,mMsgID;

    public static void openDialogListActivity(Context context,String proid,String msg_id){
        Bundle bundle = new Bundle();
        bundle.putString("proid",proid);
        bundle.putString("msg_id",msg_id);
        IntentUtils.openActivity(context,DialogListActivity.class,bundle);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog_list);
        model = RetrofitUtils.create(MessageModel.class);
        data = new ArrayList<>();
        mProID = getIntent().getStringExtra("proid");
        mMsgID = getIntent().getStringExtra("msg_id");
        mListView = (XRecyclerView) findViewById(R.id.recyclerview);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mListView.setLayoutManager(layoutManager);

        mListView.setRefreshProgressStyle(ProgressStyle.SysProgress);
        mListView.setLaodingMoreProgressStyle(ProgressStyle.SysProgress);
        mListView.setArrowImageView(R.drawable.iconfont_downgrey);
        mContext = this;
        HeadViewController.initHeadWithoutSearch(this, "对话列表");
//        mAdapter = new DialogListAdapter(mContext,data,mProID);
//        mListView.setAdapter(mAdapter);
        mListView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                getData();
            }

            @Override
            public void onLoadMore() {

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        getData();
    }

    private void getData() {
        model.getDialogList(mProID, mMsgID, new DefaultLiveHttpCallBack<MessageBean>() {
            @Override
            public void success(MessageBean messages) {
                mListView.refreshComplete();
                if (isNotFinish()) {
                    if (messages == null && messages.messages == null) {
                        return;
                    }
                }
               // data.clear();
//                data.addAll(messages.messages);
//                mAdapter.notifyDataSetChanged();
                data = messages.messages;
//                if(mAdapter == null) {
                    mAdapter = new DialogListAdapter(mContext, data, mProID);
                    mListView.setAdapter(mAdapter);
//                }else{
//                    mAdapter.notifyDataSetChanged();
//                }
            }

            @Override
            public void failure(String message) {
                if(isNotFinish()){
                    ToastUtils.showToastShort(mContext,message);
                }

            }
        });
    }
}
