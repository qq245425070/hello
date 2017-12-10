package cn.gietv.mlive.modules.login.activity;

import cn.gietv.mlive.base.AbsBaseActivity;

/**
 * author：steven
 * datetime：15/10/21 10:10
 */
public class UpdatePasswordActivity extends AbsBaseActivity  {
//    public static final String EXTRA_NEED_OLD_PWD = "extra_need_old_pwd";
//    private EditText mOldPwdEdit, mNewPwdEdit, mNewPwdAgainEdit;
//    private TextView mSureButton;
//    private UserInfoModel mModel;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(cn.gietv.mvr.R.layout.update_password_layout);
//        HeadViewController.initHeadWithoutSearch(this, "修改密码");
//        mOldPwdEdit = (EditText) findViewById(cn.gietv.mvr.R.id.update_pwd_et_old_password);
//        mNewPwdEdit = (EditText) findViewById(cn.gietv.mvr.R.id.update_pwd_et_new_password);
//        mNewPwdAgainEdit = (EditText) findViewById(cn.gietv.mvr.R.id.update_pwd_et_new_password_again);
//        mOldPwdEdit.addValidator(new METValidator("原密码不能为空") {
//            @Override
//            public boolean isValid(@NonNull CharSequence text, boolean isEmpty) {
//                return !isEmpty;
//            }
//        });
//        mNewPwdEdit.addValidator(new METValidator("密码不能为空") {
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
//        mNewPwdEdit.setMinCharacters(4);
//        mNewPwdAgainEdit.addValidator(new METValidator("两次密码不一致") {
//            @Override
//            public boolean isValid(@NonNull CharSequence text, boolean isEmpty) {
//                return text.toString().equals(mNewPwdEdit.getText().toString());
//            }
//        });
//        mNewPwdAgainEdit.setMinCharacters(4);
//        mSureButton = (TextView) findViewById(cn.gietv.mvr.R.id.update_pwd_et_btn_update);
//        mModel = RetrofitUtils.create(UserInfoModel.class);
//        mSureButton.setOnClickListener(this);
//    }
//
//    private void updatePassword() {
//        if (mOldPwdEdit.validate() && mNewPwdEdit.validate() && mNewPwdAgainEdit.validate()) {
//            String oldPwd = mOldPwdEdit.getText().toString();
//            String newPwd = mNewPwdEdit.getText().toString();
//            String userid = CacheUtils.getCache().getAsString(CacheConstants.CACHE_USERID);
//            final ProgressDialog dialog = ProgressDialogUtils.createShowDialog(this, "正在修改密码，请稍后");
//            mModel.updatePassword(userid, oldPwd, newPwd, new DefaultLiveHttpCallBack<UserCenterBean.UserinfoEntity>() {
//                @Override
//                public void success(UserCenterBean.UserinfoEntity userinfoEntity) {
//                    if (isNotFinish()) {
//                        dialog.dismiss();
//                        ToastUtils.showToast(UpdatePasswordActivity.this, "密码修改成功！请重新登录");
//                        CacheUtils.getCache().put(CacheConstants.CACHE_USERID, HttpConstants.USER_ID_NONE);
//                        CacheUtils.getCache().put(CacheConstants.CACHE_TOKEN, HttpConstants.TOKEN_NONE);
//                        setResult(RESULT_OK);
//                        finish();
//                    }
//                }
//
//                @Override
//                public void failure(String message) {
//                    if (isNotFinish()) {
//                        dialog.dismiss();
//                        ToastUtils.showToast(UpdatePasswordActivity.this, message);
//                    }
//                }
//            });
//        }
//    }
//
//    @Override
//    public void onClick(View view) {
//        updatePassword();
//    }
}
