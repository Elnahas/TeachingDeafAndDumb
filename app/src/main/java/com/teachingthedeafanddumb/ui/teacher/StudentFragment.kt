package com.teachingthedeafanddumb.ui.teacher

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

        img_exit.setOnClickListener {
            signOut()
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