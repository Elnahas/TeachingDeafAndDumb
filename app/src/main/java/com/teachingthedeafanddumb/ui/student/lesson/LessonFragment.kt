package com.teachingthedeafanddumb.ui.student.lesson

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.teachingthedeafanddumb.R
import com.teachingthedeafanddumb.data.QuizModel
import kotlinx.android.synthetic.main.fragment_lesson.*


class LessonFragment : Fragment(R.layout.fragment_lesson) {

    val args : LessonFragmentArgs by navArgs()


    @SuppressLint("SetJavaScriptEnabled")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        lifecycle.addObserver(youTubePlayerView)

        youTubePlayerView.addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
            override fun onReady(youTubePlayer: YouTubePlayer) {
                val videoId = args.lessonModel.videoUrl
                youTubePlayer.loadVideo(videoId!!, 0f)
            }
        })

        txt_lesson_title.text = args.lessonModel.lessonTitle

        img_back.setOnClickListener {
            findNavController().navigateUp()

        }

        btn_quiz.setOnClickListener {
            val bundle = Bundle()
            bundle.putSerializable("lessonModel" , args.lessonModel)
            findNavController().navigate(R.id.action_lessonFragment_to_quizFragment , bundle)
        }

    }

}