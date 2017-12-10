package cn.gietv.mlive.modules.game.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import java.util.ArrayList;
import java.util.List;

import cn.gietv.mlive.R;
import cn.gietv.mlive.base.AbsBaseActivity;
import cn.gietv.mlive.constants.CommConstants;
import cn.gietv.mlive.http.DefaultLiveHttpCallBack;
import cn.gietv.mlive.modules.game.adapter.GameListAdapter;
import cn.gietv.mlive.modules.game.bean.GameInfoBean;
import cn.gietv.mlive.modules.game.model.GameModel;
import cn.gietv.mlive.utils.HeadViewController;
import cn.gietv.mlive.utils.NumUtils;
import cn.gietv.mlive.utils.ToastUtils;
import cn.kalading.android.retrofit.utils.RetrofitUtils;

public class GameActivity extends AbsBaseActivity  implements View.OnClickListener{
    private GameModel mGameModel;
    private GameListAdapter mAdapter;
    private XRecyclerView mListView;
    private List<GameInfoBean.GameInfoEntity> mGameList = new ArrayList<>();
    private int mCurrentPage = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        HeadViewController.initHeadWithoutSearch(this,"游戏");
        mGameModel = RetrofitUtils.create(GameModel.class);
        mListView = (XRecyclerView)findViewById(R.id.game_list_lv_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mListView.setLayoutManager(layoutManager);

        mListView.setRefreshProgressStyle(ProgressStyle.SysProgress);
        mListView.setLaodingMoreProgressStyle(ProgressStyle.SysProgress);
        mListView.setArrowImageView(R.drawable.iconfont_downgrey);
        mAdapter = new GameListAdapter(this, mGameList);
        onPullDownToRefresh();
    }

    @Override
    public void onClick(View v) {

    }

    public void onPullDownToRefresh() {
        mGameModel.getGameList(CommConstants.GAME_TYPE_ALL, CommConstants.COMMON_PAGE_COUNT, mCurrentPage, CommConstants.DEFAULT_SORT_ONLINE_PERSON, new DefaultLiveHttpCallBack<GameInfoBean>() {
            @Override
            public void success(GameInfoBean gameInfoBean) {
                if (isNotFinish()) {
                    mListView.refreshComplete();
                    if (NumUtils.getPage(gameInfoBean.cnt) == mCurrentPage) {
                        mListView.setLoadingMoreEnabled(false);
                    } else if (NumUtils.getPage(gameInfoBean.cnt) > mCurrentPage) {
                        mListView.setLoadingMoreEnabled(true);
                        mCurrentPage++;
                    }
                    mGameList.clear();
                    mGameList.addAll(gameInfoBean.games);
                    mAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void failure(String message) {
                if (isNotFinish()) {
                    mListView.refreshComplete();
                    ToastUtils.showToast(GameActivity.this, message);
                }
            }
        });
    }

    public void onPullUpToRefresh() {
        mGameModel.getGameList(CommConstants.GAME_TYPE_ALL, CommConstants.COMMON_PAGE_COUNT, mCurrentPage, CommConstants.DEFAULT_SORT_ONLINE_PERSON, new DefaultLiveHttpCallBack<GameInfoBean>() {
            @Override
            public void success(GameInfoBean gameInfoBean) {
                if (isNotFinish()) {
                    if (NumUtils.getPage(gameInfoBean.cnt) == mCurrentPage) {
                        mListView.setLoadingMoreEnabled(false);
                    } else if (NumUtils.getPage(gameInfoBean.cnt) > mCurrentPage) {
                        mListView.setLoadingMoreEnabled(true);
                        mCurrentPage++;
                    }
                    mGameList.addAll(gameInfoBean.games);
                    mAdapter.notifyDataSetChanged();
                    mListView.loadMoreComplete();
                }
            }

            @Override
            public void failure(String message) {
                if (isNotFinish()) {
                    mListView.loadMoreComplete();
                    ToastUtils.showToast(GameActivity.this, message);
                }
            }
        });
    }
}
