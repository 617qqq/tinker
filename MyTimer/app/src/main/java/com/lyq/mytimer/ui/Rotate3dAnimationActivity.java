package com.lyq.mytimer.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;

import android.view.View;
import android.view.animation.Animation;
import android.widget.ImageView;

import com.lyq.mytimer.R;
import com.lyq.mytimer.base.BaseActivity;
import com.lyq.mytimer.view.Rotate3dAnimation;

public class Rotate3dAnimationActivity extends BaseActivity {

	private int[] imgs = new int[]{R.drawable.anim_3d_01, R.drawable.anim_3d_02};
	private int count;

	public static void start(Context context) {
		Intent starter = new Intent(context, Rotate3dAnimationActivity.class);
		context.startActivity(starter);
	}

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_rotate_3d);

		final ImageView imageView = findViewById(R.id.img);
		imageView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				anim(imageView);
			}
		});
	}

	private void anim(ImageView imageView) {
		startExitAnim(imageView);
	}

	private void startExitAnim(final ImageView imageView) {
		Rotate3dAnimation animation = new Rotate3dAnimation(
				0, 90, imageView.getWidth() / 2,
				imageView.getHeight(), 0, false
		);
		animation.setFillAfter(true);
		animation.setDuration(500);
		animation.setAnimationListener(new Animation.AnimationListener() {
			@Override
			public void onAnimationStart(Animation animation) {

			}

			@Override
			public void onAnimationEnd(Animation animation) {
				startAppearAnim(imageView);
			}

			@Override
			public void onAnimationRepeat(Animation animation) {

			}
		});
		imageView.startAnimation(animation);
	}

	private void startAppearAnim(ImageView imageView) {
		imageView.setImageResource(imgs[++ count % 2]);
		Rotate3dAnimation animation = new Rotate3dAnimation(
				90, 0, imageView.getWidth() / 2,
				imageView.getHeight(), 0, false
		);
		animation.setFillAfter(true);
		animation.setDuration(500);
		imageView.startAnimation(animation);
	}
}
