package com.example.inc42reader.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "Feeds")
data class Feed(@PrimaryKey(autoGenerate = true)
                val id:Int,
                @ColumnInfo(name = "title")
                val title:String,
                @ColumnInfo(name = "description")
                val description:String,
                @ColumnInfo(name = "url")
                val url:String,
                @ColumnInfo(name = "isFollowed")
                var isFollowed:Boolean=false) : Serializable
