package com.teachingthedeafanddumb.ui.teacher

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.teachingthedeafanddumb.R
import com.teachingthedeafanddumb.adapters.ResultAdapter
import com.teachingthedeafanddumb.data.model.ResultModel
import com.teachingthedeafanddumb.other.Constants
import com.teachingthedeafanddumb.utils.SpacingItemDecoration
import kotlinx.android.synthetic.main.fragment_result_students.*

class ResultStudentsFragment : Fragment(R.layout.fragment_result_students) {

    lateinit var resultAdapter : ResultAdapter
    val args : ResultStudentsFragmentArgs by  navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        setupRecyclerView()

        FirebaseDatabase.getInstance().getReference(Constants.REF_RESULTS)
            .orderByChild("uid")
            .equalTo(args.userModel.id)
            .addValueEventListener(object: ValueEventListener {
                override fun onCancelled(error: DatabaseError) {

                }

                override fun onDataChange(snapshot: DataSnapshot) {

                    val resultModel = ArrayList<ResultModel>()
                    snapshot.children.forEach {
                        val result = it.getValue(ResultModel::class.java)
                        result!!.id = it.key

                        resultModel.add(result)
                    }

                    resultAdapter.differ.submitList(resultModel)


                }

            })

        img_back.setOnClickListener {
            findNavController().navigateUp()
        }

        txt_title.text = args.userModel.fullName

    }

    private fun setupRecyclerView() {
        resultAdapter = ResultAdapter()
        recycler_view.apply {
            adapter = resultAdapter
            layoutManager = LinearLayoutManager(activity )
            addItemDecoration(SpacingItemDecoration(requireContext(), R.dimen.app_margin))
        }

    }

}