package com.teachingthedeafanddumb.ui.main.login

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.teachingthedeafanddumb.R
import kotlinx.android.synthetic.main.fragment_login.*


class LoginFragment : Fragment(R.layout.fragment_login) {


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        ccp.registerCarrierNumberEditText(input_phone_number)

        btn_login.setOnClickListener {

            val phone =  input_phone_number.text.toString().trim()
            if (phone.isEmpty())
            {
                input_phone_number.error = ""
                return@setOnClickListener
            }

            val  bundle = Bundle()
            bundle.putString("phoneNumber" , ccp.fullNumberWithPlus)
            findNavController().navigate(R.id.action_loginFragment_to_otpFragment,bundle)
        }


    }
}