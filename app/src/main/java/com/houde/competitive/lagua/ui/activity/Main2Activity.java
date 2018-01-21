package com.houde.competitive.lagua.ui.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.widget.TextView;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.houde.competitive.lagua.R;
import com.houde.competitive.lagua.ui.fragment.ArticlesFragment;
import com.houde.competitive.lagua.ui.fragment.ImagesFragment;
import com.houde.competitive.lagua.ui.fragment.RecommendFragment;
import com.houde.competitive.lagua.ui.fragment.SatinsFragment;
import com.zyw.horrarndoo.sdk.adapter.FragmentAdapter;
import com.zyw.horrarndoo.sdk.base.activity.BaseCompatActivity;
import com.zyw.horrarndoo.sdk.utils.StatusBarUtils;

import java.util.ArrayList;
import java.util.List;
public class Main2Activity extends BaseCompatActivity implements BottomNavigationBar.OnTabSelectedListener ,ViewPager.OnPageChangeListener{

    BottomNavigationBar bottomNavigationBar;
    ViewPager viewPager;
    private TextView toolbar;
    @Override
    protected void initView(Bundle savedInstanceState) {
        toolbar =  (TextView) findViewById(R.id.title);
        toolbar.setText(R.string.title_home);
        bottomNavigationBar = (BottomNavigationBar) findViewById(R.id.bottom_navigation_bar);
        viewPager = (ViewPager)findViewById(R.id.view_pager);
        bottomNavigationBar.setMode(BottomNavigationBar.MODE_FIXED);
        bottomNavigationBar.setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_STATIC);
        bottomNavigationBar.addItem(new BottomNavigationItem(R.mipmap.tuijian_xuanzhong,R.string.title_home).setInactiveIcon(ContextCompat.getDrawable(this,R.mipmap.tuijian_weixuanzhong))).setActiveColor(R.color.main_bottom_text)
                .addItem(new BottomNavigationItem(R.mipmap.duanzi_xuanzhong,R.string.title_article).setInactiveIcon(ContextCompat.getDrawable(this,R.mipmap.duanzi_weixuanzhong))).setActiveColor(R.color.main_bottom_text)
                .addItem(new BottomNavigationItem(R.mipmap.wenzhang_xuanzhong,R.string.title_satin).setInactiveIcon(ContextCompat.getDrawable(this,R.mipmap.wenzhang_weixuanzhong))).setActiveColor(R.color.main_bottom_text)
                .addItem(new BottomNavigationItem(R.mipmap.tupian_xuanzhong,R.string.title_image).setInactiveIcon(ContextCompat.getDrawable(this,R.mipmap.tupian_weixuanzhong))).setActiveColor(R.color.main_bottom_text)
                .initialise();
        bottomNavigationBar.setTabSelectedListener(this);
        List<Fragment> fragmentList = new ArrayList<>();
        fragmentList.add(RecommendFragment.newInstance());
        fragmentList.add(SatinsFragment.newInstances());
        fragmentList.add(ArticlesFragment.newInstance());
        fragmentList.add(ImagesFragment.newInstance());
        viewPager.setAdapter(new FragmentAdapter(getSupportFragmentManager(),fragmentList));
        viewPager.addOnPageChangeListener(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main2;
    }

    @Override
    public void onTabSelected(int position) {
        viewPager.setCurrentItem(position);
        setToolBarTitle(position);
    }

    private void setToolBarTitle(int position) {
        int titleId = R.string.title_home ;
        switch (position){
            case 0 :
                titleId = R.string.title_home;
                break;
            case 1:
                titleId =R.string.title_article;
                break;
            case 2:
                titleId = R.string.title_satin;

                break;
            case 3:
                titleId = R.string.title_image;
                break;
        }
        toolbar.setText(titleId);
    }

    @Override
    public void onTabUnselected(int position) {

    }

    @Override
    public void onTabReselected(int position) {
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
        bottomNavigationBar.selectTab(position);
        setToolBarTitle(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
