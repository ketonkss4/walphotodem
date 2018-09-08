package com.walphotodem.pmd.walphotoedem.adapter

import AlbumQuery
import android.arch.paging.PagedListAdapter
import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.squareup.picasso.Picasso
import com.walphotodem.pmd.walphotoedem.R
import com.walphotodem.pmd.walphotoedem.Util.PhotoSizeSelectionHelper
import com.walphotodem.pmd.walphotoedem.models.DIFF_CALLBACK

/**
 * PhotoGridAdapter extends PagedListAdapter to support the paging functionality
 * recently introduced by google abstracting away paging complexity greatly simplifying
 * setup and reducing verbosity
 */

class PhotoGridAdapter(private val photoSizeSelectionHelper: PhotoSizeSelectionHelper) :
        PagedListAdapter<AlbumQuery.Record, PhotoGridAdapter.ViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.photo_view, null)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val photoSizeSelection = photoSizeSelectionHelper.getPhotoSizeSelection()
        val photo = getItem(position)?.urls()?.filter {
            it.size_code() == photoSizeSelection
        }?.first()

        if (photo != null) {
            photo.url()?.let { holder.bind(it) }
        }
    }

    private fun loadPhotoIntoView(context: Context, imageView: ImageView, photoUrl: String) {
        Picasso.with(context)
                .load(photoUrl)
                .into(imageView)

    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val photoView: ImageView = itemView.findViewById(R.id.photo_view)
        fun bind(url: String) {
            loadPhotoIntoView(itemView.context,
                    photoView,
                    url)
        }
    }
}
