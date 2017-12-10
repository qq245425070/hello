package cn.gietv.mlive.modules.news.adapter;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import cn.gietv.mlive.R;
import cn.gietv.mlive.utils.ImageLoaderUtils;
import cn.gietv.mlive.utils.TimeUtil;

/**
 * Created by houde on 2015/11/27.
 */
public class NewsListAdapter extends CursorAdapter{
    private Context mContext;
    private ImageLoader mImageLoader ;
    private Cursor mCursor;
    public NewsListAdapter(Context context, Cursor c) {
        super(context, c);
        this.mContext = context;
        this.mImageLoader = ImageLoaderUtils.getDefaultImageLoader(context);
    }

    @Override
    public View newView(final Context context,final Cursor cursor, ViewGroup parent) {
        View convertView = LayoutInflater.from(mContext).inflate(R.layout.news_item_adapter,null);
        ViewHolder viewHolder = new ViewHolder();
        viewHolder.message = (TextView) convertView.findViewById(R.id.msg);
        viewHolder.nickname = (TextView) convertView.findViewById(R.id.nickname);
        viewHolder.time = (TextView) convertView.findViewById(R.id.time);
        viewHolder.avatar = (ImageView) convertView.findViewById(R.id.new_item_iv_avatar);
        viewHolder.count = (TextView) convertView.findViewById(R.id.count);
        convertView.setTag(viewHolder);
        /*convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NewsInfoActivity.openNewsInfoActivity(context, cursor.getString(1), cursor.getString(3), cursor.getString(2));
            }
        });*/
        return convertView;
    }

    @Override
    public void bindView(View view, Context context, final Cursor cursor) {
        mCursor = cursor;
        ViewHolder viewHolder = (ViewHolder) view.getTag();
        if(cursor.getInt(6) == 0){
            viewHolder.count.setVisibility(View.INVISIBLE);
        }else {
            viewHolder.count.setVisibility(View.VISIBLE);
            viewHolder.count.setText(cursor.getInt(6) + "");
        }
        mImageLoader.displayImage(cursor.getString(2), viewHolder.avatar);
        viewHolder.time.setText(TimeUtil.getChatTime(cursor.getLong(4)));
        viewHolder.nickname.setText(cursor.getString(3));
        viewHolder.message.setText(cursor.getString(5));
    }
    public class ViewHolder {
        TextView nickname;
        TextView message;
        TextView time;
        TextView count;
        ImageView avatar;
    }
}
