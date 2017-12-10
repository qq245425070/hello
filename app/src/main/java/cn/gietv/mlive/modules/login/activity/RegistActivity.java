package cn.gietv.mlive.modules.login.activity;

import cn.gietv.mlive.base.AbsBaseActivity;

/**
 * author：steven
 * datetime：15/10/9 18:30
 */
public class RegistActivity extends AbsBaseActivity {

//    private MaterialEditText mUsernameEdit, mPasswordEdit, mPasswordAgainEdit, mEmailEdit, mNickNameEdit;
//    private TextView mRegistButton;
//
//    private void initViews() {
//        HeadViewController.initHeadWithoutSearch(this, "注册");
//        mUsernameEdit = (MaterialEditText) findViewById(cn.gietv.mvr.R.id.regist_et_username);
//        mPasswordEdit = (MaterialEditText) findViewById(cn.gietv.mvr.R.id.regist_et_password);
//        mNickNameEdit = (MaterialEditText) findViewById(cn.gietv.mvr.R.id.regist_et_nickname);
//        mPasswordAgainEdit = (MaterialEditText) findViewById(cn.gietv.mvr.R.id.regist_et_password_again);
//        mEmailEdit = (MaterialEditText) findViewById(cn.gietv.mvr.R.id.regist_et_email);
//        mRegistButton = (TextView) findViewById(cn.gietv.mvr.R.id.regist_btn_regist);
//        mRegistButton.setOnClickListener(this);
//        mNickNameEdit.addValidator(new METValidator("昵称不能为空") {
//            @Override
//            public boolean isValid(@NonNull CharSequence text, boolean isEmpty) {
//                return !isEmpty;
//            }
//        });
//        mNickNameEdit.setOnEditorActionListener(new TextView.OnEditorActionListener() {
//            @Override
//            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
//                return (keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER);
//            }
//        });
//        mUsernameEdit.addValidator(new METValidator("用户名不能为空") {
//            @Override
//            public boolean isValid(@NonNull CharSequence text, boolean isEmpty) {
//                return !isEmpty;
//            }
//        }).addValidator(new METValidator("用户名长度应大于3个字符") {
//            @Override
//            public boolean isValid(@NonNull CharSequence text, boolean isEmpty) {
//                return text.length() >= 4;
//            }
//        });
//        mUsernameEdit.setMinCharacters(4);
//        mPasswordEdit.addValidator(new METValidator("密码不能为空") {
//            @Override
//            public boolean isValid(@NonNull CharSequence text, boolean isEmpty) {
//                return !isEmpty;
//            }
//        }).addValidator(new METValidator("密码长度应大于3个字符") {
//            @Override
//            public boolean isValid(@NonNull CharSequence text, boolean isEmpty) {
//                return text.length() >= 4;
//            }
//        });
//        mPasswordEdit.setMinCharacters(4);
//        mPasswordAgainEdit.addValidator(new METValidator("两次密码不一致") {
//            @Override
//            public boolean isValid(@NonNull CharSequence text, boolean isEmpty) {
//                return text.toString().equals(mPasswordEdit.getText().toString());
//            }
//        });
//        mPasswordAgainEdit.setMinCharacters(4);
//        mEmailEdit.addValidator(new METValidator("邮箱地址不能为空") {
//            @Override
//            public boolean isValid(@NonNull CharSequence text, boolean isEmpty) {
//                return !isEmpty;
//            }
//        }).addValidator(new METValidator("邮箱格式不正确") {
//            @Override
//            public boolean isValid(@NonNull CharSequence text, boolean isEmpty) {
//                Pattern pattern = Pattern.compile("^\\s*\\w+(?:\\.{0,1}[\\w-]+)*@[a-zA-Z0-9]+(?:[-.][a-zA-Z0-9]+)*\\.[a-zA-Z]+\\s*$");
//                return pattern.matcher(text).matches();
//            }
//        });
//    }
//
//    private void regist() {
//        if (mUsernameEdit.validate() && mNickNameEdit.validate() && mPasswordEdit.validate() && mPasswordAgainEdit.validate() && mEmailEdit.validate()) {
//            //清除缓存中的Userinfo信息
//            //CacheUtils.getCache().remove(CacheConstants.CACHE_USER_INFO);
//            RetrofitUtils.addHeader(HttpConstants.HEAD_TOKEN, HttpConstants.TOKEN_NONE);
//            RetrofitUtils.addHeader(HttpConstants.HEAD_USER_ID, HttpConstants.USER_ID_NONE);
//            RegistModel model = RetrofitUtils.create(RegistModel.class);
//            String userid = mUsernameEdit.getText().toString();
//            String password = mPasswordEdit.getText().toString();
//            String email = mEmailEdit.getText().toString();
//            String nickname = mNickNameEdit.getText().toString().replace("\\\\", "\\");
//            model.regist(nickname, userid, password, email, new DefaultLiveHttpCallBack<UserInfo>() {
//                @Override
//                public void success(UserInfo userInfo) {
//                    if (isNotFinish()) {
//                        CacheUtils.saveUserInfo(userInfo.userinfo);
//                        CacheUtils.getCache().put(CacheConstants.CACHE_TOKEN, userInfo.token);
//                        CacheUtils.getCache().put(CacheConstants.CACHE_USERID, userInfo.userinfo._id);
//                        RetrofitUtils.addHeader(HttpConstants.HEAD_TOKEN, userInfo.token);
//                        RetrofitUtils.addHeader(HttpConstants.HEAD_USER_ID, userInfo.userinfo._id);
//                        ToastUtils.showToast(RegistActivity.this, "注册成功");
//                        setResult(RESULT_OK);
//                        XmppUtils.getInstance().reconnection(RegistActivity.this);
//                        /*MqttService mqttService = (MqttService) MainApplication.getInstance().getObjcet();
//                        if(mqttService != null) {
//                            MqttController mqttController = mqttService.getMqttController();
//                            if(mqttController != null) {
//                                MqttClient client = mqttController.getMqttClient();
//                                if(client != null) {
//                                    try {
//                                        client.subscribe(ConfigUtils.PRIVATE_CHAT + CacheUtils.getCacheUserInfo()._id, 1);
//                                    } catch (MqttException e) {
//                                        e.printStackTrace();
//                                    }
//                                }
//                            }
//                        }*/
//                        finish();
//                    }
//                }
//
//                @Override
//                public void failure(String message) {
//                    if (isNotFinish()) {
//                        ToastUtils.showToast(RegistActivity.this, message);
//                    }
//                }
//            });
//        }
//    }
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(cn.gietv.mvr.R.layout.regist_layout);
//        initViews();
//    }
//
//    @Override
//    public void onClick(View view) {
//        switch (view.getId()) {
//            case cn.gietv.mvr.R.id.regist_btn_regist:
//                regist();
//                break;
//        }
//    }
}
