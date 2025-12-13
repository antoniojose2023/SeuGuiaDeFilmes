package br.com.antoniodev.seuguiadefilmes

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import br.com.antoniodev.seuguiadefilmes.api.RetrofitHelper
import br.com.antoniodev.seuguiadefilmes.databinding.ActivityFilmeDetalhesBinding
import br.com.antoniodev.seuguiadefilmes.util.Constante
import com.squareup.picasso.Picasso
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FilmeDetalhesActivity : AppCompatActivity() {
    private val binding by lazy { ActivityFilmeDetalhesBinding.inflate(layoutInflater)}

    private var idFilme: Int? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.tvVoltar.setOnClickListener {
              startActivity(Intent(this, MainActivity::class.java))
              overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        }


        val bundle = intent.extras
        if(bundle != null){
               idFilme = bundle.getInt("id")
               if(idFilme != null){

                        getDetalhesFilme( idFilme!! )

               }
        }


    }

    @SuppressLint("SetTextI18n")
    fun getDetalhesFilme(idFilme: Int){

        CoroutineScope(Dispatchers.IO).launch {

            val response = RetrofitHelper(this@FilmeDetalhesActivity).apiFilmeTMDB().getDetalhesFilme(idFilme)
            try{

                if(response.isSuccessful && response.body() != null){
                       val detalheFilme = response.body()
                       if(detalheFilme != null){
                              binding.tvTituloFilmeDetalhes.text = detalheFilme.title
                              binding.tvDescricaoFilme.text = "Sinopse : ${detalheFilme.overview}"
                              binding.tvAvaliacao.text = "Avaliação 2 : ${detalheFilme.adult}"

                              val urlImagemFilme = "${Constante.URL_BASE_IMAGEM}" + "w500/" + "${detalheFilme.poster_path}"
                              if(urlImagemFilme != null){
                                  Picasso.get().load( urlImagemFilme )
                                      //.placeholder(R.drawable.posterhorizontal)
                                      .into(binding.ivFilmeDetalhes)
                              }
                       }
                }

            }catch (ex: Exception){
                Log.i("TAG", "getDetalhesFilme: $ex")
            }

        }

    }


}