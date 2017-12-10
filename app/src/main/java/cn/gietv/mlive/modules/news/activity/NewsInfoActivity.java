package cn.gietv.mlive.modules.news.activity;

import android.content.ContentValues;
import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;

import org.jivesoftware.smack.Chat;

import cn.gietv.mlive.MainApplication;
import cn.gietv.mlive.R;
import cn.gietv.mlive.base.AbsBaseActivity;
import cn.gietv.mlive.db.DBUtils;
import cn.gietv.mlive.modules.login.activity.LoginActivity;
import cn.gietv.mlive.modules.news.adapter.NewsInfoAdapter;
import cn.gietv.mlive.modules.news.bean.NewsBean;
import cn.gietv.mlive.modules.news.bean.RosterBean;
import cn.gietv.mlive.modules.usercenter.bean.UserCenterBean;
import cn.gietv.mlive.modules.xmpp.XmppConnection;
import cn.gietv.mlive.utils.CacheUtils;
import cn.gietv.mlive.utils.ConfigUtils;
import cn.gietv.mlive.utils.HeadViewController;
import cn.gietv.mlive.utils.InputUtils;
import cn.gietv.mlive.utils.IntentUtils;
import cn.gietv.mlive.utils.MLiveQueryHandler;
import cn.gietv.mlive.utils.StringUtils;
import cn.gietv.mlive.utils.ToastUtils;
import cn.gietv.mlive.utils.UserUtils;
import cn.gietv.mlive.views.MsgListView;
import cn.kalading.android.retrofit.utils.GsonUtils;

public class NewsInfoActivity extends AbsBaseActivity implements View.OnClickListener ,MsgListView.IXListViewListener {

    public static final String USER_ID = "userID";
    public static final String NICK_NAME = "nickname";
    public static final String AVATAR = "avatar";
    private NewsInfoAdapter mAdapter;
    private EditText mEditText;
    private MsgListView mMessageListView ;
    private TextView mSendText;
    private String userID;
    private String mNickname;
    private String mAvatar;
    private UserCenterBean.UserinfoEntity mUserBean;
    private Chat mChat;
    public static void openNewsInfoActivity(Context context ,String userID,String nickname,String avatar){
        Bundle bundle = new Bundle();
        bundle.putString("userID", userID);
        bundle.putString("nickname", nickname);
        bundle.putString("avatar", avatar);
        IntentUtils.openActivity(context, NewsInfoActivity.class, bundle);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_info);
        Bundle bundle = getIntent().getExtras();
        userID = getIntent().getExtras().getString(USER_ID);
        mNickname = getIntent().getExtras().getString(NICK_NAME);
        mAvatar = getIntent().getExtras().getString(AVATAR);
        final XmppConnection xmppconnection = XmppConnection.getInstance();
        if(xmppconnection.getXMPPConnection() != null && xmppconnection.getXMPPConnection().isConnected()){
            mChat = xmppconnection.getConnection().getChatManager().createChat(userID + "@" + xmppconnection.getConnection().getServiceName(), null);
        }else{
            new Thread(){
                @Override
                public void run() {
                    mChat = xmppconnection.getConnection().getChatManager().createChat(userID + "@" + xmppconnection.getConnection().getServiceName(), null);
                }
            }.start();
        }

