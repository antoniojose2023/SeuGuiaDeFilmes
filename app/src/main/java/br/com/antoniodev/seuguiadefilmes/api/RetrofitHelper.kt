package br.com.antoniodev.seuguiadefilmes.api

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.core.os.BuildCompat
import br.com.antoniodev.seuguiadefilmes.util.AcessToken
import br.com.antoniodev.seuguiadefilmes.util.TokenStorage
import com.google.gson.GsonBuilder
import com.google.gson.internal.GsonBuildConfig
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class RetrofitHelper(context: Context) {
     val BASE_URL = "https://api.themoviedb.org/3/movie/"
     val tokenStorage = TokenStorage(context)

    val okHttpClient = OkHttpClient.Builder()
        .readTimeout(3000, TimeUnit.MILLISECONDS)
        .writeTimeout(3000, TimeUnit.MILLISECONDS)
        .connectTimeout(5000, TimeUnit.MILLISECONDS)
        .addInterceptor(IntercpterFilmeTMDB())
        .build()

     private val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build()

     fun apiFilmeTMDB(): ServicoApiFilmeTMDB {
         return retrofit.create(ServicoApiFilmeTMDB::class.java)
     }

    inner class IntercpterFilmeTMDB(): Interceptor{
        override fun intercept(chain: Interceptor.Chain): Response {

           tokenStorage.saveToken(AcessToken.getBaererToken())
           val token = tokenStorage.getToken()


            val request = chain.request().newBuilder()
                .addHeader("Authorization", "Bearer $token")
                .build()

            return chain.proceed( request )
        }

    }

}


