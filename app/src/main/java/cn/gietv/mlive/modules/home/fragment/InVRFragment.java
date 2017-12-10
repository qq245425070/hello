package cn.gietv.mlive.modules.home.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cn.gietv.mlive.base.AbsBaseFragment;

/**
 * Created by houde on 2016/4/26.
 */
public class InVRFragment extends AbsBaseFragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getActivity().finish();
        return super.onCreateView(inflater, container, savedInstanceState);
    }
}
