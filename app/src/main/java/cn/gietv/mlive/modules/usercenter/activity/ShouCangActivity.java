package cn.gietv.mlive.modules.usercenter.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import cn.gietv.mlive.R;
import cn.gietv.mlive.base.AbsBaseActivity;
import cn.gietv.mlive.http.DefaultLiveHttpCallBack;
import cn.gietv.mlive.modules.follow.model.FollowModel;
import cn.gietv.mlive.modules.program.bean.ProgramBean;
import cn.gietv.mlive.modules.usercenter.adapter.ShouCangAdapter;
import cn.gietv.mlive.utils.IntentUtils;
import cn.kalading.android.retrofit.utils.RetrofitUtils;

public class ShouCangActivity extends AbsBaseActivity implements View.OnClickListener {
    public static String EXE_DATA = "data";
    private List<ProgramBean.ProgramEntity> mVideoProgramList;
    private ShouCangAdapter mAdapter;
    private XRecyclerView mListView;
    private FollowModel mFollowModel;
    private TextView mNoContent;
    private ImageButton mSearchButton;
    private ImageButton mExitButton;
    private TextView mTitleText;
    private TextView mSubmit;
    public static void openShouCangActivity(Context context, List<ProgramBean.ProgramEntity> programEntities,String nickname){
        Bundle bundle = new Bundle();
        bundle.putSerializable(EXE_DATA, (Serializable) programEntities);
        bundle.putString("nickname",nickname);
        IntentUtils.openActivity(context,ShouCangActivity.class,bundle);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shou_cang);
        mVideoProgramList = (List<ProgramBean.ProgramEntity>) getIntent().getExtras().getSerializable(EXE_DATA);
        String userid = getIntent().getExtras().getString("nickname");
        mListView = (XRecyclerView) findViewById(R.id.recyclerview);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mListView.setLayoutManager(layoutManager);

        mListView.setRefreshProgressStyle(ProgressStyle.SysProgress);
        mListView.setLaodingMoreProgressStyle(ProgressStyle.SysProgress);
        mListView.setArrowImageView(R.drawable.iconfont_downgrey);
        mExitButton = (ImageButton) findViewById(R.id.head_ib_exit);
        mTitleText = (TextView) findViewById(R.id.head_tv_title);
        mTitleText.setText("我的收藏");
        mNoContent = (TextView) findViewById(R.id.no_content);
        mSearchButton = (ImageButton)findViewById(R.id.head_ib_search);
        if(userid != null){
            mSearchButton.setVisibility(View.INVISIBLE);
            mTitleText.setText("他的收藏");
        }
        mSubmit = (TextView) findViewById(R.id.submit);
        mSearchButton.setOnClickListener(this);
        mExitButton.setOnClickListener(this);
        mSubmit.setOnClickListener(this);
        mAdapter = new ShouCangAdapter(this,mVideoProgramList);
        mListView.setAdapter(mAdapter);
        mFollowModel = RetrofitUtils.create(FollowModel.class);
        if(mVideoProgramList == null || mVideoProgramList.size() ==0 ){
            mNoContent.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.head_ib_exit) {
            this.finish();
            return;
        } else if (v.getId() == R.id.head_ib_search) {
            if (mAdapter.getType() == 0) {
                mAdapter.setType(1);
                mAdapter.notifyDataSetChanged();
                mSubmit.setVisibility(View.VISIBLE);
                mSearchButton.setVisibility(View.GONE);
            }
        }else if (v.getId() == R.id.submit){
            mSearchButton.setVisibility(View.VISIBLE);
            mSubmit.setVisibility(View.GONE);
                if (mVideoProgramList != null && mVideoProgramList.size() > 0) {
                    List<ProgramBean.ProgramEntity> beans = new ArrayList<>();
                    for (int i = 0; i < mVideoProgramList.size(); i++) {
                        if (mVideoProgramList.get(i).check) {
                            beans.add(mVideoProgramList.get(i));
                        }
                    }
                    for (int i = 0; i < beans.size(); i++) {
                        mVideoProgramList.remove(beans.get(i));
                        mFollowModel.followByContent(beans.get(i)._id, 2, 2, new DefaultLiveHttpCallBack<String>() {
                            @Override
                            public void success(String s) {

                            }

                            @Override
                            public void failure(String message) {

                            }
                        });
                    }
                    mAdapter.setType(0);
                    mAdapter.notifyDataSetChanged();
                    if (mVideoProgramList == null || mVideoProgramList.size() == 0) {
                        mNoContent.setVisibility(View.VISIBLE);
                    }
                }
        }
    }
}
