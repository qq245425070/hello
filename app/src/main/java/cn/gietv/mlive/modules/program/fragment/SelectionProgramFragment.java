package cn.gietv.mlive.modules.program.fragment;

/**
 * Created by houde on 2016/4/18.
 */
public class SelectionProgramFragment {//extends AbsBaseFragment implements PullToRefreshBase.OnRefreshListener2{
//    public static int PROGRAM_PROTYPE_LIVE = 1;
//    public static int PROGRAM_PROTYPE_VIDEO = 2;
//    private View mCurrentView;
//    private PullToRefreshListView mListView;
//    private ProgramAdapter mAdapter;
//    private List<ProgramBean.ProgramEntity> mProgramList;
//
//    private int mCurrentPage = 1;
//    private ProgramModel mModel;
//
//    @Nullable
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        mCurrentView = inflater.inflate(R.layout.fragment_selection, container, false);
//        mListView = (PullToRefreshListView) mCurrentView.findViewById(R.id.program_all_lv_list);
//        mProgramList = new ArrayList<>();
//        mAdapter = new ProgramAdapter(getActivity(), mProgramList);
//        mListView.setAdapter(mAdapter);
//        mModel = RetrofitUtils.create(ProgramModel.class);
//        mListView.setOnRefreshListener(this);
//        mListView.setRefreshing(true);
//        onPullDownToRefresh(mListView);
//        return mCurrentView;
//    }
//
//    @Override
//    public void onPullDownToRefresh(PullToRefreshBase refreshView) {
//        mCurrentPage = 1;
//        mModel.getProgramList(CommConstants.COMMON_PAGE_COUNT, mCurrentPage, CommConstants.DEFAULT_SORT_ONLINE_PERSON, PROGRAM_PROTYPE_VIDEO, new DefaultLiveHttpCallBack<ProgramBean>() {
//            @Override
//            public void success(ProgramBean programBean) {
//                if (isNotFinish()) {
//                    if (NumUtils.getPage(programBean.cnt) == mCurrentPage) {
//                        mListView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
//                    } else if (NumUtils.getPage(programBean.cnt) > mCurrentPage) {
//                        mListView.setMode(PullToRefreshBase.Mode.BOTH);
//                        mCurrentPage++;
//                    }
//                    mProgramList.clear();
//                    if (programBean.programs != null) {
//                        mProgramList.addAll(programBean.programs);
//                        mAdapter.notifyDataSetChanged();
//                    }
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
//        mModel.getProgramList(CommConstants.COMMON_PAGE_COUNT, mCurrentPage, CommConstants.DEFAULT_SORT_ONLINE_PERSON, PROGRAM_PROTYPE_LIVE, new DefaultLiveHttpCallBack<ProgramBean>() {
//            @Override
//            public void success(ProgramBean programBean) {
//                if (isNotFinish()) {
//                    if (NumUtils.getPage(programBean.cnt) == mCurrentPage) {
//                        mListView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
//                    } else if (NumUtils.getPage(programBean.cnt) > mCurrentPage) {
//                        mListView.setMode(PullToRefreshBase.Mode.BOTH);
//                        mCurrentPage++;
//                    }
//                    mProgramList.addAll(programBean.programs);
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
}
