package cn.gietv.mlive.views;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import cn.gietv.mlive.MainApplication;
import cn.gietv.mlive.R;
import cn.gietv.mlive.constants.CacheConstants;
import cn.gietv.mlive.constants.CommConstants;
import cn.gietv.mlive.http.DefaultLiveHttpCallBack;
import cn.gietv.mlive.modules.login.activity.LoginActivity;
import cn.gietv.mlive.modules.usercenter.activity.PaymentActivity;
import cn.gietv.mlive.modules.usercenter.activity.RechargeActivity;
import cn.gietv.mlive.modules.usercenter.activity.TaskActivity;
import cn.gietv.mlive.modules.usercenter.bean.UserCenterBean;
import cn.gietv.mlive.modules.usercenter.model.UserCenterModel;
import cn.gietv.mlive.modules.video.activity.LivePlayActivity;
import cn.gietv.mlive.modules.video.bean.PropBeanList;
import cn.gietv.mlive.utils.CacheUtils;
import cn.gietv.mlive.utils.GiveGiftUtil;
import cn.gietv.mlive.utils.ImageLoaderUtils;
import cn.gietv.mlive.utils.IntentUtils;
import cn.gietv.mlive.utils.ToastUtils;
import cn.gietv.mlive.utils.UserUtils;
import cn.kalading.android.retrofit.utils.RetrofitUtils;

/**
 * Created by houde on 2016/2/2.
 */
