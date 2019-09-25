package com.lyq.mytimer;

import android.os.Bundle;

import com.lyq.mytimer.base.BaseActivity;
import com.lyq.mytimer.resume.MyResumeActivity;
import com.lyq.mytimer.ui.AttrTableViewActivity;
import com.lyq.mytimer.ui.DebutActivity;
import com.lyq.mytimer.ui.MusicAnimActivity;
import com.lyq.mytimer.view.MusicAnimView;
import com.lyq.mytimer.view.MyTimeView;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;

public class MainActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		MyTimeView view = findViewById(R.id.time);
		view.setOnKeyConfirm(new MyTimeView.OnKeyConfirm() {
			@Override
			public void on(boolean isConfirm) {
				if (isConfirm) {
					MyResumeActivity.startAction(MainActivity.this);
				}
			}
		});

		Observable.timer(1, TimeUnit.SECONDS)
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(new Consumer<Long>() {
					@Override
					public void accept(Long aLong) throws Exception {
						//AttrTableViewActivity.startAction(MainActivity.this);
//						MusicAnimActivity.startAction(MainActivity.this);
						DebutActivity.start(MainActivity.this);
					}
				}).isDisposed();
	}
}
