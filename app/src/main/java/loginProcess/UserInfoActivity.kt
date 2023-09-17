package loginProcess

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.myshot.databinding.ActivityUserInfoBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage

class UserInfoActivity : AppCompatActivity() {

    private lateinit var binding:ActivityUserInfoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding= ActivityUserInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)

            val db= FirebaseFirestore.getInstance()
        FirebaseStorage.getInstance().reference.child("user image")

        val user = hashMapOf(
            "first" to "Alan",
            "middle" to "Mathison",
            "last" to "Turing",
            "born" to 1912,
        )

// Add a new document with a generated ID
        db.collection("users")
            .add(user)
            .addOnSuccessListener {
                //  Log.d(TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
            }
            .addOnFailureListener {
                // Log.w(TAG, "Error adding document", e)
            }
    }
}