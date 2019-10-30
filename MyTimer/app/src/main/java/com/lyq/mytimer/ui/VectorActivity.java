package com.lyq.mytimer.ui;

import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.lyq.mytimer.R;

public class VectorActivity extends AppCompatActivity {

	private ImageView tvAnim ;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_vector);

		tvAnim = findViewById(R.id.tv_anim);
	}

	public void onClickAnim(View view) {
		Drawable drawable = tvAnim.getDrawable();
		if (drawable instanceof Animatable){
			((Animatable) drawable).start();
		}
	}
}
