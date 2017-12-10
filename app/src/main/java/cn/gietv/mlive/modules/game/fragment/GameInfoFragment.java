package cn.gietv.mlive.modules.game.fragment;

/**
 * author：steven
 * datetime：15/10/8 15:31
 */
public class GameInfoFragment {//extends AbsBaseFragment implements PullToRefreshBase.OnRefreshListener2 {
//    public static String EXTRA_TITLE = "extra_title";
//    public static String EXTRA_GAME_ID = "extra_game_id";
//    public static String EXTRA_TYPE = "type";
//    private View mCurrentView;
//    private PullToRefreshListView mListView;
//    private GameInfoAdapter mAdapter;
//    private List<Object> mListObject = new ArrayList<>();
//    private String mTitle;
//    private String mGameId;
//    private int mCurrentPage = 1;
//    private int mCurrentAnchorPage = 1;
//    private GameModel mGameModel;
//    private View.OnClickListener mOnClickListener;
//    private int mFirstItemHeight ;
//    private int type;
//    public static GameInfoFragment getInstence(String gameId, String title,int type) {
//        GameInfoFragment fragment = new GameInfoFragment();
//        Bundle bundle = new Bundle();
//        bundle.putString(EXTRA_TITLE, title);
//        bundle.putString(EXTRA_GAME_ID, gameId);
//        bundle.putInt(EXTRA_TYPE, type);
//        fragment.setArguments(bundle);
//        return fragment;
//    }
//
//    private void initView() {
//        mGameId = getArguments().getString(EXTRA_GAME_ID);
//        mTitle = getArguments().getString(EXTRA_TITLE);
////        mListView = (PullToRefreshListView) mCurrentView.findViewById(cn.gietv.mlive.R.id.game_info_lv_list);
//        HeadViewController.initHeadWithoutSearch(mCurrentView, getActivity(), "");
//        mAdapter = new GameInfoAdapter(getActivity(), mListObject);
//        mListView.setAdapter(mAdapter);
//        mListView.setOnScrollListener(new AbsListView.OnScrollListener() {
//            @Override
//            public void onScrollStateChanged(AbsListView view, int scrollState) {
//
//            }
//
//            @Override
//            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
//                if (firstVisibleItem == 1) {
//                    int first_child_item_height = getScrollY(view);
//                    float r =  (float)first_child_item_height / (float)mFirstItemHeight * 2;
//                    System.out.println(r);
//                    int color = 255 - (int)(255 * r);
//                    if (mFirstItemHeight / 2 < first_child_item_height) {
//                        HeadViewController.initGameHeadWithoutSearch(mCurrentView, getActivity(), mTitle);
//                    } else {
//                        HeadViewController.initGameHeadWithoutSearch(mCurrentView, getActivity(), mTitle,Color.rgb(color,color,color));
//                    }
//                } else if (firstVisibleItem == 0) {
//                    HeadViewController.initGameHeadWithoutSearch(mCurrentView, getActivity(), "");
//                } else {
//                    HeadViewController.initGameHeadWithoutSearch(mCurrentView, getActivity(), mTitle);
//                }
//            }
//        });
//        mGameModel = RetrofitUtils.create(GameModel.class);
//        mListView.setOnRefreshListener(this);
//        mListView.setMode(PullToRefreshBase.Mode.DISABLED);
//        if (mOnClickListener != null) {
//            mCurrentView.findViewById(cn.gietv.mlive.R.id.head_ib_exit).setOnClickListener(mOnClickListener);
//        }
//        mAdapter.setAlbumOnClickListener(new GameInfoAdapter.IAlbumOnClickListener() {
//            @Override
//            public void getAlbumList(int type) {
//                GameInfoFragment.this.type = type;
//                onPullDownToRefresh(mListView);
//            }
//        });
//        mAdapter.setAnchorOnclickListener(new GameInfoAdapter.IAnchorOnclickListener() {
//            @Override
//            public void getAnchorList(int type) {
//                GameInfoFragment.this.type = type;
//                onPullDownToRefresh(mListView);
//            }
//        });
//    }
//    private int getItemHeight(){
//        int result = DensityUtil.dip2px(getActivity(), 600);;
//        if(mAdapter.getCount() > 0) {
//            View listItem = mAdapter.getView(0, null, mListView);
//            listItem.measure(0, 0);
//            result = listItem.getMeasuredHeight();
//        }
//        return result;
//    }
//    public int getScrollY(AbsListView view) {
//        View c = view.getChildAt(0);
//        if (c == null ) {
//            return 0;
//        }
//        int top = c.getTop();
//        System.out.println(top);
//        return -top;
//    }
//
//    public void setOnClickListener(View.OnClickListener onClickListener) {
//        mOnClickListener = onClickListener;
//    }
//
//    @Nullable
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        mCurrentView = inflater.inflate(cn.gietv.mlive.R.layout.game_info_layout, container, false);
//        initView();
//        mListView.setRefreshing(true);
//        onPullDownToRefresh(mListView);
//        return mCurrentView;
//    }
//
//private GameInfoBean.GameInfoEntity game;
//    @Override
//    public void onPullDownToRefresh(PullToRefreshBase refreshView) {
//        if(type == 0) {//加载专辑
//            mCurrentPage = 1;
//            mGameModel.getAlbumList(mGameId,"all", CommConstants.COMMON_PAGE_COUNT, mCurrentPage, CommConstants.DEFAULT_SORT_ONLINE_PERSON, new DefaultLiveHttpCallBack<AlbumBean>() {
//                @Override
//                public void success(AlbumBean albumBean) {
//                    if (isNotFinish()) {
//                        mListView.onRefreshComplete();
//                            if (NumUtils.getPage(albumBean.cnt) == mCurrentPage) {
//                                mListView.setMode(PullToRefreshBase.Mode.DISABLED);
//                            } else if (albumBean.cnt > mCurrentPage) {
//                                mListView.setMode(PullToRefreshBase.Mode.PULL_FROM_END);
//                                mCurrentPage++;
//                            }
//                            mListObject.clear();
//                            game = albumBean.areainfo;
//                            mListObject.add(game);
//                            if (albumBean.albumlist != null) {
//                                mListObject.addAll(albumBean.albumlist);
//                            }
//                            mAdapter.notifyDataSetChanged();
//                            mListView.onRefreshComplete();
//                            mFirstItemHeight = getItemHeight();
//                        }
//                }
//
//                @Override
//                public void failure(String message) {
//                    mListView.onRefreshComplete();
//                    if (isNotFinish()) {
//                        ToastUtils.showToast(getActivity(), message);
//                    }
//                }
//            });
//        }else if(type == 1){//加载作者
//            mGameModel.getAnchors(mGameId,"all", CommConstants.COMMON_PAGE_COUNT, mCurrentAnchorPage, CommConstants.DEFAULT_SORT_ONLINE_PERSON, new DefaultLiveHttpCallBack<AuthorBean>() {
//                @Override
//                public void success(AuthorBean authorBean) {
//                    if (isNotFinish()) {
//                        mListView.onRefreshComplete();
//                        if (NumUtils.getPage(authorBean.cnt) == mCurrentPage) {
//                            mListView.setMode(PullToRefreshBase.Mode.DISABLED);
//                        } else if (authorBean.cnt > mCurrentPage) {
//                            mListView.setMode(PullToRefreshBase.Mode.PULL_FROM_END);
//                            mCurrentPage++;
//                        }
//                        mListObject.clear();
//                        mListObject.add(game);
//                        if (authorBean.userinfolist != null) {
//                            mListObject.addAll(authorBean.userinfolist);
//                        }
//                        mAdapter.notifyDataSetChanged();
//                        mFirstItemHeight = getItemHeight();
//                    }
//                }
//
//                @Override
//                public void failure(String message) {
//
//                }
//            });
//        }
//    }
//
//
//    @Override
//    public void onPullUpToRefresh(PullToRefreshBase refreshView) {
//        if(type == 0) {
//            mGameModel.getGameInfo(mGameId, CommConstants.COMMON_PAGE_COUNT, mCurrentPage, CommConstants.DEFAULT_SORT_ONLINE_PERSON, new DefaultLiveHttpCallBack<GameProgramBean>() {
//                @Override
//                public void success(GameProgramBean gameProgramBean) {
//                    mListView.onRefreshComplete();
//                    if (NumUtils.getPage(gameProgramBean.cnt) == mCurrentPage) {
//                        mListView.setMode(PullToRefreshBase.Mode.DISABLED);
//                    } else if (gameProgramBean.cnt > mCurrentPage) {
//                        mListView.setMode(PullToRefreshBase.Mode.PULL_FROM_END);
//                        mCurrentPage++;
//                    }
//                    if (gameProgramBean.programs != null) {
//                        mListObject.addAll(gameProgramBean.programs);
//                    }
//                    mAdapter.notifyDataSetChanged();
//                }
//
//                @Override
//                public void failure(String message) {
//                    ToastUtils.showToast(getActivity(), message);
//                }
//            });
//        }else if(type == 1){
//            mGameModel.getAnchors(mGameId,"all", CommConstants.COMMON_PAGE_COUNT, mCurrentAnchorPage, CommConstants.DEFAULT_SORT_ONLINE_PERSON, new DefaultLiveHttpCallBack<AuthorBean>() {
//                @Override
//                public void success(AuthorBean authorBean) {
//                    if (isNotFinish()) {
//                        mListView.onRefreshComplete();
//                        if (NumUtils.getPage(authorBean.cnt) == mCurrentPage) {
//                            mListView.setMode(PullToRefreshBase.Mode.DISABLED);
//                        } else if (authorBean.cnt > mCurrentPage) {
//                            mListView.setMode(PullToRefreshBase.Mode.PULL_FROM_END);
//                            mCurrentPage++;
//                        }
////                        mListObject.add(0,game);
//                        if (authorBean.userinfolist != null) {
//                            mListObject.addAll(authorBean.userinfolist);
//                        }
//                        mAdapter.notifyDataSetChanged();
//                        mFirstItemHeight = getItemHeight();
//                    }
//                }
//
//                @Override
//                public void failure(String message) {
//
//                }
//            });
//        }
//    }
}
