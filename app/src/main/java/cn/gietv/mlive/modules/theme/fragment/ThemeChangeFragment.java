package cn.gietv.mlive.modules.theme.fragment;

import cn.gietv.mlive.base.AbsBaseFragment;

/**
 * author：steven
 * datetime：15/10/19 10:09
 *
 */
public class ThemeChangeFragment extends AbsBaseFragment {
//    private LinearLayout mCurrentView;
//    private ArrayMap<Integer, Integer> mThemeMap = new ArrayMap<Integer, Integer>() {
//        {
//            put(R.id.theme_iv_green, CommConstants.THEME_GREEN);
//            put(R.id.theme_iv_blue, CommConstants.THEME_BLUE);
//            put(R.id.theme_iv_orange, CommConstants.THEME_ORANGE);
//            put(R.id.theme_iv_purple, CommConstants.THEME_PURPLE);
//            put(R.id.theme_iv_red, CommConstants.THEME_RED);
//            put(R.id.theme_iv_yellow, CommConstants.THEME_YELLOW);
//        }
//    };
//
//    @Nullable
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        mCurrentView = (LinearLayout) inflater.inflate(R.layout.change_theme_layout, container, false);
//        int theme = Integer.parseInt(CacheUtils.getCache().getAsString(CacheConstants.CACHE_THEME_COLOR));
//        for (int i = 0; i < mCurrentView.getChildCount(); i++) {
//            CircleImageView v = (CircleImageView) mCurrentView.getChildAt(i);
//            v.setOnClickListener(this);
//            if (theme == mThemeMap.get(v.getId())) {
//                v.setBorderColor(Color.BLACK);
//                v.setBorderWidth(DensityUtil.dip2px(getActivity(), 3));
//            }
//        }
//        return mCurrentView;
//    }
//
//    @Override
//    public void onClick(View view) {
//        int oldTheme = Integer.parseInt(CacheUtils.getCache().getAsString(CacheConstants.CACHE_THEME_COLOR));
//
//        int newTheme = mThemeMap.get(view.getId());
//        if (oldTheme != newTheme) {
//            CacheUtils.getCache().put(CacheConstants.CACHE_THEME_COLOR, String.valueOf(newTheme));
//            getActivity().sendBroadcast(new Intent(HomeActivity.THEME_CHANGE_ACTION));
//            for (int i = 0; i < mCurrentView.getChildCount(); i++) {
//                CircleImageView imageView = (CircleImageView) mCurrentView.getChildAt(i);
//                if (imageView.getId() == view.getId()) {
//                    imageView.setBorderColor(Color.BLACK);
//                    imageView.setBorderWidth(DensityUtil.dip2px(getActivity(), 3));
//                } else {
//                    imageView.setBorderWidth(0);
//                }
//            }
//        }
//    }
}
