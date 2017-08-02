package com.gdfesta.delegates_adapters

import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.util.SparseArray
import android.view.ViewGroup
import io.reactivex.functions.Consumer

/**
 * @author gdfesta
 */
open class ReactiveMainAdapter<T : RecyclerViewType>(val delegateAdapters: SparseArray<DelegateAdapter<RecyclerView.ViewHolder, RecyclerViewType>>, val itemComparator: ItemComparator<T>) : Consumer<List<T>>, RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    interface ItemComparator<in T> {
        fun areItemsTheSame(oldItem: T, newItem: T): Boolean
        fun areContentsTheSame(oldItem: T, newItem: T): Boolean
    }

    private val items = ArrayList<T>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return delegateAdapters[viewType]!!.onCreateViewHolder(parent)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = items[position]
        return delegateAdapters[item.getViewType()]!!.onBindViewHolder(holder, item)
    }

    override fun getItemCount(): Int {
        return items.size;
    }

    override fun getItemViewType(position: Int): Int {
        return items[position].getViewType()
    }

    override fun accept(newList: List<T>) {
        val oldItems = ArrayList<T>(items)
        items.clear()
        items.addAll(newList)
        DiffUtil.calculateDiff(object : DiffUtil.Callback() {
            override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                return itemComparator.areItemsTheSame(oldItems[oldItemPosition], items[newItemPosition])
            }

            override fun getOldListSize(): Int {
                return oldItems.size
            }

            override fun getNewListSize(): Int {
                return items.size
            }

            override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                return itemComparator.areContentsTheSame(oldItems[oldItemPosition], items[newItemPosition])
            }

        }, detectMoves())
    }

    protected fun detectMoves(): Boolean = false

}

