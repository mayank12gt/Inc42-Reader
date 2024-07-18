package com.example.inc42reader.adapters

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.inc42reader.models.Feed
import com.example.inc42reader.ui.FeedFragment

class ViewPagerFragAdapter(frag:Fragment, var followedFeeds: List<Feed>): FragmentStateAdapter(frag) {

    override fun getItemCount(): Int {
       return followedFeeds.size
    }

    override fun createFragment(position: Int): Fragment {
        return FeedFragment.newInstance(followedFeeds.get(position))
    }

    fun update(followedFeeds: List<Feed>){
        this.followedFeeds = followedFeeds
        notifyDataSetChanged()
    }
}