package com.lyq.mytimer.ipc.content_provider;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.TextView;

import com.lyq.mytimer.base.BaseActivity;

public class ProviderActivity extends BaseActivity {

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(new TextView(this));
		final Uri uri = Uri.parse("content://com.lyq.mytimer.ipc.book_provider");
		getContentResolver().query(uri, null, null, null, null);
		getContentResolver().query(uri, null, null, null, null);
		getContentResolver().query(uri, null, null, null, null);
	}
}
