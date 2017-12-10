package cn.gietv.mlive.modules.album.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import cn.gietv.mlive.R;
import cn.gietv.mlive.modules.album.adapter.DescriptionAdapter;
import cn.gietv.mlive.parallaxheaderviewpager.ListViewFragment;

/**
 * Created by houde on 2016/5/28.
 */
public class DescriptionFragment extends ListViewFragment {
    private DescriptionAdapter mAdapter;
    private List<Object> datas = new ArrayList<>();
    public static DescriptionFragment getInstance(int position){
        DescriptionFragment videoFragment = new DescriptionFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_POSITION, position);
        videoFragment.setArguments(bundle);
        return videoFragment;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mPosition = getArguments().getInt(ARG_POSITION);
        View view = inflater.inflate(R.layout.fragment_list_view, container, false);
        mListView = (ListView) view.findViewById(R.id.listview);
        mListView.setDividerHeight(0);
        View placeHolderView = inflater.inflate(R.layout.header_placeholder, mListView, false);
        mListView.addHeaderView(placeHolderView);
        setAdapter();
        setListViewOnScrollListener();
        return view ;
    }
    public void setData(List<Object> data){
        if(datas == null)
            datas = new ArrayList<>();
        datas.clear();;
        datas.addAll(data);
        if(mAdapter != null)
            mAdapter.notifyDataSetChanged();
    }
    private void setAdapter() {
        mAdapter = new DescriptionAdapter(getActivity(),datas);
        mListView.setAdapter(mAdapter);
    }
}
