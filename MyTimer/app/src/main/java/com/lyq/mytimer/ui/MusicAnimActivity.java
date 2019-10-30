package com.lyq.mytimer.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.lyq.mytimer.R;
import com.lyq.mytimer.base.BaseActivity;
import com.lyq.mytimer.view.MusicAnimView;
import com.zhouwei.blurlibrary.EasyBlur;

public class MusicAnimActivity extends BaseActivity implements View.OnClickListener {

	private MusicAnimView musicAnimView;
	private ImageView img1, img2;
	private TextView tvPrevious, tvAction, tvNext;

	/** 是否是播放中 */
	private boolean isPlaying;
	private ValueAnimator animator;

	private int[] resId = {R.drawable.rect_music, R.drawable.rect_re, R.drawable.rect_shelter};

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
		img1 = findViewById(R.id.img1);
		img2 = findViewById(R.id.img2);

		Bitmap overlay = BitmapFactory.decodeResource(getResources(), resId[0]);
		Bitmap finalBitmap = EasyBlur.with(this)
				.bitmap(overlay) //要模糊的图片
				.radius(25)//模糊半径
				.blur();
		img1.setImageBitmap(finalBitmap);
		img2.setImageBitmap(finalBitmap);

		tvPrevious = findViewById(R.id.btn_previous);
		tvAction = findViewById(R.id.btn_action);
		tvNext = findViewById(R.id.btn_next);

		tvPrevious.setOnClickListener(this);
		tvAction.setOnClickListener(this);
		tvNext.setOnClickListener(this);

		initAnimator();
	}

	private void initAnimator() {
		animator = ValueAnimator.ofFloat(0f, 1f).setDuration(500);
		animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
			@Override
			public void onAnimationUpdate(ValueAnimator animation) {
				float value = (float) animation.getAnimatedValue();
				img1.setAlpha(value);
				img2.setAlpha(1 - value);
			}
		});
		animator.addListener(new AnimatorListenerAdapter() {
			@Override
			public void onAnimationEnd(Animator animation) {
				super.onAnimationEnd(animation);
				img2.setAlpha(1f);
			}
		});
	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.btn_previous:
				changeCurrentIndex(false);
				musicAnimView.toPrevious(resId[curIndex]);
				break;
			case R.id.btn_action:
				isPlaying = ! isPlaying;
				tvAction.setText(isPlaying ? "暂停" : "播放");
				musicAnimView.updateState(isPlaying);
				break;
			case R.id.btn_next:
				changeCurrentIndex(true);
				musicAnimView.toNext(resId[curIndex]);
				break;
		}
	}

	public void changeCurrentIndex(boolean isNext) {
		curIndex = (curIndex + (isNext ? 1 : - 1) + resId.length) % resId.length;
		Bitmap overlay = BitmapFactory.decodeResource(getResources(), resId[curIndex]);
		Bitmap finalBitmap = EasyBlur.with(this)
				.bitmap(overlay) //要模糊的图片
				.radius(24)//模糊半径
				.blur();
		img1.setImageBitmap(finalBitmap);
		img1.setAlpha(0f);
		animator.start();
	}
}
