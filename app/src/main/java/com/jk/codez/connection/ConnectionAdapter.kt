package com.jk.codez.connection

import android.annotation.SuppressLint
import androidx.recyclerview.widget.RecyclerView
import android.view.ViewGroup
import android.view.LayoutInflater
import android.view.View
import com.jk.codez.R
import android.widget.TextView

class ConnectionAdapter @SuppressLint("NotifyDataSetChanged") constructor(private val mConnectionList: MutableList<Connection>?) :
    RecyclerView.Adapter<ConnectionAdapter.ViewHolder>() {
    fun clear() {
        mConnectionList?.clear()
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.connection_view, viewGroup, false)
        view.isFocusable = true
        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, i: Int) {
        viewHolder.bind(mConnectionList!![i])
    }

    override fun getItemCount(): Int {
        return mConnectionList?.size ?: 0
    }

    operator fun get(index: Int): Connection {
        return mConnectionList!![index]
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val tvConnectionName: TextView = view.findViewById(R.id.tv_address)
        private val tvConnectionDescription: TextView = view.findViewById(R.id.tv_codes)
        private val tvConnectionUrl: TextView = view.findViewById(R.id.tv_notes)
        fun bind(c: Connection) {
            val name = c.getName()
            val description = c.getDescription()
            val url = c.getUrl()
            tvConnectionName.text = name
            tvConnectionDescription.text = description
            tvConnectionUrl.text = url
        }

    }

    init {
        notifyDataSetChanged()
    }
}