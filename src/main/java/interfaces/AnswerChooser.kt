package interfaces

/**
 * Интерфейс для классов с выбором ответа на запрос
 */
interface AnswerChooser {
    /**
     * Метод выбора ответа на запрос пользователя
     * @param question строка запроса пользователя
     * @return возвращает ответ на запрос пользователя, если его удалось найти, либо подготовленный вариант ответа на
     * не распознанный запрос
     */
    fun getAnswer(question: String): String

    /**
     * Сеттер пороргового значения, пересечение которого считается успехом в распознавании
     * @param value пороговое значение
     */
    fun setThresholdValue(value: Double)
    fun getSynonyms(): List<String>
    fun getAnswers(): List<String>
}
