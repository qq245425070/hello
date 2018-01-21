package com.houde.competitive.lagua.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.houde.competitive.lagua.R;
import com.zyw.horrarndoo.sdk.base.fragment.BaseCompatFragment;

/**
 * @author : houde
 *         Date : 18-1-21
 *         Desc :
 */

public class ViewPagerFragment extends BaseCompatFragment {
    public static ViewPagerFragment newInstance(){
        return new ViewPagerFragment();
    }
    @Override
    public int getLayoutId() {
        return R.layout.fragment_view_pager;
    }

    @Override
    public void initUI(View view, @Nullable Bundle savedInstanceState) {

    }
}
