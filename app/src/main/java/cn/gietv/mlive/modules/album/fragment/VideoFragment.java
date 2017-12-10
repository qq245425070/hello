package cn.gietv.mlive.modules.album.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.gietv.mlive.R;
import cn.gietv.mlive.modules.album.adapter.VideoAdapter;
import cn.gietv.mlive.modules.download.activity.DownloadOverAcitvity;
import cn.gietv.mlive.modules.program.bean.ProgramBean;
import cn.gietv.mlive.parallaxheaderviewpager.ListViewFragment;
import cn.gietv.mlive.utils.IntentUtils;

/**
 * Created by houde on 2016/5/28.
 */
public class VideoFragment extends ListViewFragment {
    private List<ProgramBean.ProgramEntity> mPrograms = new ArrayList<>();
    private VideoAdapter mAdapter;
    private TextView mDownloadControl;
    private TextView mDownloadCount;
    private LinearLayout bottomParent;
    private View mRootView;
    public static VideoFragment getInstance(int position ){
        VideoFragment videoFragment = new VideoFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_POSITION, position);
        videoFragment.setArguments(bundle);
        return videoFragment;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mPosition = getArguments().getInt(ARG_POSITION);
        mRootView = inflater.inflate(R.layout.fragment_album_video, container, false);
        mListView = (ListView) mRootView.findViewById(R.id.listview);
        bottomParent = (LinearLayout)mRootView.findViewById(R.id.bottom_parent);
        mDownloadControl = (TextView) mRootView.findViewById(R.id.download_control);
        mDownloadCount = (TextView) mRootView.findViewById(R.id.download_count);
        bottomParent.setVisibility(View.GONE);
        mListView.setDividerHeight(0);
        View placeHolderView = inflater.inflate(R.layout.header_placeholder, mListView, false);
        mListView.addHeaderView(placeHolderView);
        setAdapter();
        setListViewOnScrollListener();
        mDownloadControl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentUtils.openActivity(getActivity(),DownloadOverAcitvity.class);
            }
        });
        return mRootView ;
    }

    public void setData(List<ProgramBean.ProgramEntity> programEntities){
        if(mPrograms == null)
            mPrograms = new ArrayList<>();
        mPrograms.clear();
        mPrograms.addAll(programEntities);
        if(mAdapter != null){
            mAdapter.notifyDataSetChanged();
        }
    }
    private void setAdapter() {
        mAdapter = new VideoAdapter(getActivity(),mPrograms);
        mListView.setAdapter(mAdapter);
        mAdapter.setDownloadListener(new VideoAdapter.DownloadListener() {
            @Override
            public void setDownloadResult(boolean result) {
                if(result){
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            bottomParent.setVisibility(View.VISIBLE);
                            mDownloadCount.setText(String.valueOf(Integer.valueOf(mDownloadCount.getText().toString()) + 1));
                        }
                    });

                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}
