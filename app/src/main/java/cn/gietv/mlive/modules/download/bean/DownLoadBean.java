package cn.gietv.mlive.modules.download.bean;

/**
 * Created by houde on 2016/3/3.
 */
public class DownLoadBean {
    private String title;
    private String url;
    private boolean check;
    private String imagePath;
    private String anchor;
    private String videoId;

    public void setVideoId(String id){
        this.videoId = id;
    }
    public String getVideoId(){
        return videoId;
    }
    public String getAnchor() {
        return anchor;
    }

    public void setAnchor(String anchor) {
        this.anchor = anchor;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public boolean isCheck() {
        return check;
    }

    public void setCheck(boolean check) {
        this.check = check;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
