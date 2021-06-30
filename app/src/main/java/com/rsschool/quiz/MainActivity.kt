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
        quizSQLHelper = QuizSQLHelper(this)
        quizSQLHelper?.let { dbHelper = DatabaseManager(it) }
        setContentView(binding?.root)
        uploadQuiz()
    }

    override fun onStart() {
        super.onStart()

        val questionFragment = QuestionFragment.newInstance()
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container, questionFragment)
        transaction.commit()
    }

    private fun uploadQuiz() {
        try {
            val questionJSON = FileParser().parseQuestions(assets.open("questions.json"))
            dbHelper?.saveQuestionSet(questionJSON)
            questions = dbHelper?.getQuestionSet()
            checkNotNull(questions)
        }
        catch (exception: RuntimeException) {
            Log.e(TAG, Log.getStackTraceString(exception))
            finish()
        }
    }

    override fun makeToast(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    override fun getQuestion(questNum: Int): Question? {
        return questions?.getOrNull(questNum)
    }

    override fun getQuestionsSize(): Int {
        return questions?.size ?: 0
    }

}