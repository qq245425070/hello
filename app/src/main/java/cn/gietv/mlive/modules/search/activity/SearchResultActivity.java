package cn.gietv.mlive.modules.search.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import java.io.UnsupportedEncodingException;

import cn.gietv.mlive.R;
import cn.gietv.mlive.base.AbsBaseActivity;
import cn.gietv.mlive.modules.search.adapter.SearchResultPagerAdapter;
import cn.gietv.mlive.utils.IntentUtils;
import cn.gietv.mlive.utils.StringUtils;
import cn.gietv.mlive.utils.ToastUtils;
import cn.gietv.mlive.views.PagerTab;

/**
 * Created by houde on 2016/6/5.
 */
public class SearchResultActivity extends AbsBaseActivity {
    private PagerTab mPagerTab;
    private ViewPager mViewPager;
    private SearchResultPagerAdapter mPagerAdapter;
    private String mSearchContent;
    private EditText mEditText;
    private TextView mSearchButton;
    private boolean flag = true;
    public static void openSearResultActivity(Context context,String searchContent,boolean isFinish){
        Bundle bundle = new Bundle();
        bundle.putString("content", searchContent);
        IntentUtils.openActivity(context, SearchResultActivity.class, bundle);
        if(isFinish)
            ((Activity)context).finish();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);
        mSearchContent = getIntent().getStringExtra("content");
        mEditText = (EditText) findViewById(R.id.search_et_text);
        mSearchButton = (TextView) findViewById(R.id.search_tv_search);
        mSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mEditText.setText(mSearchContent);
        setAdapter();
    }

    private void setAdapter() {
        mEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEND || (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                    switch (event.getAction()){
                        case KeyEvent.ACTION_UP:
                            //发送请求
                            seachBody();
                            return true;
                        default:
                            return true;
                    }
                }
                return false;
            }
        });
        mPagerTab = (PagerTab) findViewById(R.id.tab_indicator);
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mPagerAdapter = new SearchResultPagerAdapter(getSupportFragmentManager(),new String[]{"视频","专辑","作者"},mSearchContent);
        mViewPager.setAdapter(mPagerAdapter);
        mPagerTab.setViewPager(mViewPager);

    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0, R.anim.activity_close_exit);
    }
    private void seachBody() {
        if(!flag)
            return;
        String text = mEditText.getText().toString();
        if (StringUtils.isNotEmpty(text)) {
            try {
                text = new String(text.getBytes(), "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }else{
            ToastUtils.showToast(this, "输入内容后再搜素");
            return;
        }
        flag = false;
        SearchResultActivity.openSearResultActivity(this,text,true);
    }
}
