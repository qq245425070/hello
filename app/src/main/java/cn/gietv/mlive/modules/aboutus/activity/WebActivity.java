package cn.gietv.mlive.modules.aboutus.activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.HttpAuthHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;


import java.io.IOException;
import java.net.HttpURLConnection;

import cn.gietv.mlive.R;
import cn.gietv.mlive.base.AbsBaseActivity;
import cn.gietv.mlive.utils.HeadViewController;
import cn.gietv.mlive.utils.VersionUtils;

/**
 * author：steven
 * datetime：15/11/15 21:44
 * email：liuyingwen@kalading.com
 */
public class WebActivity extends AbsBaseActivity {
    private WebView mWebView;
    private TextView mAppNameText,mVersionCodeText;

    private ITitleListener mTitleListener;
    private IPageLoadListener mPageLoadListener;

    private String mUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.common_web_fragment);
        HeadViewController.initHeadWithoutSearch(this, "关于我们");
        mVersionCodeText = (TextView)findViewById(R.id.version_code);
        mAppNameText = (TextView) findViewById(R.id.app_name);
        mAppNameText.setText("游戏茶餐厅");
        mVersionCodeText.setText("V " + VersionUtils.getSoftVersionName(this));

        mAppNameText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        mAppNameText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
            }
        });
        /*initWebView();
        mEmptyLayout = new EmptyLayout(this, mWebView);
        mEmptyLayout.showLoading();
        AboutusModel aboutusModel = RetrofitUtils.create(AboutusModel.class);
        aboutusModel.getAboutUs(new DefaultLiveHttpCallBack<String>() {
            @Override
            public void success(String s) {
                mUrl = s;
                loadUrl();
                mEmptyLayout.clear();
            }

            @Override
            public void failure(String message) {
                mEmptyLayout.setErrorMessage(message);
                mEmptyLayout.showError();
            }
        });*/
    }

    /**
     * 初始化webview
     */
    private void initWebView() {
        mWebView = (WebView) findViewById(R.id.common_webview);
        mWebView.setWebViewClient(new CommonWebClient());
        mWebView.setWebChromeClient(new CommonWebChromeClient());
        mWebView.requestFocusFromTouch();
        WebSettings settings = mWebView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setDisplayZoomControls(false);
        settings.setSupportZoom(false);
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);
    }

    /**
     * 加载url
     */
    public void loadUrl() {
        mWebView.loadUrl(mUrl);
    }

    public void setPageLoadListener(IPageLoadListener pageLoadListener) {
        mPageLoadListener = pageLoadListener;
    }

    public void setTitleListener(ITitleListener titleListener) {
        mTitleListener = titleListener;
    }

    /**
     * webClient
     */
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

    public interface ITitleListener {
        void setTitle(String title);
    }

    public interface IPageLoadListener {
        void onPageStarted(String url);

        void onPageFinished(String title, String url);

        void onPageLoadError(String url, int errorCode);
    }
}
