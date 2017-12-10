package cn.gietv.mlive.modules.video.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;


import java.util.ArrayList;
import java.util.List;

import cn.gietv.mlive.MainApplication;
import cn.gietv.mlive.R;
import cn.gietv.mlive.base.AbsBaseFragment;
import cn.gietv.mlive.http.DefaultLiveHttpCallBack;
import cn.gietv.mlive.modules.program.bean.ProgramBean;
import cn.gietv.mlive.modules.video.adapter.VideoPlayDescAdapter;
import cn.gietv.mlive.modules.video.bean.VideoPlayBean;
import cn.gietv.mlive.modules.video.model.VideoModel;
import cn.gietv.mlive.utils.DensityUtil;
import cn.gietv.mlive.utils.ToastUtils;
import cn.gietv.mlive.views.SharePopWindow;
import cn.kalading.android.retrofit.utils.RetrofitUtils;

/**
 * Created by houde on 2016/5/29.
 */
public class VideoPlayDescFragment extends AbsBaseFragment{
    private View mRootView;
    private XRecyclerView mListView;
    private VideoPlayDescAdapter mAdapter;
    private int mTag = 1;
    public static VideoPlayDescFragment getInstance(String id){
        VideoPlayDescFragment fragment = new VideoPlayDescFragment();
        Bundle bundle = new Bundle();
        bundle.putString("id", id);
        fragment.setArguments(bundle);
        return  fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_category2, container,false);
        mListView = (XRecyclerView) mRootView.findViewById(R.id.recyclerview);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mListView.setLayoutManager(layoutManager);
        mListView.setRefreshProgressStyle(ProgressStyle.SysProgress);
        mListView.setLaodingMoreProgressStyle(ProgressStyle.SysProgress);
        mListView.setArrowImageView(R.drawable.iconfont_downgrey);
        mListView.setPullRefreshEnabled(false);
        mListView.setLoadingMoreEnabled(false);
        onPullDownToRefresh();
        return mRootView;
    }

    public void onPullDownToRefresh() {
        VideoModel model = RetrofitUtils.create(VideoModel.class);
        model.queryProByProid(getArguments().getString("id"), new DefaultLiveHttpCallBack<VideoPlayBean>() {
            @Override
            public void success(VideoPlayBean videoPlayBean) {
                if(isNotFinish()) {
                    mListView.refreshComplete();
                    List<Object> datas = new ArrayList<>();
                    if(videoPlayBean == null){
                        ToastUtils.showToast(getActivity(),"网络错误");
                        return;
                    }
                    if(videoPlayBean.program == null){
                        ToastUtils.showToast(getActivity(),"网络错误");
                        return;
                    }
                    if (videoPlayBean.program != null) {
                        datas.add(videoPlayBean.program);
                        if (videoPlayBean.program.tags != null && videoPlayBean.program.tags.size() > 0)
                            datas.add(videoPlayBean.program.tags);
                        mTag += 1;
                    }
                    if (videoPlayBean.programlist_album != null && videoPlayBean.programlist_album.size() > 0) {
                        datas.addAll(videoPlayBean.programlist_album);
                        mTag += videoPlayBean.programlist_album.size();
                    }
                    if (videoPlayBean.albumlist_tag != null && videoPlayBean.albumlist_tag.size() > 0) {
                        datas.addAll(videoPlayBean.albumlist_tag);
                        mTag += videoPlayBean.albumlist_tag.size();
                    }
                    if (videoPlayBean.programlist_tag != null && videoPlayBean.programlist_tag.size() > 0) {
                        datas.addAll(videoPlayBean.programlist_tag);
                    }
                    mAdapter = new VideoPlayDescAdapter(getActivity(), datas, mTag);
                    mListView.setAdapter(mAdapter);
                    mAdapter.setShareOnClick(new VideoPlayDescAdapter.AdapterListener() {
                        @Override
                        public void shareListener(ProgramBean.ProgramEntity mProgram) {
                            showSharePopu(mProgram);
                        }
                    });
                }
            }

            @Override
            public void failure(String message) {
                if(isNotFinish())
                    mListView.refreshComplete();
                    ToastUtils.showToast(MainApplication.getInstance(),message);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /**使用SSO授权必须添加如下代码 */
//        UMSsoHandler ssoHandler =  UMServiceFactory.getUMSocialService("com.umeng.share").getConfig().getSsoHandler(requestCode) ;
//        if(ssoHandler != null){
//            ssoHandler.authorizeCallBack(requestCode, resultCode, data);
//        }
    }
    private SharePopWindow mSharePopWindow;
    private void showSharePopu(ProgramBean.ProgramEntity mProgram) {
        if(mSharePopWindow == null ) {
            mSharePopWindow = new SharePopWindow(getActivity(), mProgram.spic, mProgram.shareurl, "我正在看 " + mProgram.name);
            mSharePopWindow.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
            mSharePopWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
            mSharePopWindow.setFocusable(true);
            mSharePopWindow.setOutsideTouchable(true);
            mSharePopWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
        mSharePopWindow.setData(mProgram._id,mProgram.type);
        WindowManager wm = (WindowManager) getActivity().getSystemService(Context.WINDOW_SERVICE);
        int height = wm.getDefaultDisplay().getHeight();
        mSharePopWindow.showAtLocation(mListView, Gravity.NO_GRAVITY, 0, height - mSharePopWindow.getHeight() - DensityUtil.dip2px(getActivity(), 52));
    }
}
