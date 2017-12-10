package cn.gietv.mlive.modules.vrgame.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;

import cn.gietv.mlive.R;
import cn.gietv.mlive.base.AbsBaseFragment;
import cn.gietv.mlive.constants.CommConstants;
import cn.gietv.mlive.http.DefaultLiveHttpCallBack;
import cn.gietv.mlive.modules.game.bean.GameInfoBean;
import cn.gietv.mlive.modules.game.model.GameModel;
import cn.gietv.mlive.modules.vrgame.adapter.VRGameAdapter;
import cn.gietv.mlive.utils.HeadViewController;
import cn.gietv.mlive.utils.ToastUtils;
import cn.kalading.android.retrofit.utils.RetrofitUtils;

/**
 * Created by houde on 2016/5/18.
 */
public class VRGameFragment extends AbsBaseFragment {
    private View mRootView;
    private RecyclerView mRecyclerView;
    private GameModel gameModel ;
    private int mCurrentPageGame = 1;
    private List<GameInfoBean.GameInfoEntity> gameList = new ArrayList<>();
    private VRGameAdapter mAdapter;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.vr_game_layout, null);
        HeadViewController.initVRSearchHead(mRootView,getActivity(),"VR新游精选");
        gameModel = RetrofitUtils.create(GameModel.class);
        mRecyclerView = (RecyclerView) mRootView.findViewById(R.id.recycler_view);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAdapter = new VRGameAdapter(getActivity(),gameList);
        mRecyclerView.setAdapter(mAdapter);
        getData();
        return mRootView;
    }

    public void getData() {
        gameModel.getGameList(CommConstants.GAME_TYPE_ALL, 99999, mCurrentPageGame, CommConstants.DEFAULT_SORT_ONLINE_PERSON, new DefaultLiveHttpCallBack<GameInfoBean>() {
            @Override
            public void success(GameInfoBean gameInfoBean) {
                if(isNotFinish()) {
                    gameList.clear();
                    if(gameInfoBean == null)
                        return;
                    gameList.addAll(gameInfoBean.games);
                    mAdapter.notifyDataSetChanged();
                }

            }

            @Override
            public void failure(String message) {
                if (isNotFinish()) {
                    ToastUtils.showToast(getActivity(), message);
                }
            }
        });
    }

    @Override
    public boolean onBack() {
        return super.onBack();
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("VR游戏页");
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("VR游戏页");
    }
}
