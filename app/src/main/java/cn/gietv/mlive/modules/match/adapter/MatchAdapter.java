package cn.gietv.mlive.modules.match.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

import cn.gietv.mlive.R;
import cn.gietv.mlive.db.DBUtils;
import cn.gietv.mlive.modules.program.bean.ProgramBean;
import cn.gietv.mlive.modules.video.activity.VideoPlayListActivity3;
import cn.gietv.mlive.utils.ImageLoaderUtils;
import cn.gietv.mlive.utils.NumUtils;
import cn.gietv.mlive.views.SquareImageView2;

/**
 * 快速入口中赛事的Adapter
 * 作者：houde on 2016/11/23 16:05
 * 邮箱：yangzhonghao@gietv.com
 */
public class MatchAdapter extends RecyclerView.Adapter<MatchAdapter.ProgramHolder> {
    private List<ProgramBean.ProgramEntity> mProgramList;
    private Context mContext;
    private ImageLoader imageLoader;
    public MatchAdapter(Context context, List<ProgramBean.ProgramEntity> programEntities){
        this.mContext = context;
        this.mProgramList = programEntities;
        imageLoader = ImageLoaderUtils.getDefaultImageLoader(context);
    }
    @Override
    public MatchAdapter.ProgramHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ProgramHolder(LayoutInflater.from(mContext).inflate(R.layout.adapter_match,null));
    }

    @Override
    public void onBindViewHolder(MatchAdapter.ProgramHolder holder, int position) {
        final ProgramBean.ProgramEntity bean = mProgramList.get(position);
        if (null == bean)
            return ;
        imageLoader.displayImage(bean.spic,holder.image);
        holder.count.setText(NumUtils.w(bean.onlines));
        if(bean.userinfo != null)
            holder.anchor.setText(bean.userinfo.nickname);
        holder.name.setText(bean.name);
        if(null != DBUtils.getInstance(mContext).getVideoById(bean._id)){
            holder.name.setTextColor(mContext.getResources().getColor(R.color.text_929292));
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VideoPlayListActivity3.openVideoPlayListActivity2(mContext,bean);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mProgramList.size();
    }
    public class ProgramHolder extends RecyclerView.ViewHolder{
        SquareImageView2 image;
        TextView name;
        TextView anchor;
        TextView count;
        public ProgramHolder(View convertView) {
            super(convertView);
            image = (SquareImageView2) convertView.findViewById(R.id.game_info_iv_image);
            name = (TextView) convertView.findViewById(R.id.album_name);
            anchor = (TextView) convertView.findViewById(R.id.album_anchor);
            count = (TextView) convertView.findViewById(R.id.play_count);
        }
    }
}
