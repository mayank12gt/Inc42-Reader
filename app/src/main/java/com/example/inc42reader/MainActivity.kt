package com.example.inc42reader

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.inc42reader.R
import com.example.inc42reader.ui.ExploreFragment
import com.example.inc42reader.ui.HomeFragment
import com.example.inc42reader.ui.SearchFragment
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val bottomNavigationView: BottomNavigationView = findViewById(R.id.bottom_nav_bar)
        val toolbar: MaterialToolbar = findViewById(R.id.topAppBar)

        bottomNavigationView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.home -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.frame, HomeFragment.newInstance()).commit()
                }
                R.id.search -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.frame, SearchFragment.newInstance()).commit()
                }
                R.id.explore -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.frame, ExploreFragment.newInstance()).commit()
                }
//                R.id.bookmarks-> {
//                    supportFragmentManager.beginTransaction()
//                        .replace(R.id.frame, BookmarksFragment.newInstance()).commit()}
            }
            true
        }
        bottomNavigationView.selectedItemId = R.id.home

    }
}