package com.lyq.mytimer.view

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Path
import android.graphics.RectF
import android.graphics.Region
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.widget.ImageView
import androidx.annotation.IntRange
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.marginEnd
import androidx.core.view.marginStart
import androidx.core.view.marginTop
import com.blankj.utilcode.util.LogUtils
import com.lyq.mytimer.R
import kotlinx.android.synthetic.main.view_control.view.*
import kotlin.math.*


class ControlView : ConstraintLayout {

    //数据相关
    private var listener: OnIndexChangeListener? = null
    private var touchIndex = -1

    //绘制相关参数
    //中心坐标
    private var x = 0  //中心坐标
    private var y = 0

    //扇面上的点在坐标系的偏差，out是外轮廓，in是内轮廓
    private var sinOutRadius = 0f
    private var sinInRadius = 0f
    private var cosOutRadius = 0f
    private var cosInRadius = 0f

    //Bitmap半径，内轮廓半径，外轮廓半径
    private var inRadius = 0f
    private var outRadius = 0f
    private var controlRadius = 0f

    private var path = arrayOf(Path(), Path(), Path(), Path())

    //点击范围
    private val regions = arrayOf(Region(), Region(), Region(), Region())

    private var oldWidth = 0
    private var oldHeight = 0
    private lateinit var imgList: Array<ImageView>

    private var anim: ValueAnimator? = null
    private var animStartX = 0
    private var animStartY = 0

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(context)
    }

    private fun inflateLayout(context: Context) {
        LayoutInflater.from(context).inflate(R.layout.view_control, this)
        imgList = arrayOf(imgTop, imgEnd, imgBottom, imgStart)
    }

    private fun init(context: Context) {
        inflateLayout(context)
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        if (width != oldWidth || height != oldHeight) {
            oldWidth = width
            oldHeight = height
            x = width / 2
            y = height / 2
            outRadius = width * 0.433f
            inRadius = width * 0.08f
            controlRadius = width * 0.297f

//            val angle = 45f
//            sinOutRadius = (sin(angle * Math.PI / 180) * outRadius).toFloat()
//            cosOutRadius = (cos(angle * Math.PI / 180) * outRadius).toFloat()
//            sinInRadius = (sin(angle * Math.PI / 180) * inRadius).toFloat()
//            cosInRadius = (cos(angle * Math.PI / 180) * inRadius).toFloat()

            //initPath()
            //initRegion()
        }
    }

    private fun initPath() {
        val rectF = RectF(x - outRadius, y - outRadius, x + outRadius, y + outRadius)
        val rectFIn = RectF(x - inRadius, y - inRadius, x + inRadius, y + inRadius)
        val sweepAngle = 95f
        val angleOffset = 0f
        path.forEach {
            it.reset()
        }

        path[0].moveTo(0f, 0f)
        path[0].lineTo(oldWidth.toFloat(), 0f)
        path[0].lineTo(x + sinInRadius, y - cosInRadius)
        path[0].arcTo(rectFIn, -45 - angleOffset, -sweepAngle)
        path[0].close()

        path[1].moveTo(oldWidth.toFloat(), 0f)
        path[1].lineTo(oldWidth.toFloat(), oldHeight.toFloat())
        path[1].lineTo(x + cosInRadius, y + sinInRadius)
        path[1].arcTo(rectFIn, 45 - angleOffset, -sweepAngle)
        path[1].close()

        path[2].moveTo(oldWidth.toFloat(), oldHeight.toFloat())
        path[2].lineTo(0f, oldHeight.toFloat())
        path[2].lineTo(x - sinInRadius, y + cosInRadius)
        path[2].arcTo(rectFIn, 135 - angleOffset, -sweepAngle)
        path[2].close()

        path[3].moveTo(0f, oldHeight.toFloat())
        path[3].lineTo(0f, 0f)
        path[3].lineTo(x - cosInRadius, y - sinInRadius)
        path[3].arcTo(rectFIn, 225 - angleOffset, -sweepAngle)
        path[3].close()
    }


    private fun initRegion() {
        val region = Region(0, 0, oldWidth, oldHeight)
        for (i in path.indices) {
            regions[i].setPath(path[i], region)
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        return when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                updateCurrentIndex(event)
                true
            }
            MotionEvent.ACTION_MOVE -> {
                updateCurrentIndex(event)
                LogUtils.e("${event.x}\t\t\t${event.y}")
                super.onTouchEvent(event)
            }
            MotionEvent.ACTION_UP -> {
                touchIndex = -1
                updateViewAndListener(event)
                super.onTouchEvent(event)
            }
            else -> super.onTouchEvent(event)
        }
    }

    private fun updateCurrentIndex(event: MotionEvent) {
//        for (i in regions.indices) {
//            if (regions[i].contains(event.x.toInt(), event.y.toInt())) {
//                touchIndex = i
//                break
//            } else if (i == regions.size - 1) {
//                touchIndex = -1
//            }
//        }
        val offsetX = x - event.x
        val offsetY = y - event.y
        touchIndex = if (offsetX < 0) {
            when {
                abs(offsetY) < abs(offsetX) -> 1
                offsetY < 0 -> 2
                else -> 0
            }
        } else {
            when {
                abs(offsetY) < abs(offsetX) -> 3
                offsetY < 0 -> 2
                else -> 0
            }
        }
        updateViewAndListener(event)
    }

    private fun updateViewAndListener(event: MotionEvent) {
        if (touchIndex == -1) {
            if (anim == null) {
                anim = createAndStartResetAnim()
            }
        } else {
            anim?.cancel()
            anim = null
            tvControl.layoutParams = (tvControl.layoutParams as LayoutParams).apply {
                val offset =
                        sqrt((x - event.x).toDouble().pow(2.0) +
                                (y - event.y).toDouble().pow(2.0)
                        ) / controlRadius
                if (offset > 1){
                    leftMargin = (x - (x - event.x) / offset).toInt()
                    topMargin = (y - (y - event.y) / offset).toInt()
                }else{
                    leftMargin = event.x.toInt()
                    topMargin = event.y.toInt()
                }
            }
        }
        imgList.withIndex().forEach {
            it.value.visibility = if (it.index == touchIndex) VISIBLE else GONE
        }
        listener?.onChange(touchIndex)
    }

    private fun createAndStartResetAnim(): ValueAnimator {
        return ValueAnimator.ofFloat(1f).apply {
            duration = 300
            val params = tvControl.layoutParams as LayoutParams
            animStartX = params.leftMargin
            animStartY = params.topMargin
            addUpdateListener {
                tvControl.layoutParams = (tvControl.layoutParams as LayoutParams).apply {
                    leftMargin = (x + (animStartX - x) * (1f - it.animatedValue as Float)).toInt()
                    topMargin = (y + (animStartY - y) * (1f - it.animatedValue as Float)).toInt()
                }
            }
            start()
        }
    }

    private fun getControlY(leftMargin: Int, top: Int): Int {
        val offset = sqrt(controlRadius.toDouble().pow(2.0) - abs((leftMargin - x)).toDouble().pow(2.0))
        return when {
            top < y - offset -> (y - offset).toInt()
            top > y + offset -> (y + offset).toInt()
            else -> top
        }
    }

    private fun getControlX(start: Int): Int {
        return when {
            start < x - controlRadius -> (x - controlRadius).toInt()
            start > x + controlRadius -> (x + controlRadius).toInt()
            else -> start
        }
    }


    interface OnIndexChangeListener {
        fun onChange(@IntRange(from = -1, to = 3) index: Int)
    }

    fun setListener(listener: OnIndexChangeListener) {
        this.listener = listener
    }
}