package com.example.inc42reader.viewmodels

import RetrofitService
import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.inc42reader.models.Feed
import com.example.inc42reader.models.RawRssFeed
import com.example.inc42reader.utils.MyResponse
import com.prof18.rssparser.RssParserBuilder
import com.prof18.rssparser.model.RssChannel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import okhttp3.Cache
import okhttp3.CacheControl
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Call
import java.io.File
import java.util.concurrent.TimeUnit

class FeedFragmentViewModel(application: Application):AndroidViewModel(application) {

    val rssData:MutableLiveData<MyResponse<RawRssFeed>> = MutableLiveData()


    private val feedData:MutableLiveData<MyResponse<RssChannel>> = MutableLiveData()
    private val morePostsData:MutableLiveData<MyResponse<RssChannel>> = MutableLiveData()
    private val morePostsState:MutableStateFlow<MyResponse<RssChannel>> = MutableStateFlow(
        MyResponse.loading())

    private val searchFeedData:MutableLiveData<MyResponse<RssChannel>> = MutableLiveData()
    private val moreSearchResultsData:MutableLiveData<MyResponse<RssChannel>> = MutableLiveData()

    private val moreSearchResultsState:MutableStateFlow<MyResponse<RssChannel>> = MutableStateFlow(
        MyResponse.loading())


    val cacheSize = (5 * 1024 * 1024).toLong()
    val myCache = Cache(application.cacheDir, cacheSize)
    fun getClient():OkHttpClient{
//        val client = OkHttpClient.Builder()
//            .cache(myCache)
//            .addNetworkInterceptor { chain ->
//                var request = chain.request()
////                request.newBuilder().header("Cache-Control", "public, max-age=" + 300).build()
////                chain.proceed(request)
//                request
//                    .newBuilder()
//                    .cacheControl(
//                        CacheControl.Builder()
//                            .maxAge(30, TimeUnit.MINUTES)
//                            .build()
//                    ).build()
//                chain.proceed(request)
//            }.addNetworkInterceptor { chain ->
//                val response = chain.proceed(chain.request())
//
//                response.cacheResponse?.let {
//                    Log.d("Cache", "Response from cache")
//                } ?: Log.d("Cache", "Response from network")
//
//                response
//            }.build()

        val client = OkHttpClient().newBuilder()
            .cache(Cache(File(getApplication<Application>().cacheDir, "http-cache"), 10L * 1024L * 1024L)) // 10 MiB
            .addNetworkInterceptor(CacheInterceptor())
            .build();

        return client
    }





    fun getFeed(feed: Feed?, paged: Int?=1): MutableLiveData<MyResponse<RssChannel>> {
        val parser = RssParserBuilder(
            callFactory = getClient(),
        ).build()


        feedData.postValue(MyResponse.loading())
        Log.d("live-data","posted-1")

        viewModelScope.launch {

            try {
                Log.d("url",feed?.url.toString())

               val rssChannel = parser.getRssChannel(feed?.url.toString()+"?paged=${paged}")
                //val rssChannel = parser.parse(feed2)
                feedData.postValue(MyResponse.success(rssChannel))

                Log.d("live-data","posted-2")

            }
            catch (exception:Exception){
                Log.d("err",exception.localizedMessage)
                Log.d("err",exception.stackTraceToString())
                feedData.postValue(MyResponse.error(exception.localizedMessage))
            }


        }
        Log.d("live-data","returned-1")

        return feedData
    }


    fun getMorePosts(feed: Feed?, paged: Int?=1): MutableLiveData<MyResponse<RssChannel>> {
        val parser = RssParserBuilder(
            callFactory = getClient(),
        ).build()


        morePostsData.postValue(MyResponse.loading())
        Log.d("live-data","posted-1")

        viewModelScope.launch {

            try {
                Log.d("url",feed?.url.toString())

                val rssChannel = parser.getRssChannel(feed?.url.toString()+"?paged=${paged}")
                //val rssChannel = parser.parse(feed2)
                morePostsData.postValue(MyResponse.success(rssChannel))

                Log.d("live-data","posted-2")

            }
            catch (exception:Exception){
                Log.d("err",exception.localizedMessage)
                Log.d("err",exception.stackTraceToString())
                morePostsData.postValue(MyResponse.error(exception.localizedMessage))
            }


        }
        Log.d("live-data","returned-1")

        return morePostsData
    }


