package com.lyq.mytimer.utils

import android.graphics.Bitmap


object BlurUtils {

    var bitmap: Bitmap? = null
        get() {
            val temp = field
            this.bitmap = null
            return temp
        }
}