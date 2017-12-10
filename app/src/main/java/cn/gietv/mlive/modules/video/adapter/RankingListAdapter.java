package cn.gietv.mlive.modules.video.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

import cn.gietv.mlive.MainApplication;
import cn.gietv.mlive.R;
import cn.gietv.mlive.base.AbsArrayAdapter;
import cn.gietv.mlive.modules.compere.activity.CompereActivity;
import cn.gietv.mlive.modules.video.bean.Contribution;
import cn.gietv.mlive.utils.CacheUtils;
import cn.gietv.mlive.utils.ImageLoaderUtils;
import cn.gietv.mlive.utils.NumUtils;
import cn.gietv.mlive.utils.UserUtils;
import cn.gietv.mlive.utils.ViewHolder;

/**
 * Created by houde on 2016/2/2.
 */
public class RankingListAdapter extends AbsArrayAdapter<Contribution> {
    private ImageLoader imageLoader;
    public RankingListAdapter(Context context, List<Contribution> objects) {
        super(context, objects);
        imageLoader = ImageLoaderUtils.getDefaultImageLoader(MainApplication.getInstance());
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Contribution contribution = getItem(position);
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View mRootView = inflater.inflate(R.layout.adapter_ranking_list, null);
        TextView ranking = ViewHolder.get(mRootView, R.id.ranking_list_ranking);
        ImageView avatar = ViewHolder.get(mRootView,R.id.ranking_list_avatar);
        ImageView rankingImg = ViewHolder.get(mRootView,R.id.ranking_list_ranking_img);
        TextView contributionValue = ViewHolder.get(mRootView,R.id.contribution_value);
        TextView nickname = ViewHolder.get(mRootView,R.id.ranking_list_nickname);
        ranking.setText(position + 1 + "");
        if(position == 0){
            ranking.setBackgroundResource(R.drawable.commen_button_light_red_selected);
            ranking.setTextColor(getContext().getResources().getColor(R.color.white));
        }else if(position == 1){
            ranking.setBackgroundResource(R.drawable.commen_button_light_orange_selected);
            ranking.setTextColor(getContext().getResources().getColor(R.color.white));
        }else if(position == 2){
            ranking.setBackgroundResource(R.drawable.commen_button_light_green_selected2);
            ranking.setTextColor(getContext().getResources().getColor(R.color.white));
        }else{
            ranking.setBackgroundResource(R.drawable.commen_button_theme_color);
            ranking.setTextColor(getContext().getResources().getColor(R.color.black));
            if(!UserUtils.isNotLogin()){
                if(CacheUtils.getCacheUserInfo()._id.equals(contribution.userinfo._id)){
                    ranking.setBackgroundResource(R.drawable.commen_button_theme_color2);
                    ranking.setTextColor(getContext().getResources().getColor(R.color.white));
                }
            }
        }
        imageLoader.displayImage(contribution.userinfo.avatar, avatar);
        imageLoader.displayImage(contribution.levelimg,rankingImg);
        contributionValue.setText(NumUtils.w(contribution.contribution) + "金角");
        nickname.setText(contribution.userinfo.nickname);
        mRootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("..............  "+contribution.userinfo._id);
                CompereActivity.openCompereActivity(getContext(),contribution.userinfo._id);
            }
        });
        return mRootView;
    }

    @Override
    public Contribution getItem(int position) {
        return super.getItem(position);
    }
}