        if(!UserUtils.isNotLogin()){
            mUserBean = CacheUtils.getCacheUserInfo();
        }
        DBUtils.getInstance(this).updateCount(userID);
        initView();
        getData();
    }

    private void initView() {
        HeadViewController.initHeadWithoutSearch(this, mNickname);
        mMessageListView = (MsgListView) findViewById(R.id.news_list_info);
        mEditText = (EditText) findViewById(R.id.news_et_content);
        mEditText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_UP) {
                    String message = mEditText.getText().toString();
                    if(StringUtils.isEmpty(message)){
                        ToastUtils.showToast(NewsInfoActivity.this,"请输入内容");
                        return true;
                    }else {
                        if(UserUtils.isNotLogin()){
                            IntentUtils.openActivity(NewsInfoActivity.this, LoginActivity.class);
                        }else {
                            mUserBean = CacheUtils.getCacheUserInfo();
                            sendMessage(message);
                            mEditText.setText("");
                        }
                    }
                    return true;
                }
                return false;
            }
        });
        mMessageListView.setPullLoadEnable(false);
        mMessageListView.setXListViewListener(this);
        mSendText = (TextView) findViewById(R.id.send_msg_button);
        mSendText.setOnClickListener(this);
        InputUtils.closeInputKeyBoard(mEditText);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.send_msg_button){
                String message = mEditText.getText().toString();
                if(StringUtils.isEmpty(message)){
                    ToastUtils.showToast(this,"请输入内容");
                    return;
                }else {
                    if(UserUtils.isNotLogin()){
                        IntentUtils.openActivity(this, LoginActivity.class);
                    }else {
                        mUserBean = CacheUtils.getCacheUserInfo();
                        sendMessage(message);
                        mEditText.setText("");
                    }
                }
        }
    }

    private void sendMessage(String message) {
        try {
            Gson gson = GsonUtils.getGson();
            NewsBean newsBean = new NewsBean();
            newsBean.nickname = mUserBean.nickname;
            newsBean.avatar = mUserBean.avatar;
            newsBean.userId = mUserBean._id;
            newsBean.time = System.currentTimeMillis();
            newsBean.message = message;
            newsBean.toOrFrom = mUserBean._id;
            newsBean.read = ConfigUtils.MESSAGE_NOT_READ;
            if(mChat != null)
                mChat.sendMessage(gson.toJson(newsBean));
            ContentValues contentValues = new ContentValues();
            contentValues.put("userId",newsBean.userId);
            contentValues.put("nickname" , newsBean.nickname);
            contentValues.put("avatar",newsBean.avatar);
            contentValues.put("message",newsBean.message);
            contentValues.put("from_me",ConfigUtils.FROM_ME);
            contentValues.put("read", ConfigUtils.MESSAGE_READ);
            contentValues.put("time",newsBean.time);
            contentValues.put("to_from", userID);
            contentValues.put("owner", newsBean.userId);
            getContentResolver().insert(uri, contentValues);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private Uri uri = Uri.parse("content://cn.gietv.mlive.NewsProvider/message");
    public void getData() {
        // 显示listview
        // 开子线程，读取数据库，
        // 获得内容以后，发送handler 信息更新主页面.
        queryHandler = new MLiveQueryHandler(getContentResolver());
        int count = DBUtils.getInstance(NewsInfoActivity.this).getMessageCount(userID);
        if(count > 20){
            count -= 20;
            page++;
            mMessageListView.setPullRefreshEnable(true);
        }else{
            count = 0;
            mMessageListView.setPullRefreshEnable(false);
        }
        queryHandler.startQuery(88, mAdapter, uri, null, "to_from = ?", new String[]{userID}, "time asc limit "+ count +" , 20");//"time asc limit " + count + " , 20"
        queryHandler.setOnQueryCompleteListener(new MLiveQueryHandler.IOnQueryCompleteListener() {
            @Override
            public void onQueryComplete(int token, Object cookie, Cursor cursor) {
                System.out.println("Cursor Count    " + cursor.getCount());
                mAdapter = new NewsInfoAdapter(NewsInfoActivity.this, cursor);
                mMessageListView.setAdapter(mAdapter);
                mMessageListView.setSelection(mAdapter.getCount() - 1);
            }
        });
       getContentResolver().registerContentObserver(uri, true, new MyContentObserver(new Handler()));
    }
    private MLiveQueryHandler queryHandler;

    @Override
    public void onRefresh() {
        if(page > 1){
            queryHandler.startQuery(88, mAdapter, uri, null, "to_from = ? ", new String[]{userID}, "time asc limit " + (DBUtils.getInstance(this).getMessageCount(userID) - page * 20) + "," + page * 20);//
            queryHandler.setOnQueryCompleteListener(new MLiveQueryHandler.IOnQueryCompleteListener() {
                @Override
                public void onQueryComplete(int token, Object cookie, Cursor cursor) {
                    int count = mAdapter.getCount();
                    mAdapter.changeCursor(cursor);
                    mMessageListView.setSelection(cursor.getCount() - count);
                    if(page * 20 == cursor.getCount()){
                        page ++;
                    }else{
                        page = 1;
                        mMessageListView.setPullRefreshEnable(false);
                    }
                }
            });
        }
       mMessageListView.stopRefresh();
    }

    private int page = 1;
    @Override
    public void onLoadMore() {

    }

    @Override
    protected void onPause() {
        super.onPause();
        if(mAdapter != null) {
            Cursor cursor = mAdapter.getCursor();
            if(cursor != null && cursor.moveToLast()){
                RosterBean rosterBean = new RosterBean();
                rosterBean.userId = userID;
                rosterBean.avatar = mAvatar;
                rosterBean.nickname = mNickname;
                rosterBean.time = cursor.getLong(4);
                rosterBean.count = 0;
                rosterBean.message = cursor.getString(5);
                rosterBean.owner = CacheUtils.getCacheUserInfo() ==null ? ConfigUtils.SYSTEM_MESSAGE_USERID:CacheUtils.getCacheUserInfo()._id;
                DBUtils.getInstance(this).saveRoster(rosterBean);
            }
        }
    }

    public class MyContentObserver extends ContentObserver {
        public MyContentObserver(Handler handler)
        {
            super(handler);
        }

        @Override
        public void onChange(boolean selfChange) {
            int count = DBUtils.getInstance(MainApplication.getInstance()).getMessageCount(userID);
            if(count > 20){
                count -= 20;
            }else{
                count = 0;
            }
            queryHandler.startQuery(88 , mAdapter , uri , null , "to_from = ? " ,new String[]{userID},"time asc limit "+ count +" , 20");//
            queryHandler.setOnQueryCompleteListener(new MLiveQueryHandler.IOnQueryCompleteListener() {
                @Override
                public void onQueryComplete(int token, Object cookie, Cursor cursor) {
                    mAdapter.changeCursor(cursor);
                    mMessageListView.setSelection(mAdapter.getCount() - 1);
                    if(cursor.getCount() == 20){
                        page = 2;
                    }
                }
            });
        }
    }
}
