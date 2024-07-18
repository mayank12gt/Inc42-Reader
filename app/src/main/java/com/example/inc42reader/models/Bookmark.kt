package com.example.inc42reader.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.prof18.rssparser.model.RssItem

@Entity(tableName = "Bookmarks")
data class Bookmark(
    @PrimaryKey(autoGenerate = true)
    val id:Int?=null,
    @ColumnInfo(name = "title")
    val title:String?,
    @ColumnInfo(name = "description")
    val description:String?,
    @ColumnInfo(name = "content")
    val content:String?,
    @ColumnInfo(name = "guid")
    val guid: String?,
    @ColumnInfo(name = "author")
    val author: String?,
    @ColumnInfo(name = "link")
    val link: String?,
    @ColumnInfo(name = "pubDate")
    val pubDate: String?,
    @ColumnInfo(name = "image")
    val image: String?,
    @ColumnInfo(name = "categories")
    val categories: List<String>,

)

fun RssItemtoBookmark(rssitem:RssItem) :Bookmark{
    return Bookmark(
        title = rssitem.title,
        description = rssitem.description,
        author = rssitem.author,
        content = rssitem.content,
        pubDate = rssitem.pubDate,
        image = rssitem.image,
        link = rssitem.link,
        guid = rssitem.guid,
        categories = rssitem.categories
    )
}



