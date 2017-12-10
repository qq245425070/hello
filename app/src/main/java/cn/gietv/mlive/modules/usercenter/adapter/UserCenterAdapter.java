package cn.gietv.mlive.modules.usercenter.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

import cn.gietv.mlive.R;
import cn.gietv.mlive.base.AbsArrayAdapter;
import cn.gietv.mlive.base.AbsBaseActivity;
import cn.gietv.mlive.constants.CommConstants;
import cn.gietv.mlive.http.DefaultLiveHttpCallBack;
import cn.gietv.mlive.modules.download.activity.DownloadOverAcitvity;
import cn.gietv.mlive.modules.home.model.NoviceTaskModel;
import cn.gietv.mlive.modules.login.activity.LoginActivity;
import cn.gietv.mlive.modules.photo.activity.PhotoUpdateActivity;
import cn.gietv.mlive.modules.program.bean.ProgramBean;
import cn.gietv.mlive.modules.usercenter.activity.HistoryVideoActivity;
import cn.gietv.mlive.modules.usercenter.activity.LocalVideoActivity;
import cn.gietv.mlive.modules.usercenter.activity.MyConcernActivity;
import cn.gietv.mlive.modules.usercenter.activity.RechargeRecordActivity;
import cn.gietv.mlive.modules.usercenter.activity.ShouCangActivity;
import cn.gietv.mlive.modules.usercenter.activity.TaskActivity;
import cn.gietv.mlive.modules.usercenter.activity.UserSubscribeActivity;
import cn.gietv.mlive.modules.usercenter.activity.UserUpdateActivity;
import cn.gietv.mlive.modules.usercenter.bean.TasksBean;
import cn.gietv.mlive.modules.usercenter.bean.UserCenterBean;
import cn.gietv.mlive.modules.usercenter.fragment.MyConcernFragment;
import cn.gietv.mlive.modules.video.activity.LivePlayListActivity;
import cn.gietv.mlive.modules.video.activity.VideoPlayListActivity3;
import cn.gietv.mlive.utils.ConfigUtils;
import cn.gietv.mlive.utils.ImageLoaderUtils;
import cn.gietv.mlive.utils.IntentUtils;
import cn.gietv.mlive.utils.NoviceTaskUtils;
import cn.gietv.mlive.utils.NumUtils;
import cn.gietv.mlive.utils.StringUtils;
import cn.gietv.mlive.utils.ToastUtils;
import cn.gietv.mlive.utils.ViewHolder;
import cn.gietv.mlive.views.SquareImageView;
import cn.kalading.android.retrofit.utils.RetrofitUtils;

/**
 * author：steven
 * datetime：15/10/8 13:53
 */
public class UserCenterAdapter extends AbsArrayAdapter<Object> {
    private static final int VIEW_TYPE_USERINFO = 0;
    private static final int VIEW_TYPE_ATTENTION = 1;
    private ImageLoader mImageLoader;
    private List<ProgramBean.ProgramEntity> mData;
    private int mDefaultType = 0;
    private String userId;
    private ProgramBean.ProgramEntity programEntity;
    private NoviceTaskModel noviceTask;
    private List<Object> objects;
    public UserCenterAdapter(Context context, List<Object> objects) {
        super(context, objects);
        mImageLoader = ImageLoaderUtils.getDefaultImageLoader(getContext());
        this.objects = objects;
        noviceTask = RetrofitUtils.create(NoviceTaskModel.class);
    }
    public void setData(List<ProgramBean.ProgramEntity> data){
        this.mData = data;
    }

