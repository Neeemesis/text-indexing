package implementations

import interfaces.AnswerChooser
import interfaces.Normalizer
import utils.NormalizerImpl

/**
 * Класс выбора ответа
 * Алгоритм выбора ответа:
 * 1. Нрмализация запроса (как пройти в библиотеку) -> (как пройти библиотеку)
 * 2. Разбиваем ввод пользователя на части по два слова (как пройти библиотеку) -> (как пройти, пройти библиотеку)
 * 3. Ищем в тезаурусе подходящие строки по тегу "name" и достаем из него строку по тегу "lemma"
 * 4. Удаляем повторяющиеся слова
 * 5. Если в базе данных есть строка с набором лемм, соответствующих получившемуся массиву - выводим ответ
 */
class ThesaurusAnswerChooser : AnswerChooser {

    override fun getAnswer(question: String): String {
        val normalizer: Normalizer = NormalizerImpl(question)
        
        TODO("Not yet implemented")
    }
}
