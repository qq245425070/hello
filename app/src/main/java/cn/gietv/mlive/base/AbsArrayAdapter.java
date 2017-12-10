package cn.gietv.mlive.base;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.List;

/**
 * author：steven
 * datetime：15/9/21 10:49
 *
 */
public class AbsArrayAdapter<T> extends ArrayAdapter<T> {
    public AbsArrayAdapter(Context context, List<T> objects) {
        super(context, 0, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return super.getView(position, convertView, parent);
    }
}
