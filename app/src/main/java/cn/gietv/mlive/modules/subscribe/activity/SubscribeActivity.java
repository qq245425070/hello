package cn.gietv.mlive.modules.subscribe.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.jcodecraeer.xrecyclerview.XRecyclerView;

import java.util.List;

import cn.gietv.mlive.R;
import cn.gietv.mlive.base.AbsBaseActivity;
import cn.gietv.mlive.constants.CommConstants;
import cn.gietv.mlive.db.DBUtils;
import cn.gietv.mlive.http.DefaultLiveHttpCallBack;
import cn.gietv.mlive.modules.follow.model.FollowModel;
import cn.gietv.mlive.modules.game.bean.GameInfoBean;
import cn.gietv.mlive.modules.game.model.GameModel;
import cn.gietv.mlive.modules.home.activity.HomeActivity;
import cn.gietv.mlive.modules.subscribe.adapter.SubscribeAdapter;
import cn.gietv.mlive.utils.HeadViewController;
import cn.gietv.mlive.utils.IntentUtils;
import cn.gietv.mlive.utils.SharedPreferenceUtils;
import cn.gietv.mlive.utils.ToastUtils;
import cn.gietv.mlive.utils.UserUtils;
import cn.kalading.android.retrofit.utils.RetrofitUtils;

/**
 * Created by houde on 2016/9/13.
 */
public class SubscribeActivity extends AbsBaseActivity implements View.OnClickListener{
    private XRecyclerView mRecyclerView;
    private GameModel mGameModel;
    private SubscribeAdapter mAdapter;
    private CheckBox mCheckBox;
    private TextView mSubscribeText,mLookRound;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subscribe);
        mRecyclerView = (XRecyclerView) findViewById(R.id.recycerview);
        HeadViewController.initHeadWithoutSearch2(this,"订阅");
        View headView = LayoutInflater.from(this).inflate(R.layout.subscribe_head,null);
        View footView = LayoutInflater.from(this).inflate(R.layout.subscribe_foot,null);
        mRecyclerView.addHeaderView(headView);
        mRecyclerView.addFootView(footView);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3);
        mRecyclerView.setLayoutManager(gridLayoutManager);
        mRecyclerView.setLoadingMoreEnabled(false);
        mRecyclerView.setPullRefreshEnabled(false);
        mGameModel = RetrofitUtils.create(GameModel.class);
        mCheckBox = (CheckBox) headView.findViewById(R.id.checkbox);
        mCheckBox.setChecked(true);
        mSubscribeText = (TextView) footView.findViewById(R.id.subscribe) ;
        mSubscribeText.setOnClickListener(this);
        mLookRound = (TextView) footView.findViewById(R.id.look_around) ;
        mLookRound.setOnClickListener(this);
        mCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    mAdapter.setCheckAll(true);
                    mCheckBox.setText("全选");
                }else{
                    mAdapter.setCheckAll(false);
                    mCheckBox.setText("全不选");
                }
            }
        });
        getData();
    }

    public void getData() {
        mGameModel.getAlbums("game", CommConstants.COMMON_PAGE_COUNT, 1, CommConstants.DEFAULT_SORT_ONLINE_PERSON, new DefaultLiveHttpCallBack<GameInfoBean>() {
            @Override
            public void success(GameInfoBean gameInfoBean) {
                if(isNotFinish()) {
                    if (gameInfoBean == null)
                        return;
                    mAdapter = new SubscribeAdapter(SubscribeActivity.this, gameInfoBean.games);
                    mRecyclerView.setAdapter(mAdapter);
                }
            }

            @Override
            public void failure(String message) {
                if(isNotFinish()){
                    ToastUtils.showToastShort(SubscribeActivity.this,message);
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        SharedPreferenceUtils.saveProp("is_first_subscribe",false);
        if(v.getId() == R.id.subscribe){
            if(mAdapter != null) {
                List<GameInfoBean.GameInfoEntity> games = mAdapter.getCheckGameList();
                GameInfoBean.GameInfoEntity game;
                if(UserUtils.isNotLogin()) {
                    DBUtils dbUtils = DBUtils.getInstance(this);
                    for (int i = 0; i < games.size(); i++) {
                        game = games.get(i);
                        dbUtils.saveArea(game._id, game.name,game.spic);
                    }
                    Intent intent = new Intent(this, HomeActivity.class);
                    startActivity(intent);
                    finish();
                }else{
                    StringBuilder sb = new StringBuilder();
                    for(int i = 0; i <games.size();i++){
                        game = games.get(i);
                        sb.append(game._id).append("#");
                    }
                    String ids = sb.toString();
                    if(ids.length()>0)
                        ids = ids.substring(0,ids.length()-1);
                    FollowModel model = RetrofitUtils.create(FollowModel.class);
                    model.followList(ids, CommConstants.CAROUSEL_TYPE_AREA, CommConstants.FOLLOW_TRUE, new DefaultLiveHttpCallBack<String>() {
                        @Override
                        public void success(String s) {
                            if(isNotFinish()) {
                                Intent intent = new Intent(SubscribeActivity.this, HomeActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        }

                        @Override
                        public void failure(String message) {
                            if(isNotFinish()){
                                Intent intent = new Intent(SubscribeActivity.this, HomeActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        }
                    });
                }
            }

        }else if(v.getId() == R.id.look_around){
            Intent intent = new Intent(this, HomeActivity.class);
            startActivity(intent);
            this.finish();
        }
    }

    @Override
    public void onBackPressed() {
        IntentUtils.openActivity(this,HomeActivity.class);
        super.onBackPressed();
    }
}
