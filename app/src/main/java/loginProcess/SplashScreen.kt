package loginProcess

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.myshot.activity.MainActivity

class SplashScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val isUserLoggedIN = SharedPref.getBoolean(SharedConst.IS_USER_LOGGED_IN)

        if (isUserLoggedIN) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        } else {
            startActivity(Intent(this, GetStartActivity::class.java))
            finish()
        }


    }
}