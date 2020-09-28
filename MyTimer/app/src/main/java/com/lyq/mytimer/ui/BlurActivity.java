package com.lyq.mytimer.ui;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.blankj.utilcode.util.BarUtils;
import com.lyq.mytimer.R;
import com.lyq.mytimer.utils.BlurUtils;

public class BlurActivity extends AppCompatActivity {

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		BarUtils.setStatusBarVisibility(this, false);
		setContentView(R.layout.ac_blur);


		findViewById(R.id.img).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				getWindow().getDecorView().buildDrawingCache();
				BlurUtils.INSTANCE.setBitmap(getWindow().getDecorView().getDrawingCache());
				new BlurDialogFg().show(getSupportFragmentManager(), "RenderDialogFg");
			}
		});
	}
}
