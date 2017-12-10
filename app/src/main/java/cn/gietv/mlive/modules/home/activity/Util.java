package cn.gietv.mlive.modules.home.activity;

/**
 * Created by houde on 2016/4/21.
 */
public class Util {
//
//    public static void launch(final Activity activity){
//        SceneActivity sceneActivity = (SceneActivity)activity;
//        long current = System.currentTimeMillis();
//        sceneActivity.quitUnity();
//        System.out.println(System.currentTimeMillis() - current);
//    }
//    public static void startVideoPlayer(Activity activity , String path , int model){
//        if(model == 0){
//            Intent intent = new Intent(activity, VRVideoPlayActivity.class);
//            Bundle bundle = new Bundle();
//            bundle.putString("path",path);
//            bundle.putInt("model", 1);
//            bundle.putInt("file_model",0);
//            bundle.putInt("come_type",0);
//            intent.putExtras(bundle);
//            activity.startActivity(intent);
//        }else{
//            Intent intent = new Intent(activity, VideoPlayerActivity.class);
//            Bundle bundle = new Bundle();
//            bundle.putString("path", path);
//            bundle.putInt("model", 1);
//            bundle.putInt("come_type",1);
//            bundle.putLong("extra_position", 0);
//            intent.putExtras(bundle);
//            activity.startActivity(intent);
//        }
//    }
//    public static void getLocalVideo(Activity activity){
//        VideoProvider videoProvider = new VideoProvider(activity);
//        List<Video> videos = videoProvider.getList();
//        UnityPlayer.UnitySendMessage("LocalVideoPanel", "localVideoListResponse", GsonUtils.getGson().toJson(videos));
//        new LocalVideoActivity.LoadImagesFromSDCard(videos,1,activity).execute();
//    }
//    public static void hasInstalled(Activity activity,String name){
//        UnityPlayer.UnitySendMessage("GameDownloadButton", "isDowndloaded", String.valueOf(PackageUtils.hasInstalled(activity, name)));
//    }
//    public static void downloadGame(Activity activity,String gameName,String url){
//        DownloadController controller = new DownloadController(activity);
//        controller.startDownload(gameName, url);
//    }
//    public static void sendHttpHead(){
//        UnityPlayer.UnitySendMessage("Skybox","setHttpHeader",GsonUtils.getGson().toJson(RetrofitUtils.mHeaderMap));
//    }
}
