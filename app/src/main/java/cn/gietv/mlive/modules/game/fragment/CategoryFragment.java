package cn.gietv.mlive.modules.game.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
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
import cn.gietv.mlive.modules.game.adapter.GameListAdapter;
import cn.gietv.mlive.modules.game.bean.GameInfoBean;
import cn.gietv.mlive.modules.game.model.GameModel;
import cn.gietv.mlive.utils.NumUtils;
import cn.gietv.mlive.utils.ToastUtils;
import cn.kalading.android.retrofit.utils.RetrofitUtils;

/**
 * Created by houde on 2016/5/16.
 */
public class CategoryFragment extends AbsBaseFragment{
    private XRecyclerView mListView;
    private View mCurrentView;
    private GameModel mGameModel;
    private GameListAdapter mAdapter;
    private int mCurrentPageGame = 1;
    private String type ;
    private List<GameInfoBean.GameInfoEntity> mGameList = new ArrayList<>();
    public static CategoryFragment getCategoryFragment(String type){
        CategoryFragment fragment = new CategoryFragment();
        Bundle bundle = new Bundle();
        bundle.putString("type", type);
        fragment.setArguments(bundle);
        return fragment;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mCurrentView = inflater.inflate(R.layout.fragment_category2, null);
        Bundle bundle = getArguments();
        if(bundle == null){
            type = "game";
        }else{
            type =bundle.getString("type");
        }

        mListView = (XRecyclerView) mCurrentView.findViewById(R.id.recyclerview);
        GridLayoutManager layoutManager1 = new GridLayoutManager(getContext(),3);
        mListView.setLayoutManager(layoutManager1);
        mListView.setRefreshProgressStyle(ProgressStyle.SysProgress);
        mListView.setLaodingMoreProgressStyle(ProgressStyle.SysProgress);
        mListView.setArrowImageView(R.drawable.iconfont_downgrey);
        mGameModel = RetrofitUtils.create(GameModel.class);
        mAdapter = new GameListAdapter(getActivity(),mGameList);
        mListView.setAdapter(mAdapter);
        onPullDownToRefresh();
        mListView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                onPullDownToRefresh();
            }

            @Override
            public void onLoadMore() {
                onPullUpToRefresh();
            }
        });
        return mCurrentView;
    }

    public void onPullDownToRefresh() {
        mGameModel.getAlbums(type, CommConstants.COMMON_PAGE_COUNT, mCurrentPageGame, CommConstants.DEFAULT_SORT_ONLINE_PERSON, new DefaultLiveHttpCallBack<GameInfoBean>() {
            @Override
            public void success(GameInfoBean infoBean) {
                if (isNotFinish()) {
                    mListView.refreshComplete();
                    if(infoBean == null)
                        return;
                    if (NumUtils.getPage(infoBean.cnt) == mCurrentPageGame) {
                        mListView.setLoadingMoreEnabled(false);
                    } else if (NumUtils.getPage(infoBean.cnt) > mCurrentPageGame) {
                        mListView.setLoadingMoreEnabled(true);
                        mCurrentPageGame++;
                    }
                    mGameList.clear();
                    mGameList.addAll(infoBean.games);
                    mAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void failure(String message) {
                if (isNotFinish()) {
                    mListView.refreshComplete();
                    ToastUtils.showToast(getActivity(), message);
                }
            }
        });
    }

    public void onPullUpToRefresh() {
        mGameModel.getAlbums(type, CommConstants.COMMON_PAGE_COUNT, mCurrentPageGame, CommConstants.DEFAULT_SORT_ONLINE_PERSON, new DefaultLiveHttpCallBack<GameInfoBean>() {
            @Override
            public void success(GameInfoBean infoBean) {
                if (isNotFinish()) {
                    mListView.loadMoreComplete();
                    if(infoBean == null)
                        return;
                    if (NumUtils.getPage(infoBean.cnt) == mCurrentPageGame) {
                        mListView.setLoadingMoreEnabled(false);
                    } else if (NumUtils.getPage(infoBean.cnt) > mCurrentPageGame) {
                        mListView.setLoadingMoreEnabled(true);
                        mCurrentPageGame++;
                    }
                    mGameList.addAll(infoBean.games);
                    mAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void failure(String message) {
                if (isNotFinish()) {
                    mListView.loadMoreComplete();
                    ToastUtils.showToast(getActivity(), message);
                }
            }
        });
    }
}
