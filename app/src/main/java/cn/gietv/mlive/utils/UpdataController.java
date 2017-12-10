package cn.gietv.mlive.utils;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import cn.gietv.mlive.R;
import cn.gietv.mlive.http.DefaultLiveHttpCallBack;
import cn.gietv.mlive.modules.update.bean.UpdateBean;
import cn.gietv.mlive.modules.update.model.UpdateModel;
import cn.kalading.android.retrofit.utils.RetrofitUtils;

import static cn.gietv.mlive.R.id.update_tv_cancel;

/**
 * author：steven
 * datetime：15/11/12 20:26
 * email：liuyingwen@kalading.com
 */
public class UpdataController {
    private Context mContext;
    private Dialog mDialog;
    private UpdateModel mUpdateModel;
    private int mVersionCode;

    public UpdataController(Context context) {
        mContext = context;
        mUpdateModel = RetrofitUtils.create(UpdateModel.class);
        mVersionCode = VersionUtils.getSoftVersionCode(context);
    }

    public void checkUpdate() {
        mUpdateModel.checkUpdate(new DefaultLiveHttpCallBack<UpdateBean>() {
            @Override
            public void success(UpdateBean updateBean) {
                if (updateBean.versionnum > mVersionCode) {
                    showUpdateDialog(updateBean);
                } else {
                    ToastUtils.showToast(mContext, "当前已是最新版本");
                }
            }

            @Override
            public void failure(String message) {
                ToastUtils.showToast(mContext, message);
            }
        });
    }
    public void indexCheckUpdate() {
        mUpdateModel.checkUpdate(new DefaultLiveHttpCallBack<UpdateBean>() {
            @Override
            public void success(UpdateBean updateBean) {
                if (updateBean.versionnum > mVersionCode) {
                    showUpdateDialog(updateBean);
                }
            }

            @Override
            public void failure(String message) {
            }
        });
    }

    private void showUpdateDialog(final UpdateBean updateBean) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.update_dialog_layout, null);
        TextView title = (TextView) view.findViewById(R.id.update_tv_title);
        TextView content = (TextView) view.findViewById(R.id.update_tv_content);
        TextView sure = (TextView) view.findViewById(R.id.update_tv_sure);
        TextView cancel = (TextView) view.findViewById(update_tv_cancel);
        String versionName="";
        try {
            // 获取软件版本号，
            versionName = mContext.getPackageManager().getPackageInfo(
                    "cn.gietv.mlive", 0).versionName;
        } catch (Exception e) {
            e.printStackTrace();
        }
        title.setText("发现新版本" + updateBean.versioncode+"，当前版本"+versionName);
        content.setText(updateBean.desc.replace("||", "\n"));


        sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                downloadApk(updateBean.apkname, updateBean.download);
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                destory();
            }
        });
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setView(view);
        mDialog = builder.create();
        mDialog.show();
    }

    private void downloadApk(String name, String path) {
        DownloadController mController = new DownloadController(mContext);
        mController.startDownload(name, path);
        destory();
    }

    public void destory() {
        if (mDialog != null && mDialog.isShowing()) {
            mDialog.dismiss();
        }
    }
}
