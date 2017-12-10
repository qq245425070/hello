package cn.gietv.mlive.modules.game.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.HashMap;
import java.util.List;

import cn.gietv.mlive.R;
import cn.gietv.mlive.base.AbsArrayAdapter;
import cn.gietv.mlive.constants.CommConstants;
import cn.gietv.mlive.http.DefaultLiveHttpCallBack;
import cn.gietv.mlive.modules.album.activity.AlbumActivity;
import cn.gietv.mlive.modules.compere.activity.AnchorActivity;
import cn.gietv.mlive.modules.follow.model.FollowModel;
import cn.gietv.mlive.modules.game.bean.GameInfoBean;
import cn.gietv.mlive.modules.login.activity.LoginActivity;
import cn.gietv.mlive.modules.statistics.StatisticsMode;
import cn.gietv.mlive.modules.usercenter.bean.UserCenterBean;
import cn.gietv.mlive.utils.DownloadController;
import cn.gietv.mlive.utils.ImageLoaderUtils;
import cn.gietv.mlive.utils.IntentUtils;
import cn.gietv.mlive.utils.NumUtils;
import cn.gietv.mlive.utils.PackageUtils;
import cn.gietv.mlive.utils.ToastUtils;
import cn.gietv.mlive.utils.UserUtils;
import cn.gietv.mlive.utils.ViewHolder;
import cn.gietv.mlive.views.SquareImageView2;
import cn.kalading.android.retrofit.utils.RetrofitUtils;

/**
 * author：steven
 * datetime：15/10/9 14:11
 */
public class GameInfoAdapter extends AbsArrayAdapter<Object> {
    private static final int VIEW_TYPE_HEAD = 0;
    private static final int VIEW_TYPE_PROGRAM = 1;
    private static final int VIEW_TYPE_ANCHOR = 2;
    private ImageLoader mImageLoader;
    private FollowModel model;
    private int type;
    private StatisticsMode mStatisticsMode = RetrofitUtils.create(StatisticsMode.class);
    public GameInfoAdapter(Context context, List<Object> objects) {
        super(context, objects);
        mImageLoader = ImageLoaderUtils.getDefaultImageLoader(getContext());
        model = RetrofitUtils.create(FollowModel.class);
    }

