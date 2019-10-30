package com.lyq.mytimer.base;

import android.app.Activity;
import androidx.appcompat.app.AppCompatActivity;

import com.lyq.mytimer.R;

public class BaseActivity extends AppCompatActivity {

	public static void startActivityAnim(Activity activity) {
		activity.overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
	}

	@Override
	public void finish() {
		super.finish();
		overridePendingTransition(R.anim.slide_left_in, R.anim.slide_right_out);
	}
}
