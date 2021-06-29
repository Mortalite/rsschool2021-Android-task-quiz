package com.rsschool.quiz

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.rsschool.quiz.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), QuestionFragment.IActionPerformedListener {

    private val TAG = this.javaClass.simpleName
    private var dbHelper: DatabaseManager? = null
    private var quizSQLHelper: QuizSQLHelper? = null
    private var binding: ActivityMainBinding? = null
    private var questions: ArrayList<Question>? = null
    private var score: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)

/*        val root = binding?.root
        try {
            if (root == null)
                throw RuntimeException("binding failed")
            else {
                setContentView(root)

                quizSQLHelper = QuizSQLHelper(this)
                if (quizSQLHelper == null)
                    throw RuntimeException("SQLHelper failed")
                dbHelper = DatabaseManager(quizSQLHelper!!)
            }
        }
        catch (exception: RuntimeException) {
            Log.e(TAG,exception.message ?: "Nullable runtime exception")
            finish()
        }*/

        quizSQLHelper = QuizSQLHelper(this)
        dbHelper = DatabaseManager(quizSQLHelper!!)

        when {
            binding == null -> Log.e(TAG, "binding failed")
            quizSQLHelper == null -> Log.e(TAG, "SQLHelper failed")
            dbHelper == null -> Log.e(TAG, "dbhelper failed")
            
        }
        setContentView(binding?.root)
        uploadQuiz()

    }

    override fun onStart() {
        super.onStart()

        val questionFragment = QuestionFragment.newInstance(0)
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container, questionFragment)
        transaction.commit()
    }

    private fun uploadQuiz() {
        try {
            val questionJSON = FileParser().parseQuestions(assets.open("questions.json"))
            dbHelper?.saveQuestionSet(questionJSON)
            questions = dbHelper?.getQuestionSet()
        }
        catch (exception: RuntimeException) {
            Log.e(TAG,exception.message ?: "Nullable runtime exception")
            finish()
        }
    }

    override fun makeToast(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    override fun getQuestion(questNum: Int): Question {
        Log.e(TAG,questions?.size.toString() ?: "Nullable runtime exception")
        questions = dbHelper?.getQuestionSet()
        if (questions == null)
            Log.e(TAG,"FAIL")
        return questions!![questNum]
    }

    override fun getQuestinoNum(): Int {
        return questions!!.size
    }

    override fun onClickNext(currQuestNum: Int, answerNum: Int) {
        val questionFragment = QuestionFragment.newInstance(currQuestNum + 1)
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container, questionFragment)
        transaction.commit()
    }


}