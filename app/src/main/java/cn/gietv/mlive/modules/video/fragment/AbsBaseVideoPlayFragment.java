package cn.gietv.mlive.modules.video.fragment;

import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.util.ArrayMap;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import cn.gietv.mlive.base.AbsBaseFragment;

/**
 * author：steven
 * datetime：15/10/11 11:19
 *
 */
public abstract class AbsBaseVideoPlayFragment extends AbsBaseFragment implements OnClickListener {
    private View mCurrentView;
    private ImageButton mExitButton, mPlayButton, mResolutionButton;
    private FrameLayout mHeadLayout, mContentLayout;
    private LinearLayout mBottomLayout;
    private TextView mTitleText;
    private VideoPlayCallback mCallback;
    private ArrayMap<String, String> mResolutionMap;
    private boolean mIsPlaying = false;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mCurrentView = inflater.inflate(getLayoutId(), container, false);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    private void initViews() {
        mExitButton = (ImageButton) mCurrentView.findViewById(cn.gietv.mlive.R.id.play_controller_ib_exit);
        mPlayButton = (ImageButton) mCurrentView.findViewById(cn.gietv.mlive.R.id.play_controller_ib_play);
        mResolutionButton = (ImageButton) mCurrentView.findViewById(cn.gietv.mlive.R.id.play_controller_ib_resolution);
        mTitleText = (TextView) mCurrentView.findViewById(cn.gietv.mlive.R.id.play_controller_tv_title);
        mHeadLayout = (FrameLayout) mCurrentView.findViewById(cn.gietv.mlive.R.id.play_controller_fl_head);
        mContentLayout = (FrameLayout) mCurrentView.findViewById(cn.gietv.mlive.R.id.play_controller_fl_content);
        mBottomLayout = (LinearLayout) mCurrentView.findViewById(cn.gietv.mlive.R.id.play_controller_ll_bottom);

        mTitleText.setText(getTitle());
        mExitButton.setOnClickListener(this);
        mPlayButton.setOnClickListener(this);
        mResolutionButton.setOnClickListener(this);
        mContentLayout.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        if (view.getId() == cn.gietv.mlive.R.id.play_controller_ib_exit) {
            getActivity().finish();
        }else if(view.getId() == cn.gietv.mlive.R.id.play_controller_ib_play) {
            if (mIsPlaying) {
                showPause();
                showStop();
                if (mCallback != null) {
                    mCallback.pause();
                    mCallback.stop();
                }
            } else {
                showPlay();
                if (mCallback != null) {
                    mCallback.play();
                }
            }
            mIsPlaying = !mIsPlaying;
        }else if(view.getId() ==  cn.gietv.mlive.R.id.play_controller_ib_resolution) {
            ResolutionPopWindow popWindow = new ResolutionPopWindow();
            popWindow.showPopupWindow();
        }else if(view.getId() == cn.gietv.mlive.R.id.play_controller_fl_content){
                if (mBottomLayout.getVisibility() == View.VISIBLE) {
                    mHeadLayout.setVisibility(View.GONE);
                    mBottomLayout.setVisibility(View.GONE);
                } else {
                    mHeadLayout.setVisibility(View.VISIBLE);
                    mBottomLayout.setVisibility(View.VISIBLE);
                }
        }
    }

    public void setCallback(VideoPlayCallback callback) {
        mCallback = callback;
    }

    public void setResolutionMap(ArrayMap<String, String> resolutionMap) {
        mResolutionMap = resolutionMap;
    }

    public abstract int getLayoutId();

    public abstract void showPlay();

    public abstract void showPause();

    public abstract void showStop();

    public abstract String getTitle();

    public static interface VideoPlayCallback {
        void play();

        void pause();

        void stop();

        void seekPosition(long position);

        void changeResolution(String resolution);

        void sendMessage(String message);
    }

    private class ResolutionPopWindow implements AdapterView.OnItemClickListener {
        private PopupWindow mPopupWindow;
        private ListView mCurrentView;

        private void initViews() {
            mCurrentView = new ListView(getActivity());
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity()
                    , android.R.layout.simple_list_item_2, android.R.id.text2, mResolutionMap.keySet().toArray(new String[]{}));
            mCurrentView.setAdapter(adapter);
            mCurrentView.setOnItemClickListener(this);
            PopupWindow popupWindow = new PopupWindow(mCurrentView, ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT, true);
            popupWindow.setBackgroundDrawable(new BitmapDrawable());
        }

        public void showPopupWindow() {
            initViews();
            mCurrentView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
            int popupWidth = mCurrentView.getMeasuredWidth();
            int popupHeight = mCurrentView.getMeasuredHeight();
            int[] location = new int[2];
            mResolutionButton.getLocationOnScreen(location);
            mPopupWindow.showAtLocation(mResolutionButton, Gravity.NO_GRAVITY, (location[0] + mResolutionButton.getWidth() / 2) - popupWidth / 2,
                    location[1] - popupHeight);
        }

        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            mPopupWindow.dismiss();
            String resolution = mResolutionMap.get(mResolutionMap.keyAt(i));
            if (mCallback != null) {
                mCallback.changeResolution(resolution);
            }
        }
    }
}
