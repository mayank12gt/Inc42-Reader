package com.example.inc42reader.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.example.inc42reader.R

import com.example.inc42reader.adapters.ViewPagerFragAdapter
import com.example.inc42reader.models.Feed
import com.example.inc42reader.viewmodels.ExploreFragmentViewModel
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class HomeFragment : Fragment() {

    private var param1: String? = null
    private var param2: String? = null

    private lateinit var followedFeeds:ArrayList<Feed>
    private lateinit var viewModel: ExploreFragmentViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
        viewModel = ViewModelProvider(this).get(ExploreFragmentViewModel::class.java)

        followedFeeds = ArrayList()

//        followedFeeds.add(Feed(2,"Edtech","https://inc42.com/industry/fintech/feed",true))
//        followedFeeds.add(Feed(3,"Fintech","https",true))
//        followedFeeds.add(Feed(3,"HealthTech","https",true))
//        followedFeeds.add(Feed(3,"Logistics","https",true))
//        followedFeeds.add(Feed(3,"Pharma","https",true))
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
      val v =  inflater.inflate(R.layout.fragment_home, container, false)
        val homeTabLayout: TabLayout = v.findViewById(R.id.home_tab_layout)
        val homeViewPager: ViewPager2 = v.findViewById(R.id.home_view_pager)
        val tabsAdapter: ViewPagerFragAdapter = ViewPagerFragAdapter(this,followedFeeds)

        homeViewPager.adapter = tabsAdapter
        viewModel.getFollowedFeeds().observe(viewLifecycleOwner,{
            followedFeeds.clear()
            followedFeeds.add(Feed(1,
                "Latest",
                "The latest startup news from the Indian Startup Ecosystem including new startup fundings, acquisitions, government policies, investments funds, and more.",
                "https://inc42.com/buzz/feed",true))
            Log.d("followedFeeds",it.toString())
//            followedFeeds.addAll(it)
//            tabsAdapter.update(followedFeeds)
            it.data?.let { it1 -> followedFeeds.addAll(it1) }
            tabsAdapter.update(followedFeeds)
            updateTabs(homeTabLayout)
        })

        TabLayoutMediator(homeTabLayout, homeViewPager){
                tab, position ->

                tab.text = followedFeeds[position].title

        }
            .attach()


    return v
    }

    private fun updateTabs(homeTabLayout: TabLayout) {
        val tabCount = homeTabLayout.tabCount
        val tab = homeTabLayout.newTab()
        val tabView = LayoutInflater.from(context).inflate(R.layout.layout_button_add_feed, homeTabLayout, false)
        tab.customView = tabView
        homeTabLayout.addTab(tab)

        tabView.findViewById<Button>(R.id.add_feeds_btn)?.setOnClickListener {
            Log.d("ButtonTab", "Button clicked")
//            navigateToNewFragment()
            parentFragmentManager.beginTransaction()
                .replace(R.id.frame, ExploreFragment.newInstance()).commit()
        }
    }

    companion object {

        @JvmStatic
        fun newInstance() =
            HomeFragment().apply {
//                arguments = Bundle().apply {
//                    putString(ARG_PARAM1, param1)
//                    putString(ARG_PARAM2, param2)
//                }
            }
    }
}