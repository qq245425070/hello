package cn.gietv.mlive.modules.usercenter.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;

import cn.gietv.mlive.R;
import cn.gietv.mlive.base.AbsBaseFragment;
import cn.gietv.mlive.constants.CacheConstants;
import cn.gietv.mlive.constants.CommConstants;
import cn.gietv.mlive.db.DBUtils;
import cn.gietv.mlive.http.DefaultLiveHttpCallBack;
import cn.gietv.mlive.modules.login.bean.UserInfo;
import cn.gietv.mlive.modules.program.bean.ProgramBean;
import cn.gietv.mlive.modules.setting.activity.SettingActivity;
import cn.gietv.mlive.modules.usercenter.adapter.UserCenterAdapter;
import cn.gietv.mlive.modules.usercenter.bean.UserCenterBean;
import cn.gietv.mlive.modules.usercenter.model.UserCenterModel;
import cn.gietv.mlive.utils.CacheUtils;
import cn.gietv.mlive.utils.IntentUtils;
import cn.gietv.mlive.utils.ToastUtils;
import cn.kalading.android.retrofit.utils.RetrofitUtils;

/**
 * author：steven
 * datetime：15/10/8 13:56
 */
public class UserCenterFragment extends AbsBaseFragment{
    public static final String EXTRA_USER_ID = "extra_user_id";

    private View mCurrentView;
    private ListView mListView;
    private ImageView mSettingView;
    private TextView mTitleView;
    private UserCenterAdapter mAdapter;
    private List<Object> mObjectList;
    private UserCenterModel mUserCenterModel;
    private int mCurrentPage = 1;
    private String mUserId;
    private UserCenterBean.UserinfoEntity mUserinfo;
    private List<ProgramBean.ProgramEntity> mLiveProgramList;
    private List<ProgramBean.ProgramEntity> mVideoProgramList;
    public static UserCenterFragment getInstence(String userid) {
        UserCenterFragment fragment = new UserCenterFragment();
        Bundle bundle = new Bundle();
        bundle.putString(EXTRA_USER_ID, userid);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mCurrentView = inflater.inflate(R.layout.user_info_center_layout, container, false);
        mListView = (ListView) mCurrentView.findViewById(R.id.user_center_lv_list);
        mTitleView = (TextView) mCurrentView.findViewById(R.id.head_tv_title);
        mCurrentView.findViewById(R.id.head_ib_exit).setVisibility(View.INVISIBLE);
        mTitleView.setText("");
        mSettingView = (ImageView) mCurrentView.findViewById(R.id.head_ib_search);
        mSettingView.setImageResource(R.mipmap.setting_black);
        mUserCenterModel = RetrofitUtils.create(UserCenterModel.class);
        mUserId = getArguments().getString(EXTRA_USER_ID);
        mObjectList = new ArrayList<>();
        mAdapter = new UserCenterAdapter(getActivity(), mObjectList);
        mListView.setAdapter(mAdapter);
        mLiveProgramList = new ArrayList<>();
        mVideoProgramList = new ArrayList<>();
        mSettingView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentUtils.openActivity(getActivity(), SettingActivity.class);
            }
        });
        return mCurrentView;
    }

    public void initData() {
        if (CacheUtils.getCacheUserInfo() == null) {
            UserCenterBean.UserinfoEntity entity = new UserCenterBean.UserinfoEntity();
            mObjectList.clear();
            mObjectList.add(entity);
            mAdapter.notifyDataSetChanged();
        } else {
            onPullDownToRefresh();
        }
    }
    @Override
    public void onResume() {
        super.onResume();
        initData();
        MobclickAgent.onPageStart("个人中心");
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("个人中心");
    }

    public void onPullDownToRefresh() {
        mCurrentPage = 1;
        mUserCenterModel.getUserInfo(CacheUtils.getCache().getAsString(CacheConstants.CACHE_USERID), CommConstants.COMMON_PAGE_COUNT, mCurrentPage, CommConstants.DEFAULT_SORT_ONLINE_PERSON, new DefaultLiveHttpCallBack<UserCenterBean>() {
            @Override
            public void success(UserCenterBean userCenterBean) {
                if (isNotFinish()) {
                    if (userCenterBean != null) {
                        UserCenterBean.UserinfoEntity userinfoEntity = userCenterBean.userinfo;
                        CacheUtils.saveUserInfo(userinfoEntity);
                        UserInfo userInfo = new UserInfo();
                        userInfo.userinfo = userinfoEntity;
                        userInfo.token = CacheUtils.getCache().getAsString(CacheConstants.CACHE_TOKEN);
                        //向数据库中保存用户信息
                        DBUtils.getInstance(getActivity()).saveUser(userInfo);
                        mObjectList.clear();
                        if (userCenterBean != null) {
                            if (userCenterBean.userinfo != null) {
                                mObjectList.add(userCenterBean.userinfo);
                            }
                            if (userCenterBean.programs != null) {
                                ProgramBean.ProgramEntity programEntity ;
                                mLiveProgramList.clear();
                                mVideoProgramList.clear();
                                for(int i = 0; i< userCenterBean.programs.size(); i++){
                                    programEntity = userCenterBean.programs.get(i);
                                    if(programEntity.type == 1){//直播
                                        mLiveProgramList.add(programEntity);
                                    }else if(programEntity.type == 2){//收藏
                                        mVideoProgramList.add(programEntity);
                                    }
                                }
                                if(mLiveProgramList != null && mLiveProgramList.size() > 0)
                                    mObjectList.addAll(mLiveProgramList);
                                mAdapter.setData(mVideoProgramList);
                            }
                        }
                       // mEmptyLayout.
                        mAdapter.notifyDataSetChanged();
                        mUserinfo = userCenterBean.userinfo;
                    }
                }
            }

            @Override
            public void failure(String message) {
                if (isNotFinish())
                 ToastUtils.showToast(getActivity(),message);
            }
        });
    }
}
