package cn.gietv.mlive.utils;

import android.content.Context;
import android.os.Environment;
import android.os.StatFs;
import android.text.format.Formatter;

import java.io.File;

/**
 * Created by houde on 2016/3/2.
 */
public class SDCardUtils {
    public static String getUseMemory(Context context){
         File path= Environment.getExternalStorageDirectory();
         StatFs statFs=new StatFs(path.getPath());
         long blocksize=statFs.getBlockSize();
         long totalblocks=statFs.getBlockCount();
         long availableblocks=statFs.getAvailableBlocks();
         long totalsize=blocksize*totalblocks;
         long availablesize=availableblocks*blocksize;
         return Formatter.formatFileSize(context, totalsize - availablesize);
    }
    public static String getTotalMemory(Context context){
        File path= Environment.getExternalStorageDirectory();
        StatFs statFs=new StatFs(path.getPath());
        long blocksize=statFs.getBlockSize();
        long totalblocks=statFs.getBlockCount();
        long totalsize=blocksize*totalblocks;
        return Formatter.formatFileSize(context, totalsize);
    }
    public static String getAvailablesMomery(Context context){
        File path= Environment.getExternalStorageDirectory();
        StatFs statFs=new StatFs(path.getPath());
        long blocksize=statFs.getBlockSize();
        long availableblocks=statFs.getAvailableBlocks();
        long availablesize=availableblocks*blocksize;
        return Formatter.formatFileSize(context, availablesize);
    }
    public static boolean haveSDCard() {
        return android.os.Environment.getExternalStorageState().equals(
                android.os.Environment.MEDIA_MOUNTED);
    }
}
