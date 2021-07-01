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

    private fun generateRandTheme() {
        questions?.let {
            for (question in it) {
                try {
                    question.themeId = getRandTheme()
                }
                catch (exception: Exception) {
                    Log.e(TAG, exception.printStackTrace().toString())
                    finishApp()
                }
            }
        }
    }

    private fun getRandTheme(): Int {
        val size = 4
        return when ((0 until size).random()) {
            0 -> R.style.Theme_Blue
            1 -> R.style.Theme_Red
            2 -> R.style.Theme_Green
            3 -> R.style.Theme_Yellow
            else -> -1
        }
    }

    private fun getSelectedAnswer(question: Question): String {
        return when (question.selectedAnswer) {
            0 -> question.option0
            1 -> question.option1
            2 -> question.option2
            3 -> question.option3
            4 -> question.option4
            else -> "Not set"
        }
    }

    private fun getScore(): Int {
        var score = 0

        questions?.let {
            for (question in it) {
                if (question.selectedAnswer == question.answer)
                    score += 1
            }
        }
        return score
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

    override fun setFragmentTheme(questionIndex: Int) {
        val question = questions?.getOrNull(questionIndex)
        question?.let { setTheme(it.themeId) }
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

    override fun makeToast(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }

    override fun getQuestion(questNum: Int): Question? {
        return questions?.getOrNull(questNum)
    }

    override fun getQuestionsSize(): Int {
        return questions?.size ?: 0
    }

    override fun getResult(): String {
        return getScore().toString() + "/" + getQuestionsSize().toString()
    }

    override fun generateResultMsg(): String {
        var msg =   "Your result: " + getResult() + "\n" +
                    "\n"

        questions?.let {
            for (question in it) {
                msg +=  question.id.toString() + ") " + question.question + "\n" +
                        "Your answer: " + getSelectedAnswer(question) + "\n" +
                        "\n"
            }
        }

        return msg
    }

    override fun finishApp() {
        finish()
    }

}