public class GiftFullPopWindow extends PopupWindow implements View.OnClickListener{
    private ImageView mGiftImage01,mGiftImage02,mGiftImage04,mGiftImage03,mGiftImage05,mGiftImage06;
    private TextView mGiftName01,mGiftName02,mGiftName03,mGiftName04,mGiftName05,mGiftName06;
    private TextView mGiftNumber01,mGiftNumber02,mGiftNumber03,mGiftNumber04,mGiftNumber05,mGiftNumber06;
    private TextView user_gift,recharge;
    private View mRoot;
    private Context context;
    public Dialog dialog;
    private PropBeanList mPropBeanList;
    private int mCount;
    private LivePlayActivity activity;
    private String mUserID;
    private String mProgramID;
    private String mPropID;
    private String mPropName;
    private String mPropImg;
    private ImageLoader mImageLoader = ImageLoaderUtils.getDefaultImageLoader(MainApplication.getInstance());;
    public GiftFullPopWindow(Context context,PropBeanList propBeanList,LivePlayActivity fragment,String userId,String programId){
        this.context = context;
        this.mPropBeanList = propBeanList;
        this.activity = fragment;
        this.mUserID = userId;
        this.mProgramID = programId;
        LayoutInflater inflater = LayoutInflater.from(context);
        mRoot = inflater.inflate(R.layout.gift_full_pop_window,null);
        recharge = (TextView)mRoot.findViewById(R.id.recharge);
        mGiftImage01 = (ImageView)mRoot.findViewById(R.id.gift_img_01);
        mGiftImage02 = (ImageView)mRoot.findViewById(R.id.gift_img_02);
        mGiftImage03 = (ImageView)mRoot.findViewById(R.id.gift_img_03);
        mGiftImage04 = (ImageView)mRoot.findViewById(R.id.gift_img_04);
        mGiftImage05 = (ImageView)mRoot.findViewById(R.id.gift_img_05);
        mGiftImage06 = (ImageView)mRoot.findViewById(R.id.gift_img_06);
        mImageLoader.displayImage(mPropBeanList.props.get(0).img,mGiftImage01);
        mImageLoader.displayImage(mPropBeanList.props.get(1).img,mGiftImage02);
        mImageLoader.displayImage(mPropBeanList.props.get(2).img,mGiftImage03);
        mImageLoader.displayImage(mPropBeanList.props.get(3).img,mGiftImage04);
        mImageLoader.displayImage(mPropBeanList.props.get(4).img,mGiftImage05);
        mImageLoader.displayImage(mPropBeanList.props.get(5).img,mGiftImage06);
        mGiftName01 = (TextView) mRoot.findViewById(R.id.gift_name_01);
        mGiftName02 = (TextView) mRoot.findViewById(R.id.gift_name_02);
        mGiftName03 = (TextView) mRoot.findViewById(R.id.gift_name_03);
        mGiftName04 = (TextView) mRoot.findViewById(R.id.gift_name_04);
        mGiftName05 = (TextView) mRoot.findViewById(R.id.gift_name_05);
        mGiftName06 = (TextView) mRoot.findViewById(R.id.gift_name_06);
        user_gift = (TextView)mRoot.findViewById(R.id.user_gift);
        mGiftName01.setText((mPropBeanList.props.get(0).name.length() > 5) ? mPropBeanList.props.get(0).name.substring(0,6):mPropBeanList.props.get(0).name);
        mGiftName02.setText(mPropBeanList.props.get(1).name);
        mGiftName03.setText(mPropBeanList.props.get(2).name);
        mGiftName04.setText(mPropBeanList.props.get(3).name);
        mGiftName05.setText(mPropBeanList.props.get(4).name);
        mGiftName06.setText(mPropBeanList.props.get(5).name);
        mGiftNumber01 = (TextView) mRoot.findViewById(R.id.gift_num_01);
        mGiftNumber02 = (TextView) mRoot.findViewById(R.id.gift_num_02);
        mGiftNumber03 = (TextView) mRoot.findViewById(R.id.gift_num_03);
        mGiftNumber04 = (TextView) mRoot.findViewById(R.id.gift_num_04);
        mGiftNumber05 = (TextView) mRoot.findViewById(R.id.gift_num_05);
        mGiftNumber06 = (TextView) mRoot.findViewById(R.id.gift_num_06);
        mGiftNumber01.setText((int)mPropBeanList.props.get(0).price + (mPropBeanList.props.get(0).type.equals("rmb") ? "金角" : "金币"));
        mGiftNumber02.setText((int)mPropBeanList.props.get(1).price + (mPropBeanList.props.get(1).type.equals("rmb") ? "金角" : "金币"));
        mGiftNumber03.setText((int)mPropBeanList.props.get(2).price + (mPropBeanList.props.get(2).type.equals("rmb") ? "金角" : "金币"));
        mGiftNumber04.setText((int)mPropBeanList.props.get(3).price + (mPropBeanList.props.get(3).type.equals("rmb") ? "金角" : "金币"));
        mGiftNumber05.setText((int)mPropBeanList.props.get(4).price + (mPropBeanList.props.get(4).type.equals("rmb") ? "金角" : "金币"));
        mGiftNumber06.setText((int)mPropBeanList.props.get(5).price + (mPropBeanList.props.get(5).type.equals("rmb") ? "金角" : "金币"));
        mGiftImage01.setOnClickListener(this);
        mGiftImage02.setOnClickListener(this);
        mGiftImage03.setOnClickListener(this);
        mGiftImage04.setOnClickListener(this);
        mGiftImage05.setOnClickListener(this);
        mGiftImage06.setOnClickListener(this);
        recharge.setOnClickListener(this);
        if(!UserUtils.isNotLogin()) {
            UserCenterModel mUserCenterModel = RetrofitUtils.create(UserCenterModel.class);
            mUserCenterModel.getUserInfo(CacheUtils.getCache().getAsString(CacheConstants.CACHE_USERID), CommConstants.COMMON_PAGE_COUNT, 1, CommConstants.DEFAULT_SORT_ONLINE_PERSON, new DefaultLiveHttpCallBack<UserCenterBean>() {
                @Override
                public void success(UserCenterBean userCenterBean) {
                    CacheUtils.saveUserInfo(userCenterBean.userinfo);
                    user_gift.setText("金币 " + userCenterBean.userinfo.mycoin + "  金角 " + userCenterBean.userinfo.myjinjiao);
                }

                @Override
                public void failure(String message) {

                }
            });
        }
        this.setContentView(mRoot);
    }

