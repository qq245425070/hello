package cn.gietv.mlive.modules.program.adapter;

import android.content.Context;
import android.os.Bundle;
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
import cn.gietv.mlive.base.AbsArrayAdapter;
import cn.gietv.mlive.constants.CommConstants;
import cn.gietv.mlive.modules.common.OnItemClick;
import cn.gietv.mlive.modules.game.bean.GameInfoBean;
import cn.gietv.mlive.modules.program.bean.ProgramBean;
import cn.gietv.mlive.modules.recommend.bean.RecommendBean;
import cn.gietv.mlive.modules.video.activity.LivePlayListActivity;
import cn.gietv.mlive.modules.video.activity.VideoPlayListActivity3;
import cn.gietv.mlive.utils.DensityUtil;
import cn.gietv.mlive.utils.ImageLoaderUtils;
import cn.gietv.mlive.utils.IntentUtils;
import cn.gietv.mlive.utils.NumUtils;
import cn.gietv.mlive.utils.ViewHolder;
import cn.gietv.mlive.views.SquareImageView;

/**
 * Created by houde on 2016/5/17.
 */
public class VRRecommendListAdapter extends AbsArrayAdapter<Object> {
    private static final int VIEW_TYPE_GAME = 1;
    private static final int VIEW_TYPE_ATTENTION = 2;
    private static final int VIEW_TYPE_LIVE_HOT = 0;
    private LayoutInflater mInflater;
    private int mShowAttentionPosition = -1;
    private ImageLoader mImageLoader;
    private OnItemClick<GameInfoBean.GameInfoEntity> mMoreClick;

    public void setMoreClick(OnItemClick<GameInfoBean.GameInfoEntity> moreClick) {
        mMoreClick = moreClick;
    }

    public VRRecommendListAdapter(Context context, List<Object> objects) {
        super(context, objects);
        mInflater = LayoutInflater.from(getContext());
        mImageLoader = ImageLoaderUtils.getDefaultImageLoader(context);
    }

    @Override
    public int getViewTypeCount() {
        return 3;
    }

    @Override
    public int getItemViewType(int position) {
        Object o = getItem(position);
        if (o instanceof RecommendBean.RecommendGameProgramsEntity) {
            return VIEW_TYPE_GAME;
        } else if(o instanceof ProgramBean.ProgramEntity){
            return VIEW_TYPE_ATTENTION;
        }else {
            return 0;
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            System.out.println(position + "............................");
            int convertLayout = 0;
            switch (getItemViewType(position)) {
                case VIEW_TYPE_GAME:
                    convertLayout = R.layout.recommend_list_item_game_vr_adapter;
                    break;
                case VIEW_TYPE_ATTENTION:
                    convertLayout = R.layout.recommend_list_item_attention_adapter;
                    break;
                case VIEW_TYPE_LIVE_HOT:
                    convertLayout = R.layout.recommend_list_item_live_hot;
                    break;
            }
            convertView = mInflater.inflate(convertLayout, null);
        }
        Object o = getItem(position);
        switch (getItemViewType(position)) {
            case VIEW_TYPE_GAME:
                handleGameView(convertView, (RecommendBean.RecommendGameProgramsEntity) o);
                break;
            case VIEW_TYPE_ATTENTION:
                handleAttentionView(position, convertView, (ProgramBean.ProgramEntity) o);
                break;
            case VIEW_TYPE_LIVE_HOT:
                handleLiveHot(convertView, (ArrayList<ProgramBean.ProgramEntity>)o);
                break;
        }
        return convertView;
    }

