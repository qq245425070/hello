package cn.gietv.mlive.modules.vrgame.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

import cn.gietv.mlive.R;
import cn.gietv.mlive.http.DefaultLiveHttpCallBack;
import cn.gietv.mlive.modules.statistics.StatisticsMode;
import cn.gietv.mlive.modules.vrgame.activity.VRGameActivity;
import cn.gietv.mlive.modules.game.bean.GameInfoBean;
import cn.gietv.mlive.utils.DownloadController;
import cn.gietv.mlive.utils.ImageLoaderUtils;
import cn.gietv.mlive.utils.PackageUtils;
import cn.kalading.android.retrofit.utils.RetrofitUtils;

/**
 * Created by houde on 2016/5/18.
 */
public class VRGameAdapter extends RecyclerView.Adapter<VRGameAdapter.ViewHolder> {
    private Context context;
    private List<GameInfoBean.GameInfoEntity> gameList;
    private ImageLoader mImageLoader;
    private StatisticsMode mStatisticsMode;
    public VRGameAdapter(Context context,List< GameInfoBean.GameInfoEntity> games){
        this.context = context;
        this.gameList = games;
        this.mImageLoader = ImageLoaderUtils.getDefaultImageLoader(context);
        mStatisticsMode = RetrofitUtils.create(StatisticsMode.class);
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, final int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.game_list_adapter2,null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        final GameInfoBean.GameInfoEntity entity = gameList.get(i);
        if(entity == null){
            return;
        }
        viewHolder.gameName.setText(entity.name);
        mImageLoader.displayImage(entity.spic, viewHolder.gameImage);
        viewHolder.gameType.setText(entity.gametypename);
        viewHolder.ratingBar.setRating(entity.score);
        viewHolder.downloadCount.setText(entity.score_cnt+"");
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VRGameActivity.openVRGameActivity(context,entity);
            }
        });
        if(entity.package_name.equals("no")){
            viewHolder.install.setVisibility(View.GONE);
        }else{
            if (PackageUtils.hasInstalled(context, entity.package_name)) {
                viewHolder.install.setText("打开");
            } else {
                viewHolder.install.setText("安装");
            }
        }
        viewHolder.install.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (PackageUtils.hasInstalled(context, entity.package_name)) {
                    PackageUtils.openApplication(context, entity.package_name);
                    //向服务器发送游戏启动
                    mStatisticsMode.gameAction(entity._id, 2, new DefaultLiveHttpCallBack<String>() {
                        @Override
                        public void success(String s) {
                        }

                        @Override
                        public void failure(String message) {
                        }
                    });
                } else {
                    DownloadController controller = new DownloadController(context);
                    controller.startDownload(entity.name + ".apk", entity.url_android);
                    entity.download++;
                    mStatisticsMode.gameAction(entity._id, 1, new DefaultLiveHttpCallBack<String>() {
                        @Override
                        public void success(String s) {
                        }

                        @Override
                        public void failure(String message) {
                        }
                    });
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return gameList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        private ImageView gameImage;
        private TextView gameName;
        private TextView gameType;
        private RatingBar ratingBar;
        private TextView install;
        private TextView downloadCount;
        public ViewHolder(View itemView) {
            super(itemView);
            gameImage = (ImageView) itemView.findViewById(R.id.game_iv_image);
            gameName = (TextView) itemView.findViewById(R.id.game_tv_name);
            gameType = (TextView) itemView.findViewById(R.id.game_type);
            ratingBar = (RatingBar) itemView.findViewById(R.id.rating_bar);
            install = (TextView) itemView.findViewById(R.id.install_text);
            downloadCount = (TextView) itemView.findViewById(R.id.download_count);
        }
    }
}
