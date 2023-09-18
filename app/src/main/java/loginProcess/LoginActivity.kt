package loginProcess

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.*
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityCompat.recreate
import androidx.core.content.ContextCompat
import com.example.myshot.activity.MainActivity
import com.example.myshot.R
import com.example.myshot.databinding.ActivityLoginBinding
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.*
import java.util.concurrent.TimeUnit

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var mAuth: FirebaseAuth
    private lateinit var mNumber: String
    private lateinit var mobileNumber: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mobileNumber = binding.mobileNumber

        mAuth = FirebaseAuth.getInstance()
        binding.loginContinue.isEnabled = false
        binding.loginContinue.alpha = 0.5F

        setHintColor()

        setTextWatcher()

    }

    private fun setTextWatcher() {
        Log.d("fire", "verification  1")
        mobileNumber.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                mNumber = mobileNumber.text.toString().trim()


                if (mNumber.length == 10) {
                    binding.loginContinue.alpha = 1F
                    binding.loginContinue.isEnabled = true
                } else {
                    binding.loginContinue.alpha = 0.5F
                    binding.loginContinue.isEnabled = false

                    binding.continueText.visibility = View.VISIBLE
                    binding.loginProgressBar.visibility = View.INVISIBLE
                }

                binding.loginContinue.setOnClickListener {
                    if (mNumber.length != 10) {
                        Toast.makeText(this@LoginActivity, "Invalid number!!", Toast.LENGTH_SHORT)
                            .show()
                    }

                    mNumber = "+91$mNumber"

                    binding.loginContinue.isEnabled = false
                    binding.loginContinue.alpha = 0.5F

                    binding.continueText.visibility = View.INVISIBLE
                    binding.loginProgressBar.visibility = View.VISIBLE

                    sendOTP()
                }

            }

        })
    }

    private fun setHintColor() {
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
    }

    private fun sendOTP() {
        Log.d("fire", "verification  send")
        mAuth.signOut()
        Log.d("fire", "signOut  ")

        val options =
            PhoneAuthOptions.newBuilder(mAuth).setPhoneNumber(mNumber) // Phone number to verify
                .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                .setActivity(this) // Activity (for callback binding)
                .setCallbacks(callbacks) // OnVerificationStateChangedCallbacks
                .build()
        PhoneAuthProvider.verifyPhoneNumber(options)


    }

    private val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {


        override fun onVerificationCompleted(credential: PhoneAuthCredential) {
            Log.d("fire", "verification  complete")
            signInWithPhoneAuthCredential(credential)
        }

        override fun onVerificationFailed(e: FirebaseException) {
            // This callback is invoked in an invalid request for verification is made,
            // for instance if the the phone number format is not valid.

            if (e is FirebaseAuthInvalidCredentialsException) {
                Log.d("tag", "ON verification failed ${e.toString()}")

                Toast.makeText(this@LoginActivity, "ON verification failed $e", Toast.LENGTH_SHORT)
                    .show()

            } else if (e is FirebaseTooManyRequestsException) {
                Log.d("tag", "ON verification failed ${e.toString()}")
                Toast.makeText(this@LoginActivity, "ON verification failed $e", Toast.LENGTH_SHORT)
                    .show()
            }
            // Show a message and update the UI
        }

        override fun onCodeSent(
            verificationId: String,
            token: PhoneAuthProvider.ForceResendingToken,
        ) {
            Log.d("fire", "otp: $verificationId")
            Log.d("fire", "token: $token")

            val intent = Intent(this@LoginActivity, OTPActivity::class.java)
            intent.putExtra("OTP", verificationId)
            intent.putExtra("resendToken", token)
            intent.putExtra("mNumber", mNumber)
            Log.d("fire", "send to otp activity")

            binding.loginContinue.isEnabled = true
            binding.loginContinue.alpha = 1F

            binding.loginProgressBar.visibility = View.INVISIBLE
            binding.continueText.visibility = View.VISIBLE

            startActivity(intent)
            finish()

        }
    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        Log.d("fire", "verification  signWith")
        mAuth.signInWithCredential(credential).addOnCompleteListener(this) { task ->
            if (task.isSuccessful) {
                Toast.makeText(this, "authSuccess", Toast.LENGTH_SHORT).show()
                //  toMain()
            } else {
                Log.d("tag", "signInWithPhoneAuthCredential ${task.exception.toString()}")
                if (task.exception is FirebaseAuthInvalidCredentialsException) {
                    // The verification code entered was invalid
                }
                // Update UI
            }
        }
    }

}
