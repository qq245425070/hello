package cn.gietv.mlive.modules.download;

/**
 * author：steven
 * datetime：15/9/26 17:23
 *
 */
public interface IDownloadProxy {
    void startDownload(String url, String filePath);

    void downloading(int progress, int maxProgress);

    void downloadFinish();

    void downloadFailed(String message);
}
