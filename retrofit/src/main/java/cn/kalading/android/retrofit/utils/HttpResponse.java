package cn.kalading.android.retrofit.utils;

import java.io.Serializable;

/**
 * author：steven
 * datetime：15/9/2 15:21
 */
public class HttpResponse<T> implements Serializable {
    public int status;
    public String msg;
    public T data;
}
