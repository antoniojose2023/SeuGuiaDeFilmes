package br.com.antoniodev.seuguiadefilmes

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.com.antoniodev.seuguiadefilmes.api.RetrofitHelper
import br.com.antoniodev.seuguiadefilmes.databinding.ActivityMainBinding
import br.com.antoniodev.seuguiadefilmes.model.filmepopular.Filme
import br.com.antoniodev.seuguiadefilmes.util.Constante
import com.squareup.picasso.Picasso
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private lateinit var adapterFilme: AdapterFilme

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        adapterFilme = AdapterFilme{filme ->
               val intent = Intent(this, FilmeDetalhesActivity::class.java)
               intent.putExtra( "id", filme.id )
               startActivity(intent)
        }

//        getFilmeAtual()
//        getFilmes()
//        getFilmesTopAvaliados()
//        configRecyclerViewFilmesPopulares()
//        configRecyclerViewFilmesTopAvaliados()

    }


    override fun onResume() {
        getFilmeAtual()
        getFilmes()
        getFilmesTopAvaliados()
        configRecyclerViewFilmesPopulares()
        configRecyclerViewFilmesTopAvaliados()
        super.onResume()
    }

    fun getFilmeAtual(){
        CoroutineScope(Dispatchers.IO).launch {

            try{
                val response = RetrofitHelper(this@MainActivity).apiFilmeTMDB().getFilmeAtual()
                if(response.isSuccessful && response.body() != null){
                    val filmeAtual = response.body()
                    if(filmeAtual != null){
                        withContext(Dispatchers.Main){
                            binding.tvNomeFilmeAtual.text = filmeAtual.title

                            val urlImagemFilme = "${Constante.URL_BASE_IMAGEM}" + "w500/" + "${filmeAtual.poster_path}"
                            Picasso.get().load(urlImagemFilme)
                                .placeholder(R.drawable.posterhorizontal)
                                .into( binding.ivFilmeDestaqueAtual )
                        }
                    }
                }else{
                    Log.i("TAG", "onCreate: erro na chamada interna")
                }

            }catch (ex: Exception){
                Log.i("TAG", "onCreate: erro na chamada externa : "+ex.message)
            }
        }
    }


    @SuppressLint("NotifyDataSetChanged")
    fun getFilmes(){

        CoroutineScope(Dispatchers.IO).launch {
            val response = RetrofitHelper(this@MainActivity).apiFilmeTMDB().getFilmes()
            try{

                if(response.isSuccessful && response.body() != null){
                          val filmes = response.body()?.results
                          if(filmes!!.isNotEmpty()){
                                 //adapterFilme.filmes.clear()
                                 adapterFilme.filmes = filmes as MutableList<Filme>
                                 adapterFilme.notifyDataSetChanged()
                          }
                }else{
                    Log.i("TAG", "onCreate: erro na chamada interna")
                }

            }catch (ex: Exception){
                Log.i("TAG", "onCreate: erro na chamada externa ${ex.message}")
            }
        }

    }

    @SuppressLint("NotifyDataSetChanged")
    fun getFilmesTopAvaliados(){

        CoroutineScope(Dispatchers.IO).launch {
            val response = RetrofitHelper(this@MainActivity).apiFilmeTMDB().getFilmesTopAvaliados()
            try{

                if(response.isSuccessful && response.body() != null){
                    val filmes = response.body()?.results
                    if(filmes!!.isNotEmpty()){
                        //adapterFilme.filmes.clear()
                        adapterFilme.filmes = filmes as MutableList<Filme>
                        adapterFilme.notifyDataSetChanged()
                    }
                }else{
                    Log.i("TAG", "onCreate: erro na chamada interna")
                }

            }catch (ex: Exception){
                Log.i("TAG", "onCreate: erro na chamada externa ${ex.message}")
            }
        }

    }

    fun configRecyclerViewFilmesPopulares(){
        binding.rvFilmesPopulares.layoutManager = GridLayoutManager(this, 3)
        binding.rvFilmesPopulares.adapter = adapterFilme
    }

    fun configRecyclerViewFilmesTopAvaliados(){
        binding.rvFilmesRecentes.layoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
        binding.rvFilmesRecentes.adapter = adapterFilme
    }

}