package implementations

import interfaces.AnswerChooser
import interfaces.Normalizer
import utils.NormalizerImpl
import utils.XmlSelector

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

    private val senses =  XmlSelector("/thesaurus/senses.xml")


    /**
     * Обработка одной строки текста (двуслова или слова)
     */
    private fun processText(text: String): List<String> {
        println("Обработка текста: $text")
        val results = senses.getAttributeValuesByAttributeNameAndAttributeValue(
            attributeName = "name",
            attributeValue = text.uppercase(),
            attributeValueName = "lemma"
        )
        println("Результаты поиска для '$text': $results")

        return results
    }

    override fun getAnswer(question: String): String {
        println("Получен вопрос: $question")
        if (question.isBlank()) {
            println("Вопрос пустой. Возвращаем стандартный ответ.")
            return "Вопрос не задан."
        }

        val normalizer: Normalizer = NormalizerImpl(question)
        normalizer.normalize(question)
        println("После нормализации: $normalizer")

        val phrases = normalizer.splitter()
        println("Разбитые фразы: $phrases")

        val finalLemmas = mutableListOf<String>()

        phrases.forEach { text ->
            val lemmas = processText(text)

            if (lemmas.isNotEmpty()) {
                println("Леммы, найденные для фразы '$text': $lemmas")
                finalLemmas.add(lemmas.first())
            } else {
                text.split(" ").forEach { word ->
                    val wordLemmas = processText(word)
                    if (wordLemmas.isEmpty()) {
                        println("Леммы не найдены для слова '$word'. Добавляем слово в результат.")
                        finalLemmas.add(word)
                    } else {
                        println("Леммы, найденные для слова '$word': $wordLemmas")
                        finalLemmas.add(wordLemmas.first())
                    }
                }
            }
        }

        val finalResult = finalLemmas.distinct().joinToString(" ")
        return finalResult
    }
}
