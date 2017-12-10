package cn.gietv.scenelib5;

import android.util.Log;

import com.google.unity.GoogleUnityActivity;

import com.unity3d.player.UnityPlayer;

/**
 * Created by Guiyuan on 2016/5/31.
 */
public class SceneActivity extends GoogleUnityActivity{
//    public void onCreate(Bundle savedInstance)
//    {
//        super.onCreate(savedInstance);
//        Intent intent = new Intent();
//        intent.setClass(getApplicationContext(), GoogleUnityActivity.class);
//        startActivity(intent);
//        finish();
//    }

    public void quitUnity()
    {
        UnityPlayer.currentActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Log.e("Unity", "quit called start");

                mUnityPlayer.quit();

                Log.e("Unity", "quit called end");
                UnityPlayer.currentActivity.finish();
            }
        });

    }
}
