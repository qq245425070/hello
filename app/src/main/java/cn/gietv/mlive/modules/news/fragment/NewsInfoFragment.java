package cn.gietv.mlive.modules.news.fragment;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cn.gietv.mlive.R;
import cn.gietv.mlive.base.AbsBaseFragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class NewsInfoFragment extends AbsBaseFragment {


    public NewsInfoFragment() {
    }

    public static NewsInfoFragment getInstence(){
        NewsInfoFragment newsInfoFragment = new NewsInfoFragment();
        return newsInfoFragment;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_news_info, container, false);
    }
}
