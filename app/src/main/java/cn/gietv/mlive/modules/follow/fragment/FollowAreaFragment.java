package cn.gietv.mlive.modules.follow.fragment;

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
import cn.gietv.mlive.db.DBUtils;
import cn.gietv.mlive.http.DefaultLiveHttpCallBack;
import cn.gietv.mlive.modules.follow.adapter.FollowAreaAdapter;
import cn.gietv.mlive.modules.game.bean.GameInfoBean;
import cn.gietv.mlive.modules.game.model.GameModel;
import cn.gietv.mlive.modules.program.bean.ProgramBean;
import cn.gietv.mlive.modules.program.model.ProgramModel;
import cn.gietv.mlive.modules.usercenter.model.UserCenterModel;
import cn.gietv.mlive.utils.NumUtils;
import cn.gietv.mlive.utils.ToastUtils;
import cn.gietv.mlive.utils.UserUtils;
import cn.kalading.android.retrofit.utils.RetrofitUtils;

/**
 * Created by houde on 2016/8/24.
 */
public class FollowAreaFragment extends AbsBaseFragment {
    private View mRootView;
    private XRecyclerView mRecyclerView;
    private GameModel gameModel;
    private String mID;
    private int mCurrentPage = 1;
    private List<ProgramBean.ProgramEntity> programEntityList;
    private FollowAreaAdapter mAdapter;
    public static FollowAreaFragment getInstance(String id,String category){
        FollowAreaFragment followAreaFragment = new FollowAreaFragment();
        Bundle bundle = new Bundle();
        bundle.putString("id", id);
        bundle.putString("category", category);
        followAreaFragment.setArguments(bundle);
        return followAreaFragment;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_category2, container, false);
        mRecyclerView = (XRecyclerView) mRootView.findViewById(R.id.recyclerview);
        LinearLayoutManager mLayoutMgr = new LinearLayoutManager(getContext());
        mLayoutMgr.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(mLayoutMgr);
        mRecyclerView.setRefreshProgressStyle(ProgressStyle.SysProgress);
        mRecyclerView.setLaodingMoreProgressStyle(ProgressStyle.SysProgress);
        mRecyclerView.setArrowImageView(R.drawable.iconfont_downgrey);
        mRecyclerView.setLoadingMoreEnabled(false);
        mID = getArguments().getString("id");
        gameModel = RetrofitUtils.create(GameModel.class);
        programEntityList = new ArrayList<>();
        mAdapter = new FollowAreaAdapter(getActivity(),programEntityList);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                getData();
            }

            @Override
            public void onLoadMore() {
                getData();
            }
        });
        getData();
        return mRootView;
    }
    private void getData() {
        if("all".equals(mID)){
            if(UserUtils.isNotLogin()){
                //没有登录，
                List<GameInfoBean.GameInfoEntity> games = DBUtils.getInstance(getActivity()).getAllArea();
                //没有订阅
                if(games == null){
                    ProgramModel programModel = RetrofitUtils.create(ProgramModel.class);
                    programModel.getProgramList(CommConstants.COMMON_PAGE_COUNT,mCurrentPage,CommConstants.DEFAULT_SORT_ONLINE_PERSON,CommConstants.CAROUSEL_TYPE_VIDEO,new DefaultLiveHttpCallBack<ProgramBean>(){
                        @Override
                        public void success(ProgramBean programBean) {
                            if (isNotFinish()) {
                                mRecyclerView.refreshComplete();
                                if (programBean == null) {
                                    return;
                                }
                                if (NumUtils.getPage(programBean.cnt) == mCurrentPage) {
                                    mRecyclerView.setLoadingMoreEnabled(false);
                                } else if (NumUtils.getPage(programBean.cnt) > mCurrentPage) {
                                    mRecyclerView.setLoadingMoreEnabled(true);
                                    mCurrentPage++;
                                }
                                if(mCurrentPage == 1)
                                    programEntityList.clear();
                                if (programBean.programs != null && programBean.programs.size() > 0)
                                    programEntityList.addAll(programBean.programs);
                                mAdapter.notifyDataSetChanged();
                            }
                        }

                        @Override
                        public void failure(String message) {
                            if (isNotFinish()) {
                                mRecyclerView.refreshComplete();
                                ToastUtils.showToast(getActivity(), message);
                            }
                        }
                    });
                    return;
                }else{
                    //没有登录，订阅之后
                    GameInfoBean.GameInfoEntity game ;
                    StringBuilder sb = new StringBuilder();
                    for(int i = 0; i< games.size();i++){
                        game = games.get(i);
                        sb.append(game._id).append("#");
                    }
                    mID = sb.toString();
                    mID = mID.substring(0,mID.length() - 1);
                    gameModel.queryprogramlistbyareaidList(mID, CommConstants.COMMON_PAGE_COUNT, mCurrentPage, CommConstants.DEFAULT_SORT_ONLINE_PERSON, new DefaultLiveHttpCallBack<ProgramBean>() {
                        @Override
                        public void success(ProgramBean programBean) {
                            if (isNotFinish()) {
                                mRecyclerView.refreshComplete();
                                if (programBean == null) {
                                    return;
                                }
                                if (NumUtils.getPage(programBean.cnt) == mCurrentPage) {
                                    mRecyclerView.setLoadingMoreEnabled(false);
                                } else if (NumUtils.getPage(programBean.cnt) > mCurrentPage) {
                                    mRecyclerView.setLoadingMoreEnabled(true);
                                    mCurrentPage++;
                                }
                                if(mCurrentPage == 1)
                                    programEntityList.clear();
                                if (programBean.programs != null && programBean.programs.size() > 0)
                                    programEntityList.addAll(programBean.programs);
                                mAdapter.notifyDataSetChanged();
                            }
                        }

                        @Override
                        public void failure(String message) {
                            if (isNotFinish()) {
                                mRecyclerView.refreshComplete();
                                ToastUtils.showToast(getActivity(), message);
                            }
                        }
                    });
                    return;
                }
            }else{
                //登录之后，查询全部
                UserCenterModel userCenterModel = RetrofitUtils.create(UserCenterModel.class);
                userCenterModel.getAllVideo( CommConstants.COMMON_PAGE_COUNT, mCurrentPage, CommConstants.DEFAULT_SORT_ONLINE_PERSON, new DefaultLiveHttpCallBack<ProgramBean>() {
                    @Override
                    public void success(ProgramBean programBean) {
                        if(isNotFinish()){
                            mRecyclerView.refreshComplete();
                            if( programBean == null || programBean.programs == null)
                                return;
                            if (NumUtils.getPage(programBean.cnt) == mCurrentPage) {
                                mRecyclerView.setLoadingMoreEnabled(false);
                            } else if (NumUtils.getPage(programBean.cnt) > mCurrentPage) {
                                mRecyclerView.setLoadingMoreEnabled(true);
                                mCurrentPage++;
                            }
                            if(mCurrentPage == 1)
                                programEntityList.clear();
                            if (programBean.programs != null && programBean.programs.size() > 0)
                                programEntityList.addAll(programBean.programs);
                            mAdapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void failure(String message) {
                        if (isNotFinish()) {
                            mRecyclerView.refreshComplete();
                            ToastUtils.showToast(getActivity(), message);
                        }
                    }
                });
                return;
            }
        }
        //根据ID查询各个专区的内容
        gameModel.queryprogramlistbyareaid(mID, "all", CommConstants.COMMON_PAGE_COUNT, mCurrentPage, CommConstants.DEFAULT_SORT_ONLINE_PERSON, new DefaultLiveHttpCallBack<ProgramBean>() {
            @Override
            public void success(final ProgramBean programBean) {
                if (isNotFinish()) {
                    mRecyclerView.refreshComplete();
                    if (programBean == null) {
                        return;
                    }
                    if (NumUtils.getPage(programBean.cnt) == mCurrentPage) {
                        mRecyclerView.setLoadingMoreEnabled(false);
                    } else if (NumUtils.getPage(programBean.cnt) > mCurrentPage) {
                        mRecyclerView.setLoadingMoreEnabled(true);
                        mCurrentPage++;
                    }
                    if(mCurrentPage == 1)
                        programEntityList.clear();
                    if (programBean.programs != null && programBean.programs.size() > 0)
                        programEntityList.addAll(programBean.programs);
                    mAdapter.notifyDataSetChanged();
//                    mAdapter = new FollowAreaAdapter(getActivity(), programEntityList);
//                    mRecyclerView.setAdapter(mAdapter);
                }
            }

            @Override
            public void failure(String message) {
                if (isNotFinish()) {
                    mRecyclerView.refreshComplete();
                    ToastUtils.showToast(getActivity(), message);
                }
            }
        });

    }
}
