package implementations

import dataTypes.QueryTypes
import implementations.database.DAO
import interfaces.AnswerChooser

/**
 * Класс, реулизующий логику выбора варианта ответа на зпрос пользователя с помощью статистического анализа
 * Строка, введенная пользователем сравнивается с сохраненными в базе данных вопросами. Затем, если совпадения найти
 * не удалось, выгружает из таблицы words все контекстные синонимы для каждого из слов запроса и создает массив
 * возможных запросов, в котором методом перебора заменяет все слова на их контекстные синонимы.
 * Каждая из этих строк сравнивается с сохраненной в таблице questions, и если удалось найти точное совпадение,
 * то возвращает на него ответ. Если совпадение пересекло пороговое значение, то возвращается ответ, а строка передается
 * в класс StatisticTeacher для обучения системы, а пользователю восвращается заранее подготовленная строка.
 */
class StatisticAnswerChooser(
    private val dao: DAO
) : AnswerChooser {

    private var thresholdValue: Double = 1.0

    /**
     * Сообщение при безуспешном поиске ответа
     */

    override fun getAnswer(question: String): String {
        val queryResult: List<String>? = dao.queryExecute("SELECT question FROM questions_and_answers", "question", QueryTypes.SELECT)

        val wordsInQuestion = question.replace(Regex("[^а-яА-Я\\s]"), "").split(" ")

        wordsInQuestion.forEach { word ->
            val synonyms = dao.findSynonymsByWord(word)
            synonyms.forEach { synonym ->
                val synonymExists = dao.queryExecute("SELECT connected_word FROM words WHERE word = '$word' AND connected_word = '$synonym'", "connected_word", QueryTypes.SELECT)
                if (synonymExists.isNullOrEmpty()) {
                    dao.queryExecute("INSERT INTO words (word, connected_word) VALUES ('$word', '$synonym')", "", QueryTypes.INSERT)
                    println("Добавлено новое связанное слово: '$word' с синонимом '$synonym'.")
                }
            }
        }

        var bestMatch: String? = null
        var bestMatchCount = 0

        queryResult?.forEach { dbQuestion ->
            val matchCount = dbQuestion.split(" ").intersect(wordsInQuestion.toSet()).size
            if (matchCount > bestMatchCount) {
                bestMatchCount = matchCount
                val answer = dao.queryExecute("SELECT answer FROM questions_and_answers WHERE question = '$dbQuestion'", "answer", QueryTypes.SELECT)?.firstOrNull()
                if (answer != null) {
                    bestMatch = answer
                }
            }
        }

        return bestMatch ?: DEFAULT_ANSWER
    }

    override fun setThresholdValue(value: Double) {
        thresholdValue = value
    }

    override fun getAnswers(): List<String>{
        return dao.queryExecute("SELECT question FROM questions_and_answers", "question", QueryTypes.SELECT) ?: emptyList()
    }

    override fun getSynonyms(): List<String> {
        return dao.queryExecute("SELECT word FROM words", "word", QueryTypes.SELECT) ?: emptyList()
    }
    companion object{
        const val DEFAULT_ANSWER = "Ответ найти не удалось, пожалуйста, повторите запрос."
    }
}
