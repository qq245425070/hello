package cn.gietv.mlive.modules.news.adapter;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import cn.gietv.mlive.R;
import cn.gietv.mlive.modules.compere.activity.CompereActivity;
import cn.gietv.mlive.utils.CacheUtils;
import cn.gietv.mlive.utils.ConfigUtils;
import cn.gietv.mlive.utils.ImageLoaderUtils;

/**
 * Created by houde on 2015/12/17.
 */
public class NewsInfoAdapter extends CursorAdapter {

    private Context mContext;
    private ImageLoader mImageLoader;
    public NewsInfoAdapter(Context context, Cursor c) {
        super(context, c);
        this.mContext = context;
        mImageLoader = ImageLoaderUtils.getDefaultImageLoader(context);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.news_info_item,null);
        ViewHolder viewHolder = new ViewHolder();
        viewHolder.avastarLeft = (ImageView) view.findViewById(R.id.new_item_iv_avatar);
        viewHolder.avastarRight = (ImageView) view.findViewById(R.id.new_item_iv_avatar_right);
        viewHolder.messageLeft = (TextView) view.findViewById(R.id.news_message);
        viewHolder.messageRight = (TextView) view.findViewById(R.id.news_message_right);
        viewHolder.leftParent = (LinearLayout) view.findViewById(R.id.new_item_left_parent);
        viewHolder.rightParent = (LinearLayout) view.findViewById(R.id.new_item_right_parent);
        view.setTag(viewHolder);
        return view;
    }

    @Override
    public void bindView(View view, final Context context, final Cursor cursor) {
        ViewHolder viewHolder = (ViewHolder) view.getTag();
        if(cursor.getInt(6) == ConfigUtils.FROM_ME){
            viewHolder.leftParent.setVisibility(View.GONE);
            viewHolder.rightParent.setVisibility(View.VISIBLE);
            viewHolder.messageRight.setText(cursor.getString(5));
            mImageLoader.displayImage(cursor.getString(2), viewHolder.avastarRight);
            viewHolder.avastarRight.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CompereActivity.openCompereActivity(context,CacheUtils.getCacheUserInfo()._id);
                }
            });

        }else{
            viewHolder.leftParent.setVisibility(View.VISIBLE);
            viewHolder.rightParent.setVisibility(View.GONE);
            viewHolder.messageLeft.setText(cursor.getString(5));
            mImageLoader.displayImage(cursor.getString(2), viewHolder.avastarLeft);
            viewHolder.avastarLeft.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CompereActivity.openCompereActivity(context,cursor.getString(1));
                }
            });
        }

        if(ConfigUtils.SYSTEM_MSG_ACTIVITY.equals(cursor.getString(9))){
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }else if(ConfigUtils.SYSTEM_MSG_VIDEO.equals(cursor.getString(9))){
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }else if(ConfigUtils.SYSTEM_MSG_LIVE.equals(cursor.getString(9))){
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        } else if(ConfigUtils.SYSTEM_MSG_OPEN.equals(cursor.getString(9))){
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        } else if(ConfigUtils.SYSTEM_MSG_TRAILER.equals(cursor.getString(9))){
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }

    }
    public class ViewHolder {
        LinearLayout rightParent;
        LinearLayout leftParent;
        TextView messageLeft;
        TextView messageRight;
        ImageView avastarLeft;
        ImageView avastarRight;
    }
}
   /* @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            switch (getItemViewType(position)){
                case FROM_ME:
                    convertView = LayoutInflater.from(mContext).inflate(R.layout.news_info_item,null);
                    break;
                case FROM_OTHER:
                    convertView = LayoutInflater.from(mContext).inflate(R.layout.news_info_left_item,null);
            }
        }
        return convertView;
        return null;
    }*/

/*    @Override
    public int getViewTypeCount() {
        return 2;
    }*/

 /*   @Override
    public int getItemViewType(int position) {
        if (((Cursor)getItem(position)).getColumnIndex("to_from") == FROM_ME) {
            return FROM_ME;
        } else {
            return FROM_OTHER;
        }
    }*/

/*
private static final int FROM_OTHER = 0;
private static final int FROM_ME = 1;
private Context mContext;
private List<NewsBean> newsBeans;
private ImageLoader mImageLoader;
    public NewsInfoAdapter(Context context, List<NewsBean> newsBeans) {
        super();
        this.mContext = context;
        this.newsBeans = newsBeans;
        mImageLoader = ImageLoaderUtils.getDefaultImageLoader(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            switch (getItemViewType(position)){
                case FROM_ME:
                    convertView = LayoutInflater.from(mContext).inflate(R.layout.news_info_item,null);
                    break;
                case FROM_OTHER:
                    convertView = LayoutInflater.from(mContext).inflate(R.layout.news_info_left_item,null);
            }
        }
        TextView message = ViewHolder.get(convertView,R.id.news_message);
        ImageView avatar = ViewHolder.get(convertView,R.id.new_item_iv_avatar);
        NewsBean newsBean = newsBeans.get(position);
        mImageLoader.displayImage(newsBean.avatar, avatar);
        message.setText(newsBean.message);
        return convertView;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        if(getItem(position).from_me == FROM_ME){
            return  FROM_ME;
        }else {
            return FROM_OTHER;
        }
    }*/
