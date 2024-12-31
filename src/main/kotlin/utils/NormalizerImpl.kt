package utils

import interfaces.Normalizer
import java.util.ArrayList

class NormalizerImpl(input: String) : Normalizer {

    private val serviceWords by lazy {
        XmlSelector("/thesaurus/service_words.xml")
            .getElementValuesByAttributeNameAndAttributeValue("name", "service")
            .map { it.uppercase() }
    }

    private var locInput = input


    override fun normalize(input: String): Boolean {
        return try {
            locInput = input
                .uppercase()
                .trim()
                .split(" ")
                .filter { it !in serviceWords }
                .joinToString(" ")
            true
        } catch (e: Exception) {
            println("Error normalizing input: ${e.message}")
            false
        }
    }

    override fun splitter(): ArrayList<String> {
        return try {
            val words = locInput.split(" ")
            val phrases = ArrayList<String>()

            for (i in 0 until words.size - 1) {
                phrases.add("${words[i]} ${words[i + 1]}")
            }

            phrases
        } catch (e: Exception) {
            println("Error splitting input: ${e.message}")
            ArrayList()
        }
    }
}