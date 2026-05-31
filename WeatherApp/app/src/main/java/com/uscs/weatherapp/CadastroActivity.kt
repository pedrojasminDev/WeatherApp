package com.uscs.weatherapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class CadastroActivity : AppCompatActivity() {

    private lateinit var editTextNome: EditText
    private lateinit var editTextEmail: EditText
    private lateinit var editTextSenha: EditText
    private lateinit var editTextConfirmarSenha: EditText
    private lateinit var buttonCadastrar: Button
    private lateinit var textViewLogin: TextView
    private lateinit var dbHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cadastro)

        editTextNome = findViewById(R.id.editTextNome)
        editTextEmail = findViewById(R.id.editTextEmail)
        editTextSenha = findViewById(R.id.editTextSenha)
        editTextConfirmarSenha = findViewById(R.id.editTextConfirmarSenha)
        buttonCadastrar = findViewById(R.id.buttonCadastrar)
        textViewLogin = findViewById(R.id.textViewLogin)
        dbHelper = DatabaseHelper(this)

        buttonCadastrar.setOnClickListener {
            val nome = editTextNome.text.toString().trim()
            val email = editTextEmail.text.toString().trim()
            val senha = editTextSenha.text.toString().trim()
            val confirmarSenha = editTextConfirmarSenha.text.toString().trim()

            if (nome.isEmpty() || email.isEmpty() || senha.isEmpty()) {
                Toast.makeText(this, "Preencha todos os campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (senha != confirmarSenha) {
                Toast.makeText(this, "As senhas não coincidem", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val success = dbHelper.cadastrarUsuario(nome, email, senha)
            if (success) {
                Toast.makeText(this, "Cadastro realizado com sucesso!", Toast.LENGTH_LONG).show()
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this, "Erro: Email já cadastrado", Toast.LENGTH_SHORT).show()
            }
        }

        textViewLogin.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}