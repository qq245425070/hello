package cn.gietv.mlive.modules.recommend.adapter;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

import cn.gietv.mlive.base.AbsArrayAdapter;
import cn.gietv.mlive.modules.common.OnItemClick;
import cn.gietv.mlive.modules.compere.activity.CompereActivity;
import cn.gietv.mlive.modules.recommend.bean.RecommendBean;
import cn.gietv.mlive.modules.usercenter.bean.UserCenterBean;
import cn.gietv.mlive.utils.ImageLoaderUtils;
import cn.gietv.mlive.utils.ViewHolder;

/**
 * author：steven
 * datetime：15/9/21 10:39
 */
public class RecommendCompereAdapter2 extends AbsArrayAdapter<RecommendBean.RecommendAnchorEntity> {
    private ImageLoader mImageLoader;
    private int mWidth = ViewGroup.LayoutParams.WRAP_CONTENT;
    private OnItemClick<UserCenterBean.UserinfoEntity> mOnItemClick;

    public void setOnItemClick(OnItemClick<UserCenterBean.UserinfoEntity> onItemClick) {
        mOnItemClick = onItemClick;
    }

    public RecommendCompereAdapter2(Context context, List<RecommendBean.RecommendAnchorEntity> objects) {
        super(context, objects);
        mImageLoader = ImageLoaderUtils.getDefaultImageLoader(getContext());

        WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(outMetrics);
        int width = outMetrics.widthPixels;
       // mWidth = width / 4;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(cn.gietv.mlive.R.layout.recommend_hlistview_item, null);
        }

        final RecommendBean.RecommendAnchorEntity anchor = getItem(position);
        ImageView img = ViewHolder.get(convertView, cn.gietv.mlive.R.id.recommend_iv_compere);
        TextView tvName = ViewHolder.get(convertView, cn.gietv.mlive.R.id.recommend_tv_compere_name);
        TextView tvGame = ViewHolder.get(convertView, cn.gietv.mlive.R.id.recommend_tv_compere_game);
        mImageLoader.displayImage(anchor.userinfo.avatar, img);
        tvName.setText(anchor.userinfo.nickname);
        // FIXME
        tvGame.setText(anchor.gamename);
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                UserCenterActivity.openUserCenterActivity(getContext(), anchor.userinfo._id, anchor.userinfo.nickname);
                if (mOnItemClick != null) {
                    mOnItemClick.onClick(anchor.userinfo);
                } else {
                    CompereActivity.openCompereActivity(getContext(), anchor.userinfo._id);
                }
            }
        });
        return convertView;
    }
}
