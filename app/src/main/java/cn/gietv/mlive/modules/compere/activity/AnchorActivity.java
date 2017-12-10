package cn.gietv.mlive.modules.compere.activity;

import android.content.Context;
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
import cn.gietv.mlive.modules.compere.adapter.CompereAdapter;
import cn.gietv.mlive.modules.compere.bean.AlbumBean;
import cn.gietv.mlive.modules.compere.model.CompereModel;
import cn.gietv.mlive.modules.usercenter.bean.UserCenterBean;
import cn.gietv.mlive.utils.HeadViewController;
import cn.gietv.mlive.utils.IntentUtils;
import cn.gietv.mlive.utils.NumUtils;
import cn.gietv.mlive.utils.ToastUtils;
import cn.kalading.android.retrofit.utils.RetrofitUtils;

/**
 * Created by houde on 2016/5/19.
 */
public class AnchorActivity extends AbsBaseActivity implements View.OnClickListener {
    public static final String EXTRA_USER_ID = "extra_user_id";
    public static final String EXTRA_USER_NAME = "extra_user_NAME";
    private XRecyclerView mListView;
    private CompereAdapter mAdapter;
    private List<Object> mObjectList;
    private CompereModel mCompereModel;

    private int mCurrentPage = 1;
    private String mUserId;
    private UserCenterBean.UserinfoEntity mUserinfo;
    public static void openAnchorActivity(Context context,String id,String nickname){
        Bundle bundle =new Bundle();
        bundle.putString("extra_user_id",id);
        bundle.putString("extra_user_id",nickname);
        IntentUtils.openActivity(context,AnchorActivity.class,bundle);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.compere_layout);
        mListView = (XRecyclerView) findViewById(R.id.user_center_lv_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mListView.setLayoutManager(layoutManager);

        mListView.setRefreshProgressStyle(ProgressStyle.SysProgress);
        mListView.setLaodingMoreProgressStyle(ProgressStyle.SysProgress);
        mListView.setArrowImageView(R.drawable.iconfont_downgrey);
        mListView.setLoadingMoreEnabled(false);
        mCompereModel = RetrofitUtils.create(CompereModel.class);
        mUserId = getIntent().getStringExtra(EXTRA_USER_ID);
        mObjectList = new ArrayList<>();
        mAdapter = new CompereAdapter(this, mObjectList);
        HeadViewController.initHeadWithoutSearch(AnchorActivity.this, getIntent().getStringExtra(EXTRA_USER_NAME));
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
    }

    @Override
    public void onClick(View v) {

    }
    public void onPullDownToRefresh() {
        mCurrentPage = 1;
        mCompereModel.getAlbumList(mUserId, CommConstants.COMMON_PAGE_COUNT, mCurrentPage, CommConstants.DEFAULT_SORT_ONLINE_PERSON, new DefaultLiveHttpCallBack<AlbumBean>() {
            @Override
            public void success(AlbumBean albumBean) {
                if (isNotFinish()) {
                    if (albumBean == null) {
                        ToastUtils.showToast(AnchorActivity.this, "用户不存在");
                        AnchorActivity.this.finish();
                        return;
                    }
                    mListView.refreshComplete();
                    if (NumUtils.getPage(albumBean.cnt) == mCurrentPage) {
                        mListView.setLoadingMoreEnabled(false);
                    } else if (albumBean.cnt > mCurrentPage) {
                        mListView.setLoadingMoreEnabled(true);
                        mCurrentPage++;
                    }
                    mObjectList.clear();
                    mObjectList.add(albumBean.authorinfo);
                    if (albumBean.albumlist != null) {
                        mObjectList.addAll(albumBean.albumlist);
                    }
                    mAdapter.notifyDataSetChanged();
                    mUserinfo = albumBean.authorinfo;
                }
            }

            @Override
            public void failure(String message) {
                ToastUtils.showToast(AnchorActivity.this, message);
            }
        });
    }

    public void onPullUpToRefresh() {
        mCompereModel.getAlbumList(mUserId, CommConstants.COMMON_PAGE_COUNT, mCurrentPage, CommConstants.DEFAULT_SORT_ONLINE_PERSON, new DefaultLiveHttpCallBack<AlbumBean>() {
            @Override
            public void success(AlbumBean albumBean) {
                if (isNotFinish()) {
                    mListView.loadMoreComplete();
                    if (NumUtils.getPage(albumBean.cnt) == mCurrentPage) {
                        mListView.setLoadingMoreEnabled(false);
                    } else if (albumBean.cnt > mCurrentPage) {
                        mListView.setLoadingMoreEnabled(true);
                        mCurrentPage++;
                    }
                    if (albumBean.albumlist != null) {
                        mObjectList.addAll(albumBean.albumlist);
                        mAdapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void failure(String message) {
                ToastUtils.showToast(AnchorActivity.this, message);
            }
        });
    }
}
