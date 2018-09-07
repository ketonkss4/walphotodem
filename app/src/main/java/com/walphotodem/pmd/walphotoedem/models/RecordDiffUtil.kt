package com.walphotodem.pmd.walphotoedem.models

import AlbumQuery
import android.support.v7.util.DiffUtil

/**
 * This function lets a PagedListAdapter differentiate between objects in a list
 */
val DIFF_CALLBACK: DiffUtil.ItemCallback<AlbumQuery.Record> = object : DiffUtil.ItemCallback<AlbumQuery.Record>() {

    // Check if items represent the same thing.
    override fun areItemsTheSame(oldItem: AlbumQuery.Record, newItem: AlbumQuery.Record): Boolean {
        return oldItem.urls()?.first()?.url() == newItem.urls()?.first()?.url()
    }

    // Checks if the item contents have changed.
    override fun areContentsTheSame(oldItem: AlbumQuery.Record, newItem: AlbumQuery.Record): Boolean {
        return true // Assume record details don't change
    }
}