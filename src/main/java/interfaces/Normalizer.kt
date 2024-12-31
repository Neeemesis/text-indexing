package interfaces

import java.util.ArrayList

/**
 * Интерфейс для сервиса обработки запроса пользователя
 */
interface Normalizer {
    /**
     * Метод нормализации запроса пользователя
     * При создании экземпляра класса, реализующего интерфейс передается ввод пользователя. Строка, получившаяся в
     * результате нормализации не возвращается в вызывающий класс, а сохраняется в реализующем для дальнейшей работы.
     * @param input строка запроса пользователя
     * @return true если нормализация прошла успешно, false если не успешно
     */
    fun normalize(input: String): Boolean

    /**
     * Метод разбиения запроса на двусловия
     * Обрабатывается та же строка, что и в предыдущем методе
     * @return массив двусловий
     */
    fun splitter(): ArrayList<String>
}