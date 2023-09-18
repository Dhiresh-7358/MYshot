package loginProcess

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.Spannable
import android.text.SpannableString
import android.text.TextWatcher
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.myshot.R
import com.example.myshot.activity.MainActivity
import com.example.myshot.databinding.ActivityUserInfoBinding
import com.google.android.material.card.MaterialCardView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage

class UserInfoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUserInfoBinding
    private lateinit var name: String
    private lateinit var email: String
    private lateinit var continueInfo: MaterialCardView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityUserInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setHint()

        continueInfo = binding.infoContinue

        continueInfo.isEnabled = false
        continueInfo.alpha = 0.5F

        continueInfo = binding.infoContinue

        binding.name.addTextChangedListener(textWatcher)
        binding.email.addTextChangedListener(textWatcher)
    }


    private fun storeUserData() {
        val db = FirebaseFirestore.getInstance()
        Log.d("fire", "fun called")
        //   FirebaseStorage.getInstance().reference.child("user image")

        val user = hashMapOf(
            "name" to name,
            "email" to email,
        )
        val mAuth = FirebaseAuth.getInstance()
        val userId = mAuth.currentUser?.uid
// Add a new document with a generated ID
        if (userId != null) {
            db.collection("users")
                .document(userId)
                .set(user)
                .addOnSuccessListener {

                    continueInfo.isEnabled = true
                    continueInfo.alpha = 1F

                    binding.infoProgressBar.visibility = View.INVISIBLE
                    binding.continueText.visibility = View.VISIBLE

                    startActivity(Intent(this, MainActivity::class.java))
                    finish()

                }
                .addOnFailureListener {
                    Toast.makeText(this, "$it", Toast.LENGTH_SHORT).show()
                }
        }
    }

    private fun setHint() {
        val nameHint = "Name*"
        val spannable = SpannableString(nameHint)
        val pinkColor = ContextCompat.getColor(this, R.color.pink)
        val colorSpan = ForegroundColorSpan(pinkColor)

        spannable.setSpan(colorSpan, 4, 5, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        binding.name.hint = spannable

        val emailHint = "Email*"
        val spannable2 = SpannableString(emailHint)
        spannable2.setSpan(colorSpan, 5, 6, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

        binding.email.hint = spannable2
    }

    private val textWatcher = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            name = binding.name.text.toString()
            email = binding.email.text.toString()

            if (name.isNotEmpty() && email.isNotEmpty()) {
                continueInfo.alpha = 1F
                continueInfo.isEnabled = true


            }

            continueInfo.setOnClickListener {

                if (!email.contains("@") && !email.contains("@")) {
                    Toast.makeText(this@UserInfoActivity, "Invalid email!!", Toast.LENGTH_SHORT)
                        .show()
                } else {
                    continueInfo.isEnabled = false
                    continueInfo.alpha = 0.5F

                    binding.infoProgressBar.visibility = View.VISIBLE
                    binding.continueText.visibility = View.INVISIBLE

                    storeUserData()
                }
            }

        }
    }
}
