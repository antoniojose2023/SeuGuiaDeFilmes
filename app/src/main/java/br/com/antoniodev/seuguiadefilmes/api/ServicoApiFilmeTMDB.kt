package br.com.antoniodev.seuguiadefilmes.api

import br.com.antoniodev.seuguiadefilmes.model.filmeatual.FilmeAtual
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers

interface ServicoApiFilmeTMDB {

    //@Headers("Authorization:${RetrofitHelper.token}")
    @GET("latest")
    suspend fun getFilmeAtual(): Response<FilmeAtual>



}