    fun getMorePostsState(feed: Feed?, paged: Int?=1): MutableStateFlow<MyResponse<RssChannel>> {
        val parser = RssParserBuilder(
            callFactory = getClient(),
        ).build()


        morePostsState.value = MyResponse.loading()
        Log.d("live-data","posted-1")

        viewModelScope.launch {

            try {
                Log.d("url",feed?.url.toString())

                val rssChannel = parser.getRssChannel(feed?.url.toString()+"?paged=${paged}")
                //val rssChannel = parser.parse(feed2)
                morePostsState.value = MyResponse.success(rssChannel)

                Log.d("live-data","posted-2")

            }
            catch (exception:Exception){
                Log.d("err",exception.localizedMessage)
                Log.d("err",exception.stackTraceToString())
                morePostsState.value = MyResponse.error(exception.localizedMessage)
            }


        }
        Log.d("live-data","returned-1")

        return morePostsState
    }

    fun getMoreSearchResultsState(query: String?,paged:Int=1): MutableStateFlow<MyResponse<RssChannel>> {
        val parser = RssParserBuilder(
            callFactory = getClient(),
        ).build()


        moreSearchResultsState.value= MyResponse.loading()


        viewModelScope.launch {
            try {
                Log.d("query",query.toString())
                Log.d("searchFn","called")

                val rssChannel = parser.getRssChannel("https://inc42.com/feed?s=${query}&paged=${paged}")
                //val rssChannel = parser.parse(feed2)
                moreSearchResultsState.value = MyResponse.success(rssChannel)
            }
            catch (exception:Exception){
                Log.d("err",exception.localizedMessage)
                Log.d("err",exception.stackTraceToString())
                moreSearchResultsState.value = MyResponse.error(exception.localizedMessage)
            }


        }
        return moreSearchResultsState
    }




    fun getSearchFeed(query: String?,paged:Int=1): MutableLiveData<MyResponse<RssChannel>> {
        val parser = RssParserBuilder(
            callFactory = getClient(),
        ).build()




        viewModelScope.launch {
            searchFeedData.postValue(MyResponse.loading())
            try {
                Log.d("query",query.toString())
                Log.d("searchFn","called")

                val rssChannel = parser.getRssChannel("https://inc42.com/feed?s=${query}&paged=${paged}")
                //val rssChannel = parser.parse(feed2)
                searchFeedData.postValue(MyResponse.success(rssChannel))
            }
            catch (exception:Exception){
                Log.d("err",exception.localizedMessage)
                Log.d("err",exception.stackTraceToString())
                searchFeedData.postValue(MyResponse.error(exception.localizedMessage))
            }


        }
        return searchFeedData
    }


    fun getMoreSearchResults(query: String?,paged:Int=1): MutableLiveData<MyResponse<RssChannel>> {
        val parser = RssParserBuilder(
            callFactory = getClient(),
        ).build()




        viewModelScope.launch {
            moreSearchResultsData.postValue(MyResponse.loading())
            try {
                Log.d("query",query.toString())
                Log.d("searchFn","called")

                val rssChannel = parser.getRssChannel("https://inc42.com/feed?s=${query}&paged=${paged}")
                //val rssChannel = parser.parse(feed2)
                moreSearchResultsData.postValue(MyResponse.success(rssChannel))
            }
            catch (exception:Exception){
                Log.d("err",exception.localizedMessage)
                Log.d("err",exception.stackTraceToString())
                moreSearchResultsData.postValue(MyResponse.error(exception.localizedMessage))
            }


        }
        return moreSearchResultsData
    }







    fun getRawRss(): LiveData<MyResponse<RawRssFeed>> {
        rssData.postValue(MyResponse.loading())
         viewModelScope.launch {

             RetrofitClient.getRetrofit().create(RetrofitService::class.java).getRssFeed()
                 .enqueue(object : retrofit2.Callback<RawRssFeed> {
                     override fun onResponse(
                         call: Call<RawRssFeed>,
                         response: retrofit2.Response<RawRssFeed>
                     ) {
                         if (response.isSuccessful) {
                             val rssFeed = response.body()
                             Log.d("raw rss", rssFeed.toString())
                             rssData.postValue(MyResponse.success(data = rssFeed) as MyResponse<RawRssFeed>?)

                         }
                     }

                     override fun onFailure(call: Call<RawRssFeed>, t: Throwable) {
                         rssData.postValue(MyResponse.error(t.toString()))
                     }
                 })
         }

        return rssData
    }


}

class CacheInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val response: Response = chain.proceed(chain.request())
        val cacheControl = CacheControl.Builder()
            .maxAge(5, TimeUnit.MINUTES)
            .build()
        return response.newBuilder()
            .header("Cache-Control", cacheControl.toString())
            .build()
    }
}
