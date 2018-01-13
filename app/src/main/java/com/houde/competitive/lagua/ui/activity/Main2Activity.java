package com.houde.competitive.lagua.ui.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.houde.competitive.lagua.R;
import com.houde.competitive.lagua.ui.fragment.ArticlesFragment;
import com.houde.competitive.lagua.ui.fragment.ImagesFragment;
import com.houde.competitive.lagua.ui.fragment.RecommendFragment;
import com.houde.competitive.lagua.ui.fragment.SatinsFragment;
import com.zyw.horrarndoo.sdk.adapter.FragmentAdapter;
import com.zyw.horrarndoo.sdk.base.activity.BaseCompatActivity;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class Main2Activity extends BaseCompatActivity implements BottomNavigationBar.OnTabSelectedListener ,ViewPager.OnPageChangeListener{

    @BindView(R.id.bottom_navigation_bar)
    BottomNavigationBar bottomNavigationBar;
    @BindView(R.id.view_pager)
    ViewPager viewPager;
    private Toolbar toolbar;
    @Override
    protected void initView(Bundle savedInstanceState) {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("首页");
        setSupportActionBar(toolbar);
        bottomNavigationBar = (BottomNavigationBar) findViewById(R.id.bottom_navigation_bar);
        viewPager = (ViewPager)findViewById(R.id.view_pager);
        bottomNavigationBar.setMode(BottomNavigationBar.MODE_FIXED);
        bottomNavigationBar.setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_STATIC);
        bottomNavigationBar.addItem(new BottomNavigationItem(R.mipmap.tuijian_xuanzhong,"推荐").setInactiveIcon(ContextCompat.getDrawable(this,R.mipmap.tuijian_weixuanzhong)))
                .addItem(new BottomNavigationItem(R.mipmap.duanzi_xuanzhong,"段子").setInactiveIcon(ContextCompat.getDrawable(this,R.mipmap.duanzi_weixuanzhong)))
                .addItem(new BottomNavigationItem(R.mipmap.wenzhang_xuanzhong,"文章").setInactiveIcon(ContextCompat.getDrawable(this,R.mipmap.wenzhang_weixuanzhong)))
                .addItem(new BottomNavigationItem(R.mipmap.tuijian_xuanzhong,"图片").setInactiveIcon(ContextCompat.getDrawable(this,R.mipmap.tupian_weixuanzhong)))
                .initialise();
        bottomNavigationBar.setActiveColor(R.color.colorPrimary);
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
        switch (position){
            case 0 :
                toolbar.setTitle("首页");
                break;
            case 1:
                toolbar.setTitle("段子");
                break;
            case 2:
                toolbar.setTitle("文章");

                break;
            case 3:
                toolbar.setTitle("图片");
                break;
        }
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
