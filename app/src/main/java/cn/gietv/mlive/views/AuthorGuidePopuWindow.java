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
public class AuthorGuidePopuWindow extends PopupWindow {
    public AuthorGuidePopuWindow(Context context){
        View mRootView = LayoutInflater.from(context).inflate(R.layout.popu_author,null);
        TextView text1 = (TextView) mRootView.findViewById(R.id.text1);
        TextView text2 = (TextView) mRootView.findViewById(R.id.text2);
        Typeface typeface = Typeface.createFromAsset(context.getAssets(),"font.TTF");
        text1.setTypeface(typeface);
        text2.setTypeface(typeface);
        mRootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        setContentView(mRootView);
    }
}
