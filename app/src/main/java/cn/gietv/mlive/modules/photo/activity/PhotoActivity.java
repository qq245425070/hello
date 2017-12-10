package cn.gietv.mlive.modules.photo.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.GridView;
import android.widget.TextView;

import cn.gietv.mlive.R;
import cn.gietv.mlive.base.AbsBaseActivity;
import cn.gietv.mlive.http.DefaultLiveHttpCallBack;
import cn.gietv.mlive.modules.photo.adapter.PhotoAdapter;
import cn.gietv.mlive.modules.photo.bean.PhotoBean;
import cn.gietv.mlive.modules.photo.model.PhotoModel;
import cn.gietv.mlive.utils.HeadViewController;
import cn.gietv.mlive.utils.IntentUtils;
import cn.gietv.mlive.utils.ToastUtils;
import cn.kalading.android.retrofit.utils.RetrofitUtils;

/**
 * author：steven
 * datetime：15/10/16 23:56
 *
 */
public class PhotoActivity extends AbsBaseActivity {
    public static final String EXTRA_USER_ID = "extra_user_id";
    public static final String EXTRA_USER_NICKNAME = "extra_user_nickname";
    private String mUserId;
    private GridView mGridView;
    private PhotoAdapter mAdapter;
    private PhotoModel mPhotoModel;
    private String mNickname;
    private TextView mMessageText ;

    public static void openPhotoActivity(Activity activity, String userId,String nickname) {
        Bundle bundle = new Bundle();
        bundle.putString("extra_user_id", userId);
        bundle.putString("extra_user_nickname",nickname);
        IntentUtils.openActivity(activity, PhotoActivity.class, bundle);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.photo_view_layout);
        HeadViewController.initHeadWithoutSearch(this, "照片");
        mUserId = getIntent().getExtras().getString(EXTRA_USER_ID);
        mNickname = getIntent().getExtras().getString(EXTRA_USER_NICKNAME);
        mPhotoModel = RetrofitUtils.create(PhotoModel.class);
        mGridView = (GridView) findViewById(R.id.photo_gv_list);
        mMessageText = (TextView) findViewById(R.id.message);
        getData();
    }

    private void getData() {
        mPhotoModel.getUserPhoto(mUserId, new DefaultLiveHttpCallBack<PhotoBean>() {
            @Override
            public void success(PhotoBean photoBean) {
                if (isNotFinish()) {
                    if (photoBean.photos != null && photoBean.photos.size() > 0) {
                        mMessageText.setVisibility(View.INVISIBLE);
                        mAdapter = new PhotoAdapter(PhotoActivity.this, photoBean.photos);
                        mGridView.setAdapter(mAdapter);
                    }else{
                        mGridView.setVisibility(View.INVISIBLE);
                        mMessageText.setVisibility(View.VISIBLE);
                        mMessageText.setText(mNickname + "还没有上传过相片");
                    }
                }
            }

            @Override
            public void failure(String message) {
                if (isNotFinish()) {
                    ToastUtils.showToast(PhotoActivity.this,message);
                }
            }
        });
    }
}
