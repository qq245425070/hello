package cn.gietv.mlive.modules.author.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import cn.gietv.mlive.R;
import cn.gietv.mlive.modules.author.adapter.AlbumAdapter;
import cn.gietv.mlive.modules.game.bean.GameInfoBean;
import cn.gietv.mlive.parallaxheaderviewpager.ListViewFragment;

/**
 * Created by houde on 2016/5/29.
 */
public class AlbumFragment extends ListViewFragment {
    private List<GameInfoBean.GameInfoEntity> mPrograms = new ArrayList<>();;
    private AlbumAdapter mAdapter;
    private View mRootView;
    public static AlbumFragment getInstance(int position ){
        AlbumFragment videoFragment = new AlbumFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_POSITION, position);
        videoFragment.setArguments(bundle);
        return videoFragment;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mPosition = getArguments().getInt(ARG_POSITION);
        mRootView = inflater.inflate(R.layout.fragment_list_view, container, false);
        mListView = (ListView) mRootView.findViewById(R.id.listview);
        mListView.setDividerHeight(0);
        View placeHolderView = inflater.inflate(R.layout.header_placeholder, mListView, false);
        mListView.addHeaderView(placeHolderView);
        setAdapter();
        setListViewOnScrollListener();
        return mRootView ;
    }
    public void setData(List<GameInfoBean.GameInfoEntity> programEntities){
        if(mPrograms == null)
            mPrograms = new ArrayList<>();
        mPrograms.clear();
        mPrograms.addAll(programEntities);
        if(mAdapter != null)
            mAdapter.notifyDataSetChanged();
    }
    private void setAdapter() {
        mAdapter = new AlbumAdapter(getActivity(),mPrograms);
        mListView.setAdapter(mAdapter);
    }
}
