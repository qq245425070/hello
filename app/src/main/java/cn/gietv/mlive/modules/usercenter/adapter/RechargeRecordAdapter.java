package cn.gietv.mlive.modules.usercenter.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import cn.gietv.mlive.R;
import cn.gietv.mlive.modules.usercenter.bean.RechargeRecordBean;
import cn.gietv.mlive.utils.TimeUtil;

/**
 * Created by houde on 2016/3/24.
 */
public class RechargeRecordAdapter extends RecyclerView.Adapter<RechargeRecordAdapter.ViewHolder> {

    private Context mContext;
    private List<RechargeRecordBean.RecordBean> mData;
    public RechargeRecordAdapter(Context context, List<RechargeRecordBean.RecordBean> objects) {
        mContext = context;
        mData = objects;
    }

    public void getView(int position, ViewHolder viewHolder) {
        RechargeRecordBean.RecordBean bean = getItem(position);
        viewHolder.dateText.setText(TimeUtil.getYearAndMonth(bean.paytime));
        viewHolder.descText.setText(bean.desc);
        viewHolder.titleText.setText(bean.title);
        viewHolder.timeText.setText(TimeUtil.getHourAndMonth(bean.paytime));
        viewHolder.numText.setText(bean.num);
    }

    private RechargeRecordBean.RecordBean getItem(int position) {
        return mData.get(position);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.recharge_record_item,null));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        getView(position,holder);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView titleText,descText,timeText,numText,dateText;

       public ViewHolder(View convertView) {
           super(convertView);
           dateText = (TextView) convertView.findViewById(R.id.recharge_date);
           timeText = (TextView) convertView.findViewById(R.id.recharge_pay_time);
           numText = (TextView) convertView.findViewById(R.id.recharge_num);
           titleText = (TextView) convertView.findViewById(R.id.recharge_title);
           descText = (TextView) convertView.findViewById(R.id.recharge_desc);
       }
   }
}
