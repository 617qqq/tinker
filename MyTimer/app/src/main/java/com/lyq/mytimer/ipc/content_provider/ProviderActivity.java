package com.lyq.mytimer.ipc.content_provider;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.util.Log;
import android.widget.TextView;

import com.lyq.mytimer.base.BaseActivity;

public class ProviderActivity extends BaseActivity {

	private final String TAG = "ProviderActivity";

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(new TextView(this));

		Uri bookUri = Uri.parse("content://com.lyq.mytimer.ipc.book_provider/book");
		ContentValues values = new ContentValues();
		values.put("_id", 6);
		values.put("name", "程序设计的艺术");
		getContentResolver().insert(bookUri, values);
		Cursor bookCursor = getContentResolver().query(bookUri, new String[]{"_id", "name"}, null, null, null);
		while (bookCursor.moveToNext()) {
			Book book = new Book();
			book.bookId = bookCursor.getInt(0);
			book.bookName = bookCursor.getString(1);
			Log.d(TAG, "query book:" + book.toString());
		}
		bookCursor.close();

		Uri userUri = Uri.parse("content://com.lyq.mytimer.ipc.book_provider/user");
		Cursor userCursor = getContentResolver().query(userUri, new String[]{"_id", "name", "sex"}, null, null, null);
		while (userCursor.moveToNext()) {
			User user = new User();
			user.userId = userCursor.getInt(0);
			user.userName = userCursor.getString(1);
			user.isMale = userCursor.getInt(2) == 1;
			Log.d(TAG, "query user:" + user.toString());
		}
		userCursor.close();
	}
}
