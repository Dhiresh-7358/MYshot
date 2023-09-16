package loginProcess

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.SpannedString
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.myshot.MainActivity
import com.example.myshot.R
import com.example.myshot.databinding.ActivityLoginBinding
import com.google.android.gms.common.internal.Objects.ToStringHelper
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.*
import java.util.concurrent.TimeUnit

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var  mAuth: FirebaseAuth
    private lateinit var mNumber: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mAuth=FirebaseAuth.getInstance()

        val hint = "Mobile Numbers*"
        val spannable = SpannableString(hint)
        val pinkColor = ContextCompat.getColor(this, R.color.pink)
        val colorSpan = ForegroundColorSpan(pinkColor)

        spannable.setSpan(colorSpan, 14, 15, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        binding.mobileNumber.hint = spannable

        val desc = "By continuing, I agree to the Term Use &\n" + "Privacy Policy "
        val spannable2 = SpannableString(desc)

        spannable2.setSpan(colorSpan, 30, 38, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        val colorSpan2 = ForegroundColorSpan(pinkColor)
       spannable2.setSpan(colorSpan2, 41, 55, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)


        binding.termsCondition.text = SpannedString(spannable2)

        binding.loginContinue.setOnClickListener {
            mNumber=binding.mobileNumber.text.toString()
            if(mNumber.length==10){
                mNumber="+91$mNumber"

                sendOTP()
            }
            else{
                Log.d("fire","invalid no. $mNumber number")
            }

        }
    }

    private fun sendOTP() {
        Log.d("fire","ON verification start")

        val options = PhoneAuthOptions.newBuilder(mAuth)
            .setPhoneNumber(mNumber) // Phone number to verify
            .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
            .setActivity(this) // Activity (for callback binding)
            .setCallbacks(callbacks) // OnVerificationStateChangedCallbacks
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)


    }

    private val  callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        override fun onVerificationCompleted(credential: PhoneAuthCredential) {
            Log.d("fire","ON verification complete")

            signInWithPhoneAuthCredential(credential)
        }

        override fun onVerificationFailed(e: FirebaseException) {
            // This callback is invoked in an invalid request for verification is made,
            // for instance if the the phone number format is not valid.

            if (e is FirebaseAuthInvalidCredentialsException) {
                Log.d("tag","ON verification failed ${e.toString()}")
                // Invalid request
            } else if (e is FirebaseTooManyRequestsException) {
                Log.d("tag","ON verification failed ${e.toString()}")
                // The SMS quota for the project has been exceeded
            }
            // Show a message and update the UI
        }

        override fun onCodeSent(
            verificationId: String,
            token: PhoneAuthProvider.ForceResendingToken,
        ) {

                val intent=Intent(this@LoginActivity,OTPActivity::class.java)
                intent.putExtra("OTP",verificationId)
                intent.putExtra("resendToken",token)
                intent.putExtra("mNumber",mNumber )
            Log.d("fire","send to otp activity")
                startActivity(intent)


        }
    }

    private fun toMain(){
        startActivity(Intent(this,MainActivity::class.java ))
    }
//    override fun onStart() {
//        super.onStart()
//        if(mAuth.currentUser!=null){
//            startActivity(Intent(this,MainActivity::class.java ))
//        }
//
//    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        mAuth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "authSuccess", Toast.LENGTH_SHORT).show()
                  //  toMain()
                } else {
                        Log.d("tag","signInWithPhoneAuthCredential ${task.exception.toString()}")
                    if (task.exception is FirebaseAuthInvalidCredentialsException) {
                        // The verification code entered was invalid
                    }
                    // Update UI
                }
            }
    }

}