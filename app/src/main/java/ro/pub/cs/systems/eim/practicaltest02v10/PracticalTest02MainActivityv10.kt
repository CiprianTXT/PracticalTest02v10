package ro.pub.cs.systems.eim.practicaltest02v10

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class PracticalTest02MainActivityv10 : AppCompatActivity() {
    private lateinit var pokemonNameText: EditText
    private lateinit var searchBtn: Button
    private lateinit var pokemonImageView: ImageView
    private lateinit var powerUpTextView: TextView
    private lateinit var pokemonTypeTextView: TextView
    private lateinit var switchActivityBtn: Button
    private lateinit var pokeReceiver: BroadcastReceiver

    companion object {
        private const val BASE_URL = "https://pokeapi.co//"
        private const val ACTION_RECEIVED = "ro.pub.cs.systems.eim.practicaltest02v10.POKEMON_RECEIVED"
        private const val POWER_UP = ""
        private const val TYPE = ""
    }

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val pokeService = retrofit.create(PracticalTest02Servicev10::class.java)

    private fun getPokemonFromAPI(pokemonInput: String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = pokeService.getPokemonInfo(pokemonInput.lowercase())
                Log.d("Response", response.toString())
                if (response.isSuccessful) {
                    val pokemon = response.body()
                    if (pokemon != null) {
                        updateUI(pokemon)
                    }
                } else {
                    showError("Pokemon not found!")
                }
            } catch (e: Exception) {
                showError("Error: ${e.message}")
            }
        }
    }

    private suspend fun updateUI(pokemon: Pokemon) {
        withContext(Dispatchers.Main) {
            val ability = pokemon.abilities.joinToString(", ") { it.ability.name }
            Log.d("Ability", ability)
            powerUpTextView.text = "Abilities: $ability"
            val type = pokemon.types.joinToString(", ") { it.type.name }
            Log.d("Type", type)
            pokemonTypeTextView.text = "Type: $type}"
            Glide.with(this@PracticalTest02MainActivityv10)
                .load(pokemon.sprites.frontDefault)
                .into(pokemonImageView)
        }
    }

    private fun showError(message: String) {
        CoroutineScope(Dispatchers.Main).launch {
            Toast.makeText(this@PracticalTest02MainActivityv10, message, Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_practical_test02v10_main)

        pokemonNameText = findViewById(R.id.pokemonNameText)
        searchBtn = findViewById(R.id.searchBtn)
        pokemonImageView = findViewById(R.id.pokemonImageView)
        powerUpTextView = findViewById(R.id.powerUpTextView)
        pokemonTypeTextView = findViewById(R.id.pokemonTypeTextView)
        switchActivityBtn = findViewById(R.id.switchActivityBtn)

        searchBtn.setOnClickListener {
            val pokemonInput = pokemonNameText.text.toString()
            if (pokemonInput.isNotEmpty()) {
                getPokemonFromAPI(pokemonInput)
            } else {
                showError("Please enter a Pokémon name!")
            }
        }

        switchActivityBtn.setOnClickListener {
            val intent = Intent(this, PracticalTest02SecondaryActivityv10::class.java)
            startActivity(intent)
        }
    }
}