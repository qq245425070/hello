package cn.gietv.mlive.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by houde on 2016/2/26.
 */
public class DownLoadImageUtil {
    private static final String TAG = "DownloadToPhone";
    private static final String PACKAGE_NAME = "cn.gietv.mvr";
    private static final String URI_PREFIX = "file:///data/data/"+PACKAGE_NAME+"/files/";
    public static void loadfile(final Context context,final String imageUrl, final String imageName){
        Runnable getImage = new Runnable(){
            public void run() {
                try {
                    URL u = new URL(imageUrl);
                    HttpURLConnection c = (HttpURLConnection) u.openConnection();
                    c.setRequestMethod("GET");
                    c.connect();
                    if(c.getResponseCode() == 200) {
                        FileOutputStream f = context.openFileOutput(imageName, Activity.MODE_WORLD_WRITEABLE);
                        InputStream in = c.getInputStream();
                        byte[] buffer = new byte[1024];
                        int len1 = 0;
                        while ((len1 = in.read(buffer)) > 0) {
                            f.write(buffer, 0, len1);
                        }
                        f.close();
                        c.disconnect();
                    }
                }catch (Exception e) {
                    Log.e(TAG, "could not download and save IMAGE file", e);
                }
            }
        };
        File file = new File(URI_PREFIX + imageName);
        if(!file.exists()){
            new Thread(getImage).start();
        }
    }
    public static Bitmap getBitmap(Context context,String imageName){
        try {
            return BitmapFactory.decodeStream(context.openFileInput(imageName));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return  null;
    }
}
