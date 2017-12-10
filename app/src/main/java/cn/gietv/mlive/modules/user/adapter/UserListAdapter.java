package cn.gietv.mlive.modules.user.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import cn.gietv.mlive.base.AbsArrayAdapter;
import cn.gietv.mlive.modules.compere.activity.CompereActivity;
import cn.gietv.mlive.modules.usercenter.bean.UserCenterBean;
import cn.gietv.mlive.utils.ImageLoaderUtils;
import cn.gietv.mlive.utils.NumUtils;
import cn.gietv.mlive.utils.ViewHolder;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * author：steven
 * datetime：15/10/16 23:04
 */
public class UserListAdapter extends AbsArrayAdapter<UserCenterBean.UserinfoEntity> {
    private ImageLoader mImageLoader;

    public UserListAdapter(Context context, List<UserCenterBean.UserinfoEntity> objects) {
        super(context, objects);
        mImageLoader = ImageLoaderUtils.getDefaultImageLoader(getContext());
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(cn.gietv.mlive.R.layout.compere_list_adapter, null);
        }
        final UserCenterBean.UserinfoEntity entity = getItem(position);
        ImageView image = ViewHolder.get(convertView, cn.gietv.mlive.R.id.compere_list_iv_image);
        mImageLoader.displayImage(entity.avatar, image);
        ViewHolder.setText(convertView, cn.gietv.mlive.R.id.compere_list_tv_name, entity.nickname);
        ViewHolder.setText(convertView, cn.gietv.mlive.R.id.compere_list_tv_attentionCount, NumUtils.w(entity.follows) + "人关注");
        if (position == getCount() - 1) {
            ViewHolder.get(convertView, cn.gietv.mlive.R.id.line).setVisibility(View.VISIBLE);
        } else {
            ViewHolder.get(convertView, cn.gietv.mlive.R.id.line).setVisibility(View.GONE);
        }
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CompereActivity.openCompereActivity(getContext(), entity._id);
            }
        });
        return convertView;
    }
}
