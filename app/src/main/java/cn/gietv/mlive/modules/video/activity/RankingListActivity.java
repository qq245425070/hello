package cn.gietv.mlive.modules.video.activity;

import android.os.Bundle;
import android.widget.ListView;

import cn.gietv.mlive.R;
import cn.gietv.mlive.base.AbsBaseActivity;
import cn.gietv.mlive.http.DefaultLiveHttpCallBack;
import cn.gietv.mlive.modules.video.adapter.RankingListAdapter;
import cn.gietv.mlive.modules.video.bean.BlackListBean;
import cn.gietv.mlive.modules.video.model.MessageModel;
import cn.gietv.mlive.utils.HeadViewController;
import cn.gietv.mlive.utils.ToastUtils;
import cn.kalading.android.retrofit.utils.RetrofitUtils;

public class RankingListActivity extends AbsBaseActivity {
    public static final String LIVE_NICKNAME = "live_nickname";
    public static final String LIVE_ROOM_ID = "room_id";
    private String mLiveNickname;
    private ListView mListView;
    private MessageModel mMessageModel;
    private String mRoomId;
    private RankingListAdapter mAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ranking_list);
        mLiveNickname = getIntent().getExtras().getString(LIVE_NICKNAME);
        mRoomId = getIntent().getExtras().getString(LIVE_ROOM_ID);
        HeadViewController.initHeadWithoutSearch(this,mLiveNickname + "的贡献排行榜");
        mListView = (ListView) findViewById(R.id.ranking_list);
        mMessageModel = RetrofitUtils.create(MessageModel.class);
        mMessageModel.getMqttTopic(mRoomId, new DefaultLiveHttpCallBack<BlackListBean>() {//获取Topic的接口，改为获取黑名单的接口
            @Override
            public void success(BlackListBean blackListBean) {
                System.out.println(blackListBean.contributionlist.size());
                mAdapter = new RankingListAdapter(RankingListActivity.this,blackListBean.contributionlist);
                mListView.setAdapter(mAdapter);
            }

            @Override
            public void failure(String message) {
                ToastUtils.showToast(RankingListActivity.this,message);
            }
        });
    }

}
