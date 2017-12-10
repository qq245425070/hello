package cn.gietv.mlive.modules.user.activity;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import cn.gietv.mlive.R;
import cn.gietv.mlive.base.AbsBaseActivity;
import cn.gietv.mlive.constants.CommConstants;
import cn.gietv.mlive.http.DefaultLiveHttpCallBack;
import cn.gietv.mlive.modules.compere.adapter.CompereListAdapter;
import cn.gietv.mlive.modules.follow.model.FollowModel;
import cn.gietv.mlive.modules.game.bean.GameInfoBean;
import cn.gietv.mlive.modules.user.bean.UserBean;
import cn.gietv.mlive.utils.CacheUtils;
import cn.gietv.mlive.utils.HeadViewController;
import cn.gietv.mlive.utils.IntentUtils;
import cn.gietv.mlive.utils.NumUtils;
import cn.gietv.mlive.utils.ToastUtils;
import cn.kalading.android.retrofit.utils.GsonUtils;
import cn.kalading.android.retrofit.utils.RetrofitUtils;

import static cn.gietv.mlive.modules.usercenter.bean.UserCenterBean.UserinfoEntity;

/**
 * author：steven
 * datetime：15/10/16 23:06
 */
public class UserListActivity extends AbsBaseActivity implements OnClickListener{
    public static int USER_MODEL_ATTENTION = 1;
    public static int USER_MODEL_BE_ATTENTION = 2;
    public static int USER_MODEL_GAME_ATTENTION = 3;
    public static String EXTRA_USER_MODEL = "extra_user_model";
    public static String EXTRA_USER_ID = "extra_user_id";
    public static String EXTRA_USER_NICKNAME = "extra_user_nickname";
    public static String EXTRA_USER_FOLLOWS = "extra_user_follows";
    public static String EXTRA_TYPE = "extra_type";
    public static int EXTRA_COME_IN_NEWS = 4;
    private int mType = 0;
    private String mUserId;
    private int mModel;
    private XRecyclerView mListView;
    private CompereListAdapter mAdapter;
    private List<UserinfoEntity> mUserList = new ArrayList<>();
    private int mCurrentPage = 1;
    private FollowModel mFollowModel;
    private String mNickname;
    private int mFollows;
    private LinearLayout mTitleParent;
    private TextView mFriendText,mGameText,mAreaText;
    private View mFriendLine,mGameLine,mAreaLine,mTempLine;
    private final int TYPE_USER = 4;
    private final int TYPE_ANCHOR = 8;
    private final int TYPE_AREA = 5;
    private int mCurrentType = 0 ;
    private List<GameInfoBean.GameInfoEntity> mGameList = new ArrayList<>();

    public static void openUserListActivity(int type,Activity activity, String userId, int model,String concern,String nickname,int follows) {
        Bundle bundle = new Bundle();
        bundle.putString(EXTRA_USER_ID, userId);
        bundle.putInt(EXTRA_USER_MODEL, model);
        bundle.putString(CompereListAdapter.CONCERN, concern);
        bundle.putString(EXTRA_USER_NICKNAME, nickname);
        bundle.putInt(EXTRA_USER_FOLLOWS, follows);
        bundle.putInt("resourceType",type);
        IntentUtils.openActivity(activity, UserListActivity.class, bundle);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.compere_list_layout);
        mListView = (XRecyclerView) findViewById(R.id.user_center_lv_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mListView.setLayoutManager(layoutManager);
        mListView.setRefreshProgressStyle(ProgressStyle.SysProgress);
        mListView.setLaodingMoreProgressStyle(ProgressStyle.SysProgress);
        mListView.setArrowImageView(R.drawable.iconfont_downgrey);
        mListView.setLoadingMoreEnabled(false);
        mUserId = getIntent().getExtras().getString(EXTRA_USER_ID);
        mModel = getIntent().getExtras().getInt(EXTRA_USER_MODEL);
        mNickname = getIntent().getExtras().getString(EXTRA_USER_NICKNAME);
        mFollows = getIntent().getExtras().getInt(EXTRA_USER_FOLLOWS);
        mType = getIntent().getExtras().getInt(EXTRA_TYPE);
        mAdapter = new CompereListAdapter(this, mUserList,getIntent().getExtras().getString(CompereListAdapter.CONCERN));
        mListView.setAdapter(mAdapter);
        mFollowModel = RetrofitUtils.create(FollowModel.class);
        mTitleParent = (LinearLayout) findViewById(R.id.title_parent);
        mFriendLine = findViewById(R.id.friend_line);
        mFriendText = (TextView) findViewById(R.id.friend_text);
        mGameLine = findViewById(R.id.game_line);
        mGameText = (TextView) findViewById(R.id.game_text);
        mAreaLine = findViewById(R.id.area_line);
        mAreaText = (TextView) findViewById(R.id.area_text);
        mCurrentType = getIntent().getIntExtra("type",4);
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
    }

    @Override
    protected void onResume() {
        super.onResume();
        getData();
    }

