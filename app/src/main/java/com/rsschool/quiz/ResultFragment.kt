package com.rsschool.quiz

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.rsschool.quiz.databinding.FragmentResultBinding

class ResultFragment : Fragment() {

    private var _binding: FragmentResultBinding? = null
    private val binding get() = _binding!!
    private var listener: IResultListener? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)

        listener = context as IResultListener
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

        binding.scoreId.text =  listener?.getScore().toString() + "/" + listener?.getQuestionsSize().toString()
        binding.resetButton.setOnClickListener {
            with(listener) {
                this?.resetSelectedAnswers()
                this?.openQuestionFragment(0)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    interface IResultListener {
        fun openQuestionFragment(questionIndex: Int)
        fun resetSelectedAnswers()
        fun getScore(): Int
        fun getQuestionsSize(): Int
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