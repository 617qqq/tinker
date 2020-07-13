package com.lyq.mytimer.ui;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.LottieAnimationView;
import com.lyq.mytimer.R;

public class LottieActivity extends AppCompatActivity {

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_lottie);

		LottieAnimationView lottieAnimationView = findViewById(R.id.lottieAnimationView);

		lottieAnimationView.addAnimatorUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
			@Override
			public void onAnimationUpdate(ValueAnimator valueAnimator) {
				Toast.makeText(LottieActivity.this, "onAnimationUpdate", Toast.LENGTH_SHORT).show();
			}
		});

		lottieAnimationView.addAnimatorListener(new Animator.AnimatorListener() {
			@Override
			public void onAnimationStart(Animator animator) {
				Toast.makeText(LottieActivity.this, "onAnimationStart", Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onAnimationEnd(Animator animator) {
				Toast.makeText(LottieActivity.this, "onAnimationEnd", Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onAnimationCancel(Animator animator) {
				Toast.makeText(LottieActivity.this, "onAnimationCancel", Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onAnimationRepeat(Animator animator) {
				Toast.makeText(LottieActivity.this, "onAnimationRepeat", Toast.LENGTH_SHORT).show();
			}
		});

//		lottieAnimationView.pauseAnimation();
//		lottieAnimationView.cancelAnimation();
//		lottieAnimationView.playAnimation();
	}
}
