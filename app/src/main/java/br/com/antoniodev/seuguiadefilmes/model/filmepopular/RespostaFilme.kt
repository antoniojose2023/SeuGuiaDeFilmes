package br.com.antoniodev.seuguiadefilmes.model.filmepopular

data class RespostaFilme(
    val page: Int,
    val results: List<Filme>,
    val total_pages: Int,
    val total_results: Int
)