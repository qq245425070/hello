package cn.gietv.mlive.modules.category.fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import java.util.ArrayList;
import java.util.List;

import cn.gietv.mlive.R;
import cn.gietv.mlive.base.AbsBaseFragment;
import cn.gietv.mlive.constants.CommConstants;
import cn.gietv.mlive.http.DefaultLiveHttpCallBack;
import cn.gietv.mlive.modules.category.activity.CategoryActivity;
import cn.gietv.mlive.modules.category.adapter.CategoryAdapter;
import cn.gietv.mlive.modules.game.bean.AlbumBean;
import cn.gietv.mlive.modules.game.bean.AuthorBean;
import cn.gietv.mlive.modules.game.model.GameModel;
import cn.gietv.mlive.modules.program.bean.ProgramBean;
import cn.gietv.mlive.utils.NumUtils;
import cn.gietv.mlive.utils.ToastUtils;
import cn.kalading.android.retrofit.utils.RetrofitUtils;

/**
 * Created by houde on 2016/5/27.
 */
public class CategoryFragment extends AbsBaseFragment {
    private View mRootView;
    private XRecyclerView mListView;
    private GameModel gameModel;
    private List<Object> programEntityList;
    private CategoryAdapter mAdapter;
    private String mId;
    private int mCurrentPage;
    private List<Object> mAllData = new ArrayList<>();
    private int mType;//记录标签属于哪个Type  2  7  8
//    private int mPosition; //记录标签选中的位置
//    private int[] location;
    private ProgressDialog mDialog;
    public static CategoryFragment getInstance(String id,int type,String category ){
        CategoryFragment categoryFragment = new CategoryFragment();
        Bundle bundle = new Bundle();
        bundle.putString("id",id);
        bundle.putInt("type", type);
        categoryFragment.setArguments(bundle);
        return categoryFragment;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_category2, container,false);
        mListView = (XRecyclerView) mRootView.findViewById(R.id.recyclerview);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mListView.setLayoutManager(layoutManager);
        mListView.setRefreshProgressStyle(ProgressStyle.SysProgress);
        mListView.setLaodingMoreProgressStyle(ProgressStyle.SysProgress);
        mListView.setArrowImageView(R.drawable.iconfont_downgrey);
        mListView.setLoadingMoreEnabled(false);
        mDialog = new ProgressDialog(getActivity());
        mDialog.setMessage("正在请求数据，请等候");
        gameModel = RetrofitUtils.create(GameModel.class);
        mId = getArguments().getString("id");
        mType = getArguments().getInt("type");
        programEntityList = new ArrayList<>();
        mAdapter = new CategoryAdapter(getActivity(), programEntityList,0);
        mListView.setAdapter(mAdapter);
        onPullDownToRefresh();
        mListView.setLoadingListener(new XRecyclerView.LoadingListener() {
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
        switch (mType){
            case 2:
                gameModel.queryProgramTypeListByTag(mId, 2, CommConstants.COMMON_PAGE_COUNT, mCurrentPage, CommConstants.DEFAULT_SORT_ONLINE_PERSON, new DefaultLiveHttpCallBack<ProgramBean>() {
                    @Override
                    public void success(ProgramBean programBean) {
                        if(isNotFinish()) {
                            mDialog.dismiss();
                            mListView.refreshComplete();
                            if(programBean == null){
                                return;
                            }
                            ((CategoryActivity)getActivity()).setIsFollow(programBean.isfollow);
                            if (NumUtils.getPage(programBean.cnt) == mCurrentPage) {
                                mListView.setLoadingMoreEnabled(false);
                            } else if (NumUtils.getPage(programBean.cnt) > mCurrentPage) {
                                mListView.setLoadingMoreEnabled(true);
                                mCurrentPage++;
                            }
                            programEntityList.clear();
                            if (programBean.taglist != null && programBean.taglist.size() > 0)
                                programEntityList.add(programBean.taglist);
                            if (programBean.programs != null && programBean.programs.size() > 0)
                                programEntityList.addAll(programBean.programs);
                            mAdapter = new CategoryAdapter(getActivity(), programEntityList, 0);
                            mListView.setAdapter(mAdapter);
                            mAllData.addAll(programEntityList);
                            mAdapter.setAdapterListener(new CategoryAdapter.AdapterListener() {
                                @Override
                                public void changeData(String tag, int postionFlag) {
                                }
                            });
                        }
                    }

                    @Override
                    public void failure(String message) {
                        if(isNotFinish()) {
                            mListView.refreshComplete();
                            ToastUtils.showToast(getActivity(), message);
                            mDialog.dismiss();
                        }
                    }
                });
                break;
            case 7:
                gameModel.queryAlbumTypeListByTag(mId, 7, CommConstants.COMMON_PAGE_COUNT, mCurrentPage, CommConstants.DEFAULT_SORT_ONLINE_PERSON, new DefaultLiveHttpCallBack<AlbumBean>() {
                    @Override
                    public void success(AlbumBean albumBean) {
                        if (isNotFinish()) {
                            mDialog.dismiss();
                            mListView.refreshComplete();
                            if(albumBean == null){
                                return;
                            }
                            if (NumUtils.getPage(albumBean.cnt) == mCurrentPage) {
                                mListView.setLoadingMoreEnabled(false);
                            } else if (NumUtils.getPage(albumBean.cnt) > mCurrentPage) {
                                mListView.setLoadingMoreEnabled(true);
                                mCurrentPage++;
                            }
                            programEntityList.clear();
                            if (albumBean.taglist != null && albumBean.taglist.size() > 0)
                                programEntityList.add(albumBean.taglist);
                            if (albumBean.albumlist != null && albumBean.albumlist.size() > 0)
                                programEntityList.addAll(albumBean.albumlist);
                            mAdapter = new CategoryAdapter(getActivity(), programEntityList, 0);
                            mListView.setAdapter(mAdapter);
                            mAllData.addAll(programEntityList);
                            mAdapter.setAdapterListener(new CategoryAdapter.AdapterListener() {
                                @Override
                                public void changeData(String tag, int postionFlag) {
                                }
                            });
                        }
                    }

                    @Override
                    public void failure(String message) {
                        if (isNotFinish()) {
                            mListView.refreshComplete();
                            ToastUtils.showToast(getActivity(), message);
                            mDialog.dismiss();
                        }
                    }
                });
                break;
            case 8:
                gameModel.queryAuthorTypeListByTag(mId, 8, CommConstants.COMMON_PAGE_COUNT, mCurrentPage, CommConstants.DEFAULT_SORT_ONLINE_PERSON, new DefaultLiveHttpCallBack<AuthorBean>() {
                    @Override
                    public void success(AuthorBean authorBean) {
                        if (isNotFinish()) {
                            mDialog.dismiss();
                            mListView.refreshComplete();
                            if(authorBean == null){
                                return;
                            }
                            if (NumUtils.getPage(authorBean.cnt) == mCurrentPage) {
                                mListView.setLoadingMoreEnabled(false);
                            } else if (NumUtils.getPage(authorBean.cnt) > mCurrentPage) {
                                mListView.setLoadingMoreEnabled(true);
                                mCurrentPage++;
                            }
                            programEntityList.clear();
                            if (authorBean.taglist != null && authorBean.taglist.size() > 0)
                                programEntityList.add(authorBean.taglist);
                            if (authorBean.authorlist != null && authorBean.authorlist.size() > 0)
                                programEntityList.addAll(authorBean.authorlist);
                            mAdapter = new CategoryAdapter(getActivity(), programEntityList, 0);
                            mListView.setAdapter(mAdapter);
                            mAllData.addAll(programEntityList);
                            mAdapter.setAdapterListener(new CategoryAdapter.AdapterListener() {
                                @Override
                                public void changeData(String tag, int postionFlag) {
                                }
                            });
                        }
                    }

                    @Override
                    public void failure(String message) {
                        if(isNotFinish()) {
                            mListView.refreshComplete();
                            ToastUtils.showToast(getActivity(), message);
                            mDialog.dismiss();
                        }
                    }
                });
                break;
        }
    }
    public void onPullUpToRefresh() {
        switch (mType){
            case 2:
                gameModel.queryProgramTypeListByTag(mId, 2, CommConstants.COMMON_PAGE_COUNT, mCurrentPage, CommConstants.DEFAULT_SORT_ONLINE_PERSON, new DefaultLiveHttpCallBack<ProgramBean>() {
                    @Override
                    public void success(ProgramBean programBean) {
                        if(isNotFinish()) {
                            mDialog.dismiss();
                            mListView.refreshComplete();
                            if(programBean== null)
                                return;
                            if (NumUtils.getPage(programBean.cnt) == mCurrentPage) {
                                mListView.setLoadingMoreEnabled(false);
                            } else if (NumUtils.getPage(programBean.cnt) > mCurrentPage) {
                                mListView.setLoadingMoreEnabled(true);
                                mCurrentPage++;
                            }
                            programEntityList.addAll(programBean.programs);
                            mAdapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void failure(String message) {
                        if(isNotFinish()) {
                            mListView.refreshComplete();
                            ToastUtils.showToast(getActivity(), message);
                            mDialog.dismiss();
                        }
                    }
                });
                break;
            case 7:
                gameModel.queryAlbumTypeListByTag(mId, 7, CommConstants.COMMON_PAGE_COUNT, mCurrentPage, CommConstants.DEFAULT_SORT_ONLINE_PERSON, new DefaultLiveHttpCallBack<AlbumBean>() {
                    @Override
                    public void success(AlbumBean albumBean) {
                        if (isNotFinish()) {
                            mDialog.dismiss();
                            mListView.refreshComplete();
                            if(albumBean == null)
                                return;
                            if (NumUtils.getPage(albumBean.cnt) == mCurrentPage) {
                                mListView.setLoadingMoreEnabled(false);
                            } else if (NumUtils.getPage(albumBean.cnt) > mCurrentPage) {
                                mListView.setLoadingMoreEnabled(true);
                                mCurrentPage++;
                            }
                            if (albumBean.albumlist != null && albumBean.albumlist.size() > 0)
                                programEntityList.addAll(albumBean.albumlist);
                            mAdapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void failure(String message) {
                        if (isNotFinish()) {
                            mListView.refreshComplete();
                            ToastUtils.showToast(getActivity(), message);
                            mDialog.dismiss();
                        }
                    }
                });
                break;
            case 8:
                gameModel.queryAuthorTypeListByTag(mId, 8, CommConstants.COMMON_PAGE_COUNT, mCurrentPage, CommConstants.DEFAULT_SORT_ONLINE_PERSON, new DefaultLiveHttpCallBack<AuthorBean>() {
                    @Override
                    public void success(AuthorBean authorBean) {
                        if (isNotFinish()) {
                            mListView.refreshComplete();
                            mDialog.dismiss();
                            if(authorBean == null)
                                return;
                            if (NumUtils.getPage(authorBean.cnt) == mCurrentPage) {
                                mListView.setLoadingMoreEnabled(false);
                            } else if (NumUtils.getPage(authorBean.cnt) > mCurrentPage) {
                                mListView.setLoadingMoreEnabled(true);
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
                            mListView.refreshComplete();
                            ToastUtils.showToast(getActivity(), message);
                            mDialog.dismiss();
                        }
                    }
                });
                break;
        }

    }
}
