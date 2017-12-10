package cn.gietv.mlive.modules.usercenter.activity;

import cn.gietv.mlive.base.AbsBaseActivity;

public class PaymentActivity extends AbsBaseActivity {
//    private static int WEIXIN = 1;
//    private static int ZHIFUBAO = 2;
//    private String mUserId;
//    private int mCount;
//    private TextView mUserIdText,mOfferText,mAmountText,mResultText;
//    private Button mSubmit;
//    private ImageView mZhifuImage,mWeixinImage;
//    private String mGoodsID,mNickname;
//    private int payMethod;
//    private IWXAPI api;
//    private PayReq req = new PayReq();
//    private Map<String,String> resultunifiedorder;
//    private int mResult;
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_payment);
//        //HeadViewController.initHeadWithoutSearch(this, "订单支付");
//        MainApplication.getInstance().addActivityList(this);
//        mUserId = getIntent().getExtras().getString("userid");
//        mCount = getIntent().getExtras().getInt("count");
//        mNickname = getIntent().getExtras().getString("nickname");
//        api = WXAPIFactory.createWXAPI(this, null);
////        mMqttController = new MqttController(UrlConstants.Mqtt.URL_MQTT_HOST, VersionUtils.getDeviceId(this),"",new MyMqttHandler());
////        mMqttController.connect();
//        init();
//    }
//
//    private void init() {
//        mResultText = (TextView)findViewById(R.id.result_text);
//        mUserIdText = (TextView) findViewById(R.id.userid_text);
//        mOfferText = (TextView) findViewById(R.id.offer_text);
//        mAmountText = (TextView) findViewById(R.id.amount_text);
//        mSubmit = (Button) findViewById(R.id.submit);
//        mZhifuImage = (ImageView) findViewById(R.id.zifubao_img);
//        mWeixinImage = (ImageView) findViewById(R.id.weixin_img);
//        mSubmit.setOnClickListener(this);
//        mWeixinImage.setOnClickListener(this);
//        mZhifuImage.setOnClickListener(this);
//        mUserIdText.setText("充值账号：" + mNickname);
//        mOfferText.setText("支付订单：" + (mCount/100) + "金角");
//        mAmountText.setText("支付金额：" + (mCount / 100) + "元");
//        payMethod = WEIXIN;
//        mZhifuImage.setImageResource(R.mipmap.quanquan);
//        mWeixinImage.setImageResource(R.mipmap.allow_image);
//        ImageButton exitButton = (ImageButton)findViewById(cn.gietv.mvr.R.id.head_ib_exit);
//        TextView titleText = (TextView)findViewById(cn.gietv.mvr.R.id.head_tv_title);
//        titleText.setText("订单支付");
//        exitButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                onBackPressed();
//            }
//        });
//    }
//
//    @Override
//    public void onBackPressed() {
//        Intent intent = new Intent();
//        intent.putExtra("propname",getIntent().getStringExtra("propname"));
//        intent.putExtra("count",mCount);
//        intent.putExtra("propid",getIntent().getStringExtra("propid"));
//        setResult(mResult, intent);
//        MainApplication.getInstance().destroyActivity();
//        finish();
////        if(mMqttController != null){
////            mMqttController.destory();
////        }
//        super.onBackPressed();
//    }
//
//    @Override
//    public void onClick(View v) {
//        if (v.getId()== R.id.submit) {
//            payment();
//        }else if(v.getId() == R.id.weixin_img) {
//            payMethod = WEIXIN;
//        }else if(v.getId() == R.id.zifubao_img){
//                payMethod = ZHIFUBAO;
//        }
//    }
//    private String mOrderID;
//    private String mWXPayNotifyURL;
//    private void payment() {
//        OrderModel model = RetrofitUtils.create(OrderModel.class);
//        model.createOrder(mUserId, mNickname, mUserId, mNickname, "jinjiao", mCount / 100, new DefaultLiveHttpCallBack<OrderBean>() {
//            @Override
//            public void success(OrderBean s) {
//                mOrderID = s.orderid;
//                mWXPayNotifyURL = s.wxnotifyurl;
//                System.out.println(mWXPayNotifyURL);
//                callThirdPay(s.orderid);
//            }
//
//            @Override
//            public void failure(String message) {
//                ToastUtils.showToast(PaymentActivity.this, message);
//            }
//        });
//
//    }
////    private MqttController mMqttController;
//    private void callThirdPay(String orderId) {
////        try {
////            mMqttController.getMqttClient().subscribe(ConfigUtils.TOPIC_ORDER+orderId);
////        } catch (MqttException e) {
////            e.printStackTrace();
////        }
//        if(payMethod == WEIXIN){
//            //生成订单
//            GetPrepayIdTask getPrepayIdTask = new GetPrepayIdTask();
//            getPrepayIdTask.execute();
//        }
//        else if(payMethod == ZHIFUBAO){
//            ToastUtils.showToast(this,"暂不支持");
//        }
//    }
//
//    public void messageArrivedPay(String message) {
//        try {
//            JSONObject jsonObject = new JSONObject(message);
//            int status = jsonObject.getInt("status");
//            if(status == 0){
//                JSONObject jsonObject2 = jsonObject.getJSONObject("data");
//                UserCenterBean.UserinfoEntity userinfo = CacheUtils.getCacheUserInfo();
//                userinfo.myjinjiao = jsonObject2.getInt("myjinjiao");
//                CacheUtils.saveUserInfo(userinfo);
//                ToastUtils.showToast(this, "充值成功");
//                mResultText.setText("充值成功");
//                mResult = 88;
//            }else{
//                ToastUtils.showToast(this, jsonObject.getString("msg"));
//                mResultText.setText("充值失败");
//                mResult = 44;
//            }
//            mSubmit.setBackgroundResource(R.drawable.commen_button_theme_color2);
//            mSubmit.setClickable(false);
//            mResultText.setVisibility(View.VISIBLE);
//            //取消订阅
////            try {
////                mMqttController.getMqttClient().unsubscribe(ConfigUtils.TOPIC_ORDER+mOrderID);
////                mMqttController.destory();
////            } catch (MqttException e) {
////                e.printStackTrace();
////            }
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//
//    }
//
//    private class GetPrepayIdTask extends AsyncTask<Void, Void, Map<String,String>> {
//
//        private ProgressDialog dialog;
//
//        @Override
//        protected void onPreExecute() {
//            dialog = ProgressDialog.show(PaymentActivity.this, "提示", "正在获取预支付订单...");
//        }
//
//        @Override
//        protected void onPostExecute(Map<String,String> result) {
//            if (dialog != null) {
//                dialog.dismiss();
//            }
//            resultunifiedorder = result;
//            Log.e("msg",result.toString());
//            sendPayReq();
//        }
//
//        @Override
//        protected void onCancelled() {
//            super.onCancelled();
//        }
//
//        @Override
//        protected Map<String,String>  doInBackground(Void... params) {
//
//            String url = String.format("https://api.mch.weixin.qq.com/pay/unifiedorder");
//            String entity = genProductArgs();
//            byte[] buf = Util.httpPost(url, entity);
//            String content = new String(buf);
//            Map<String,String> xml=decodeXml(content);
//            return xml;
//        }
//    }
//    public Map<String,String> decodeXml(String content) {
//
//        try {
//            Map<String, String> xml = new HashMap<String, String>();
//            XmlPullParser parser = Xml.newPullParser();
//            parser.setInput(new StringReader(content));
//            int event = parser.getEventType();
//            while (event != XmlPullParser.END_DOCUMENT) {
//
//                String nodeName=parser.getName();
//                switch (event) {
//                    case XmlPullParser.START_DOCUMENT:
//                        break;
//                    case XmlPullParser.START_TAG:
//                        if("xml".equals(nodeName)==false){
//                            //实例化student对象
//                            xml.put(nodeName,parser.nextText());
//                        }
//                        break;
//                    case XmlPullParser.END_TAG:
//                        break;
//                }
//                event = parser.next();
//            }
//            return xml;
//        } catch (Exception e) {
//            Log.e("orion",e.toString());
//        }
//        return null;
//
//    }
//    private String toXml(List<NameValuePair> params) {
//        StringBuilder sb = new StringBuilder();
//        sb.append("<xml>");
//        for (int i = 0; i < params.size(); i++) {
//            sb.append("<"+params.get(i).getName()+">");
//
//
//            sb.append(params.get(i).getValue());
//            sb.append("</"+params.get(i).getName()+">");
//        }
//        sb.append("</xml>");
//        return sb.toString();
//    }
//    private String genNonceStr() {
//        Random random = new Random();
//        return Util.MD5(String.valueOf(random.nextInt(10000)).getBytes());
//    }
//
//    private String genProductArgs() {
//        StringBuffer xml = new StringBuffer();
//        try {
//            String	nonceStr = genNonceStr();
//            xml.append("</xml>");
//            List<NameValuePair> packageParams = new LinkedList<NameValuePair>();
//            packageParams.add(new BasicNameValuePair("appid", ConfigUtils.WEIXIN_APPID));
//            packageParams.add(new BasicNameValuePair("body", "独角"));
//            packageParams.add(new BasicNameValuePair("mch_id", ConfigUtils.MCH_ID));
//            packageParams.add(new BasicNameValuePair("nonce_str", nonceStr));
//            packageParams.add(new BasicNameValuePair("notify_url", mWXPayNotifyURL));
//            packageParams.add(new BasicNameValuePair("out_trade_no",mOrderID));
//            packageParams.add(new BasicNameValuePair("spbill_create_ip","127.0.0.1"));
//            packageParams.add(new BasicNameValuePair("total_fee", mCount+""));//mCount
//            packageParams.add(new BasicNameValuePair("trade_type", "APP"));
//            String sign = genPackageSign(packageParams);
//            packageParams.add(new BasicNameValuePair("sign", sign));
//            String xmlstring =toXml(packageParams);
//            return new String(xmlstring.getBytes(),"ISO8859-1");
//        } catch (Exception e) {
//            e.printStackTrace();
//            return null;
//        }
//    }
//    private String genPackageSign(List<NameValuePair> params) {
//        StringBuilder sb = new StringBuilder();
//        for (int i = 0; i < params.size(); i++) {
//            sb.append(params.get(i).getName());
//            sb.append('=');
//            sb.append(params.get(i).getValue());
//            sb.append('&');
//        }
//        sb.append("key=");
//        sb.append(ConfigUtils.WEIXIN_API_KEY);
//        String packageSign = Util.MD5(sb.toString().getBytes()).toUpperCase();
//        return packageSign;
//    }
//
//    private void sendPayReq() {
//        //api.registerApp(ConfigUtils.WEIXIN_APPID);
//        genPayReq();
//        api.sendReq(req);
//    }
//    private void genPayReq() {
//        req.appId = ConfigUtils.WEIXIN_APPID;
//        req.partnerId = ConfigUtils.MCH_ID;
//        req.prepayId = resultunifiedorder.get("prepay_id");
//        req.packageValue = "Sign=WXPay";
//        req.nonceStr = genNonceStr();
//        req.timeStamp = String.valueOf(genTimeStamp());
//        List<NameValuePair> signParams = new LinkedList<>();
//        signParams.add(new BasicNameValuePair("appid", req.appId));
//        signParams.add(new BasicNameValuePair("noncestr", req.nonceStr));
//        signParams.add(new BasicNameValuePair("package", req.packageValue));
//        signParams.add(new BasicNameValuePair("partnerid", req.partnerId));
//        signParams.add(new BasicNameValuePair("prepayid", req.prepayId));
//        signParams.add(new BasicNameValuePair("timestamp", req.timeStamp));
//        req.sign = genAppSign(signParams);
//    }
//    private long genTimeStamp() {
//        return System.currentTimeMillis() / 1000;
//    }
//    private String genAppSign(List<NameValuePair> params) {
//        StringBuilder sb = new StringBuilder();
//
//        for (int i = 0; i < params.size(); i++) {
//            sb.append(params.get(i).getName());
//            sb.append('=');
//            sb.append(params.get(i).getValue());
//            sb.append('&');
//        }
//        sb.append("key=");
//        sb.append(ConfigUtils.WEIXIN_API_KEY);
//        String appSign = Util.MD5(sb.toString().getBytes()).toUpperCase();
//        return appSign;
//    }

//class MyMqttHandler extends MqttHandler {
//        @Override
//        public void connectionLost(Throwable t) {
//
//        }
//
//        @Override
//        public void connectSuccess() {
//            System.out.println("连接成功......");
//        }
//
//        @Override
//        public void deliveryComplete(IMqttDeliveryToken token) {
//
//        }
//
//        @Override
//        public void messageArrivedPay(String message) throws Exception {
//            PaymentActivity.this.messageArrivedPay(message);
//        }
//
//        @Override
//        public void messageArrivedBlackList(String message) throws Exception {
//
//        }
//
//        @Override
//        public void messageArrivedNotice(String message) throws Exception {
//
//        }
//
//        @Override
//        public void messageArrivedSystem(String message) throws Exception {
//
//        }
//    }
}
