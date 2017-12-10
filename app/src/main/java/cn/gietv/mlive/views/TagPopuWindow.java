package cn.gietv.mlive.views;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.List;

import cn.gietv.mlive.R;
import cn.gietv.mlive.modules.category.adapter.CategoryAdapter;
import cn.gietv.mlive.utils.DensityUtil;

/**
 * Created by houde on 2016/6/8.
 */
public class TagPopuWindow extends PopupWindow {
    private View mRootView;
    private FlowLayout mFlowLayout;
    private int positionFlag;
    private TextView tempText;
    private Context mContext;
    public TagPopuWindow(Context context, final List<String> labels,int height){
        this.mContext = context;
        mRootView = LayoutInflater.from(context).inflate(R.layout.popu_tag,null);
        ScrollView scrollView = (ScrollView) mRootView.findViewById(R.id.scrollView);
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) scrollView.getLayoutParams();
        params.height = height;
        scrollView.smoothScrollTo(0,0);
        scrollView.setLayoutParams(params);
        mFlowLayout = (FlowLayout) mRootView.findViewById(R.id.flow_layout);
        ViewGroup.MarginLayoutParams lp = new ViewGroup.MarginLayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.leftMargin = DensityUtil.dip2px(mContext,8);
        lp.topMargin = DensityUtil.dip2px(mContext,6);
        lp.bottomMargin = DensityUtil.dip2px(mContext,6);
        if(!labels.get(0).equals("全部"))
            labels.add(0,"全部");
        for (int i = 0;i < labels.size();i ++){
            final TextView textView = (TextView) LayoutInflater.from(context).inflate(R.layout.flowlayout_text,null);
            textView.setText(labels.get(i));
            textView.setTextColor(mContext.getResources().getColor(R.color.text_color_101010));
            if(positionFlag == i){
                textView.setBackgroundResource(R.drawable.commen_button_light_green2);
                textView.setTextColor(Color.parseColor("#4fc396"));
                tempText = textView;
            }else {
                textView.setBackgroundResource(R.drawable.commen_button_theme_color5);
            }
            final int finalI = i;
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (tempText != null) {
                        tempText.setBackgroundResource(R.drawable.commen_button_theme_color5);
                        tempText.setTextColor(mContext.getResources().getColor(R.color.text_color_101010));
                    }
                    textView.setBackgroundResource(R.drawable.commen_button_light_green2);
                    textView.setTextColor(Color.parseColor("#4fc396"));
                    tempText = textView;
                    positionFlag = finalI;
                    if(adapterListener != null){
                        adapterListener.changeData(labels.get(finalI),finalI);
                    }
                    dismiss();
                }
            });
            mFlowLayout.addView(textView, lp);
        }
        setContentView(mRootView);
    }
    private CategoryAdapter.AdapterListener adapterListener;
    public void setAdapterListener(CategoryAdapter.AdapterListener listener){
        adapterListener = listener;
    }
    public void setBg(int position){
        if(mFlowLayout != null){
            TextView childAt = (TextView) mFlowLayout.getChildAt(position);
            if(tempText != null){
                tempText.setTextColor(mContext.getResources().getColor(R.color.text_color_101010));
                tempText.setBackgroundResource(R.drawable.commen_button_theme_color5);
            }
            childAt.setTextColor(Color.parseColor("#4fc396"));
            childAt.setBackgroundResource(R.drawable.commen_button_light_green2);
            tempText = childAt;
        }
    }
}
