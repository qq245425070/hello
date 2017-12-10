package cn.gietv.mlive.views;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.TextView;

import cn.gietv.mlive.R;
import cn.gietv.mlive.constants.CommConstants;
import cn.gietv.mlive.http.DefaultLiveHttpCallBack;
import cn.gietv.mlive.modules.video.model.VideoModel;
import cn.gietv.mlive.utils.ToastUtils;
import cn.kalading.android.retrofit.utils.RetrofitUtils;

/**
 * Created by houde on 2016/8/5.
 */
public class ReportPopuWindow extends PopupWindow implements View.OnClickListener {
    private String mID;
    private int mType;
    private Context mContext;
    private View mRootView;
    private TextView mReportText,mCancelText,mNotPlayText;
    private VideoModel model;
    public ReportPopuWindow(Context context,String id,int type){
        this.mContext = context;
        this.mType = type;
        this.mID = id;
        mRootView = LayoutInflater.from(context).inflate(R.layout.report_layout,null);
        mReportText = (TextView) mRootView.findViewById(R.id.report);
        mCancelText = (TextView) mRootView.findViewById(R.id.cancel);
        mNotPlayText = (TextView) mRootView.findViewById(R.id.not_play);
        if(type == CommConstants.TYPE_COMMENT){
            mNotPlayText.setVisibility(View.INVISIBLE);
        }
        mNotPlayText.setOnClickListener(this);
        model = RetrofitUtils.create(VideoModel.class);
        mReportText.setOnClickListener(this);
        mCancelText.setOnClickListener(this);
        setContentView(mRootView);
    }

    public void setTypeAndID(int type , String id){
        this.mType = type;
        this.mID = id;
        if(type == CommConstants.TYPE_COMMENT){
            mNotPlayText.setVisibility(View.INVISIBLE);
        }else{
            mNotPlayText.setVisibility(View.VISIBLE);
        }
    }
    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.report){
            dismiss();
            model.report(this.mID, this.mType,"report", new DefaultLiveHttpCallBack<String>() {
                @Override
                public void success(String s) {
                    ToastUtils.showToastShort(mContext,"举报成功");
                }

                @Override
                public void failure(String message) {

                }
            });
        }else if(v.getId() == R.id.cancel){
            dismiss();
        }else if(v.getId() == R.id.not_play){
            dismiss();
            ((Activity)mContext).finish();
            model.report(this.mID, this.mType, "noplay", new DefaultLiveHttpCallBack<String>() {
                @Override
                public void success(String s) {

                }

                @Override
                public void failure(String message) {

                }
            });
        }
    }
}
