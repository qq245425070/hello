package cn.gietv.mlive.modules.news.fragment;


import android.app.Fragment;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.umeng.analytics.MobclickAgent;

import java.util.List;

import cn.gietv.mlive.R;
import cn.gietv.mlive.base.AbsBaseFragment;
import cn.gietv.mlive.constants.CacheConstants;
import cn.gietv.mlive.db.DBUtils;
import cn.gietv.mlive.modules.common.OnItemClick;
import cn.gietv.mlive.modules.news.activity.NewsInfoActivity;
import cn.gietv.mlive.modules.news.adapter.NewsListAdapter;
import cn.gietv.mlive.modules.news.bean.NewsBean;
import cn.gietv.mlive.modules.news.bean.RosterBean;
import cn.gietv.mlive.modules.usercenter.bean.UserCenterBean;
import cn.gietv.mlive.utils.CacheUtils;
import cn.gietv.mlive.utils.ConfigUtils;
import cn.gietv.mlive.utils.HeadViewController;
import cn.gietv.mlive.utils.IntentUtils;
import cn.gietv.mlive.utils.MLiveQueryHandler;
import cn.gietv.mlive.utils.Synchrodata;
import cn.gietv.mlive.utils.UserUtils;

/**
 * A simple {@link Fragment} subclass.
 */
public class NewsListFragment extends AbsBaseFragment implements OnItemClick<NewsBean> {

    public static final String USER_ID = "userID";
    public static final String MODEL = "model";
    private View mCurrentView;
    private ListView mListView;
    private NewsListAdapter mNewsAdapter;
    private Uri uri = Uri.parse("content://cn.gietv.mlive.RosterProvider/roster");
    private MLiveQueryHandler queryHandler;
    private String mUserId;

    public NewsListFragment getInstence(String userId,int model) {
        Bundle bundle = new Bundle();
        bundle.putString(USER_ID, userId);
        bundle.putInt(MODEL, model);
        NewsListFragment newsListFragment = new NewsListFragment();
        newsListFragment.setArguments(bundle);
        return newsListFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mCurrentView = inflater.inflate(R.layout.fragment_news, container, false);
        HeadViewController.setNewsPlus(mCurrentView, getActivity(), "消息");
        mListView = (ListView) mCurrentView.findViewById(R.id.news_list_lv_list);
        mNewsAdapter = new NewsListAdapter(getActivity(),null);
        mListView.setAdapter(mNewsAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                RosterBean rosterBean = rosterBeans.get(position);
                Log.e("ceshi",rosterBean.userId);
                NewsInfoActivity.openNewsInfoActivity(getActivity(), rosterBean.userId, rosterBean.nickname, rosterBean.avatar);
            }
        });
        return mCurrentView;
    }
    private List<RosterBean> rosterBeans;
    @Override
    public void onResume() {
        super.onResume();
        if(UserUtils.isNotLogin()) {
            mUserId = CacheUtils.getCache().getAsString(CacheConstants.CACHE_USERID);
            rosterBeans = DBUtils.getInstance(getActivity()).getSystemRoster(ConfigUtils.SYSTEM_MESSAGE_USERID);
        }else{
            UserCenterBean.UserinfoEntity user = CacheUtils.getCacheUserInfo();
            if(user == null){
                mUserId = CacheUtils.getCache().getAsString(CacheConstants.CACHE_USERID);
                rosterBeans = DBUtils.getInstance(getActivity()).getSystemRoster(ConfigUtils.SYSTEM_MESSAGE_USERID);
            }else {
                mUserId =user._id;
                rosterBeans = DBUtils.getInstance(getActivity()).getAllRoster(mUserId);
            }
        }
        //修改现在头像和以前头像不匹配的问题
        Synchrodata.getInstance(getActivity()).synchrodata(rosterBeans);
        getData();
        MobclickAgent.onPageStart("消息页");
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("消息页");
    }

    @Override
    public void onClick(NewsBean newsBean) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("newsBean", newsBean);
        IntentUtils.openActivity(getActivity(), NewsInfoActivity.class, bundle);
    }

    public void getData() {
        queryHandler = new MLiveQueryHandler(getActivity().getContentResolver());
        if(UserUtils.isNotLogin()){
            System.out.println("isNotLogin   true");
            queryHandler.startQuery(88, mNewsAdapter, uri, null, " userId = ?", new String[]{ConfigUtils.SYSTEM_MESSAGE_USERID}, "time desc");
            queryHandler.setOnQueryCompleteListener(new MLiveQueryHandler.IOnQueryCompleteListener() {
                @Override
                public void onQueryComplete(int token, Object cookie, Cursor cursor) {
                    if (cursor == null) {
                        return;
                    }
                    mNewsAdapter.changeCursor(cursor);
                }
            });
        }else {
            System.out.println("isNotLogin   false");
//            queryHandler.startQuery(88, mNewsAdapter, uri, null, null, null, "time desc");
            queryHandler.startQuery(88, mNewsAdapter, uri, null, "owner = ? or userId = ?", new String[]{mUserId, ConfigUtils.SYSTEM_MESSAGE_USERID}, "time desc");
            queryHandler.setOnQueryCompleteListener(new MLiveQueryHandler.IOnQueryCompleteListener() {
                @Override
                public void onQueryComplete(int token, Object cookie, Cursor cursor) {
                    if (cursor == null) {
                        return;
                    }
                    mNewsAdapter.changeCursor(cursor);
                    System.out.println(cursor.getCount());
                }
            });
            getActivity().getContentResolver().registerContentObserver(uri, true, new MyContentObserver(new Handler()));
        }

    }

    public class MyContentObserver extends ContentObserver {
        public MyContentObserver(Handler handler)
        {
            super(handler);
        }

        @Override
        public void onChange(boolean selfChange) {
            queryHandler.startQuery(88 , mNewsAdapter , uri ,null,"owner = ? or userId = ?",new String[]{mUserId,ConfigUtils.SYSTEM_MESSAGE_USERID},"time desc");//
            queryHandler.setOnQueryCompleteListener(new MLiveQueryHandler.IOnQueryCompleteListener() {
                @Override
                public void onQueryComplete(int token, Object cookie, Cursor cursor) {
                    mNewsAdapter.changeCursor(cursor);
                    System.out.println(cursor.getCount() + "onChange");
                    if(UserUtils.isNotLogin()) {
                        rosterBeans = DBUtils.getInstance(getActivity()).getSystemRoster(ConfigUtils.SYSTEM_MESSAGE_USERID);
                    }else{
                        rosterBeans = DBUtils.getInstance(getActivity()).getAllRoster(mUserId);
                    }
                }
            });
        }
    }
}
