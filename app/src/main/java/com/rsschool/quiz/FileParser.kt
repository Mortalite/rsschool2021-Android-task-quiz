package com.rsschool.quiz

import org.json.JSONArray
import java.io.IOException
import java.io.InputStream

class FileParser {

    public fun parseQuestions(inputStream: InputStream) : JSONArray {
        var arr = arrayListOf<String>()
        var json:String? = null
        try {
            json = inputStream.bufferedReader().use { it.readText() }
            return JSONArray(json)
        } catch (exception: IOException) {
            exception.printStackTrace()
        }
        return JSONArray()
    }

}