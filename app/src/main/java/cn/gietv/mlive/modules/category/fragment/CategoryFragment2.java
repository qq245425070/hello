package cn.gietv.mlive.modules.category.fragment;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import java.util.ArrayList;
import java.util.List;

import cn.gietv.mlive.R;
import cn.gietv.mlive.constants.CommConstants;
import cn.gietv.mlive.http.DefaultLiveHttpCallBack;
import cn.gietv.mlive.modules.category.activity.CategoryActivity2;
import cn.gietv.mlive.modules.category.adapter.CategoryAdapter;
import cn.gietv.mlive.modules.category.adapter.CategoryAdapter2;
import cn.gietv.mlive.modules.game.bean.AlbumBean;
import cn.gietv.mlive.modules.game.bean.AuthorBean;
import cn.gietv.mlive.modules.game.model.GameModel;
import cn.gietv.mlive.modules.program.bean.ProgramBean;
import cn.gietv.mlive.parallaxheaderviewpager.RecyclerViewFragment;
import cn.gietv.mlive.utils.DensityUtil;
import cn.gietv.mlive.utils.NumUtils;
import cn.gietv.mlive.utils.ToastUtils;
import cn.gietv.mlive.views.TagPopuWindow;
import cn.kalading.android.retrofit.utils.RetrofitUtils;

/**
 * Created by houde on 2016/8/19.
 */
public class CategoryFragment2 extends RecyclerViewFragment {
    private View mRootView;
    private GameModel gameModel;
    private List<Object> programEntityList;
    private CategoryAdapter2 mAdapter;
    private String mId;
    private int mCurrentPage;
    private String mCategroy;
    private List<Object> mAllData = new ArrayList<>();
    private int mType;//记录标签属于哪个Type  2  7  8
    private int mTagPosition; //记录标签选中的位置
    private TagPopuWindow mPopuWindow;
    private List<String> mProgramTags;
    private int[] location;
    private ShowPopuWindow mTagPopuWindow;
    private ProgressDialog mDialog;
    public static CategoryFragment2 getInstance(String id,String category,int position ){
        CategoryFragment2 categoryFragment = new CategoryFragment2();
        Bundle bundle = new Bundle();
        bundle.putString("id", id);
        bundle.putString("category", category);
        bundle.putInt(ARG_POSITION, position);
        categoryFragment.setArguments(bundle);
        return categoryFragment;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mPosition = getArguments().getInt(ARG_POSITION);
        Log.e("ceshi","CategoryFragment2");
        mRootView = inflater.inflate(R.layout.fragment_category2, container, false);
        mRecyclerView = (XRecyclerView) mRootView.findViewById(R.id.recyclerview);
        location = new int[2];
        mLayoutMgr = new LinearLayoutManager(getContext());
        mLayoutMgr.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(mLayoutMgr);
        mRecyclerView.setRefreshProgressStyle(ProgressStyle.SysProgress);
        mRecyclerView.setLaodingMoreProgressStyle(ProgressStyle.SysProgress);
        mRecyclerView.setArrowImageView(R.drawable.iconfont_downgrey);
        mRecyclerView.setPullRefreshEnabled(false);
//        mRecyclerView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
//            @Override
//            public void onGlobalLayout() {
//                location = new int[2];
//                mRecyclerView.getLocationOnScreen(location);
//                mRecyclerView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
//            }
//        });
        mDialog = new ProgressDialog(getActivity());
        mDialog.setMessage("正在请求数据，请等候");
        gameModel = RetrofitUtils.create(GameModel.class);
        mId = getArguments().getString("id");
        mCategroy = getArguments().getString("category");
        mType = getArguments().getInt("type");
        programEntityList = new ArrayList<>();
        mAdapter = new CategoryAdapter2(getActivity(), programEntityList,0);
        mRecyclerView.setAdapter(mAdapter);
        setRecyclerViewOnScrollListener();
        onPullDownToRefresh();
        mRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                onPullDownToRefresh();
            }

