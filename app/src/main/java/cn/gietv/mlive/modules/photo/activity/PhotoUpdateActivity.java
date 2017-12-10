package cn.gietv.mlive.modules.photo.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import cn.gietv.mlive.R;
import cn.gietv.mlive.base.AbsBaseActivity;
import cn.gietv.mlive.constants.CacheConstants;
import cn.gietv.mlive.http.DefaultLiveHttpCallBack;
import cn.gietv.mlive.modules.photo.adapter.PhotoUpdateAdapter;
import cn.gietv.mlive.modules.photo.bean.PhotoBean;
import cn.gietv.mlive.modules.photo.model.PhotoModel;
import cn.gietv.mlive.utils.CacheUtils;
import cn.gietv.mlive.utils.HeadViewController;
import cn.gietv.mlive.utils.ProgressDialogUtils;
import cn.gietv.mlive.utils.StringUtils;
import cn.gietv.mlive.utils.ToastUtils;
import cn.kalading.android.retrofit.utils.RetrofitUtils;
import retrofit.mime.TypedFile;

/**
 * author：steven
 * datetime：15/10/20 19:24
 *
 */
public class PhotoUpdateActivity extends AbsBaseActivity implements View.OnClickListener {
    public static final int RESULT_LOAD_IMAGE = 0;
    private ImageView mDeleteImage;// mAddImage,
    private GridView mGridView;
    private List<PhotoBean.PhotosEntity> mPhotoList = new ArrayList<>();
    private PhotoUpdateAdapter mAdapter;
    private PhotoModel mPhotoModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.photo_update_layout);
        HeadViewController.initUpdatePhoto(this, "照片");
      //  mAddImage = (ImageView) findViewById(R.id.photo_update_iv_add);
        //mDeleteImage = (ImageView) findViewById(R.id.photo_update_iv_delete);
        mDeleteImage = (ImageButton) findViewById(R.id.head_ib_search);
        mGridView = (GridView) findViewById(R.id.photo_update_gv_list);
        mPhotoModel = RetrofitUtils.create(PhotoModel.class);

        mAdapter = new PhotoUpdateAdapter(this, mPhotoList);
        mGridView.setAdapter(mAdapter);
       // mAddImage.setOnClickListener(this);
        mDeleteImage.setOnClickListener(this);
        getData();
    }

    private void getData() {
        mPhotoModel.getUserPhoto(CacheUtils.getCache().getAsString(CacheConstants.CACHE_USERID), new DefaultLiveHttpCallBack<PhotoBean>() {
            @Override
            public void success(PhotoBean photoBean) {
                if (isNotFinish()) {
                    mPhotoList.clear();
                    mPhotoList.addAll(photoBean.photos);
                    mAdapter.notifyDataSetChanged();
                    mAdapter.clearChecked();
                }
            }

            @Override
            public void failure(String message) {
                if (isNotFinish()) {
                    ToastUtils.showToast(PhotoUpdateActivity.this,message);
                }
            }
        });
    }

    private void addPhoto(String path) {
        File file = new File(path);
        TypedFile typedFile = new TypedFile("image/jpg", file);
        final ProgressDialog dialog = ProgressDialogUtils.createShowDialog(this, "正在上传照片，请稍后");
        mPhotoModel.addPhoto(typedFile, new DefaultLiveHttpCallBack<String>() {
            @Override
            public void success(String s) {
                if (isNotFinish()) {
                    dialog.dismiss();
                    ToastUtils.showToast(PhotoUpdateActivity.this, "照片上传成功");
                    getData();
                }
            }

            @Override
            public void failure(String message) {
                if (isNotFinish()) {
                    dialog.dismiss();
                    ToastUtils.showToast(PhotoUpdateActivity.this, message);
                }
            }
        });
    }

    private void deletePhoto() {
        final Set<String> set = mAdapter.getChecked();
        if (set.isEmpty()) {
            ToastUtils.showToast(PhotoUpdateActivity.this, "请选择照片");
            return;
        }
        String urls = StringUtils.join(set.iterator(), ";");
        final ProgressDialog dialog = ProgressDialogUtils.createShowDialog(this, "正在删除照片，请稍后");
        mPhotoModel.deletePhoto(urls, new DefaultLiveHttpCallBack<String>() {
            @Override
            public void success(String s) {
                if (isNotFinish()) {
                    dialog.dismiss();
                    ToastUtils.showToast(PhotoUpdateActivity.this, "已删除 " + set.size() + " 张照片");
                    getData();
                }
            }

            @Override
            public void failure(String message) {
                if (isNotFinish()) {
                    dialog.dismiss();
                    ToastUtils.showToast(PhotoUpdateActivity.this, message);
                }
            }
        });
    }


    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.head_ib_search) {
                deletePhoto();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            System.out.println(selectedImage.toString());
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            Cursor cursor = null;
            try {
                cursor = getContentResolver().query(selectedImage,
                        filePathColumn, null, null, null);
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                String picturePath = cursor.getString(columnIndex);
                System.out.println("picturePath = " + picturePath);
                addPhoto(picturePath);
            } catch (Exception e) {
                e.printStackTrace();
                addPhoto(selectedImage.toString().replace("file://",""));
            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }
        }
    }
}
