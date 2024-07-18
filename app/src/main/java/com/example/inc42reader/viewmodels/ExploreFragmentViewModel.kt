package com.example.inc42reader.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.inc42reader.models.Feed
import com.example.inc42reader.database.FeedsDB
import com.example.inc42reader.repo.Repo
import com.example.inc42reader.utils.MyResponse
import kotlinx.coroutines.launch

class ExploreFragmentViewModel(application: Application):AndroidViewModel(application) {

    val repo by lazy {
        Repo(FeedsDB.getDatabase(application))
    }
    val feedsListData:MutableLiveData<MyResponse<List<Feed>>> = MutableLiveData()

    val followedFeedsListData:MutableLiveData<MyResponse<List<Feed>>> = MutableLiveData()
    init {
        getAllFeeds()
    }

    fun getAllFeeds():MutableLiveData<MyResponse<List<Feed>>>{
        viewModelScope.launch{
            feedsListData.postValue(MyResponse.loading())
            repo.getAllFeeds().collect{
                feedsListData.postValue(MyResponse.success(data = it))
            }
        }
        return feedsListData
    }

    fun followFeed(feed: Feed){
        viewModelScope.launch {
            repo.FollowFeed(feed)
        }

    }

    fun getFollowedFeeds(): MutableLiveData<MyResponse<List<Feed>>> {
        viewModelScope.launch{
            followedFeedsListData.postValue(MyResponse.loading())
            repo.getFollowedFeeds().collect{
                followedFeedsListData.postValue(MyResponse.success(data = it))

            }
        }
        return followedFeedsListData
    }

    fun searchFeeds(query:String):MutableLiveData<MyResponse<List<Feed>>>{
        viewModelScope.launch {
            feedsListData.postValue(MyResponse.loading())
            repo.searchFeeds(query).collect({
                feedsListData.postValue(MyResponse.success(data = it))
            })
        }
        return feedsListData
    }



}