package implementations.database

import dataTypes.QueryTypes
import interfaces.DAOConnection
import java.sql.Connection
import java.sql.DriverManager
import java.sql.ResultSet

/**
 * Класс взаимодействия с БД
 */
class DAO : DAOConnection {
    /**
     * Подключение к БД
     */
    private var conn: Connection? = null
    override fun openConnection(dbName: String): Boolean {
        return try {
            conn = DriverManager.getConnection("jdbc:sqlite:$dbName")
            println("Connection to SQLite has been established.")
            true
        } catch (e: Exception) {
            println("Error opening connection: ${e.message}")
            false
        }
    }

    override fun insertSynonyms(word: String, synonyms: List<String>) {
        synonyms.forEach { synonym ->
            queryExecute("INSERT INTO words (word, connected_word) VALUES ('$word', '$synonym')", "", QueryTypes.INSERT)
        }
    }

    override fun findSynonymsByWord(word: String): List<String> {
        return queryExecute("SELECT connected_word FROM words WHERE word = '$word'", "connected_word", QueryTypes.SELECT) ?: listOf(word)
    }



    override fun closeConnection(): Boolean {
        return try {
            conn?.close()
            println("Connection closed.")
            true
        } catch (e: Exception) {
            println("Error closing connection: ${e.message}")
            false
        }
    }

    override fun queryExecute(query: String, column: String, type: QueryTypes): List<String>? {
        val transaction = conn?.createStatement()
        return try {
            val resultSet: ResultSet? = transaction?.executeQuery(query)
            val results = mutableListOf<String>()
            while (resultSet?.next() == true) {
                val value = resultSet.getString(column)
                if (value != null) {
                    results.add(value)
                }
            }
            results.ifEmpty { null }
        } catch (e: Exception) {
            println("SQL error: ${e.message}")
            null
        }
    }
}
