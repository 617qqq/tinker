package com.lyq.mytimer.utils;

import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by pixel on 2017/3/6.
 * <p>
 * 提供与网络相关的工具
 */

public abstract class NetworkUtil {
    private static final String TAG = "NetworkUtil";

    public interface OnDownCallBack {
        void onProgress(long downSize, long fileSize, boolean complete);

        void onError(Exception e);
    }

    public interface OnRequestCallBack {
        void onString(String string);

        void onError(Exception e);
    }

    private static void runOnUiThread(Runnable runnable) {
        new Handler(Looper.getMainLooper()).post(runnable);
    }

    /**
     * 获取链接对象
     */
    public static HttpURLConnection getConnection(String downUrl, String method) throws Exception {
        URL url = new URL(downUrl);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod(method);
        conn.setRequestProperty("Accept", "*/*");
        conn.setRequestProperty("Accept-Language", "zh_CN");    // 语言
        conn.setRequestProperty("Charset", "UTF-8");                // 字符
        return conn;
    }

    public static void get(String url, OnRequestCallBack onRequestCallBack) {
        get(url, null, onRequestCallBack);
    }

    /**
     * 发送GET请求
     */
    public static void get(final String url, final String resultCharsetName, final OnRequestCallBack onRequestCallBack) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                InputStream inputStream = null;
                try {
                    inputStream = getConnection(url, "GET").getInputStream();
                    final ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    int i = -1;
                    while ((i = inputStream.read()) != -1) {
                        baos.write(i);
                    }
                    if (onRequestCallBack == null) return;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (resultCharsetName != null) {
                                try {
                                    onRequestCallBack.onString(baos.toString(resultCharsetName));
                                } catch (UnsupportedEncodingException e) {
                                    onRequestCallBack.onError(e);
                                }
                            } else {
                                onRequestCallBack.onString(baos.toString());
                            }
                        }
                    });
                } catch (final Exception e) {
                    e.printStackTrace();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            onRequestCallBack.onError(e);
                        }
                    });
                } finally {
                    try {
                        inputStream.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    /**
     * 下载文件
     *
     * @param downUrl        下载地址
     * @param savePath       保存地址
     * @param onDownCallBack 回调进度
     */
    public static void down(final String downUrl, final String savePath, final OnDownCallBack onDownCallBack) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                RandomAccessFile currentPart = null;
                InputStream inputStream = null;
                try {
                    long downSize = 0L;
                    currentPart = new RandomAccessFile(savePath, "rw");
                    final long fileSize = getConnection(downUrl, "GET").getContentLength();    // 获取大小是单次请求
                    HttpURLConnection connection = getConnection(downUrl, "GET");
                    inputStream = connection.getInputStream();
                    byte[] buffer = new byte[1024];
                    int hasRead = -1;
                    while ((hasRead = inputStream.read(buffer)) != -1) {
                        currentPart.write(buffer, 0, hasRead);
                        downSize += hasRead;
                        if (onDownCallBack != null) {
                            onDownCallBack.onProgress(downSize, fileSize, false);
                        }
                    }
                    if (onDownCallBack != null)
                        onDownCallBack.onProgress(downSize, fileSize, true); //
                } catch (Exception e) {
                    e.printStackTrace();
                    if (onDownCallBack != null)
                        onDownCallBack.onError(e);
                } finally {
                    try {
                        currentPart.close();
                        inputStream.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    /**
     * 同步Cookie到WebView（实现WebView不再需要验证）
     */
    public static void initWebView(WebView webView) {
        if (webView != null) {
            WebSettings settings = webView.getSettings();
            settings.setAllowFileAccess(true);  // 设置可以访问文件
            settings.setJavaScriptEnabled(true);//如果访问的页面中有Javascript，则webview必须设置支持Javascript
            settings.setUserAgentString(webView.getClass().getSimpleName());    // 用户字符串
            settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
            settings.setAppCacheEnabled(true);
            settings.setDomStorageEnabled(true);
            settings.setDatabaseEnabled(true);

            settings.setSupportZoom(true);
            settings.setBuiltInZoomControls(true);
            settings.setDisplayZoomControls(false);
            settings.setDefaultFontSize(12);
            settings.setLoadWithOverviewMode(true);
            settings.setUseWideViewPort(true);

            settings.setPluginState(WebSettings.PluginState.ON);
            // settings.setPluginsEnabled(true);//可以使用插件
            settings.setJavaScriptCanOpenWindowsAutomatically(true);
            settings.setDefaultTextEncodingName("UTF-8");
            webView.setVisibility(View.VISIBLE);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                CookieManager.getInstance().setAcceptThirdPartyCookies(webView, true);// 5.0要求开启第三方Cookie支持
            }

            webView.setWebChromeClient(new WebChromeClient());
            webView.setWebViewClient(new WebViewClient());
        }
    }

    /**
     * Url编码
     */
    public static String UrlEncoder(String string) {
        String result = "";
        try {
            result = URLEncoder.encode(string, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return result;
    }


}
