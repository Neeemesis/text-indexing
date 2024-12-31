import implementations.StatisticAnswerChooser
import implementations.database.DAO
import interfaces.AnswerChooser
import interfaces.DAOConnection

/**
 * НЕ НУЖНО ПОДКЛЮЧАТЬСЯ К БД В МЕЙНЕ, ЭТО ЗДЕСЬ ДЛЯ ПРИМЕРА
 */
fun main() {
    val dao: DAO = DAO()
    dao.openConnection("wordsdb.db")
    val answerChooser: AnswerChooser = StatisticAnswerChooser(dao)


    val question = "как дойти в библиотеку"
    val cleanedQuestion = question.replace(Regex("[^а-яА-Я\\s]"), "").split(" ")


    println("Пользовательский запрос (после очистки): $cleanedQuestion")

    val queryResult = answerChooser.getAnswers()
    println("Вопросы, хранящиеся в базе данных: $queryResult")

    val synonymsResult = answerChooser.getSynonyms()
    println("Список синонимов в базе данных (после обновлений): $synonymsResult")

    val answer = answerChooser.getAnswer(question)
    println("Ответ на запрос: $answer")

    dao.closeConnection()
}
