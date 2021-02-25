package com.example.awesomeapp.home

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.example.awesomeapp.CustomRecyclerViewScrollListener
import com.example.awesomeapp.CustomViewModelFactory
import com.example.awesomeapp.R
import com.example.awesomeapp.adapter.GridPhotoAdapter
import com.example.awesomeapp.adapter.ListPhotoAdapter
import com.example.awesomeapp.data.model.Photo
import com.example.awesomeapp.databinding.FragmentHomeBinding
import com.example.awesomeapp.detail.DetailFragment


class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel
    private lateinit var listPhotoAdapter: ListPhotoAdapter
    private lateinit var gridPhotoAdapter: GridPhotoAdapter
    private lateinit var mScrollListener: CustomRecyclerViewScrollListener
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

        //Inisialisasi untuk view model
        val viewModelFactory = CustomViewModelFactory.getInstance()
        homeViewModel = ViewModelProvider(
            this,
            viewModelFactory
        )[HomeViewModel::class.java]

        //Inisialiasi viewbinding dengan view
        val binding = FragmentHomeBinding.bind(view)
        fragmentHomeBinding = binding

        setUpCollapsingToolbar()
        createMenu()
        setTodayPhoto()

        //Cek apakah tampilan sedang dalam mode list atau grid
        if (homeViewModel.isListMode) {
            showRecyclerList()
        } else {
            showRecyclerGrid()
        }
        populateList()
    }

    //SetUp warna title pada toolbar
    private fun setUpCollapsingToolbar() {
        fragmentHomeBinding!!.collapsingToolbar.setExpandedTitleTextAppearance(R.style.ExpandedAppBar)
        fragmentHomeBinding!!.collapsingToolbar.setCollapsedTitleTextAppearance(R.style.CollapsedAppBar)
    }

    //Mengambil foto untuk tampilan expanded
    private fun setTodayPhoto() {
        homeViewModel.getTodayPhoto().observe(viewLifecycleOwner, Observer { response ->
            if (response != null) {
                if (response.error == null) {
                    //Apabila sukses
                    if (response.data?.isNotEmpty()!!) {
                        val photo = response.data?.get(0)
                        context?.let {
                            Log.d("image url", photo?.src?.landscape)
                            Glide.with(it)
                                .load(photo?.src?.landscape)
                                .apply(
                                    RequestOptions.placeholderOf(R.drawable.ic_baseline_image)
                                        .error(R.drawable.ic_image_not_supported_black)
                                )
                                .diskCacheStrategy(DiskCacheStrategy.DATA)
                                .into(fragmentHomeBinding!!.ivHomeCurated)
                        }
                    }
                } else {
                    //Tampilkan error apabila gagal
                    Toast.makeText(
                        context,
                        "Error: ${response?.error?.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        })
    }

    private fun populateList() {
        //Munculkan loading
        showLoading()

        //Mengambil foto untuk ditampilkan pada recyclerview
        val defaultQuery = "nature"
        homeViewModel.getSearchedPhotos(defaultQuery, homeViewModel.currentPage)
            .observe(viewLifecycleOwner, Observer { response ->
                if (response != null) {
                    if (response.error == null) {
                        //Apabila berhasil
                        if (homeViewModel.isListMode) {
                            response.data?.let { listPhotoAdapter.setData(it) }
                        } else {
                            response.data?.let { gridPhotoAdapter.setData(it) }
                        }
                        homeViewModel.currentPage+=1
                    }
                } else {
                    //Apabila gagal tampilkan pesan
                    Toast.makeText(
                        context,
                        "Error: ${response?.error?.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                //Sembunyikan loading
                hideLoading()
            })
    }

    private fun showRecyclerList() {
        //Tampilkan dalam mode List
        homeViewModel.currentPage = 1
        fragmentHomeBinding?.rvPhotos?.layoutManager = LinearLayoutManager(context)
        mScrollListener =
            CustomRecyclerViewScrollListener(fragmentHomeBinding?.rvPhotos?.layoutManager as LinearLayoutManager)
        mScrollListener.setOnLoadMoreListener(object : CustomRecyclerViewScrollListener.OnLoadMoreListener{
            override fun onLoadMore() {
                loadMore()
            }
        })
        fragmentHomeBinding?.rvPhotos?.addOnScrollListener(mScrollListener)
        fragmentHomeBinding?.rvPhotos?.adapter = listPhotoAdapter
        listPhotoAdapter.setOnItemClickCallback(object : ListPhotoAdapter.OnItemClickCallback {
            override fun onItemClicked(data: Photo) {
                openDetailPhoto(data)
            }
        })
    }

    private fun showRecyclerGrid() {
        //Tampilkan dalam mode Grid
        homeViewModel.currentPage = 1
        fragmentHomeBinding?.rvPhotos?.layoutManager = GridLayoutManager(context, 2)
        mScrollListener =
            CustomRecyclerViewScrollListener(fragmentHomeBinding?.rvPhotos?.layoutManager as GridLayoutManager)
        mScrollListener.setOnLoadMoreListener(object : CustomRecyclerViewScrollListener.OnLoadMoreListener{
            override fun onLoadMore() {
                loadMore()
            }
        })
        fragmentHomeBinding?.rvPhotos?.addOnScrollListener(mScrollListener)
        fragmentHomeBinding?.rvPhotos?.adapter = gridPhotoAdapter

        gridPhotoAdapter.setOnItemClickCallback(object : GridPhotoAdapter.OnItemClickCallback {
            override fun onItemClicked(data: Photo) {
                openDetailPhoto(data)
            }
        })
    }

    override fun onDestroyView() {
        //Lepas viewbinding ketika keluar tampilan
        fragmentHomeBinding = null
        super.onDestroyView()
    }

    private fun createMenu() {
        //Tambahkan menu grid dan list pada toolbar
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

    private fun openDetailPhoto(photo: Photo) {
        //Menyiapkan data untuk fragment detail
        val mDetailFragment = DetailFragment()
        val mBundle = Bundle()
        mBundle.putString(DetailFragment.EXTRA_PHOTOGRAPHER_URL, photo.photographerUrl)
        mBundle.putString(DetailFragment.EXTRA_PHOTOGRAPHER, photo.photographer)
        mBundle.putString(DetailFragment.EXTRA_IMAGE_URI, photo.src.large)
        mDetailFragment.arguments = mBundle

        //Pindah ke detail fragment
        val mFragmentManager = fragmentManager
        mFragmentManager?.beginTransaction()?.apply {
            setCustomAnimations(
                R.anim.slide_in,
                R.anim.fade_out,
                R.anim.fade_in,
                R.anim.slide_out
            )
            replace(R.id.fl_main, mDetailFragment, DetailFragment::class.java.simpleName)
            addToBackStack(null)
            commit()
        }
    }

    private fun showLoading() {
        fragmentHomeBinding?.progressHome?.visibility = View.VISIBLE
    }

    private fun hideLoading() {
        fragmentHomeBinding?.progressHome?.visibility = View.GONE
    }

    private fun loadMore() {
        showLoading()
        val defaultQuery = "nature"
        homeViewModel.getSearchedPhotos(defaultQuery, homeViewModel.currentPage)
            .observe(viewLifecycleOwner, Observer { response ->
                if (response != null) {
                    if (response.error == null) {
                        //Apabila berhasil
                        response.data?.let {
                            if (homeViewModel.isListMode) {
                                response.data?.let {
                                    listPhotoAdapter.listPhoto.addAll(it)
                                    listPhotoAdapter.notifyDataSetChanged()
                                }
                            } else {
                                response.data?.let {
                                    gridPhotoAdapter.listPhoto.addAll(it)
                                    gridPhotoAdapter.notifyDataSetChanged()
                                }
                            }
                            homeViewModel.currentPage+=1
                        }
                    }
                } else {
                    //Apabila gagal tampilkan pesan
                    Toast.makeText(
                        context,
                        "Error: ${response?.error?.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                //Sembunyikan loading
                hideLoading()
                mScrollListener.setLoaded()
            })
    }

    companion object {

        @JvmStatic
        fun newInstance() =
            HomeFragment()
    }
}