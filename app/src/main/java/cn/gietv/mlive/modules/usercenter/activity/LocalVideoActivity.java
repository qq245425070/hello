package cn.gietv.mlive.modules.usercenter.activity;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import cn.gietv.mlive.R;
import cn.gietv.mlive.base.AbsBaseActivity;
import cn.gietv.mlive.modules.usercenter.adapter.LocalVideoAdapter;
import cn.gietv.mlive.modules.usercenter.bean.LoadedImage;
import cn.gietv.mlive.modules.usercenter.bean.Video;
import cn.gietv.mlive.modules.usercenter.model.AbstructProvider;
import cn.gietv.mlive.modules.usercenter.model.VideoProvider;
import cn.gietv.mlive.modules.video.activity.VRVideoPlayActivity;
import cn.gietv.mlive.utils.HeadViewController;

/**
 * Created by houde on 2016/4/22.
 */
public class LocalVideoActivity extends AbsBaseActivity {
    public LocalVideoActivity instance = null;
    ListView mJieVideoListView;
    LocalVideoAdapter mJieVideoListViewAdapter;
    List<Video> listVideos;
    int videoSize;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_local_video);
        HeadViewController.initHeadWithoutSearch(this,"本地视频");
        instance = this;
        AbstructProvider provider = new VideoProvider(instance);
        listVideos = provider.getList();
        videoSize = listVideos.size();
        mJieVideoListViewAdapter = new LocalVideoAdapter(this, listVideos);
        mJieVideoListView = (ListView)findViewById(R.id.listView);
        mJieVideoListView.setAdapter(mJieVideoListViewAdapter);
        mJieVideoListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                VRVideoPlayActivity.openVRVideoPlayActivity(LocalVideoActivity.this,2,0,null,listVideos.get(position).getPath(),"");
            }
        });
        loadImages();
    }
    /**
     * Load images.
     */
    private void loadImages() {
        final Object data = getLastNonConfigurationInstance();
        if (data == null) {
            new LoadImagesFromSDCard(listVideos,0,this).execute();
        } else {
            final LoadedImage[] photos = (LoadedImage[]) data;
            if (photos.length == 0) {
                new LoadImagesFromSDCard(listVideos,0,this).execute();
            }
            for (LoadedImage photo : photos) {
                addImage(photo);
            }
        }
    }
    public void addImage(LoadedImage... value) {
        for (LoadedImage image : value) {
            mJieVideoListViewAdapter.addPhoto(image);
            mJieVideoListViewAdapter.notifyDataSetChanged();
        }
    }

  public static class LoadImagesFromSDCard extends AsyncTask<Object, LoadedImage, Object> {
        private List<Video> mListVideos;
        private int mType;
        private Activity mActivity;
        public LoadImagesFromSDCard(List<Video> list,int type,Activity activity){
            this.mListVideos = list;
            mType = type;
            this.mActivity = activity;
        }
        @Override
        protected Object doInBackground(Object... params) {
            Bitmap bitmap = null;
            File file ;
            FileOutputStream fos;
            for (int i = 0; i < mListVideos.size(); i++) {
                file = new File("/mnt/sdcard/cn.gietv.mvr/");
                if(!file.exists()){
                    file.mkdirs();
                }
                file = new File("/mnt/sdcard/cn.gietv.mvr/" + mListVideos.get(i).getTitle() + ".png");
                if(file.exists()){
                    try {
                        bitmap = BitmapFactory.decodeStream(new FileInputStream(file));
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                } else {
                    bitmap = getVideoThumbnail(mListVideos.get(i).getPath(), 120, 120, MediaStore.Video.Thumbnails.MINI_KIND);
                    if (bitmap != null) {
                        try {
                            fos = new FileOutputStream(file);
                            bitmap.compress(Bitmap.CompressFormat.PNG, 90, fos);
                            fos.flush();
                            fos.close();
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
                publishProgress(new LoadedImage(bitmap));
            }
            return null;
        }

        @Override
        public void onProgressUpdate(LoadedImage... value) {
            if(mType == 0)
                ((LocalVideoActivity)mActivity).addImage(value);
        }

        /**
         * 获取视频缩略图
         * @param videoPath
         * @param width
         * @param height
         * @param kind
         * @return
         */
        private Bitmap getVideoThumbnail(String videoPath, int width , int height, int kind){
            Bitmap bitmap = null;
            bitmap = ThumbnailUtils.createVideoThumbnail(videoPath, kind);
            bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height, ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
            return bitmap;
        }
    }
}
