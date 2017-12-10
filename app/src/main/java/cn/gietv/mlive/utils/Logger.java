package cn.gietv.mlive.utils;

import android.util.Log;

/**
 * @author hakan eryargi (r a f t)
 */
public class Logger {

	private static final String LOG_TAG = "Arox-Cardboard-Demo";
	
	// should not be instantiated
	private Logger() {}
	
	public static void v(String msg) {
		Log.v(LOG_TAG, msg);
	}
	
	public static void d(String msg) {
		Log.d(LOG_TAG, msg);
	}
	
	public static void i(String msg) {
		Log.i(LOG_TAG, msg);
	}
	
	public static void w(String msg) {
		Log.w(LOG_TAG, msg);
	}

	public static void w(Throwable tr) {
		Log.w(LOG_TAG, tr);
	}
	
	public static void w(String msg, Throwable tr) {
		Log.w(LOG_TAG, msg, tr);
	}
	
	public static void e(String msg) {
		Log.e(LOG_TAG, msg);
	}

	public static void e(String msg, Throwable tr) {
		Log.e(LOG_TAG, msg, tr);
	}
	
}
