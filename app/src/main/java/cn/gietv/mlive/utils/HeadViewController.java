package cn.gietv.mlive.utils;

import android.app.Activity;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.UnsupportedEncodingException;

import cn.gietv.mlive.R;
import cn.gietv.mlive.modules.download.activity.DownloadOverAcitvity;
import cn.gietv.mlive.modules.login.activity.LoginActivity;
import cn.gietv.mlive.modules.search.activity.SearchActivity;
import cn.gietv.mlive.modules.search.activity.SearchResultActivity;
import cn.gietv.mlive.modules.usercenter.activity.TaskHelpActivity;
import cn.gietv.mlive.modules.usercenter.bean.UserCenterBean;
//import cn.gietv.scenelib5.SceneActivity;

/**
 * author：steven
 * datetime：15/10/8 21:27
 */
public class HeadViewController {
    public static void initSearchHead(final Activity activity, String title) {
        ImageButton exitButton = (ImageButton) activity.findViewById(cn.gietv.mlive.R.id.head_ib_exit);
        TextView titleText = (TextView) activity.findViewById(cn.gietv.mlive.R.id.head_tv_title);
        ImageButton searchButton = (ImageButton) activity.findViewById(cn.gietv.mlive.R.id.head_ib_search);
        titleText.setText(title);
        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.finish();
            }
        });
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentUtils.openActivity(activity, SearchActivity.class);
                activity.overridePendingTransition(R.anim.activity_open, R.anim.activity_close);
            }
        });
    }

    public static void initHeadWithoutSearch(final Activity activity, String title) {
        ImageButton exitButton = (ImageButton) activity.findViewById(cn.gietv.mlive.R.id.head_ib_exit);
        TextView titleText = (TextView) activity.findViewById(cn.gietv.mlive.R.id.head_tv_title);
        ImageButton searchButton = (ImageButton) activity.findViewById(cn.gietv.mlive.R.id.head_ib_search);
        if (StringUtils.isNotEmpty(title)) {
            titleText.setText(title);
        }
        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.finish();
            }
        });
        searchButton.setVisibility(View.INVISIBLE);
    }
    public static void initTaskHeader(final Activity activity,String title){
        ImageButton exitButton = (ImageButton) activity.findViewById(cn.gietv.mlive.R.id.head_ib_exit);
        TextView titleText = (TextView) activity.findViewById(cn.gietv.mlive.R.id.head_tv_title);
        ImageButton searchButton = (ImageButton) activity.findViewById(cn.gietv.mlive.R.id.head_ib_search);
        if (StringUtils.isNotEmpty(title)) {
            titleText.setText(title);
        }
        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.finish();
            }
        });
        searchButton.setImageBitmap(BitmapFactory.decodeResource(activity.getResources(), R.mipmap.level_help));
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentUtils.openActivity(activity, TaskHelpActivity.class);
            }
        });
    }

    public static void initHeadWithoutSearch(View view, final Activity activity, String title) {
        ImageButton exitButton = (ImageButton) view.findViewById(cn.gietv.mlive.R.id.head_ib_exit);
        TextView titleText = (TextView) view.findViewById(cn.gietv.mlive.R.id.head_tv_title);
        ImageButton searchButton = (ImageButton) view.findViewById(cn.gietv.mlive.R.id.head_ib_search);
        titleText.setText(title);
        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.finish();
            }
        });
        searchButton.setVisibility(View.INVISIBLE);
    }
    public static void initGameHeadWithoutSearch(View view, final Activity activity, String title,int color) {
        ImageButton exitButton = (ImageButton) view.findViewById(cn.gietv.mlive.R.id.head_ib_exit);
        TextView titleText = (TextView) view.findViewById(cn.gietv.mlive.R.id.head_tv_title);
        titleText.setTextColor(color);
        ImageButton searchButton = (ImageButton) view.findViewById(cn.gietv.mlive.R.id.head_ib_search);
        titleText.setText(title);
        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.onBackPressed();
            }
        });
        searchButton.setVisibility(View.INVISIBLE);
    }
    public static void initGameHeadWithoutSearch(View view, final Activity activity, String title) {
        ImageButton exitButton = (ImageButton) view.findViewById(cn.gietv.mlive.R.id.head_ib_exit);
        TextView titleText = (TextView) view.findViewById(cn.gietv.mlive.R.id.head_tv_title);
        ImageButton searchButton = (ImageButton) view.findViewById(cn.gietv.mlive.R.id.head_ib_search);
        titleText.setText(title);
        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.onBackPressed();
            }
        });
        searchButton.setVisibility(View.INVISIBLE);
    }

    public static void initSearchHead(final View view, final Activity activity, String title) {
        ImageButton exitButton = (ImageButton) view.findViewById(cn.gietv.mlive.R.id.head_ib_exit);
        TextView titleText = (TextView) view.findViewById(cn.gietv.mlive.R.id.head_tv_title);
        ImageButton searchButton = (ImageButton) view.findViewById(cn.gietv.mlive.R.id.head_ib_search);
        titleText.setText(title);
        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.onBackPressed();
            }
        });
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentUtils.openActivity(activity, SearchActivity.class);
                activity.overridePendingTransition(R.anim.activity_open, R.anim.activity_close);
            }
        });
    }

    public static void initSearchHeadWithoutReturn(final View view, final Activity activity, String title) {
        ImageButton exitButton = (ImageButton) view.findViewById(cn.gietv.mlive.R.id.head_ib_exit);
        TextView titleText = (TextView) view.findViewById(cn.gietv.mlive.R.id.head_tv_title);
        ImageButton searchButton = (ImageButton) view.findViewById(cn.gietv.mlive.R.id.head_ib_search);
        titleText.setText(title);
        exitButton.setVisibility(View.INVISIBLE);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentUtils.openActivity(activity, SearchActivity.class);
                activity.overridePendingTransition(R.anim.activity_open, R.anim.activity_close);
            }
        });
    }
    public static void initVRSearchHead(final View view, final Activity activity, String title) {
        ImageButton exitButton = (ImageButton) view.findViewById(cn.gietv.mlive.R.id.head_ib_exit);
        TextView titleText = (TextView) view.findViewById(cn.gietv.mlive.R.id.head_tv_title);
        ImageButton searchButton = (ImageButton) view.findViewById(cn.gietv.mlive.R.id.head_ib_search);
        titleText.setText(title);
        searchButton.setVisibility(View.INVISIBLE);
        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.finish();
            }
        });
