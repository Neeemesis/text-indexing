import implementations.ThesaurusAnswerChooser
import interfaces.AnswerChooser

fun main() {
    val answerChooser: AnswerChooser = ThesaurusAnswerChooser()

    println(answerChooser.getAnswer("Как пройти в библиотеку"))
}
