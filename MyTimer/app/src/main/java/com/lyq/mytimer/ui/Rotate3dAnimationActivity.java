package com.lyq.mytimer.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;

import com.lyq.mytimer.R;
import com.lyq.mytimer.base.BaseActivity;
import com.lyq.mytimer.view.Rotate3dAnimation;

public class Rotate3dAnimationActivity extends BaseActivity {

	public static void start(Context context) {
		Intent starter = new Intent(context, Rotate3dAnimationActivity.class);
		context.startActivity(starter);
	}

	float x = 180;

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
		x += 10;
		Rotate3dAnimation animation = new Rotate3dAnimation(
				0, x, imageView.getWidth() / 2,
				imageView.getHeight() / 2, 0, true
		);
		animation.setDuration(10000);
		imageView.startAnimation(animation);
	}
}
