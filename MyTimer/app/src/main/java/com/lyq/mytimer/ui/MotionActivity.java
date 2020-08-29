package com.lyq.mytimer.ui;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.motion.widget.MotionLayout;

import com.blankj.utilcode.util.ScreenUtils;
import com.lyq.mytimer.R;

public class MotionActivity extends AppCompatActivity {

	boolean isEnd;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_motion);

		final MotionLayout motionLayout = findViewById(R.id.motionLayout);
		motionLayout.getConstraintSet(R.id.end).getConstraint(R.id.bg).layout.mHeight = ScreenUtils.getScreenHeight();
	}
}
