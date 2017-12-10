package cn.gietv.mlive.modules.recommend.fragment;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;

import java.io.UnsupportedEncodingException;
import java.util.List;

import cn.gietv.mlive.R;
import cn.gietv.mlive.base.AbsBaseFragment;
import cn.gietv.mlive.constants.CommConstants;
import cn.gietv.mlive.http.DefaultLiveHttpCallBack;
import cn.gietv.mlive.modules.download.activity.DownloadOverAcitvity;
import cn.gietv.mlive.modules.game.bean.GameInfoBean;
import cn.gietv.mlive.modules.recommend.adapter.ReCommendHomePagerAdapter;
import cn.gietv.mlive.modules.recommend.model.RecommendModel;
import cn.gietv.mlive.modules.search.activity.SearchResultActivity;
import cn.gietv.mlive.utils.ConfigUtils;
import cn.gietv.mlive.utils.IntentUtils;
import cn.gietv.mlive.utils.SharedPreferenceUtils;
import cn.gietv.mlive.utils.StringUtils;
import cn.gietv.mlive.utils.ToastUtils;
import cn.gietv.mlive.views.FindGuidePopuWindow;
import cn.gietv.mlive.views.slidingTab.SlidingTabLayout;
import cn.kalading.android.retrofit.utils.RetrofitUtils;

/**
 * Created by houde on 2016/9/28.
 */
public class RecommendHomeFragment extends AbsBaseFragment {
    private View mRootView;
    private ViewPager mPager;
    private SlidingTabLayout magicIndicator2;
    private RecommendModel model;
    private int mCurrentPage = 1;
    private List<GameInfoBean.GameInfoEntity> mGameList;
    private EditText mEditText;
    private ImageView mSearchButton;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_recommend_home,null);
        mPager = (ViewPager) mRootView.findViewById(R.id.viewpager);
        magicIndicator2 = (SlidingTabLayout) mRootView.findViewById(R.id.tab_indicator);
        magicIndicator2.setDistributeEvenly(false);
        model = RetrofitUtils.create(RecommendModel.class);
        model.getAreaList("game", CommConstants.COMMON_PAGE_COUNT, mCurrentPage, CommConstants.DEFAULT_SORT_ONLINE_PERSON, new DefaultLiveHttpCallBack<GameInfoBean>() {
            @Override
            public void success(GameInfoBean gameInfoBean) {
                if(isNotFinish()){
                    if (gameInfoBean == null || gameInfoBean.games == null)
                        return;
                    mGameList = gameInfoBean.games;
                    GameInfoBean.GameInfoEntity game = new GameInfoBean.GameInfoEntity();
                    game.name = "全部";
                    game._id = "all";
                    mGameList.add(0,game);
                    mPager.setAdapter(new ReCommendHomePagerAdapter(getChildFragmentManager(),mGameList));
                    magicIndicator2.setViewPager(mPager);
                }
            }

            @Override
            public void failure(String message) {

            }
        });
        mEditText = (EditText) mRootView.findViewById(R.id.search_et_text);
        mSearchButton = (ImageView) mRootView.findViewById(R.id.search_tv_search);
        mSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentUtils.openActivity(getActivity(), DownloadOverAcitvity.class);
            }
        });
        mEditText.requestFocus();
        mEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEND || (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                    switch (event.getAction()){
                        case KeyEvent.ACTION_UP:
                            //发送请求
                            seachBody();
                            return true;
                        default:
                            return true;
                    }

                }
                return false;
            }
        });
        return mRootView;
    }
    private void seachBody() {
        String text = mEditText.getText().toString();
        if (StringUtils.isNotEmpty(text)) {
            try {
                text = new String(text.getBytes(), "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }else{
            ToastUtils.showToast(getActivity(),"输入内容后再搜素");
            return;
        }
        SearchResultActivity.openSearResultActivity(getActivity(),text,false);

    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("推荐页");
        if(SharedPreferenceUtils.getBoolean(ConfigUtils.FIRST_VERSION + ConfigUtils.FIRST_IN_FIND_PAGE,true)) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    showGuideWindow();
                }
            }, 100);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("推荐页");
    }

    private void showGuideWindow(){
        SharedPreferenceUtils.saveProp(ConfigUtils.FIRST_VERSION + ConfigUtils.FIRST_IN_FIND_PAGE,false);
        FindGuidePopuWindow popuWindow = new FindGuidePopuWindow(getActivity());
        popuWindow.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        popuWindow.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        popuWindow.setOutsideTouchable(true);
        popuWindow.setFocusable(true);
        popuWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        popuWindow.showAtLocation(mRootView, Gravity.NO_GRAVITY,0,0);
    }
}
