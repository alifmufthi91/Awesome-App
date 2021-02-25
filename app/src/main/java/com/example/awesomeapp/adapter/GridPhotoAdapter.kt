package com.example.awesomeapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.awesomeapp.databinding.ItemGridPhotoBinding
import com.example.awesomeapp.data.model.Photo
import java.util.*
import kotlin.collections.ArrayList

//Adapter untuk recyclerview apabila tampilan
//dalam mode Grid
class GridPhotoAdapter : RecyclerView.Adapter<GridPhotoAdapter.GridViewHolder>() {
    private var onItemClickCallback: OnItemClickCallback? = null
    var listPhoto = ArrayList<Photo>()

    fun setData(items: ArrayList<Photo>) {
        listPhoto.clear()
        listPhoto.addAll(items)
        notifyDataSetChanged()
    }

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    fun getData(): ArrayList<Photo> = listPhoto

    inner class GridViewHolder(private val binding: ItemGridPhotoBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(photo: Photo) {
            with(binding) {
                Glide.with(itemView.context)
                        .load(photo.src.medium)
                        .diskCacheStrategy(DiskCacheStrategy.DATA)
                        .into(ivGridPhoto)
                tvGridItem.text = photo.photographer
                itemView.setOnClickListener { onItemClickCallback?.onItemClicked(photo) }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GridViewHolder {
        val binding = ItemGridPhotoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return GridViewHolder(binding)
    }

    override fun onBindViewHolder(holder: GridViewHolder, position: Int) {
        holder.bind(listPhoto[position])
    }

    override fun getItemCount(): Int = listPhoto.size

    interface OnItemClickCallback {
        fun onItemClicked(data: Photo)
    }
}