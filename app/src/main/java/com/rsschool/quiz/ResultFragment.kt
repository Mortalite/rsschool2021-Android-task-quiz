package com.rsschool.quiz

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.rsschool.quiz.databinding.FragmentResultBinding

class ResultFragment : Fragment() {

    private var _binding: FragmentResultBinding? = null
    private val binding get() = _binding!!
    private var resultListener: IResultListener? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)

        resultListener = context as IResultListener
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentResultBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.scoreId.text = resultListener?.getResult() ?: "Not set"
        binding.shareButton.setOnClickListener { shareQuiz() }
        binding.resetButton.setOnClickListener { resetQuiz() }
        binding.finishButton.setOnClickListener { finishQuiz() }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun shareQuiz() {
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_SUBJECT, "Quiz results!")
            putExtra(Intent.EXTRA_TEXT, resultListener?.generateResultMsg())
        }
        startActivity(Intent.createChooser(intent, "Choose email client..."))
    }

    private fun resetQuiz() {
        with(resultListener) {
            this?.resetSelectedAnswers()
            this?.openQuestionFragment(0)
        }
    }

    private fun finishQuiz() {
        with(resultListener) {
            this?.finishApp()
        }
    }

    interface IResultListener {
        fun openQuestionFragment(questionIndex: Int)
        fun resetSelectedAnswers()
        fun getResult(): String
        fun generateResultMsg(): String
        fun finishApp()
        fun makeToast(msg: String)
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            ResultFragment().apply {
                arguments = Bundle().apply {}
            }
    }
}