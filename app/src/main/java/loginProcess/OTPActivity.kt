package loginProcess

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.myshot.MainActivity
import com.example.myshot.R
import com.example.myshot.databinding.ActivityLoginBinding
import com.example.myshot.databinding.ActivityOtpactivityBinding
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.*
import com.google.firebase.auth.PhoneAuthProvider.ForceResendingToken
import java.util.concurrent.TimeUnit

class OTPActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOtpactivityBinding
    private lateinit var resent: TextView
    private lateinit var otp: String
    private lateinit var resendToken: PhoneAuthProvider.ForceResendingToken
    private lateinit var mNumber: String
    private lateinit var mAuth: FirebaseAuth
    private lateinit var typeOtp: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOtpactivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mAuth = FirebaseAuth.getInstance()

        otp = intent.getStringExtra("OTP").toString()
        resendToken = intent.getParcelableExtra("resendToken")!!
        mNumber = intent.getStringExtra("mNUmber").toString()

        val hint = "   OTP*"
        val spannable = SpannableString(hint)
        val pinkColor = ContextCompat.getColor(this, R.color.pink)
        val colorSpan = ForegroundColorSpan(pinkColor)

        spannable.setSpan(colorSpan, 6, 7, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        binding.otp.hint = spannable

        resent = binding.resentOtpButton

        resent.visibility = View.INVISIBLE

//        setTimer(this)

        binding.otpContinue.setOnClickListener {
            typeOtp = binding.otp.text.toString()
            verifyOtp()
            setTimer(this)
            resent.visibility = View.INVISIBLE

        }

        resent.setOnClickListener {
            resendOtp()
//            setTimer(this)
//            resent.visibility = View.INVISIBLE
        }

    }

    private fun resendOtp() {

        setTimer(this)
        resent.visibility = View.INVISIBLE

        val options = PhoneAuthOptions.newBuilder(mAuth)
            .setPhoneNumber(mNumber) // Phone number to verify
            .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
            .setActivity(this) // Activity (for callback binding)
            .setCallbacks(callbacks) // OnVerificationStateChangedCallbacks
            .setForceResendingToken(resendToken)
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    private val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        override fun onVerificationCompleted(credential: PhoneAuthCredential) {

            signInWithPhoneAuthCredential(credential)
        }

        override fun onVerificationFailed(e: FirebaseException) {
            // This callback is invoked in an invalid request for verification is made,
            // for instance if the the phone number format is not valid.

            if (e is FirebaseAuthInvalidCredentialsException) {
                Log.d("tag", "ON verification failed ${e.toString()}")
                // Invalid request
            } else if (e is FirebaseTooManyRequestsException) {
                Log.d("tag", "ON verification failed ${e.toString()}")
                // The SMS quota for the project has been exceeded
            }
            // Show a message and update the UI
        }

        override fun onCodeSent(
            verificationId: String,
            token: PhoneAuthProvider.ForceResendingToken
        ) {

            otp = verificationId
            resendToken = token

//                val intent=Intent(this@OTPActivity,OTPActivity::class.java)
//                intent.putExtra("OTP",verificationId)
//                intent.putExtra("resendToken",token)
//                intent.putExtra("mNumber",mNumber )
//                startActivity(intent)

        }
    }

    private fun verifyOtp() {
        val credential: PhoneAuthCredential = PhoneAuthProvider.getCredential(
            otp, typeOtp
        )
        signInWithPhoneAuthCredential(credential)
    }

    private fun toMain() {
        startActivity(Intent(this, MainActivity::class.java))
    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        mAuth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    SharedPref.putBoolean(SharedConst.IS_USER_LOGGED_IN,true)
                    Toast.makeText(this, "authSuccess", Toast.LENGTH_SHORT).show()
                    toMain()
                } else {
                    // Sign in failed, display a message and update the UI
                    Log.d("tag", "signInWithPhoneAuthCredential ${task.exception.toString()}")
                    if (task.exception is FirebaseAuthInvalidCredentialsException) {
                        // The verification code entered was invalid
                    }
                    // Update UI
                }
            }
    }

    private fun setTimer(context: Context) {

        val totalTimeMillis = 60000L  // 10 seconds
        val intervalMillis = 1000L    // 1 second

        val timer = object : CountDownTimer(totalTimeMillis, intervalMillis) {

            override fun onTick(millisUntilFinished: Long) {

                val secondsRemaining = millisUntilFinished / 1000
                binding.otpTimer.text = "00:$secondsRemaining"
            }

            override fun onFinish() {

                resent.visibility = View.VISIBLE

//                binding.resentOtpButton.setOnClickListener {
//                    //setTimer(context)
//                 //   resent.visibility = View.INVISIBLE
//                }
            }
        }

        timer.start()
    }


}