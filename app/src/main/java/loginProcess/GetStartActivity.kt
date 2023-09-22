package loginProcess

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import androidx.core.content.ContextCompat
import com.example.myshot.R
import com.example.myshot.databinding.ActivityGetStartBinding

class GetStartActivity : AppCompatActivity() {
    private lateinit var binding: ActivityGetStartBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityGetStartBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val text = "Find the best \nPhotographer \nof your \nchoice!"
        val spannable = SpannableString(text)
        val pinkColor = ContextCompat.getColor(this, R.color.pink)
        val colorSpan = ForegroundColorSpan(pinkColor) // Change Color.RED to your desired color

// Apply the color span to the specific text range (e.g., "colored text")
        spannable.setSpan(colorSpan, 32, 45, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

        binding.title.text = spannable
        binding.saveButton.setOnClickListener{
            startActivity(Intent(this,LoginActivity::class.java))
        }
    }
}