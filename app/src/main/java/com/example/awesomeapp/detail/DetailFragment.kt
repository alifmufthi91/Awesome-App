package com.example.awesomeapp.detail

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.awesomeapp.R
import com.example.awesomeapp.databinding.FragmentDetailBinding


class DetailFragment : Fragment() {

    private lateinit var detailViewModel: DetailViewModel
    private var fragmentDetailBinding: FragmentDetailBinding? = null

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_detail, container, false)
        //Inisialisasi view binding pada fragment Detail
        val binding = FragmentDetailBinding.bind(view)
        fragmentDetailBinding = binding

        setUpToolbar()
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        //Inisialisasi view model untuk fragment detail
        detailViewModel = ViewModelProvider(
                this,
                ViewModelProvider.NewInstanceFactory()
        )[DetailViewModel::class.java]

        //Ambil data yang dikirim dari fragment sebelumnya
        if (arguments != null) {
            val photographerUrlFromBundle = arguments?.getString(EXTRA_PHOTOGRAPHER_URL)
            detailViewModel.photographerUrl = photographerUrlFromBundle
            val photographerFromBundle = arguments?.getString(EXTRA_PHOTOGRAPHER)
            detailViewModel.photographer = photographerFromBundle
            val imageUriFromBundle = arguments?.getString(EXTRA_IMAGE_URI)
            detailViewModel.imageUri = imageUriFromBundle
        }

        setUpDetail()
    }

    //Menambahkan back button pada toolbar
    private fun setUpToolbar() {
        val toolbar = fragmentDetailBinding!!.toolbarDetail
        toolbar.setTitleTextColor(Color.WHITE)
        toolbar.navigationIcon = ResourcesCompat.getDrawable(resources, R.drawable.ic_baseline_arrow_back_white, null)
        toolbar.setNavigationOnClickListener {
            fragmentManager?.popBackStack()
        }
    }

    //Tampilkan data yang diambil
    private fun setUpDetail() {
        with(fragmentDetailBinding!!) {
            Glide.with(requireContext())
                    .load(detailViewModel.imageUri)
                    .diskCacheStrategy(DiskCacheStrategy.DATA)
                    .into(ivDetailPhoto)
            tvDetailTitle.text = detailViewModel.photographer
            tvDetailPhotographer.text = detailViewModel.photographerUrl
        }
    }


    companion object {

        var EXTRA_PHOTOGRAPHER_URL = "extra_photographer_url"
        var EXTRA_PHOTOGRAPHER = "extra_photographer"
        var EXTRA_IMAGE_URI = "extra_image_uri"

    }
}