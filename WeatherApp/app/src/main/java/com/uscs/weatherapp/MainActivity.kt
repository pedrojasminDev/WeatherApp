package com.uscs.weatherapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.*
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {

    private lateinit var editTextCity: EditText
    private lateinit var buttonSearch: Button
    private lateinit var buttonHistorico: Button
    private lateinit var textViewWelcome: TextView
    private lateinit var textViewCity: TextView
    private lateinit var textViewTemp: TextView
    private lateinit var textViewCondition: TextView
    private lateinit var textViewHumidity: TextView

    private val API_KEY = "08cad898cac454550fc0b1334b71dc54"  // ← colocar chave aqui
    private lateinit var weatherService: WeatherApiService
    private lateinit var dbHelper: DatabaseHelper

    private var userId: Int = -1
    private var userEmail: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Recebe dados do login
        userId = intent.getIntExtra("USER_ID", -1)
        userEmail = intent.getStringExtra("USER_EMAIL") ?: ""

        if (userId == -1) {
            // Se não veio do login, volta para a tela de login
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
            return
        }

        // Inicializar componentes
        editTextCity = findViewById(R.id.editTextCity)
        buttonSearch = findViewById(R.id.buttonSearch)
        buttonHistorico = findViewById(R.id.buttonHistorico)
        textViewWelcome = findViewById(R.id.textViewWelcome)
        textViewCity = findViewById(R.id.textViewCity)
        textViewTemp = findViewById(R.id.textViewTemp)
        textViewCondition = findViewById(R.id.textViewCondition)
        textViewHumidity = findViewById(R.id.textViewHumidity)

        dbHelper = DatabaseHelper(this)
        val nomeUsuario = dbHelper.getUsuarioNome(userId)
        textViewWelcome.text = "Olá, $nomeUsuario! 👋"

        // Configurar Retrofit
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.openweathermap.org/data/2.5/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        weatherService = retrofit.create(WeatherApiService::class.java)



        // Botão de buscar clima
        buttonSearch.setOnClickListener {
            val city = editTextCity.text.toString().trim()
            if (city.isNotEmpty()) {
                buscarClimaReal(city)
            } else {
                Toast.makeText(this, "Digite o nome de uma cidade", Toast.LENGTH_SHORT).show()
            }
        }

        buttonHistorico.setOnClickListener {
            mostrarHistorico()
        }
    }

    private fun buscarClimaReal(cityName: String) {
        textViewTemp.text = "⏳ Carregando..."
        textViewCondition.text = ""
        textViewHumidity.text = ""

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = weatherService.getWeather(cityName, API_KEY)

                withContext(Dispatchers.Main) {
                    atualizarTela(response)
                    // Salvar pesquisa no histórico
                    dbHelper.salvarPesquisa(userId, cityName)
                    Toast.makeText(this@MainActivity, "Pesquisa salva no histórico!", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    textViewTemp.text = "❌ Erro!"
                    textViewCondition.text = e.message ?: "Cidade não encontrada"
                    textViewHumidity.text = ""
                    Toast.makeText(
                        this@MainActivity,
                        "Erro: Verifique o nome da cidade",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }

    private fun atualizarTela(weather: WeatherResponse) {
        textViewCity.text = weather.name
        textViewTemp.text = "${weather.main.temp.toInt()}°C"
        textViewCondition.text = weather.weather[0].description.replaceFirstChar { it.uppercase() }
        textViewHumidity.text = "💧 Umidade: ${weather.main.humidity}%"
    }

    private fun mostrarHistorico() {
        val historico = dbHelper.getHistorico(userId)

        if (historico.isEmpty()) {
            Toast.makeText(this, "Nenhuma pesquisa encontrada", Toast.LENGTH_SHORT).show()
            return
        }

        // Construir mensagem para mostrar
        val mensagem = StringBuilder("📜 HISTÓRICO DE PESQUISAS\n\n")
        for (item in historico) {
            mensagem.append("📍 ${item.cidade}\n")
            mensagem.append("   🕐 ${item.dataHora}\n\n")
        }

        // Mostrar histórico
        AlertDialog.Builder(this)
            .setTitle("Histórico de Cidades")
            .setMessage(mensagem.toString())
            .setPositiveButton("Fechar", null)
            .setNeutralButton("Limpar Histórico") { _, _ ->
                dbHelper.limparHistorico(userId)
                Toast.makeText(this, "Histórico limpo!", Toast.LENGTH_SHORT).show()
            }
            .show()
    }
}