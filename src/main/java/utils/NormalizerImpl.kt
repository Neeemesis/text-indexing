package utils

import interfaces.Normalizer
import java.util.ArrayList

class NormalizerImpl(private val input: String): Normalizer {

    private var locInput = input

    override fun normalize(input: String): Boolean {
        TODO("Not yet implemented")
        //locInput = result
    }

    override fun splitter(): ArrayList<String> {
        TODO("Not yet implemented")
        //locInput.split()
        //return locInput
    }
}