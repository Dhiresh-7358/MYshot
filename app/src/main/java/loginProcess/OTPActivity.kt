package loginProcess

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.text.Editable
import android.text.Spannable
import android.text.SpannableString
import android.text.TextWatcher
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.myshot.activity.MainActivity
import com.example.myshot.R
import com.example.myshot.databinding.ActivityOtpactivityBinding
import com.google.android.material.card.MaterialCardView
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.*
import java.util.concurrent.TimeUnit

class OTPActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOtpactivityBinding
    private lateinit var resent: TextView
    private lateinit var otp: String
    private lateinit var resendToken: PhoneAuthProvider.ForceResendingToken
    private lateinit var mNumber: String
    private lateinit var mAuth: FirebaseAuth
    private lateinit var typeOtp: String
    private lateinit var continueOtp: MaterialCardView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOtpactivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mAuth = FirebaseAuth.getInstance()


        otp = intent.getStringExtra("OTP").toString()
        resendToken = intent.getParcelableExtra("resendToken")!!
        mNumber = intent.getStringExtra("mNumber").toString()
        Log.d("fire", "no. $mNumber")
        binding.sendSms.text = "Send via SMS to  $mNumber"

        setHint()

        continueOtp = binding.otpContinue
        resent = binding.resentOtpButton

        continueOtp.isEnabled = false
        continueOtp.alpha = 0.5F

        setTextWatcher()

        resent.visibility = View.INVISIBLE

//        setTimer(this)

        resent.setOnClickListener {
            resendOtp()
//            setTimer(this)
//            resent.visibility = View.INVISIBLE
        }
        binding.anotherNumber.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }

    }

    override fun onBackPressed() {
        val intent = Intent(this, GetStartActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }

    private fun setHint() {

        val hint = "   OTP*"
        val spannable = SpannableString(hint)
        val pinkColor = ContextCompat.getColor(this, R.color.pink)
        val colorSpan = ForegroundColorSpan(pinkColor)

        spannable.setSpan(colorSpan, 6, 7, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        binding.otp.hint = spannable
    }

    private fun setTextWatcher() {
        binding.otp.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                typeOtp = binding.otp.text.toString()

                if (typeOtp.length == 6) {
                    continueOtp.alpha = 1F
                    continueOtp.isEnabled = true

                } else {
                    continueOtp.alpha = 0.5F
                    continueOtp.isEnabled = false
                }

                continueOtp.setOnClickListener {

                    continueOtp.isEnabled = false
                    continueOtp.alpha = 0.5F

                    binding.otpProgressBar.visibility = View.VISIBLE
                    binding.continueText.visibility = View.INVISIBLE

                    if (typeOtp.length != 6) {
                        Toast.makeText(this@OTPActivity, "Invalid number!!", Toast.LENGTH_SHORT)
                            .show()
                    }
                    verifyOtp()
                    setTimer(this@OTPActivity)
                    resent.visibility = View.INVISIBLE
                }

            }

        })
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
        }
    }

    private fun verifyOtp() {
        val credential: PhoneAuthCredential = PhoneAuthProvider.getCredential(
            otp, typeOtp
        )
        signInWithPhoneAuthCredential(credential)
    }

    private fun toMain() {
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        mAuth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = task.result?.additionalUserInfo
                    if (user != null) {
                        if (user.isNewUser) {
                            SharedPref.putBoolean(SharedConst.IS_USER_LOGGED_IN, true)
                            startActivity(Intent(this, UserInfoActivity::class.java))
                            finish()
                        } else {
                            SharedPref.putBoolean(SharedConst.IS_USER_LOGGED_IN, true)

                            continueOtp.isEnabled = true
                            continueOtp.alpha = 1F

                            binding.otpProgressBar.visibility = View.INVISIBLE
                            binding.continueText.visibility = View.VISIBLE

                            Toast.makeText(this, "authSuccess", Toast.LENGTH_SHORT).show()
                            toMain()
                        }
                    }

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