    private void checkedButton(int position){
        ToastUtils.showToast(context,"赠送成功");
        mCount = (int) mPropBeanList.props.get(position).price * 100;
        mPropName = mPropBeanList.props.get(position).name;
        mPropID = mPropBeanList.props.get(position)._id;
        mPropImg = mPropBeanList.props.get(position).img;
        if(mPropBeanList.props.get(position).type.equals("rmb")){
            giveGiftJinjiao(mPropBeanList.props.get(position)._id,mPropBeanList.props.get(position).name);
        }else{
            giveGiftAcoin(mPropBeanList.props.get(position)._id,mPropBeanList.props.get(position).name);
        }
        this.dismiss();
    }
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.gift_img_01) {
            checkedButton(0);
        } else if (v.getId() == R.id.gift_img_02) {
            checkedButton(1);
        } else if (v.getId() == R.id.gift_img_03){
            checkedButton(2);
        }else if(v.getId() == R.id.gift_img_04) {
            checkedButton(3);
        }else if(v.getId() == R.id.gift_img_05) {
            checkedButton(4);
        }else if(v.getId() == R.id.gift_img_06) {
            checkedButton(5);
        }else if(v.getId() == R.id.cancel_buy) {
            if (dialog != null && dialog.isShowing())
                dialog.dismiss();
        }else if(v.getId() == R.id.confirm_buy) {
            if (mConfirmString.contains("购买")) {
                Bundle bundle = new Bundle();
                bundle.putString("userid", CacheUtils.getCacheUserInfo()._id);
                bundle.putInt("count", mCount);
                bundle.putString("propid", mPropID);
                bundle.putString("propname", mPropName);
                bundle.putString("nickname", CacheUtils.getCacheUserInfo().nickname);
                IntentUtils.openActivityForResult((Activity) context, PaymentActivity.class, 66, bundle);
            } else {
                IntentUtils.openActivity(context, TaskActivity.class);
            }
        }else if(v.getId() == R.id.recharge){
                IntentUtils.openActivity(context, RechargeActivity.class);
        }
    }
    private void giveGiftAcoin(String type,String propName){
        if(UserUtils.isNotLogin()){
            IntentUtils.openActivity(context, LoginActivity.class);
            return ;
        }
        if(mUserID.equals(CacheUtils.getCacheUserInfo()._id)){
            ToastUtils.showToast(context, "自己不能给自己赠送金币");
            return;
        }
        if(CacheUtils.getCacheUserInfo().mycoin < mCount/100){
            System.out.println(mCount);
            System.out.println(CacheUtils.getCacheUserInfo().mycoin);
            showDialog("您的金币不足，是否直接购买", "去做任务");
            return;
        }
        GiveGiftUtil.giveGiftAcoin(type, mUserID, activity, mCount / 100, propName, mProgramID,mPropImg);
        if(this.isShowing()){
            this.dismiss();
        }
    }
    private void giveGiftJinjiao(String propid,String propName){
        if(UserUtils.isNotLogin()){
            IntentUtils.openActivity(context, LoginActivity.class);
            return ;
        }
        if(mUserID.equals(CacheUtils.getCacheUserInfo()._id)){
            ToastUtils.showToast(context, "自己不能给自己赠送金角");
            return;
        }
        if(CacheUtils.getCacheUserInfo().myjinjiao < mCount / 100){
            System.out.println(mCount);
            System.out.println(CacheUtils.getCacheUserInfo().myjinjiao);
            showDialog("您的金角不足，是否直接购买", "直接购买");
            return;
        }
        GiveGiftUtil.giveGiftJinjao(propid, mUserID, activity, mCount / 100, propName,mProgramID,mPropImg);
        if(this.isShowing()){
            this.dismiss();
        }
    }
    private String mConfirmString;
    private void showDialog(String title,String text){
        dialog = new AlertDialog.Builder(context).create();
        View dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_gift_buzu,null);
        TextView cancelText = (TextView) dialogView.findViewById(R.id.cancel_buy);
        TextView confirmText = (TextView) dialogView.findViewById(R.id.confirm_buy);
        TextView titleText = (TextView) dialogView.findViewById(R.id.title);
        titleText.setText(title);
        confirmText.setText(text);
        this.mConfirmString = text;
        cancelText.setOnClickListener(this);
        confirmText.setOnClickListener(this);
        dialog.show();
        dialog.getWindow().setContentView(dialogView);
    }
}
