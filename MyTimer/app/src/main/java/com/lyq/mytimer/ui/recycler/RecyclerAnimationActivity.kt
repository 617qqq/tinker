package com.lyq.mytimer.ui.recycler

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.lyq.mytimer.R
import com.lyq.mytimer.adapter.SimpleAdapter
import kotlinx.android.synthetic.main.activity_recycler_animation.*
import java.util.*


class RecyclerAnimationActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recycler_animation)

        val list = arrayListOf<String>()
        for (i in 0..100) {
            list.add(i.toString())
        }
        val adapter = SimpleAdapter(this, list)
        rv.adapter = adapter
        rv.layoutManager = LinearLayoutManager(this)
        rv.itemAnimator = MyItemAnimator()

        btnAdd.setOnClickListener {
            list.add(3, "add")
            adapter.notifyItemInserted(3)
        }

        btnRemove.setOnClickListener {
            list.removeAt(3)
            adapter.notifyItemRemoved(3)
        }
        btnMove.setOnClickListener {
            Collections.swap(list, 3, 5)
            adapter.notifyItemMoved(3, 5)
        }
        btnChange.setOnClickListener {
            list[3] = "change"
            adapter.notifyItemChanged(3)
        }
    }
}