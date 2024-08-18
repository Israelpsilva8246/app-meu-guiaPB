import com.ufpb.meuguia.model.Attraction
import com.ufpb.meuguia.model.AttractionType
import com.ufpb.meuguia.model.TurismSegmentation
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import java.util.concurrent.TimeUnit

interface ApiService {

    @GET("api/turists")
    suspend fun getAttractions(): List<Attraction>

    @GET("api/turists/byName")
    suspend fun getAttractionByName(@Query("name") name: String): List<Attraction>

    @GET("api/turists/bySegmentations")
    suspend fun getAttractionBySegmentations(@Query("segmentations") segmentations: String): List<Attraction>

    @GET("api/turists/byCity")
    suspend fun getAttractionByCity(@Query("city") city: String): List<Attraction>

    @GET("api/turists/byType")
    suspend fun getAttractionByType(@Query("attractionTypes") attractionTypes: String): List<Attraction>

    @GET("api/types")
    suspend fun getAttractionTypes(): List<AttractionType>

    @GET("api/turists")
    suspend fun getAttractionName(): List<Attraction>

    @GET("api/segmentations")
    suspend fun getAttractionSeg(): List<TurismSegmentation>

    companion object {
        private const val BASE_URL = "https://0334-190-111-131-121.ngrok-free.app/"
        private const val TIMEOUT_SECONDS = 30L

        private var apiService: ApiService? = null

        fun getInstance(): ApiService {
            if (apiService == null) {
                val okHttpClient = OkHttpClient.Builder()
                    .connectTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
                    .readTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
                    .writeTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
                    .build()

                apiService = Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(okHttpClient)
                    .build().create(ApiService::class.java)
            }
            return apiService!!
        }
    }
}
