package com.lyq.mytimer.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import com.lyq.mytimer.R;
import com.lyq.mytimer.base.BaseActivity;
import com.lyq.mytimer.view.MusicAnimView;
import com.zhouwei.blurlibrary.EasyBlur;

public class MusicAnimActivity extends BaseActivity {

	private ConstraintLayout root;
	private MusicAnimView musicAnimView;
	private ImageView img;

	public static void startAction(Context context) {
		Intent intent = new Intent(context, MusicAnimActivity.class);
		context.startActivity(intent);
		startActivityAnim((Activity) context);
	}

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
//		View decorView = getWindow().getDecorView();
//		decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN | View.GONE);

		setContentView(R.layout.activity_music_anim);


		root = findViewById(R.id.root_activity_music_anim);
		musicAnimView = findViewById(R.id.music_anim_view);
		img = findViewById(R.id.img);

		Bitmap overlay = BitmapFactory.decodeResource(getResources(), R.drawable.bg_re);
		Bitmap finalBitmap = EasyBlur.with(this)
				.bitmap(overlay) //要模糊的图片
				.radius(24)//模糊半径
				.blur();
		img.setImageBitmap(finalBitmap);

		root.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Bitmap overlay = BitmapFactory.decodeResource(getResources(), R.drawable.rect_33);
				Bitmap finalBitmap = EasyBlur.with(MusicAnimActivity.this)
						.bitmap(overlay) //要模糊的图片
						.radius(10)//模糊半径
						.blur();
				img.setImageBitmap(finalBitmap);

				musicAnimView.setBitmap(R.drawable.rect_33);
			}
		});
	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);
	}

}
