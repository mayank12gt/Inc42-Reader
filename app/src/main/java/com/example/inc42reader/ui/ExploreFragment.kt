package com.example.inc42reader.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.inc42reader.R
import com.example.inc42reader.adapters.FeedsAdapter
import com.example.inc42reader.models.Feed
import com.example.inc42reader.utils.MyResponse
import com.example.inc42reader.viewmodels.ExploreFragmentViewModel
import com.google.android.material.progressindicator.CircularProgressIndicator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.ArrayList
import java.util.Timer
import java.util.TimerTask


class ExploreFragment : Fragment(), OnFeedClicked, OnFollowClicked {
//    // TODO: Rename and change types of parameters
//    private var param1: String? = null
//    private var param2: String? = null
    private lateinit var viewModel: ExploreFragmentViewModel
    private lateinit var loadingIndicator:CircularProgressIndicator
    private lateinit var  adapter:FeedsAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        arguments?.let {
//            param1 = it.getString(ARG_PARAM1)
//            param2 = it.getString(ARG_PARAM2)
//        }


        viewModel = ViewModelProvider(this).get(ExploreFragmentViewModel::class.java)


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

    val v = inflater.inflate(R.layout.fragment_explore, container, false)
    val feedsList:List<Feed> = ArrayList<Feed>()

    adapter = FeedsAdapter(feedsList,this,this)
    val feedsRV = v.findViewById<RecyclerView>(R.id.feeds_rv)
        loadingIndicator = v.findViewById(R.id.feeds_loading_indicator)
        val searchView: SearchView = v.findViewById(R.id.feeds_search_view)


        feedsRV.layoutManager = LinearLayoutManager(context)
        feedsRV.adapter = adapter

        getFeeds()



        searchView.setOnQueryTextListener(object:SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                TODO("Not yet implemented")
                return false
            }

            var timer:Timer? = null

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText?.trim()?.isEmpty()!=true){
                    Log.d("query",newText.toString())
                    timer?.cancel()
                    timer  = Timer()
                    timer?.schedule(object: TimerTask(){
                        override fun run() {
                            Log.d("querySearched",newText.toString())
                            CoroutineScope(Dispatchers.Main).launch {
                                searchFeeds(newText.toString())
                            }

                        }

                    },100)
                }
                else{
                    Log.d("query","empty")
                    getFeeds()
                }
                return true
            }

        })


    return v
    }

    private fun searchFeeds(query: String) {
        viewModel.searchFeeds(query).observe(viewLifecycleOwner,{
            when(it.status){
                MyResponse.Status.LODING -> {
                    Log.d("loading","true")
                    loadingIndicator.visibility = View.VISIBLE
                }
                MyResponse.Status.SUCCESS ->{
                    Log.d("loading","false")
                    loadingIndicator.visibility = View.GONE
                    it.data?.let { it1 -> adapter.update(it1) }
                }
                MyResponse.Status.ERROR -> TODO()
            }
        })


    }

    private fun getFeeds(){
        viewModel.getAllFeeds().observe(viewLifecycleOwner,{
            when(it.status){
                MyResponse.Status.LODING ->{
                    Log.d("loading","true")
                    loadingIndicator.visibility = View.VISIBLE}
                MyResponse.Status.SUCCESS ->{
                    Log.d("loading","false")
                    loadingIndicator.visibility = View.GONE
                    it.data?.let { it1 -> adapter.update(it1) }
                }
                MyResponse.Status.ERROR -> TODO()
            }

        })
    }

    companion object {

        @JvmStatic
        fun newInstance() =
            ExploreFragment().apply {
//                arguments = Bundle().apply {
//                    putString(ARG_PARAM1, param1)
//                    putString(ARG_PARAM2, param2)
//                }
            }
    }

    override fun onFeedClicked(feed: Feed, position: Int) {

       parentFragmentManager.beginTransaction().
       replace(R.id.frame,FeedFragment.newInstance(feed)).
       addToBackStack(null).commit()
    }

    override fun OnFollowClicked(feed: Feed, position: Int) {
        feed.isFollowed=!feed.isFollowed
        Log.d("followed", feed.isFollowed.toString())
      viewModel.followFeed(feed)
    }
}