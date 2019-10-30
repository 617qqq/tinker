package com.lyq.mytimer.ui;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.WindowManager;
import android.widget.TextView;

import com.lyq.mytimer.R;

import java.util.Locale;

public class DpiActivity extends AppCompatActivity {

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_dpi);

		WindowManager wm = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
		DisplayMetrics dm = new DisplayMetrics();
		wm.getDefaultDisplay().getMetrics(dm);
		TextView textView = findViewById(R.id.tv_dpi);
		textView.setText(String.format(Locale.CHINA, "densityDpi \t %d \n density \t %.2f", dm.densityDpi, dm.density));
	}
}
