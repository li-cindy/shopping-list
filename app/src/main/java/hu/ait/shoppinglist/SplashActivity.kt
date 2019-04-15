package hu.ait.shoppinglist

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.animation.AnimationUtils
import kotlinx.android.synthetic.main.activity_splash.*
import android.support.v4.content.ContextCompat.startActivity
import android.support.v4.os.HandlerCompat.postDelayed



class SplashActivity : AppCompatActivity() {

    private val SPLASH_DISPLAY_LENGTH : Long = 3000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        var iconAnim = AnimationUtils.loadAnimation(this@SplashActivity, R.anim.icon_anim)
        ivShoppingIcon.startAnimation(iconAnim)

        Handler().postDelayed({
            var mainIntent = Intent()
            mainIntent.setClass(this@SplashActivity,
                ScrollingActivity::class.java)

            startActivity(mainIntent)
            this@SplashActivity.finish()
        }, SPLASH_DISPLAY_LENGTH)

    }
}
