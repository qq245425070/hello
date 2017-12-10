package cn.gietv.mlive.modules.vrgame.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import java.util.ArrayList;
import java.util.List;

import cn.gietv.mlive.R;
import cn.gietv.mlive.base.AbsBaseFragment;
import cn.gietv.mlive.constants.CommConstants;
import cn.gietv.mlive.http.DefaultLiveHttpCallBack;
import cn.gietv.mlive.modules.video.bean.MessageBean;
import cn.gietv.mlive.modules.vrgame.adapter.GameCommentAdapter;
import cn.gietv.mlive.modules.vrgame.model.VRGameModel;
import cn.gietv.mlive.utils.NumUtils;
import cn.gietv.mlive.utils.ProgressDialogUtils;
import cn.gietv.mlive.utils.ToastUtils;
import cn.kalading.android.retrofit.utils.RetrofitUtils;

/**
 * Created by houde on 2016/6/24.
 */
public class VRGameCommentFragment extends AbsBaseFragment{
    private View mRootView;
    private XRecyclerView mListView;
    private GameCommentAdapter mAdapter;
    private List<Object> mMessageData;
    private VRGameModel model;
    private String mID;
    private int mCurrentPage;
    public static VRGameCommentFragment getInstance(String id){
        VRGameCommentFragment fragment = new VRGameCommentFragment();
        Bundle bundle = new Bundle();
        bundle.putString("id", id);
        fragment.setArguments(bundle);
        return fragment;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_vrgame_comment,null);
        mListView = (XRecyclerView) mRootView.findViewById(R.id.recyclerview);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mListView.setLayoutManager(layoutManager);
        mListView.setRefreshProgressStyle(ProgressStyle.SysProgress);
        mListView.setLaodingMoreProgressStyle(ProgressStyle.SysProgress);
        mListView.setArrowImageView(R.drawable.iconfont_downgrey);
        mID = getArguments().getString("id");
        mCurrentPage = 1;
        model = RetrofitUtils.create(VRGameModel.class);
        mMessageData = new ArrayList<>();
        mAdapter = new GameCommentAdapter(getActivity(),mMessageData, mID);
        mListView.setAdapter(mAdapter);
        getData();
        mListView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                getData();
            }

            @Override
            public void onLoadMore() {
                onPullUpToRefresh();
            }
        });
        return mRootView;
    }

    private void getData() {
        model.getMessagesByProId(mID, CommConstants.COMMON_PAGE_COUNT, mCurrentPage, CommConstants.DEFAULT_SORT_ONLINE_PERSON, new DefaultLiveHttpCallBack<MessageBean>() {
            @Override
            public void success(MessageBean messages) {
                if(isNotFinish()){
                    if(dialog != null && dialog.isShowing()){
                        dialog.dismiss();
                    }
                    mListView.refreshComplete();
                    if(messages == null || messages.messages == null ){
                        return ;
                    }
                    if(NumUtils.getPage(messages.cnt) == mCurrentPage){
                        mListView.setLoadingMoreEnabled(false);
                    }else if(NumUtils.getPage(messages.cnt ) > mCurrentPage){
                        mListView.setLoadingMoreEnabled(true);
                        mCurrentPage ++;
                    }
                    mMessageData.clear();
                    if(messages.scoreratio != null ){
                        mMessageData.add(messages.scoreratio);
                    }
                    mMessageData.addAll(messages.messages);
                    mAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void failure(String message) {
                if(isNotFinish()){
                    mListView.refreshComplete();
                    ToastUtils.showToastShort(getActivity(),message);
                }
            }
        });
    }

    public void onPullUpToRefresh() {
        model.getMessagesByProId(mID, CommConstants.COMMON_PAGE_COUNT, mCurrentPage, CommConstants.DEFAULT_SORT_ONLINE_PERSON, new DefaultLiveHttpCallBack<MessageBean>() {
            @Override
            public void success(MessageBean messages) {
                if (isNotFinish()) {
                    mListView.loadMoreComplete();
                    if (messages == null || messages.messages == null) {
                        return;
                    }
                    if (NumUtils.getPage(messages.cnt) == mCurrentPage) {
                        mListView.setLoadingMoreEnabled(false);
                    } else if (NumUtils.getPage(messages.cnt) > mCurrentPage) {
                        mListView.setLoadingMoreEnabled(true);
                        mCurrentPage++;
                    }
                    mMessageData.addAll(messages.messages);
                    mAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void failure(String message) {
                if (isNotFinish()) {
                    mListView.loadMoreComplete();
                    ToastUtils.showToastShort(getActivity(), message);
                }
            }
        });
    }
//
//    @Override
//    public void onResume() {
//        super.onResume();
//        getData();
//    }
    private ProgressDialog dialog;
    public void setMessage(MessageBean.MessagesEntity message){
        if(message != null) {
            dialog = ProgressDialogUtils.createShowDialog(getActivity(),"正在请求数据");
            getData();
//            mMessageData.add(1, message);
//            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == 66 && requestCode == Activity.RESULT_OK && data != null){
            MessageBean.MessagesEntity message = (MessageBean.MessagesEntity) data.getSerializableExtra("message");
            mMessageData.add(1,message);
            mAdapter.notifyDataSetChanged();
        }

    }
}
