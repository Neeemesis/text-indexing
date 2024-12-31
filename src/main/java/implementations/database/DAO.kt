package implementations.database

import dataTypes.QueryTypes
import interfaces.DAOConnection
import java.sql.Connection

/**
 * класс взаимодействия с БД
 */
class DAO : DAOConnection {
    /**
     * подключение к БД
     */
    private var conn: Connection? = null
    override fun openConnection(dbName: String): Boolean {
        TODO("Not yet implemented")
    }

    override fun closeConnection(): Boolean {
        TODO("Not yet implemented")
    }

    override fun queryExecute(query: String, column: String, type: QueryTypes): List<String>? {
        TODO("Not yet implemented")
    }
}
