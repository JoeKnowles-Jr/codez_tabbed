package com.jk.codez.item

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import android.view.View.OnLongClickListener
import com.jk.codez.R
import androidx.recyclerview.widget.RecyclerView.OnChildAttachStateChangeListener

class ItemClickSupport private constructor(private val recyclerView: RecyclerView) {
    private var onItemClickListener: OnItemClickListener? = null
    private var onItemLongClickListener: OnItemLongClickListener? = null
    private val onClickListener = View.OnClickListener { view ->
        if (onItemClickListener != null) {
            val holder = recyclerView.getChildViewHolder(view)
            onItemClickListener!!.onItemClicked(recyclerView, holder.bindingAdapterPosition, view)
        }
    }
    private val onLongClickListener = OnLongClickListener { view ->
        if (onItemLongClickListener != null) {
            val holder = recyclerView.getChildViewHolder(view)
            return@OnLongClickListener onItemLongClickListener!!.onItemLongClicked(
                recyclerView,
                holder.bindingAdapterPosition,
                view
            )
        }
        false
    }

    fun setOnItemClickListener(listener: OnItemClickListener?): ItemClickSupport {
        onItemClickListener = listener
        return this
    }

    fun setOnItemLongClickListener(listener: OnItemLongClickListener?) {
        onItemLongClickListener = listener
    }

    interface OnItemClickListener {
        fun onItemClicked(recyclerView: RecyclerView?, position: Int, view: View?)
    }

    interface OnItemLongClickListener {
        fun onItemLongClicked(recyclerView: RecyclerView?, position: Int, view: View?): Boolean
    }

    companion object {
        fun addTo(view: RecyclerView): ItemClickSupport? {
            return view.getTag(R.id.item_click_support) as ItemClickSupport?
        }
    }

    init {
        recyclerView.setTag(R.id.item_click_support, this)
        val attachListener: OnChildAttachStateChangeListener =
            object : OnChildAttachStateChangeListener {
                override fun onChildViewAttachedToWindow(view: View) {
                    if (onItemClickListener != null) {
                        view.setOnClickListener(onClickListener)
                    }
                    if (onItemLongClickListener != null) {
                        view.setOnLongClickListener(onLongClickListener)
                    }
                }

                override fun onChildViewDetachedFromWindow(view: View) {}
            }
        recyclerView.addOnChildAttachStateChangeListener(attachListener)
    }
}