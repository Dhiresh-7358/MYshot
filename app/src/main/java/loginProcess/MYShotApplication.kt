package loginProcess

import android.app.Application

class MYShotApplication:Application() {

    override fun onCreate() {
        super.onCreate()

        SharedPref.init(this)
    }
}