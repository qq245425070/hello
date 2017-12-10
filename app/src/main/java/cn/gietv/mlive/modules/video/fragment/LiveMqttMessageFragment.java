package cn.gietv.mlive.modules.video.fragment;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.HashMap;

import cn.gietv.mlive.base.AbsBaseFragment;
import cn.gietv.mlive.utils.DensityUtil;
import cn.gietv.mlive.utils.DownLoadImageUtil;
import cn.gietv.mlive.utils.SharedPreferenceUtils;
import master.flame.danmaku.controller.IDanmakuView;
import master.flame.danmaku.danmaku.model.BaseDanmaku;
import master.flame.danmaku.danmaku.model.DanmakuTimer;
import master.flame.danmaku.danmaku.model.IDanmakus;
import master.flame.danmaku.danmaku.model.IDisplayer;
import master.flame.danmaku.danmaku.model.android.DanmakuContext;
import master.flame.danmaku.danmaku.model.android.Danmakus;
import master.flame.danmaku.danmaku.model.android.SpannedCacheStuffer;
import master.flame.danmaku.danmaku.parser.BaseDanmakuParser;

/**
 * author：steven
 * datetime：15/10/15 21:59
 */
public class LiveMqttMessageFragment extends AbsBaseFragment {
    private View mCurrentView;
    private IDanmakuView mDanmakuView,mDanmakuViewTop;
    private BaseDanmakuParser mParser;
    private Bitmap bitmap;
    private BackgroundCacheStuffer cacheStuffer;
    private DanmakuContext mContext;
    private class BackgroundCacheStuffer extends SpannedCacheStuffer{
        final Paint paint = new Paint(Paint.DITHER_FLAG);
        private Rect mSrcRect, mDestRect;
        private Bitmap bitmap;
        public BackgroundCacheStuffer(Bitmap bitmap){
            this.bitmap = bitmap;
        }
        @Override
        public void drawBackground(BaseDanmaku danmaku, Canvas canvas, float left, float top) {
            if(bitmap == null){
                paint.setColor(0x00000000);//设置弹幕的背景颜色
                canvas.drawRect(left + 2, top + 2, left + danmaku.paintWidth - 2, top + danmaku.paintHeight - 2, paint);
            }else {
                mSrcRect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
                mDestRect = new Rect(0, 0, bitmap.getWidth() * 2, bitmap.getHeight() * 2);
                canvas.drawBitmap(bitmap, mSrcRect, mDestRect, paint);
            }
        }
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mCurrentView = inflater.inflate(cn.gietv.mlive.R.layout.danmu_layout, container, false);
        mDanmakuView = (IDanmakuView) mCurrentView.findViewById(cn.gietv.mlive.R.id.sv_danmaku);
//        config = DanmakuGlobalConfig.DEFAULT;
        mContext = DanmakuContext.create();
        cacheStuffer = new BackgroundCacheStuffer(null);
        // 设置最大显示行数
        HashMap<Integer, Integer> maxLinesPair = new HashMap<Integer, Integer>();
        maxLinesPair.put(BaseDanmaku.TYPE_SCROLL_RL, 5); // 滚动弹幕最大显示5行
        // 设置是否禁止重叠
        HashMap<Integer, Boolean> overlappingEnablePair = new HashMap<Integer, Boolean>();
        overlappingEnablePair.put(BaseDanmaku.TYPE_SCROLL_RL, true);
        overlappingEnablePair.put(BaseDanmaku.TYPE_FIX_TOP, true);
        mContext.setDanmakuStyle(IDisplayer.DANMAKU_STYLE_STROKEN, 3).setDuplicateMergingEnabled(false).setScrollSpeedFactor(2.2f).setScaleTextSize(1.2f)
                .setCacheStuffer(new SpannedCacheStuffer(), null) // 图文混排使用SpannedCacheStuffer
//        .setCacheStuffer(new BackgroundCacheStuffer())  // 绘制背景使用BackgroundCacheStuffer
                .setMaximumLines(maxLinesPair).setCacheStuffer(cacheStuffer,null)
                .preventOverlapping(overlappingEnablePair);
//        config.setDanmakuStyle(DanmakuGlobalConfig.DANMAKU_STYLE_STROKEN, 3).setDuplicateMergingEnabled(false).setMaximumVisibleSizeInScreen(80).setScrollSpeedFactor(2f);
        mDanmakuView.setCallback(new master.flame.danmaku.controller.DrawHandler.Callback() {
            @Override
            public void updateTimer(DanmakuTimer timer) {
            }

            @Override
            public void drawingFinished() {

            }

            @Override
            public void danmakuShown(BaseDanmaku danmaku) {
                //                    Log.d("DFM", "danmakuShown(): text=" + danmaku.text);
            }

            @Override
            public void prepared() {
                mDanmakuView.start();
            }
        });