    @Override
    public int getItemViewType(int position) {
        Object o = getItem(position);
        if (o instanceof UserCenterBean.UserinfoEntity) {
            userId = ((UserCenterBean.UserinfoEntity) o)._id;
            return VIEW_TYPE_USERINFO;
        } else if (o instanceof ProgramBean.ProgramEntity) {
            programEntity = (ProgramBean.ProgramEntity) getItem(position);
            return VIEW_TYPE_ATTENTION;
        }
        programEntity = (ProgramBean.ProgramEntity) getItem(position);
        return VIEW_TYPE_ATTENTION;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    private boolean flag = true;
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            switch (getItemViewType(position)) {
                case VIEW_TYPE_ATTENTION:
                    convertView = LayoutInflater.from(getContext()).inflate(R.layout.user_info_center_item_layout, null);
                    break;
                case VIEW_TYPE_USERINFO:
                    convertView = LayoutInflater.from(getContext()).inflate(R.layout.user_info_center_head_layout, null);
                    break;
            }
        }
        switch (getItemViewType(position)) {
            case VIEW_TYPE_ATTENTION:
                handleAttention(position, convertView, (ProgramBean.ProgramEntity) getItem(position));
                break;
            case VIEW_TYPE_USERINFO:
                handleUserInfo(convertView, (UserCenterBean.UserinfoEntity) getItem(position));
                break;
        }
        return convertView;
    }

    public void handleUserInfo(View convertView, final UserCenterBean.UserinfoEntity user) {
        if(user == null){
            return;
        }
        LinearLayout headParent = ViewHolder.get(convertView,R.id.head_parent);
        LinearLayout other_parent = ViewHolder.get(convertView,R.id.other_parent);
        LinearLayout commenParent = ViewHolder.get(convertView,R.id.commen_parent);
        LinearLayout fansParent = ViewHolder.get(convertView,R.id.fans_parent);
        LinearLayout photoParent = ViewHolder.get(convertView,R.id.photo_parent);
        LinearLayout historyParent = ViewHolder.get(convertView,R.id.history_parent);
        LinearLayout subscribeParent = ViewHolder.get(convertView,R.id.subscribe_parent);
        LinearLayout signParent = ViewHolder.get(convertView,R.id.sign_parent);
//        LinearLayout edit_parent = ViewHolder.get(convertView,R.id.edit_parent);
        ImageView image = ViewHolder.get(convertView, R.id.user_center_iv_image);
        TextView name = ViewHolder.get(convertView, R.id.user_center_tv_name);
        TextView desc = ViewHolder.get(convertView, R.id.user_center_tv_desc);
        TextView task = ViewHolder.get(convertView,R.id.user_center_btn_task);
        TextView empty = ViewHolder.get(convertView,R.id.empty_text);
        TextView editText = ViewHolder.get(convertView,R.id.edit_user);

        task.setVisibility(View.GONE);
        LinearLayout liveParent = ViewHolder.get(convertView,R.id.live_parent);
        LinearLayout shoucangParent = ViewHolder.get(convertView,R.id.shoucang_parent);
        LinearLayout downloadParent = ViewHolder.get(convertView,R.id.download_parent);
        LinearLayout rechargeParent = ViewHolder.get(convertView,R.id.recharge_parent);
        LinearLayout loaclParent = ViewHolder.get(convertView,R.id.local_parent);
        task.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentUtils.openActivity(getContext(), TaskActivity.class);
            }
        });
        RelativeLayout userParent = ViewHolder.get(convertView, R.id.user_center_parent);
        TextView updateButton = ViewHolder.get(convertView, R.id.user_center_btn_update);
        final ImageView downImage = ViewHolder.get(convertView,R.id.down_user_setting);
        if(flag) {
            downImage.setImageResource(R.mipmap.down_user_setting);
        }
        else{
            downImage.setImageResource(R.mipmap.icon_right_button);
        }
        if (StringUtils.isEmpty(user._id)) {
            updateButton.setText("登录");
            name.setVisibility(View.INVISIBLE);
            desc.setVisibility(View.GONE);
            empty.setVisibility(View.VISIBLE);
            desc.setText(" ");
            image.setImageResource(R.mipmap.icon_person);
            other_parent.setVisibility(View.INVISIBLE);

            updateButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    IntentUtils.openActivity(getContext(), LoginActivity.class);
                }
            });
            headParent.setVisibility(View.VISIBLE);
            userParent.setVisibility(View.GONE);
            shoucangParent.setVisibility(View.GONE);
            downloadParent.setVisibility(View.GONE);
            loaclParent.setVisibility(View.GONE);
            historyParent.setVisibility(View.GONE);
            subscribeParent.setVisibility(View.GONE);
            editText.setVisibility(View.GONE);
            signParent.setVisibility(View.GONE);
            convertView.findViewById(R.id.line6).setVisibility(View.GONE);
            convertView.findViewById(R.id.line1).setVisibility(View.GONE);
            convertView.findViewById(R.id.line2).setVisibility(View.GONE);
            convertView.findViewById(R.id.line3).setVisibility(View.GONE);
            convertView.findViewById(R.id.line8).setVisibility(View.GONE);
            convertView.findViewById(R.id.line9).setVisibility(View.GONE);
        } else {
            empty.setVisibility(View.GONE);
            other_parent.setVisibility(View.VISIBLE);
            name.setVisibility(View.VISIBLE);
            headParent.setVisibility(View.GONE);
            userParent.setVisibility(View.VISIBLE);
            historyParent.setVisibility(View.VISIBLE);
            subscribeParent.setVisibility(View.VISIBLE);
            editText.setVisibility(View.VISIBLE);
            signParent.setVisibility(View.GONE);
            final TextView getJinjiao = ViewHolder.get(convertView,R.id.get_jinjiao);
            ImageView user_icon = ViewHolder.get(convertView, R.id.user_icon);
            TextView nickname = ViewHolder.get(convertView,R.id.nickname_user);
            final TextView jinjiaoNum = ViewHolder.get(convertView,R.id.jinjiao_number);
            jinjiaoNum.setText(user.mycoin+"");
            TextView fanText = ViewHolder.get(convertView,R.id.fans_text);
            fanText.setText( String.valueOf(user.follows));
            TextView descText = ViewHolder.get(convertView,R.id.user_desc);
            if(user.desc.length() > 21){
                descText.setGravity(Gravity.LEFT);
            }else{
                descText.setGravity(Gravity.CENTER);
            }
            descText.setText(user.desc);
            TextView followText = ViewHolder.get(convertView,R.id.commen_text);
            followText.setText( String.valueOf(user.myfollow));
            TextView photoText = ViewHolder.get(convertView,R.id.photo_text);
            photoText.setText( String.valueOf(user.photo));
            ImageView editImage = ViewHolder.get(convertView,R.id.edit_user_info);
            if (StringUtils.isEmpty(user.avatar)) {
                user_icon.setImageResource(R.mipmap.icon_person);
            } else {
                mImageLoader.displayImage(user.avatar, user_icon);
            }
            user_icon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(UserUpdateActivity.EXTRA_USER_INFO, user);
                    IntentUtils.openActivity(getContext(), UserUpdateActivity.class, bundle);
                }
            });
            noviceTask.queryTask(new DefaultLiveHttpCallBack<TasksBean>() {
                @Override
                public void success(TasksBean tasksBean) {
                    TasksBean.TaskBean bean = null;
                    getJinjiao.setText("领取");
                    getJinjiao.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            NoviceTaskUtils.doDayTask(ConfigUtils.TASK_DAY_LOGIN, (AbsBaseActivity) getContext());
                            getJinjiao.setText("已领取");
                            getJinjiao.setClickable(false);
                            noviceTask.sendDayTask(ConfigUtils.TASK_DAY_LOGIN, new DefaultLiveHttpCallBack<String>() {
                                @Override
                                public void success(String s) {
                                    jinjiaoNum.setText(user.mycoin + 10 + "");
                                }

                                @Override
                                public void failure(String message) {
                                    ToastUtils.showToast(getContext(), message);
                                }
                            });
                        }
                    });
                    for (int i = 0; i < tasksBean.taskqualifications.size(); i++) {
                        bean = tasksBean.taskqualifications.get(i);
                        switch (bean.taskcode) {
                            case ConfigUtils.TASK_DAY_LOGIN:
                                if (bean.status > 0) {//表示有资格  >0 可领取   -1 已领取  0没有资格领取
                                } else {
                                    getJinjiao.setText("已领取");
                                    getJinjiao.setClickable(false);
                                }
                                break;
                        }
                    }
                }

                @Override
                public void failure(String message) {

                }
            });
            name.setVisibility(View.VISIBLE);
            desc.setVisibility(View.VISIBLE);
            image.setVisibility(View.VISIBLE);
            shoucangParent.setVisibility(View.VISIBLE);
            downloadParent.setVisibility(View.VISIBLE);
            loaclParent.setVisibility(View.VISIBLE);
            convertView.findViewById(R.id.line6).setVisibility(View.VISIBLE);
            convertView.findViewById(R.id.line1).setVisibility(View.VISIBLE);
            convertView.findViewById(R.id.line3).setVisibility(View.VISIBLE);
            convertView.findViewById(R.id.line8).setVisibility(View.VISIBLE);
            convertView.findViewById(R.id.line9).setVisibility(View.VISIBLE);
            downloadParent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    IntentUtils.openActivity(getContext(), DownloadOverAcitvity.class);
                }
            });
            rechargeParent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    IntentUtils.openActivity(getContext(), RechargeRecordActivity.class);
                }
            });
            shoucangParent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle = new Bundle();
                    if (mData != null)
                        bundle.putSerializable(ShouCangActivity.EXE_DATA, (Serializable) mData);
                    IntentUtils.openActivity(getContext(), ShouCangActivity.class, bundle);
                }
            });
            liveParent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (flag) {
                        downImage.setImageResource(R.mipmap.icon_right_button);
                        flag = false;
                        if (programEntity != null)
                            objects.remove(programEntity);
                        notifyDataSetChanged();
                    } else {
                        downImage.setImageResource(R.mipmap.down_user_setting);
                        flag = true;
                        if (programEntity != null)
                            objects.add(programEntity);
                        notifyDataSetChanged();
                    }
                }
            });
            nickname.setText(user.nickname);
            historyParent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    IntentUtils.openActivity(getContext(), HistoryVideoActivity.class);
                }
            });
            fansParent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    UserListActivity.openUserListActivity(user.type, (Activity) getContext(), user._id, UserListActivity.USER_MODEL_ATTENTION, CompereListAdapter.CONCERN, user.nickname, user.follows);
                    MyConcernActivity.openMyConcernActivity(getContext(),user._id,user.nickname,MyConcernFragment.FLAG_FANS);
                }
            });

            commenParent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    UserSubscribeActivity.openUserSubscribeActivity(getContext(),user._id,user.nickname);
                    MyConcernActivity.openMyConcernActivity(getContext(),user._id,user.nickname, MyConcernFragment.FLAG_FOLLOW);
                }
            });
            editText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(UserUpdateActivity.EXTRA_USER_INFO, user);
                    IntentUtils.openActivity(getContext(), UserUpdateActivity.class, bundle);
                }
            });
            loaclParent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.setClass(getContext(), LocalVideoActivity.class);
                    getContext().startActivity(intent);
                }
            });
            photoParent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    IntentUtils.openActivity(getContext(), PhotoUpdateActivity.class);
                }
            });
            subscribeParent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    UserSubscribeActivity.openUserSubscribeActivity(getContext(),user._id,user.nickname);
                }
            });
        }
    }

    public void handleAttention(int position, View convertView, final ProgramBean.ProgramEntity program) {
        SquareImageView image = ViewHolder.get(convertView, R.id.user_center_iv_game_image);
        TextView name = ViewHolder.get(convertView, R.id.user_center_tv_game_name);
        TextView desc = ViewHolder.get(convertView, R.id.user_center_list_tv_game_desc);
        TextView playCount = ViewHolder.get(convertView, R.id.user_center_list_tv_play_count);
        TextView attentionCount = ViewHolder.get(convertView, R.id.user_center_list_tv_attention_count);
        ImageView smal_iv = ViewHolder.get(convertView, R.id.smal_iv);
        attentionCount.setVisibility(View.INVISIBLE);
        image.setRatio(1.78f);
        mImageLoader.displayImage(program.spic, image);
        name.setText(program.name);
        if (CommConstants.CAROUSEL_TYPE_LIVE == program.type) {
            ViewHolder.get(convertView, R.id.image_live).setVisibility(View.VISIBLE);
            smal_iv.setBackgroundResource(R.drawable.live_count);
        } else {
            ViewHolder.get(convertView, R.id.image_live).setVisibility(View.GONE);
            smal_iv.setBackgroundResource(R.drawable.sanjiaoxing);
        }
        desc.setText(program.userinfo.nickname );
        playCount.setText(NumUtils.w(program.onlines));
        attentionCount.setText(NumUtils.w(program.follows));

        TextView typeText = ViewHolder.get(convertView, R.id.user_center_tv_type);
        if (mPositionMap.get(position) != null) {
            typeText.setVisibility(View.VISIBLE);
            typeText.setText(mPositionMap.get(position));
            typeText.setVisibility(View.GONE);
        } else {
            typeText.setVisibility(View.GONE);
        }
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (program.type == CommConstants.CAROUSEL_TYPE_LIVE) {
                    Bundle bundle = new Bundle();
//                    bundle.putSerializable(LivePlayListActivity.EXTRA_PROGRAM, program);
                    IntentUtils.openActivity(getContext(), LivePlayListActivity.class, bundle);
                } else {
                    VideoPlayListActivity3.openVideoPlayListActivity2(getContext(), program);
                }
            }
        });
        //objects.remove(getItem(position));
    }

    private HashMap<Integer, String> mPositionMap = new HashMap<>();

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
        mPositionMap.clear();
        int dType = 0;
        for (int i = 0; i < getCount(); i++) {
            Object o = getItem(i);
            if (o instanceof ProgramBean.ProgramEntity) {
                ProgramBean.ProgramEntity p = (ProgramBean.ProgramEntity) o;
                if (p.type != dType) {
                    if (p.type == 1) {
                        mPositionMap.put(i, "我的直播");
                    } else if (p.type == 2) {
                        mPositionMap.put(i, "我的视频");
                    }
                    dType = p.type;
                }
            }

        }
    }
}
