package cn.gietv.mlive.modules.follow.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

import cn.gietv.mlive.R;
import cn.gietv.mlive.db.DBUtils;
import cn.gietv.mlive.modules.author.activity.AuthorActivity;
import cn.gietv.mlive.modules.category.activity.CategoryActivity;
import cn.gietv.mlive.modules.program.bean.ProgramBean;
import cn.gietv.mlive.modules.usercenter.bean.UserCenterBean;
import cn.gietv.mlive.modules.video.activity.VideoPlayListActivity3;
import cn.gietv.mlive.utils.DensityUtil;
import cn.gietv.mlive.utils.ImageLoaderUtils;
import cn.gietv.mlive.utils.NumUtils;

/**
 * Created by houde on 2016/8/24.
 */
public class FollowAreaAdapter extends RecyclerView.Adapter<FollowAreaAdapter.FollowViewHolder> {
    private Context mContext;
    private List<ProgramBean.ProgramEntity> data;
    private ImageLoader mImageLoader;
    private TextView temText;
    public FollowAreaAdapter(Context context,List<ProgramBean.ProgramEntity> list){
        this.mContext = context;
        data = list;
        mImageLoader = ImageLoaderUtils.getDefaultImageLoader(mContext);
    }
    @Override
    public FollowViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new FollowViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_follow_area,null));
    }

    @Override
    public void onBindViewHolder(final FollowViewHolder holder, int position) {
        final ProgramBean.ProgramEntity bean = data.get(position);
        if(bean == null)
            return;
        if( bean.spic != null &&bean.spic.equals(holder.videoImage.getTag())){

        }else {
            mImageLoader.displayImage(bean.spic, holder.videoImage);
            holder.videoImage.setTag(bean.spic);
        }
        holder.videoImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VideoPlayListActivity3.openVideoPlayListActivity2(mContext,bean);
                holder.videoName.setTextColor(mContext.getResources().getColor(R.color.text_929292));
            }
        });
        holder.videoName.setText(bean.name);
        if(null != DBUtils.getInstance(mContext).getVideoById(bean._id)){
            holder.videoName.setTextColor(mContext.getResources().getColor(R.color.text_929292));
        }
        if(bean.userinfo != null) {
            final UserCenterBean.UserinfoEntity user = bean.userinfo;
            holder.authorName.setText(bean.userinfo.nickname);
            mImageLoader.displayImage(bean.userinfo.avatar, holder.authorImage);
            holder.authorImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AuthorActivity.openAnchorActivity(user.nickname,user._id,mContext,user.isfollow);
                }
            });
        }
        holder.commenNum.setText(NumUtils.w(bean.message_cnt));
        holder.playNum.setText(NumUtils.w(bean.onlines));
        if(bean.tags != null && bean.tags.size() > 0 ){
            holder.tagParent.setVisibility(View.VISIBLE);
            holder.tagParent.removeAllViews();
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            lp.rightMargin = DensityUtil.dip2px(mContext,8);
            for(int i = 0; i< bean.tags.size();i++){
                final TextView textView = (TextView) LayoutInflater.from(mContext).inflate(R.layout.flowlayout_text,null);
                textView.setText(bean.tags.get(i));
                textView.setTextColor(mContext.getResources().getColor(R.color.text_929292));
                textView.setBackgroundResource(R.drawable.commen_button_theme_color5);
                final int finalI = i;
                textView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (temText != null) {
                            temText.setBackgroundResource(R.drawable.commen_button_theme_color5);
                            temText.setTextColor(mContext.getResources().getColor(R.color.text_929292));
                        }
                        textView.setBackgroundResource(R.drawable.commen_button_light_green2);
                        textView.setTextColor(Color.parseColor("#4fc396"));
                        CategoryActivity.openActivity(mContext, bean.tags.get(finalI), bean.tags.get(finalI), 0);
                        temText = textView;
                    }
                });
                holder.tagParent.addView(textView,lp);
            }
        }else{
            holder.tagParent.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount() {
        return data.size();
    }
    public class FollowViewHolder extends ViewHolder{
        TextView videoName,authorName,commenNum,playNum;
        ImageView videoImage,authorImage;
        LinearLayout tagParent;
        public FollowViewHolder(View itemView) {
            super(itemView);
            videoName = (TextView) itemView.findViewById(R.id.video_name);
            authorName = (TextView) itemView.findViewById(R.id.author_name);
            commenNum = (TextView) itemView.findViewById(R.id.commen_count);
            playNum = (TextView) itemView.findViewById(R.id.play_count);
            videoImage =(ImageView) itemView.findViewById(R.id.video_image);
            tagParent = (LinearLayout) itemView.findViewById(R.id.tag_parent);
            authorImage = (ImageView) itemView.findViewById(R.id.author_image);
        }
    }
}
