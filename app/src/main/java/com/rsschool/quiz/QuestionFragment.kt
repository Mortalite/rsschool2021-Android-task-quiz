package com.rsschool.quiz

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioGroup
import com.rsschool.quiz.databinding.FragmentQuestionBinding


class QuestionFragment : Fragment() {

    var _binding: FragmentQuestionBinding? = null
    val binding get() = _binding!!
    var currentQuestion: Question? = null
    var radioGroup: RadioGroup? = null
    var currentQuestionIndex = 0
    var checkedAnswer = 0
    lateinit var dbHelper: DatabaseManager
    lateinit var quizSQLHelper: QuizSQLHelper
    private var listener: IActionPerformedListener? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)

        listener = context as IActionPerformedListener
        quizSQLHelper = QuizSQLHelper(context)
        dbHelper = DatabaseManager(quizSQLHelper)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentQuestionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.back.setOnClickListener { loadPrevQuestion() }
        binding.next.setOnClickListener { loadNextQuestion() }

//        radioGroup.setOnCheckedChangeListener { _, checkedId ->
//            R.id.radioA ->
//        } }

        currentQuestion = listener?.getQuestion(currentQuestionIndex)
        drawQuestion()
    }

    private fun loadPrevQuestion() {
        listener?.makeToast("index = " + currentQuestionIndex)
        if (currentQuestionIndex > 0) {
            currentQuestionIndex -= 1
            drawQuestion()
        }
    }

    private fun loadNextQuestion() {
        listener?.makeToast("index = " + currentQuestionIndex)
        if (currentQuestionIndex < 6) {
            currentQuestionIndex += 1
            drawQuestion()
        }
    }

    fun getCheckedAnswer() {
    }

    private fun drawQuestion() {
        currentQuestion = listener?.getQuestion(currentQuestionIndex)
        binding.let {
            it.QuestionView.text = currentQuestion?.question ?: "Not set"
            it.radioA.text = currentQuestion?.optiona ?: "Not set"
            it.radioB.text = currentQuestion?.optionb ?: "Not set"
            it.radioC.text = currentQuestion?.optionc ?: "Not set"
            it.radioD.text = currentQuestion?.optiond ?: "Not set"
            it.radioE.text = currentQuestion?.optione ?: "Not set"
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    interface IActionPerformedListener {
        fun getQuestion(questNum: Int): Question
        fun getQuestinoNum(): Int
        fun onClickNext(currQuestNum: Int, answerNum: Int)
        fun makeToast(msg: String)
    }

    companion object {
        @JvmStatic
        fun newInstance(index: Int) =
            QuestionFragment().apply {
                arguments = Bundle().apply {
                    currentQuestionIndex = index
                }
            }
    }
}