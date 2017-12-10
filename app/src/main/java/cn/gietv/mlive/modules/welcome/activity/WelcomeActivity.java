package cn.gietv.mlive.modules.welcome.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.WindowManager;

import cn.gietv.mlive.R;
import cn.gietv.mlive.base.AbsBaseActivity;
import cn.gietv.mlive.modules.welcome.fragment.WelcomeFragment;

/**
 * author：steven
 * datetime：15/10/21 15:37
 *
 */
public class WelcomeActivity extends AbsBaseActivity {
    private ViewPager mViewPager;
    private int IMAGES[] = {R.mipmap.welcome_1, R.mipmap.welcome_2, R.mipmap.welcome_3};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.welcome_layout);
        mViewPager = (ViewPager) findViewById(R.id.welcome_viewpager);
        mViewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                WelcomeFragment fragment = new WelcomeFragment();
                fragment.setDrawable(IMAGES[position]);
                fragment.setShowExit(position == IMAGES.length - 1);
                return fragment;
            }

            @Override
            public int getCount() {
                return IMAGES.length;
            }
        });
    }
}
