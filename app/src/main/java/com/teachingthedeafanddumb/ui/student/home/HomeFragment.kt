package com.teachingthedeafanddumb.ui.student.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.teachingthedeafanddumb.R
import com.teachingthedeafanddumb.adapters.LessonAdapter
import com.teachingthedeafanddumb.data.model.LessonModel
import com.teachingthedeafanddumb.other.Constants
import com.teachingthedeafanddumb.other.Constants.REF_LESSONS
import com.teachingthedeafanddumb.utils.SpacingItemDecoration
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

        img_exit.setOnClickListener {
            signOut()
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


    private fun signOut() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("تسجيل خروج")
            .setMessage("هل تريد حقا الخروج من التطبيق ؟")
            .setPositiveButton("نعم") { dialog, _ ->

                FirebaseAuth.getInstance().signOut()
                navigateFirstTabWithClearStack()

            }
            .setNegativeButton("لا") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    fun navigateFirstTabWithClearStack() {
        val navController = findNavController()
        val navHostFragment: NavHostFragment =
            requireActivity().supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val inflater = navHostFragment.navController.navInflater
        val graph = inflater.inflate(R.navigation.nav_graph_home)
        graph.startDestination = R.id.loginFragment

        navController.graph = graph
    }


}