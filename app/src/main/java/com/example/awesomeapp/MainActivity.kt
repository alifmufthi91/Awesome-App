package com.example.awesomeapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.awesomeapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val mFragmentManager = supportFragmentManager
    private val mHomeFragment = HomeFragment.newInstance()
    private var fragment =
        mFragmentManager.findFragmentByTag(HomeFragment::class.java.simpleName)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        if (fragment !is DetailFragment) {
            mFragmentManager
                .beginTransaction()
                .add(
                    R.id.fl_main,
                    mHomeFragment,
                    HomeFragment::class.java.simpleName
                )
                .commit()
        }
    }


}