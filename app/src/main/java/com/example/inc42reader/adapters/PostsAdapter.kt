package com.example.inc42reader.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.inc42reader.ui.OnBookmarkBtnClicked
import com.example.inc42reader.ui.OnLoadMoreClicked
import com.example.inc42reader.ui.OnPostItemClicked
import com.google.android.material.button.MaterialButton
import com.google.android.material.progressindicator.CircularProgressIndicator
import com.makeramen.roundedimageview.RoundedImageView
import com.example.inc42reader.R
import com.prof18.rssparser.model.RssChannel
import com.prof18.rssparser.model.RssItem
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class PostsAdapter(var postsList:List<Pair<RssItem,RssChannel>>,var onPostItemClicked: OnPostItemClicked, var onBookmarkBtnClicked: OnBookmarkBtnClicked, var onLoadMoreClicked: OnLoadMoreClicked): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val VIEW_TYPE_ITEM = 0
        private const val VIEW_TYPE_LOAD_MORE = 1
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == postsList.size) VIEW_TYPE_LOAD_MORE else VIEW_TYPE_ITEM
    }


    class viewholder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val postImage:RoundedImageView = itemView.findViewById(R.id.post_Image)
        val postTitle:TextView = itemView.findViewById(R.id.post_title)
        val postAuthor:TextView = itemView.findViewById(R.id.post_author)
        val postPubDate:TextView = itemView.findViewById(R.id.post_pub_date)
        val bookmarkBtn:ImageView = itemView.findViewById(R.id.bookmark_btn)
    }

    class buttonviewholder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val loadMoreBtn:MaterialButton = itemView.findViewById(R.id.load_more_btn)
        val loadMoreProgressIndicator:CircularProgressIndicator = itemView.findViewById(R.id.posts_loading_more_indicator)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType== VIEW_TYPE_ITEM){
            return viewholder(LayoutInflater.from(parent.context).inflate(R.layout.feed_post_item,parent,false))

        }else{
            return buttonviewholder(LayoutInflater.from(parent.context).inflate(R.layout.layout_button ,parent,false))

        }
    }

    override fun getItemCount(): Int {
        return postsList.size+1
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is viewholder) {
            val post = postsList.get(position)
            if (post.first.image != null) {
                Glide.with(holder.itemView.context).load(post.first.image)
             .placeholder(R.drawable.image_placeholder)
               .error(R.drawable.image_placeholder)
                    .into(holder.postImage)
            }
            holder.postTitle.text = post.first.title
            holder.postAuthor.text = post.first.author
            holder.postPubDate.text = formatDate(post.first.pubDate)
            holder.bookmarkBtn.setOnClickListener {
                onBookmarkBtnClicked.onBookmarkBtnClicked(post.first)
            }



            holder.itemView.setOnClickListener {
                onPostItemClicked.onPostItemClicked(
                    title = post.first.title,
                    author = post.first.author,
                    desc = post.first.description,
                    content = post.first.content,
                    pubDate = post.first.pubDate,
                    categories = post.first.categories,
                    image = post.first.image,
                    link = post.first.link
                )
            }
        }
        else if(holder is buttonviewholder){
            if (postsList.isEmpty()){
                holder.loadMoreBtn.visibility = View.GONE
            }else{
                holder.loadMoreBtn.visibility = View.VISIBLE
            }
            holder.loadMoreBtn.setOnClickListener {
                Log.d("load_more","clicked")
                onLoadMoreClicked.onLoadMoreClicked(2, holder.loadMoreProgressIndicator,holder.loadMoreBtn)
            }

        }
    }

    fun update(postsList: List<Pair<RssItem, RssChannel>>){
        this.postsList = postsList
        notifyDataSetChanged()
    }
    fun formatDate(pubDate:String?): String {
        val inputFormat = SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.ENGLISH)
        val outputFormat = SimpleDateFormat("dd MMM, yyyy", Locale.ENGLISH)
        val date: Date = inputFormat.parse(pubDate)
        return outputFormat.format(date)
    }
}