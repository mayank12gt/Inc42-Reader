package com.example.inc42reader.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.inc42reader.models.Feed
import com.example.inc42reader.ui.OnFeedClicked
import com.example.inc42reader.ui.OnFollowClicked
import com.google.android.material.button.MaterialButton
import com.example.inc42reader.R

class FeedsAdapter(var feedsList: List<Feed>,var onFeedClicked: OnFeedClicked,var onFollowClicked: OnFollowClicked): RecyclerView.Adapter<FeedsAdapter.viewholder>() {

    class viewholder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var feedTitle = itemView.findViewById<TextView>(R.id.feed_title)
        var feedDesc = itemView.findViewById<TextView>(R.id.feed_desc)
        var followBtn = itemView.findViewById<MaterialButton>(R.id.follow_btn)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewholder {
        return viewholder(LayoutInflater.from(parent.context).inflate(R.layout.feed_item,parent,false))
    }

    override fun getItemCount(): Int {
        return feedsList.size
    }

    override fun onBindViewHolder(holder: viewholder, position: Int) {
        val feed = feedsList.get(position)
        holder.feedTitle.text = feed.title
        holder.feedDesc.text = feed.description

        if(feed.isFollowed){
            holder.followBtn.text = "Followed"
            holder.followBtn.icon =ContextCompat.getDrawable(holder.itemView.context, R.drawable.followed_icon)

        }
        else{
            holder.followBtn.text = "Follow"
            holder.followBtn.icon =ContextCompat.getDrawable(holder.itemView.context, R.drawable.follow_icon)

        }

        holder.itemView.setOnClickListener {
            onFeedClicked.onFeedClicked(feed, position)
        }
        holder.followBtn.setOnClickListener {
            onFollowClicked.OnFollowClicked(feed, position)
        }
    }

    fun update(feeds:List<Feed>){
        this.feedsList = feeds
        notifyDataSetChanged()
    }
}