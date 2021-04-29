package com.nosa.posapp.features.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.nosa.posapp.R
import dagger.hilt.android.AndroidEntryPoint
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.example.githubproject.utils.Status
import com.nosa.posapp.db.CachedUser
import com.nosa.posapp.features.forgotPassword.ForgotPasswordActivity
import com.nosa.posapp.features.main.MainActivity
import com.nosa.posapp.utils.Utils
import kotlinx.android.synthetic.main.activity_login.*

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    private val loginViewModel : LoginViewModel by viewModels()
    var cachedUser: CachedUser = CachedUser(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        setupUI()
        setupObserver()
    }

    private fun setupUI() {
        login_btn.setOnClickListener { v ->
            Utils.getDeviceIMEI(this@LoginActivity)?.let {
                Log.d("LoginActivity", "--- device IMEI ${it}")
                loginViewModel.login(user_name_et.text.toString(), password_et.text.toString(), it)
            } ?: kotlin.run {
                Toast.makeText(this@LoginActivity, "CAN NOT GET DEVICE IMEI", Toast.LENGTH_LONG).show()
            }
        }
        forgot_password_tv.setOnClickListener(View.OnClickListener { v ->
            startActivity(Intent(this@LoginActivity, ForgotPasswordActivity::class.java))
        })
    }

    private fun setupObserver() {
        loginViewModel.user.observe(this, Observer {
            when(it.status){
                Status.SUCCESS -> {
                    if (it.data != null ){
                        if (it.data.success && it.data.data != null) {
                            cachedUser.saveUser(it.data.data)
                            startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                            finish()
                        }else if (!it.data.success){
                            it.data.message?.let { Toast.makeText(this, it.toString(), Toast.LENGTH_SHORT).show() }
                        }
                    }else {
                        it.message?.let { message -> Toast.makeText(this, message, Toast.LENGTH_SHORT).show() }
                    }
                }
                Status.ERROR -> {
                    Log.d("LoginActivity", "--- error")
                    Toast.makeText(this@LoginActivity, getString(R.string.connection_error), Toast.LENGTH_SHORT).show()
                }
                Status.LOADING -> {
                    Log.d("LoginActivity", "--- LOADING")
                }
            }
        })
    }

}