package interfaces

import dataTypes.QueryTypes

interface DAOConnection {
    /**
     * Открытие подключения к БД
     *
     * @param dbName название БД
     */
    fun openConnection(dbName: String): Boolean

    /**
     * Закрытие подключения к БД
     */
    fun closeConnection(): Boolean

    /**
     * Выполнение запроса к БД
     *
     * @param query текст запроса
     * @param type тип запроса
     * @param column возвращаемый столбец
     */
    fun queryExecute(query: String, column: String, type: QueryTypes): List<String>?


    fun findSynonymsByWord(word: String): List<String>

    fun insertSynonyms(word: String, synonyms: List<String>)
}
