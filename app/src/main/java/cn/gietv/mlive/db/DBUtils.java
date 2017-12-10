package cn.gietv.mlive.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import cn.gietv.mlive.constants.CacheConstants;
import cn.gietv.mlive.modules.download.bean.M3U8Bean;
import cn.gietv.mlive.modules.download.bean.M3U8DetailBean;
import cn.gietv.mlive.modules.game.bean.GameInfoBean;
import cn.gietv.mlive.modules.login.bean.UserInfo;
import cn.gietv.mlive.modules.news.bean.NewsBean;
import cn.gietv.mlive.modules.news.bean.RosterBean;
import cn.gietv.mlive.modules.program.bean.ProgramBean;
import cn.gietv.mlive.modules.usercenter.bean.UserCenterBean;
import cn.gietv.mlive.utils.CacheUtils;
import cn.gietv.mlive.utils.ConfigUtils;

/**
 * Created by houde on 2015/11/25.
 */
public class DBUtils {
    public static final String DATABASE_NAME = "dujiao.db";
    private final String TABLE_NAME = "daytask";
    public static final String MESSAGE_TABLE_NAME = "message";
    public static final String ROSTER_TABLE_NAME = "roster";
    public static final String ATTENTION_ANCHOR = "attentionanchor";
    public static final String VIDEO_TABLE_NAME = "uservideo";
    public static final String UID_TABLE = "uid_tab";
    public static final String FOLLOW_AREA = "follow_area";
    public static final String USER_TABLE = "user";
    private MLiveSQLiteOpenHelper helper;
    private DBUtils(Context context){
        helper = new MLiveSQLiteOpenHelper(context, DATABASE_NAME, ConfigUtils.DATABASE_VERSION_CODE);
    }
    private static DBUtils dbUtils;
    public static DBUtils getInstance(Context context){
       if(dbUtils == null){
            dbUtils = new DBUtils(context);
        }
        return  dbUtils;
    }
    public boolean getData(String name){
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, null, "name = ?", new String[]{name}, null, null, null);
        boolean result = cursor.moveToNext();
        cursor.close();
        return result;
    }
    public long getNumber(String name){//获取任务观看时间或者发言数
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, null, "name = ?", new String[]{name}, null, null, null);
        long result = 0;
        if(cursor.moveToNext()) {
            result = cursor.getLong(2);
        }
        cursor.close();
        return result;
    }
    public int getTaskResult(String name){//获取任务是否被领取
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, null, "name = ?", new String[]{name}, null, null, null);
        int result = -1;
        if(cursor.moveToNext()){
            result = cursor.getInt(3);
        }
        cursor.close();
        return result;
    }
    public boolean editTeskResult(String name,int result){
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("result", result);
        long res =  db.update(TABLE_NAME,values,"name = ?",new String[]{name});
        if(res == 0){
            values.put("name",name);
            res = db.insert(TABLE_NAME, null, values);
        }
        return (res == 0) ? false:true;
    }

    public int getTaskCount(String name){//获取完成任务的数量
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, null, "name = ?", new String[]{name}, null, null, null);
        int result = -1;
        if(cursor.moveToNext()){
            result = cursor.getInt(4);
        }
        cursor.close();
        return result;
    }
    public boolean editTaskCount(String name,int count,int result){//修改完成任务的数量和结果
        SQLiteDatabase db = helper.getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put("result", result);
        values.put("count", count);
        long res =  db.update(TABLE_NAME,values,"name = ?",new String[]{name});
        if(res == 0){
            values.put("name",name);
            res = db.insert(TABLE_NAME, null, values);
        }
        return (res == 0) ? false:true;
    }
    public boolean saveTask(String name,long number,int res){
        SQLiteDatabase db = helper.getWritableDatabase();
        boolean result = false;
        ContentValues contentValues = new ContentValues();
        contentValues.put("number",number);
        contentValues.put("result",res);
        int changeRow = db.update(TABLE_NAME, contentValues, "name = ?", new String[]{name});
        if(changeRow == 0){
            contentValues.put("name", name);
            result = db.insert(TABLE_NAME,null,contentValues) > 0;
        }else{
            result = true;
        }
        Cursor cursor = db.query(TABLE_NAME, null, "name = ?", new String[]{name}, null, null, null);
        cursor.moveToNext();
        cursor.close();
        return result;
    }
    public boolean saveTask(String name,long number){
        SQLiteDatabase db = helper.getWritableDatabase();
        boolean result = false;
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        contentValues.put("number",number);
        int changeRow = db.update(TABLE_NAME, contentValues, "name = ?", new String[]{name});
        if(changeRow == 0){
            result = db.insert(TABLE_NAME,null,contentValues) > 0;
        }else{
            result = true;
        }
        return result;
    }

    //对Message表的操作(保存消息)
    public boolean saveMessage(NewsBean newsBean){
        SQLiteDatabase db = helper.getWritableDatabase();
        boolean result = false;
        ContentValues contentValues = new ContentValues();
        contentValues.put("userId",newsBean.userId);
        contentValues.put("nickname" , newsBean.nickname);
        contentValues.put("avatar",newsBean.avatar);
        contentValues.put("message",newsBean.message);
        contentValues.put("from_me",newsBean.from_me);
        contentValues.put("read",newsBean.read);
        contentValues.put("time",newsBean.time);
        contentValues.put("to_from",newsBean.toOrFrom);
        long changeRow = db.insert(MESSAGE_TABLE_NAME, null, contentValues);
        result = changeRow > 0;
        return result;
    }
    //跟新消息为已读
    public boolean updateRead(NewsBean newsBean){
        SQLiteDatabase db = helper.getWritableDatabase();
        boolean result = false;
        ContentValues contentValues = new ContentValues();
        contentValues.put("read",newsBean.read);
        int changeRow = db.update(MESSAGE_TABLE_NAME, contentValues, "toOrFrom = ?", new String[]{newsBean.userId});
        result = changeRow > 0;
        return result;
    }
    //获得所有的消息
    public List<NewsBean> getAllMessage(String userId,int page){
        int pageCount = 20;
        List<NewsBean> newsBeans = new ArrayList<>();
        SQLiteDatabase db = helper.getWritableDatabase();
        Cursor cursor =db.query(MESSAGE_TABLE_NAME, null, "to_from = ?", new String[]{userId}, null, null, "time asc", +((page - 1) * pageCount) + "," + pageCount);
        while(cursor.moveToNext()){
            NewsBean newsBean = new NewsBean();
            newsBean.userId = cursor.getString(1);
            newsBean.avatar = cursor.getString(2);
            newsBean.nickname = cursor.getString(3);
            newsBean.time = cursor.getLong(4);
            newsBean.message = cursor.getString(5);
            newsBean.from_me = cursor.getInt(6);
            newsBean.read = cursor.getInt(7);
            newsBean.toOrFrom = cursor.getString(8);
            newsBeans.add(newsBean);
        }
        cursor.close();
        return newsBeans;
    }
    //更新联系人表的用户信息
    public boolean updateRoster(String avatar,String nickname,String userid){
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("avatar",avatar);
        values.put("nickname",nickname);
        long count = db.update(ROSTER_TABLE_NAME, values, "userId = ?", new String[]{userid});
        boolean result = count > 0;
        return  result;
    }
    //更新消息表的用户信息
    public boolean updateMessageInfo(String avatar,String nickname,String userid){
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("avatar",avatar);
        values.put("nickname",nickname);
        long count = db.update(MESSAGE_TABLE_NAME,values,"userId = ?",new String[]{userid});
        boolean result = count > 0;
        return  result;
    }
    public Cursor getPageMessage(String userId ,int page){
        int pageCount = 20;
        SQLiteDatabase db = helper.getWritableDatabase();
        Cursor cursor = db.query(MESSAGE_TABLE_NAME, null, "to_from = ?", new String[]{userId}, null, null, "time asc", 0 + "," + page * pageCount);
        return cursor;
    }
    //获得最后一条消息的内容
    public NewsBean getLastMessage(String toOrFrom){
        SQLiteDatabase db = helper.getWritableDatabase();
        Cursor cursor = db.query(MESSAGE_TABLE_NAME, null, "to_from = ?", new String[]{toOrFrom}, null, null, "time desc");
        NewsBean newsBean = null;
        if(cursor.moveToNext()){
            newsBean = new NewsBean();
            newsBean.userId = cursor.getString(1);
            newsBean.avatar = cursor.getString(2);
            newsBean.nickname = cursor.getString(3);
            newsBean.time = cursor.getLong(4);
            newsBean.message = cursor.getString(5);
            newsBean.from_me = cursor.getInt(6);
            newsBean.read = cursor.getInt(7);
            newsBean.toOrFrom = cursor.getString(8);
        }
        cursor.close();
        return newsBean;
    }
    public int getMessageCount(String toOrFrom){
        SQLiteDatabase db = helper.getWritableDatabase();
        Cursor cursor = db.query(MESSAGE_TABLE_NAME, null, "to_from = ?", new String[]{toOrFrom}, null, null, null);
        int result = cursor.getCount();
        cursor.close();
        return result;
    }
    //保存好友
    public boolean saveRoster(RosterBean rosterBean){
        SQLiteDatabase db = helper.getWritableDatabase();
        boolean result = false;
        ContentValues contentValues = new ContentValues();
        contentValues.put("userId", rosterBean.userId);
        contentValues.put("avatar",rosterBean.avatar);
        contentValues.put("nickname",rosterBean.nickname);
        contentValues.put("time",rosterBean.time);
        contentValues.put("message",rosterBean.message);
        contentValues.put("count",rosterBean.count);
        contentValues.put("owner",rosterBean.owner);
        int changeRow = db.update(ROSTER_TABLE_NAME, contentValues, "userId = ?", new String[]{rosterBean.userId});
        if(changeRow == 0){
            result = db.insert(ROSTER_TABLE_NAME,null,contentValues) > 0;
        }else{
            result = true;
        }
        return result;
    }
    public int getNotReadCount(String userId){
        SQLiteDatabase db = helper.getWritableDatabase();
        Cursor cursor = db.query(ROSTER_TABLE_NAME, null, "userId = ?", new String[]{userId}, null, null, null);
        int count = 0;
        if(cursor.moveToNext()){
            count = cursor.getInt(6);
        }
        cursor.close();
        return count;
    }
    public int getNotReadCount(){
        SQLiteDatabase db = helper.getWritableDatabase();
        Cursor cursor = db.query(ROSTER_TABLE_NAME, null, "count != ? and owner = ?", new String[]{0 + "", CacheUtils.getCacheUserInfo()==null ? CacheUtils.getCache().getAsString(CacheConstants.CACHE_USERID):CacheUtils.getCacheUserInfo()._id}, null, null, null);
//        Cursor cursor = db.query(ROSTER_TABLE_NAME, null, "count != ? ", new String[]{0 + ""}, null, null, null);
        int result = cursor.getCount();
        cursor.close();
        return  result;
    }
    public List<RosterBean> getAllRoster(String userid){
        SQLiteDatabase db = helper.getWritableDatabase();
        List<RosterBean> rosterBeans = new ArrayList<>();
//        Cursor cursor = db.query(ROSTER_TABLE_NAME, null, "owner = ? or userId = ?", new String[]{userid,ConfigUtils.SYSTEM_MESSAGE_USERID}, null, null, "time desc");
        Cursor cursor = db.query(ROSTER_TABLE_NAME, null, null, null, null, null, "time desc");
        while (cursor.moveToNext()){
            RosterBean rosterBean = new RosterBean();
            rosterBean.userId = cursor.getString(1);
            rosterBean.avatar = cursor.getString(2);
            rosterBean.nickname = cursor.getString(3);
            rosterBean.time = cursor.getLong(4);
            rosterBean.message = cursor.getString(5);
            rosterBean.count = cursor.getInt(6);
            rosterBeans.add(rosterBean);
        }
        cursor.close();
        return rosterBeans;
    }
    public List<RosterBean> getSystemRoster(String userid){
        SQLiteDatabase db = helper.getWritableDatabase();
        List<RosterBean> rosterBeans = new ArrayList<>();
        Cursor cursor = db.query(ROSTER_TABLE_NAME, null, "userId = ?", new String[]{userid}, null, null, "time desc");
        while (cursor.moveToNext()){
            RosterBean rosterBean = new RosterBean();
            rosterBean.userId = cursor.getString(1);
            rosterBean.avatar = cursor.getString(2);
            rosterBean.nickname = cursor.getString(3);
            rosterBean.time = cursor.getLong(4);
            rosterBean.message = cursor.getString(5);
            rosterBean.count = cursor.getInt(6);
            rosterBeans.add(rosterBean);
        }
        cursor.close();
        System.out.println(rosterBeans.size());
        return rosterBeans;
    }
    public boolean getRoster(String userId){
        SQLiteDatabase db = helper.getWritableDatabase();
        Cursor cursor = db.query(ROSTER_TABLE_NAME, null, "userId = ?", new String[]{userId}, null, null, null);
        boolean result = cursor.moveToNext();
        cursor.close();
        return result;
    }
    public void updateCount(String userId){
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("count",0);
        db.update(ROSTER_TABLE_NAME, contentValues, "userId = ?", new String[]{userId});
    }
    public void saveAttentionAnchor(String  userid,int flag){//保存关注列表
        ContentValues values = null;
        SQLiteDatabase db = helper.getWritableDatabase();
        values = new ContentValues();
        values.put("userid",userid);
        values.put("switch",flag);
        db.insert(ATTENTION_ANCHOR, null, values);
    }
    public void deleteAttentionAnchor(String userid){//删除关注的主播
        SQLiteDatabase db = helper.getWritableDatabase();
        db.delete(ATTENTION_ANCHOR, "userid = ?", new String[]{userid});
    }
    public void updateAttentionAnchor(String userid,int flag){
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("switch",flag);
        db.update(ATTENTION_ANCHOR, values, "userid = ?", new String[]{userid});
    }
    public boolean queryAttentionAnchor(String userid){
        SQLiteDatabase db = helper.getWritableDatabase();
        Cursor cursor = db.query(ATTENTION_ANCHOR, null, "userid = ?", new String[]{userid}, null, null, null);
        boolean result = cursor.moveToNext();
        cursor.close();
        return result;
    }
    //保存下载视频信息
    public boolean updateM3u8(M3U8Bean bean){
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("nativepath",bean.getNativePath());
        values.put("networkpath", bean.getNetworkPath());
        values.put("status", bean.getStatus());
        values.put("image", bean.getImage());
        values.put("progress", bean.getProgress());
        values.put("total",bean.getTotal());
        values.put("anchor",bean.getAnchor());
        long changeCol = db.update("m3u8", values, "name = ?", new String[]{bean.getName()});
        if(changeCol == 0 ){
            values.put("name", bean.getName());
            changeCol = db.insert("m3u8", null, values);
        }
        return changeCol == 0 ? false:true;
    }
    public void updateM3u8ByName(String parentname, String statusPause) {
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("status", statusPause);
        long changeCol = db.update("m3u8", values, "name = ?", new String[]{parentname});
    }
    public boolean deleteM3u8(String name){
        SQLiteDatabase db = helper.getWritableDatabase();
        return db.delete("m3u8", "name = ?", new String[]{name}) == 0 ? false : true;
    }
    public M3U8Bean getBean(String name){
        SQLiteDatabase db = helper.getWritableDatabase();
        M3U8Bean bean = null;
        Cursor cursor = db.query("m3u8", null, "name = ?", new String[]{name}, null, null, null);
        if(cursor.moveToNext()){
            bean = new M3U8Bean();
            bean.setName(cursor.getString(0));
            bean.setNativePath(cursor.getString(1));
            bean.setNetworkPath(cursor.getString(2));
            bean.setStatus(cursor.getString(3));
            bean.setImage(cursor.getString(4));
            bean.setProgress(cursor.getInt(5));
            bean.setTotal(cursor.getInt(6));
            bean.setAnchor(cursor.getString(7));
        }
        cursor.close();
        return bean;
    }
    public List<M3U8Bean> getAllSuccessBean(){
        SQLiteDatabase db = helper.getWritableDatabase();
        List<M3U8Bean> beans = new ArrayList<>();
        Cursor cursor = db.query("m3u8", null, "status=?", new String[]{ConfigUtils.STATUS_SUCCESS}, null, null, null);
       // Cursor cursor = db.query("m3u8", null, null, null, null, null, null);
        M3U8Bean bean = null;
        while(cursor.moveToNext()){
            bean = new M3U8Bean();
            bean.setName(cursor.getString(0));
            bean.setNativePath(cursor.getString(1));
            bean.setNetworkPath(cursor.getString(2));
            bean.setStatus(cursor.getString(3));
            bean.setImage(cursor.getString(4));
            bean.setProgress(cursor.getInt(5));
            bean.setTotal(cursor.getInt(6));
            bean.setAnchor(cursor.getString(7));
            beans.add(bean);
        }
        cursor.close();
        return beans;
    }
    public List<M3U8Bean> getAllNoOverBean(){
        SQLiteDatabase db = helper.getWritableDatabase();
        List<M3U8Bean> beans = new ArrayList<>();
        Cursor cursor = db.query("m3u8", null, "status=? or status = ?", new String[]{ConfigUtils.STATUS_PAUSE,ConfigUtils.STATUS_RUNNING}, null, null, null);
        M3U8Bean bean = null;
        while(cursor.moveToNext()){
            bean = new M3U8Bean();
            bean.setName(cursor.getString(0));
            bean.setNativePath(cursor.getString(1));
            bean.setNetworkPath(cursor.getString(2));
            bean.setStatus(cursor.getString(3));
            bean.setImage(cursor.getString(4));
            bean.setProgress(cursor.getInt(5));
            bean.setTotal(cursor.getInt(6));
            bean.setAnchor(cursor.getString(7));
            beans.add(bean);
        }
        cursor.close();
        return beans;
    }
    public int getAllNoOverBeanCount(){
        SQLiteDatabase db = helper.getWritableDatabase();
        Cursor cursor = db.query("m3u8", null, "status=? or status = ?", new String[]{ConfigUtils.STATUS_PAUSE, ConfigUtils.STATUS_RUNNING}, null, null, null);
        int result = cursor.getCount();
        cursor.close();;
        return result;
    }

    public boolean updateM3u8DetailByUrl(M3U8DetailBean bean){
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("id",bean.getId());
        values.put("nativePath", bean.getNativePath());
        values.put("status", bean.getStatus());
        values.put("parent_name", bean.getParentName());
        values.put("file_name", bean.getFileName());
        long changeCol = db.update("m3u8_detail", values, "url = ?", new String[]{bean.getUrl()});
        if(changeCol == 0 ){
            values.put("url", bean.getUrl());
            changeCol = db.insert("m3u8_detail", null, values);
        }
        return changeCol == 0 ? false:true;
    }
    public boolean updateM3u8DetailById(M3U8DetailBean bean){
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("url",bean.getUrl());
        values.put("nativePath", bean.getNativePath());
        values.put("status", bean.getStatus());
        values.put("parent_name", bean.getParentName());
        values.put("file_name", bean.getFileName());
        long changeCol = db.update("m3u8_detail", values, "id = ?", new String[]{bean.getId() + ""});
        if(changeCol == 0 ){
            values.put("id", bean.getId());
            changeCol = db.insert("m3u8_detail", null, values);
        }
        return changeCol == 0 ? false:true;
    }
    public boolean deleteM3u8Detail(String url){
        SQLiteDatabase db = helper.getWritableDatabase();
        return db.delete("m3u8_detail", "url = ?", new String[]{url}) == 0 ? false : true;
    }
    public List<M3U8DetailBean> getAllDetailBean(String parentName){
        SQLiteDatabase db = helper.getWritableDatabase();
        List<M3U8DetailBean> beans = new ArrayList<>();
        M3U8DetailBean bean = null;
        Cursor cursor = db.query("m3u8_detail", null, "parent_name = ?", new String[]{parentName}, null, null, null);
        while(cursor.moveToNext()){
            bean = new M3U8DetailBean();
            bean.setId(cursor.getLong(0));
            bean.setUrl(cursor.getString(1));
            bean.setNativePath(cursor.getString(2));
            bean.setStatus(cursor.getString(3));
            bean.setParentName(cursor.getString(4));
            bean.setFileName(cursor.getString(5));
            beans.add(bean);
        }
        cursor.close();
        return beans;
    }
    public List<M3U8DetailBean> getAllNoDownloadByParentName(String parentName,String status){
        SQLiteDatabase db = helper.getWritableDatabase();
        List<M3U8DetailBean> beans = new ArrayList<>();
        M3U8DetailBean bean = null;
        Cursor cursor = db.query("m3u8_detail", null, "parent_name = ? and status = ?", new String[]{parentName,status}, null, null, null);
        while(cursor.moveToNext()){
            bean = new M3U8DetailBean();
            bean.setId(cursor.getLong(0));
            bean.setUrl(cursor.getString(1));
            bean.setNativePath(cursor.getString(2));
            bean.setStatus(cursor.getString(3));
            bean.setParentName(cursor.getString(4));
            bean.setFileName(cursor.getString(5));
            beans.add(bean);
        }
        cursor.close();
        return beans;
    }
    public M3U8DetailBean getM3U8DetailByUrl(String url){
        SQLiteDatabase db = helper.getWritableDatabase();
        M3U8DetailBean bean = null;
        Cursor cursor = db.query("m3u8_detail",null,"url = ?" ,new String[]{url},null,null,null);
        if(cursor.moveToNext()){
            bean = new M3U8DetailBean();
            bean.setId(cursor.getLong(0));
            bean.setUrl(cursor.getString(1));
            bean.setNativePath(cursor.getString(2));
            bean.setStatus(cursor.getString(3));
            bean.setParentName(cursor.getString(4));
            bean.setFileName(cursor.getString(5));
        }
        cursor.close();
        return bean;
    }
    public M3U8DetailBean getM3U8DetailById(long id){
        SQLiteDatabase db = helper.getWritableDatabase();
        M3U8DetailBean bean = null;
        Cursor cursor = db.query("m3u8_detail",null,"id = ?" ,new String[]{id+""},null,null,null);
        if(cursor.moveToNext()){
            bean = new M3U8DetailBean();
            bean.setId(cursor.getLong(0));
            bean.setUrl(cursor.getString(1));
            bean.setNativePath(cursor.getString(2));
            bean.setStatus(cursor.getString(3));
            bean.setParentName(cursor.getString(4));
            bean.setFileName(cursor.getString(5));
        }
        cursor.close();
        return bean;
    }
    public M3U8DetailBean getNoDownloadByParentName(String parentName,String status){
        List<M3U8DetailBean> beans = getAllNoDownloadByParentName(parentName, status);
        if(beans != null && beans.size() > 0){
            return beans.get(0);
        }
        return null;
    }

    public boolean saveUserVideo(String videoid,int position,String path){
        SQLiteDatabase db = helper.getWritableDatabase();
        boolean returnValue = false;
        ContentValues values = new ContentValues();
        values.put("_id", videoid);
        values.put("position", position);
        values.put("path", path);
        int result = db.update(VIDEO_TABLE_NAME, values, "_id='" + videoid + "'", null);
        if (result == 0)
            returnValue = db.insert(VIDEO_TABLE_NAME, null, values) > 0;
        else
            returnValue = true;
        return returnValue;
    }
    public ProgramBean.ProgramEntity getVideoById(String id) {
        SQLiteDatabase db = helper.getWritableDatabase();
        Cursor c = db.query(VIDEO_TABLE_NAME, new String[] { "_id", "position","path" }, "_id='" + id + "'", null, null, null, null);
        int numRows = c.getCount();
        c.moveToFirst();
        ProgramBean.ProgramEntity video = null;
        if (numRows > 0) {
            video = new ProgramBean.ProgramEntity();
            video._id = c.getString(0);
            video.position = c.getInt(1);
            video.url =  c.getString(2);
        }
        c.close();
        return video;
    }
    //保存用户名
    public boolean saveNickname(String uid,String nickname,String token,String password){
        boolean returnValue = false;
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("_id",uid);
        contentValues.put("nickname", nickname);
        contentValues.put("token",token);
        contentValues.put("password",password);
        int  result = db.update(UID_TABLE,contentValues,"_id = ?" , new String[]{uid} );
        if(result == 0){
            returnValue = db.insert(UID_TABLE,null,contentValues) > 0;
        }else{
            returnValue = true;
        }
        return  returnValue;
    }
    public String getPassword(String uid){
        SQLiteDatabase db = helper.getReadableDatabase();
        String password = "";
        Cursor cursor = db.query(UID_TABLE,new String[]{"password"},"_id = '" + uid + "'",null,null,null,null);
        if(cursor.moveToNext()){
            password = cursor.getString(cursor.getColumnIndex("password"));
        }
        cursor.close();
        return password;
    }
    public List<String> getUid(String headStr){//获取用户名，根据输入数据的变化返回不同的保存数据
        List<String> uidList ;
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.query(UID_TABLE,new String[]{"_id"},"_id like '" + headStr + "%'",null,null,null,null);
        if(cursor.getCount() > 0){
            uidList = new ArrayList<>();
            while (cursor.moveToNext()){
                uidList.add(cursor.getString(0));
            }
            return uidList;
        }
        return null;
    }
    public boolean saveArea(String id,String name,String spic){//保存关注的专区
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("_id",id);
        values.put("name",name);
        values.put("spic",spic);
        long result = db.update(FOLLOW_AREA , values ,"_id = ?" , new String[]{id});
        if(result == 0)
            result = db.insert(FOLLOW_AREA,null,values);
        return result == 1;
    }
    public List<GameInfoBean.GameInfoEntity> getAllArea(){
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.query(FOLLOW_AREA,null,null,null,null,null,null);
        List<GameInfoBean.GameInfoEntity> games ;
        if(cursor.getCount() > 0){
            games = new ArrayList<>();
            GameInfoBean.GameInfoEntity game ;
            while (cursor.moveToNext()){
                game = new GameInfoBean.GameInfoEntity();
                game._id = cursor.getString(cursor.getColumnIndex("_id"));
                game.name = cursor.getString(cursor.getColumnIndex("name"));
                game.spic = cursor.getString(cursor.getColumnIndex("spic"));
                games.add(game);
            }
            return  games;
        }
        return null;
    }
    // 当前登录用的信息
    public int saveUser(UserInfo userInfo){
        if(userInfo != null && userInfo.userinfo != null) {
            SQLiteDatabase db = helper.getWritableDatabase();
            ContentValues values = new ContentValues();
            UserCenterBean.UserinfoEntity user = userInfo.userinfo;
            values.put("_id", user._id);
            values.put("nickname", user.nickname);
            values.put("onlines", user.onlines);
            values.put("follows", user.follows);
            values.put("myfollow", user.myfollow);
            values.put("photo", user.photo);
            values.put("gender", user.gender);
            values.put("avatar", user.avatar);
            values.put("desc", user.desc);
            values.put("email", user.email);
            values.put("token", userInfo.token);
            int result = db.update(USER_TABLE, values, "_id = ?", new String[]{user._id});
            if (result == 0)
                result = (int)db.insert(USER_TABLE, null, values);
            return result;
        }
        return  0;
    }
    //删除登录用户信息
    public void deleteUser(String id){
        SQLiteDatabase db = helper.getWritableDatabase();
        db.delete(USER_TABLE,"_id = ?", new String[]{id});
    }
    public UserInfo getUser(){
        //    _id,nickname,onlines,follows,myfollow,gender,photo,avatar,desc,email,token;
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.query(USER_TABLE,null,null,null,null,null,null);
        if(cursor.moveToNext()){
            UserInfo userInfo = new UserInfo();
            UserCenterBean.UserinfoEntity user = new UserCenterBean.UserinfoEntity();
            user._id = cursor.getString(cursor.getColumnIndex("_id"));
            user.nickname = cursor.getString(cursor.getColumnIndex("nickname"));
            user.onlines = cursor.getInt(cursor.getColumnIndex("onlines"));
            user.follows = cursor.getInt(cursor.getColumnIndex("follows"));
            user.myfollow = cursor.getInt(cursor.getColumnIndex("myfollow"));
            user.gender = cursor.getInt(cursor.getColumnIndex("gender"));
            user.photo = cursor.getInt(cursor.getColumnIndex("photo"));
            user.avatar = cursor.getString(cursor.getColumnIndex("avatar"));
            user.desc = cursor.getString(cursor.getColumnIndex("desc"));
            user.email = cursor.getString(cursor.getColumnIndex("email"));
            userInfo.token = cursor.getString(cursor.getColumnIndex("token"));
            userInfo.userinfo = user;
            return userInfo;
        }
        return null;
    }
}
