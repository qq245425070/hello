package cn.gietv.mlive.receiver;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.webkit.MimeTypeMap;

import java.io.File;

import cn.gietv.mlive.MainApplication;
import cn.gietv.mlive.db.DBUtils;
import cn.gietv.mlive.modules.download.bean.M3U8Bean;
import cn.gietv.mlive.modules.download.bean.M3U8DetailBean;
import cn.gietv.mlive.utils.ConfigUtils;
import cn.gietv.mlive.utils.DownloadController;
import cn.gietv.mlive.utils.SharedPreferenceUtils;

/**
 * author：steven
 * datetime：15/10/9 10:52
 */
public class DownloadReceiver extends BroadcastReceiver {
@Override
public void onReceive(Context context, Intent intent) {
    if (DownloadManager.ACTION_DOWNLOAD_COMPLETE.equals(intent.getAction())) {
        long reference = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
        try {
            DownloadController.idMap.remove(reference);
            DownloadManager manager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
            manager.openDownloadedFile(reference);
            DownloadManager.Query query = new DownloadManager.Query();
            query.setFilterById(reference);
            Cursor c = manager.query(query);
            if (c.moveToFirst()) {
                // 获取文件下载路径
                String filename = c.getString(c.getColumnIndex(DownloadManager.COLUMN_LOCAL_FILENAME));
                // 如果文件名不为空
                if (filename != null) {
                    File file = new File(filename);
                    if (!file.exists()) {
                        return;
                    }
                    if(filename.endsWith(".apk")) {
                        Intent i = new Intent(Intent.ACTION_VIEW);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        i.setDataAndType(Uri.parse("file://" + file.toString()),
                                "application/vnd.android.package-archive");
                        context.startActivity(i);
//                        android.os.Process.killProcess(android.os.Process.myPid());
                    }else if(filename.endsWith(".ts")){//向数据库中保存下载信息
                        String url =  c.getString(c.getColumnIndex(DownloadManager.COLUMN_URI));
                        System.out.println("url    " + url);
                        DBUtils dbUtils = DBUtils.getInstance(context);
                        M3U8DetailBean m3U8DetailBean = dbUtils.getM3U8DetailByUrl(url);
                        if(m3U8DetailBean == null)
                            return;
                        m3U8DetailBean.setStatus(ConfigUtils.STATUS_SUCCESS);
                        dbUtils.updateM3u8DetailByUrl(m3U8DetailBean);
                        M3U8Bean bean = dbUtils.getBean(m3U8DetailBean.getParentName());
                        if(bean == null){
                            return;
                        }
                        int progress = bean.getProgress() + 1;
                        int total = bean.getTotal();
                        bean.setProgress(progress);
                        if(progress == total){
                            bean.setStatus(ConfigUtils.STATUS_SUCCESS);
                        }
                        dbUtils.updateM3u8(bean);
                        m3U8DetailBean = dbUtils.getNoDownloadByParentName(m3U8DetailBean.getParentName(),ConfigUtils.STATUS_PAUSE);
                        if(m3U8DetailBean != null && SharedPreferenceUtils.getBoolean(m3U8DetailBean.getParentName()+ConfigUtils.STATUS_RUNNING,true)){
                            file = new File(m3U8DetailBean.getNativePath());
                            DownloadManager.Request request = new DownloadManager.Request(Uri.parse(m3U8DetailBean.getUrl()));
                            request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI);
                            request.setAllowedOverRoaming(false);
                            //设置文件类型
                            MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
                            String mimeString = mimeTypeMap.getMimeTypeFromExtension(MimeTypeMap.getFileExtensionFromUrl(url));
                            request.setMimeType(mimeString);
                            //在通知栏中显示
                            request.setVisibleInDownloadsUi(false);
                            request.setShowRunningNotification(false);
                            request.setDestinationUri(Uri.fromFile(file));
                            request.setTitle(m3U8DetailBean.getParentName());
                            long id = manager.enqueue(request);
                            m3U8DetailBean.setId(id);
                            MainApplication.getInstance().saveDownloadStatus(m3U8DetailBean.getParentName(), id);
                            m3U8DetailBean.setStatus(ConfigUtils.STATUS_RUNNING);
                            dbUtils.updateM3u8DetailByUrl(m3U8DetailBean);
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("downloaderror ----- " + e.getMessage());
        }
    }
}
}
