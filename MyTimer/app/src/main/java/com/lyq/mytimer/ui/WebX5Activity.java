package com.lyq.mytimer.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.lyq.mytimer.R;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;

public class WebX5Activity extends AppCompatActivity {

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_web_x5);

		WebView mWebView = findViewById(R.id.web_view);

		WebSettings settings = mWebView.getSettings();
		settings.setAllowFileAccess(true);  // 设置可以访问文件
		settings.setJavaScriptEnabled(true);//如果访问的页面中有Javascript，则webview必须设置支持Javascript
		settings.setUserAgentString(mWebView.getClass().getSimpleName());    // 用户字符串
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
		settings.setJavaScriptCanOpenWindowsAutomatically(true);
		settings.setDefaultTextEncodingName("UTF-8");
		mWebView.setVisibility(View.VISIBLE);

		mWebView.setWebChromeClient(new WebChromeClient());

		mWebView.loadUrl("http://baidu.com/");
	}
}