    public void setType (int type){
        this.type = type;
    }
    @Override
    public int getItemViewType(int position) {
        Object o = getItem(position);
        if(position == 0){
            return VIEW_TYPE_HEAD;
        }else {
            if (o instanceof GameInfoBean.GameInfoEntity) {
                return VIEW_TYPE_PROGRAM;
            }else{
                return VIEW_TYPE_ANCHOR;
            }
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
                case VIEW_TYPE_PROGRAM:
                    convertView = LayoutInflater.from(getContext()).inflate(R.layout.game_info_item_layout2, null);
                    break;
                case VIEW_TYPE_ANCHOR:
                    convertView = LayoutInflater.from(getContext()).inflate(R.layout.game_info_item_anchor,null);
                    break;
            }
        }
        switch (getItemViewType(position)) {
            case VIEW_TYPE_HEAD:
                handleHeadView(convertView, (GameInfoBean.GameInfoEntity) getItem(position));
                break;
            case VIEW_TYPE_PROGRAM:
                handleItem(position, convertView, (GameInfoBean.GameInfoEntity) getItem(position));
                break;
            case VIEW_TYPE_ANCHOR:
                handleAnchorItem(position, convertView, (UserCenterBean.UserinfoEntity) getItem(position));
        }
        return convertView;
    }

    private void handleAnchorItem(int position, View convertView, final UserCenterBean.UserinfoEntity bean) {
        if(bean == null){
            return;
        }
        ImageView anchorImage = ViewHolder.get(convertView,R.id.anchor_image);
        TextView anchorName = ViewHolder.get(convertView,R.id.anchor_name);
        final TextView anchorFollow = ViewHolder.get(convertView,R.id.anchor_follow);
        final ImageView followImage = ViewHolder.get(convertView,R.id.follow_image);
        anchorName.setText(bean.nickname);
        anchorFollow.setText(bean.desc);
        mImageLoader.displayImage(bean.avatar, anchorImage);
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                CompereActivity.openCompereActivity(getContext(), bean._id);
                AnchorActivity.openAnchorActivity(getContext(),bean._id,bean.nickname);
            }
        });
        if(bean.isfollow == CommConstants.TRUE){
            followImage.setImageResource(R.drawable.over_concern);
        }else {
            followImage.setImageResource(R.drawable.no_concern);
        }
        followImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (UserUtils.isNotLogin()) {
                    IntentUtils.openActivity(getContext(), LoginActivity.class);
                    return;
                }
                if(bean.isfollow == CommConstants.TRUE){
                    model.follow(bean._id, CommConstants.FOLLOW_ACTION_OFF, bean.type, new DefaultLiveHttpCallBack<String>() {
                        @Override
                        public void success(String s) {
                            anchorFollow.setText(String.valueOf(--bean.follows));
                            ToastUtils.showToast(getContext(), "取消关注成功");
                            followImage.setImageResource(R.drawable.no_concern);
                            bean.isfollow = CommConstants.FALSE;
                        }

                        @Override
                        public void failure(String message) {
                            ToastUtils.showToast(getContext(), message);
                        }
                    });
                }else {
                    model.follow(bean._id, CommConstants.FOLLOW_ACTION_ON, bean.type, new DefaultLiveHttpCallBack<String>() {
                        @Override
                        public void success(String s) {
                            anchorFollow.setText(String.valueOf(++bean.follows));
                            ToastUtils.showToast(getContext(), "关注成功");
                            followImage.setImageResource(R.drawable.over_concern);
                            bean.isfollow = CommConstants.TRUE;
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

    private void handleHeadView(View convertView, final GameInfoBean.GameInfoEntity bean) {
        if (bean == null) {
            return;
        }
        LinearLayout parent = ViewHolder.get(convertView,R.id.parent);
        if(type != 0){
            parent.setVisibility(View.GONE);
        }
        TextView gameSizeType = ViewHolder.get(convertView,R.id.game_size_type);
        SquareImageView2 image = ViewHolder.get(convertView, R.id.game_info_iv_image);
        TextView albumText = ViewHolder.get(convertView,R.id.album_text);
        TextView anchorText = ViewHolder.get(convertView,R.id.anchor_text);
        final View line1 = ViewHolder.get(convertView,R.id.line1);
        final View line2 = ViewHolder.get(convertView,R.id.line2);
        TextView name = ViewHolder.get(convertView, R.id.game_info_tv_name);
        TextView desc = ViewHolder.get(convertView, R.id.game_info_tv_desc);
        TextView videoCount = ViewHolder.get(convertView, R.id.game_info_tv_video);
        TextView attentionCount = ViewHolder.get(convertView, R.id.game_info_tv_attention_count);
        TextView installButton = ViewHolder.get(convertView, R.id.game_info_btn_install);
        final TextView attentionButton = ViewHolder.get(convertView, R.id.game_info_btn_attention);
        mImageLoader.displayImage(bean.spic, image);
        name.setText(bean.name);
        desc.setText(bean.desc);
        if(type == 1){
            gameSizeType.setVisibility(View.VISIBLE);
            gameSizeType.setText(bean.gametypename+" | "+bean.size);
        }

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
//                UserListActivity.openUserListActivity(bean.type,(Activity) getContext(), bean._id, UserListActivity.USER_MODEL_GAME_ATTENTION, CompereListAdapter.CONCERN,bean.name,bean.follows);
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
                if (bean.isfollow == CommConstants.TRUE) {
                    model.follow(bean._id, CommConstants.FOLLOW_ACTION_OFF, bean.type, new DefaultLiveHttpCallBack<String>() {
                        @Override
                        public void success(String s) {
                            ToastUtils.showToast(getContext(), "取消关注成功");
                            //dialog.dismiss();
                            attentionButton.setText("关注");
//                            for (int i = 0; i < getCount(); i++) {
//                                Object o = getItem(i);
//                                if (o instanceof ProgramBean.ProgramEntity) {
//                                    ProgramBean.ProgramEntity e = (ProgramBean.ProgramEntity) o;
//                                    e.game.isfollow = CommConstants.FALSE;
//                                }
//                            }
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
                    model.follow(bean._id, CommConstants.FOLLOW_ACTION_ON, bean.type, new DefaultLiveHttpCallBack<String>() {
                        @Override
                        public void success(String s) {
                            ToastUtils.showToast(getContext(), "关注成功");
                            attentionButton.setText("已关注");
//                            for (int i = 0; i < getCount(); i++) {
//                                Object o = getItem(i);
//                                if (o instanceof ProgramBean.ProgramEntity) {
//                                    ProgramBean.ProgramEntity e = (ProgramBean.ProgramEntity) o;
//                                    e.game.isfollow = CommConstants.TRUE;
//                                }
//                            }
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
        albumText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                line1.setBackgroundColor(Color.parseColor("#43e88d"));
                line2.setBackgroundColor(Color.WHITE);
                if(albumOnClickListener != null){
                    albumOnClickListener.getAlbumList(0);
                }
            }
        });
        anchorText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                line2.setBackgroundColor(Color.parseColor("#43e88d"));
                line1.setBackgroundColor(Color.WHITE);
                if(anchorOnclickListener != null){
                    anchorOnclickListener.getAnchorList(1);
                }
            }
        });
    }

    private void handleItem(int position, View convertView, final GameInfoBean.GameInfoEntity bean) {
        if (bean == null) {
            return;
        }
        SquareImageView2 image = ViewHolder.get(convertView, R.id.game_info_iv_image);
        TextView name = ViewHolder.get(convertView, R.id.album_name);
        TextView anchor = ViewHolder.get(convertView, R.id.album_anchor);
        TextView count = ViewHolder.get(convertView,R.id.play_count);
        count.setText(bean.programnums+"个视频");
        mImageLoader.displayImage(bean.spic, image);
        name.setText(bean.name);
        // FIXME 点击事件
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlbumActivity.openAlbumActivity(bean.name,bean._id,getContext());
            }
        });
    }
    private IAlbumOnClickListener albumOnClickListener;
    private IAnchorOnclickListener anchorOnclickListener;
    public void setAlbumOnClickListener(IAlbumOnClickListener listener){
        this.albumOnClickListener = listener;
    }
    public void setAnchorOnclickListener(IAnchorOnclickListener listener){
        this.anchorOnclickListener = listener;
    }
   public interface IAlbumOnClickListener{
        void getAlbumList(int type);
    }
    public interface  IAnchorOnclickListener{
        void getAnchorList(int type);
    }
    private HashMap<Integer, String> mPositionMap = new HashMap<>();

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
//        mPositionMap.clear();
//        int dType = 0;
//        for (int i = 0; i < getCount(); i++) {
//            Object o = getItem(i);
//            if (o instanceof ProgramBean.ProgramEntity) {
//                ProgramBean.ProgramEntity p = (ProgramBean.ProgramEntity) o;
//                if (p.type != dType) {
//                    if (p.type == 1) {
//                        mPositionMap.put(i, "当前直播");
//                    } else if (p.type == 2) {
//                        mPositionMap.put(i, "游戏视频");
//                    }
//                    dType = p.type;
//                }
//            }
//
//        }
    }
}
