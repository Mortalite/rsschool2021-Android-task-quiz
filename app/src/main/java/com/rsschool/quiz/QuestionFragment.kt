package com.rsschool.quiz

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.rsschool.quiz.databinding.FragmentQuestionBinding

class QuestionFragment : Fragment() {

    private var _binding: FragmentQuestionBinding? = null
    private val binding get() = _binding!!
    private var questListener: IQuestionListener? = null
    private var currentQuestion: Question? = null
    private var currentQuestionIndex = 0

    override fun onAttach(context: Context) {
        super.onAttach(context)

        questListener = context as IQuestionListener
    }

    override fun onGetLayoutInflater(savedInstanceState: Bundle?): LayoutInflater {
        with(questListener) {
            this?.setFragmentTheme(currentQuestionIndex)
        }
        return super.onGetLayoutInflater(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentQuestionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.toolbarBack.setOnClickListener { loadPrevQuestion() }
        binding.radioGroup.setOnCheckedChangeListener { _, checkedId -> checkedChangeListener(checkedId) }
        binding.back.setOnClickListener { loadPrevQuestion() }
        binding.next.setOnClickListener { loadNextQuestion() }

        drawQuestion()
    }

    private fun loadPrevQuestion() {
        currentQuestionIndex -= 1
        with(questListener) {
            this?.openQuestionFragment(currentQuestionIndex)
        }
    }

    private fun loadNextQuestion() {
        currentQuestionIndex += 1
        with(questListener) {
            this?.openQuestionFragment(currentQuestionIndex)
        }
    }

    private fun drawQuestion() {
        with(questListener) {
            currentQuestion = this?.getQuestion(currentQuestionIndex)
        }

        isResultFragment()
        isSelectedCheck()
        isActiveBack()
        isNextSubmit()
        toolbarManager()
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

    private fun isResultFragment() {
        val questionsSize = questListener?.getQuestionsSize() ?: 1

        if (currentQuestionIndex == questionsSize)
            questListener?.openResultFragment()
    }

    private fun isActiveBack() {
        binding.back.isEnabled = currentQuestionIndex != 0
    }

    private fun isNextSubmit() {
        val questionsSize = questListener?.getQuestionsSize() ?: 1

        if (currentQuestionIndex + 1 == questionsSize)
            binding.next.text = getString(R.string.next_btn_last_question)
        else
            binding.next.text = getString(R.string.next_btn_question)
    }

    private fun checkedChangeListener(checkedId: Int) {
        currentQuestion?.checkedId = checkedId
        currentQuestion?.selectedAnswer = getSelectedAnswer()
        binding.next.isEnabled = true
    }

    private fun isSelectedCheck() {
        val checkedAnswer = currentQuestion?.checkedId ?: -1

        if (checkedAnswer != -1) {
            binding.radioGroup.check(checkedAnswer)
        }
        else {
            binding.radioGroup.clearCheck()
            binding.next.isEnabled = false
        }
    }

    private fun getSelectedAnswer(): Int {
        return when (currentQuestion?.checkedId) {
            binding.radio0.id -> 0
            binding.radio1.id -> 1
            binding.radio2.id -> 2
            binding.radio3.id -> 3
            binding.radio4.id -> 4
            else -> -1
        }
    }

    private fun toolbarManager() {
        if (currentQuestionIndex == 0)
            binding.toolbarBack.visibility = View.INVISIBLE
        binding.toolbarText.text = getString(R.string.toolbar_text, currentQuestion?.id.toString())
    }

    interface IQuestionListener {
        fun openQuestionFragment(questionIndex: Int)
        fun openResultFragment()
        fun setFragmentTheme(questionIndex: Int)
        fun getQuestion(questNum: Int): Question?
        fun getQuestionsSize(): Int
        fun makeToast(msg: String)
    }

    companion object {
    @JvmStatic
    fun newInstance(questionIndex: Int) =
        QuestionFragment().apply {
            arguments = Bundle().apply {
                currentQuestionIndex = questionIndex
            }
        }
    }
}