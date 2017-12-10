package cn.gietv.mlive.modules.category.adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.siyamed.shapeimageview.RoundedImageView;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import cn.gietv.mlive.R;
import cn.gietv.mlive.base.AbsBaseActivity;
import cn.gietv.mlive.constants.CommConstants;
import cn.gietv.mlive.db.DBUtils;
import cn.gietv.mlive.modules.program.bean.ProgramBean;
import cn.gietv.mlive.modules.recommend.adapter.RecommendPagerAdapter;
import cn.gietv.mlive.modules.recommend.bean.RecommendBean;
import cn.gietv.mlive.modules.video.activity.LivePlayListActivity;
import cn.gietv.mlive.modules.video.activity.VideoPlayListActivity3;
import cn.gietv.mlive.utils.ImageLoaderUtils;
import cn.gietv.mlive.utils.IntentUtils;
import cn.gietv.mlive.utils.NumUtils;
import cn.gietv.mlive.utils.ViewHolder;
import me.relex.circleindicator.CircleIndicator;

/**
 * Created by houde on 2016/8/17.
 */
public class RecommendAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int VIEW_TYPE_GAME = 1;
    private static final int VIEW_TYPE_ATTENTION = 2;
    private static final int VIEW_TYPE_RECOMMENT = 3;
    private static final int TYPE_HEADER = 4;
    public int i=0;
    private LayoutInflater mInflater;
    private Context mContext;
    private List<Object> data;
    private ImageLoader mImageLoader;
    private ArrayList<RecommendBean.RecommendCarouseEntity> recommendCarouseEntities;

    public RecommendAdapter(Context context,List<Object> list){
        mInflater = LayoutInflater.from(context);

        mImageLoader = ImageLoaderUtils.getDefaultImageLoader(mContext);
        this.mContext = context;
        this.data = list;
        recommendCarouseEntities = new ArrayList<>();
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int convertLayout = 0;
        switch (viewType) {
            case TYPE_HEADER:
                View view = LayoutInflater.from(mContext).inflate(R.layout.recycler_header, parent, false);
                return new RecyclerHeaderViewHolder(view);
            case VIEW_TYPE_GAME:
                convertLayout = R.layout.category_recommend_layout;
                return new GameViewHolder(mInflater.inflate(convertLayout, null));
            case VIEW_TYPE_ATTENTION:
                convertLayout = R.layout.recommend_list_item_attention_adapter;
                return new AttentionViewHolder(mInflater.inflate(convertLayout, null));
            case VIEW_TYPE_RECOMMENT:
                convertLayout = R.layout.recommend_head_layout;
                return new RecommentViewHolder(mInflater.inflate(convertLayout, null));
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(position > 0) {
            Object o = data.get(position-1);
            if (holder instanceof GameViewHolder) {
                handleGameView((GameViewHolder) holder, (RecommendBean.RecommendGameProgramsEntity) o);
            } else if (holder instanceof AttentionViewHolder) {
                handleAttentionView(position, holder.itemView, (ProgramBean.ProgramEntity) o);

            } else if (holder instanceof RecommentViewHolder) {
                handleRecomment(holder.itemView, (ArrayList<RecommendBean.RecommendCarouseEntity>) o);
            }
        }
    }
    private void handleAttentionView(int position, View convertView, final ProgramBean.ProgramEntity gameEntity ) {
        TextView myAttention = ViewHolder.get(convertView, R.id.recommend_list_tv_my_attention);
        TextView gameNameText = ViewHolder.get(convertView, R.id.recommend_tv_game_name);
        TextView gameDescText = ViewHolder.get(convertView, R.id.recommend_list_tv_game_desc);
        TextView playCountText = ViewHolder.get(convertView, R.id.recommend_list_tv_play_count);
        TextView attentionCountText = ViewHolder.get(convertView, R.id.recommend_list_tv_attention_count);
        RoundedImageView showImage = ViewHolder.get(convertView, R.id.recommend_list_iv_game_attention);
        View line = ViewHolder.get(convertView,R.id.line1);
        Log.e("ceshi","i="+i);
        if(i==0){
            myAttention.setVisibility(View.VISIBLE);
        }else{
            myAttention.setVisibility(View.GONE);
        }
        i=i+1;
        line.setVisibility(View.GONE);
        gameNameText.setText(gameEntity.name);
        if (CommConstants.CAROUSEL_TYPE_LIVE == gameEntity.type) {
            ViewHolder.get(convertView, R.id.image_live).setVisibility(View.VISIBLE);
        } else {
            ViewHolder.get(convertView, R.id.image_live).setVisibility(View.GONE);
        }
        if(gameEntity.userinfo == null){
            return;
        }
        gameDescText.setText(gameEntity.userinfo.nickname);
        playCountText.setText(NumUtils.w(gameEntity.onlines));
        attentionCountText.setText(NumUtils.w(gameEntity.follows));
        mImageLoader.displayImage(gameEntity.spic, showImage);
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (gameEntity.type == CommConstants.CAROUSEL_TYPE_LIVE) {
                    Bundle bundle = new Bundle();
                    IntentUtils.openActivity(mContext, LivePlayListActivity.class, bundle);
                } else {
                    VideoPlayListActivity3.openVideoPlayListActivity2(mContext, gameEntity);
                }
            }
        });
    }

    private void handleGameView(GameViewHolder holder, RecommendBean.RecommendGameProgramsEntity o) {
        if(o == null)
            return;
        if(o.game != null)
            holder.title.setText(o.game.name);
        holder.contentParent.removeAllViews();
        if(o.programs!= null && o.programs.size()> 0){
            int size = o.programs.size();
            if(o.programs.size() % 2 != 0){
                o.programs.remove(size - 1);
            }
            for (int i = 0;i<o.programs.size();i = i+2){
                holder.contentParent.addView(getItemView(o.programs.get(i),o.programs.get(i+1)));
            }
        }
    }

    private View getItemView(final ProgramBean.ProgramEntity programEntity, final ProgramBean.ProgramEntity programEntity1) {
        View view = mInflater.inflate(R.layout.category_item_layout,null);
        ImageView image1 = (ImageView) view.findViewById(R.id.video_spic_1);
        ImageView image2 = (ImageView) view.findViewById(R.id.video_spic_2);
        final TextView title1 = (TextView) view.findViewById(R.id.video_title_1);
        final TextView title2 = (TextView) view.findViewById(R.id.video_title_2);
        TextView author1 = (TextView) view.findViewById(R.id.video_author1);
        TextView author2 = (TextView) view.findViewById(R.id.video_author2);
        TextView playCount1 = (TextView) view.findViewById(R.id.play_count1);
        TextView playCount2 = (TextView) view.findViewById(R.id.play_count2);
        if(programEntity == null && programEntity1 == null){
            view.setVisibility(View.GONE);
            return view;
        }
        if(programEntity != null){
            mImageLoader.displayImage(programEntity.spic,image1);
            title1.setText(programEntity.name);
            if(DBUtils.getInstance(mContext).getVideoById(programEntity._id) != null){
                title1.setTextColor(mContext.getResources().getColor(R.color.text_929292));
            }
            image1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    VideoPlayListActivity3.openVideoPlayListActivity2(mContext, programEntity);
                    title1.setTextColor(mContext.getResources().getColor(R.color.text_929292));
                }
            });
            if(programEntity.userinfo != null){
                author1.setText(programEntity.userinfo.nickname);
                playCount1.setText(NumUtils.w(programEntity.onlines));
            }
        }else{
            image1.setVisibility(View.INVISIBLE);
            title1.setVisibility(View.INVISIBLE);
        }
        if(programEntity1 != null){
            mImageLoader.displayImage(programEntity1.spic,image2);
            title2.setText(programEntity1.name);
            if(DBUtils.getInstance(mContext).getVideoById(programEntity._id) != null){
                title2.setTextColor(mContext.getResources().getColor(R.color.text_929292));
            }
            image2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    VideoPlayListActivity3.openVideoPlayListActivity2(mContext, programEntity1);
                    title2.setTextColor(mContext.getResources().getColor(R.color.text_929292));
                }
            });
            if(programEntity1.userinfo != null){
                author2.setText(programEntity1.userinfo.nickname);
                playCount2.setText(NumUtils.w(programEntity1.onlines));
            }
        }else{
            image2.setVisibility(View.INVISIBLE);
            title2.setVisibility(View.INVISIBLE);
        }
        return view;
    }

    @Override
    public int getItemViewType(int position) {
        if(position > 0) {
            Object o = data.get(position - 1);
            if (o instanceof RecommendBean.RecommendGameProgramsEntity) {
                return VIEW_TYPE_GAME;
            } else if (o instanceof ProgramBean.ProgramEntity) {
                return VIEW_TYPE_ATTENTION;
            } else if (o instanceof ArrayList) {
                return VIEW_TYPE_RECOMMENT;
            } else {
                return 0;
            }
        }else{
            return TYPE_HEADER;
        }
    }

    @Override
    public int getItemCount() {
        return data == null ? 1 : data.size() + 1;
    }
    ViewPager mViewPager;
    RecommendPagerAdapter mPagerAdapter;
    private void handleRecomment(View convertView, ArrayList<RecommendBean.RecommendCarouseEntity> o) {
        if(o == null){
            return;
        }
        recommendCarouseEntities.clear();
        recommendCarouseEntities.addAll(o);
        if(mViewPager == null )
            mViewPager = (ViewPager) convertView.findViewById(R.id.recommend_vp_page);
        if(mPagerAdapter == null) {
            mPagerAdapter = new RecommendPagerAdapter(((AbsBaseActivity) mContext).getSupportFragmentManager(), recommendCarouseEntities);
            mViewPager.setAdapter(mPagerAdapter);
        }else{
            mPagerAdapter.notifyDataSetChanged();
        }
        CircleIndicator mIndicator = (CircleIndicator) convertView.findViewById(R.id.indicator);
        mIndicator.setViewPager(mViewPager);
        mIndicator.onPageSelected(0);
        mViewPager.setCurrentItem(0);
    }
    class GameViewHolder extends RecyclerView.ViewHolder{
        LinearLayout contentParent;
        TextView title;
        public GameViewHolder(View itemView) {
            super(itemView);
            contentParent = (LinearLayout) itemView.findViewById(R.id.content_parent);
            title = (TextView) itemView.findViewById(R.id.title);
        }
    }
    class AttentionViewHolder extends  RecyclerView.ViewHolder{

        public AttentionViewHolder(View itemView) {
            super(itemView);
        }
    }
    class RecommentViewHolder extends RecyclerView.ViewHolder{

        public RecommentViewHolder(View itemView) {
            super(itemView);
        }
    }
//    class LiveHotViewHolder extends RecyclerView.ViewHolder{
//        public LiveHotViewHolder(View itemView) {
//            super(itemView);
//        }
//    }
    private static class RecyclerHeaderViewHolder extends RecyclerView.ViewHolder {

        public RecyclerHeaderViewHolder(View itemView) {
            super(itemView);
        }
    }
}
