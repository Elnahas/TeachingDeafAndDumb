package com.teachingthedeafanddumb.ui.main.register

import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ServerValue
import com.google.firebase.storage.FirebaseStorage
import com.google.gson.Gson
import com.teachingthedeafanddumb.R
import com.teachingthedeafanddumb.data.model.Role
import com.teachingthedeafanddumb.data.model.UserModel
import com.teachingthedeafanddumb.other.Constants
import com.teachingthedeafanddumb.other.Constants.FOLDER_PROFILE_IMAGE
import com.teachingthedeafanddumb.other.Constants.REF_USERS
import com.teachingthedeafanddumb.ui.viewmodel.UserViewModel
import com.teachingthedeafanddumb.utils.CustomLoading.Companion.hideProgressBar
import com.teachingthedeafanddumb.utils.CustomLoading.Companion.showProgressBar
import com.teachingthedeafanddumb.utils.Resource
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_register.*
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@AndroidEntryPoint
class RegisterFragment : Fragment(R.layout.fragment_register) {

    var uri: Uri? = null
    val args : RegisterFragmentArgs by navArgs()

    lateinit var firebaseAuth : FirebaseAuth

    var refUser = FirebaseDatabase.getInstance().getReference(REF_USERS)

    val viewModel : UserViewModel by viewModels()

    @Inject
    lateinit var sharedPref: SharedPreferences

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        firebaseAuth = FirebaseAuth.getInstance()

        val phoneNumber = args.phoneNumber
        input_phone_number.setText(phoneNumber)
        img_user.setOnClickListener {
            changePhotoUser()
        }


        btn_sign_up.setOnClickListener {
            registerUser()
        }

        viewModel.userMutableLiveData.observe(viewLifecycleOwner , Observer {

            when(it){

                is Resource.Success->{
                    hideProgressBar()
                    sharedPref.edit()
                        .putString(Constants.KEY_USER_MODEL_JSON,  Gson().toJson(it.data))
                        .apply()

//                    val navOptions = NavOptions.Builder()
//                        .setPopUpTo(R.id.registerFragment, true)
//                        .setPopUpTo(R.id.loginFragment, true)
//                        .build()
//                    findNavController().navigate(
//                        R.id.action_registerFragment_to_restaurantFragment,
//                        savedInstanceState,
//                        navOptions
//                    )
                    navigateFirstTabWithClearStack()

                }

                is Resource.Loading->{
                    showProgressBar(requireContext() , false)
                }
            }

        })
    }



    private fun registerUser() {

        val full_name = input_name.text.toString().trim()

        val userModel = UserModel()

        //set Data
        userModel.apply {

            fullName = full_name
            id = firebaseAuth.uid!!
            createAt = ServerValue.TIMESTAMP
            role = if (radioStu.isChecked) Role.STUDENT else Role.TEACHER
            phone = input_phone_number.text.toString()

        }

        viewModel.registerNewUser(userModel , uri)


    }

    fun navigateFirstTabWithClearStack() {
        val navController = findNavController()
        val navHostFragment: NavHostFragment =
            requireActivity().supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val inflater = navHostFragment.navController.navInflater
        val graph = inflater.inflate(R.navigation.nav_graph_home)
        graph.startDestination = R.id.homeFragment

        navController.graph = graph
    }


    private fun changePhotoUser() {

        CropImage.activity()
            .setAspectRatio(1, 1)
            .setRequestedSize(250, 250)
            .setCropShape(CropImageView.CropShape.OVAL)
            .start(requireContext()!!,this)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        super.onActivityResult(requestCode, resultCode, data)


        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE
            && resultCode == Activity.RESULT_OK && data != null
        ) {

            uri = CropImage.getActivityResult(data).uri
            img_user.setImageURI(uri)
//            img_upload.downloadAndSetImage(it)


        }
    }
}