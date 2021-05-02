package com.tradex.pos.features.forgotPassword

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.example.githubproject.utils.Status
import com.tradex.pos.R
import com.tradex.pos.db.CachedSysConstants
import com.tradex.pos.db.CachedUser
import com.tradex.pos.features.forgotPassword.viewmodel.ForgotPasswordViewModel
import com.tradex.pos.features.login.LoginActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_forgot_password.*

@AndroidEntryPoint
class ForgotPasswordActivity : AppCompatActivity() {

    private val forgotPasswordViewModel: ForgotPasswordViewModel by viewModels()
    private var telephone: String?= null
    private var vCode: String?= null
    private var verificationPhase: VerificationPhase = VerificationPhase.PHONE
    private val cachedUser: CachedUser = CachedUser(this)
    private val cachedSys: CachedSysConstants = CachedSysConstants(this)
    private var mPhone: String?  = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)

        setupUI()
        setupObserver()
    }

    private fun setupUI(){
        cachedSys.getSystemConstants()?.let { system ->
            ccp.setCountryForPhoneCode(system.country_code.toInt())
        }

        back_to_login_tv.setOnClickListener { v -> finish() }
        back_iv.setOnClickListener { v -> finish() }
        send_btn.setOnClickListener(View.OnClickListener { v ->
            when(verificationPhase){
                VerificationPhase.PHONE -> {
                    if (isValidData()) {
                        forgotPasswordViewModel.getActivationCode("en", mPhone!!)
                    }
                }
                VerificationPhase.VERIFY -> {
                    telephone?.let {
                        vCode = pinview.value
                        forgotPasswordViewModel.verifyCode("en", it, pinview.value)
                    }
                }
                VerificationPhase.ACTIVATE -> {
                    if (isValidResetPasswordData()) {
                        vCode?.let { code ->
                            forgotPasswordViewModel.resetPassword("en", code, password_et.text.toString(), password_confirm_et.text.toString())
                        }
                    }
                }
            }
        })
        pinview.setPinViewEventListener { pinview, fromUser ->
            telephone?.let { forgotPasswordViewModel.verifyCode("en", it, pinview.value) }
        }
    }

    private fun setupObserver(){
        forgotPasswordViewModel.result.observe(this, Observer {
            when(it.status){
                Status.SUCCESS -> {
                    loading_ll_fp.visibility = View.GONE
                    it?.data?.success?.let { success ->
                        if(success){
                            telephone = mPhone
                            verificationPhase = VerificationPhase.VERIFY
                            ccp_ll.visibility = View.INVISIBLE
                            editText_carrierNumber_ll.visibility = View.INVISIBLE
                            pin_layout.visibility = View.VISIBLE
                            retrieve_hint_tv.text = getText(R.string.activiation_sent_to)
                            send_btn.text = getText(R.string.confirm)
                        }else {Toast.makeText(this@ForgotPasswordActivity, it.data.message, Toast.LENGTH_SHORT).show()}
                    }
                }
                Status.LOADING -> {loading_ll_fp.visibility = View.VISIBLE}
                Status.ERROR -> {loading_ll_fp.visibility = View.GONE
                    Toast.makeText(this@ForgotPasswordActivity, getString(R.string.connection_error), Toast.LENGTH_SHORT).show()
                }
            }
        })

        forgotPasswordViewModel.verify_result.observe(this, Observer {
            when(it.status){
                Status.SUCCESS -> {
                    vCode = pinview.value
                    loading_ll_fp.visibility = View.GONE
                    it.data?.success?.let { success ->
                        if (success){
                            verificationPhase = VerificationPhase.ACTIVATE
                            confirm_password_cl.visibility = View.VISIBLE
                            ccp_ll.visibility = View.INVISIBLE
                            editText_carrierNumber_ll.visibility = View.INVISIBLE
                            pin_layout.visibility = View.INVISIBLE
                            retrieve_hint_tv.text = getText(R.string.create_new_pass_hint)
                            send_btn.text = getText(R.string.create_new_pass)
                            Toast.makeText(this@ForgotPasswordActivity, getString(R.string.success), Toast.LENGTH_SHORT).show()
                        }else{

                        }
                    }
                }
                Status.LOADING -> {loading_ll_fp.visibility = View.VISIBLE}
                Status.ERROR -> {loading_ll_fp.visibility = View.GONE
                    Toast.makeText(this@ForgotPasswordActivity, getString(R.string.connection_error), Toast.LENGTH_SHORT).show()
                }
            }
        })

        forgotPasswordViewModel.reset_result.observe(this, Observer {
            when(it.status){
                Status.SUCCESS -> {
                    loading_ll_fp.visibility = View.GONE
                    it.data?.success?.let { success ->
                        if (success){
                            startActivity(Intent(this@ForgotPasswordActivity, LoginActivity::class.java)
                                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK))
                        }else{

                        }
                    }
                }
                Status.LOADING -> {loading_ll_fp.visibility = View.VISIBLE}
                Status.ERROR -> {loading_ll_fp.visibility = View.GONE
                    Toast.makeText(this@ForgotPasswordActivity, getString(R.string.connection_error), Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    private fun isValidData(): Boolean{
        if (editText_carrierNumber.text.isNullOrEmpty()){
            Toast.makeText(this, getString(R.string.phone_is_required), Toast.LENGTH_SHORT).show()
            return false
        }
        mPhone = editText_carrierNumber.text.toString()
        if (mPhone?.startsWith("0", false)!!){
            mPhone = mPhone?.substring(1)
        }
        mPhone = "+".plus(ccp.selectedCountryCode.plus(mPhone))
        return true
    }

    private fun isValidResetPasswordData(): Boolean{
        if (password_et.text.isNullOrEmpty() || password_confirm_et.text.isNullOrEmpty()){
            Toast.makeText(this, getString(R.string.password_is_required), Toast.LENGTH_SHORT).show()
            return false
        }
        if (password_et.text.toString() != password_confirm_et.text.toString()){
            Toast.makeText(this, getString(R.string.password_mismatch), Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }

}