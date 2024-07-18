package com.example.inc42reader.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.inc42reader.R

import com.example.inc42reader.adapters.PostsAdapter
import com.example.inc42reader.utils.MyResponse
import com.example.inc42reader.viewmodels.FeedFragmentViewModel
import com.google.android.material.button.MaterialButton
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.progressindicator.CircularProgressIndicator
import com.prof18.rssparser.model.RssChannel
import com.prof18.rssparser.model.RssItem
import kotlinx.coroutines.launch
import java.util.Timer


class SearchFragment : Fragment(),OnBookmarkBtnClicked,OnPostItemClicked,OnLoadMoreClicked {

    lateinit var viewModel: FeedFragmentViewModel
    lateinit var loadingIndicator: CircularProgressIndicator
    lateinit var postsRV: RecyclerView
    lateinit var adapter: PostsAdapter
    lateinit var postsList: ArrayList<Pair<RssItem, RssChannel>>
    var currentPage:Int=1
     var searchQuery:String=""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
//            param1 = it.getString(ARG_PARAM1)
//            param2 = it.getString(ARG_PARAM2)
        }
        postsList  = ArrayList()
        currentPage =1
        viewModel = ViewModelProvider(this).get(FeedFragmentViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v= inflater.inflate(R.layout.fragment_search, container, false)
        loadingIndicator = v.findViewById(R.id.search_loading_indicator)
        postsRV = v.findViewById(R.id.search_posts_rv)
        adapter = PostsAdapter(postsList,this,this,this)
        postsRV.layoutManager = LinearLayoutManager(context)
        postsRV.adapter = adapter

        val searchView: SearchView = v.findViewById(R.id.articles_search_view)


        searchView.setOnQueryTextListener(object:SearchView.OnQueryTextListener{
            var timer: Timer? = null

            override fun onQueryTextSubmit(query: String?): Boolean {
                Log.d("searchQR","submitted")
                if (query?.trim()?.isEmpty()!=true){
                    Log.d("query",query.toString())
//                    timer?.cancel()
//                    timer  = Timer()
//                    timer?.schedule(object: TimerTask(){
//                        override fun run() {
//                            Log.d("querySearched",query.toString())
//                            CoroutineScope(Dispatchers.Main).launch {
//                                searchArticles(query.toString())
//                            }
//
//                        }
//
//                    },0)
                    postsList.clear()
                    adapter.update(postsList)
                    searchQuery = query.toString()
                    searchArticles(searchQuery)
                }
                else{
                    Log.d("query","empty")
                    postsList.clear()
                    adapter.update(postsList)
                }
                return true
            }


            override fun onQueryTextChange(newText: String?): Boolean {
               return false
            }

        })



        return v
    }

    private fun searchArticles(query: String) {

            viewModel.getSearchFeed(query).observe(viewLifecycleOwner,{
                when(it.status){
                    MyResponse.Status.LODING->{
                       postsList.clear()
                        adapter.update(postsList)
                        Log.d("search-posts","loading")
                        loadingIndicator.visibility =View.VISIBLE
                    }

                    MyResponse.Status.SUCCESS -> {
                        postsList.clear()
                        adapter.update(postsList)
                        loadingIndicator.visibility =View.GONE
                        val channel = it.data
                        it.data?.items?.forEach {
                            Log.d("search-posts",it.title.toString())
                            postsList.add(Pair(it,channel) as Pair<RssItem, RssChannel>)
                        }

                        adapter.update(postsList)
                        Log.d("search-posts","success")
                    }

                    MyResponse.Status.ERROR -> {
                        Log.d("search-posts","error")
                        loadingIndicator.visibility =View.GONE
                        showErrorDialog()
                       // Toast.makeText(context,"Error-${it.message}",Toast.LENGTH_SHORT).show()
                    }
                }
            })


    }

    fun loadMorePosts(query:String,loadingIndicator: CircularProgressIndicator, loadMoreBtn: MaterialButton) {

//        viewModel.getMoreSearchResults(query,++currentPage).observe(viewLifecycleOwner,{
//
//            when(it.status){
//                MyResponse.Status.LODING->{
//                    Log.d("posts","loading")
//                    loadingIndicator.visibility =View.VISIBLE
//                    loadMoreBtn.visibility = View.GONE
//                }
//                MyResponse.Status.SUCCESS -> {
//                    Log.d("posts","success")
//
//                    loadingIndicator.visibility =View.GONE
//                    loadMoreBtn.visibility = View.VISIBLE
//                    val channel = it.data
////                       postsList.clear()
////                       adapter.update(postsList)
////                       it.data?.items?.forEach {
////                           Log.d("posts","added")
////                           postsList.add(Pair(it,channel) as Pair<RssItem, RssChannel>)
////                       }
//
//                    val newItems = channel?.items?.filterNot { item ->
//                        postsList.any { it.first.link == item.link }
//                    }
//                    newItems?.forEach { item ->
//                        postsList.add(Pair(item, channel))
//                    }
//
//                    adapter.update(postsList)
//                }
//                MyResponse.Status.ERROR -> {
//                    Log.d("posts","error")
//                    loadingIndicator.visibility =View.GONE
//                    loadMoreBtn.visibility = View.VISIBLE
//
//                    Toast.makeText(context,"Error-${it.message}",Toast.LENGTH_SHORT).show()
//                }
//            }
//        })

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.getMoreSearchResultsState(query, ++currentPage).collect {
//                    when (it.status) {
//                        MyResponse.Status.LODING -> {
//                            Log.d("posts", "loading")
//                            loadingIndicator.visibility = View.VISIBLE
//                            loadMoreBtn.visibility = View.GONE
//                        }
//
//                        MyResponse.Status.SUCCESS -> {
//                            Log.d("posts", "success")
//
//                            loadingIndicator.visibility = View.GONE
//                            loadMoreBtn.visibility = View.VISIBLE
//                            val channel = it.data
////                       postsList.clear()
////                       adapter.update(postsList)
////                            it.data?.items?.forEach {
////                                Log.d("posts", "added")
////                                postsList.add(Pair(it, channel) as Pair<RssItem, RssChannel>)
////                            }
//
//                            val newItems = channel?.items?.filterNot { item ->
//                                postsList.any { it.first.link == item.link }
//                            }
//                            newItems?.forEach { item ->
//                                postsList.add(Pair(item, channel))
//                            }
//
//                            adapter.update(postsList)
//                        }
//
//                        MyResponse.Status.ERROR -> {
//                            Log.d("posts", "error")
//                            loadingIndicator.visibility = View.GONE
//                            loadMoreBtn.visibility = View.VISIBLE
//
//                            Toast.makeText(context, "Error-${it.message}", Toast.LENGTH_SHORT)
//                                .show()
//                        }
//                    }

                    when(it.status){
                        MyResponse.Status.LODING->{
                            Log.d("posts","loading")
                            loadingIndicator.visibility =View.VISIBLE
                            loadMoreBtn.visibility = View.GONE
                            loadMoreBtn.isClickable = false
                        }
                        MyResponse.Status.SUCCESS -> {
                            Log.d("posts","success")

                            loadingIndicator.visibility =View.GONE
                            loadMoreBtn.visibility = View.VISIBLE
                            val channel = it.data
//                       postsList.clear()
//                       adapter.update(postsList)
//                       it.data?.items?.forEach {
//                           Log.d("posts","added")
//                           postsList.add(Pair(it,channel) as Pair<RssItem, RssChannel>)
//                       }

                            val newItems = channel?.items?.filterNot { item ->
                                postsList.any { it.first.link == item.link }
                            }

                            newItems?.forEach { item ->
                                postsList.add(Pair(item, channel))
                            }

                            adapter.update(postsList)
                        }
                        MyResponse.Status.ERROR -> {
                            Log.d("posts","error")
                            loadingIndicator.visibility =View.GONE
                            loadMoreBtn.visibility = View.VISIBLE
                            showErrorDialog()
                           // Toast.makeText(context,"Error-${it.message}",Toast.LENGTH_SHORT).show()
                        }
                    }

                }
            }
        }
    }


    companion object {

        @JvmStatic
        fun newInstance() =
            SearchFragment().apply {
                arguments = Bundle().apply {
//                    putString(ARG_PARAM1, param1)
//                    putString(ARG_PARAM2, param2)
                }
            }
    }

    private var errorDialog: androidx.appcompat.app.AlertDialog? = null
    private fun showErrorDialog(){if (errorDialog == null) {
        Log.d("err dialog","called")
        errorDialog = MaterialAlertDialogBuilder(requireContext())
            .setTitle("An Error Occurred")
            .setMessage("Please check your internet connectivity and try again")
            .setNeutralButton("Retry") { dialog, which ->
                dialog.dismiss()

                searchArticles(searchQuery)

            }
            .setOnDismissListener {
                // Reset the dialog instance when dismissed
                errorDialog = null
            }
            .create()
    }

        errorDialog?.setCancelable(false)


        errorDialog?.show()

    }

    override fun onBookmarkBtnClicked(post: RssItem) {
        Log.d("bookmarkBtn","clicked")

    }

    override fun onPostItemClicked(
        title: String?,
        author: String?,
        desc: String?,
        content: String?,
        categories: List<String>?,
        pubDate: String?,
        image: String?,
        link:String?
    ) {


        activity?.supportFragmentManager?.beginTransaction()?.addToBackStack(null)
            ?.replace(R.id.frame, PostFragment.newInstance(title, author, desc, content, pubDate, categories,image))
            ?.commit()
    }

    override fun onLoadMoreClicked(
        paged: Int,
        loadingIndicator: CircularProgressIndicator,
        loadMoreBtn: MaterialButton
    ) {
        Log.d("loadmoresearch","clicked")
        loadMorePosts(searchQuery,loadingIndicator,loadMoreBtn)
    }
}