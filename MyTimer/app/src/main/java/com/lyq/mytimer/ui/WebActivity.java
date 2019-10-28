package com.lyq.mytimer.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;

import com.lyq.mytimer.R;
import com.lyq.mytimer.utils.NetworkUtil;

public class WebActivity extends AppCompatActivity {

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_web);

		WebView webView = findViewById(R.id.web_view);
		NetworkUtil.initWebView(webView);
		webView.loadUrl("http://baidu.com/");
	}
}
