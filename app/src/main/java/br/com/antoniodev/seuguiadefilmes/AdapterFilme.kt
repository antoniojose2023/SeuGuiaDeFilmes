package br.com.antoniodev.seuguiadefilmes

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.com.antoniodev.seuguiadefilmes.databinding.ItemFilmeBinding
import br.com.antoniodev.seuguiadefilmes.model.filmepopular.Filme
import br.com.antoniodev.seuguiadefilmes.util.Constante
import com.squareup.picasso.Picasso
import kotlinx.coroutines.withContext

class AdapterFilme(val onclickFilme: (Filme) -> Unit= {}): RecyclerView.Adapter<AdapterFilme.ViewHolderFilme>() {

    var filmes: MutableList<Filme> = mutableListOf()

    override fun onCreateViewHolder( parent: ViewGroup, viewType: Int): ViewHolderFilme {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemFilmeBinding.inflate( layoutInflater, parent, false )
        return ViewHolderFilme(binding)
    }

    override fun onBindViewHolder(holder: ViewHolderFilme,  position: Int) {
          val filme = filmes[position]
          holder.bind( filme )
    }

    override fun getItemCount() = filmes.size

    inner class ViewHolderFilme(val binding: ItemFilmeBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(filme: Filme){

             with(binding){
                    tvNomeFilme.text = filme.title

                    if(filme.poster_path.isNotEmpty()){
                        val urlImagemFilme = "${Constante.URL_BASE_IMAGEM}" + "w500/" + "${filme.poster_path}"
                        Picasso.get().load( urlImagemFilme ).into(ivFilmeImagem)
                    }

                 itemView.setOnClickListener {
                        onclickFilme(filme)
                 }
             }

        }
    }

}