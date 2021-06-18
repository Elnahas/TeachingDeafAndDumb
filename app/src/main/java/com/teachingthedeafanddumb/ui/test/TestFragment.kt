package com.teachingthedeafanddumb.ui.test

import android.os.Bundle
import android.os.UserHandle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.teachingthedeafanddumb.R
import com.teachingthedeafanddumb.data.model.UserDataModel
import com.teachingthedeafanddumb.other.Constants.REF_USER
import kotlinx.android.synthetic.main.fragment_test.*


class TestFragment : Fragment(R.layout.fragment_test) {


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        btn_save.setOnClickListener {

            var userName = edt_user_name.text.toString()
            var phoneNumber = edt_phone.text.toString()
            var address = edt_address.text.toString()


            if (userName.isEmpty()) {
                Toast.makeText(requireContext(), "يجب ادخال اسم المستخدم", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            if (phoneNumber.isEmpty()){
                Toast.makeText(requireContext() , "يجب ادخال رقم الهاتف" , Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            if (address.isEmpty()){
                Toast.makeText(requireContext() , "يجب ادخال عنوان المستخدم" , Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }


            val userdata = UserDataModel(userName , phoneNumber , address)

            FirebaseDatabase.getInstance().getReference(REF_USER)
                .push()
                .setValue(userdata)
                .addOnSuccessListener {
                    Toast.makeText(requireContext() , "تم حفظ البيانات بنجاح" , Toast.LENGTH_LONG).show()
                }.addOnFailureListener {
                    Toast.makeText(requireContext() , "يوجد مشكله في الاتصال" , Toast.LENGTH_LONG).show()
                }




            //Toast.makeText(requireContext() , "Hello" , Toast.LENGTH_LONG).show()
        }

        btn_get.setOnClickListener {

            FirebaseDatabase.getInstance().getReference(REF_USER)
                .addListenerForSingleValueEvent(object : ValueEventListener{
                    override fun onCancelled(error: DatabaseError) {
                    }

                    override fun onDataChange(snapshot: DataSnapshot) {

                        var list : ArrayList<UserDataModel> = ArrayList()

                        snapshot.children.forEach { datasnap->

                            var singleData =  datasnap.getValue(UserDataModel::class.java)

                            list.add(singleData!!)

                            Log.d("TESTTEST" , singleData!!.userName)

                        }
                    }

                })

        }
    }
}