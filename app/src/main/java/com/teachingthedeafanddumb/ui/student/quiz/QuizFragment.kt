package com.teachingthedeafanddumb.ui.student.quiz

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.ImageView
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.teachingthedeafanddumb.R
import com.teachingthedeafanddumb.data.model.QuizModel
import com.teachingthedeafanddumb.data.model.ResultModel
import com.teachingthedeafanddumb.other.Constants.REF_RESULTS
import com.teachingthedeafanddumb.utils.CustomLoading.Companion.hideProgressBar
import com.teachingthedeafanddumb.utils.CustomLoading.Companion.showProgressBar
import kotlinx.android.synthetic.main.fragment_quiz.*


class QuizFragment : Fragment(R.layout.fragment_quiz), View.OnClickListener {

    val args: QuizFragmentArgs by navArgs()

    private var mCurrentPosition: Int = 1
    private var mQuestionList: ArrayList<QuizModel> = ArrayList()
    private var mSelectedOptionPosition: Int = 0

    var correct: Int = 0
    var wrong: Int = 0
    var missed: Int = 0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        args.lessonModel.quiz!!.forEach {

            mQuestionList.add(it.value)
        }

        mQuestionList!!.shuffled()
        progressBar.max = mQuestionList!!.size
        setQuestion()

        imgA.setOnClickListener(this)
        imgB.setOnClickListener(this)
        imgC.setOnClickListener(this)
        imgD.setOnClickListener(this)

        txtA.setOnClickListener(this)
        txtB.setOnClickListener(this)
        txtC.setOnClickListener(this)
        txtD.setOnClickListener(this)

        btn_submit.setOnClickListener(this)
    }

    @SuppressLint("SetTextI18n")
    private fun setQuestion() {

        val question = mQuestionList!![mCurrentPosition - 1]

        defaultOptionsView()

        if (mCurrentPosition == mQuestionList!!.size) {
            btn_submit.text = getString(R.string.finish)
        } else {
            btn_submit.text = getString(R.string.submit)
        }

        progressBar.progress = mCurrentPosition
        tv_progress.text = "$mCurrentPosition" + "/" + progressBar.max

        if (!question.image) {
            tv_question.text = question.question
            img_question.visibility = View.GONE
            tv_question.visibility = View.VISIBLE

        } else {
            Glide.with(requireContext()).load(question.question)
                .into(img_question)
            img_question.visibility = View.VISIBLE
            tv_question.visibility = View.GONE

        }

        if (question.text)
        {
            txtA.text = question.optionOne
            txtB.text = question.optionTwo
            txtC.text = question.optionThree
            txtD.text = question.optionFour

            txtA.visibility = View.VISIBLE
            txtB.visibility = View.VISIBLE
            txtC.visibility = View.VISIBLE
            txtD.visibility = View.VISIBLE

            imgA.visibility = View.INVISIBLE
            imgB.visibility = View.INVISIBLE
            imgC.visibility = View.INVISIBLE
            imgD.visibility = View.INVISIBLE
        }
        else
        {
            Glide.with(requireContext()).load(question.optionOne)
                .into(imgA)

            Glide.with(requireContext()).load(question.optionTwo)
                .into(imgB)

            Glide.with(requireContext()).load(question.optionThree)
                .into(imgC)

            Glide.with(requireContext()).load(question.optionFour)
                .into(imgD)

            txtA.visibility = View.INVISIBLE
            txtB.visibility = View.INVISIBLE
            txtC.visibility = View.INVISIBLE
            txtD.visibility = View.INVISIBLE

            imgA.visibility = View.VISIBLE
            imgB.visibility = View.VISIBLE
            imgC.visibility = View.VISIBLE
            imgD.visibility = View.VISIBLE
        }


    }

    private fun defaultOptionsView() {


        chkA.setImageResource(R.drawable.ic_check_off)
        chkB.setImageResource(R.drawable.ic_check_off)
        chkC.setImageResource(R.drawable.ic_check_off)
        chkD.setImageResource(R.drawable.ic_check_off)

    }

    override fun onClick(v: View?) {


        when (v?.id) {
            R.id.imgA , R.id.txtA  -> {
                selectedOptionView(chkA, 1)
            }
            R.id.imgB , R.id.txtB-> {
                selectedOptionView(chkB, 2)
            }
            R.id.imgC , R.id.txtC -> {
                selectedOptionView(chkC, 3)
            }
            R.id.imgD , R.id.txtD -> {
                selectedOptionView(chkD, 4)
            }

            R.id.btn_submit -> {

                if (mSelectedOptionPosition == 0) {

                    if (btn_submit.text == getString(R.string.submit))
                        missed += 1

                    mCurrentPosition++

                    when {
                        mCurrentPosition <= mQuestionList!!.size -> {
                            setQuestion()
                        }
                        else -> {


                        }
                    }
                } else {
                    val question = mQuestionList?.get(mCurrentPosition - 1)

                    if (question!!.correctOption != mSelectedOptionPosition) {
                        //answerView(mSelectedOptionPosition, R.drawable.wrong_option_border_bg)
                        wrong += 1
                    } else {
                        correct += 1
                    }

                    //answerView(question.correctOption!!, R.drawable.correct_option_border_bg)

                    if (mCurrentPosition == mQuestionList!!.size) {
                        btn_submit.text = getString(R.string.finish)
                    } else {
                        btn_submit.text = getString(R.string.submit)
                    }

                    //stopClickOptionsView()

                    mSelectedOptionPosition = 0


                    mCurrentPosition++

                    when {
                        mCurrentPosition <= mQuestionList!!.size -> {
                            setQuestion()
                        }
                        else -> {


                            showProgressBar(requireContext(), false)

                            //Send to Db Result
                            val result =
                                ResultModel()
                            result.createAt = System.currentTimeMillis().toString()
                            result.resultCorrect = correct
                            result.resultMissed = missed
                            result.resultWrong = wrong
                            result.lessonNumber = args.lessonModel.lessonNumber
                            result.uid =  FirebaseAuth.getInstance().uid
                            result.lessonId = args.lessonModel.id
                            result.uid_lessonId = FirebaseAuth.getInstance().uid + "_" + args.lessonModel.id

                            FirebaseDatabase.getInstance()
                                .getReference(REF_RESULTS)
                                .child(result.createAt.toString())
                                .setValue(result).addOnSuccessListener {

                                    hideProgressBar()

                                    val bundle = Bundle()

                                    bundle.putSerializable("resultModel", result)

                                    findNavController().navigate(
                                        R.id.action_quizFragment_to_resultFragment,
                                        bundle
                                    )

                                }


                        }
                    }
                }

            }
        }
    }

    private fun selectedOptionView(check: ImageView, selectedOptionNum: Int) {

        defaultOptionsView()

        mSelectedOptionPosition = selectedOptionNum

        check.setImageResource(R.drawable.ic_check_on)
    }


}