package cn.gietv.mlive.modules.game.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

import cn.gietv.mlive.R;
import cn.gietv.mlive.base.AbsArrayAdapter;
import cn.gietv.mlive.constants.CommConstants;
import cn.gietv.mlive.http.DefaultLiveHttpCallBack;
import cn.gietv.mlive.modules.follow.model.FollowModel;
import cn.gietv.mlive.modules.game.bean.GameInfoBean;
import cn.gietv.mlive.modules.login.activity.LoginActivity;
import cn.gietv.mlive.modules.program.bean.ProgramBean;
import cn.gietv.mlive.modules.statistics.StatisticsMode;
import cn.gietv.mlive.utils.DownloadController;
import cn.gietv.mlive.utils.ImageLoaderUtils;
import cn.gietv.mlive.utils.IntentUtils;
import cn.gietv.mlive.utils.NumUtils;
import cn.gietv.mlive.utils.PackageUtils;
import cn.gietv.mlive.utils.ToastUtils;
import cn.gietv.mlive.utils.UserUtils;
import cn.gietv.mlive.utils.ViewHolder;
import cn.gietv.mlive.views.SquareImageView;
import cn.kalading.android.retrofit.utils.RetrofitUtils;

/**
 * Created by houde on 2016/5/17.
 */
public class GameInfoLiveAdapter extends AbsArrayAdapter<Object> {
    private static final int VIEW_TYPE_HEAD = 0;
    private static final int VIEW_TYPE_ALBUM = 1;
    private static final int VIEW_TYPE_ANCHOR = 2;
    private ImageLoader mImageLoader;
    private StatisticsMode mStatisticsMode = RetrofitUtils.create(StatisticsMode.class);
    public GameInfoLiveAdapter(Context context, List<Object> objects) {
        super(context, objects);
        mImageLoader = ImageLoaderUtils.getDefaultImageLoader(getContext());
    }
    @Override
    public int getItemViewType(int position) {
        Object o = getItem(position);
        if (o instanceof GameInfoBean.GameInfoEntity) {
            return VIEW_TYPE_HEAD;
        } else if (o instanceof ProgramBean.ProgramEntity) {
            return VIEW_TYPE_ALBUM;
        }else {
            return VIEW_TYPE_ANCHOR;
        }
    }
    @Override
    public int getViewTypeCount() {
        return 3;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            switch (getItemViewType(position)) {
                case VIEW_TYPE_HEAD:
                    convertView = LayoutInflater.from(getContext()).inflate(R.layout.game_info_head_layout, null);
                    break;
            }
        }
        switch (getItemViewType(position)) {
            case VIEW_TYPE_HEAD:
                handleHeadView(convertView, (GameInfoBean.GameInfoEntity) getItem(position));
                break;
        }
        return convertView;
    }
    private void handleHeadView(View convertView, final GameInfoBean.GameInfoEntity bean) {
        if (bean == null) {
            return;
        }
        SquareImageView image = ViewHolder.get(convertView, R.id.game_info_iv_image);
        TextView name = ViewHolder.get(convertView, R.id.game_info_tv_name);
        TextView desc = ViewHolder.get(convertView, R.id.game_info_tv_desc);
        TextView videoCount = ViewHolder.get(convertView, R.id.game_info_tv_video);
        TextView attentionCount = ViewHolder.get(convertView, R.id.game_info_tv_attention_count);
        TextView installButton = ViewHolder.get(convertView, R.id.game_info_btn_install);
        final TextView attentionButton = ViewHolder.get(convertView, R.id.game_info_btn_attention);
        image.setRatio(1.78f);
        mImageLoader.displayImage(bean.spic.replace("_logo",""), image);
        name.setText(bean.name);
        desc.setText(bean.desc);

        TextView videoCountC = ViewHolder.get(convertView, R.id.game_info_tv_video_count);
        videoCount.setText("视频");
        videoCountC.setText(NumUtils.w(bean.programnums));

        TextView attentionCountC = ViewHolder.get(convertView, R.id.game_info_tv_attention_count_count);
        LinearLayout attentionCountL = ViewHolder.get(convertView, R.id.game_info_ll_attention_count);
        attentionCount.setText("粉丝");
        attentionCountC.setText(NumUtils.w(bean.follows));
        attentionCountL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                UserListActivity.openUserListActivity(bean.type,(Activity) getContext(), bean._id, UserListActivity.USER_MODEL_GAME_ATTENTION, CompereListAdapter.CONCERN, bean.name, bean.follows);
            }
        });
        if(bean.package_name.equals("no")){
            installButton.setVisibility(View.GONE);
        }else{
            if (PackageUtils.hasInstalled(getContext(), bean.package_name)) {
                installButton.setText("打开");
            } else {
                installButton.setText("安装");
            }
        }

        if (bean.isfollow == CommConstants.TRUE) {
            attentionButton.setText("已关注");
            attentionButton.setBackgroundResource(R.drawable.commen_button_theme_color2);
            attentionButton.setTextColor(getContext().getResources().getColor(R.color.white));
        } else {
            attentionButton.setText("关注");
            attentionButton.setBackgroundResource(R.drawable.commen_button_theme_color);
            attentionButton.setTextColor(getContext().getResources().getColor(R.color.text_color_101010));
        }
        installButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (PackageUtils.hasInstalled(getContext(), bean.package_name)) {
                    PackageUtils.openApplication(getContext(), bean.package_name);
                    //向服务器发送游戏启动
                    mStatisticsMode.gameAction(bean._id, 2, new DefaultLiveHttpCallBack<String>() {
                        @Override
                        public void success(String s) {

                        }

                        @Override
                        public void failure(String message) {

                        }
                    });
                } else {
                    DownloadController controller = new DownloadController(getContext());
                    controller.startDownload(bean.name + ".apk", bean.url_android);
                    bean.download++;
                    notifyDataSetChanged();
                    // 向服务器发送游戏下载
                    mStatisticsMode.gameAction(bean._id, 1, new DefaultLiveHttpCallBack<String>() {
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
        attentionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (UserUtils.isNotLogin()) {
                    IntentUtils.openActivity(getContext(), LoginActivity.class);
                    return;
                }
                // FIXME
                FollowModel model = RetrofitUtils.create(FollowModel.class);
                if (bean.isfollow == CommConstants.TRUE) {
                    model.followByContent(bean._id, bean.type, CommConstants.FOLLOW_ACTION_OFF, new DefaultLiveHttpCallBack<String>() {
                        @Override
                        public void success(String s) {
                            ToastUtils.showToast(getContext(), "取消关注成功");
                            attentionButton.setText("关注");
                            bean.follows--;
                            bean.isfollow = CommConstants.FALSE;
                            notifyDataSetChanged();
                        }
                        @Override
                        public void failure(String message) {
                            ToastUtils.showToast(getContext(), message);
                        }
                    });
                } else {
                    model.followByContent(bean._id, bean.type, CommConstants.FOLLOW_ACTION_ON, new DefaultLiveHttpCallBack<String>() {
                        @Override
                        public void success(String s) {
                            ToastUtils.showToast(getContext(), "关注成功");
                            attentionButton.setText("已关注");
                            bean.follows++;
                            bean.isfollow = CommConstants.TRUE;
                            notifyDataSetChanged();
                        }

                        @Override
                        public void failure(String message) {
                            ToastUtils.showToast(getContext(), message);
                        }
                    });
                }
            }
        });
    }
}
