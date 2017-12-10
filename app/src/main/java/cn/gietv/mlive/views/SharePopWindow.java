package cn.gietv.mlive.views;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.umeng.socialize.ShareAction;
import com.umeng.socialize.ShareContent;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMVideo;

import cn.gietv.mlive.R;
import cn.gietv.mlive.http.DefaultLiveHttpCallBack;
import cn.gietv.mlive.modules.statistics.StatisticsMode;
import cn.kalading.android.retrofit.utils.RetrofitUtils;

/**
 * Created by houde on 2016/3/18.
 */
public class SharePopWindow extends PopupWindow implements View.OnClickListener{
    /*
    * 1:微信2微信朋友圈3QQ 4QQ空间5微博
    * */
    private final int WEIXIN = 1;
    private final int FREND_CIRCLE = 2;
    private final int QQ = 3;
    private final int SINA = 5;
    private int mShareType;
    private Context mContext;
    private View mRootView;
    private ImageView mQQImage,mWeixinImage,mSinaImage,mFriendImage;
    private String mImagePath,mShareUrl,mContent;
    private ShareAction mShareAction;
    private String mRecourceID;
    private int mRecourceType;
    private StatisticsMode mode;
    public SharePopWindow(Context context,String imagePath,String shareUrl,String title){
        this.mContext = context;
        this.mode = RetrofitUtils.create(StatisticsMode.class);
        this.mImagePath = imagePath;
        this.mShareUrl = shareUrl;
        this.mContent = title;
        mShareAction = new ShareAction((Activity)context);
        mRootView = LayoutInflater.from(context).inflate(R.layout.pop_shared,null);
        mQQImage = (ImageView)mRootView.findViewById(R.id.share_qq);
        mWeixinImage = (ImageView)mRootView.findViewById(R.id.share_weixin);
        mSinaImage = (ImageView)mRootView.findViewById(R.id.share_sina);
        mFriendImage = (ImageView)mRootView.findViewById(R.id.share_friend);
        mWeixinImage.setOnClickListener(this);
        mQQImage.setOnClickListener(this);
        mSinaImage.setOnClickListener(this);
        mFriendImage.setOnClickListener(this);
        setContentView(mRootView);
    }
    public void setData(String resourceid,int resouceType){
        this.mRecourceID = resourceid;
        this.mRecourceType = resouceType;
    }
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.share_friend) {
            shareWeixinFriend();
            mShareType = FREND_CIRCLE;
        }else if(v.getId() == R.id.share_qq) {
            shareQQ();
            mShareType = QQ;
        }else if(v.getId() == R.id.share_sina) {
            shareSina();
            mShareType = SINA;
        }else if(v.getId() == R.id.share_weixin){
            shareWeixin();
            mShareType = WEIXIN;
        }
    }
    private void shareSina(){
        UMShare(SHARE_MEDIA.SINA);
    }
    private void shareQQ(){
        UMShare(SHARE_MEDIA.QQ);
    }
    private void shareWeixinFriend(){
        UMVideo umVideo = new UMVideo(mShareUrl);
        umVideo.setThumb(mImagePath);
        umVideo.setTitle(mContent);
        UMImage image = new UMImage(mContext, mImagePath);
        // 设置视频缩略图
        umVideo.setThumb(image);
        umVideo.setDescription(mContent);
        ShareContent shareContent = new ShareContent();
        shareContent.mTitle = mContent;
        shareContent.mText = mContent;
        shareContent.mTargetUrl = mShareUrl;
        shareContent.mMedia = new UMImage(mContext,mImagePath);
        mShareAction.setPlatform(SHARE_MEDIA.WEIXIN_CIRCLE).withMedia(umVideo).setShareContent(shareContent).setCallback(umShareListener).share();
    }
    private void shareWeixin() {
        UMShare(SHARE_MEDIA.WEIXIN);
    }
    private void UMShare(SHARE_MEDIA share){
        ShareContent shareContent = new ShareContent();
        shareContent.mTitle = "游戏茶餐厅";
        shareContent.mText = mContent;
        shareContent.mTargetUrl = mShareUrl;
        if(mShareType != SINA)
            shareContent.mMedia = new UMImage(mContext,mImagePath);
        mShareAction.setPlatform(share).withMedia(getUMVideo()).setShareContent(shareContent).setCallback(umShareListener).share();
    }
    private UMVideo getUMVideo(){
        UMVideo umVideo = new UMVideo(mShareUrl);
        umVideo.setThumb(mImagePath);
        umVideo.setTitle("游戏茶餐厅");
        UMImage image = new UMImage(mContext, mImagePath);
        // 设置视频缩略图
        umVideo.setThumb(image);
        umVideo.setDescription(mContent);
        return umVideo;
    }

    private UMShareListener umShareListener = new UMShareListener() {
        @Override
        public void onResult(SHARE_MEDIA platform) {
            if(platform.name().equals("WEIXIN_FAVORITE")){
                Toast.makeText(mContext,platform + " 收藏成功啦",Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(mContext, platform + " 分享成功啦", Toast.LENGTH_SHORT).show();
            }
            sendShareData();
        }

        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            Toast.makeText(mContext,platform + " 分享失败啦", Toast.LENGTH_SHORT).show();
            if(t!=null){
                Log.d("throw","throw:"+t.getMessage());
            }
        }

        @Override
        public void onCancel(SHARE_MEDIA platform) {
            Toast.makeText(mContext,platform + " 分享取消了", Toast.LENGTH_SHORT).show();
        }
    };

    private void sendShareData() {
        mode.sendShareData(mRecourceID, mRecourceType, mShareType, new DefaultLiveHttpCallBack<String>() {
            @Override
            public void success(String s) {

            }

            @Override
            public void failure(String message) {

            }
        });
    }
}
