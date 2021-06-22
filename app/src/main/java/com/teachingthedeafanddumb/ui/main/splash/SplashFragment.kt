package com.teachingthedeafanddumb.ui.main.splash

import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.gson.Gson
import com.teachingthedeafanddumb.R
import com.teachingthedeafanddumb.data.model.Role
import com.teachingthedeafanddumb.data.model.UserModel
import com.teachingthedeafanddumb.other.Constants
import com.teachingthedeafanddumb.other.Constants.FIRST_TIME_TOGGLE
import com.teachingthedeafanddumb.ui.viewmodel.UserViewModel
import com.teachingthedeafanddumb.utils.Resource
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SplashFragment : Fragment(R.layout.fragment_splash) {

    val viewModel: UserViewModel by viewModels()
    val auth = FirebaseAuth.getInstance()

    @Inject
    lateinit var sharedPref: SharedPreferences
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireActivity().window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        Handler().postDelayed({
            checkLoginUser()
        }, 1000)

        viewModel.isUserExistsMutableLiveData.observe(viewLifecycleOwner,
            androidx.lifecycle.Observer {

                when (it) {

                    is Resource.Success -> {
                        goToHomeActivity(it.data)
                    }

                    is Resource.Error -> {

                        when (it.message) {

                            Constants.KEY_NOT_EXISTS -> {
                                auth.signOut()
                                findNavController().navigate(R.id.loginFragment)
                            }
                            else -> {
                                auth.signOut()
                                findNavController().navigate(R.id.loginFragment)
//                            findNavController().navigate(R.id.action_splashFragment_to_loginFragment)
                            }
                        }

                    }
                }
            })

    }


    private fun checkLoginUser() {

//        val FIRST_TIME_TOGGLE = sharedPref.getBoolean(FIRST_TIME_TOGGLE, false)
//
//        if (!FIRST_TIME_TOGGLE) {
//            findNavController().navigate(R.id.action_splashFragment_to_introFragment)


//        } else {

            if (auth.currentUser != null) {

                auth.currentUser?.let {
                    it.phoneNumber?.let {
                        viewModel.isUserExists(it)
                    }
                }

            } else
                requireActivity().findNavController(R.id.nav_host_fragment).navigate(
                    R.id.action_splashFragment_to_loginFragment)


//        }

    }

    private fun goToHomeActivity(userModel: UserModel?) {


        //get data to save into pref
        sharedPref.edit()
            .putString(Constants.KEY_USER_MODEL_JSON, Gson().toJson(userModel))
            .apply()

        if (userModel!!.role!! == Role.STUDENT)
        navigateFirstTabWithClearStack()
        else
            navigateToStudents()



    }


    fun navigateFirstTabWithClearStack() {

        val navController = Navigation.findNavController(requireActivity() , R.id.nav_host_fragment)
        val navHostFragment: NavHostFragment =
            requireActivity().supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val inflater = navHostFragment.navController.navInflater
        val graph = inflater.inflate(R.navigation.nav_graph_home)
        graph.startDestination = R.id.homeFragment

        navController.graph = graph
    }

    fun navigateToStudents() {

        val navController = Navigation.findNavController(requireActivity() , R.id.nav_host_fragment)
        val navHostFragment: NavHostFragment =
            requireActivity().supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val inflater = navHostFragment.navController.navInflater
        val graph = inflater.inflate(R.navigation.nav_graph_home)
        graph.startDestination = R.id.studentFragment

        navController.graph = graph
    }
}