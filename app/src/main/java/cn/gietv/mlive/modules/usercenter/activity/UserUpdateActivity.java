package cn.gietv.mlive.modules.usercenter.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.File;

import cn.gietv.mlive.R;
import cn.gietv.mlive.base.AbsBaseActivity;
import cn.gietv.mlive.http.DefaultLiveHttpCallBack;
import cn.gietv.mlive.modules.login.activity.LoginActivity;
import cn.gietv.mlive.modules.photo.activity.PhotoUpdateActivity;
import cn.gietv.mlive.modules.usercenter.bean.UserCenterBean;
import cn.gietv.mlive.modules.usercenter.model.UserCenterModel;
import cn.gietv.mlive.utils.CacheUtils;
import cn.gietv.mlive.utils.HeadViewController;
import cn.gietv.mlive.utils.ImageLoaderUtils;
import cn.gietv.mlive.utils.IntentUtils;
import cn.gietv.mlive.utils.ProgressDialogUtils;
import cn.gietv.mlive.utils.ToastUtils;
import cn.gietv.mlive.utils.UserUtils;
import cn.kalading.android.retrofit.utils.RetrofitUtils;
import retrofit.mime.TypedFile;

/**
 * author：steven
 * datetime：15/10/11 21:13
 */
public class UserUpdateActivity extends AbsBaseActivity implements View.OnClickListener {
    public static final String EXTRA_USER_INFO = "extra_user_info";
    private UserCenterBean.UserinfoEntity mUserInfoBean;
    private ProgressDialog dialog;
    private ImageLoader mImageLoader;
    private LinearLayout mAvatarParent,mNicknameParent,mPhoneParent,mEmailParent,mPhotoParent,mAcoinParent,mJinjiaoParent,mTaskParent,mDescParent;
    private ImageView mAvatar,mPhoneArrow,mEmailArrow;
    private UserCenterModel mModel;
    private TextView mNickname,mTelephone,mEmail,mJinjiaoCount,mAcoinCount,mTelephoneDesc,mDescText;
    private void initView() {
        mAvatar = (ImageView) findViewById(R.id.avatar_image);
        mNickname = (TextView) findViewById(R.id.nickname);
        mTelephone = (TextView) findViewById(R.id.telephone);
        mEmail = (TextView) findViewById(R.id.email);
        mJinjiaoCount = (TextView) findViewById(R.id.jinjiao_count);
        mAcoinCount = (TextView) findViewById(R.id.acoin_count);
        mAvatarParent = (LinearLayout) findViewById(R.id.avatar_parent);
        mNicknameParent = (LinearLayout) findViewById(R.id.nickname_parent);
        mPhoneParent = (LinearLayout) findViewById(R.id.telephone_parent);
        mEmailParent = (LinearLayout) findViewById(R.id.email_parent);
        mPhotoParent = (LinearLayout) findViewById(R.id.photo_parent);
        mAcoinParent = (LinearLayout) findViewById(R.id.acoin_parent);
        mJinjiaoParent = (LinearLayout) findViewById(R.id.jinjiao_parent);
        mTaskParent = (LinearLayout) findViewById(R.id.task_parent);
        mPhoneArrow = (ImageView) findViewById(R.id.phone_arrow);
        mEmailArrow = (ImageView) findViewById(R.id.email_arrow);
        mDescParent = (LinearLayout) findViewById(R.id.desc_parent);
        mDescText = (TextView)findViewById(R.id.desc);
        mTelephoneDesc = (TextView) findViewById(R.id.telephone_desc);
        mImageLoader.displayImage(mUserInfoBean.avatar, mAvatar);
        mAvatarParent.setOnClickListener(this);
        mNicknameParent.setOnClickListener(this);
        mPhotoParent.setOnClickListener(this);
        mAcoinParent.setOnClickListener(this);
        mJinjiaoParent.setOnClickListener(this);
        mTaskParent.setOnClickListener(this);
        mDescParent.setOnClickListener(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_user_info_layout);
        HeadViewController.initHeadWithoutSearch(this, "个人账户信息");
        mImageLoader = ImageLoaderUtils.getDefaultImageLoader(this);
        mUserInfoBean = (UserCenterBean.UserinfoEntity) getIntent().getExtras().getSerializable(EXTRA_USER_INFO);
        initView();
        mModel = RetrofitUtils.create(UserCenterModel.class);

    }

    @Override
    protected void onResume() {
        super.onResume();
        mUserInfoBean = CacheUtils.getCacheUserInfo();
        mImageLoader.displayImage(mUserInfoBean.avatar, mAvatar);
        mNickname.setText(mUserInfoBean.nickname);
        mAcoinCount.setText(mUserInfoBean.mycoin + "");
        mJinjiaoCount.setText(mUserInfoBean.myjinjiao + "");
        if(TextUtils.isEmpty(mUserInfoBean.email)){
            mEmailParent.setOnClickListener(this);
        }else {
            mEmailArrow.setVisibility(View.INVISIBLE);
            mEmailParent.setClickable(false);
        }
        mEmail.setText(mUserInfoBean.email);
        if(TextUtils.isEmpty(mUserInfoBean.mobilephone)){
            mPhoneParent.setOnClickListener(this);
        }else{
            mPhoneArrow.setVisibility(View.INVISIBLE);
            mTelephoneDesc.setVisibility(View.GONE);
            mPhoneParent.setClickable(false);
        }
        String replaceString = "";
        if(mUserInfoBean.mobilephone != null && mUserInfoBean.mobilephone.length()> 8) {
            replaceString = mUserInfoBean.mobilephone.substring(3, 7);
            mTelephone.setText(mUserInfoBean.mobilephone.replace(replaceString,"****"));
        }else{
            mTelephone.setText(mUserInfoBean.mobilephone);
        }
        mDescText.setText(mUserInfoBean.desc);
        mNickname.setText(mUserInfoBean.nickname);
        mAcoinCount.setText(mUserInfoBean.mycoin + "");
        mJinjiaoCount.setText(mUserInfoBean.myjinjiao + "");
    }

