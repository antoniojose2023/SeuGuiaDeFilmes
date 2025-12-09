package br.com.antoniodev.seuguiadefilmes

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import br.com.antoniodev.seuguiadefilmes.api.RetrofitHelper
import br.com.antoniodev.seuguiadefilmes.databinding.ActivityMainBinding
import br.com.antoniodev.seuguiadefilmes.util.Constante
import com.squareup.picasso.Picasso
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        CoroutineScope(Dispatchers.IO).launch {

            try{
                val response = RetrofitHelper(this@MainActivity).apiFilmeTMDB().getFilmeAtual()
                if(response.isSuccessful && response.body() != null){
                      val filmeAtual = response.body()
                      if(filmeAtual != null){
                          withContext(Dispatchers.Main){
                              binding.tvNomeFilmeAtual.text = filmeAtual.title

                              val urlImagemFilme = "${Constante.URL_BASE_IMAGEM}" + "w500/" + "${filmeAtual.poster_path}"
                              Picasso.get().load(urlImagemFilme).into( binding.ivFilmeDestaqueAtual )
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
}