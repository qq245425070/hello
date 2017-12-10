package cn.gietv.mlive.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * author：steven
 * datetime：15/10/17 22:05
 *
 */
public class SquareImageView extends ImageView {
    private float ratio = 1.33f;

    public void setRatio(float ratio) {
        this.ratio = ratio;
    }

    public SquareImageView(Context context) {
        super(context);
    }

    public SquareImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        // 父容器传过来的宽度方向上的模式
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        // 父容器传过来的高度方向上的模式
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        // 父容器传过来的宽度的值
        int width = MeasureSpec.getSize(widthMeasureSpec) - getPaddingLeft()
                - getPaddingRight();
        // 父容器传过来的高度的值
        int height = MeasureSpec.getSize(heightMeasureSpec) - getPaddingLeft()
                - getPaddingRight();

        if (widthMode == MeasureSpec.EXACTLY
                && heightMode != MeasureSpec.EXACTLY && ratio != 0.0f) {
            // 判断条件为，宽度模式为Exactly，也就是填充父窗体或者是指定宽度；
            // 且高度模式不是Exaclty，代表设置的既不是fill_parent也不是具体的值，于是需要具体测量
            // 且图片的宽高比已经赋值完毕，不再是0.0f
            // 表示宽度确定，要测量高度
            height = (int) (width / ratio + 0.5f);
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(height,
                    MeasureSpec.EXACTLY);
        } else if (widthMode != MeasureSpec.EXACTLY
                && heightMode == MeasureSpec.EXACTLY && ratio != 0.0f) {
            // 判断条件跟上面的相反，宽度方向和高度方向的条件互换
            // 表示高度确定，要测量宽度
            width = (int) (height * ratio + 0.5f);

            widthMeasureSpec = MeasureSpec.makeMeasureSpec(width,
                    MeasureSpec.EXACTLY);
        }

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
