package com.example.awesomeapp.home

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.example.awesomeapp.detail.DetailFragment
import com.example.awesomeapp.adapter.GridPhotoAdapter
import com.example.awesomeapp.adapter.ListPhotoAdapter
import com.example.awesomeapp.R
import com.example.awesomeapp.databinding.FragmentHomeBinding
import com.example.awesomeapp.data.model.Photo


class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel
    private lateinit var listPhotoAdapter: ListPhotoAdapter
    private lateinit var gridPhotoAdapter: GridPhotoAdapter
    private var fragmentHomeBinding: FragmentHomeBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        listPhotoAdapter = ListPhotoAdapter()
        gridPhotoAdapter = GridPhotoAdapter()
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        homeViewModel = ViewModelProvider(
                this,
                ViewModelProvider.NewInstanceFactory()
        )[HomeViewModel::class.java]

        //Inisialiasi viewbinding dengan view
        val binding = FragmentHomeBinding.bind(view)
        fragmentHomeBinding = binding
        setUpCollapsingToolbar()
        createMenu()
        setTodayPhoto()
        if (homeViewModel.isListMode) {
            showRecyclerList()
        } else {
            showRecyclerGrid()
        }
        populateList()
    }

    private fun setUpCollapsingToolbar() {
        fragmentHomeBinding!!.collapsingToolbar.setExpandedTitleTextAppearance(R.style.ExpandedAppBar)
        fragmentHomeBinding!!.collapsingToolbar.setCollapsedTitleTextAppearance(R.style.CollapsedAppBar)
    }

    private fun setTodayPhoto() {
        homeViewModel.getTodayPhoto().observe(viewLifecycleOwner, Observer { photos ->
            if (photos != null) {
                if (photos.isNotEmpty()) {
                    val photo = photos[0]
                    context?.let {
                        Log.d("image url", photo.src.landscape)
                        Glide.with(it)
                                .load(photo.src.landscape)
                                .apply(
                                        RequestOptions.placeholderOf(R.drawable.ic_baseline_image)
                                                .error(R.drawable.ic_image_not_supported_black)
                                )
                                .diskCacheStrategy(DiskCacheStrategy.DATA)
                                .into(fragmentHomeBinding!!.ivHomeCurated)
                    }
                }
            }
        })
    }

    private fun populateList() {
        val defaultQuery = "nature"
        homeViewModel.getSearchedPhotos(defaultQuery).observe(viewLifecycleOwner, Observer { photos ->
            if (photos != null) {
                if (homeViewModel.isListMode) {
                    listPhotoAdapter.setData(photos)
                } else {
                    gridPhotoAdapter.setData(photos)
                }
            }
        })
    }

    private fun showRecyclerList() {
        fragmentHomeBinding?.rvPhotos?.layoutManager = LinearLayoutManager(context)
        fragmentHomeBinding?.rvPhotos?.adapter = listPhotoAdapter

        listPhotoAdapter.setOnItemClickCallback(object : ListPhotoAdapter.OnItemClickCallback {
            override fun onItemClicked(data: Photo, title: String) {
                openDetailPhoto(data, title)
            }
        })
    }

    private fun showRecyclerGrid() {
        fragmentHomeBinding?.rvPhotos?.layoutManager = GridLayoutManager(context, 2)
        fragmentHomeBinding?.rvPhotos?.adapter = gridPhotoAdapter

        gridPhotoAdapter.setOnItemClickCallback(object : GridPhotoAdapter.OnItemClickCallback {
            override fun onItemClicked(data: Photo, title: String) {
                openDetailPhoto(data, title)
            }
        })
    }

    override fun onDestroyView() {
        //Lepas viewbinding ketika keluar tampilan
        fragmentHomeBinding = null
        super.onDestroyView()
    }

    private fun createMenu() {
        val toolbar = fragmentHomeBinding!!.toolbar
        toolbar.inflateMenu(R.menu.home_menu)
        toolbar.setTitleTextColor(Color.WHITE)
        toolbar.setOnMenuItemClickListener { menuItem ->
            setMode(menuItem.itemId)
            true
        }
    }

    private fun setMode(selectedMode: Int) {
        when (selectedMode) {
            R.id.menu_home_grid -> {
                homeViewModel.isListMode = false
                showRecyclerGrid()
                populateList()
            }
            R.id.menu_home_list -> {
                homeViewModel.isListMode = true
                showRecyclerList()
                populateList()
            }
        }
    }

    private fun openDetailPhoto(photo: Photo, title: String) {
        val mDetailFragment = DetailFragment()
        val mBundle = Bundle()
        mBundle.putString(DetailFragment.EXTRA_TITLE, title)
        mBundle.putString(DetailFragment.EXTRA_PHOTOGRAPHER, photo.photographer)
        mBundle.putString(DetailFragment.EXTRA_IMAGE_URI, photo.src.large)
        mDetailFragment.arguments = mBundle
        val mFragmentManager = fragmentManager
        mFragmentManager?.beginTransaction()?.apply {
            replace(R.id.fl_main, mDetailFragment, DetailFragment::class.java.simpleName)
            addToBackStack(null)
            commit()
        }
    }

    companion object {

        @JvmStatic
        fun newInstance() =
                HomeFragment()
    }
}