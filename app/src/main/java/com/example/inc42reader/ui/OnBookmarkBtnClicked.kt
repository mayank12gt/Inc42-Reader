package com.example.inc42reader.ui

import com.prof18.rssparser.model.RssItem

interface OnBookmarkBtnClicked {
    fun onBookmarkBtnClicked(post:RssItem)
}