            @Override
            public void onLoadMore() {
                onPullUpToRefresh();
            }
        });
        return mRootView;
    }

    public void onPullDownToRefresh() {
        mCurrentPage = 1;
        switch (mCategroy) {
            case "video":
                gameModel.queryprogramlistbyareaid(mId,"all", CommConstants.COMMON_PAGE_COUNT, mCurrentPage, CommConstants.DEFAULT_SORT_ONLINE_PERSON, new DefaultLiveHttpCallBack<ProgramBean>() {
                    @Override
                    public void success(final ProgramBean programBean) {
                        if (isNotFinish()) {
                            mDialog.dismiss();
                            mRecyclerView.refreshComplete();
                            if(programBean == null){
                                return ;
                            }
                            if(programBean.albuminfo != null){
                                ((CategoryActivity2)getActivity()).changeSubscribeBg(programBean.albuminfo.isfollow);
                            }
                            if (NumUtils.getPage(programBean.cnt) == mCurrentPage) {
                                mRecyclerView.setLoadingMoreEnabled(false);
                            } else if (NumUtils.getPage(programBean.cnt) > mCurrentPage) {
                                mRecyclerView.setLoadingMoreEnabled(true);
                                mCurrentPage++;
                            }
                            programEntityList.clear();
                            if (programBean.taglist != null && programBean.taglist.size() > 0) {
                                programEntityList.add(programBean.taglist);
                                mProgramTags = programBean.taglist;
                            }
                            if (programBean.programs != null && programBean.programs.size() > 0)
                                programEntityList.addAll(programBean.programs);
                            mAdapter = new CategoryAdapter2(getActivity(), programEntityList, 0);
                            mRecyclerView.setAdapter(mAdapter);
                            mAllData.addAll(programEntityList);
                            mAdapter.setAdapterListener(new ProgramAdapterListener());
                            mTagPopuWindow = new ShowPopuWindow();
                            mAdapter.setShowPopuWindowListener(mTagPopuWindow);
                        }
                    }

                    @Override
                    public void failure(String message) {
                        if(isNotFinish()) {
                            mDialog.dismiss();
                            mRecyclerView.refreshComplete();
                            ToastUtils.showToast(getActivity(), message);
                        }
                    }
                });
                break;
            case "album":
                gameModel.getAlbumList(mId,"all", CommConstants.COMMON_PAGE_COUNT, mCurrentPage, CommConstants.DEFAULT_SORT_ONLINE_PERSON, new DefaultLiveHttpCallBack<AlbumBean>() {
                    @Override
                    public void success(AlbumBean albumBean) {
                        if(isNotFinish()) {
                            mDialog.dismiss();
                            mRecyclerView.refreshComplete();
                            if(albumBean == null){
                                return ;
                            }
                            if (NumUtils.getPage(albumBean.cnt) == mCurrentPage) {
                                mRecyclerView.setLoadingMoreEnabled(false);
                            } else if (NumUtils.getPage(albumBean.cnt) > mCurrentPage) {
                                mRecyclerView.setLoadingMoreEnabled(true);
                                mCurrentPage++;
                            }
                            programEntityList.clear();
                            if (albumBean.taglist != null && albumBean.taglist.size() > 0) {
                                programEntityList.add(albumBean.taglist);
                                mProgramTags = albumBean.taglist;
                            }
                            if (albumBean.albumlist != null && albumBean.albumlist.size() > 0)
                                programEntityList.addAll(albumBean.albumlist);
                            mAdapter = new CategoryAdapter2(getActivity(), programEntityList, 0);
                            mRecyclerView.setAdapter(mAdapter);
                            mAllData.addAll(programEntityList);
                            mAdapter.setAdapterListener(new AlbumAdapterListener());
                            mTagPopuWindow = new ShowPopuWindow();
                            mAdapter.setShowPopuWindowListener(mTagPopuWindow);
                        }
                    }

                    @Override
                    public void failure(String message) {
                        if (isNotFinish()) {
                            mDialog.dismiss();
                            mRecyclerView.refreshComplete();
                            ToastUtils.showToast(getActivity(), message);
                        }
                    }
                });
                break;
            case "anchor":
                gameModel.getAnchors(mId, "all", CommConstants.COMMON_PAGE_COUNT, mCurrentPage, CommConstants.DEFAULT_SORT_ONLINE_PERSON, new DefaultLiveHttpCallBack<AuthorBean>() {
                    @Override
                    public void success(AuthorBean authorBean) {
                        if (isNotFinish()) {
                            mDialog.dismiss();
                            mRecyclerView.refreshComplete();
                            if (authorBean == null) {
                                return;
                            }
                            if (NumUtils.getPage(authorBean.cnt) == mCurrentPage) {
                                mRecyclerView.setLoadingMoreEnabled(false);
                            } else if (NumUtils.getPage(authorBean.cnt) > mCurrentPage) {
                                mRecyclerView.setLoadingMoreEnabled(true);
                                mCurrentPage++;
                            }
                            programEntityList.clear();
                            if (authorBean.taglist != null && authorBean.taglist.size() > 0) {
                                programEntityList.add(authorBean.taglist);
                                mProgramTags = authorBean.taglist;
                            }
                            if (authorBean.userinfolist != null && authorBean.userinfolist.size() > 0)
                                programEntityList.addAll(authorBean.userinfolist);
                            mAdapter = new CategoryAdapter2(getActivity(), programEntityList, 0);
                            mRecyclerView.setAdapter(mAdapter);
                            mAllData.addAll(programEntityList);
                            mAdapter.setAdapterListener(new AuthorAdapterListener());
                            mTagPopuWindow = new ShowPopuWindow();
                            mAdapter.setShowPopuWindowListener(mTagPopuWindow);
                        }

                    }

                    @Override
                    public void failure(String message) {
                        if (isNotFinish()) {
                            mRecyclerView.refreshComplete();
                            ToastUtils.showToast(getActivity(), message);
                            mDialog.dismiss();
                        }
                    }
                });
                break;
        }
    }

    private void getAuthorByTag(String tag, int postionFlag) {
        programEntityList.clear();
        if("全部".equals(tag) || postionFlag == 0){
            onPullDownToRefresh();
            return;
        }
        mDialog.show();
        gameModel.getAnchors(mId, tag, 9999, 1, CommConstants.DEFAULT_SORT_ONLINE_PERSON, new DefaultLiveHttpCallBack<AuthorBean>() {
            @Override
            public void success(AuthorBean authorBean) {
                if (isNotFinish()) {
                    mDialog.dismiss();
                    mRecyclerView.setLoadingMoreEnabled(false);
                    if (authorBean.taglist != null && authorBean.taglist.size() > 0)
                        programEntityList.add(authorBean.taglist);
                    if (authorBean.userinfolist != null && authorBean.userinfolist.size() > 0)
                        programEntityList.addAll(authorBean.userinfolist);
                    mAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void failure(String message) {
                if(isNotFinish()) {
                    mRecyclerView.refreshComplete();
                    ToastUtils.showToast(getActivity(), message);
                    mDialog.dismiss();
                }
            }
        });
    }

    private void getAlbumByTag(String tag, int postionFlag) {
        programEntityList.clear();
        if("全部".equals(tag) || postionFlag == 0){
            onPullDownToRefresh();
            return;
        }
        mDialog.show();
        gameModel.getAlbumList(mId, tag, 9999, 1, CommConstants.DEFAULT_SORT_ONLINE_PERSON, new DefaultLiveHttpCallBack<AlbumBean>() {
            @Override
            public void success(AlbumBean albumBean) {
                if(isNotFinish()) {
                    mDialog.dismiss();
                    mRecyclerView.setLoadingMoreEnabled(false);
                    if (albumBean.taglist != null && albumBean.taglist.size() > 0)
                        programEntityList.add(albumBean.taglist);
                    if (albumBean.albumlist != null && albumBean.albumlist.size() > 0)
                        programEntityList.addAll(albumBean.albumlist);
                    mAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void failure(String message) {
                if(isNotFinish()) {
                    mDialog.dismiss();
                    mRecyclerView.setLoadingMoreEnabled(false);
                    ToastUtils.showToast(getActivity(), message);
                }
            }
        });
    }

    private void getProgramByTag(String tag,int postionFlag) {
        programEntityList.clear();
        if("全部".equals(tag) || postionFlag == 0){
            onPullDownToRefresh();
            return;
        }
        mDialog.show();
        gameModel.queryprogramlistbyareaid(mId, tag, 9999, 1, CommConstants.DEFAULT_SORT_ONLINE_PERSON, new DefaultLiveHttpCallBack<ProgramBean>() {
            @Override
            public void success(ProgramBean programBean) {
                if(isNotFinish()) {
                    mDialog.dismiss();
                    mRecyclerView.setLoadingMoreEnabled(false);
                    if (programBean.taglist != null && programBean.taglist.size() > 0)
                        programEntityList.add(programBean.taglist);
                    if (programBean.programs != null && programBean.programs.size() > 0)
                        programEntityList.addAll(programBean.programs);
                    mAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void failure(String message) {
                if(isNotFinish()) {
                    mDialog.dismiss();
                    mRecyclerView.refreshComplete();
                    ToastUtils.showToast(getActivity(), message);
                }
            }
        });
    }

    public void onPullUpToRefresh() {
        switch (mCategroy) {
            case "video":
                gameModel.queryprogramlistbyareaid(mId, "all", CommConstants.COMMON_PAGE_COUNT, mCurrentPage, CommConstants.DEFAULT_SORT_ONLINE_PERSON, new DefaultLiveHttpCallBack<ProgramBean>() {
                    @Override
                    public void success(ProgramBean programBean) {
                        if(isNotFinish()) {
                            mDialog.dismiss();
                            mRecyclerView.refreshComplete();
                            if(programBean == null)
                                return;
                            if (NumUtils.getPage(programBean.cnt) == mCurrentPage) {
                                mRecyclerView.setLoadingMoreEnabled(false);
                            } else if (NumUtils.getPage(programBean.cnt) > mCurrentPage) {
                                mRecyclerView.setLoadingMoreEnabled(true);
                                mCurrentPage++;
                            }
                            programEntityList.addAll(programBean.programs);
                            mAdapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void failure(String message) {
                        if(isNotFinish()) {
                            mRecyclerView.refreshComplete();
                            ToastUtils.showToast(getActivity(), message);
                            mDialog.dismiss();
                        }
                    }
                });
                break;
            case "album":
                gameModel.getAlbumList(mId, "all", CommConstants.COMMON_PAGE_COUNT, mCurrentPage, CommConstants.DEFAULT_SORT_ONLINE_PERSON, new DefaultLiveHttpCallBack<AlbumBean>() {
                    @Override
                    public void success(AlbumBean albumBean) {
                        if(isNotFinish()) {
                            mDialog.dismiss();
                            mRecyclerView.refreshComplete();
                            if(albumBean == null)
                                return;
                            if (NumUtils.getPage(albumBean.cnt) == mCurrentPage) {
                                mRecyclerView.setLoadingMoreEnabled(false);
                            } else if (NumUtils.getPage(albumBean.cnt) > mCurrentPage) {
                                mRecyclerView.setLoadingMoreEnabled(true);
                                mCurrentPage++;
                            }
                            if (albumBean.albumlist != null && albumBean.albumlist.size() > 0)
                                programEntityList.addAll(albumBean.albumlist);
                            mAdapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void failure(String message) {
                        if(isNotFinish()) {
                            mRecyclerView.refreshComplete();
                            ToastUtils.showToast(getActivity(), message);
                            mDialog.dismiss();
                        }
                    }
                });
                break;
            case "anchor":
                gameModel.getAnchors(mId, "all", CommConstants.COMMON_PAGE_COUNT, mCurrentPage, CommConstants.DEFAULT_SORT_ONLINE_PERSON, new DefaultLiveHttpCallBack<AuthorBean>() {
                    @Override
                    public void success(AuthorBean authorBean) {
                        if(isNotFinish()) {
                            mDialog.dismiss();
                            mRecyclerView.refreshComplete();
                            if(authorBean == null)
                                return;
                            if (NumUtils.getPage(authorBean.cnt) == mCurrentPage) {
                                mRecyclerView.setLoadingMoreEnabled(false);
                            } else if (NumUtils.getPage(authorBean.cnt) > mCurrentPage) {
                                mRecyclerView.setLoadingMoreEnabled(true);
                                mCurrentPage++;
                            }
                            if (authorBean.userinfolist != null && authorBean.userinfolist.size() > 0)
                                programEntityList.addAll(authorBean.userinfolist);
                            mAdapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void failure(String message) {
                        if(isNotFinish()) {
                            mRecyclerView.refreshComplete();
                            ToastUtils.showToast(getActivity(), message);
                            mDialog.dismiss();
                        }
                    }
                });
                break;
        }

    }
    class ProgramAdapterListener implements CategoryAdapter.AdapterListener{
        @Override
        public void changeData(String tag, int positionFlag) {
            mTagPosition = positionFlag;
            mAdapter.setBg(positionFlag);
            mDialog.show();
            getProgramByTag(tag,positionFlag);
        }
    }
    class AlbumAdapterListener implements CategoryAdapter.AdapterListener{
        @Override
        public void changeData(String tag, int positionFlag) {
            mTagPosition = positionFlag;
            mAdapter.setBg(positionFlag);
            mDialog.show();
            getAlbumByTag(tag,positionFlag);
        }
    }
    class AuthorAdapterListener implements CategoryAdapter.AdapterListener{
        @Override
        public void changeData(String tag, int positionFlag) {
            mTagPosition = positionFlag;
            mAdapter.setBg(positionFlag);
            mDialog.show();
            getAuthorByTag(tag,positionFlag);
        }
    }
    class ShowPopuWindow implements CategoryAdapter.ShowPopuWindowListener {
        @Override
        public void showPopuWindow(int height) {
            if(mPopuWindow == null){
                if(mProgramTags != null) {
                    mPopuWindow = new TagPopuWindow(getActivity(), mProgramTags,height);
                    mPopuWindow.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
                    mPopuWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
                    mPopuWindow.setFocusable(true);
                    mPopuWindow.setOutsideTouchable(true);
                    mPopuWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                }
            }
            mPopuWindow.setAnimationStyle(R.style.AnimationPreview);
            location = ((CategoryActivity2)getActivity()).getLocation();
            Log.e("ceshi","location[0]=" + location[0] + "       location[1]=" + location[1]);
            mPopuWindow.showAtLocation(mRootView, Gravity.NO_GRAVITY, location[0], location[1] + DensityUtil.dip2px(getActivity(),51));//+ DensityUtil.dip2px(getActivity(),getActivity().getResources().getDimension(R.dimen.header_height)
            switch (mCategroy) {
                case "video":
                    mPopuWindow.setAdapterListener(new ProgramAdapterListener());
                    mPopuWindow.setBg(mTagPosition);
                    break;
                case "album":
                    mPopuWindow.setAdapterListener(new AlbumAdapterListener());
                    mPopuWindow.setBg(mTagPosition);
                    break;
                case "anchor":
                    mPopuWindow.setAdapterListener(new AuthorAdapterListener());
                    mPopuWindow.setBg(mTagPosition);
                    break;
            }
        }
    }
    private LinearLayoutManager mLayoutMgr ;
    @Override
    protected void setScrollOnLayoutManager(int scrollY) {
        mLayoutMgr.scrollToPositionWithOffset(0, -scrollY);
//        setRecyclerViewOnScrollListener();
    }
}
