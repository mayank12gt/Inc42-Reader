package com.example.inc42reader.ui

interface OnPostItemClicked {
    fun onPostItemClicked(title:String?, author:String?,desc:String?,content:String?, categories:List<String>?
    ,pubDate:String?,image:String?,link:String?)
}