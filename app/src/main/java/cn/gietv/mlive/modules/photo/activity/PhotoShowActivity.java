package cn.gietv.mlive.modules.photo.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import cn.gietv.mlive.base.AbsBaseActivity;
import cn.gietv.mlive.modules.photo.bean.PhotoBean;
import cn.gietv.mlive.modules.photo.fragment.PhotoShowFragment;
import cn.gietv.mlive.utils.HeadViewController;
import cn.gietv.mlive.views.HackyViewPager;

import java.util.List;

/**
 * author：steven
 * datetime：15/10/20 20:50
 *
 */
public class PhotoShowActivity extends AbsBaseActivity {
    public static final String EXTRA_PHOTO_LIST = "extra_photo_list";
    public static final String EXTRA_POSITION = "extra_position";
    private List<PhotoBean.PhotosEntity> mList;
    private HackyViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(cn.gietv.mlive.R.layout.photo_show_layout);
        HeadViewController.initHeadWithoutSearch(this, "照片");
        mViewPager = (HackyViewPager) findViewById(cn.gietv.mlive.R.id.photo_show_vp_pager);
        mList = (List<PhotoBean.PhotosEntity>) getIntent().getExtras().getSerializable(EXTRA_PHOTO_LIST);
        int position = getIntent().getExtras().getInt(EXTRA_POSITION);
        mViewPager.setAdapter(new PhotoShowPagerAdapter(getSupportFragmentManager()));
        mViewPager.setCurrentItem(position);
    }

    private class PhotoShowPagerAdapter extends FragmentPagerAdapter {

        public PhotoShowPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return PhotoShowFragment.getInstence(mList.get(position).url);
        }

        @Override
        public int getCount() {
            return mList.size();
        }
    }
}
