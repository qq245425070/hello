package cn.gietv.mlive.modules.program.adapter;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

import cn.gietv.mlive.base.AbsArrayAdapter;
import cn.gietv.mlive.constants.CommConstants;
import cn.gietv.mlive.modules.program.bean.ProgramBean;
import cn.gietv.mlive.modules.video.activity.LivePlayListActivity;
import cn.gietv.mlive.modules.video.activity.VideoPlayListActivity3;
import cn.gietv.mlive.utils.ImageLoaderUtils;
import cn.gietv.mlive.utils.IntentUtils;
import cn.gietv.mlive.utils.NumUtils;
import cn.gietv.mlive.utils.ViewHolder;
import cn.gietv.mlive.views.SquareImageView;

/**
 * author：steven
 * datetime：15/10/8 09:45
 */
public class ProgramAdapter extends AbsArrayAdapter<ProgramBean.ProgramEntity> {
    private ImageLoader mImageLoader;

    public ProgramAdapter(Context context, List<ProgramBean.ProgramEntity> objects) {
        super(context, objects);
        mImageLoader = ImageLoaderUtils.getDefaultImageLoader(getContext());
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(cn.gietv.mlive.R.layout.program_list_item_adapter, null);
        }
        final ProgramBean.ProgramEntity item = getItem(position);
        TextView gameNameText = ViewHolder.get(convertView, cn.gietv.mlive.R.id.program_tv_game_name);
        TextView gameDescText = ViewHolder.get(convertView, cn.gietv.mlive.R.id.program_list_tv_game_desc);
        TextView playCountText = ViewHolder.get(convertView, cn.gietv.mlive.R.id.program_list_tv_play_count);
        TextView attentionCountText = ViewHolder.get(convertView, cn.gietv.mlive.R.id.program_list_tv_attention_count);
        attentionCountText.setVisibility(View.INVISIBLE);
        SquareImageView showImage = ViewHolder.get(convertView, cn.gietv.mlive.R.id.program_list_iv_image);
        // FIXME 底部文字
        gameNameText.setText(item.name);
        attentionCountText.setVisibility(View.GONE);
        ImageView imageView = ViewHolder.get(convertView, cn.gietv.mlive.R.id.live_count);
        imageView.setImageBitmap(BitmapFactory.decodeResource(getContext().getResources(), cn.gietv.mlive.R.mipmap.program_play_count_icon));
        if(item.userinfo == null){
            return convertView;
        }
        gameDescText.setText(item.userinfo.nickname);
        playCountText.setText(NumUtils.w(item.onlines));
        attentionCountText.setText(NumUtils.w(item.follows));
        showImage.setRatio(1.78f);
        mImageLoader.displayImage(item.spic, showImage);
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (item.type == CommConstants.CAROUSEL_TYPE_LIVE) {
                    Bundle bundle = new Bundle();
//                    bundle.putSerializable(LivePlayListActivity.EXTRA_PROGRAM, item);
                    IntentUtils.openActivity(getContext(), LivePlayListActivity.class, bundle);
                    //((AbsBaseActivity) getContext()).overridePendingTransition(R.anim.activity_open,R.anim.activity_open);
                } else {
                    VideoPlayListActivity3.openVideoPlayListActivity2(getContext(), item);
                }
            }
        });
        return convertView;
    }
}
