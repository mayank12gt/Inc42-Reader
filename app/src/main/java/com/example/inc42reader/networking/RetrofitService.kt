import com.example.inc42reader.models.RawRssFeed
import retrofit2.Call
import retrofit2.http.GET

interface RetrofitService {
    @GET("buzz/feed/")
    fun getRssFeed(): Call<RawRssFeed>
}
