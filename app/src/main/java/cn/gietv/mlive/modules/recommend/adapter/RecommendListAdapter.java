package cn.gietv.mlive.modules.recommend.adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import cn.gietv.mlive.R;
import cn.gietv.mlive.constants.CommConstants;
import cn.gietv.mlive.db.DBUtils;
import cn.gietv.mlive.modules.common.OnItemClick;
import cn.gietv.mlive.modules.game.bean.GameInfoBean;
import cn.gietv.mlive.modules.program.bean.ProgramBean;
import cn.gietv.mlive.modules.recommend.bean.RecommendBean;
import cn.gietv.mlive.modules.video.activity.LivePlayListActivity;
import cn.gietv.mlive.modules.video.activity.VideoPlayListActivity3;
import cn.gietv.mlive.utils.ImageLoaderUtils;
import cn.gietv.mlive.utils.IntentUtils;
import cn.gietv.mlive.utils.NumUtils;
import cn.gietv.mlive.utils.ViewHolder;

/**
 * author：steven
 * datetime：15/9/21 19:56
 */
public class RecommendListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int VIEW_TYPE_GAME = 1;
    private static final int VIEW_TYPE_ATTENTION = 2;
    private static final int VIEW_TYPE_LIVE_HOT = 0;
    private LayoutInflater mInflater;
    private int mShowAttentionPosition = -1;
    private ImageLoader mImageLoader;
    private OnItemClick<GameInfoBean.GameInfoEntity> mMoreClick;
    private int mFlag;
    private Context mContext;
    private List<Object> data;
    public void setMoreClick(OnItemClick<GameInfoBean.GameInfoEntity> moreClick) {
        mMoreClick = moreClick;
    }

    public RecommendListAdapter(Context context, List<Object> objects ,int flag) {
        mInflater = LayoutInflater.from(context);
        mContext = context;
        this.data = objects;
        mImageLoader = ImageLoaderUtils.getDefaultImageLoader(context);
        this.mFlag = flag-1 ;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int convertLayout = 0;
        switch (viewType) {
            case VIEW_TYPE_GAME:
                convertLayout = R.layout.recommend_list_item_game_adapter2;
                return new GameViewHolder(mInflater.inflate(convertLayout, null));
            case VIEW_TYPE_ATTENTION:
                convertLayout = R.layout.recommend_list_item_attention_adapter2;
                return new AttentionViewHolder(mInflater.inflate(convertLayout, null));
            case VIEW_TYPE_LIVE_HOT:
                convertLayout = R.layout.recommend_list_item_live_hot;
                return new LiveHotViewHolder(mInflater.inflate(convertLayout, null));
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Object o = data.get(position);
        if(holder instanceof  GameViewHolder){
            handleGameView((GameViewHolder)holder, (RecommendBean.RecommendGameProgramsEntity) o);
        }else if(holder instanceof AttentionViewHolder){
            handleAttentionView(position, (AttentionViewHolder)holder, (List<ProgramBean.ProgramEntity>) o);
        }else if(holder instanceof LiveHotViewHolder){
            handleLiveHot(holder.itemView, (ArrayList<ProgramBean.ProgramEntity>)o);
        }
    }

    @Override
    public int getItemViewType(int position) {
        Object o = data.get(position);
        if (o instanceof RecommendBean.RecommendGameProgramsEntity) {
            return VIEW_TYPE_GAME;
        }else if(o instanceof ArrayList){
            return VIEW_TYPE_ATTENTION;
        }else {
            return 0;
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    private void handleLiveHot(View convertView, ArrayList<ProgramBean.ProgramEntity> o) {
        if(o == null){
            return ;
        }
        LayoutInflater inflater =  LayoutInflater.from(mContext);
        LinearLayout liveHotParent = ViewHolder.get(convertView, R.id.hot_live_parent);
        liveHotParent.removeAllViews();
        for(int i = 0; i < o.size(); i++){
            final ProgramBean.ProgramEntity hotLive = o.get(i);
            if(hotLive == null ){
                continue;
            }
            RelativeLayout liveHot = (RelativeLayout) inflater.inflate(R.layout.recommend_list_live_hot_item, null);
            TextView videoName= (TextView) liveHot.findViewById(R.id.video_name);
            videoName.setText(hotLive.name.length() >14 ? hotLive.name.substring(0,14):hotLive.name);
            ImageView recommend_list_iv_game_1 = (ImageView) liveHot.findViewById(R.id.recommend_list_iv_game_1);
            mImageLoader.displayImage(hotLive.shareurl,recommend_list_iv_game_1);
            liveHotParent.addView(liveHot, i);
            liveHot.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle = new Bundle();
//                    bundle.putSerializable(LivePlayListActivity.EXTRA_PROGRAM, hotLive);
                    IntentUtils.openActivity(mContext, LivePlayListActivity.class, bundle);
                }
            });
        }

    }

    private void handleAttentionView(int position, AttentionViewHolder holder, final List<ProgramBean.ProgramEntity> programEntities ) {
        if(programEntities == null)
            return;
        // 我的关注
        if(position > mFlag){
            holder.title.setText("猜你喜欢");
        }else{
            holder.title.setText("推荐视频");
        }
        holder.parent.removeAllViews();
        if( programEntities != null) {
            int size = programEntities.size();
            if (programEntities.size() % 2 != 0) {
                programEntities.remove(size - 1);
            }
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            for (int i = 0; i < programEntities.size(); i = i + 2) {
                holder.parent.addView(getItemView(programEntities.get(i), programEntities.get(i + 1)), layoutParams);
            }
        }
    }

    private void handleGameView(GameViewHolder holder, final RecommendBean.RecommendGameProgramsEntity gameProgramsEntity) {
        if(gameProgramsEntity == null ){
            return;
        }
        View convertView = holder.itemView;
        LinearLayout gameContent = (LinearLayout) convertView.findViewById(R.id.game_content);
        gameContent.removeAllViews();
        TextView gameNameText = ViewHolder.get(convertView, R.id.recommend_list_tv_game_name);
        TextView gamemore = ViewHolder.get(convertView, R.id.game_more);
        if(gameProgramsEntity.game == null)
            return;
        gameNameText.setText(gameProgramsEntity.game.name);
//        if(gameProgramsEntity.game.isfollow == CommConstants.TRUE){
//            concernImage.setImageBitmap(BitmapFactory.decodeResource(mContext.getResources(), cn.gietv.mlive.R.drawable.over_concern));
//        }else{
//            concernImage.setImageBitmap(BitmapFactory.decodeResource(mContext.getResources(), cn.gietv.mlive.R.drawable.no_concern));
//        }
//        concernImage.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (UserUtils.isNotLogin()) {
//                    IntentUtils.openActivity(mContext, LoginActivity.class);
//                    return;
//                }
//                FollowModel model = RetrofitUtils.create(FollowModel.class);
//                if (gameProgramsEntity.game.isfollow == CommConstants.TRUE) {
//                    model.follow(gameProgramsEntity.game._id, CommConstants.FOLLOW_ACTION_OFF, gameProgramsEntity.game.type, new DefaultLiveHttpCallBack<String>() {
//                        @Override
//                        public void success(String s) {
//                            ToastUtils.showToast(mContext, "取消关注成功");
//                            concernImage.setImageBitmap(BitmapFactory.decodeResource(mContext.getResources(), cn.gietv.mlive.R.drawable.no_concern));
//                            gameProgramsEntity.game.follows--;
//                            gameProgramsEntity.game.isfollow = CommConstants.FALSE;
//                            downloadCountText.setText(NumUtils.w(gameProgramsEntity.game.follows) + " 人关注");
//                        }
//
//                        @Override
//                        public void failure(String message) {
//                            ToastUtils.showToast(mContext, message);
//                        }
//                    });
//                } else {
//                    model.follow(gameProgramsEntity.game._id, CommConstants.FOLLOW_ACTION_ON, gameProgramsEntity.game.type, new DefaultLiveHttpCallBack<String>() {
//                        @Override
//                        public void success(String s) {
//                            concernImage.setImageBitmap(BitmapFactory.decodeResource(mContext.getResources(), cn.gietv.mlive.R.drawable.over_concern));
//                            gameProgramsEntity.game.follows++;
//                            gameProgramsEntity.game.isfollow = CommConstants.TRUE;
//                            downloadCountText.setText(NumUtils.w(gameProgramsEntity.game.follows) + " 人关注");
//                        }
//
//                        @Override
//                        public void failure(String message) {
//                            ToastUtils.showToast(mContext, message);
//                        }
//                    });
//                }
//            }
//        });
//        gamemore.setText("更多《" + gameProgramsEntity.game.name + "》相关视频");
        gamemore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mMoreClick != null) {
                    mMoreClick.onClick(gameProgramsEntity.game);
                }
            }
        });
        if( gameProgramsEntity.programs != null) {
            int size = gameProgramsEntity.programs.size();
            if(gameProgramsEntity.programs.size() % 2 != 0){
                gameProgramsEntity.programs.remove(size-1);
            }
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            for (int i = 0;i<gameProgramsEntity.programs.size();i = i+2){
                holder.contentView.addView(getItemView(gameProgramsEntity.programs.get(i),gameProgramsEntity.programs.get(i+1)),layoutParams);
            }
        }
    }
    private View getItemView(final ProgramBean.ProgramEntity programEntity, final ProgramBean.ProgramEntity programEntity1) {
        View view = mInflater.inflate(R.layout.category_item_layout,null);
        ImageView image1 = (ImageView) view.findViewById(R.id.video_spic_1);
        ImageView image2 = (ImageView) view.findViewById(R.id.video_spic_2);
        final TextView title1 = (TextView) view.findViewById(R.id.video_title_1);
        TextView author1 = (TextView) view.findViewById(R.id.video_author1);
        TextView author2 = (TextView) view.findViewById(R.id.video_author2);
        TextView playcount1 = (TextView) view.findViewById(R.id.play_count1);
        TextView playcount2 = (TextView) view.findViewById(R.id.play_count2);
        final TextView title2 = (TextView) view.findViewById(R.id.video_title_2);
        if(programEntity == null && programEntity1 == null){
            view.setVisibility(View.GONE);
            return view;
        }
        if(programEntity != null){
            mImageLoader.displayImage(programEntity.spic,image1);
            title1.setText(programEntity.name);
            if(programEntity.userinfo != null)
                author1.setText(programEntity.userinfo.nickname);
            playcount1.setText(NumUtils.w(programEntity.onlines));
            if(null != DBUtils.getInstance(mContext).getVideoById(programEntity._id)){
                title1.setTextColor(mContext.getResources().getColor(R.color.text_929292));
            }
            image1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    VideoPlayListActivity3.openVideoPlayListActivity2(mContext, programEntity);
                    title1.setTextColor(mContext.getResources().getColor(R.color.text_929292));
                }
            });
        }else{
            image1.setVisibility(View.INVISIBLE);
            title1.setVisibility(View.INVISIBLE);
        }
        if(programEntity1 != null){
            mImageLoader.displayImage(programEntity1.spic,image2);
            title2.setText(programEntity1.name);
            if(programEntity1.userinfo != null)
                author2.setText(programEntity1.userinfo.nickname);
            playcount2.setText(NumUtils.w(programEntity1.onlines));
            image2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    VideoPlayListActivity3.openVideoPlayListActivity2(mContext, programEntity1);
                    title2.setTextColor(mContext.getResources().getColor(R.color.text_929292));
                }
            });
            if(null != DBUtils.getInstance(mContext).getVideoById(programEntity1._id)){
                title2.setTextColor(mContext.getResources().getColor(R.color.text_929292));
            }
        }else{
            image2.setVisibility(View.INVISIBLE);
            title2.setVisibility(View.INVISIBLE);
        }
        return view;
    }

    private void handleGamePrograms(ImageView image, ImageView liveIcon, final ProgramBean.ProgramEntity gameProgramsEntity) {
        mImageLoader.displayImage(gameProgramsEntity.spic,image);
        if (gameProgramsEntity.type == 1) {
            liveIcon.setVisibility(View.VISIBLE);
        } else {
            liveIcon.setVisibility(View.GONE);
        }
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (gameProgramsEntity.type == CommConstants.CAROUSEL_TYPE_LIVE) {
                    Bundle bundle = new Bundle();
                    IntentUtils.openActivity(mContext, LivePlayListActivity.class, bundle);
                } else {
                    VideoPlayListActivity3.openVideoPlayListActivity2(mContext, gameProgramsEntity);
                }

            }
        });
    }
    class GameViewHolder extends RecyclerView.ViewHolder{
        LinearLayout contentView;
        public GameViewHolder(View itemView) {
            super(itemView);
            contentView = (LinearLayout) itemView.findViewById(R.id.game_content);
        }
    }
    class AttentionViewHolder extends  RecyclerView.ViewHolder{
        TextView title;
        LinearLayout parent;
        public AttentionViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.recommend_list_tv_my_attention);
            parent = (LinearLayout) itemView.findViewById(R.id.video_parent);
        }
    }
    class LiveHotViewHolder extends RecyclerView.ViewHolder{
        public LiveHotViewHolder(View itemView) {
            super(itemView);
        }
    }
}
