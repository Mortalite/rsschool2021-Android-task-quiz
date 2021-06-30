package com.rsschool.quiz

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.rsschool.quiz.databinding.FragmentQuestionBinding


class QuestionFragment : Fragment() {

    var _binding: FragmentQuestionBinding? = null
    val binding get() = _binding!!
    var currentQuestion: Question? = null
    var currentQuestionIndex = 0
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

        binding.radioGroup.setOnCheckedChangeListener { _, checkedId -> checkedChangeListener(checkedId) }
        binding.back.setOnClickListener { loadPrevQuestion() }
        binding.next.setOnClickListener { loadNextQuestion() }

        drawQuestion()
    }

    private fun loadPrevQuestion() {
//        listener?.makeToast("index = " + currentQuestionIndex)
        currentQuestionIndex -= 1
        drawQuestion()
    }

    private fun loadNextQuestion() {
        currentQuestionIndex += 1
        drawQuestion()
    }

    private fun drawQuestion() {
        currentQuestion = listener?.getQuestion(currentQuestionIndex)

        isResultFragment()
        isSelectedCheck()
        isActiveBack()
        isNextSubmit()
        binding.let {
            it.QuestionView.text = currentQuestion?.question ?: "Not set"
            it.radio0.text = currentQuestion?.option0 ?: "Not set"
            it.radio1.text = currentQuestion?.option1 ?: "Not set"
            it.radio2.text = currentQuestion?.option2 ?: "Not set"
            it.radio3.text = currentQuestion?.option3 ?: "Not set"
            it.radio4.text = currentQuestion?.option4 ?: "Not set"
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun isResultFragment() {

    }

    fun isActiveBack() {
        binding.back.setEnabled(currentQuestionIndex != 0)
    }

    fun isNextSubmit() {
        val questionsSize = listener?.getQuestionsSize() ?: 1

        if (currentQuestionIndex + 1 == questionsSize)
            binding.next.text = "Sumbit"
        else
            binding.next.text = "Next"
    }

    fun checkedChangeListener(checkedId: Int) {
        currentQuestion?.chosen_answer = checkedId
    }

    fun isSelectedCheck() {
        val checkedAnswer = currentQuestion?.chosen_answer ?: -1

        if (checkedAnswer != -1)
            binding.radioGroup.check(checkedAnswer)
        else
            binding.radioGroup.clearCheck()
    }

    interface IActionPerformedListener {
        fun getQuestion(questNum: Int): Question?
        fun getQuestionsSize(): Int
        fun makeToast(msg: String)
    }

    companion object {
    @JvmStatic
    fun newInstance() =
        QuestionFragment().apply {
            arguments = Bundle().apply {}
        }
    }
}