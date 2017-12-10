package cn.gietv.mlive.utils;

/**
 * Created by houde on 2015/12/17.
 */
import android.content.AsyncQueryHandler;
import android.content.ContentResolver;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;

public class MLiveQueryHandler extends AsyncQueryHandler {

    public MLiveQueryHandler(ContentResolver cr) {
        super(cr);
    }

    @Override
    /**
     * 异步查询结束后，会回调此方法
     * @param token  是 startQuery 方法中的第一个参数，用于标记是哪次查询
     * @param cookie 是 startQuery 方法中的第二个参数，是一个对象
     * @param cursor 是 startQuery 方法的返回结果
     */
    protected void onQueryComplete(int token, Object cookie, Cursor cursor) {

        // 触发异步查询完成后的监听
        if(onQueryCompleteListener!=null){
            onQueryCompleteListener.onQueryComplete(token, cookie, cursor);
        }


        if(cookie instanceof CursorAdapter){
            CursorAdapter adapter = (CursorAdapter) cookie;
            // 将cursor 设置给adapter
            // adapter 会自动刷新listView
            adapter.changeCursor(cursor);

//			listView.setSelection(adapter.getCount()-1); // 让listView 显示最后一条信息
            // 触发 cursor 改变后的事件监听
            if(cursurChagedListener!=null){
                cursurChagedListener.onCursorChanged(token, cookie, cursor);
            }

        }
    }

    private IOnQueryCompleteListener onQueryCompleteListener;

    public void setOnQueryCompleteListener(IOnQueryCompleteListener onQueryCompleteListener){
        this.onQueryCompleteListener = onQueryCompleteListener;
    }

    /**
     * 当异步查询完成后，的回调接口
     * @author leo
     *
     */
    public interface IOnQueryCompleteListener{
        /**
         * 当 查询完成以后，回调此方法
         * @param token
         * @param cookie
         * @param cursor
         */
        void onQueryComplete(int token, Object cookie, Cursor cursor);
    }


    private IOnCursurChagedListener cursurChagedListener;

    public void setOnCursorChangedListener(IOnCursurChagedListener cursurChagedListener){
        this.cursurChagedListener = cursurChagedListener;
    }

    /**
     * 当cursor 发后改变时的回调接口
     * @author leo
     *
     */
    public interface IOnCursurChagedListener{

        /**
         * 当异步查询完成后，cursor 改变以后，回调此方法
         * @param token
         * @param cookie
         * @param cursor
         */
        void onCursorChanged(int token, Object cookie, Cursor cursor);

    }



}
