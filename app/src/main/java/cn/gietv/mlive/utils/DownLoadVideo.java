package cn.gietv.mlive.utils;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.webkit.MimeTypeMap;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import cn.gietv.mlive.MainApplication;
import cn.gietv.mlive.db.DBUtils;
import cn.gietv.mlive.modules.download.bean.M3U8Bean;
import cn.gietv.mlive.modules.download.bean.M3U8DetailBean;

/**
 * Created by houde on 2016/3/3.
 */
public class DownLoadVideo {
    private static List<String> urls = new ArrayList<>();
    private static DownLoadVideo downLoadVideo;
    private DownloadManager downloadManager;
    private DBUtils dbUtils;
    private static List<String> mDownloadURL = new ArrayList<>();
    private DownLoadVideo(Context context){
        downloadManager = (DownloadManager)context.getSystemService(Context.DOWNLOAD_SERVICE);
        dbUtils = DBUtils.getInstance(context);
    }
    public static DownLoadVideo getInstance(Context context){
        if(downLoadVideo == null){
            downLoadVideo = new DownLoadVideo(context);
        }
        return downLoadVideo;
    }
    public boolean getNetwork(String savePath, final Context context,String url,String fileName,String imagepath,String anchor){
        if(mDownloadURL.contains(fileName)){
            ((Activity)context).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    ToastUtils.showToast(context, "该视频已经在下载");
                }
            });
            return false;
        }
        File file = new File(savePath);
        if(!file.exists()){
            file.mkdirs();
        }
        try {
            HttpURLConnection httpConnection = (HttpURLConnection) new URL(url).openConnection();
            httpConnection.setRequestMethod("GET");
            httpConnection.setReadTimeout(60 * 1000);
            if(httpConnection.getResponseCode() == 200){
                InputStream inputStream = httpConnection.getInputStream();
                BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder buf = new StringBuilder();
                StringBuilder buf2 = new StringBuilder();
                File nativeFile = new File(savePath+"/"+fileName+"_native.m3u8");
                File networkFile = new File(savePath+"/"+fileName+"_network.m3u8");
                BufferedWriter bw = new BufferedWriter(new FileWriter(networkFile));
                BufferedWriter bw2 = new BufferedWriter(new FileWriter(nativeFile));
                String readLine = null;
                int num = 0;
                while((readLine =br.readLine()) != null){
                    buf2.append(readLine+"\r\n");
                    if (readLine.length() > 0 && readLine.startsWith("http://")) {
                        urls.add(readLine);
                        buf.append(num+".ts\r\n");
                        num++;
                    }else {
                        buf.append(readLine+"\r\n");
                    }
                }
                bw2.write(buf.toString(), 0, buf.length());
                bw.write(buf2.toString(), 0, buf.length());
                br.close();
                bw.close();
                bw2.close();
                M3U8Bean bean = new M3U8Bean();
                bean.setName(fileName);
                bean.setNativePath(nativeFile.getAbsolutePath());
                bean.setNetworkPath(networkFile.getAbsolutePath());
                bean.setTotal(num);
                bean.setStatus(ConfigUtils.STATUS_RUNNING);
                bean.setProgress(0);
                bean.setImage(imagepath);
                bean.setAnchor(anchor);
                dbUtils.updateM3u8(bean);
                M3U8DetailBean m3U8DetailBean = null ;
                File fileChirld = null;
                for(int i = 0; i < urls.size();i++ ){
                    fileChirld = new File(savePath + "/" + i+".ts");
                    m3U8DetailBean = new M3U8DetailBean();
                    m3U8DetailBean.setFileName(i + ".ts");
                    m3U8DetailBean.setParentName(fileName);
                    m3U8DetailBean.setNativePath(fileChirld.getAbsolutePath());
                    m3U8DetailBean.setStatus(ConfigUtils.STATUS_PAUSE);
                    m3U8DetailBean.setUrl(urls.get(i));
                    dbUtils.updateM3u8DetailByUrl(m3U8DetailBean);
                }
                if(m3U8DetailBean != null){
                    SharedPreferenceUtils.saveProp(m3U8DetailBean.getParentName() + ConfigUtils.STATUS_RUNNING, true);
                    startDownload(savePath + "/",m3U8DetailBean.getFileName(), m3U8DetailBean.getUrl(), fileName, context);
                }
                urls.clear();
            }
        }catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        mDownloadURL.add(fileName);
        return true;
    }
    public void startDownload(String savepath,String fileName, String url,String name,Context context) {
        if (StringUtils.isEmpty(url) || !url.startsWith("http")) {
            return;
        }
        File file1 = new File(savepath);
        if(!file1.exists()){
            file1.mkdirs();
        }
        M3U8DetailBean bean = new M3U8DetailBean();
        File file = new File(savepath + fileName);
        Uri downloadUri = Uri.parse(url);
        DownloadManager.Request request = new DownloadManager.Request(downloadUri);
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
        request.setTitle(name);
        long id = downloadManager.enqueue(request);
        bean.setId(id);
        bean.setStatus(ConfigUtils.STATUS_RUNNING);
        bean.setNativePath(file.getAbsolutePath());
        bean.setParentName(name);
        bean.setUrl(url);
        bean.setFileName(fileName);
        dbUtils.updateM3u8DetailByUrl(bean);
        MainApplication.getInstance().saveDownloadStatus(name,id);
    }
    public void pauseDownload(final Context context, final String parentname){
        SharedPreferenceUtils.saveProp(parentname + ConfigUtils.STATUS_RUNNING, false);
        dbUtils.updateM3u8ByName(parentname, ConfigUtils.STATUS_PAUSE);
       /* M3U8DetailBean m3U8DetailBean = dbUtils.getNoDownloadByParentName(parentname, ConfigUtils.STATUS_RUNNING);
        if(m3U8DetailBean == null){
            return;
        }
        m3U8DetailBean.setStatus(ConfigUtils.STATUS_PAUSE);
        dbUtils.updateM3u8DetailByUrl(m3U8DetailBean);*/
    }
    public void resumeDownload(final Context context, final String parentname){
        System.out.println("resumeDownload");
        SharedPreferenceUtils.saveProp(parentname + ConfigUtils.STATUS_RUNNING, true);
        M3U8DetailBean bean = dbUtils.getNoDownloadByParentName(parentname, ConfigUtils.STATUS_PAUSE);
        if(bean != null){
            Uri downloadUri = Uri.parse(bean.getUrl());
            DownloadManager.Request request = new DownloadManager.Request(downloadUri);
            request.setAllowedNetworkTypes( DownloadManager.Request.NETWORK_WIFI);
            request.setAllowedOverRoaming(false);
            //设置文件类型
            MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
            String mimeString = mimeTypeMap.getMimeTypeFromExtension(MimeTypeMap.getFileExtensionFromUrl(bean.getUrl()));
            request.setMimeType(mimeString);
            //在通知栏中显示
            request.setVisibleInDownloadsUi(false);
            request.setShowRunningNotification(false);
            File file = new File(bean.getNativePath());
            request.setDestinationUri(Uri.fromFile(file));
            request.setTitle(bean.getParentName());
            long id= downloadManager.enqueue(request);
            bean.setId(id);
            bean.setStatus(ConfigUtils.STATUS_RUNNING);
            dbUtils.updateM3u8DetailByUrl(bean);
            dbUtils.updateM3u8ByName(parentname, ConfigUtils.STATUS_RUNNING);
            return;
        }
        DownloadManager.Query pausedDownloadQuery = new DownloadManager.Query();
        pausedDownloadQuery.setFilterByStatus(DownloadManager.STATUS_FAILED);
        Cursor pausedDownloads =downloadManager.query(pausedDownloadQuery);
        System.out.println(pausedDownloads.getCount());
        while(pausedDownloads.moveToNext()){
            long id = pausedDownloads.getLong(pausedDownloads.getColumnIndex(DownloadManager.COLUMN_ID));
            String title = pausedDownloads.getString(pausedDownloads.getColumnIndex(DownloadManager.COLUMN_TITLE));
            if(!parentname.equals(title)){
                continue;
            }
            downloadManager.remove(id);
            System.out.println(id);
            bean = dbUtils.getM3U8DetailById(id);
            if(bean == null){
                continue;
            }
            Uri downloadUri = Uri.parse(bean.getUrl());
            DownloadManager.Request request = new DownloadManager.Request(downloadUri);
            request.setAllowedNetworkTypes( DownloadManager.Request.NETWORK_WIFI);
            request.setAllowedOverRoaming(false);
            //设置文件类型
            MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
            String mimeString = mimeTypeMap.getMimeTypeFromExtension(MimeTypeMap.getFileExtensionFromUrl(bean.getUrl()));
            request.setMimeType(mimeString);
            //在通知栏中显示
            request.setVisibleInDownloadsUi(false);
            File file = new File(bean.getNativePath());
            request.setDestinationUri(Uri.fromFile(file));
            request.setTitle(bean.getParentName());
            id = downloadManager.enqueue(request);
            bean.setId(id);
            dbUtils.updateM3u8DetailByUrl(bean);
            return;
        }

    }
    public void deleteFile(final List<M3U8Bean> beans, final Context context){
        new Thread(){
            @Override
            public void run() {
                M3U8Bean bean = null;
                M3U8DetailBean detailBean = null;
                File file = null;
                List<M3U8DetailBean>  detailBeans = null;
                for(int i = 0; i< beans.size();i++){
                    bean = beans.get(i);
                    detailBeans = dbUtils.getAllDetailBean(bean.getName());
                    SharedPreferenceUtils.remove(bean.getName() + ConfigUtils.STATUS_RUNNING);
                    for(int j = 0;j<detailBeans.size();j++){
                        detailBean = detailBeans.get(j);
                        if(detailBean != null) {
                            downloadManager.remove(detailBean.getId());
                        }
                        System.out.println(detailBean.getNativePath());
                        file = new File(detailBean.getNativePath());
                        if(file.exists()){
                            System.out.println(file.delete());
                            file.delete();
                        }
                        dbUtils.deleteM3u8Detail(detailBean.getUrl());
                    }
                    deleteFilesByDirectory(new File( Environment.getExternalStoragePublicDirectory("download") + "/" + bean.getName()));
                }

            }
        }.start();
    }
   private void deleteFilesByDirectory(File file){
       CacheController.deleteFilesByDirectory(file);
       file.delete();
   }
}
