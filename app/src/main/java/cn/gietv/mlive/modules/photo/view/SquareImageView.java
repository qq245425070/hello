package cn.gietv.mlive.modules.photo.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * author：steven
 * datetime：15/10/16 23:58
 *
 */
public class SquareImageView extends ImageView {
    public SquareImageView(Context context) {
        super(context);
    }

    public SquareImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int height = MeasureSpec.makeMeasureSpec(MeasureSpec.getSize(widthMeasureSpec), MeasureSpec.EXACTLY);
        super.onMeasure(widthMeasureSpec, height);
    }
}
