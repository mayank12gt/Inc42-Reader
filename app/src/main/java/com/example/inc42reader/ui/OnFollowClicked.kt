package com.example.inc42reader.ui

import com.example.inc42reader.models.Feed

interface OnFollowClicked {
    fun OnFollowClicked(feed: Feed, position:Int)
}