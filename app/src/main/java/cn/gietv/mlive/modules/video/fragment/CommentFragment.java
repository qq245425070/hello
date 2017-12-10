package cn.gietv.mlive.modules.video.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import java.util.ArrayList;
import java.util.List;

import cn.gietv.mlive.R;
import cn.gietv.mlive.base.AbsBaseFragment;
import cn.gietv.mlive.http.DefaultLiveHttpCallBack;
import cn.gietv.mlive.modules.login.activity.LoginActivity;
import cn.gietv.mlive.modules.video.activity.VideoPlayListActivity3;
import cn.gietv.mlive.modules.video.adapter.MessageAdapter2;
import cn.gietv.mlive.modules.video.bean.MessageBean;
import cn.gietv.mlive.modules.video.model.MessageModel;
import cn.gietv.mlive.utils.InputUtils;
import cn.gietv.mlive.utils.IntentUtils;
import cn.gietv.mlive.utils.NumUtils;
import cn.gietv.mlive.utils.StringUtils;
import cn.gietv.mlive.utils.ToastUtils;
import cn.gietv.mlive.utils.UserUtils;
import cn.kalading.android.retrofit.utils.RetrofitUtils;

/**
 * Created by houde on 2016/5/30.
 */
public class CommentFragment extends AbsBaseFragment {
    private View mRootView;
    private MessageAdapter2 messageAdapter2;
    private XRecyclerView mListView;
    private TextView mSendButton;
    private EditText mMessageContent;
    private TextView mSofa;
    private String mID;
    private MessageModel model;
    private int mCurrentPage = 1;
    private List<MessageBean.MessagesEntity> messagesEntityList;
    public static CommentFragment getInstance(String id,String urlfrom){
        CommentFragment commentFragment = new CommentFragment();
        Bundle bundle = new Bundle();
        bundle.putString("id", id);
        bundle.putString("urlfrom",urlfrom);
        commentFragment.setArguments(bundle);
        return commentFragment;
    }
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            boolean flag;
            switch (msg.what){
                case 101:
                    flag = (boolean)msg.obj;
                    if(flag)
                        mListView.setLoadingMoreEnabled(true);
                    else
                        mListView.setLoadingMoreEnabled(false);
                    break;
                case 102:
                    flag = (boolean)msg.obj;
                    if(flag)
                        mListView.setPullRefreshEnabled(true);
                    else
                        mListView.setPullRefreshEnabled(false);
                    break;

            }
        }
    };
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_comment,null);
        mListView = (XRecyclerView) mRootView.findViewById(R.id.listView);
        mSendButton = (TextView) mRootView.findViewById(R.id.send_msg_button);
        mMessageContent = (EditText) mRootView.findViewById(R.id.video_et_content);
        mSofa = (TextView) mRootView.findViewById(R.id.sofa);
        mID = getArguments().getString("id");
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mListView.setItemAnimator(new DefaultItemAnimator());
        mListView.setLayoutManager(linearLayoutManager);
        mListView.setRefreshProgressStyle(ProgressStyle.SysProgress);
        mListView.setLaodingMoreProgressStyle(ProgressStyle.SysProgress);
        mListView.setArrowImageView(R.drawable.iconfont_downgrey);
        model = RetrofitUtils.create(MessageModel.class);
        messagesEntityList = new ArrayList<>();
        mListView.setLoadingMoreEnabled(false);
        mListView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                getData();
            }

            @Override
            public void onLoadMore() {
                loadMore();
            }
        });
        mSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSendButton.setClickable(false);
                sendMessage();
            }
        });
        return mRootView;
    }

    private void loadMore() {
        model.getMessageByProId(getArguments().getString("id"), 50, mCurrentPage, 1, new DefaultLiveHttpCallBack<MessageBean>() {
            @Override
            public void success(MessageBean messageBean) {
                if (isNotFinish()) {
                    if (messageBean.messages != null && messageBean.messages.size() > 0) {
                        if (NumUtils.getPage(messageBean.cnt, 50) <= mCurrentPage) {
                            mListView.setLoadingMoreEnabled(false);
                        } else if (NumUtils.getPage(messageBean.cnt, 50) > mCurrentPage) {
                            mListView.setLoadingMoreEnabled(true);
                            mCurrentPage++;
                        }
                        messagesEntityList.addAll(messageBean.messages);
                        mListView.loadMoreComplete();
                        if (messageAdapter2 != null)
                            messageAdapter2.notifyDataSetChanged();
                        else {
                            messageAdapter2 = new MessageAdapter2(getActivity(), messagesEntityList, mID);
                            messageAdapter2.setReportListener(mReportListener);
                            mListView.setAdapter(messageAdapter2);
                        }
                        if (mSofa.getVisibility() == View.VISIBLE)
                            mSofa.setVisibility(View.INVISIBLE);
                    }
                }
            }

            @Override
            public void failure(String message) {
                if (isNotFinish())
                    mListView.loadMoreComplete();
                ToastUtils.showToast(getActivity(), message);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        getData();
    }

    private VideoPlayFragment4.ReportListener mReportListener;
    public void setShowReportWindowListener(VideoPlayFragment4.ReportListener reportWindow){
        this.mReportListener = reportWindow;
    }
    private void getData() {
        mCurrentPage = 1;
            model.getMessageByProId(getArguments().getString("id"), 50, mCurrentPage, 1, new DefaultLiveHttpCallBack<MessageBean>() {
                @Override
                public void success(MessageBean messageBean) {
                    if (isNotFinish()) {
                        if (messageBean.messages != null && messageBean.messages.size() > 0) {
                            if (NumUtils.getPage(messageBean.cnt, 50) <= mCurrentPage) {
                                mListView.setLoadingMoreEnabled(false);
                            } else if (NumUtils.getPage(messageBean.cnt, 50) > mCurrentPage) {
                                mListView.setLoadingMoreEnabled(true);
                                mCurrentPage++;
                            }
                            messagesEntityList = messageBean.messages;
                            mListView.refreshComplete();
                            messageAdapter2 = new MessageAdapter2(getActivity(), messagesEntityList, mID);
                            messageAdapter2.setReportListener(mReportListener);
                            mListView.setAdapter(messageAdapter2);
                            if (mSofa.getVisibility() == View.VISIBLE)
                                mSofa.setVisibility(View.INVISIBLE);
                        }
                    }
                }

                @Override
                public void failure(String message) {
                    if (isNotFinish())
                        ToastUtils.showToast(getActivity(), message);
                }
            });
    }
    private MessageModel messageModel;
    private void sendMessage(){
        if (UserUtils.isNotLogin()) {
            IntentUtils.openActivity(getActivity(), LoginActivity.class);
            mSendButton.setClickable(true);
            return;
        }
        final String message = mMessageContent.getText().toString();
        if (StringUtils.isEmpty(message)) {
            ToastUtils.showToast(getActivity(), "请输入内容");
            mSendButton.setClickable(true);
            return;
        }
        if (message.length() > 1100) {
            ToastUtils.showToast(getActivity(), "输入字数太多，请分多条发送");
            mSendButton.setClickable(true);
            return;
        }
        if(messageModel == null)
            messageModel = RetrofitUtils.create(MessageModel.class);
        messageModel.sendMessage(mID, message, 2,"","","", new DefaultLiveHttpCallBack<MessageBean.MessagesEntity>() {
            @Override
            public void success(MessageBean.MessagesEntity messagesEntity) {
                mSendButton.setClickable(true);
                if (isNotFinish()) {
                    if(messageAdapter2 == null){
                        messageAdapter2 = new MessageAdapter2(getActivity(),new ArrayList<MessageBean.MessagesEntity>(),mID);
                        mListView.setAdapter(messageAdapter2);
                    }
                    if(mSofa.getVisibility() == View.VISIBLE) {
                        mSofa.setVisibility(View.INVISIBLE);
                        if(((VideoPlayListActivity3)getActivity()).mSign != null) {
                            ((VideoPlayListActivity3) getActivity()).mSign.setVisibility(View.INVISIBLE);
                        }
                        if( ((VideoPlayListActivity3) getActivity()).mSignText != null) {
                            ((VideoPlayListActivity3) getActivity()).mSignText.setVisibility(View.VISIBLE);
                            ((VideoPlayListActivity3) getActivity()).mSignText.setText("(1)");
                            ((VideoPlayListActivity3) getActivity()).mSignText.setTextColor(Color.parseColor("#4fc396"));
                        }
                    }else{
                        if(((VideoPlayListActivity3)getActivity()).mSignText != null) {
                            String count = ((VideoPlayListActivity3) getActivity()).mSignText.getText().toString();
                            int countNum = 0;
                            if(TextUtils.isEmpty(count)){
                                countNum = 1;
                            }else {
                                try {
                                    countNum = Integer.valueOf(count.replace("(", "").replace(")", "")) + 1;
                                }catch (NumberFormatException e){
                                    e.printStackTrace();
                                }
                            }
                            ((VideoPlayListActivity3)getActivity()).mSignText.setText("("+countNum+")");
                        }
                    }
                    messageAdapter2.addMessageEntity(messagesEntity);
                    messageAdapter2.setReportListener(mReportListener);
                    messageAdapter2.notifyDataSetChanged();
                    mMessageContent.setText("");
                    InputUtils.closeInputKeyBoard(mMessageContent);
                }
            }

            @Override
            public void failure(String message) {
                if (isNotFinish()) {
                    mSendButton.setClickable(true);
                    ToastUtils.showToast(getActivity(), message);
                }
            }
        });
    }
}
