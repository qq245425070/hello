package com.houde.competitive.lagua.ui.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.houde.competitive.lagua.R;
import com.zyw.horrarndoo.sdk.base.BasePresenter;
import com.zyw.horrarndoo.sdk.base.fragment.BaseMVPCompatFragment;

public class ImagesFragment extends BaseMVPCompatFragment {

    public static ImagesFragment newInstance() {
        ImagesFragment fragment = new ImagesFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_image;
    }

    @Override
    public void initUI(View view, @Nullable Bundle savedInstanceState) {
    }


    @NonNull
    @Override
    public BasePresenter initPresenter() {
        return null;
    }
}
