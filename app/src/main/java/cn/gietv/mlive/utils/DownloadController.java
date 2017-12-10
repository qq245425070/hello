package cn.gietv.mlive.utils;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.util.ArrayMap;
import android.webkit.MimeTypeMap;

import java.io.File;

import cn.gietv.mlive.http.DefaultLiveHttpCallBack;
import cn.gietv.mlive.modules.home.model.NoviceTaskModel;
import cn.kalading.android.retrofit.utils.RetrofitUtils;

/**
 * author：steven
 * datetime：15/10/9 10:25
 */
public class DownloadController {
    private static String DOWNLOAD_PATH = Environment.getDownloadCacheDirectory().getPath();
    public static ArrayMap<Long, String> idMap = new ArrayMap<>();
    private Context mContext;
    private DownloadManager mDownloadManager;

    public DownloadController(Context context) {
        mContext = context;
        mDownloadManager = (DownloadManager) mContext.getSystemService(Context.DOWNLOAD_SERVICE);
    }

    private boolean checkFile(String path) {
        File folder = new File(path);
        return (folder.exists() && folder.isDirectory()) || folder.mkdirs();
    }

    public void startDownload(String fileName, String url) {
        if (StringUtils.isEmpty(url) || !url.startsWith("http")) {
            return;
        }
        if (idMap.containsValue(url)) {
            ToastUtils.showToast(mContext, "正在下载中...");
            return;
        }
        //向服务器更新下载游戏任务信息
        NoviceTaskModel noviceTaskModel = RetrofitUtils.create(NoviceTaskModel.class);
        noviceTaskModel.updateTask(ConfigUtils.TASK_DOWNLOAD_GAME, 1, new DefaultLiveHttpCallBack<String>() {
            @Override
            public void success(String s) {

            }

            @Override
            public void failure(String message) {

            }
        });
        Uri downloadUri = Uri.parse(url);
        DownloadManager.Request request = new DownloadManager.Request(downloadUri);
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE | DownloadManager.Request.NETWORK_WIFI);
        request.setAllowedOverRoaming(false);
        //设置文件类型
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        String mimeString = mimeTypeMap.getMimeTypeFromExtension(MimeTypeMap.getFileExtensionFromUrl(url));
        request.setMimeType(mimeString);
        //在通知栏中显示
//        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setVisibleInDownloadsUi(true);
        //sdcard的目录下的download文件夹
//        request.setDestinationInExternalPublicDir("/download/", fileName);
        File file = new File(Environment.getExternalStorageDirectory().getPath() + "/download/"+fileName);
        request.setDestinationUri(Uri.fromFile(file));
        request.setTitle(fileName);
        long id = mDownloadManager.enqueue(request);
        idMap.put(id, url);
        //=========================================================
//
//        if (StringUtils.isEmpty(url) || !url.startsWith("http")) {
//            return;
//        }
//        if (idMap.containsValue(url)) {
//            ToastUtils.showToast(mContext, "正在下载中...");
//            return;
//        }
//        Uri downloadUri = Uri.parse(url);
//        DownloadManager.Request request = new DownloadManager.Request(downloadUri);
//        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE | DownloadManager.Request.NETWORK_WIFI);
//        request.setAllowedOverRoaming(false);
//        //设置文件类型
//        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
//        String mimeString = mimeTypeMap.getMimeTypeFromExtension(MimeTypeMap.getFileExtensionFromUrl(url));
//        request.setMimeType(mimeString);
//        //在通知栏中显示
//        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
//        request.setVisibleInDownloadsUi(true);
//        //sdcard的目录下的download文件夹
//        request.setDestinationInExternalPublicDir("/download/", fileName);
//        request.setTitle(fileName);
//        long id = mDownloadManager.enqueue(request);
//        idMap.put(id, url);
    }
}
