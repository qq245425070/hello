package cn.gietv.mlive.modules.game.fragment;

import cn.gietv.mlive.base.AbsBaseFragment;

public class GameListFragment2 extends AbsBaseFragment{
//    private final int GAME_DATA = 0;
//    private final int AREA_DATA = 1;
//    private final int ANCHOR_DATA = 2;
//    private int mCurrenData ;
//    private View mCurrentView;
//    private PullToRefreshListView mListView;
//    private GameModel mGameModel;
//    private CompereListAdapter mAnchorAdapter;
//    private int mCurrentPageGame = 1;
//    private int mCurrentPageArea = 1;
//    private int mCurrentPageAnchor = 1;
//    private GameListAdapter mAdapter;
//    private List<GameInfoBean.GameInfoEntity> mGameList = new ArrayList<>();
//    private List<GameInfoBean.GameInfoEntity> mAreaList ;
//    private List<UserCenterBean.UserinfoEntity> mAnchorList;
//    private GameInfoFragment fragment;
//
//    @Override
//    public boolean onBack() {
//        if (fragment != null) {
//            FragmentManager fm = getChildFragmentManager();
//            FragmentTransaction t = fm.beginTransaction();
//            t.remove(fragment);
//            t.commit();
//            fragment = null;
//            return false;
//        }
//        return true;
//    }
//
//    @Nullable
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        mCurrentView = inflater.inflate(R.layout.game_list_layout, container, false);
//        HeadViewController.initSearchHeadWithoutReturn(mCurrentView, getActivity(), "分类");
//        mGameModel = RetrofitUtils.create(GameModel.class);
//        mListView = (PullToRefreshListView) mCurrentView.findViewById(R.id.game_list_lv_list);
//        mListView.setMode(PullToRefreshBase.Mode.DISABLED);
//        mListView.setOnRefreshListener(this);
//        mAdapter = new GameListAdapter(getActivity(), mGameList);
//        mListView.setAdapter(mAdapter);
//        mListView.setRefreshing(true);
//        mListView.getRefreshableView().setFooterDividersEnabled(true);
//        mListView.getRefreshableView().setDivider(null);
//        onPullDownToRefresh(mListView);
//        mAdapter.setOnItemClick(new OnItemClick<GameInfoBean.GameInfoEntity>() {
//            @Override
//            public void onClick(GameInfoBean.GameInfoEntity gameInfoEntity) {
//                FragmentManager fm = getChildFragmentManager();
//                FragmentTransaction t = fm.beginTransaction();
//                t.setCustomAnimations(R.anim.fragment_slide_right_in,R.anim.fragment_slide_left_out);
//                fragment = GameInfoFragment.getInstence(gameInfoEntity._id, gameInfoEntity.name, gameInfoEntity.type);
//                fragment.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        onBack();
//                    }
//                });
//                t.add(R.id.fragment, fragment);
//                t.commit();
//            }
//        });
//        return mCurrentView;
//    }
//
//    @Override
//    public void onPullDownToRefresh(PullToRefreshBase refreshView) {
//        mGameModel.getGameList(CommConstants.GAME_TYPE_ALL, CommConstants.COMMON_PAGE_COUNT, mCurrentPageGame, CommConstants.DEFAULT_SORT_ONLINE_PERSON, new DefaultLiveHttpCallBack<GameInfoBean>() {
//            @Override
//            public void success(GameInfoBean gameInfoBean) {
//                if (isNotFinish()) {
//                    if (NumUtils.getPage(gameInfoBean.cnt) == mCurrentPageGame) {
//                        mListView.setMode(PullToRefreshBase.Mode.DISABLED);
//                    } else if (NumUtils.getPage(gameInfoBean.cnt) > mCurrentPageGame) {
//                        mListView.setMode(PullToRefreshBase.Mode.PULL_FROM_END);
//                        mCurrentPageGame++;
//                    }
//                    mGameList.clear();
//                    mGameList.addAll(gameInfoBean.games);
//                    mAdapter.notifyDataSetChanged();
//                    mListView.onRefreshComplete();
//                }
//            }
//
//            @Override
//            public void failure(String message) {
//                if (isNotFinish()) {
//                    mListView.onRefreshComplete();
//                    ToastUtils.showToast(getActivity(), message);
//                }
//            }
//        });
//    }
//
//    @Override
//    public void onPullUpToRefresh(PullToRefreshBase refreshView) {
//        switch (mCurrenData) {
//            case GAME_DATA:
//            mGameModel.getGameList(CommConstants.GAME_TYPE_ALL, CommConstants.COMMON_PAGE_COUNT, mCurrentPageGame, CommConstants.DEFAULT_SORT_ONLINE_PERSON, new DefaultLiveHttpCallBack<GameInfoBean>() {
//                @Override
//                public void success(GameInfoBean gameInfoBean) {
//                    if (isNotFinish()) {
//                        if (NumUtils.getPage(gameInfoBean.cnt) == mCurrentPageGame) {
//                            mListView.setMode(PullToRefreshBase.Mode.DISABLED);
//                        } else if (NumUtils.getPage(gameInfoBean.cnt) > mCurrentPageGame) {
//                            mListView.setMode(PullToRefreshBase.Mode.PULL_FROM_END);
//                            mCurrentPageGame++;
//                        }
//                        mGameList.addAll(gameInfoBean.games);
//                        mAdapter.notifyDataSetChanged();
//                        mListView.onRefreshComplete();
//                    }
//                }
//
//                @Override
//                public void failure(String message) {
//                    if (isNotFinish()) {
//                        mListView.onRefreshComplete();
//                        ToastUtils.showToast(getActivity(), message);
//                    }
//                }
//            });
//                break;
//            case AREA_DATA:
//                getAreaList();
//                break;
//            case ANCHOR_DATA:
//                getAnchorList();
//                break;
//        }
//    }
//
//    @Override
//    public void onClick(View v) {
//        switch (v.getId()){
//            case R.id.game_text:
//                mCurrenData = GAME_DATA;
//                if(mGameList.size() == 0){
//                    onPullDownToRefresh(mListView);
//                }else{
//                    mAdapter = new GameListAdapter(getActivity(),mGameList);
//                    mListView.setAdapter(mAdapter);
//                }
//                break;
//            case R.id.area_text:
//                mCurrenData = AREA_DATA;
//                if(mAreaList == null){
//                    getAreaList();
//                }else{
//                    mAdapter = new GameListAdapter(getActivity(),mAreaList);
//                    mListView.setAdapter(mAdapter);
//                }
//                break;
////            case R.id.anchor_text:
////                mCurrenData = ANCHOR_DATA;
////                if(mAnchorList == null){
////                    getAnchorList();
////                }else {
////                    mListView.setAdapter(mAnchorAdapter);
////                }
////                break;
//        }
//    }
//
//    private void getAnchorList() {
//        mGameModel.getAnchorList(CommConstants.COMMON_PAGE_COUNT, mCurrentPageAnchor, CommConstants.DEFAULT_SORT_ONLINE_PERSON, new DefaultLiveHttpCallBack<UserBean>() {
//            @Override
//            public void success(UserBean userBean) {
//                mAnchorList = userBean.users;
//                String usersString = CacheUtils.getRecommendAnchor();
//                JSONArray jsonArray = null;
//                if(mCurrentPageAnchor == 1){
//                    try {
//                        jsonArray = new JSONArray(usersString);
//                        UserCenterBean.UserinfoEntity userinfoEntity = null;
//                        for (int i = jsonArray.length()-1;i >=0 ;i--){
//                            userinfoEntity = GsonUtils.getGson().fromJson(String.valueOf(jsonArray.getJSONObject(i)), UserCenterBean.UserinfoEntity.class);
//                            mAnchorList.add(0,userinfoEntity);
//                        }
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                }
//                mAnchorAdapter = new CompereListAdapter(getActivity(),mAnchorList,CompereListAdapter.CONCERN);
//                mListView.setAdapter(mAnchorAdapter);
//            }
//
//            @Override
//            public void failure(String message) {
//
//            }
//        });
//    }
//
//    private void getAreaList() {
//        mGameModel.getAreaList(CommConstants.GAME_TYPE_ALL, CommConstants.COMMON_PAGE_COUNT, mCurrentPageAnchor, CommConstants.DEFAULT_SORT_ONLINE_PERSON, new DefaultLiveHttpCallBack<GameInfoBean>() {
//            @Override
//            public void success(GameInfoBean gameInfoBean) {
//                mAreaList = gameInfoBean.games;
//                mAdapter = new GameListAdapter(getActivity(),mAreaList);
//                mListView.setAdapter(mAdapter);
//            }
//
//            @Override
//            public void failure(String message) {
//
//            }
//        });
//    }

}
