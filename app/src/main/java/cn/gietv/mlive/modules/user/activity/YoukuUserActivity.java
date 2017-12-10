package cn.gietv.mlive.modules.user.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import cn.gietv.mlive.R;
import cn.gietv.mlive.base.AbsBaseActivity;
import cn.gietv.mlive.modules.usercenter.bean.UserCenterBean;
import cn.gietv.mlive.utils.HeadViewController;
import cn.gietv.mlive.utils.ImageLoaderUtils;

/**
 * 作者：houde on 2016/12/26 17:26
 * 邮箱：yangzhonghao@gietv.com
 */
public class YoukuUserActivity extends AbsBaseActivity {
    private UserCenterBean.UserinfoEntity mUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_youku_user);
        mUser = (UserCenterBean.UserinfoEntity) getIntent().getSerializableExtra("user");
        ImageView avatarImage = (ImageView) findViewById(R.id.user_icon);
        LinearLayout myConcern = (LinearLayout) findViewById(R.id.commen_parent);
        LinearLayout fansParent = (LinearLayout) findViewById(R.id.fans_parent);
        LinearLayout photoParent = (LinearLayout) findViewById(R.id.photo_parent);
        TextView followText = (TextView) findViewById(R.id.follow_text);
        TextView fansText = (TextView) findViewById(R.id.fans_text);
        TextView photoText = (TextView) findViewById(R.id.photo_text);
        findViewById(R.id.private_news).setVisibility(View.INVISIBLE);
        findViewById(R.id.concern).setVisibility(View.INVISIBLE);
        ImageLoader imageLoader = ImageLoaderUtils.getDefaultImageLoader(this);
        if(mUser != null){
            HeadViewController.initHeadWithoutSearch(this,mUser.nickname);
            imageLoader.displayImage(mUser.avatar,avatarImage);
            photoText.setText(String.valueOf(mUser.photo));
            fansText.setText(String.valueOf(mUser.follows));
            followText.setText(String.valueOf(mUser.myfollow));
        }
    }
}
