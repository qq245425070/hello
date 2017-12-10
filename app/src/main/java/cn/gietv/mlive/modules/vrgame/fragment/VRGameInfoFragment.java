package cn.gietv.mlive.modules.vrgame.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import cn.gietv.mlive.R;
import cn.gietv.mlive.base.AbsBaseFragment;
import cn.gietv.mlive.modules.author.adapter.AlbumAdapter;
import cn.gietv.mlive.modules.game.bean.GameInfoBean;
import cn.gietv.mlive.utils.ImageLoaderUtils;

/**
 * Created by houde on 2016/5/19.
 */
public class VRGameInfoFragment extends AbsBaseFragment{
    public static String EXTRA_TITLE = "extra_title";
    public static String EXTRA_GAME_ID = "extra_game_id";
    private View mCurrentView;
    private ListView mListView;
    private View mHeadView;
    private RecyclerView mRecyclerView;
    private TextView mGameDesc,mGameType,mGameSize,mGameVersion;
    private GameInfoBean.GameInfoEntity mGame;
    private List<String> mImageSources;
    private ImageLoader mImageLoader;
    public static VRGameInfoFragment getInstence(GameInfoBean.GameInfoEntity game) {
        VRGameInfoFragment fragment = new VRGameInfoFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("game",game);
        fragment.setArguments(bundle);
        return fragment;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mCurrentView = inflater.inflate(R.layout.game_info_layout, container, false);
        mGame = (GameInfoBean.GameInfoEntity) getArguments().getSerializable("game");
        mImageLoader = ImageLoaderUtils.getDefaultImageLoader(getActivity());
        if(mGame != null)
            initView();
        mImageSources = mGame.screenshot_imgs;
        return mCurrentView;
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void initView() {
        mListView = (ListView) mCurrentView.findViewById(R.id.listView);
        mHeadView = LayoutInflater.from(getActivity()).inflate(R.layout.vr_game_info_head,null);
        mRecyclerView = (RecyclerView) mHeadView.findViewById(R.id.recycler_view);
        mGameDesc = (TextView) mHeadView.findViewById(R.id.game_desc);
        mGameType = (TextView) mHeadView.findViewById(R.id.game_type);
        mGameSize = (TextView) mHeadView.findViewById(R.id.game_size);
        mGameVersion = (TextView) mHeadView.findViewById(R.id.game_version);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setAdapter(new RecyclerAdapter());
        mListView.addHeaderView(mHeadView);
        mListView.setAdapter(new AlbumAdapter(getActivity(), new ArrayList<GameInfoBean.GameInfoEntity>()));
        if(mGame.screenshot_imgs == null || mGame.screenshot_imgs.size() == 0){
            mRecyclerView.setVisibility(View.GONE);
        }
        mGameDesc.setText(mGame.desc);
        mGameType.setText("游戏类型  " + mGame.gametypename);
        mGameSize.setText("游戏大小  " + mGame.size);
//        mGameVersion.setText("游戏版本  " + mGame.version);
    }
    class RecyclerAdapter extends RecyclerView.Adapter<ImageHolder>{

        @Override
        public ImageHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            return new ImageHolder(LayoutInflater.from(getActivity()).inflate(R.layout.layout_vrinfo_head_recycler,null));
        }

        @Override
        public void onBindViewHolder(ImageHolder imageHolder, int i) {
            mImageLoader.displayImage(mImageSources.get(i),imageHolder.image);
        }

        @Override
        public int getItemCount() {
            return mImageSources.size();
        }
    }
    class ImageHolder extends RecyclerView.ViewHolder{
        private ImageView image;
        public ImageHolder(View itemView) {
            super(itemView);
            image = (ImageView) itemView.findViewById(R.id.image);
        }
    }


//    mReceiver = new BootReceiver();
//getActivity().unregisterReceiver(mReceiver);
//    IntentFilter filter = new IntentFilter();
//    filter.addAction("android.intent.action.PACKAGE_ADDED");
//    filter.addDataScheme("package");
//    getActivity().registerReceiver(mReceiver, filter);

//    class BootReceiver extends BroadcastReceiver {
//
//        @Override
//        public void onReceive(Context context, Intent intent){
//            //接收安装广播
//            if (intent.getAction().equals("android.intent.action.PACKAGE_ADDED")) {
//                String packageName = intent.getDataString();
//                System.out.println("安装了:" + packageName + "包名的程序");
//                if(mAdapter != null){
//                    mAdapter.notifyDataSetChanged();
//                }
//            }
//
//        }
//    }
}
