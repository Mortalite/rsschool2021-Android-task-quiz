package com.rsschool.quiz

import android.content.ContentValues
import android.provider.BaseColumns
import org.json.JSONArray
import java.io.Serializable

class DatabaseManager(quizSQLHelper: QuizSQLHelper) : Serializable {

    val db = quizSQLHelper.writableDatabase

    object UserDTO : BaseColumns {
        const val TABLE_NAME = "userinfo"
        const val COLUMN_NAME_SCORE = "score"
    }

    private val CREATE_USERTABLE =
        "CREATE TABLE IF NOT EXISTS ${UserDTO.TABLE_NAME} (" +
                "${BaseColumns._ID} INTEGER PRIMARY KEY," +
                "${UserDTO.COLUMN_NAME_SCORE} INTEGER)"

    private val DELETE_USERTABLE = "DROP TABLE IF EXISTS ${UserDTO.TABLE_NAME}"

    object QuiestionDTO : BaseColumns {
        const val TABLE_NAME = "questiondto"
        const val COLUMN_NAME_QUESTION = "question"
        const val COLUMN_NAME_OPTION0 = "option0"
        const val COLUMN_NAME_OPTION1 = "option1"
        const val COLUMN_NAME_OPTION2 = "option2"
        const val COLUMN_NAME_OPTION3 = "option3"
        const val COLUMN_NAME_OPTION4 = "option4"
        const val COLUMN_NAME_CHECKED_ANSWER = "checked_answer"
        const val COLUMN_NAME_ANSWER = "answer"
    }

    private val CREATE_QUESTION_DB =
        "CREATE TABLE IF NOT EXISTS ${QuiestionDTO.TABLE_NAME} (" +
                "${BaseColumns._ID} INTEGER PRIMARY KEY," +
                "${QuiestionDTO.COLUMN_NAME_QUESTION} TEXT," +
                "${QuiestionDTO.COLUMN_NAME_OPTION0} TEXT," +
                "${QuiestionDTO.COLUMN_NAME_OPTION1} TEXT," +
                "${QuiestionDTO.COLUMN_NAME_OPTION2} TEXT," +
                "${QuiestionDTO.COLUMN_NAME_OPTION3} TEXT," +
                "${QuiestionDTO.COLUMN_NAME_OPTION4} TEXT," +
                "${QuiestionDTO.COLUMN_NAME_CHECKED_ANSWER} TEXT," +
                "${QuiestionDTO.COLUMN_NAME_ANSWER} INTEGER)"

    private val DELETE_QUESTION_DB = "DROP TABLE IF EXISTS ${QuiestionDTO.TABLE_NAME}"

    fun getUserData(isguest: Int): User? {
        val projection = arrayOf(
            BaseColumns._ID,
            UserDTO.COLUMN_NAME_SCORE
        )
        // How you want the results sorted in the resulting Cursor
        val sortOrder = "${BaseColumns._ID} ASC"

        val cursor = db.query(
            UserDTO.TABLE_NAME,   // The table to query
            projection,             // The array of columns to return (pass null to get all)
            null,              // The columns for the WHERE clause
            null,          // The values for the WHERE clause
            null,                   // don't group the rows
            null,                   // don't filter by row groups
            sortOrder               // The sort order
        )

        with(cursor) {
            while (moveToNext()) {
                val id = getInt(getColumnIndexOrThrow(BaseColumns._ID))
                val score = getInt(getColumnIndexOrThrow(UserDTO.COLUMN_NAME_SCORE))

                return User(id, score)
            }
        }
        return null
    }

    fun saveQuestionSet(questionSet: JSONArray) {
        db.execSQL(CREATE_USERTABLE)
        db.execSQL(DELETE_QUESTION_DB)
        db.execSQL(CREATE_QUESTION_DB)

        for (i in 0 until questionSet.length()) {
            var jsonObject = questionSet.getJSONObject(i)

            val values = ContentValues().apply {
                put(QuiestionDTO.COLUMN_NAME_QUESTION, jsonObject.getString("Question"))
                put(QuiestionDTO.COLUMN_NAME_OPTION0, jsonObject.getString("Option0"))
                put(QuiestionDTO.COLUMN_NAME_OPTION1, jsonObject.getString("Option1"))
                put(QuiestionDTO.COLUMN_NAME_OPTION2, jsonObject.getString("Option2"))
                put(QuiestionDTO.COLUMN_NAME_OPTION3, jsonObject.getString("Option3"))
                put(QuiestionDTO.COLUMN_NAME_OPTION4, jsonObject.getString("Option4"))
                put(QuiestionDTO.COLUMN_NAME_CHECKED_ANSWER, -1)
                put(QuiestionDTO.COLUMN_NAME_ANSWER, jsonObject.getInt("Answer"))
            }

            val newRowId = db?.insert(QuiestionDTO.TABLE_NAME, null, values)
        }
    }

    public fun getQuestionSet(): ArrayList<Question> {
        val projection = arrayOf(
            BaseColumns._ID,
            QuiestionDTO.COLUMN_NAME_QUESTION,
            QuiestionDTO.COLUMN_NAME_OPTION0,
            QuiestionDTO.COLUMN_NAME_OPTION1,
            QuiestionDTO.COLUMN_NAME_OPTION2,
            QuiestionDTO.COLUMN_NAME_OPTION3,
            QuiestionDTO.COLUMN_NAME_OPTION4,
            QuiestionDTO.COLUMN_NAME_CHECKED_ANSWER,
            QuiestionDTO.COLUMN_NAME_ANSWER
        )

        val sortOrder = "${BaseColumns._ID} ASC"

        val cursor = db.query(
            QuiestionDTO.TABLE_NAME,
            projection,
            null,
            null,
            null,
            null,
            sortOrder
        )

        val questionSet = ArrayList<Question>()
        with(cursor) {
            while (moveToNext()) {
                val id = getInt(getColumnIndexOrThrow(BaseColumns._ID))
                val question = getString(getColumnIndexOrThrow(QuiestionDTO.COLUMN_NAME_QUESTION))
                val option0 = getString(getColumnIndexOrThrow(QuiestionDTO.COLUMN_NAME_OPTION0))
                val option1 = getString(getColumnIndexOrThrow(QuiestionDTO.COLUMN_NAME_OPTION1))
                val option2 = getString(getColumnIndexOrThrow(QuiestionDTO.COLUMN_NAME_OPTION2))
                val option3 = getString(getColumnIndexOrThrow(QuiestionDTO.COLUMN_NAME_OPTION3))
                val option4 = getString(getColumnIndexOrThrow(QuiestionDTO.COLUMN_NAME_OPTION4))
                val chosen_answer = getInt(getColumnIndexOrThrow(QuiestionDTO.COLUMN_NAME_CHECKED_ANSWER))
                val answer = getInt(getColumnIndexOrThrow(QuiestionDTO.COLUMN_NAME_ANSWER))
                questionSet.add(Question(id, question, option0, option1, option2, option3, option4, chosen_answer, answer))
            }
        }
        return questionSet
    }



}