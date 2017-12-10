package cn.gietv.mlive.views;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;

import java.util.Collections;
import java.util.List;

import cn.gietv.mlive.R;

/**
 * Created by houde on 2016/7/28.
 */
public class ShowUidPopuWindow extends PopupWindow {
    private List<String> data;
    private Context mContext;
    private View mRootView ;
    private ListView mListView;
    private ArrayAdapter<String> mAdapter;
    public ShowUidPopuWindow(Context context, final List<String> data2){
        this.mContext = context;
        this.data = data2;
        mRootView = LayoutInflater.from(context).inflate(R.layout.fragment_list_view,null);
        mListView = (ListView) mRootView.findViewById(R.id.listview);
        mListView.setBackgroundColor(Color.WHITE);
        mAdapter = new ArrayAdapter<>(mContext,android.R.layout.simple_list_item_1,data);
        mListView.setAdapter(mAdapter );
        this.setContentView(mRootView);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(listener != null){
                    listener.itemOnClick(data.get(position));
                }
            }
        });
    }
    public void setData(List<String> data){
        mAdapter = new ArrayAdapter<>(mContext,android.R.layout.simple_list_item_1,data);
        mListView.setAdapter(mAdapter);
    }
    public void setItemOnClickListener(ItemOnClickListener listener){
        this.listener = listener;
    }
    private ItemOnClickListener listener;
    public interface ItemOnClickListener{
        void itemOnClick(String uid);
    }
}
