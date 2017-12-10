package cn.gietv.mlive.modules.photo.model;

import cn.gietv.mlive.http.DefaultLiveHttpCallBack;
import cn.gietv.mlive.modules.photo.bean.PhotoBean;

import retrofit.http.GET;
import retrofit.http.Multipart;
import retrofit.http.POST;
import retrofit.http.Part;
import retrofit.http.Query;
import retrofit.mime.TypedFile;

/**
 * author：steven
 * datetime：15/10/14 22:52
 *
 */
public interface PhotoModel {
    @GET("/user/queryphotobyuserid.action")
    void getUserPhoto(@Query("userinfoid") String userId, DefaultLiveHttpCallBack<PhotoBean> callBack);

    @GET("/upload/delphoto.action")
    void deletePhoto(@Query("url") String urls, DefaultLiveHttpCallBack<String> callBack);

    @Multipart
    @POST("/upload/photo.action")
    void addPhoto(@Part("upfile") TypedFile filePath, DefaultLiveHttpCallBack<String> callback);
}