//        searchButton.setImageResource(R.mipmap.vr_icon);
//        searchButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                IntentUtils.openActivity(activity, SceneActivity.class);
//            }
//        });
    }

    public static void setTitleText(final View view, String text) {
        TextView titleText = (TextView) view.findViewById(cn.gietv.mlive.R.id.head_tv_title);
        titleText.setText(text);
    }

    public static void setBlackMode(Activity activity) {
        LinearLayout headLayout = (LinearLayout) activity.findViewById(cn.gietv.mlive.R.id.head_ll_head);
        TextView titleText = (TextView) activity.findViewById(cn.gietv.mlive.R.id.head_tv_title);
        headLayout.setBackgroundColor(Color.parseColor("#353E45"));
        titleText.setTextColor(Color.WHITE);
        ImageButton searchButton = (ImageButton) activity.findViewById(cn.gietv.mlive.R.id.head_ib_search);
        searchButton.setVisibility(View.INVISIBLE);
    }

    //设置消息Fragment的头
    public static void setNewsPlus(final View view, final Activity activity, String title){
        ImageButton exitButton = (ImageButton) view.findViewById(cn.gietv.mlive.R.id.head_ib_exit);
        TextView titleText = (TextView) view.findViewById(cn.gietv.mlive.R.id.head_tv_title);
        ImageButton searchButton = (ImageButton) view.findViewById(cn.gietv.mlive.R.id.head_ib_search);
        exitButton.setVisibility(View.INVISIBLE);//隐藏退出图标
        titleText.setText(title);
        searchButton.setImageResource(R.mipmap.plus2);
        searchButton.setVisibility(View.INVISIBLE);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(UserUtils.isNotLogin()){
                    IntentUtils.openActivity(activity, LoginActivity.class);
                }else {
                    UserCenterBean.UserinfoEntity user = CacheUtils.getCacheUserInfo();
//                    UserListActivity.openUserListActivity(activity, user._id, UserListActivity.USER_MODEL_BE_ATTENTION, CompereListAdapter.CONCERN_OTHER, user.nickname, user.myfollow,UserListActivity.EXTRA_COME_IN_NEWS);
                    activity.overridePendingTransition(R.anim.activity_open,R.anim.activity_close);
                }
            }
        });
    }
    //设置消息详情页的头
    public static void setNewsActivity(final View view, final Activity activity, String title){
        ImageButton exitButton = (ImageButton) view.findViewById(cn.gietv.mlive.R.id.head_ib_exit);
        TextView titleText = (TextView) view.findViewById(cn.gietv.mlive.R.id.head_tv_title);
        ImageButton searchButton = (ImageButton) view.findViewById(cn.gietv.mlive.R.id.head_ib_search);
        titleText.setText(title);
        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.finish();
            }
        });
        searchButton.setVisibility(View.INVISIBLE);//隐藏搜索图片
        // searchButton.setImageBitmap(BitmapFactory.decodeResource(R.id.)); //为ImageButton换图片
    }
    //初始化照片Activity的头
    public static void initUpdatePhoto(final Activity activity, String title){
        ImageButton exitButton = (ImageButton) activity.findViewById(cn.gietv.mlive.R.id.head_ib_exit);
        TextView titleText = (TextView) activity.findViewById(cn.gietv.mlive.R.id.head_tv_title);
        ImageButton searchButton = (ImageButton) activity.findViewById(cn.gietv.mlive.R.id.head_ib_search);
        titleText.setText(title);
        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.finish();
            }
        });
       // searchButton.setVisibility(View.INVISIBLE);//隐藏搜索图片
        searchButton.setImageBitmap(BitmapFactory.decodeResource(activity.getResources(), cn.gietv.mlive.R.mipmap.delete_black));
    }
    public static void initHeadWithoutSearch2(final Activity activity , String title){
        ImageButton exitButton = (ImageButton) activity.findViewById(cn.gietv.mlive.R.id.head_ib_exit);
        TextView titleText = (TextView) activity.findViewById(cn.gietv.mlive.R.id.head_tv_title);
        ImageButton searchButton = (ImageButton) activity.findViewById(cn.gietv.mlive.R.id.head_ib_search);
        titleText.setText(title);
        searchButton.setVisibility(View.INVISIBLE);
        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.onBackPressed();
            }
        });
    }
    /**
     * 快速入口中作者和排行榜标题头的设置
     * 作者：houde on 2016/11/23 15:14
     * 邮箱：yangzhonghao@gietv.com
     */
    public static void initFastEntryHead(final Activity activity){
       ImageView mExitImage = (ImageView) activity.findViewById(R.id.head_ib_exit);
       ImageView mDownLoadImage = (ImageView) activity.findViewById(R.id.search_tv_search);
       final EditText mSearchEdit = (EditText) activity.findViewById(R.id.search_et_text);
        mExitImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.finish();
            }
        });
        mDownLoadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentUtils.openActivity(activity, DownloadOverAcitvity.class);
            }
        });
        mSearchEdit.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEND || (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                    switch (event.getAction()){
                        case KeyEvent.ACTION_UP:
                            //发送请求
                            seachBody(mSearchEdit,activity);
                            return true;
                        default:
                            return true;
                    }

                }
                return false;
            }
        });
    }

    private static void seachBody(EditText mSearchEdit,Activity activity) {
        String text = mSearchEdit.getText().toString();
        if (StringUtils.isNotEmpty(text)) {
            try {
                text = new String(text.getBytes(), "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }else{
            ToastUtils.showToast(activity,"输入内容后再搜素");
            return;
        }
        SearchResultActivity.openSearResultActivity(activity,text,false);
    }
}
