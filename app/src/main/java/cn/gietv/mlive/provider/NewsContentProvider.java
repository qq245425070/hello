package cn.gietv.mlive.provider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import cn.gietv.mlive.db.DBUtils;
import cn.gietv.mlive.db.MLiveSQLiteOpenHelper;
import cn.gietv.mlive.utils.ConfigUtils;

public class NewsContentProvider extends ContentProvider {

    private static final UriMatcher MATCHER = new UriMatcher(UriMatcher.NO_MATCH);
    private MLiveSQLiteOpenHelper mHelper;
    private static final int NEWS_BEANS = 1;
    private static final int NEWS_BEAN = 2;
    static {
        MATCHER.addURI("cn.gietv.mlive.NewsProvider", DBUtils.MESSAGE_TABLE_NAME,NEWS_BEANS);
        MATCHER.addURI("cn.gietv.mlive.NewsProvider","message/#",NEWS_BEAN);
    }
    public NewsContentProvider() {
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public String getType(Uri uri) {
        switch (MATCHER.match(uri)) {
            case NEWS_BEANS:
                return "vnd.android.cursor.item/student";
            case NEWS_BEAN:
                return "vnd.android.cursor.dir/student";
            default:
                throw new IllegalArgumentException("Unkwon Uri:" + uri.toString());
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        SQLiteDatabase db = mHelper.getWritableDatabase();
        switch (MATCHER.match(uri)) {
            case NEWS_BEANS:
                long id =db.insert(DBUtils.MESSAGE_TABLE_NAME,null,values);
                Uri noteUri = ContentUris.withAppendedId(uri, id);
                getContext().getContentResolver().notifyChange(uri,null);
                return noteUri;
            default:
            throw new UnsupportedOperationException("Not yet implemented");
        }

    }

    @Override
    public boolean onCreate() {
        mHelper = new MLiveSQLiteOpenHelper(getContext(),DBUtils.DATABASE_NAME, ConfigUtils.DATABASE_VERSION_CODE);
        return false;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteDatabase db  = mHelper.getReadableDatabase();
        switch (MATCHER.match(uri)) {
            case NEWS_BEANS:
                return db.query(DBUtils.MESSAGE_TABLE_NAME,projection,selection,selectionArgs,null,null,sortOrder);
            case NEWS_BEAN:
                return db.query(DBUtils.MESSAGE_TABLE_NAME,projection,selection,selectionArgs,null,null,sortOrder);
            default:
            throw new UnsupportedOperationException("Not yet implemented");
        }
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        SQLiteDatabase db  = mHelper.getReadableDatabase();
        switch (MATCHER.match(uri)){
            case NEWS_BEANS:
                return db.update(DBUtils.MESSAGE_TABLE_NAME,values,selection,selectionArgs);
            case NEWS_BEAN:
                return db.update(DBUtils.MESSAGE_TABLE_NAME,values,selection,selectionArgs);
            default:
                throw new UnsupportedOperationException("Not yet implemented");
        }

    }
}
