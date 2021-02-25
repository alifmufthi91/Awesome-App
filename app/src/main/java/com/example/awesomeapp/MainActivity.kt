package com.example.awesomeapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.awesomeapp.databinding.ActivityMainBinding
import com.example.awesomeapp.home.HomeFragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //Melakukan binding agar view dapat digunakan
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        //Inisialisasi fragment manager dan menampilkan fragment home sebagai default
        val mFragmentManager = supportFragmentManager
        val mHomeFragment = HomeFragment.newInstance()
        val fragment =
                mFragmentManager.findFragmentByTag(HomeFragment::class.java.simpleName)
        if (fragment !is HomeFragment) {
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