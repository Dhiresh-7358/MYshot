package loginProcess

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.SpannedString
import android.text.style.ForegroundColorSpan
import androidx.core.content.ContextCompat
import com.example.myshot.R
import com.example.myshot.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)


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
            startActivity(Intent(this,OTPActivity::class.java))
        }


    }
}