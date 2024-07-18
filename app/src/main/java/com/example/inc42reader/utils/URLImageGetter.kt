import android.content.Context
import android.graphics.drawable.Drawable
import android.text.Html
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition

class URLImageGetter(private val textView: TextView, private val context: Context) : Html.ImageGetter {

    override fun getDrawable(source: String): Drawable {
        val urlDrawable = URLDrawable()

        Glide.with(context)
            .load(source)
            .into(object : CustomTarget<Drawable>() {
                override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable>?) {
                    // Fit the image to the width of the TextView while maintaining aspect ratio
                    val aspectRatio = resource.intrinsicWidth.toFloat() / resource.intrinsicHeight
                    val width = textView.width
                    val height = (width / aspectRatio).toInt()

                    resource.setBounds(0, 0, width, height)
                    urlDrawable.setBounds(0, 0, width, height)
                    urlDrawable.drawable = resource

                    textView.text = textView.text // Refresh the text
                    textView.invalidate() // Invalidate to trigger re-draw
                }

                override fun onLoadCleared(placeholder: Drawable?) {
                    // Handle placeholder if needed
                }
            })

        return urlDrawable
    }

    private inner class URLDrawable : Drawable() {
        var drawable: Drawable? = null

        override fun draw(canvas: android.graphics.Canvas) {
            drawable?.draw(canvas)
        }

        override fun setAlpha(alpha: Int) {
            drawable?.alpha = alpha
        }

        override fun setColorFilter(colorFilter: android.graphics.ColorFilter?) {
            drawable?.colorFilter = colorFilter
        }

        override fun getOpacity(): Int {
            return drawable?.opacity ?: android.graphics.PixelFormat.UNKNOWN
        }
    }
}
