package cn.gietv.mlive.utils;

import android.text.Spanned;
import android.util.SparseArray;
import android.view.View;
import android.widget.TextView;

/**
 * 简化ViewHolder
 */
public class ViewHolder {
    @SuppressWarnings("unchecked")
    public static <T extends View> T get(View view, int id) {
        SparseArray<View> viewHolder = (SparseArray<View>) view.getTag();
        if (viewHolder == null) {
            viewHolder = new SparseArray<View>();
            view.setTag(viewHolder);
        }
        View childView = viewHolder.get(id);
        if (childView == null) {
            childView = view.findViewById(id);
            viewHolder.put(id, childView);
        }
        return (T) childView;
    }

    public static void setText(View view, int resid, String text) {
        if (StringUtils.isNotEmpty(text)) {
            TextView textView = ViewHolder.get(view, resid);
            textView.setText(text);
        }
    }

    public static void setText(View view, int resid, Spanned text) {
        if (text != null) {
            TextView textView = ViewHolder.get(view, resid);
            textView.setText(text);
        }
    }
}