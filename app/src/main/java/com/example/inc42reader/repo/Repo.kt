package com.example.inc42reader.repo


import com.example.inc42reader.models.Feed
import com.example.inc42reader.database.FeedsDB
import kotlinx.coroutines.flow.Flow


class Repo(val db: FeedsDB) {


    suspend fun getAllFeeds(): Flow<List<Feed>> {
        return db.getDao().getAllFeeds()
    }

    suspend fun getFollowedFeeds(): Flow<List<Feed>> {
        return db.getDao().getFollowedFeeds()
    }

    suspend fun FollowFeed(feed: Feed) {
       db.getDao().updateFeed(feed)
    }

    suspend fun searchFeeds(query:String): Flow<List<Feed>> {
        return db.getDao().searchTags(query)
    }









}