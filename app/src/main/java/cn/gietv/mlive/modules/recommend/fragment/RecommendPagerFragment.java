package cn.gietv.mlive.modules.recommend.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nostra13.universalimageloader.core.ImageLoader;

import cn.gietv.mlive.base.AbsBaseFragment;
import cn.gietv.mlive.constants.CommConstants;
import cn.gietv.mlive.modules.album.activity.AlbumActivity;
import cn.gietv.mlive.modules.category.activity.CategoryActivity;
import cn.gietv.mlive.modules.compere.activity.AnchorActivity;
import cn.gietv.mlive.modules.compere.activity.CompereActivity;
import cn.gietv.mlive.modules.program.bean.ProgramBean;
import cn.gietv.mlive.modules.recommend.bean.RecommendBean;
import cn.gietv.mlive.modules.video.activity.VideoPlayListActivity3;
import cn.gietv.mlive.utils.ImageLoaderUtils;
import cn.gietv.mlive.views.SquareImageView;

/**
 * author：steven
 * datetime：15/9/21 19:42
 *
 */
public class RecommendPagerFragment extends AbsBaseFragment implements View.OnClickListener {
    private static final String EXTRA_CAROUSE = "extra_carouse";
    private View mCurrentView;
    private ImageLoader mImageLoader;
    private SquareImageView mShowImage;
    private RecommendBean.RecommendCarouseEntity mCarouseEntity;

    public static RecommendPagerFragment getInstence(RecommendBean.RecommendCarouseEntity carouseEntity) {
        RecommendPagerFragment fragment = new RecommendPagerFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(EXTRA_CAROUSE, carouseEntity);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mCurrentView = inflater.inflate(cn.gietv.mlive.R.layout.recommend_pager_item, container, false);
        mShowImage = (SquareImageView) mCurrentView.findViewById(cn.gietv.mlive.R.id.recommend_iv_pager);
        mShowImage.setRatio(1.78f);
        mImageLoader = ImageLoaderUtils.getDefaultImageLoader(getActivity());
        mCarouseEntity = (RecommendBean.RecommendCarouseEntity) getArguments().getSerializable(EXTRA_CAROUSE);
        mImageLoader.displayImage(mCarouseEntity.carouselimg, mShowImage);
        mShowImage.setOnClickListener(this);
        return mCurrentView;
    }

    @Override
    public void onClick(View view) {
        switch (mCarouseEntity.carouseltype) {
            case CommConstants.CAROUSEL_TYPE_LIVE:
                break;
            case CommConstants.CAROUSEL_TYPE_VIDEO:
            case CommConstants.CAROUSEL_TYPE_360:
            case CommConstants.CAROUSEL_TYPE_3D:
                ProgramBean.ProgramEntity proEntity = new ProgramBean.ProgramEntity();
                proEntity._id = mCarouseEntity.resourceid;
                proEntity.name = mCarouseEntity.carouselname;
                proEntity.type = mCarouseEntity.carouseltype;
                VideoPlayListActivity3.openVideoPlayListActivity2(getActivity(), proEntity);
                break;
            case CommConstants.CAROUSEL_TYPE_GAME:
                break;
            case CommConstants.CAROUSEL_TYPE_COMPERE:
                CompereActivity.openCompereActivity(getActivity(), mCarouseEntity.resourceid);
                break;
            case CommConstants.CAROUSEL_TYPE_AREA:
                CategoryActivity.openActivity(getActivity(), mCarouseEntity.resourceid, mCarouseEntity.carouselname, mCarouseEntity.carouseltype);
                break;
            case CommConstants.CAROUSEL_TYPE_WCA:
                Intent intent = new Intent();
                intent.setAction("android.intent.action.VIEW");
                Uri content_url = Uri.parse(mCarouseEntity.resourceid);
                intent.setData(content_url);
                startActivity(intent);
                break;
            case CommConstants.CAROUSEL_TYPE_ALBUM:
                AlbumActivity.openAlbumActivity(mCarouseEntity.carouselname,mCarouseEntity.resourceid,getActivity());
                break;
            case CommConstants.CAROUSEL_TYPE_ANCHOR:
                AnchorActivity.openAnchorActivity(getActivity(),mCarouseEntity.resourceid,mCarouseEntity.carouselname);
                break;
        }

    }
}
