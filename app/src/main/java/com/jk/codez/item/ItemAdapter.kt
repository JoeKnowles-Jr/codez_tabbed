package com.jk.codez.item

import android.annotation.SuppressLint
import androidx.recyclerview.widget.RecyclerView
import android.view.ViewGroup
import android.view.LayoutInflater
import android.view.View
import com.jk.codez.R
import android.widget.TextView

class ItemAdapter @SuppressLint("NotifyDataSetChanged") constructor(private val mItemList: MutableList<Item?>) :
    RecyclerView.Adapter<ItemAdapter.ViewHolder>() {
    fun clear() {
        mItemList.clear()
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        val view =
            LayoutInflater.from(viewGroup.context).inflate(R.layout.item_view, viewGroup, false)
        view.isFocusable = true
        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, i: Int) {
        viewHolder.bind(mItemList[i])
    }

    override fun getItemCount(): Int {
        return mItemList.size
    }

    operator fun get(index: Int): Item? {
        return mItemList[index]
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvAddress: TextView = view.findViewById(R.id.tv_address)
        val tvCodes: TextView = view.findViewById(R.id.tv_codes)
        val tvNotes: TextView = view.findViewById(R.id.tv_notes)
        fun bind(i: Item?) {
            val num = i?.getNumber()
            val addy = if (num != null) num.toString() + " " + i.getStreet() else "No number"
            tvAddress.text = addy
            tvCodes.text = i?.codesString
            tvNotes.text = i?.getNotes()
        }

    }

    init {
        notifyDataSetChanged()
    }
}