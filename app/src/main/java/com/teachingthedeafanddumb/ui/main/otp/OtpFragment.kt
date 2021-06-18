package com.teachingthedeafanddumb.ui.main.otp

import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.gson.Gson
import com.teachingthedeafanddumb.R
import com.teachingthedeafanddumb.data.model.Role
import com.teachingthedeafanddumb.data.model.UserModel
import com.teachingthedeafanddumb.other.Constants
import com.teachingthedeafanddumb.other.Constants.FIELD_PHONE
import com.teachingthedeafanddumb.other.Constants.REF_USERS
import com.teachingthedeafanddumb.utils.CustomLoading.Companion.hideProgressBar
import com.teachingthedeafanddumb.utils.CustomLoading.Companion.showProgressBar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_otp.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject


@AndroidEntryPoint
class OtpFragment : Fragment(R.layout.fragment_otp) {

    var refUser = FirebaseDatabase.getInstance().getReference(REF_USERS)


    lateinit var auth: FirebaseAuth

    val args: OtpFragmentArgs by navArgs()

    lateinit var phoneNumber: String

    //lateinit var countDownTimer: CountDownTimer

    private var storedVerificationId = ""

    lateinit var verificationCallBack: PhoneAuthProvider.OnVerificationStateChangedCallbacks
    private  var resendToken: PhoneAuthProvider.ForceResendingToken? = null


    @Inject
    lateinit var sharedPref: SharedPreferences

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        setListener()

        phoneNumber?.let { sendOtp(it) }

    }


    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {

        showProgressBar(requireContext(), false)

        auth.signInWithCredential(credential)
            .addOnCompleteListener { task ->

                if (task.isSuccessful) {

                    val user = task.result?.user

                                val loginRequest = user?.uid?.let {

                                    //LoginRequest
                                    refUser.orderByChild(FIELD_PHONE).equalTo(auth.currentUser!!.phoneNumber!!)
                                        .addListenerForSingleValueEvent(object  : ValueEventListener{
                                            override fun onCancelled(error: DatabaseError) {


                                            }

                                            override fun onDataChange(snapshot: DataSnapshot) {

                                                if (snapshot != null && snapshot.exists()) {

                                                    var user : UserModel?=null

                                                    snapshot.children.forEach{

                                                        user = it.getValue(UserModel::class.java)!!

                                                    }

                                                    hideProgressBar()

                                                    //get data to save into pref
                                                    sharedPref.edit()
                                                        .putString(Constants.KEY_USER_MODEL_JSON,  Gson().toJson(user))
                                                        .apply()

                                                    if (user!!.role == Role.STUDENT)
                                                    navigateFirstTabWithClearStack()
                                                    else
                                                        navigateToStudents()



                                                    //Open Home

                                                } else {

                                                    hideProgressBar()

                                                    val bundle = Bundle()
                                                    bundle.putString("phoneNumber", phoneNumber)


                                                    findNavController().navigate(
                                                        R.id.action_otpFragment_to_registerFragment,
                                                        bundle)

                                                    //KEY_NOT_EXISTS

                                                }

                                            }

                                        })


                                }


//                                Log.d("FCM", msg)


                    // ...
                } else {
                    hideProgressBar()
                    if (task.exception is FirebaseAuthInvalidCredentialsException) {
                        // The verification code entered was invalid
//                            YoYo.with(Techniques.Shake)
//                                .duration(1000)
//                                .playOn(binding.editOtp)
                        (task.exception as FirebaseAuthInvalidCredentialsException).printStackTrace()
                        Toast.makeText(requireContext(), "Verification failed!", Toast.LENGTH_LONG)
                            .show()
                        if (edt_otp!=null)
                            edt_otp.setText("")
                    }
                }
//                countDownTimer.cancel()
//                if (txt_timer_to_resend!=null)
//                txt_timer_to_resend.isEnabled = true

            }


    }


    private fun setListener() {

        edt_otp.addTextChangedListener {

            if (edt_otp.text.toString().isNotEmpty() && edt_otp.text.toString().length == 6) {
                if (storedVerificationId.isNotEmpty()) {
                    val credential = PhoneAuthProvider.getCredential(
                        storedVerificationId,
                        edt_otp.text.toString()
                    )
                    signInWithPhoneAuthCredential(credential)
                }
            }

        }


        verificationCallBack = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            override fun onVerificationCompleted(p0: PhoneAuthCredential) {
                Toast.makeText(requireContext(), "Verification Successful!", Toast.LENGTH_LONG)
                    .show()
//                edt_otp.setText(p0.smsCode)
                signInWithPhoneAuthCredential(p0)
            }

            override fun onVerificationFailed(p0: FirebaseException) {

                hideProgressBar()

                p0.printStackTrace()

                Toast.makeText(requireContext(), "Verification failed!", Toast.LENGTH_LONG).show()
                edt_otp.setText("")
                //txt_timer_to_resend.isEnabled = true
            }

            override fun onCodeSent(
                verificationId: String,
                token: PhoneAuthProvider.ForceResendingToken
            ) {

                hideProgressBar()
                storedVerificationId = verificationId
                resendToken = token
//                txt_timer_to_resend.isEnabled = false
//                countDownTimer.start()
            }

            override fun onCodeAutoRetrievalTimeOut(p0: String) {
                super.onCodeAutoRetrievalTimeOut(p0)
//                txt_timer_to_resend.isEnabled = true
//                Toast.makeText(requireContext(), "Verification failed!", Toast.LENGTH_LONG).show()
//                Timber.w("ERORRR + " + p0)

            }
        }


//        txt_timer_to_resend.setOnClickListener {
//            phoneNumber?.let {
//                resendToken?.let {
//
//                    phoneNumber?.let {
//                        resendToken?.let {
//                            resendVerificationCode(phoneNumber, resendToken!!)
//                        }
//                    }
//                }
//            }
//        }

        toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()

        }
    }

    private fun initView() {

        auth = FirebaseAuth.getInstance()

        phoneNumber = args.phoneNumber

        txt_phone_verify.text =
            StringBuilder().append(getString( R.string.verify_number)).append(" ")
                .append(phoneNumber)
        txt_note_automatically_send.text =
            StringBuilder().append(getString( R.string.waiting_to_automatically_detect_an_sms_send_to))
                .append(" ").append(phoneNumber).append(".")

    }

    private fun sendOtp(number: String) {
//        progressDialog.setMessage("Sending OTP")

        PhoneAuthProvider.getInstance().verifyPhoneNumber(
            number, // Phone number to verify
            60, // Timeout duration
            TimeUnit.SECONDS, // Unit of timeout
            requireActivity(), // Activity (for callback binding)
            verificationCallBack
        ) // OnVerificationStateChangedCallbacks
    }


    fun resendVerificationCode(number: String, token: PhoneAuthProvider.ForceResendingToken) {

        PhoneAuthProvider.getInstance().verifyPhoneNumber(
            number,        // Phone number to verify
            60,                 // Timeout duration
            TimeUnit.SECONDS,   // Unit of timeout
            requireActivity(),               // Activity (for callback binding)
            verificationCallBack,         // OnVerificationStateChangedCallbacks
            token // ForceResendingToken from callbacks
        );

    }



    fun navigateFirstTabWithClearStack() {
        val navController = findNavController()
        val navHostFragment: NavHostFragment =
            requireActivity().supportFragmentManager.findFragmentById( R.id.nav_host_fragment) as NavHostFragment
        val inflater = navHostFragment.navController.navInflater
        val graph = inflater.inflate( R.navigation.nav_graph_home)
        graph.startDestination =  R.id.homeFragment

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