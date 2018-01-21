package com.houde.competitive.lagua.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.houde.competitive.lagua.R;
import com.zyw.horrarndoo.sdk.utils.DisplayUtils;

/**
 * @author : houde
 *         Date : 18-1-21
 *         Desc :
 */

public class RecommendDecoration extends RecyclerView.ItemDecoration {

    public Context mContext;
    public Drawable mDivider;
    public RecommendDecoration(Context context){
        mContext = context;
        mDivider = mContext.getResources().getDrawable(R.drawable.line_shape);
    }
    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        drawHorizontalLine(c,parent);
    }
    //画横线, 这里的parent其实是显示在屏幕显示的这部分
    public void drawHorizontalLine(Canvas c, RecyclerView parent){
        int left = parent.getPaddingLeft() + DisplayUtils.dp2px(18);
        int right = parent.getWidth() - parent.getPaddingRight() - DisplayUtils.dp2px(18);
        final int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++){
            final View child = parent.getChildAt(i);
            //获得child的布局信息
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams)child.getLayoutParams();
            final int top = child.getBottom() + params.bottomMargin;
            final int bottom = top + mDivider.getIntrinsicHeight();
            mDivider.setBounds(left, top, right, bottom);
            mDivider.draw(c);
            //Log.d("wnw", left + " " + top + " "+right+"   "+bottom+" "+i);
        }
    }

}
