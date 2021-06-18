package com.teachingthedeafanddumb.ui.student.result

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.teachingthedeafanddumb.R
import kotlinx.android.synthetic.main.fragment_result.*

class ResultFragment : Fragment(R.layout.fragment_result) {

    val args :ResultFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        results_correct_text.text = args.resultModel.resultCorrect.toString()
        results_wrong_text.text = args.resultModel.resultWrong.toString()
        results_missed_text.text = args.resultModel.resultMissed.toString()

        //Calculate Progress
        val total: Long = args.resultModel.resultCorrect!!.toLong() + args.resultModel.resultWrong!!.toLong()+ args.resultModel.resultMissed!!.toLong()
        val percent: Long = args.resultModel.resultCorrect!!.toLong() * 100 / total

        results_percent.text = "$percent%"
        results_progress.progress = percent.toInt()

        btn_home.setOnClickListener {

            findNavController().navigate(R.id.action_resultFragment_to_homeFragment)

        }


    }

}