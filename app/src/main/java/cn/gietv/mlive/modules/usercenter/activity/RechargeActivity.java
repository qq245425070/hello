package cn.gietv.mlive.modules.usercenter.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import cn.gietv.mlive.MainApplication;
import cn.gietv.mlive.R;
import cn.gietv.mlive.base.AbsBaseActivity;
import cn.gietv.mlive.http.DefaultLiveHttpCallBack;
import cn.gietv.mlive.modules.login.activity.LoginActivity;
import cn.gietv.mlive.modules.usercenter.adapter.ProductAdapter;
import cn.gietv.mlive.modules.usercenter.bean.ProductBeanList;
import cn.gietv.mlive.modules.usercenter.model.UserCenterModel;
import cn.gietv.mlive.utils.CacheUtils;
import cn.gietv.mlive.utils.HeadViewController;
import cn.gietv.mlive.utils.IntentUtils;
import cn.gietv.mlive.utils.ToastUtils;
import cn.gietv.mlive.utils.UserUtils;
import cn.kalading.android.retrofit.utils.RetrofitUtils;

public class RechargeActivity extends AbsBaseActivity implements View.OnClickListener, AdapterView.OnItemClickListener {

    private TextView mResult,mUserIdText;
    private LinearLayout mParent;
    private View mView;
    private GridView mGridView ;
    private ProductAdapter mAdapter;
    private Button submit;
    private String mGoodsId;
    private int count;
    private List<ProductBeanList.ProductBean> productBeans;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recharge);
        HeadViewController.initHeadWithoutSearch(this, "金角充值");
        MainApplication.getInstance().addActivityList(this);
        mGridView = (GridView)findViewById(R.id.product_parents);
        UserCenterModel medol = RetrofitUtils.create(UserCenterModel.class);
        medol.queryChargeList(new DefaultLiveHttpCallBack<ProductBeanList>() {
            @Override
            public void success(ProductBeanList productBeanList) {
                productBeans = productBeanList.recharges;
                mAdapter = new ProductAdapter(RechargeActivity.this,productBeanList.recharges);
                mGridView.setAdapter(mAdapter);
            }

            @Override
            public void failure(String message) {

            }
        });
        mGridView.setOnItemClickListener(this);
        init();
    }

    private void init() {
        mParent = (LinearLayout) findViewById(R.id.recharge_parent);
        mUserIdText = (TextView) findViewById(R.id.userid_text);
        mResult = (TextView) findViewById(R.id.result);
        submit = (Button) findViewById(R.id.submit);
        submit.setOnClickListener(this);
        mUserIdText.setText("充值账号：" + CacheUtils.getCacheUserInfo().nickname);
    }

    @Override
    public void onClick(View v) {
                if(UserUtils.isNotLogin()){
                    IntentUtils.openActivity(this, LoginActivity.class);
                }
                if(count == 0){
                    ToastUtils.showToast(this,"请选择要充值的金角数量");
                    return;
                }
                Bundle bundle = new Bundle();
                bundle.putInt("count",count);
                bundle.putString("nickname",CacheUtils.getCacheUserInfo().nickname);
                bundle.putString("userid", CacheUtils.getCacheUserInfo()._id);
                IntentUtils.openActivity(this, PaymentActivity.class, bundle);
                finish();
    }

    private void setBackground(View text,int count,String goodsid) {
        text.setBackgroundResource(R.drawable.commen_button_light_green_selected2);
        if(mView != null) {
            mView.setBackgroundResource(R.drawable.commen_button_theme_color);
        }
        mView = text;
        this.count = count;
        this.mGoodsId = goodsid;
        mResult.setText("售价：" + (count/100) + "元（1金角 = 1元）");
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if(productBeans != null){
            setBackground(view,productBeans.get(position).price,productBeans.get(position)._id);
        }
    }
}
