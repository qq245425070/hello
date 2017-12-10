package cn.gietv.mlive.modules.program.fragment;

import cn.gietv.mlive.base.AbsBaseFragment;

/**
 * Created by houde on 2016/5/17.
 */
public class VRRecommendFragment extends AbsBaseFragment {
   /* private View mCurrentView;
    private View mHeaderView;
    private ViewPager mViewPager;
    private RecyclerView mHListView;
    private ListView mListView;
    private CirclePageIndicator mIndicator;
    private RecommendModel mModel;

    private RecommendCompereAdapter mCompereAdapter;
    private RecommendPagerAdapter mPagerAdapter;
    private VRRecommendListAdapter mListAdapter;

    private int mCurrentPage = 0;

    private Timer mTimer;
    private Handler mHandler = new Handler();
    private boolean mHasLoadData = false;

    private void initViews() {
        mHeaderView = LayoutInflater.from(getActivity()).inflate(cn.gietv.mlive.R.layout.recommend_head_layout, null);
        mViewPager = (ViewPager) mHeaderView.findViewById(cn.gietv.mlive.R.id.recommend_vp_page);
        mHListView = (RecyclerView) mHeaderView.findViewById(cn.gietv.mlive.R.id.recommend_lv_list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mHListView.setLayoutManager(linearLayoutManager);
        final ViewTreeObserver observer = mHListView.getViewTreeObserver();
        observer.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                int width = mViewPager.getWidth();
                int viewPagerHeight = (int) (width / 1.7777778);
                FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(width, viewPagerHeight);
                mViewPager.setLayoutParams(params);
                mHListView.getViewTreeObserver().removeOnPreDrawListener(this);
                return true;
            }
        });
        mIndicator = (CirclePageIndicator) mHeaderView.findViewById(cn.gietv.mlive.R.id.indicator);
        mListView = (ListView) mCurrentView.findViewById(cn.gietv.mlive.R.id.recommend_lv_list);
        mListView.addHeaderView(mHeaderView);
    }
    private void getData() {
        String userid = CacheUtils.getCache().getAsString(CacheConstants.CACHE_USERID);
        String token = CacheUtils.getCache().getAsString(CacheConstants.CACHE_TOKEN);
        mModel.getRecommendData(userid, token, new DefaultLiveHttpCallBack<RecommendBean>() {
            @Override
            public void success(RecommendBean recommendBean) {
                if (isNotFinish()) {
                    System.out.println(recommendBean);
                    loadDataSuccess(recommendBean);
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

    public boolean onBack() {
        if (mCompereInfoFragment != null) {
            FragmentManager fm = getChildFragmentManager();
            android.support.v4.app.FragmentTransaction ft = fm.beginTransaction();
            ft.remove(mCompereInfoFragment);
            ft.commit();
            mCompereInfoFragment = null;
            return false;
        }
        if (mGameInfoFragment != null) {
            FragmentManager fm = getChildFragmentManager();
            android.support.v4.app.FragmentTransaction ft = fm.beginTransaction();
            ft.remove(mGameInfoFragment);
            ft.commit();
            mGameInfoFragment = null;
            return false;
        }
        return true;
    }

    private CompereInfoFragment mCompereInfoFragment;
    private GameInfoFragment mGameInfoFragment;

    private void loadDataSuccess(RecommendBean recommendBean) {
        if (isNotFinish()) {
            mPagerAdapter = new RecommendPagerAdapter(getFragmentManager(), recommendBean.carousels);
            mViewPager.setAdapter(mPagerAdapter);
            mIndicator.setViewPager(mViewPager);
            List<UserCenterBean.UserinfoEntity> users = new ArrayList<>();
            if(recommendBean.users != null && recommendBean.users.size() > 0) {
                mCompereAdapter = new RecommendCompereAdapter(getActivity(), recommendBean.users);
                for (int i = 0; i < recommendBean.users.size(); i++) {
                    users.add(recommendBean.users.get(i).userinfo);
                }
                Gson gson = GsonUtils.getGson();
                String usersString = gson.toJson(users);
                CacheUtils.saveRecommendAnchor(usersString);
                mCompereAdapter.setOnItemClick(new OnItemClick<UserCenterBean.UserinfoEntity>() {
                    @Override
                    public void onClick(UserCenterBean.UserinfoEntity userinfoEntity) {
                        FragmentManager fm = getChildFragmentManager();
                        android.support.v4.app.FragmentTransaction ft = fm.beginTransaction();
                        ft.setCustomAnimations(R.anim.activity_open,R.anim.activity_close,R.anim.activity_open,R.anim.activity_close);
                        mCompereInfoFragment = CompereInfoFragment.getInstence(userinfoEntity._id);
                        mCompereInfoFragment.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                onBack();
                            }
                        });
                        ft.add(cn.gietv.mlive.R.id.fragment, mCompereInfoFragment);
                        ft.addToBackStack(null);
                        ft.commit();
                    }
                });
                ViewGroup.LayoutParams layoutParams = mHListView.getLayoutParams();
                layoutParams.height = DensityUtil.dip2px(getActivity(), 130);
                mHListView.setLayoutParams(layoutParams);
                mHListView.setAdapter(mCompereAdapter);
            }else{
                mHeaderView.findViewById(R.id.anchor_parent).setVisibility(View.GONE);
                mHListView.setVisibility(View.GONE);
            }
            List<Object> list = new ArrayList<>();
            if(recommendBean.livehot != null && recommendBean.livehot.size() != 0){
                list.add(recommendBean.livehot);
            }
            if (recommendBean.programsbygameid != null && recommendBean.programsbygameid.size() != 0) {
                list.addAll(recommendBean.programsbygameid);
            }
            if (recommendBean.programs != null && recommendBean.programs.size() != 0) {
                list.addAll(recommendBean.programs);
            }
            mListAdapter = new VRRecommendListAdapter(getActivity(), list);
            mListAdapter.setMoreClick(new OnItemClick<GameInfoBean.GameInfoEntity>() {
                @Override
                public void onClick(GameInfoBean.GameInfoEntity gameInfoEntity) {
                    GameInfoActivity.openGameInfoActivity(getActivity(), gameInfoEntity._id, gameInfoEntity.name, gameInfoEntity.type);
                }
            });
            mListView.setAdapter(mListAdapter);
            mHasLoadData = true;
            initCirculationPage();
        }
    }

    private void initCirculationPage() {
        if (!mHasLoadData) {
            return;
        }
        if (mTimer != null) {
            mTimer.cancel();
        }
        mTimer = new Timer();
        mTimer.schedule(new CirculationTimerTask(), 0, 5000);
    }

    @Override
    public void onResume() {
        super.onResume();
        initCirculationPage();
        if(mListAdapter != null){
            mListAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mTimer != null) {
            mTimer.cancel();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mCurrentView = inflater.inflate(cn.gietv.mlive.R.layout.recommend_vr_layout, container, false);
        initViews();
        mModel = RetrofitUtils.create(RecommendModel.class);
        getData();
        return mCurrentView;
    }
    private class CirculationTimerTask extends TimerTask {
        @Override
        public void run() {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    mViewPager.setCurrentItem(mCurrentPage);
                    if (mCurrentPage < mPagerAdapter.getCount() - 1) {
                        mCurrentPage++;
                    } else {
                        mCurrentPage = 0;
                    }
                }
            });
        }
    }*/
}
