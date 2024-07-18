import retrofit2.Retrofit
import retrofit2.converter.simplexml.SimpleXmlConverterFactory

object RetrofitClient {
    private var retrofit: Retrofit? = null

    @Synchronized
    fun getRetrofit(): Retrofit {
        if (retrofit == null) {
            retrofit = Retrofit.Builder()
                .baseUrl("https://inc42.com/")
                .addConverterFactory(SimpleXmlConverterFactory.create())
                .build()
        }
        return retrofit!!
    }
}
