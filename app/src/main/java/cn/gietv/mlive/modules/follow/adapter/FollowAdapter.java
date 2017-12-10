package cn.gietv.mlive.modules.follow.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by houde on 2016/8/26.
 */
public class FollowAdapter extends RecyclerView.Adapter<FollowAdapter.FollowViewHolder> {
    @Override
    public FollowViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(FollowViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class FollowViewHolder extends RecyclerView.ViewHolder{

        public FollowViewHolder(View itemView) {
            super(itemView);
        }
    }
}
