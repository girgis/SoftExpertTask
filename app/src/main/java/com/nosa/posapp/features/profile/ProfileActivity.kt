package com.nosa.posapp.features.profile

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.nosa.posapp.R
import com.nosa.posapp.data.model.User
import com.nosa.posapp.db.CachedUser
import com.nosa.posapp.features.login.LoginActivity
import com.nosa.posapp.features.main.MainActivity
import com.nosa.posapp.utils.Utils
import kotlinx.android.synthetic.main.activity_profile.*

class ProfileActivity : AppCompatActivity(), View.OnClickListener {

    val cachedUser: CachedUser = CachedUser(this@ProfileActivity)
    var lang = "en"
    var user: User? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        cachedUser?.getUser()?.let { Utils.setLocale(this, it?.lang) }
        super.onCreate(savedInstanceState)
        cachedUser.getUser()?.let { user -> Utils.setLocale(this, user.lang)
            lang = user.lang
            this.user = user
        }
        setContentView(R.layout.activity_profile)

        setupUI()
        fillUI()
        setLocalization()
    }

    private fun setupUI(){
        language_cl.setOnClickListener(View.OnClickListener { v: View? ->
            if (lang == "ar"){
                user?.lang = "en"
                user?.let { cachedUser.saveUser(user!!) }
            }else{
                user?.lang = "ar"
                user?.let { cachedUser.saveUser(user!!) }
            }
            startActivity(Intent(this@ProfileActivity, MainActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
            finish()
        })
        logout_cl.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.logout_cl -> {
               cachedUser.clearUser()
                startActivity(Intent(this@ProfileActivity, LoginActivity::class.java)
                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK))
                finish()
            }
        }
    }

    private fun fillUI(){
        if (lang == "ar"){
            userName_txt_value_tv.text = user?.branch?.name_ar
        }else
            userName_txt_value_tv.text = user?.branch?.name
        email_txt_value_tv.text =  user?.branch?.email
        id_txt_value_tv.text = user?.branch?.brand_id
        phone_txt_value_tv.text = user?.branch?.phone
        address_txt_value_tv.text = user?.branch?.address
    }

    private fun setLocalization(){
        if (lang == "ar"){
            email_arrow_iv.rotationY = 0F
            phone_arrow_iv.rotationY = 0F
            language_arrow_iv.rotationY = 0F
            change_password_arrow_iv.rotationY = 0F
        }
    }

}