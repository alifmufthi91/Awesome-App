package com.example.awesomeapp.home

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.example.awesomeapp.data.model.Photo
import com.example.awesomeapp.data.source.ApiResponse
import com.example.awesomeapp.data.source.LocalMain
import com.nhaarman.mockitokotlin2.verify
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertNotNull
import junit.framework.TestCase
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner
import kotlin.collections.ArrayList

@RunWith(MockitoJUnitRunner::class)
class HomeViewModelTest {

    @Rule
    @JvmField
    val rule = InstantTaskExecutorRule()

    private lateinit var viewModel: HomeViewModel

    @Mock
    private lateinit var homeRepository: HomeRepository

    @Mock
    private lateinit var observer: Observer<ApiResponse>

    @Before
    fun setUp() {
        viewModel = HomeViewModel(homeRepository)
    }

    @Test
    fun getSearchedShows(){
        val dummyPhotos = LocalMain().getSearchedPhotos()
        val apiResponse = ApiResponse(dummyPhotos)
        val responsePhotos = MutableLiveData<ApiResponse>()
        responsePhotos.value = apiResponse

        `when`(homeRepository.getSearchedPhotos("nature",1)).thenReturn(responsePhotos)
        val photoList = viewModel.getSearchedPhotos("nature", 1).value?.data
        verify(homeRepository).getSearchedPhotos("nature", 1)
        assertNotNull(photoList)
        if (photoList != null) {
            assertEquals(photoList.size, 15)
        }
        viewModel.getSearchedPhotos("nature", 1).observeForever(observer)
        verify(observer).onChanged(apiResponse)

    }

}