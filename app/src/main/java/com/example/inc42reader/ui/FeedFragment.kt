package com.example.inc42reader.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.inc42reader.R

import com.example.inc42reader.adapters.PostsAdapter
import com.example.inc42reader.models.Feed
import com.example.inc42reader.models.Item
import com.example.inc42reader.utils.MyResponse
import com.example.inc42reader.viewmodels.FeedFragmentViewModel
import com.google.android.material.button.MaterialButton
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.progressindicator.CircularProgressIndicator
import com.prof18.rssparser.model.RssChannel
import com.prof18.rssparser.model.RssItem
import kotlinx.coroutines.launch


private const val ARG_FEED = "feed"



class FeedFragment : Fragment(), OnBookmarkBtnClicked, OnPostItemClicked, OnLoadMoreClicked {

    private var feed: Feed? = null
    lateinit var viewModel: FeedFragmentViewModel
    lateinit var loadingIndicator:CircularProgressIndicator
    lateinit var postsRV:RecyclerView
    lateinit var adapter:PostsAdapter
    lateinit var postsList: ArrayList<Pair<RssItem,RssChannel>>
    lateinit var rawRssItems: ArrayList<Item>
     var currentPage:Int=1


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("frag","oncreate")
        arguments?.let {
            feed = it.getSerializable(ARG_FEED) as Feed?

        }
        postsList  = ArrayList()
        rawRssItems = ArrayList()
        currentPage =1
        viewModel = ViewModelProvider(this).get(FeedFragmentViewModel::class.java)
    }

    override fun onResume() {
        super.onResume()
       // activity?.findViewById<MaterialToolbar>(R.id.topAppBar)?.title = "Inc 42-${feed?.title}"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

      val v=inflater.inflate(R.layout.fragment_feed, container, false)
//        val loadMoreBtn:MaterialButton = v.findViewById(R.id.load_more_btn)
        loadingIndicator = v.findViewById(R.id.posts_loading_indicator)
        postsRV = v.findViewById(R.id.posts_rv)
        adapter = PostsAdapter(postsList,this,this,this)
        postsRV.layoutManager = LinearLayoutManager(context)
        postsRV.adapter = adapter
        loadFeedPosts()

//        loadMoreBtn.setOnClickListener {
//            loadFeedPosts()
//        }

        return v
    }



    fun loadFeedPosts(){

        viewModel.getFeed(feed).observe(viewLifecycleOwner,{

               when(it.status){
                   MyResponse.Status.LODING->{
                       Log.d("posts","loading")
                       loadingIndicator.visibility =View.VISIBLE
                   }
                   MyResponse.Status.SUCCESS -> {
                       Log.d("posts","success")

                       loadingIndicator.visibility =View.GONE
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
                       showErrorDialog()
                       Log.d("posts","error")
                       loadingIndicator.visibility =View.GONE
                     //  Toast.makeText(context,"Error-${it.message}",Toast.LENGTH_SHORT).show()
                   }
               }
        })

    }

    fun loadMorePosts(loadingIndicator: CircularProgressIndicator, loadMoreBtn: MaterialButton){

//        viewModel.getMorePosts(feed,++currentPage).observe(viewLifecycleOwner,{
//
//            when(it.status){
//                MyResponse.Status.LODING->{
//                    Log.d("posts","loading")
//                    loadingIndicator.visibility =View.VISIBLE
//                    loadMoreBtn.visibility = View.GONE
//                    loadMoreBtn.isClickable = false
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
//
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
            repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.getMorePostsState(feed,++currentPage).collect{
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

                  //  Toast.makeText(context,"Error-${it.message}",Toast.LENGTH_SHORT).show()
                }
            }
                }
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

                    loadFeedPosts()

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




    override fun onDestroyView() {
        Log.d("frag","destroyed")
        super.onDestroyView()


    }

    override fun onDetach() {
        super.onDetach()
        Log.d("frag","destach")

        //activity?.findViewById<MaterialToolbar>(R.id.topAppBar)?.title = "Inc 42"
    }


    override fun onPause() {
        super.onPause()
        Log.d("frag","paused")

        //activity?.findViewById<MaterialToolbar>(R.id.topAppBar)?.title = "Inc 42"
    }

    companion object {

        @JvmStatic
        fun newInstance(feed: Feed) =
            FeedFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(ARG_FEED, feed)

                }
            }
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

        val rawItem = rawRssItems.find { it.link== link}
        Log.d("clicked raw item",rawItem?.title.toString())

        activity?.supportFragmentManager?.beginTransaction()?.setCustomAnimations(androidx.appcompat.R.anim.abc_slide_in_bottom,
            androidx.appcompat.R.anim.abc_slide_out_bottom, androidx.appcompat.R.anim.abc_slide_in_top,
            androidx.appcompat.R.anim.abc_slide_out_bottom)
            ?.add(R.id.frame, PostFragment.newInstance(title, author, desc, content, pubDate, categories,image))?.addToBackStack(null)
            ?.commit()
    }

    override fun onLoadMoreClicked(
        paged: Int,
        loadingIndicator: CircularProgressIndicator,
        loadMoreBtn: MaterialButton
    ) {
        loadMorePosts(loadingIndicator,loadMoreBtn)
    }
}