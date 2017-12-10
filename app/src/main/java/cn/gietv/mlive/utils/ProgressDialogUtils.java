package cn.gietv.mlive.utils;

import android.app.ProgressDialog;
import android.content.Context;

/**
 * author：steven
 * datetime：15/10/9 14:33
 *
 */
public class ProgressDialogUtils {
    public static ProgressDialog createShowDialog(Context context, String text) {
        return ProgressDialog.show(context, "", text);
    }
}
