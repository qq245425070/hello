package cn.gietv.mlive.modules.program.fragment;

/**
 * author：steven
 * datetime：15/10/5 16:40
 */
public class ProgramAllFragment {//extends AbsBaseFragment implements View.OnClickListener{
//    private View mCurrentView ;
//    private TextView mPanoramaTV,m3DTV,tempTV;
//    private ImageView mSeachImage;
//    private VRRecommendFragment mLiveFragent;
//    private SelectionGameFragment mGameFragment;
//    private FragmentManager fragmentManager;
//    @Nullable
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        mCurrentView = inflater.inflate(R.layout.program_all_layout,null);
//        mPanoramaTV = (TextView) mCurrentView.findViewById(R.id.vr_video);
//        m3DTV = (TextView) mCurrentView.findViewById(R.id.ddd_tv);
//        mSeachImage = (ImageView) mCurrentView.findViewById(R.id.head_ib_search);
//        mLiveFragent = new VRRecommendFragment();
//        fragmentManager = getChildFragmentManager();
//        fragmentManager.beginTransaction().replace(R.id.frame_layout,mLiveFragent).commit();
//        mPanoramaTV.setOnClickListener(this);
//        mSeachImage.setOnClickListener(this);
//        m3DTV.setOnClickListener(this);
//        chengeBg(mPanoramaTV);
//        return mCurrentView;
//    }
//
// @Override
// public void onClick(View v) {
//  if (v.getId() == R.id.vr_video) {
//      if(mLiveFragent == null)
//         mLiveFragent = new VRRecommendFragment();
//      fragmentManager.beginTransaction().replace(R.id.frame_layout, mLiveFragent).commit();
//      chengeBg(mPanoramaTV);
//  }else if(v.getId() == R.id.ddd_tv) {
//      if(mGameFragment == null)
//        mGameFragment = new SelectionGameFragment();
//      fragmentManager.beginTransaction().replace(R.id.frame_layout, mGameFragment).commit();
//      chengeBg(m3DTV);
//  }else  if(v.getId() == R.id.head_ib_search){
//    IntentUtils.openActivity(getActivity(), SearchActivity.class);
//    getActivity().overridePendingTransition(R.anim.activity_open, R.anim.activity_close);
//  }
// }
// private void chengeBg(TextView textView){
//     if(tempTV != null){
//         tempTV.setTextColor(getActivity().getResources().getColor(R.color.black));
//         tempTV.setBackgroundColor(Color.argb(255, 255, 255, 255));
//     }
//     textView.setTextColor(getActivity().getResources().getColor(R.color.theme_green));
//     textView.setBackgroundResource(R.drawable.program_head_bg);
//  tempTV = textView;
// }

}
