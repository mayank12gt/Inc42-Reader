package com.example.inc42reader.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.inc42reader.models.Feed
import com.prof18.rssparser.model.RssItem
import kotlinx.coroutines.flow.Flow

@Dao
interface BookmarksDAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addBookmark(post:RssItem)

    @Query("select * from Feeds order by isFollowed DESC ")
    fun getAllFeeds(): Flow<List<Feed>>

    @Query("select * from Feeds where isFollowed = 1")
    fun getFollowedFeeds(): Flow<List<Feed>>

    @Update
    suspend fun updateFeed(feed: Feed)

    @Query("select *,INSTR(LOWER(title), LOWER(:query)) AS position from Feeds where  title LIKE '%' || :query || '%'  order by position")
    fun searchTags(query: String?): Flow<List<Feed>>
}