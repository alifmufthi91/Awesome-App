package com.example.awesomeapp

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Switch
import android.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.awesomeapp.databinding.FragmentHomeBinding
import com.example.awesomeapp.model.GetPhotosResponse
import com.example.awesomeapp.networking.RemoteDataSource
import retrofit2.Call
import retrofit2.Response


class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel
    private lateinit var listPhotoAdapter: ListPhotoAdapter
    private var fragmentHomeBinding: FragmentHomeBinding? = null

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
        showRecyclerList()
        populateList()
    }

    private fun setUpCollapsingToolbar(){
        fragmentHomeBinding!!.collapsingToolbar.setExpandedTitleTextAppearance(R.style.ExpandedAppBar)
        fragmentHomeBinding!!.collapsingToolbar.setCollapsedTitleTextAppearance(R.style.CollapsedAppBar)
    }

    private fun setTodayPhoto(){
        homeViewModel.getTodayPhoto().observe(viewLifecycleOwner, Observer { photos ->
            if (photos != null){
                if(photos.isNotEmpty()){
                    val photo = photos[0]
                    context?.let {
                        Log.d("image url", photo.src.landscape)
                        Glide.with(it)
                            .load(photo.src.landscape)
                            .apply(
                                RequestOptions.placeholderOf(R.drawable.ic_baseline_image)
                                    .error(R.drawable.ic_image_not_supported_black)
                            )
                            .into(fragmentHomeBinding!!.ivHomeCurated)
                    }
                }
            }
        })
    }

    private fun populateList(){
        val defaultQuery = "nature"
        homeViewModel.getSearchedPhotos(defaultQuery).observe(viewLifecycleOwner, Observer { photos ->
            if (photos != null){
                listPhotoAdapter.setData(photos)
            }
        })
    }

    private fun showRecyclerList() {
        fragmentHomeBinding?.rvPhotos?.layoutManager = LinearLayoutManager(context)
        listPhotoAdapter = ListPhotoAdapter()
        fragmentHomeBinding?.rvPhotos?.adapter = listPhotoAdapter
    }

    override fun onDestroyView() {
        //Lepas viewbinding ketika keluar tampilan
        fragmentHomeBinding = null
        super.onDestroyView()
    }

    private fun createMenu(){
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
                showRecyclerList()
                populateList()
            }
            R.id.menu_home_list -> {
            }
        }
    }

    companion object {

        @JvmStatic
        fun newInstance() =
            HomeFragment()
    }
}