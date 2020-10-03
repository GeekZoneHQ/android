package zone.geek.membership.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import zone.geek.membership.R

class SplashActivity : AppCompatActivity() {
    companion object {
        private const val SPLASH_TIME_OUT: Long = 3000
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        Handler().postDelayed({
            val intent = Intent(this, GeekZoneActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NO_HISTORY
            // This method will be executed once the timer is over
            // Start GeekZoneActivity
            startActivity(intent)

            // close this activity
            finish()
        }, SPLASH_TIME_OUT)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
    }
}