        mParser = new BaseDanmakuParser() {
            @Override
            protected IDanmakus parse() {
                return new Danmakus();
            }
        };
        mDanmakuView.prepare(mParser,mContext);
        mDanmakuView.showFPS(false);
        mDanmakuView.enableDanmakuDrawingCache(true);
//        mDanmakuViewTop = (IDanmakuView)mCurrentView.findViewById(R.id.sv_danmaku_top);
//        mDanmakuViewTop.setVisibility(View.VISIBLE);
//        mDanmakuViewTop.prepare(new BaseDanmakuParser() {
//            @Override
//            protected IDanmakus parse() {
//                return new Danmakus();
//            }
//        });
//        mDanmakuViewTop.showFPS(false);
//        mDanmakuViewTop.enableDanmakuDrawingCache(true);
//        mDanmakuViewTop.setCallback(new DrawHandler.Callback() {
//            @Override
//            public void prepared() {
//                mDanmakuViewTop.start();
//            }
//
//            @Override
//            public void updateTimer(DanmakuTimer timer) {
//
//            }
//        });
        return mCurrentView;
    }

    public synchronized void addDanmaku(String msg,int textSize) {
        BaseDanmaku danmaku = mContext.mDanmakuFactory.createDanmaku(BaseDanmaku.TYPE_SCROLL_RL);
        if (danmaku == null || mDanmakuView == null) {
            return;
        }

//        config.setCacheStuffer(cacheStuffer);
        danmaku.text = msg;
        danmaku.padding = 5;
        danmaku.priority = 1;
        danmaku.isLive = true;
        danmaku.time = mDanmakuView.getCurrentTime() + 1200;
        danmaku.textColor = Color.parseColor("#FFFFFF");
        danmaku.textSize = DensityUtil.dip2px(getActivity(), SharedPreferenceUtils.getInt("video_font",textSize));
        danmaku.textShadowColor = 0;
        Log.e("ceshi","addDanmaku " + msg);
        mDanmakuView.addDanmaku(danmaku);
    }
    public synchronized void addTopDanmaku(String nickname,String msg,String propName,String imageName) {
        BaseDanmaku danmaku = mContext.mDanmakuFactory.createDanmaku(BaseDanmaku.TYPE_SCROLL_RL);
        if (danmaku == null || mDanmakuViewTop == null) {
            return;
        }
        bitmap = DownLoadImageUtil.getBitmap(this.getActivity(),imageName);
        cacheStuffer = new BackgroundCacheStuffer(bitmap);
        mContext.setCacheStuffer(cacheStuffer,null);
        SpannableStringBuilder spannable = createSpannable(nickname,msg,propName);
        danmaku.text = spannable;
        danmaku.padding = 10;
        danmaku.priority = 1;
        danmaku.isLive = true;
        danmaku.time = mDanmakuView.getCurrentTime() + 2200;
        danmaku.textSize = DensityUtil.dip2px(getActivity(), 24);
        danmaku.textColor = Color.parseColor("#FFFFFF");
        danmaku.textShadowColor = 0;
        // danmaku.underlineColor = Color.GREEN;
        //    danmaku.borderColor = 0xFF999999;
        mDanmakuViewTop.addDanmaku(danmaku);
    }

    private SpannableStringBuilder createSpannable(String nickname,String msg,String propName) {
        String text = "       ";
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(text);
        spannableStringBuilder.append(nickname);
        spannableStringBuilder.append(msg);
        spannableStringBuilder.append(propName);
        ForegroundColorSpan span = new ForegroundColorSpan(Color.RED);
        spannableStringBuilder.setSpan(span, text.length(), nickname.length() + text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//        span = new ForegroundColorSpan(Color.RED);
//        int startPosition =nickname.length() + text.length() + msg.length();
//        int endPosition = startPosition + propName.length();
//        spannableStringBuilder.setSpan(span, startPosition, endPosition, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        //spannableStringBuilder.setSpan(span, 3, 6, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
       // spannableStringBuilder.setSpan(new BackgroundColorSpan(Color.parseColor("#88000000")), 0, spannableStringBuilder.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        return spannableStringBuilder;
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mDanmakuView != null && mDanmakuView.isPrepared()) {
            mDanmakuView.pause();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mDanmakuView != null && mDanmakuView.isPrepared() && mDanmakuView.isPaused()) {
            mDanmakuView.resume();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mDanmakuView != null) {
            mDanmakuView.release();
            mDanmakuView = null;
        }
    }

    boolean isShow = true;
    public boolean getDanmuShowing(){
        return isShow;
    }
    public void controlDanmu() {
        if (mDanmakuView != null) {
            if (isShow) {
                mDanmakuView.hide();
            } else {
                mDanmakuView.show();
            }
            isShow = !isShow;
        }
    }

}
