package com.walphotodem.pmd.walphotoedem.models

import AlbumQuery
import android.support.v7.util.DiffUtil

/**
 * This function lets a PagedListAdapter differentiate between objects in a list
 */
val DIFF_CALLBACK: DiffUtil.ItemCallback<AlbumQuery.Record> = object : DiffUtil.ItemCallback<AlbumQuery.Record>() {

    // Check if items represent the same thing.
    override fun areItemsTheSame(oldItem: AlbumQuery.Record, newItem: AlbumQuery.Record): Boolean {
        //since there is no ID field on the Record items
        //the unique photo id appended to the end of the url is used
        val oldItemUrl = oldItem.urls()?.first()?.url()
        val oldItemId = oldItemUrl?.split("/")?.last()
        val newItemUrl = newItem.urls()?.first()?.url()
        val newItemId = newItemUrl?.split("/")?.last()
        return oldItemId == newItemId
    }

    // Checks if the item contents have changed.
    override fun areContentsTheSame(oldItem: AlbumQuery.Record, newItem: AlbumQuery.Record): Boolean {
        return oldItem.urls()?.first()?.url() == newItem.urls()?.first()?.url()
    }
}