    private void updateUserInfo() {
     /*   String nickname = mNicknameEdit.getText().toString();
        String desc = mDescEdit.getText().toString();
        int gender = mGirlButton.isChecked() ? 0 : 1;
        if (StringUtils.isEmpty(nickname)) {
            ToastUtils.showToast(this, "昵称不能为空");
            return;
        }
        *//*else{
            String check = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
            Pattern regex = Pattern.compile(check);
            Matcher matcher = regex.matcher(email);
            boolean isMatched = matcher.matches();
            if(!isMatched){
                ToastUtils.showToast(this,"请输入正确的邮箱");
                return;
            }
        }*//*
        if (StringUtils.isEmpty(desc)) {
            ToastUtils.showToast(this, "个人简介不能为空");
            return;
        }*/
     /*   final ProgressDialog dialog = ProgressDialogUtils.createShowDialog(UserUpdateActivity.this, "正在修改个人信息，请稍后");
        mModel.updateUserInfo(nickname, desc, gender, mUserInfoBean.email, new DefaultLiveHttpCallBack<UserCenterBean.UserinfoEntity>() {
            @Override
            public void success(UserCenterBean.UserinfoEntity userinfoEntity) {
                if (isNotFinish()) {
                    ToastUtils.showToast(UserUpdateActivity.this, "个人信息修改成功");
                    //第一次修改个人信息，赠送游戏币
                    //NoviceTaskUtils.doNoviceTask(ConfigUtils.FIRST_EDIT_INFO, UserUpdateActivity.this);
                    CacheUtils.saveUserInfo(userinfoEntity);
                    dialog.dismiss();
                    finish();
                }
            }

            @Override
            public void failure(String message) {
                if (isNotFinish()) {
                    ToastUtils.showToast(UserUpdateActivity.this, message);
                    dialog.dismiss();
                }
            }
        });*/
    }

    private void addHeadPhoto(String path) {
        File file = new File(path);
        TypedFile typedFile = new TypedFile("image/jpg", file);
        dialog = ProgressDialogUtils.createShowDialog(this, "正在上传头像，请稍后");
        mModel.changeHeadImage(typedFile, new DefaultLiveHttpCallBack<String>() {
            @Override
            public void success(String s) {
                if (isNotFinish()) {
                    dialog.dismiss();
                    ToastUtils.showToast(UserUpdateActivity.this, "头像上传成功");
                    mImageLoader.displayImage(mUserInfoBean.avatar, mAvatar);
                    //向数据库中保存上传头像成功的信息
                    /*if(NoviceTaskUtils.getNewTaskData(ConfigUtils.TASK_NEW_IMG,UserUpdateActivity.this) == -1) {
                        NoviceTaskUtils.editTaskCount(ConfigUtils.TASK_NEW_IMG, UserUpdateActivity.this, 0, 0);
                    }*/
                }
            }

            @Override
            public void failure(String message) {
                if (isNotFinish()) {
                    dialog.dismiss();
                    ToastUtils.showToast(UserUpdateActivity.this, "头像上传失败，请稍后重试");
                }
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0 && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            Cursor cursor = null;
            try {
                cursor = getContentResolver().query(selectedImage,
                        filePathColumn, null, null, null);
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                String picturePath = cursor.getString(columnIndex);
                addHeadPhoto(picturePath);
            } catch (Exception e) {
                e.printStackTrace();
                String picturePath = selectedImage.toString().replace("file://","");
                addHeadPhoto(picturePath);
            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }
        } else if (requestCode == 1 && resultCode == RESULT_OK) {
            finish();
        }
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.avatar_parent) {
                Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(i, 0);
        } else if (R.id.photo_parent == view.getId()){
            IntentUtils.openActivity(this, PhotoUpdateActivity.class);
        }else if(R.id.nickname_parent == view.getId()) {
            EditNicknameActivity.getInstance(this, CacheUtils.getCacheUserInfo());
        }else if(view.getId() == R.id.telephone_parent) {
            IntentUtils.openActivity(this, BindingPhoneActivity.class);
          //  finish();
        }else if(view.getId() == R.id.task_parent) {
            IntentUtils.openActivity(this, TaskActivity.class);
        }else if(view.getId() == R.id.acoin_parent) {
            IntentUtils.openActivity(this, TaskActivity.class);
        }else if(view.getId() == R.id.email_parent) {
            IntentUtils.openActivity(this, BindingEmailActivity.class);
        }else if(view.getId() == R.id.jinjiao_parent){
            if(UserUtils.isNotLogin()){
                IntentUtils.openActivity(this, LoginActivity.class);
            }else{
                IntentUtils.openActivity(this,RechargeActivity.class);
            }
        }else if(view.getId() == R.id.desc_parent){
            if(UserUtils.isNotLogin()){
                IntentUtils.openActivity(this, LoginActivity.class);
            }else{
                IntentUtils.openActivity(this,EditDescActivity.class);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }
}
