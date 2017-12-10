package cn.gietv.mlive.views;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.TextView;

import cn.gietv.mlive.R;

/**
 * Created by houde on 2016/10/25.
 */
public class TagGuidePopuWindow extends PopupWindow {
    private Context mContext;
    private View mRootView;
    public TagGuidePopuWindow(Context context){
        mRootView = LayoutInflater.from(context).inflate(R.layout.popu_tag_guide,null);
        TextView text = (TextView) mRootView.findViewById(R.id.text);
        Typeface typeFace =Typeface.createFromAsset(context.getAssets(),"font.TTF");
        text.setTypeface(typeFace);
        mRootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        setContentView(mRootView);
    }
}
