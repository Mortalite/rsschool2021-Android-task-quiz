package com.rsschool.quiz

import org.json.JSONArray
import java.io.IOException
import java.io.InputStream

class FileParser {

    fun parseQuestions(inputStream: InputStream) : JSONArray {
        try {
            val json = inputStream.bufferedReader().use { it.readText() }
            return JSONArray(json)
        } catch (exception: IOException) {
            exception.printStackTrace()
        }
        return JSONArray()
    }

}