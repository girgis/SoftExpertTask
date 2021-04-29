package com.nosa.posapp.features.success

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.example.githubproject.utils.Status
import com.nosa.posapp.R
import com.nosa.posapp.db.CachedSysConstants
import com.nosa.posapp.db.CachedUser
import com.nosa.posapp.features.main.MainActivity
import com.nosa.posapp.features.success.viewmodel.SuccessViewModel
import com.nosa.posapp.utils.Utils
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_forgot_password.*
import kotlinx.android.synthetic.main.activity_successful.*

@AndroidEntryPoint
class SuccessfulActivity : AppCompatActivity() {

    companion object{
        const val INTENT_SESSION: String = "intent_session"
    }

    private val successViewModel: SuccessViewModel by viewModels()
    val cachedUser:CachedUser = CachedUser(this)
    private var session_id: String? = null
    private val cachedSys: CachedSysConstants = CachedSysConstants(this)
    private var mPhone: String?  = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        cachedUser?.getUser()?.let { Utils.setLocale(this, it?.lang) }
        setContentView(R.layout.activity_successful)

        getData()
        setupUI()
        setupObserver()
    }

    private fun getData(){
        session_id = intent.getStringExtra(INTENT_SESSION)
    }

    private fun setupUI(){
        cachedSys.getSystemConstants()?.let { system ->
            ccp_success.setCountryForPhoneCode(system.country_code.toInt())
        }

        home_btn.setOnClickListener(View.OnClickListener { v ->
            startActivity(Intent(this@SuccessfulActivity, MainActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
            finish()
        })

        send_phone_iv.setOnClickListener(View.OnClickListener { v ->
            if (isValidData())
                getInvoice()
        })
        print_bill_btn.setOnClickListener(View.OnClickListener { v ->
            if (isValidData())
                getInvoice()
        })
    }

    private fun setupObserver(){
        successViewModel.result.observe(this, Observer {
            when(it.status){
                Status.SUCCESS -> {loading_ll.visibility = View.GONE
                    Toast.makeText(this@SuccessfulActivity, getString(R.string.success), Toast.LENGTH_SHORT).show()
                    it.data?.data?.let { message ->
                        Utils.openLinkInBrowser(message, this@SuccessfulActivity)
                    }
                }
                Status.LOADING -> {loading_ll.visibility = View.VISIBLE}
                Status.ERROR -> {loading_ll.visibility = View.GONE}
            }
        })
    }

    private fun getInvoice(){
        Utils.getDeviceIMEI(this)?.let { terminal ->
            cachedUser.getUser()?.lang?.let { lang ->
                cachedUser.getUser()?.api_token?.let { token ->
                    session_id?.let { s ->
                        mPhone?.let {
                            successViewModel.getInvoiceView(lang, token, terminal, s, it)
                        }
                    }
                }
            }
        }
    }

    private fun isValidData(): Boolean{
        if (phone_et.text.isNullOrEmpty()){
            Toast.makeText(this@SuccessfulActivity, getString(R.string.phone_required), Toast.LENGTH_SHORT).show()
            return false
        }
        mPhone = phone_et.text.toString()
        if (mPhone?.startsWith("0", false)!!){
            mPhone = mPhone?.substring(1)
        }
        mPhone = "+".plus(ccp_success.selectedCountryCode.plus(mPhone))

        return true
    }

    override fun onBackPressed() {
        startActivity(Intent(this@SuccessfulActivity, MainActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
        finish()
    }

}