    private void handleLiveHot(View convertView, ArrayList<ProgramBean.ProgramEntity> o) {
        if(o == null){
            return ;
        }
        LayoutInflater inflater =  LayoutInflater.from(getContext());
        LinearLayout liveHotParent = ViewHolder.get(convertView, R.id.hot_live_parent);
        liveHotParent.removeAllViews();
        for(int i = 0; i < o.size(); i++){
            final ProgramBean.ProgramEntity hotLive = o.get(i);
            RelativeLayout liveHot = (RelativeLayout) inflater.inflate(R.layout.recommend_list_live_hot_item, null);
            TextView videoName= (TextView) liveHot.findViewById(R.id.video_name);
            videoName.setText(hotLive.name.length() >14 ? hotLive.name.substring(0,14):hotLive.name);
            ImageView recommend_list_iv_game_1 = (ImageView) liveHot.findViewById(R.id.recommend_list_iv_game_1);
            mImageLoader.displayImage(hotLive.spic, recommend_list_iv_game_1);
            liveHotParent.addView(liveHot, i);
            liveHot.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle = new Bundle();
//                    bundle.putSerializable(LivePlayListActivity.EXTRA_PROGRAM, hotLive);
                    IntentUtils.openActivity(getContext(), LivePlayListActivity.class, bundle);
                }
            });
        }

    }

    private void handleAttentionView(int position, View convertView, final ProgramBean.ProgramEntity gameEntity) {
        TextView myAttention = ViewHolder.get(convertView, R.id.recommend_list_tv_my_attention);
        TextView gameNameText = ViewHolder.get(convertView, R.id.recommend_tv_game_name);
        TextView gameDescText = ViewHolder.get(convertView, R.id.recommend_list_tv_game_desc);
        TextView playCountText = ViewHolder.get(convertView, R.id.recommend_list_tv_play_count);
        TextView attentionCountText = ViewHolder.get(convertView, R.id.recommend_list_tv_attention_count);
        SquareImageView showImage = ViewHolder.get(convertView, R.id.recommend_list_iv_game_attention);
        showImage.setRatio(1.78F);
        // 我的关注
        ViewGroup v = (ViewGroup) myAttention.getParent();
        LinearLayout.LayoutParams p = (LinearLayout.LayoutParams) v.getLayoutParams();
        if (mShowAttentionPosition == -1) {
            myAttention.setVisibility(View.VISIBLE);
            mShowAttentionPosition = position;
        } else if (mShowAttentionPosition == position) {
            myAttention.setVisibility(View.VISIBLE);
            p.topMargin = DensityUtil.dip2px(getContext(), 8);
        } else {
            myAttention.setVisibility(View.GONE);
            p.topMargin = DensityUtil.dip2px(getContext(), 0);
        }
        // FIXME 底部文字
        gameNameText.setText(gameEntity.name);
        if (CommConstants.CAROUSEL_TYPE_LIVE == gameEntity.type) {
            ViewHolder.get(convertView, R.id.image_live).setVisibility(View.VISIBLE);
        } else {
            ViewHolder.get(convertView, R.id.image_live).setVisibility(View.GONE);
        }
        gameDescText.setText(gameEntity.userinfo.nickname );
        playCountText.setText(NumUtils.w(gameEntity.onlines));
        attentionCountText.setText(NumUtils.w(gameEntity.follows));
        mImageLoader.displayImage(gameEntity.spic, showImage);

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (gameEntity.type == CommConstants.CAROUSEL_TYPE_LIVE) {
                    Bundle bundle = new Bundle();
//                    bundle.putSerializable(LivePlayListActivity.EXTRA_PROGRAM, gameEntity);
                    IntentUtils.openActivity(getContext(), LivePlayListActivity.class, bundle);
                } else {
                    VideoPlayListActivity3.openVideoPlayListActivity2(getContext(), gameEntity);
                }
            }
        });
    }

    private void handleGameView(View convertView, final RecommendBean.RecommendGameProgramsEntity gameProgramsEntity) {
        TextView textView = ViewHolder.get(convertView,R.id.name);
        textView.setText(gameProgramsEntity.game.name);
        if(gameProgramsEntity == null){
            return;
        }
        if(gameProgramsEntity.programs != null) {
            LinearLayout gameParent1 = ViewHolder.get(convertView,R.id.game_parent1);
            LinearLayout gameParent2 = ViewHolder.get(convertView,R.id.game_parent2);
            LinearLayout gameParent3 = ViewHolder.get(convertView,R.id.game_parent3);
            LinearLayout gameParent4 = ViewHolder.get(convertView,R.id.game_parent4);
            ImageView image1 = ViewHolder.get(convertView,R.id.video_image1);
            ImageView image2 = ViewHolder.get(convertView,R.id.video_image2);
            ImageView image3 = ViewHolder.get(convertView,R.id.video_image3);
            ImageView image4 = ViewHolder.get(convertView,R.id.video_image4);
            TextView text1 = ViewHolder.get(convertView,R.id.view_name1);
            TextView text2 = ViewHolder.get(convertView,R.id.view_name2);
            TextView text3 = ViewHolder.get(convertView,R.id.view_name3);
            TextView text4 = ViewHolder.get(convertView,R.id.view_name4);
            switch(gameProgramsEntity.programs.size()){
                case 1:
                    gameParent1.setVisibility(View.VISIBLE);
                    gameParent2.setVisibility(View.GONE);
                    gameParent3.setVisibility(View.GONE);
                    gameParent4.setVisibility(View.GONE);
                    mImageLoader.displayImage(gameProgramsEntity.programs.get(0).spic, image1);
                    text1.setText(gameProgramsEntity.programs.get(0).name);
                    image1.setOnClickListener(new MyClickListener(gameProgramsEntity.programs.get(0)));
                    break;
                case 2:
                    gameParent1.setVisibility(View.VISIBLE);
                    gameParent2.setVisibility(View.VISIBLE);
                    gameParent3.setVisibility(View.GONE);
                    gameParent4.setVisibility(View.GONE);
                    mImageLoader.displayImage(gameProgramsEntity.programs.get(0).spic, image1);
                    text1.setText(gameProgramsEntity.programs.get(0).name);
                    image1.setOnClickListener(new MyClickListener(gameProgramsEntity.programs.get(0)));
                    mImageLoader.displayImage(gameProgramsEntity.programs.get(1).spic, image2);
                    text2.setText(gameProgramsEntity.programs.get(1).name);
                    image2.setOnClickListener(new MyClickListener(gameProgramsEntity.programs.get(1)));
                    break;
                case 3:
                    gameParent1.setVisibility(View.VISIBLE);
                    gameParent2.setVisibility(View.VISIBLE);
                    gameParent3.setVisibility(View.VISIBLE);
                    gameParent4.setVisibility(View.GONE);
                    mImageLoader.displayImage(gameProgramsEntity.programs.get(0).spic, image1);
                    text1.setText(gameProgramsEntity.programs.get(0).name);
                    image1.setOnClickListener(new MyClickListener(gameProgramsEntity.programs.get(0)));
                    mImageLoader.displayImage(gameProgramsEntity.programs.get(1).spic, image2);
                    text2.setText(gameProgramsEntity.programs.get(1).name);
                    image2.setOnClickListener(new MyClickListener(gameProgramsEntity.programs.get(1)));
                    mImageLoader.displayImage(gameProgramsEntity.programs.get(2).spic, image3);
                    text3.setText(gameProgramsEntity.programs.get(2).name);
                    image3.setOnClickListener(new MyClickListener(gameProgramsEntity.programs.get(2)));
                    break;
                case 4:
                default:
                    gameParent1.setVisibility(View.VISIBLE);
                    gameParent2.setVisibility(View.VISIBLE);
                    gameParent3.setVisibility(View.VISIBLE);
                    gameParent4.setVisibility(View.VISIBLE);
                    mImageLoader.displayImage(gameProgramsEntity.programs.get(0).spic, image1);
                    text1.setText(gameProgramsEntity.programs.get(0).name);
                    image1.setOnClickListener(new MyClickListener(gameProgramsEntity.programs.get(0)));
                    mImageLoader.displayImage(gameProgramsEntity.programs.get(1).spic, image2);
                    text2.setText(gameProgramsEntity.programs.get(1).name);
                    image2.setOnClickListener(new MyClickListener(gameProgramsEntity.programs.get(1)));
                    mImageLoader.displayImage(gameProgramsEntity.programs.get(2).spic, image3);
                    text3.setText(gameProgramsEntity.programs.get(2).name);
                    image3.setOnClickListener(new MyClickListener(gameProgramsEntity.programs.get(2)));
                    mImageLoader.displayImage(gameProgramsEntity.programs.get(3).spic, image4);
                    text4.setText(gameProgramsEntity.programs.get(3).name);
                    image4.setOnClickListener(new MyClickListener(gameProgramsEntity.programs.get(3)));
                    break;
            }
        }
    }

    private void handleGamePrograms(ImageView image, ImageView liveIcon, final ProgramBean.ProgramEntity gameProgramsEntity) {
        mImageLoader.displayImage(gameProgramsEntity.spic, image);
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
//                    bundle.putSerializable(LivePlayListActivity.EXTRA_PROGRAM, gameProgramsEntity);
                    IntentUtils.openActivity(getContext(), LivePlayListActivity.class, bundle);
                } else {
                    VideoPlayListActivity3.openVideoPlayListActivity2(getContext(), gameProgramsEntity);
                }

            }
        });
    }
    class MyClickListener implements View.OnClickListener{
        ProgramBean.ProgramEntity hotLive;
        public MyClickListener(ProgramBean.ProgramEntity hotLive){
            this.hotLive = hotLive;
        }
        @Override
        public void onClick(View v) {
            if (hotLive.type == CommConstants.CAROUSEL_TYPE_LIVE) {
                Bundle bundle = new Bundle();
//                bundle.putSerializable(LivePlayListActivity.EXTRA_PROGRAM, hotLive);
                IntentUtils.openActivity(getContext(), LivePlayListActivity.class, bundle);
            } else {
                VideoPlayListActivity3.openVideoPlayListActivity2(getContext(), hotLive);
            }
        }
    }
}
