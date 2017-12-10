package cn.gietv.mlive.utils;

import android.content.Context;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

/**
 * author：steven
 * datetime：15/10/29 14:25
 * email：liuyingwen@kalading.com
 */
public class InputUtils {
    //强制隐藏软键盘
    public static void closeInputKeyBoard(EditText editText) {
        Context context = editText.getContext();
        InputMethodManager imm = ((InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE));
//        imm.hideSoftInputFromWindow(editText.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
    }
    //强制显示软键盘
    public static void showInputKeyBoard(EditText editText){
        Context context = editText.getContext();
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(editText,InputMethodManager.RESULT_SHOWN);
    }
}
