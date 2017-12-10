package cn.gietv.mlive.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * author：steven
 * datetime：15/10/17 22:05
 *
 */
public class RecommendImageView extends ImageView {
    private float ratio = 1.565f;

    public void setRatio(float ratio) {
        this.ratio = ratio;
    }

    public RecommendImageView(Context context) {
        super(context);
    }

    public RecommendImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // 父容器传过来的宽度的值
        int width = MeasureSpec.getSize(widthMeasureSpec)/2;
        widthMeasureSpec = MeasureSpec.makeMeasureSpec(width,
                MeasureSpec.EXACTLY);
            // 判断条件为，宽度模式为Exactly，也就是填充父窗体或者是指定宽度；
            // 且高度模式不是Exaclty，代表设置的既不是fill_parent也不是具体的值，于是需要具体测量
            // 且图片的宽高比已经赋值完毕，不再是0.0f
            // 表示宽度确定，要测量高度
        int height = (int) (width / ratio + 0.5f)*2;
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(height,
                    MeasureSpec.EXACTLY);

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
