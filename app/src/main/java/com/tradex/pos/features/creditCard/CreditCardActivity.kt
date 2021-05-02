package com.tradex.pos.features.creditCard

import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.nfc.NfcAdapter
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toolbar
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.example.githubproject.utils.Status
import com.google.android.material.snackbar.Snackbar
import com.tradex.pos.R
import com.tradex.pos.data.model.Cart
import com.tradex.pos.db.CachedUser
import com.tradex.pos.features.scanning.ScanningViewModel
import com.tradex.pos.features.success.SuccessfulActivity
import com.tradex.pos.utils.Utils
import com.pro100svitlo.creditCardNfcReader.CardNfcAsyncTask
import com.pro100svitlo.creditCardNfcReader.utils.CardNfcUtils
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_credit_card.*

@AndroidEntryPoint
class CreditCardActivity : AppCompatActivity(), CardNfcAsyncTask.CardNfcInterface {

    companion object{
        const val INTENT_DATA: String = "intent_data"
    }
    private val scanningViewModel: ScanningViewModel by viewModels()
    val cachedUser: CachedUser = CachedUser(this)
    var cart: Cart? = null
    private fun getTag() = "credit"
    var tagId: String? = null
    private var mCardNfcAsyncTask: CardNfcAsyncTask? = null
    private val mToolbar: Toolbar? = null
    private var mCardReadyContent: LinearLayout? = null
    private var mPutCardContent: TextView? = null
    private var mCardNumberText: TextView? = null
    private var mExpireDateText: TextView? = null
    private var mCardLogoIcon: ImageView? = null
    private var mNfcAdapter: NfcAdapter? = null
    private var mTurnNfcDialog: AlertDialog? = null
    private var mProgressDialog: ProgressDialog? = null
    private var mDoNotMoveCardMessage: String? = null
    private var mUnknownEmvCardMessage: String? = null
    private var mCardWithLockedNfcMessage: String? = null
    private var mIsScanNow = false
    private var mIntentFromCreate = false
    private var mCardNfcUtils: CardNfcUtils? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        cachedUser?.getUser()?.let { Utils.setLocale(this, it?.lang) }
        setContentView(R.layout.activity_credit_card)

        getData()

        mNfcAdapter = NfcAdapter.getDefaultAdapter(this)
        if (mNfcAdapter == null) {
            val noNfc = findViewById<View>(R.id.candidatesArea) as TextView
            noNfc.visibility = View.VISIBLE
        } else {
            mCardNfcUtils = CardNfcUtils(this)
            mPutCardContent = findViewById<View>(R.id.content_putCard) as TextView
            mCardReadyContent =
                findViewById<View>(R.id.content_cardReady) as LinearLayout
            mCardNumberText = findViewById<View>(R.id.text1) as TextView
            mExpireDateText = findViewById<View>(R.id.text2) as TextView
            mCardLogoIcon =
                findViewById<View>(R.id.icon) as ImageView
            createProgressDialog()
            initNfcMessages()
            mIntentFromCreate = true
            onNewIntent(intent)
        }