    private void handleListView(UserBean bean) {
        if (NumUtils.getPage(bean.cnt) == mCurrentPage) {
            mListView.setLoadingMoreEnabled(false);
        } else if (NumUtils.getPage(bean.cnt) > mCurrentPage) {
            mListView.setLoadingMoreEnabled(true);
            mCurrentPage++;
        }
    }

    private void handleLoadMore(UserBean bean) {
        if (bean.users != null) {
            mUserList.addAll(bean.users);
            mAdapter.notifyDataSetChanged();
        }
    }

    private void handleRefresh(UserBean bean) {
        if (bean.users != null) {
            mUserList.clear();
            if(bean.users != null && bean.users.size() == 0 && USER_MODEL_GAME_ATTENTION != mModel){
                String usersString = CacheUtils.getRecommendAnchor();
                if(usersString != null){
                    try {
                        mAdapter.setConcernStatus(CompereListAdapter.CONCERN);
                        if(mModel == USER_MODEL_BE_ATTENTION) {
                            HeadViewController.initHeadWithoutSearch(this, "推荐主播");
                        }else if(mModel == USER_MODEL_ATTENTION){
                            HeadViewController.initHeadWithoutSearch(UserListActivity.this, "让更多小伙伴知道你");
                        }
                        JSONArray jsonArray = new JSONArray(usersString);
                        UserinfoEntity userinfoEntity = null;
                        for (int i = 0;i<jsonArray.length();i++){
                            userinfoEntity = GsonUtils.getGson().fromJson(String.valueOf(jsonArray.getJSONObject(i)), UserinfoEntity.class);
                            bean.users.add(userinfoEntity);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
            mUserList.addAll(bean.users);
            mAdapter.notifyDataSetChanged();
        }
    }
    private void getData() {
        if(mCurrentType == TYPE_USER) {
            if (mModel == USER_MODEL_ATTENTION) {
                mFollowModel.getFollowUserByUserId(mUserId, CommConstants.COMMON_PAGE_COUNT, mCurrentPage, CommConstants.DEFAULT_SORT_ONLINE_PERSON, mRefreshCallBack);
            }
        }
    }

    public void onPullUpToRefresh() {
        if(mCurrentType == TYPE_USER) {
                mFollowModel.getFollowUserByUserId(mUserId, CommConstants.COMMON_PAGE_COUNT, mCurrentPage, CommConstants.DEFAULT_SORT_ONLINE_PERSON, mLoadMoreCalBack);
        }
    }

    private DefaultLiveHttpCallBack<UserBean> mRefreshCallBack = new DefaultLiveHttpCallBack<UserBean>() {
        @Override
        public void success(UserBean userBean) {
            if(mModel == USER_MODEL_BE_ATTENTION) {
                if(mType == EXTRA_COME_IN_NEWS){
                    HeadViewController.initHeadWithoutSearch(UserListActivity.this, mNickname + "关注了" + mFollows + "个人");
                    mAdapter.setConcernStatus(CompereListAdapter.CHAT);
                }else {
                    HeadViewController.initHeadWithoutSearch(UserListActivity.this, mNickname + "关注了" + mFollows + "个人");
                    mTitleParent.setVisibility(View.VISIBLE);
                    mFriendLine.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#42a6bd")));
                    mTempLine = mFriendLine;
                    mFriendText.setOnClickListener(UserListActivity.this);
                    mGameText.setOnClickListener(UserListActivity.this);
                    mAreaText.setOnClickListener(UserListActivity.this);
                    mListView.setAdapter(mAdapter);
                }
            } else if(mModel == USER_MODEL_ATTENTION ){
                HeadViewController.initHeadWithoutSearch(UserListActivity.this, "有" + mFollows + "个人关注了" + mNickname);
            }else if(USER_MODEL_GAME_ATTENTION == mModel){
                HeadViewController.initHeadWithoutSearch(UserListActivity.this, "有" + mFollows + "个人关注了" + mNickname);
            }else{
                HeadViewController.initSearchHead(UserListActivity.this,"");
            }
            if (isNotFinish()) {
                handleListView(userBean);
                handleRefresh(userBean);
            }
        }

        @Override
        public void failure(String message) {
            if (isNotFinish()) {
            }

        }
    };
    private DefaultLiveHttpCallBack<UserBean> mLoadMoreCalBack = new DefaultLiveHttpCallBack<UserBean>() {
        @Override
        public void success(UserBean userBean) {
            if (isNotFinish()) {
                handleListView(userBean);
                handleLoadMore(userBean);
            }

        }

        @Override
        public void failure(String message) {
            if (isNotFinish()) {
                ToastUtils.showToast(UserListActivity.this, message);
            }
        }
    };

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.friend_text) {
            updateUI(mFriendLine);
            mCurrentType = TYPE_USER;
            getData();
        }else if(v.getId() == R.id.game_text) {
            updateUI(mGameLine);
            mCurrentType = TYPE_ANCHOR;
            getData();
        }else  if(v.getId() == R.id.area_text){
            updateUI(mAreaLine);
            mCurrentType = TYPE_AREA;
            getData();
        }
    }

    private void updateUI(View view) {
        mTempLine.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#f0f0f0")));
        view.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#42a6bd")));
        mTempLine = view;

    }
}
