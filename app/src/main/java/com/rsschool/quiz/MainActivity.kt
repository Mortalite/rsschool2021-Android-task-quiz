package com.rsschool.quiz

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.rsschool.quiz.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(),   QuestionFragment.IQuestionListener,
                                            ResultFragment.IResultListener {

    private val TAG = this.javaClass.simpleName
    private var dbHelper: DatabaseManager? = null
    private var quizSQLHelper: QuizSQLHelper? = null
    private var binding: ActivityMainBinding? = null
    private var questions: ArrayList<Question>? = null

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

        generateRandTheme()
        openQuestionFragment(0)
    }

    override fun openQuestionFragment(questionIndex: Int) {
        val questionFragment = QuestionFragment.newInstance(questionIndex)
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container, questionFragment)
        transaction.commit()
    }

    override fun openResultFragment() {
        val questionFragment = ResultFragment.newInstance()
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container, questionFragment)
        transaction.commit()
    }

    override fun setFragmentTheme(questionIndex: Int) {
        val question = questions?.getOrNull(questionIndex)
        if (question != null)
            setTheme(question.themeId)
    }

    fun generateRandTheme() {
        questions?.let {
            for (question in it)
                question.themeId = getRandTheme()
        }
    }

    fun getRandTheme(): Int {
        val size = 4
        val rand = (0 until size).random()
        return when (rand) {
            0 -> R.style.AppTheme_Blue
            1 -> R.style.AppTheme_Red
            2 -> R.style.AppTheme_Green
            3 -> R.style.AppTheme_Yellow
            else -> -1
        }
    }

    override fun resetSelectedAnswers() {
        questions?.let {
            for (question in it) {
                question.selectedAnswer = -1
                question.checkedId = -1
            }
            generateRandTheme()
        }
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

    override fun getScore(): Int {
        var score = 0

        questions?.let {
            for (question in it) {
                if (question.selectedAnswer == question.answer)
                    score += 1
            }
        }
        return score
    }

    override fun getQuestionsSize(): Int {
        return questions?.size ?: 0
    }

}