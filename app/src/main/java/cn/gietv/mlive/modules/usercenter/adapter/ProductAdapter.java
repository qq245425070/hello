package cn.gietv.mlive.modules.usercenter.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import cn.gietv.mlive.R;
import cn.gietv.mlive.base.AbsArrayAdapter;
import cn.gietv.mlive.modules.usercenter.bean.ProductBeanList;

/**
 * Created by houde on 2016/1/29.
 */
public class ProductAdapter extends AbsArrayAdapter<ProductBeanList.ProductBean> {
    private List<ProductBeanList.ProductBean> productBeans;
    private Context mContext;
    public ProductAdapter(Context context, List<ProductBeanList.ProductBean> objects) {
        super(context, objects);
        this.productBeans = objects;
        this.mContext = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rootView = LayoutInflater.from(mContext).inflate(R.layout.adapter_product,null);
        TextView textView = (TextView) rootView.findViewById(R.id.product_name);
        textView.setText(productBeans.get(position).name);
        return rootView;
    }
}
