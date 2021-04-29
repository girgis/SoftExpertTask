package com.nosa.posapp.features.cart.success

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.nosa.posapp.R
import com.nosa.posapp.db.CachedUser
import com.nosa.posapp.features.main.MainActivity
import com.nosa.posapp.utils.Utils
import kotlinx.android.synthetic.main.activity_successful.*

class SuccessCartActivity : AppCompatActivity() {
    val cachedUser: CachedUser = CachedUser(this)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        cachedUser?.getUser()?.let { Utils.setLocale(this, it?.lang) }
        setContentView(R.layout.activity_success_cart)

        setupUI()

    }

    private fun setupUI(){
        home_btn.setOnClickListener(View.OnClickListener { v ->
            startActivity(Intent(this@SuccessCartActivity, MainActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
            finish()
        })
    }

    override fun onBackPressed() {
        startActivity(Intent(this@SuccessCartActivity, MainActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
        finish()
    }

}