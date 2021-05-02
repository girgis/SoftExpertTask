package com.tradex.pos.features.cart.success

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.example.githubproject.utils.Status
import com.tradex.pos.R
import com.tradex.pos.db.CachedUser
import com.tradex.pos.features.main.MainActivity
import com.tradex.pos.features.success.SuccessfulActivity
import com.tradex.pos.features.success.viewmodel.SuccessViewModel
import com.tradex.pos.utils.Utils
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_success_cart.*

@AndroidEntryPoint
class SuccessCartActivity : AppCompatActivity() {

    val cachedUser: CachedUser = CachedUser(this)
    private var session_id: String? = null
    private val successViewModel: SuccessViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        cachedUser?.getUser()?.let { Utils.setLocale(this, it?.lang) }
        setContentView(R.layout.activity_success_cart)

        getData()
        setupUI()
        setupObserver()
    }

    private fun getData(){
        session_id = intent.getStringExtra(SuccessfulActivity.INTENT_SESSION)
    }

    private fun setupUI(){
        home_btn.setOnClickListener(View.OnClickListener { v ->
            startActivity(Intent(this@SuccessCartActivity, MainActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
            finish()
        })
        print_bill_btn.setOnClickListener(View.OnClickListener {
            getInvoice()
        })
    }

    private fun getInvoice(){
        Utils.getDeviceIMEI(this)?.let { terminal ->
            cachedUser.getUser()?.lang?.let { lang ->
                cachedUser.getUser()?.api_token?.let { token ->
                    session_id?.let { s ->
                        successViewModel.getInvoiceView(lang, token, terminal, s, "")
                    }
                }
            }
        }
    }

    private fun setupObserver(){
        successViewModel.result.observe(this, Observer {
            when(it.status){
                Status.SUCCESS -> {loading_ll_fp.visibility = View.GONE
                    Toast.makeText(this@SuccessCartActivity, getString(R.string.success), Toast.LENGTH_SHORT).show()
                    it.data?.data?.let { message ->
                        Utils.openLinkInBrowser(message, this@SuccessCartActivity)
                    }
                }
                Status.LOADING -> {loading_ll_fp.visibility = View.VISIBLE}
                Status.ERROR -> {loading_ll_fp.visibility = View.GONE}
            }
        })
    }

    override fun onBackPressed() {
        startActivity(Intent(this@SuccessCartActivity, MainActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
        finish()
    }

}