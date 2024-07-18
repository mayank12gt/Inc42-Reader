package com.example.inc42reader.ui

import URLImageGetter
import android.os.Bundle
import android.text.Html
import android.text.method.LinkMovementMethod
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.TextView
import com.bumptech.glide.Glide
import com.example.inc42reader.R

import com.makeramen.roundedimageview.RoundedImageView
import org.jsoup.Jsoup
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


private const val ARG_TITLE = "title"
private const val ARG_AUTHOR= "author"
private const val ARG_DESC = "description"
private const val ARG_CONTENT = "content"
private const val ARG_CATEGORIES = "categories"
private const val ARG_PUB_DATE = "pub_date"
private const val ARG_IMAGE = "image"




class PostFragment : Fragment() {

    private var title: String? = null
    private var author: String? = null
    private var description: String? = null
    private var content: String? = null
    private var categories: ArrayList<String>? = null
    private var pubDate: String? = null
    private var image: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            title = it.getString(ARG_TITLE)
            author = it.getString(ARG_AUTHOR)
            description = it.getString(ARG_DESC)
            content = it.getString(ARG_CONTENT)
            pubDate = it.getString(ARG_PUB_DATE)
            categories = it.getStringArrayList(ARG_CATEGORIES)
            image = it.getString(ARG_IMAGE)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

       val v = inflater.inflate(R.layout.fragment_post, container, false)
        Log.d("title", title.toString())
        val txt= Jsoup.parse(content).text()
        val postTitle = v.findViewById<TextView>(R.id.post_title)
        val postAuthor = v.findViewById<TextView>(R.id.post_author)
        val postPubDate = v.findViewById<TextView>(R.id.post_pub_date)
        val postContent = v.findViewById<TextView>(R.id.post_content)
        val postImage  = v.findViewById<RoundedImageView>(R.id.post_image)

        Log.d("content",txt)
        Log.d("content",content!!)

        postTitle.text = title
        postAuthor.text = author
        postPubDate.text = formatDate(pubDate)
        postTitle.text = title
//        val imageGetter = context?.let { URLImageGetter(postContent, it) }
//        val spanned:Spanned = Html.fromHtml(content!!,HtmlCompat.FROM_HTML_MODE_LEGACY,imageGetter,null)
       postContent.movementMethod = LinkMovementMethod.getInstance()
//        postContent.text = spanned

        postContent.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                postContent.viewTreeObserver.removeOnGlobalLayoutListener(this)

                val imageGetter = context?.let { URLImageGetter(postContent, it) }
                val spanned = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                    Html.fromHtml(content, Html.FROM_HTML_MODE_LEGACY, imageGetter, null)
                } else {
                    Html.fromHtml(content, imageGetter, null)
                }

                postContent.text = spanned
            }
        })


//        val spannedDesc:Spanned = Html.fromHtml(content!!,HtmlCompat.FROM_HTML_MODE_LEGACY,imageGetter,null)
//        postContent.text = spanned
        context?.let {
            Glide.with(it).load(image)
    //                .placeholder(R.drawable.ic_baseline_image_24)
    //                .error(R.drawable.ic_baseline_image_24)
                .into(postImage)
        }




        return v
    }

    companion object {

        @JvmStatic
        fun newInstance(title:String?, author:String?, desc:String?, content:String?, pubDate:String?, categories:List<String>?,image:String?) =
            PostFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_TITLE, title)
                    putString(ARG_AUTHOR, author)
                    putString(ARG_DESC,desc)
                    putString(ARG_PUB_DATE, pubDate)
                    putString(ARG_CONTENT, content)
                    putString(ARG_IMAGE, image)
                    putStringArrayList(ARG_CATEGORIES,ArrayList( categories))

                }
            }
    }

    fun formatDate(pubDate:String?): String {
        val inputFormat = SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.ENGLISH)
        val outputFormat = SimpleDateFormat("dd MMM, yyyy", Locale.ENGLISH)
        val date: Date = inputFormat.parse(pubDate)
        return outputFormat.format(date)
    }
}