package cn.gietv.mlive.modules.search.activity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.jcodecraeer.xrecyclerview.XRecyclerView;

import java.util.ArrayList;
import java.util.List;

import cn.gietv.mlive.R;
import cn.gietv.mlive.base.AbsBaseActivity;
import cn.gietv.mlive.http.DefaultLiveHttpCallBack;
import cn.gietv.mlive.modules.category.adapter.CategoryAdapter;
import cn.gietv.mlive.modules.search.bean.TagSearchBean;
import cn.gietv.mlive.modules.search.model.SearchModel;
import cn.gietv.mlive.utils.HeadViewController;
import cn.gietv.mlive.utils.IntentUtils;
import cn.gietv.mlive.utils.ToastUtils;
import cn.kalading.android.retrofit.utils.RetrofitUtils;

/**
 * Created by houde on 2016/5/31.
 */
public class TagSearchActivity extends AbsBaseActivity {
    private XRecyclerView mListView;
    private int type;
    private List<Object> datas;
    private String mTag;
    public static void openTagSearchActivity(Context context,String tag,int type){
        Bundle bundle = new Bundle();
        bundle.putString("tag",tag);
        bundle.putInt("type",type);
        IntentUtils.openActivity(context,TagSearchActivity.class,bundle);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_layout);
        findViewById(R.id.search_head).setVisibility(View.GONE);
        mListView = (XRecyclerView) findViewById(R.id.search_lv_list);
        type =  getIntent().getIntExtra("type", 0);
        mTag = getIntent().getStringExtra("tag");
        datas = new ArrayList<>();
        HeadViewController.initHeadWithoutSearch(this, mTag);
        getData();
    }
    private void getData(){
        SearchModel model = RetrofitUtils.create(SearchModel.class);
        model.getListByTag(mTag,type, 9999, 1, 1, new DefaultLiveHttpCallBack<TagSearchBean>() {
            @Override
            public void success(TagSearchBean tagSearchBean) {
                if(isNotFinish()) {
                    switch (type) {
                        case 2:
                            if (tagSearchBean.programs != null && tagSearchBean.programs.size() > 0) {
                                datas.clear();
                                datas.addAll(tagSearchBean.programs);
                                mListView.setAdapter(new CategoryAdapter(TagSearchActivity.this, datas, 0));
                            }
                            break;
                        case 7:
                            if (tagSearchBean.albumlist != null && tagSearchBean.albumlist.size() > 0) {
                                datas.clear();
                                datas.addAll(tagSearchBean.albumlist);
                                mListView.setAdapter(new CategoryAdapter(TagSearchActivity.this, datas, 0));
                            }
                            break;
                        case 8:
                            if (tagSearchBean.authorlist != null && tagSearchBean.authorlist.size() > 0) {
                                datas.clear();
                                datas.addAll(tagSearchBean.authorlist);
                                mListView.setAdapter(new CategoryAdapter(TagSearchActivity.this, datas, 0));
                            }
                            break;
                    }
                }
            }

            @Override
            public void failure(String message) {
                if(isNotFinish())
                    ToastUtils.showToastShort(TagSearchActivity.this,message);
            }
        });
    }
}
