package com.lyq.mytimer.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.lyq.mytimer.R;
import com.lyq.mytimer.base.BaseActivity;
import com.lyq.mytimer.view.MusicAnimView;
import com.zhouwei.blurlibrary.EasyBlur;

public class MusicAnimActivity extends BaseActivity implements View.OnClickListener {

	private MusicAnimView musicAnimView;
	private ImageView img;
	private TextView tvPrevious, tvAction, tvNext;

	/** 是否是播放中 */
	private boolean isPlaying;

	private int[] resId;

	private int curIndex;

	public static void startAction(Context context) {
		Intent intent = new Intent(context, MusicAnimActivity.class);
		context.startActivity(intent);
		startActivityAnim((Activity) context);
	}

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_music_anim);

		musicAnimView = findViewById(R.id.music_anim_view);
		img = findViewById(R.id.img);

		Bitmap overlay = BitmapFactory.decodeResource(getResources(), R.drawable.bg_re);
		Bitmap finalBitmap = EasyBlur.with(this)
				.bitmap(overlay) //要模糊的图片
				.radius(24)//模糊半径
				.blur();
		img.setImageBitmap(finalBitmap);

		tvPrevious = findViewById(R.id.btn_previous);
		tvAction = findViewById(R.id.btn_action);
		tvNext = findViewById(R.id.btn_next);

		tvPrevious.setOnClickListener(this);
		tvAction.setOnClickListener(this);
		tvNext.setOnClickListener(this);
	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.btn_previous:
				int index = curIndex - 1 + resId.length;
				index = index % resId.length;
				musicAnimView.toPrevious(resId[index]);
				break;
			case R.id.btn_action:
				isPlaying = ! isPlaying;
				tvAction.setText(isPlaying ? "暂停" : "播放");
				musicAnimView.updateState(isPlaying);
				break;
			case R.id.btn_next:
				int next = curIndex + 1 + resId.length;
				next = next % resId.length;
				musicAnimView.toNext(resId[next]);
				break;
		}
	}
}