        initViews()
        setupObserver()
    }

    private fun initViews(){
        cart?.let {
            price_v_tv.text = it.total.toString()
        }
        send_btn_iv.setOnClickListener(View.OnClickListener { v ->
            Utils.getDeviceIMEI(this)?.let { terminal ->
                cachedUser?.getUser()?.api_token?.let {
                    cachedUser?.getUser()?.lang?.let { it1 ->
                        scanningViewModel.makeOrder(
                            it1, it, terminal, cart?.session_id!!, "0599855959",
                            "3", "", ""
                        )
                    }
                }
            }
        })
    }

    private fun setupObserver(){
        scanningViewModel.result.observe(this, Observer { it ->
            when(it.status){
                Status.SUCCESS -> {
                    loading_ll_2.visibility = View.GONE
                    goToSuccess()
                }
                    Status.LOADING -> {loading_ll_2.visibility = View.VISIBLE}
                    Status.LOADING -> {loading_ll_2.visibility = View.GONE}
                }
            })
    }

    fun goToSuccess(){
        cart?.session_id?.let { startActivity(Intent(this@CreditCardActivity, SuccessfulActivity::class.java)
            .putExtra(SuccessfulActivity.INTENT_SESSION, it)) }
    }

    private fun getData(){
        Log.d("CreditCardActivity", "---- getData")
        if (intent.hasExtra(INTENT_DATA)) {
            Log.d("CreditCardActivity", "---- hasExtra")
            cart = intent.getParcelableExtra(INTENT_DATA) as Cart
            Log.d("CreditCardActivity", "---- cart: ${cart.toString()}")
        }
    }

    override fun onResume() {
        super.onResume()
        mIntentFromCreate = false
        if (mNfcAdapter != null && !mNfcAdapter!!.isEnabled()){
            showTurnOnNfcDialog();
            mPutCardContent?.setVisibility(View.GONE);
        } else if (mNfcAdapter != null){
            if (!mIsScanNow){
                mPutCardContent?.setVisibility(View.VISIBLE);
                mCardReadyContent?.setVisibility(View.GONE);
            }
            mCardNfcUtils?.enableDispatch()
        }
    }

    override fun onPause() {
        super.onPause()
        if (mNfcAdapter != null) {
            mCardNfcUtils?.disableDispatch();
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        if (mNfcAdapter != null && mNfcAdapter!!.isEnabled) {
            mCardNfcAsyncTask = CardNfcAsyncTask.Builder(this, intent, mIntentFromCreate)
                .build()
        }
    }

    override fun startNfcReadCard() {
        mIsScanNow = true;
        mProgressDialog?.show()
    }

    override fun cardIsReadyToRead() {
        mPutCardContent!!.visibility = View.GONE
        mCardReadyContent!!.visibility = View.VISIBLE
        var card = mCardNfcAsyncTask!!.cardNumber
        card = getPrettyCardNumber(card)
        val expiredDate = mCardNfcAsyncTask!!.cardExpireDate
        val cardType = mCardNfcAsyncTask!!.cardType
        candidatesArea.visibility = View.GONE
        cachedUser.getUser()?.let { user ->
            if (user.lang == "ar"){
                mCardNumberText!!.text = card.substring(card.length - 4).plus("  ****  ****  ****  ")
            }else mCardNumberText!!.text = "****  ****  ****  ".plus(card.substring(card.length - 4))
        }

        mExpireDateText!!.text = expiredDate
        parseCardType(cardType)
    }

    override fun finishNfcReadCard() {
        mProgressDialog?.dismiss();
        mCardNfcAsyncTask = null;
        mIsScanNow = false;
    }

    override fun cardWithLockedNfc() {
        mCardWithLockedNfcMessage?.let { showSnackBar(it) };
    }

    override fun doNotMoveCardSoFast() {
        mDoNotMoveCardMessage?.let { showSnackBar(it) };
    }

    override fun unknownEmvCard() {
        mUnknownEmvCardMessage?.let { showSnackBar(it) };
    }

    private fun createProgressDialog() {
        val title = getString(R.string.inquire)
        val mess = getString(R.string.ad_progressBar_mess)
        mProgressDialog = ProgressDialog(this)
        mProgressDialog!!.setTitle(title)
        mProgressDialog!!.setMessage(mess)
        mProgressDialog!!.isIndeterminate = true
        mProgressDialog!!.setCancelable(false)
    }

    private fun showSnackBar(message: String) {
        Snackbar.make(mToolbar!!, message, Snackbar.LENGTH_SHORT).show()
    }

    private fun showTurnOnNfcDialog() {
        if (mTurnNfcDialog == null) {
            val title = getString(R.string.ad_nfcTurnOn_title)
            val mess = getString(R.string.ad_nfcTurnOn_message)
            val pos = getString(R.string.ad_nfcTurnOn_pos)
            val neg = getString(R.string.ad_nfcTurnOn_neg)
            mTurnNfcDialog = AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(mess)
                .setPositiveButton(
                    pos
                ) { dialogInterface, i -> // Send the user to the settings page and hope they turn it on
                    if (Build.VERSION.SDK_INT >= 16) {
                        startActivity(Intent(Settings.ACTION_NFC_SETTINGS))
                    } else {
                        startActivity(Intent(Settings.ACTION_WIRELESS_SETTINGS))
                    }
                }
                .setNegativeButton(
                    neg
                ) { dialogInterface, i -> onBackPressed() }.create()
        }
        mTurnNfcDialog!!.show()
    }

    private fun initNfcMessages() {
        mDoNotMoveCardMessage = getString(R.string.snack_doNotMoveCard)
        mCardWithLockedNfcMessage = getString(R.string.snack_lockedNfcCard)
        mUnknownEmvCardMessage = getString(R.string.snack_unknownEmv)
    }

    private fun parseCardType(cardType: String) {
        if (cardType == CardNfcAsyncTask.CARD_UNKNOWN) {
            Snackbar.make(
                mToolbar!!,
                getString(R.string.snack_unknown_bank_card),
                Snackbar.LENGTH_LONG
            )
                .setAction("GO") { goToRepo() }
        } else if (cardType == CardNfcAsyncTask.CARD_VISA) {
            mCardLogoIcon!!.setImageResource(R.drawable.ic_nfc)
        } else if (cardType == CardNfcAsyncTask.CARD_MASTER_CARD) {
            mCardLogoIcon!!.setImageResource(R.drawable.ic_nfc)
        }
    }

    private fun getPrettyCardNumber(card: String): String? {
        val div = " - "
        return (card.substring(0, 4) + div + card.substring(4, 8) + div + card.substring(8, 12)
                + div + card.substring(12, 16))
    }

    private fun goToRepo() {
        val i = Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.repoUrl)))
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        i.setPackage("com.android.chrome")
        try {
            startActivity(i)
        } catch (e: ActivityNotFoundException) {
            i.setPackage(null)
            startActivity(i)
        }
    }

}