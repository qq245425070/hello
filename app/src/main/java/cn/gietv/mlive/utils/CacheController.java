package cn.gietv.mlive.utils;

import android.content.Context;
import android.text.format.Formatter;

import java.io.File;

/**
 * Created by houde on 2016/3/17.
 */
public class CacheController {

    public static long getFolderSize(File file) throws Exception {
        long size = 0;
        try {
            File[] fileList = file.listFiles();
            for (int i = 0; i < fileList.length; i++) {
                // 如果下面还有文件
                if (fileList[i].isDirectory()) {
                    size = size + getFolderSize(fileList[i]);
                } else {
                    size = size + fileList[i].length();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return size;
    }
    public static String getCacheSize(Context context,File file){
        try {
            return Formatter.formatFileSize(context,getFolderSize(file));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "0M";
    }
    //删除文件夹下的所有文件
    public static void deleteFilesByDirectory(File directory) {
        if (directory != null && directory.exists() && directory.isDirectory()) {
            for (File item : directory.listFiles()) {
                item.delete();
            }
        }
    }
}
