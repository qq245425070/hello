package cn.gietv.mlive.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * author:houde
 * Created by houde on 2015/11/25.
 */
public class MLiveSQLiteOpenHelper extends SQLiteOpenHelper{
    public MLiveSQLiteOpenHelper(Context context, String name,int versionCode) {
        super(context, name, null, versionCode);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //每日任务表
        db.execSQL("create table if not exists daytask (_id integer primary key autoincrement ,name varchar(50),number long,result interger,count interger)");
        //信息内容表
        db.execSQL("create table if not exists message (_id integer primary key autoincrement ,userId varchar(50) ,avatar varchar(50),nickname varchar(50)," +
                "time Long, message varchar(50), from_me integer,read integer,to_from varchar(50),type ,title,content,proid,desc,owner)");
        //联系人表
        db.execSQL("create table if not exists roster (_id integer primary key autoincrement , userId varchar(50) ,avatar varchar(50), nickname varchar(50)," +
                "time Long , message varchar(50) ,count integer , read integer,owner)");
        //关注作者表
        db.execSQL("create table if not exists attentionanchor(userid varchor(50),switch integer)");
        //下载视频表
        db.execSQL("create table if not exists m3u8 (name,nativepath,networkpath,status,image,progress,total,anchor,program_id)");
        //下载视频详情表（每个视频下的ts文件）
        db.execSQL("create table if not exists m3u8_detail(id ,url,nativepath,status,parent_name,file_name,program_id)");
        //记录视频播放时间的表
        db.execSQL("create table if not exists uservideo (_id String primary key  ,position integer, path String)");
        //记录用户登录的_id的表用于自动登录
        db.execSQL("create table if not exists uid_tab (_id String primary key  ,nickname varchar(50),token,password)");
        //第一次启动，没有登录，保存关注的专区表
        db.execSQL("create table if not exists follow_area (_id String primary key ,name varchar(50),spic)");
        //记录当前登录用户的信息表
        db.execSQL("create table if not exists user(_id,nickname,onlines,follows,myfollow,gender,photo,avatar,desc,email,token)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists daytask");
        db.execSQL("drop table if exists message");
        db.execSQL("drop table if exists roster");
        db.execSQL("drop table if exists attentionanchor");
        db.execSQL("drop table if exists m3u8");
        db.execSQL("drop table if exists m3u8_detail");
        db.execSQL("drop table if exists uservideo");
        db.execSQL("drop table if exists nickname_tab");
        db.execSQL("drop table if exists uid_tab");
        db.execSQL("drop table if exists follow_area");
        onCreate(db);
    }
}
