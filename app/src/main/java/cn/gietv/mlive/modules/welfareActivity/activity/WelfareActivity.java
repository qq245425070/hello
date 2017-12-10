package cn.gietv.mlive.modules.welfareActivity.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.DownloadListener;
import android.webkit.HttpAuthHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.TextView;

import cn.gietv.mlive.R;
import cn.gietv.mlive.base.AbsBaseActivity;

/**
 * 作者：houde on 2016/11/25 11:28
 * 邮箱：yangzhonghao@gietv.com
 */
public class WelfareActivity extends AbsBaseActivity{
    private WebView mWebView;
    private ITitleListener mTitleListener;
    private IPageLoadListener mPageLoadListener;
    private String mUrl = "http://api.mlive.gietv.cn:8080/liveservice/welfare_end.jsp";
//    private String mUrl = "http://192.168.200.189:8080/liveservice/welfare_end.jsp";
    private ImageButton exitButton;
    private TextView titleText;
    private ImageButton searchButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welfare);
        exitButton = (ImageButton) findViewById(cn.gietv.mlive.R.id.head_ib_exit);
        titleText = (TextView) findViewById(cn.gietv.mlive.R.id.head_tv_title);
        searchButton = (ImageButton) findViewById(cn.gietv.mlive.R.id.head_ib_search);
        searchButton.setVisibility(View.INVISIBLE);
        titleText.setText("福利社");
        setOnClickListener();
        initWebView();
        loadUrl();
    }

    private void setOnClickListener() {
        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if( mWebView.canGoBack()) {
                    mWebView.goBack();
                }else{
                    finish();
                }

            }
        });
    }

    private void initWebView() {
        mWebView = (WebView) findViewById(R.id.webView);
        mWebView.setWebViewClient(new CommonWebClient());
        mWebView.setWebChromeClient(new CommonWebChromeClient());
        mWebView.requestFocusFromTouch();
        WebSettings settings = mWebView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setDisplayZoomControls(false);
        settings.setSupportZoom(false);
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);
        mWebView.setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
                Uri uri = Uri.parse(url);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });
    }
    public void loadUrl() {
        mWebView.loadUrl(mUrl);
    }
    public void setPageLoadListener(IPageLoadListener pageLoadListener) {
        mPageLoadListener = pageLoadListener;
    }

    public void setTitleListener(ITitleListener titleListener) {
        mTitleListener = titleListener;
    }

    private class CommonWebClient extends WebViewClient {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            if (mPageLoadListener != null) {
                mPageLoadListener.onPageStarted(url);
            }
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            if (mPageLoadListener != null) {
                mPageLoadListener.onPageFinished(view.getTitle(), url);
            }
        }

        @Override
        public void onReceivedHttpAuthRequest(WebView view, HttpAuthHandler handler, String host, String realm) {
            super.onReceivedHttpAuthRequest(view, handler, host, realm);
        }

        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            super.onReceivedError(view, errorCode, description, failingUrl);
            if (mPageLoadListener != null) {
                mPageLoadListener.onPageLoadError(failingUrl, errorCode);
            }
        }
    }

    private class OnErrorClick implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            mWebView.loadUrl(mUrl);
        }
    }

    private class CommonWebChromeClient extends WebChromeClient {
        @Override
        public void onReceivedTitle(WebView view, String title) {
            super.onReceivedTitle(view, title);
            if (mTitleListener != null) {
                mTitleListener.setTitle(title);
            }
        }
    }
    public interface IPageLoadListener {
        void onPageStarted(String url);

        void onPageFinished(String title, String url);

        void onPageLoadError(String url, int errorCode);
    }
    public interface ITitleListener {
        void setTitle(String title);
    }

}
