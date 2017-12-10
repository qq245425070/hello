package cn.gietv.mlive.base;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.umeng.analytics.MobclickAgent;

/**
 * author：steven
 * datetime：15/9/21 19:43
 *
 */
public abstract class AbsBaseFragment extends Fragment {
    public boolean isNotFinish() {
        return getActivity() != null && !getActivity().isFinishing();
    }

    public boolean onBack() {
        return true;
    }

    public void removeChildFragment(Fragment fragment) {
        FragmentManager fm = getChildFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.remove(fragment);
        ft.commit();
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(getClass().getName());
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(getClass().getName());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

}
