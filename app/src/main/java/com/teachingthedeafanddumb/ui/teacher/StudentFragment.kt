package com.teachingthedeafanddumb.ui.teacher

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
import com.teachingthedeafanddumb.adapters.UsersAdapter
import com.teachingthedeafanddumb.data.model.Role
import com.teachingthedeafanddumb.data.model.UserModel
import com.teachingthedeafanddumb.other.Constants
import com.teachingthedeafanddumb.utils.SpacingItemDecoration
import kotlinx.android.synthetic.main.fragment_student.*

class StudentFragment : Fragment(R.layout.fragment_student) {

    lateinit var usersAdapter : UsersAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        setupRecyclerView()

        FirebaseDatabase.getInstance().getReference(Constants.REF_USERS)
            .orderByChild("role")
            .equalTo(Role.STUDENT.name)
            .addValueEventListener(object: ValueEventListener {
                override fun onCancelled(error: DatabaseError) {

                }

                override fun onDataChange(snapshot: DataSnapshot) {

                    val listLesson = ArrayList<UserModel>()
                    snapshot.children.forEach {
                        val user = it.getValue(UserModel::class.java)
                        user!!.id = it.key

                        listLesson.add(user)
                    }

                    usersAdapter.differ.submitList(listLesson)


                }

            })

        usersAdapter.setOnItemClickListener { user, view ->

            val bundle = Bundle()
            bundle.putSerializable("userModel" , user)
            findNavController().navigate(R.id.action_studentFragment_to_resultStudentsFragment , bundle)
        }

    }

    private fun setupRecyclerView() {
        usersAdapter = UsersAdapter()
        recycler_students.apply {
            adapter = usersAdapter
            layoutManager = LinearLayoutManager(activity )
            addItemDecoration(SpacingItemDecoration(requireContext(), R.dimen.app_margin))
        }

    }

}