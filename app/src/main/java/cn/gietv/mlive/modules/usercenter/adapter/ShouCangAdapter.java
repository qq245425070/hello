package cn.gietv.mlive.modules.usercenter.adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import cn.gietv.mlive.R;
import cn.gietv.mlive.constants.CommConstants;
import cn.gietv.mlive.modules.program.bean.ProgramBean;
import cn.gietv.mlive.modules.video.activity.LivePlayListActivity;
import cn.gietv.mlive.modules.video.activity.VideoPlayListActivity3;
import cn.gietv.mlive.utils.ImageLoaderUtils;
import cn.gietv.mlive.utils.IntentUtils;

/**
 * Created by houde on 2016/3/25.
 */
public class ShouCangAdapter extends RecyclerView.Adapter<ShouCangAdapter.ViewHolder>{
    private Context mContext;
    private int mType;
    private ImageLoaderUtils imageLoader;
    private List<ProgramBean.ProgramEntity> mData;
    public ShouCangAdapter(Context context, List<ProgramBean.ProgramEntity> objects) {
        this.mContext = context;
        imageLoader = ImageLoaderUtils.getInstance();
        this.mData = objects;
    }


    public void getView(int position, ViewHolder holder) {
        ViewHolder viewHolder = holder;
        final ProgramBean.ProgramEntity programEntity = getItem(position);
        if(programEntity == null )
            return;
        viewHolder.mCount.setVisibility(View.VISIBLE);
        if(mType == 0){
            viewHolder.checkBox.setVisibility(View.GONE);
        }else{
            viewHolder.checkBox.setVisibility(View.VISIBLE);
            final ViewHolder finalViewHolder = viewHolder;
            viewHolder.checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    programEntity.check = finalViewHolder.checkBox.isChecked();
                }
            });
        }
        viewHolder.videoTitle.setTextColor(mContext.getResources().getColor(R.color.text_929292));
        imageLoader.downLoadImage(mContext, viewHolder.imageView,programEntity.spic,false);
        if(programEntity.name != null && programEntity.name.length() > 50)
            programEntity.name = programEntity.name.substring(0,50)  + "……";
        viewHolder.videoTitle.setText(programEntity.name);
        if(programEntity.userinfo != null)
            viewHolder.anchor.setText(programEntity.userinfo.nickname);
        viewHolder.videoInfo.setText(programEntity.onlines+"");
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (programEntity.type == CommConstants.CAROUSEL_TYPE_LIVE) {
                    Bundle bundle = new Bundle();
                    IntentUtils.openActivity(mContext, LivePlayListActivity.class, bundle);
                } else {
                    VideoPlayListActivity3.openVideoPlayListActivity2(mContext, programEntity);
                }
            }
        });
    }

    private ProgramBean.ProgramEntity getItem(int position) {
        return mData.get(position);
    }

    public void setType(int type){
        this.mType = type;
    }
    public int getType(){
        return mType;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.shoucang_item,null));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        getView(position,holder);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        CheckBox checkBox;
        ImageView imageView,mCount;
        TextView videoTitle,anchor,videoInfo;

        public ViewHolder(View itemView) {
            super(itemView);
            checkBox = (CheckBox) itemView.findViewById(R.id.download_check);
            imageView = (ImageView) itemView.findViewById(R.id.download_video_image);
            videoTitle = (TextView) itemView.findViewById(R.id.download_video_name);
            anchor = (TextView)itemView.findViewById(R.id.download_anchor);
            videoInfo = (TextView)itemView.findViewById(R.id.download_info);
            mCount = (ImageView)itemView.findViewById(R.id.shoucang_count);
        }
    }
}
