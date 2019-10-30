package com.lyq.mytimer.ui;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
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
