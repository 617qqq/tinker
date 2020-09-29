package com.lyq.mytimer.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ShareCompat;

import com.blankj.utilcode.util.ToastUtils;

public class AndroidShareActivity extends AppCompatActivity {

	private int REQUEST_IMAGE_GET = 100;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		TextView view = new TextView(this);
		setContentView(view);
		view.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
				intent.setType("image/*");
				if (intent.resolveActivity(getPackageManager()) != null) {
					startActivityForResult(intent, REQUEST_IMAGE_GET);
				}
			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == REQUEST_IMAGE_GET && resultCode == RESULT_OK) {
			Uri fullPhotoUri = data.getData();
			shareFile(fullPhotoUri);
			// Do work with photo saved at fullPhotoUri
		}
	}

	private void shareFile(Uri fullPhotoUri) {
		ToastUtils.showShort("开启分享");
		ShareCompat.IntentBuilder sc = ShareCompat.IntentBuilder.from(this);
		sc.setType("image/*");
		sc.setSubject("ooooo");
		sc.setText("LLLLL");
		sc.addStream(fullPhotoUri);
		sc.createChooserIntent();
		sc.startChooser();

//		Intent intent = new Intent(Intent.ACTION_SEND);
//		intent.putExtra(Intent.EXTRA_STREAM, fullPhotoUri);
//		intent.putExtra(Intent.EXTRA_TEXT, "OOOOO");
//		intent.putExtra(Intent.EXTRA_TITLE, "LLLLL");
//		intent.setType("image/*");
//		Intent chooser = Intent.createChooser(intent, "分享到：");
//		if (intent.resolveActivity(getPackageManager()) != null) {
//			startActivity(chooser);
//		} else {
//			ToastUtils.showShort("getPackageManager 为空");
//		}
	}
}
