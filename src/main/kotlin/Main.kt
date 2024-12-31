import implementations.ThesaurusAnswerChooser
import interfaces.AnswerChooser

fun main() {
    val answerChooser: AnswerChooser = ThesaurusAnswerChooser()

//    val query = "ГИПЕРТРОФИЯ ОРГАНА"
    val query = "вычитка текста"


    println(answerChooser.getAnswer(query))
}
