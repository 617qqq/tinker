package com.lyq.mytimer.adapter;

import android.support.annotation.Nullable;

/**
 * RecyclerView Adapter 的点击回调
 */

public interface OnListClickListener {

    int TAG_1 = 1;
    int TAG_2 = 2;
    int TAG_3 = 3;
    int TAG_4 = 4;
    int TAG_5 = 5;
    int TAG_6 = 6;

    //可根据tag来区分点击的是item内部哪个控件
    void onTagClick(int tag, int position, @Nullable Object... object);
}
