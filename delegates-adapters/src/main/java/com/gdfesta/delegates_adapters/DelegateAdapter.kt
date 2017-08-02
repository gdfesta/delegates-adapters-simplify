package com.gdfesta.delegates_adapters

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup

interface DelegateAdapter<VH : RecyclerView.ViewHolder, in T : RecyclerViewType> {
    fun onCreateViewHolder(parent: ViewGroup): VH
    fun onBindViewHolder(holder: VH, item: T)
}