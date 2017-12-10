package cn.gietv.mlive.modules.welcome.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import cn.gietv.mlive.base.AbsBaseFragment;
import cn.gietv.mlive.modules.home.activity.HomeActivity;
import cn.gietv.mlive.modules.subscribe.activity.SubscribeActivity;
import cn.gietv.mlive.utils.IntentUtils;
import cn.gietv.mlive.utils.SharedPreferenceUtils;

/**
 * author：steven
 * datetime：15/10/21 15:55
 *
 */
public class WelcomeFragment extends AbsBaseFragment {
    private View mCurrentView;
    private ImageView mImageView;
    private TextView mExitText;
    private boolean mShowExit = false;
    private int mDrawable;

    public void setDrawable(int drawable) {
        mDrawable = drawable;
    }

    public void setShowExit(boolean showExit) {
        mShowExit = showExit;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mCurrentView = inflater.inflate(cn.gietv.mlive.R.layout.welcome_fragment, container, false);
        mImageView = (ImageView) mCurrentView.findViewById(cn.gietv.mlive.R.id.welcome_iv_image);
        mExitText = (TextView) mCurrentView.findViewById(cn.gietv.mlive.R.id.welcome_tv_close);
        mExitText.setVisibility(mShowExit ? View.VISIBLE : View.GONE);
        mImageView.setImageResource(mDrawable);
        mExitText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                SharedPreferenceUtils.saveProp(HomeActivity.SHARE_FIRST_INTO, true);
                if(SharedPreferenceUtils.getBoolean("first_install",true)){
                    IntentUtils.openActivity(getActivity(), SubscribeActivity.class);
                    SharedPreferenceUtils.saveProp(HomeActivity.SHARE_FIRST_INTO, false);
                    getActivity().finish();
                }else {
                    IntentUtils.openActivity(getActivity(), HomeActivity.class);
                    getActivity().finish();
                }
            }
        });
        return mCurrentView;
    }
}
