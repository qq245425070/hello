package cn.gietv.mlive.modules.compere.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;

import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import java.util.ArrayList;
import java.util.List;

import cn.gietv.mlive.R;
import cn.gietv.mlive.base.AbsBaseActivity;
import cn.gietv.mlive.constants.CommConstants;
import cn.gietv.mlive.http.DefaultLiveHttpCallBack;
import cn.gietv.mlive.modules.compere.adapter.CompereListAdapter;
import cn.gietv.mlive.modules.compere.bean.CompereListBean;
import cn.gietv.mlive.modules.compere.model.CompereModel;
import cn.gietv.mlive.modules.usercenter.bean.UserCenterBean;
import cn.gietv.mlive.utils.ConfigUtils;
import cn.gietv.mlive.utils.IntentUtils;
import cn.gietv.mlive.utils.NumUtils;
import cn.gietv.mlive.utils.ToastUtils;
import cn.kalading.android.retrofit.utils.RetrofitUtils;

/**
 * author：steven
 * datetime：15/10/16 17:55
 *
 */
public class CompereListActivity extends AbsBaseActivity {
    public static String EXTRA_USER_ID = "extra_user_id";
    private String mUserId;
    private XRecyclerView mListView;
    private CompereListAdapter mAdapter;
    private List<UserCenterBean.UserinfoEntity> mUserList = new ArrayList<>();
    private int mCurrentPage = 1;
    private CompereModel mCompereModel;

    public static void openCompereListActivity(Activity activity, String userId) {
        Bundle bundle = new Bundle();
        bundle.putString(EXTRA_USER_ID, userId);
        IntentUtils.openActivity(activity, CompereListActivity.class, bundle);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.compere_list_layout);
        mUserId = getIntent().getExtras().getString(EXTRA_USER_ID);
        mAdapter = new CompereListAdapter(this, mUserList,CompereListAdapter.CONCERN);
        mListView = (XRecyclerView) findViewById(R.id.user_center_lv_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mListView.setLayoutManager(layoutManager);
        mListView.setRefreshProgressStyle(ProgressStyle.SysProgress);
        mListView.setLaodingMoreProgressStyle(ProgressStyle.SysProgress);
        mListView.setArrowImageView(R.drawable.iconfont_downgrey);
        mListView.setLoadingMoreEnabled(false);
        mListView.setAdapter(mAdapter);
        mCompereModel = RetrofitUtils.create(CompereModel.class);
        getData();
    }

    private void getData() {
        mCompereModel.getAttentionCompere(mUserId, CommConstants.COMMON_PAGE_COUNT, mCurrentPage,ConfigUtils.QUERY_ATTENTION_USER, CommConstants.DEFAULT_SORT_ONLINE_PERSON, new DefaultLiveHttpCallBack<CompereListBean>() {
            @Override
            public void success(CompereListBean userCenterBean) {
                if (isNotFinish()) {
                    if (NumUtils.getPage(userCenterBean.cnt) == mCurrentPage) {
                        mListView.setLoadingMoreEnabled(false);
                    } else if (NumUtils.getPage(userCenterBean.cnt) > mCurrentPage) {
                        mListView.setLoadingMoreEnabled(false);
                        mCurrentPage++;
                    }
                    if (userCenterBean.users != null) {
                        mUserList.clear();
                        mUserList.addAll(userCenterBean.users);
                        mAdapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void failure(String message) {
                if (isNotFinish()) {
                    ToastUtils.showToast(CompereListActivity.this,message);
                }
            }
        });
    }

    public void onPullUpToRefresh() {
        mCompereModel.getAttentionCompere(mUserId, CommConstants.COMMON_PAGE_COUNT, mCurrentPage, ConfigUtils.QUERY_ATTENTION_USER,CommConstants.DEFAULT_SORT_ONLINE_PERSON, new DefaultLiveHttpCallBack<CompereListBean>() {
            @Override
            public void success(CompereListBean userCenterBean) {
                if (isNotFinish()) {
                    mListView.loadMoreComplete();
                    if (NumUtils.getPage(userCenterBean.cnt) == mCurrentPage) {
                        mListView.setLoadingMoreEnabled(false);
                    } else if (NumUtils.getPage(userCenterBean.cnt) > mCurrentPage) {
                        mListView.setLoadingMoreEnabled(true);
                        mCurrentPage++;
                    }
                    if (userCenterBean.users != null) {
                        mUserList.addAll(userCenterBean.users);
                        mAdapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void failure(String message) {
                if (isNotFinish()) {
                    ToastUtils.showToast(CompereListActivity.this, message);
                    mListView.loadMoreComplete();
                }
            }
        });
    }
}
