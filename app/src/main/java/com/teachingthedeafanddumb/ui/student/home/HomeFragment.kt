package com.teachingthedeafanddumb.ui.student.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.teachingthedeafanddumb.R
import com.teachingthedeafanddumb.adapters.LessonAdapter
import com.teachingthedeafanddumb.data.model.LessonModel
import com.teachingthedeafanddumb.other.Constants.REF_LESSONS
import elnahas.deliveroo.utilits.SpacingItemDecoration
import kotlinx.android.synthetic.main.fragment_home.*


class HomeFragment : Fragment(R.layout.fragment_home) {


    lateinit var lessonAdapter : LessonAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()

        FirebaseDatabase.getInstance().getReference(REF_LESSONS)
            .addListenerForSingleValueEvent(object:ValueEventListener{
                override fun onCancelled(error: DatabaseError) {


                }

                override fun onDataChange(snapshot: DataSnapshot) {

                    val listLesson = ArrayList<LessonModel>()

                    snapshot.children.forEach {
                        val lesson = it.getValue(LessonModel::class.java)
                        lesson!!.id = it.key

                        listLesson.add(lesson)
                    }

                    lessonAdapter.differ.submitList(listLesson)


                }

            })

        lessonAdapter.setOnItemClickListener { lessonModel, view ->

            val bundle = Bundle()
            bundle.putSerializable("lessonModel" , lessonModel)
            findNavController().navigate(R.id.action_homeFragment_to_lessonFragment , bundle)
        }

    }

    private fun setupRecyclerView() {
        lessonAdapter = LessonAdapter()
        recycler_lesson.apply {
            adapter = lessonAdapter
            layoutManager = LinearLayoutManager(activity )
            addItemDecoration(SpacingItemDecoration(requireContext(), R.dimen.app_margin))
        }

    }

}