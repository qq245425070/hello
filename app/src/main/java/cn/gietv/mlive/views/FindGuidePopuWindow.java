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
public class FindGuidePopuWindow extends PopupWindow {
    public FindGuidePopuWindow(Context context){
        View mRootView = LayoutInflater.from(context).inflate(R.layout.popu_find,null);
        TextView text = (TextView) mRootView.findViewById(R.id.text);
        Typeface typeface = Typeface.createFromAsset(context.getAssets(),"font.TTF");
        text.setTypeface(typeface);
        mRootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        setContentView(mRootView);
    }
}
