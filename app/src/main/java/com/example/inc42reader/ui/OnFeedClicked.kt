package com.example.inc42reader.ui

import com.example.inc42reader.models.Feed

interface OnFeedClicked {

    fun onFeedClicked(feed:Feed, position:Int)
}