package com.uscs.weatherapp

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "weather_app.db"
        private const val DATABASE_VERSION = 1

        // Nomes das tabelas
        const val TABLE_USUARIOS = "usuarios"
        const val TABLE_HISTORICO = "historico"

        // Colunas da tabela "usuarios"
        const val COLUMN_ID = "id"
        const val COLUMN_NOME = "nome"
        const val COLUMN_EMAIL = "email"
        const val COLUMN_SENHA = "senha"

        // Colunas da tabela "historico"
        const val COLUMN_HISTORICO_ID = "id"
        const val COLUMN_USUARIO_ID = "usuario_id"
        const val COLUMN_CIDADE = "cidade"
        const val COLUMN_DATA_HORA = "data_hora"
    }

    override fun onCreate(db: SQLiteDatabase) {
        // Criar tabela de "usuários"
        val createUsuariosTable = """
            CREATE TABLE $TABLE_USUARIOS (
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_NOME TEXT NOT NULL,
                $COLUMN_EMAIL TEXT NOT NULL UNIQUE,
                $COLUMN_SENHA TEXT NOT NULL
            )
        """.trimIndent()
        db.execSQL(createUsuariosTable)

        // Criar tabela de "histórico"
        val createHistoricoTable = """
            CREATE TABLE $TABLE_HISTORICO (
                $COLUMN_HISTORICO_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_USUARIO_ID INTEGER NOT NULL,
                $COLUMN_CIDADE TEXT NOT NULL,
                $COLUMN_DATA_HORA TEXT NOT NULL,
                FOREIGN KEY($COLUMN_USUARIO_ID) REFERENCES $TABLE_USUARIOS($COLUMN_ID)
            )
        """.trimIndent()
        db.execSQL(createHistoricoTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_HISTORICO")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_USUARIOS")
        onCreate(db)
    }

    // ==================== FUNÇÕES DE USUÁRIO ====================

    fun cadastrarUsuario(nome: String, email: String, senha: String): Boolean {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_NOME, nome)
            put(COLUMN_EMAIL, email)
            put(COLUMN_SENHA, senha)
        }
        val result = db.insert(TABLE_USUARIOS, null, values)
        db.close()
        return result != -1L
    }

    fun fazerLogin(email: String, senha: String): Int {
        val db = readableDatabase
        val query = "SELECT $COLUMN_ID FROM $TABLE_USUARIOS WHERE $COLUMN_EMAIL = ? AND $COLUMN_SENHA = ?"
        val cursor = db.rawQuery(query, arrayOf(email, senha))

        var userId = -1
        if (cursor.moveToFirst()) {
            userId = cursor.getInt(0)
        }
        cursor.close()
        db.close()
        return userId
    }

    fun getUsuarioNome(userId: Int): String {
        val db = readableDatabase
        val query = "SELECT $COLUMN_NOME FROM $TABLE_USUARIOS WHERE $COLUMN_ID = ?"
        val cursor = db.rawQuery(query, arrayOf(userId.toString()))

        var nome = ""
        if (cursor.moveToFirst()) {
            nome = cursor.getString(0)
        }
        cursor.close()
        db.close()
        return nome
    }

    // ==================== FUNÇÕES DE HISTÓRICO ====================

    fun salvarPesquisa(userId: Int, cidade: String) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_USUARIO_ID, userId)
            put(COLUMN_CIDADE, cidade)
            put(COLUMN_DATA_HORA, java.text.SimpleDateFormat("dd/MM/yyyy HH:mm", java.util.Locale.getDefault()).format(java.util.Date()))
        }
        db.insert(TABLE_HISTORICO, null, values)
        db.close()
    }

    fun getHistorico(userId: Int): List<HistoricoItem> {
        val historico = mutableListOf<HistoricoItem>()
        val db = readableDatabase
        val query = """
            SELECT $COLUMN_CIDADE, $COLUMN_DATA_HORA 
            FROM $TABLE_HISTORICO 
            WHERE $COLUMN_USUARIO_ID = ? 
            ORDER BY $COLUMN_HISTORICO_ID DESC
        """.trimIndent()
        val cursor = db.rawQuery(query, arrayOf(userId.toString()))

        while (cursor.moveToNext()) {
            val cidade = cursor.getString(0)
            val dataHora = cursor.getString(1)
            historico.add(HistoricoItem(cidade, dataHora))
        }
        cursor.close()
        db.close()
        return historico
    }

    fun limparHistorico(userId: Int) {
        val db = writableDatabase
        db.delete(TABLE_HISTORICO, "$COLUMN_USUARIO_ID = ?", arrayOf(userId.toString()))
        db.close()
    }
}

// Classe para representar um item do histórico
data class HistoricoItem(val cidade: String, val dataHora: String)