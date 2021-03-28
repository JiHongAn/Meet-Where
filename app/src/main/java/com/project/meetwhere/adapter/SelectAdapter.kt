package com.project.meetwhere.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.project.meetwhere.R
import com.project.meetwhere.model.Station
import kotlinx.android.synthetic.main.item_station.view.*

class SelectAdapter(private val items: ArrayList<Station>) :
    RecyclerView.Adapter<SelectAdapter.ViewHolder>() {

    interface ItemClick {
        fun onClick(view: View, position: Int)
    }

    var itemClick: ItemClick? = null

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        val listener = View.OnClickListener { v ->
            if (itemClick != null) {
                itemClick?.onClick(v, position)
            }
        }

        holder.apply {
            bind(listener, item)
            itemView.tag = item
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflatedView =
            LayoutInflater.from(parent.context).inflate(R.layout.item_select, parent, false)
        return ViewHolder(inflatedView)
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private var view: View = view

        fun bind(clickEvent: View.OnClickListener, item: Station) {
            // 역 이름 지정
            view.text_name.text = item.name

            var lineBtn = arrayOf(
                view.line1,
                view.line2,
                view.line3,
                view.line4,
                view.line5,
                view.line6,
                view.line7,
                view.line8,
                view.line9
            )

            var temp = arrayOf(0, 0, 0, 0, 0, 0, 0, 0, 0)

            // 해당하는 노선 보여주기
            if (item.line != null) {
                val arr = item.line!!.split(",")

                for (i in arr.indices) {
                    var line = arr[i].toInt()
                    line -= 1
                    temp[line] = 1
                }

                for (i in temp.indices) {
                    if (temp[i] == 0) {
                        lineBtn[i].visibility = View.GONE
                    } else {
                        lineBtn[i].visibility = View.VISIBLE
                    }
                }
            }

            // touch event
            view.setOnClickListener(clickEvent)
        }
    }
}