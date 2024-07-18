package com.example.inc42reader.ui

import com.google.android.material.button.MaterialButton
import com.google.android.material.progressindicator.CircularProgressIndicator

interface OnLoadMoreClicked {
    fun onLoadMoreClicked(paged:Int,loadingIndicator: CircularProgressIndicator,loadMoreBtn: MaterialButton)
}