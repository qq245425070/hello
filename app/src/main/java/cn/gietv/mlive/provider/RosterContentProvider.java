package cn.gietv.mlive.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import cn.gietv.mlive.db.DBUtils;
import cn.gietv.mlive.db.MLiveSQLiteOpenHelper;
import cn.gietv.mlive.utils.ConfigUtils;

/**
 * Created by houde on 2015/12/20.
 */
public class RosterContentProvider extends ContentProvider {
    private static final UriMatcher MATCHER = new UriMatcher(UriMatcher.NO_MATCH);
    private MLiveSQLiteOpenHelper mHelper;
    private static final int ROSTERS = 1;
    //private static final int NEWS_BEAN = 2;
    static {
        MATCHER.addURI("cn.gietv.mlive.RosterProvider", DBUtils.ROSTER_TABLE_NAME,ROSTERS);
    }
    @Override
    public boolean onCreate() {
        mHelper = new MLiveSQLiteOpenHelper(getContext(),DBUtils.DATABASE_NAME, ConfigUtils.DATABASE_VERSION_CODE);
        return false;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteDatabase db = mHelper.getWritableDatabase();
        return db.query(DBUtils.ROSTER_TABLE_NAME,projection,selection,selectionArgs,null,null,sortOrder);
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        SQLiteDatabase db = mHelper.getWritableDatabase();
        db.insert(DBUtils.ROSTER_TABLE_NAME,null,values);
        getContext().getContentResolver().notifyChange(uri,null);
        return uri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        SQLiteDatabase db = mHelper.getWritableDatabase();
        int result = db.update(DBUtils.ROSTER_TABLE_NAME,values,selection,selectionArgs);
        getContext().getContentResolver().notifyChange(uri,null);
        return result;
    }
}
