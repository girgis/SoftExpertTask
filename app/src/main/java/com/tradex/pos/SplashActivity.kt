package com.tradex.pos

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import com.tradex.pos.db.CachedUser
import com.tradex.pos.features.login.LoginActivity
import com.tradex.pos.features.main.MainActivity
import com.tradex.pos.utils.Utils
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {

    companion object {
        const val SPLASH_CONSTANT: Long = 3000
    }

    lateinit var cachedUser: CachedUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        Log.d("SplashActivity", "--- device_id: ${Utils.getDeviceIMEI(this)} ")
        goToSplash()
    }

    private fun goToSplash(){
        Handler().postDelayed({
            if (!isFinishing) {
                cachedUser = CachedUser(this@SplashActivity)
                if (cachedUser.getUser() != null){
                    val mIntent = Intent(this@SplashActivity, MainActivity::class.java)
                    startActivity(mIntent)
                    finish()
                }else {
                    val mIntent = Intent(this@SplashActivity, LoginActivity::class.java)
                    startActivity(mIntent)
                    finish()
                }
            }
        }, SPLASH_CONSTANT)
    }

}