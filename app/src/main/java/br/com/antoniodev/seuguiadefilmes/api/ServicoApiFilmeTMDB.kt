package br.com.antoniodev.seuguiadefilmes.api

import br.com.antoniodev.seuguiadefilmes.model.filmeatual.FilmeAtual
import br.com.antoniodev.seuguiadefilmes.model.filmedetalhes.FilmeDetalhe
import br.com.antoniodev.seuguiadefilmes.model.filmepopular.RespostaFilme
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path

interface ServicoApiFilmeTMDB {

    //@Headers("Authorization:${RetrofitHelper.token}")
    @GET("latest")
    suspend fun getFilmeAtual(): Response<FilmeAtual>

    @GET("popular")
    suspend fun getFilmes(): Response<RespostaFilme>

    @GET("top_rated")
    suspend fun getFilmesTopAvaliados(): Response<RespostaFilme>

    @GET("{id}")
    suspend fun getDetalhesFilme(@Path("id") idFilme: Int): Response<FilmeDetalhe>

}