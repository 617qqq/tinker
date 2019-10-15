package com.lyq.mytimer.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.lyq.mytimer.R;
import com.lyq.mytimer.base.BaseActivity;
import com.lyq.mytimer.view.ShadowDrawable;

public class ShadowActivity extends BaseActivity {

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_shadow);

		final TextView tv = findViewById(R.id.tv_shadow);
		final ShadowDrawable drawable = new ShadowDrawable(tv);
		drawable.setCorner(100,60,60,100);
		tv.setBackground(drawable);

		SeekBar seekBar = findViewById(R.id.seek_bar_radius);
		seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
				ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) tv.getLayoutParams();
				params.height = progress;
				tv.setMaxHeight(progress);
				tv.setLayoutParams(params);
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {

			}

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {

			}
		});
		SeekBar seekBarX = findViewById(R.id.seek_bar_x);
		seekBarX.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
				drawable.setX(seekBar.getProgress());
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {

			}

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {

			}
		});
		SeekBar seekBarY = findViewById(R.id.seek_bar_y);
		seekBarY.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
				drawable.setY(seekBar.getProgress());
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {

			}

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {

			}
		});
	}
}
