package com.example.awesomeapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.awesomeapp.databinding.ItemRowPhotoBinding
import com.example.awesomeapp.data.model.Photo
import java.util.*
import kotlin.collections.ArrayList

class ListPhotoAdapter : RecyclerView.Adapter<ListPhotoAdapter.ListViewHolder>() {
    private var onItemClickCallback: OnItemClickCallback? = null
    private var listPhoto = ArrayList<Photo>()

    fun setData(items: ArrayList<Photo>) {
        listPhoto.clear()
        listPhoto.addAll(items)
        notifyDataSetChanged()
    }

    fun getData(): ArrayList<Photo> = listPhoto

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    inner class ListViewHolder(private val binding: ItemRowPhotoBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(photo: Photo) {
            with(binding) {
                Glide.with(itemView.context)
                        .load(photo.src.medium)
                        .diskCacheStrategy(DiskCacheStrategy.DATA)
                        .into(ivRowItem)
                val title = photo.url
                val titleRemovedPrexix = title.replace("https://www.pexels.com/photo/", "")
                val titleRemovedId = titleRemovedPrexix.replace(photo.id.toString() + "/", "")
                val titleFormatted = titleRemovedId.replace("-", " ").capitalize(Locale.ROOT)
                tvRowItem.text = titleFormatted
                itemView.setOnClickListener {
                    onItemClickCallback?.onItemClicked(photo, titleFormatted)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding = ItemRowPhotoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.bind(listPhoto[position])
    }

    override fun getItemCount(): Int = listPhoto.size

    interface OnItemClickCallback {
        fun onItemClicked(data: Photo, title: String)
    }
}