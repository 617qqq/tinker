package com.lyq.mytimer.ui

import android.media.MediaPlayer
import android.os.Bundle
import android.view.MotionEvent
import android.view.SurfaceHolder
import androidx.appcompat.app.AppCompatActivity
import com.blankj.utilcode.util.ToastUtils
import com.lyq.mytimer.R
import kotlinx.android.synthetic.main.act_video.*


class VideoActivity : AppCompatActivity() {

    private var mHolder: SurfaceHolder? = null
    private var resIndex = 0

    private lateinit var mPlayerPlaying: MediaPlayer
    private lateinit var mPlayerNext: MediaPlayer
    private val res = arrayOf(R.raw.record, R.raw.concept, R.raw.clothes_love)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.act_video)
        initView()
    }

    private fun initView() {
        mHolder = target.holder
        addHolderCallback()
    }

    private fun addHolderCallback() {
        mHolder?.addCallback(object : SurfaceHolder.Callback {
            override fun surfaceCreated(holder: SurfaceHolder?) {
                mHolder = holder
                addHolderCallback()
                mPlayerPlaying = MediaPlayer.create(this@VideoActivity, res[getResIndex()])
                mPlayerPlaying.setDisplay(mHolder)
                mPlayerPlaying.start()
                setNext()
            }

            override fun surfaceChanged(holder: SurfaceHolder?, format: Int, width: Int, height: Int) {
                mHolder = holder
                addHolderCallback()
                mPlayerPlaying.setDisplay(mHolder)
            }

            override fun surfaceDestroyed(holder: SurfaceHolder?) {
                mHolder = null
                mPlayerPlaying.release()
            }
        })
    }

    private fun setNext() {
        mPlayerNext = MediaPlayer.create(this@VideoActivity, res[getResIndex()])
        mPlayerPlaying.setOnCompletionListener {
            ToastUtils.showShort("finish")
            val temp = mPlayerPlaying
            mPlayerPlaying = mPlayerNext
            temp.release()
            mPlayerPlaying.setDisplay(mHolder)
            mPlayerPlaying.start()
            setNext()
        }
    }

    private fun getResIndex(): Int {
        val temp = resIndex
        resIndex++
        if (resIndex == res.size) {
            resIndex = 0
        }
        return temp
    }


    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        finish()
        return super.dispatchTouchEvent(ev)
    }
}