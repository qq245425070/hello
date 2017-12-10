package cn.gietv.mlive.modules.vrgame.adapter;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.text.DecimalFormat;
import java.util.List;

import cn.gietv.mlive.R;
import cn.gietv.mlive.modules.compere.activity.CompereActivity;
import cn.gietv.mlive.modules.video.bean.MessageBean;
import cn.gietv.mlive.modules.vrgame.activity.WriteCommentActivity;
import cn.gietv.mlive.utils.ImageLoaderUtils;
import cn.gietv.mlive.utils.IntentUtils;
import cn.gietv.mlive.utils.TimeUtil;

/**
 * Created by houde on 2016/6/24.
 */
public class GameCommentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final int SCORE = 0;
    private final int COMMENT = 1;
    private ImageLoader mImageLoad;
    private LayoutInflater mInflater;
    private String mID;
    private DecimalFormat decimalFormat;
    private Context mContext;
    private List<Object> mData;
    public GameCommentAdapter(Context context, List<Object> objects,String id) {
        mImageLoad = ImageLoaderUtils.getDefaultImageLoader(context);
        mInflater = LayoutInflater.from(context);
        this.mID = id;
        decimalFormat = new DecimalFormat(".0");
        this.mContext = context;
        this.mData = objects;
    }

  @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
      if(viewType == SCORE ) {
          return new ScoreViewHolder(LayoutInflater.from(mContext).inflate(R.layout.game_comment_score_item,null));
      }else if(viewType == COMMENT){
          return new CommentViewHolder(LayoutInflater.from(mContext).inflate(R.layout.game_comment_item,null));
      }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof ScoreViewHolder){
            handleScore(holder.itemView, position);
        }else if(holder instanceof CommentViewHolder){
            handleComment(holder.itemView, position);
        }
    }

    @Override
    public int getItemViewType(int position) {
        Object o = getItem(position);
        if (o instanceof MessageBean.Scoreratio){
            return SCORE;
        }
        return COMMENT;
    }

    private Object getItem(int position) {
        return mData.get(position);
    }


    @Override
    public int getItemCount() {
        return mData.size();
    }

    private void handleComment(View convertView,int position) {
        final MessageBean.MessagesEntity message = (MessageBean.MessagesEntity) getItem(position);
        if(message == null)
            return ;

        ImageView avatar = (ImageView) convertView.findViewById(R.id.avatar_image);
        TextView nickname= (TextView) convertView.findViewById(R.id.nickname);
        TextView createDate = (TextView) convertView.findViewById(R.id.create_date);
        RatingBar ratingBar = (RatingBar) convertView.findViewById(R.id.rating_bar);
        TextView comment = (TextView) convertView.findViewById(R.id.comment_content);
        mImageLoad.displayImage(message.avatar,avatar);
        nickname.setText(message.nickname);
        createDate.setText(TimeUtil.format(message.date));
        ratingBar.setRating(message.score);
        comment.setText(message.message);
        avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CompereActivity.openCompereActivity(mContext, message.uid);
            }
        });
    }

    private void handleScore(View convertView,int position) {
        MessageBean.Scoreratio scoreratio = (MessageBean.Scoreratio) getItem(position);
        RatingBar scoreRatingBar = (RatingBar) convertView.findViewById(R.id.rating_bar_score);
        TextView score = (TextView) convertView.findViewById(R.id.score);
        if(scoreratio.score == 0){
            score.setText("3");
            scoreRatingBar.setRating(3);
        }else{
            score.setText(String.valueOf(scoreratio.score));
            scoreRatingBar.setRating(scoreratio.score);
        }

        View line1 = convertView.findViewById(R.id.line1);
        View line1_1 = convertView.findViewById(R.id.line1_1);
        TextView proportion1 = (TextView) convertView.findViewById(R.id.proportion1);
        setData(line1,line1_1,proportion1,scoreratio.score_cnt,scoreratio.score_5);

        View line2 = convertView.findViewById(R.id.line2);
        View line1_2 = convertView.findViewById(R.id.line1_2);
        TextView proportion2 = (TextView) convertView.findViewById(R.id.proportion2);
        setData(line2,line1_2,proportion2,scoreratio.score_cnt,scoreratio.score_4);

        View line3 = convertView.findViewById(R.id.line3);
        View line1_3 = convertView.findViewById(R.id.line1_3);
        TextView proportion3 = (TextView) convertView.findViewById(R.id.proportion3);
        setData(line3,line1_3,proportion3,scoreratio.score_cnt,scoreratio.score_3);

        View line4 = convertView.findViewById(R.id.line4);
        View line1_4 = convertView.findViewById(R.id.line1_4);
        TextView proportion4 = (TextView) convertView.findViewById(R.id.proportion4);
        setData(line4,line1_4,proportion4,scoreratio.score_cnt,scoreratio.score_2);

        View line5 = convertView.findViewById(R.id.line5);
        View line1_5 = convertView.findViewById(R.id.line1_5);
        TextView proportion5 = (TextView) convertView.findViewById(R.id.proportion5);
        setData(line5,line1_5,proportion5,scoreratio.score_cnt,scoreratio.score_1);

        TextView writeComment = (TextView) convertView.findViewById(R.id.write_comment);
        writeComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("id",mID);
                IntentUtils.openActivityForResult((Activity) mContext, WriteCommentActivity.class, 66, bundle);
            }
        });

    }

    private void setData(View line1,View line1_1,TextView proportion,int cnt,int score){
        LinearLayout.LayoutParams params1 = (LinearLayout.LayoutParams) line1.getLayoutParams();
        LinearLayout.LayoutParams params1_1 = (LinearLayout.LayoutParams) line1_1.getLayoutParams();
        if(score == 0){
            params1.weight = 0;
            line1.setLayoutParams(params1);
            params1_1.weight = 100;
            line1_1.setLayoutParams(params1_1);
            proportion.setText("0.0%");
        }else{
            String result = "0.0";
            if(cnt != 0){
                result = decimalFormat.format((float)score * 100 /cnt);
            }
            float resultFloat =  Float.valueOf(result);
            proportion.setText(result + "%");
            params1.weight = resultFloat;
            line1.setLayoutParams(params1);
            params1_1.weight = 100 - resultFloat;
            line1_1.setLayoutParams(params1_1);
        }
    }
    class ScoreViewHolder extends RecyclerView.ViewHolder{
        public ScoreViewHolder(View itemView) {
            super(itemView);
        }
    }
    class CommentViewHolder extends RecyclerView.ViewHolder {
        public CommentViewHolder(View itemView) {
            super(itemView);
        }
    }
}
