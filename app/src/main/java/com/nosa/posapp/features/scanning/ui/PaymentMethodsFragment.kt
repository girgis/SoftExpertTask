package com.nosa.posapp.features.scanning.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.graphics.drawable.DrawableCompat
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.example.githubproject.utils.Status
import com.nosa.posapp.R
import com.nosa.posapp.features.creditCard.CreditCardActivity
import com.nosa.posapp.features.scanning.ScanningActivity
import com.nosa.posapp.features.scanning.ScanningViewModel
import com.nosa.posapp.utils.Utils
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_payment_methods.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [PaymentMethodsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
@AndroidEntryPoint
class PaymentMethodsFragment : Fragment(), View.OnClickListener {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var onlinePaymentBTN: ImageButton? = null
    private var creditPaymentBTN: ImageButton? = null
    private var cashPaymentBTN: ImageButton? = null
    private val scanningViewModel: ScanningViewModel by activityViewModels()
    private var scanningActivity: ScanningActivity? = null
    private var paymentType: Int = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_payment_methods, container, false)
        setupUI(view)
        setupObserver()
        return view
    }

    override fun onAttach(context: Context) {
        if (context is ScanningActivity) scanningActivity = context as ScanningActivity
        super.onAttach(context)
    }

    override fun onDetach() {
        scanningActivity = null
        super.onDetach()
    }

    private fun setupUI(view: View){
        onlinePaymentBTN = view.findViewById<ImageButton>(R.id.online_payment_btn)
        creditPaymentBTN = view.findViewById<ImageButton>(R.id.credit_payment_btn)
        cashPaymentBTN = view.findViewById<ImageButton>(R.id.cash_payment_btn)

        onlinePaymentBTN?.setOnClickListener(this)
        creditPaymentBTN?.setOnClickListener(this)
        cashPaymentBTN?.setOnClickListener(this)

        view.findViewById<Button>(R.id.next_btn).setOnClickListener(this)
    }

    private fun setupObserver(){
        activity?.let {
            scanningViewModel.result.observe(it, Observer { t ->
                when(t.status){
                    Status.SUCCESS -> {
                        loading_ll.visibility = View.GONE
                        scanningActivity?.let { scanningActivity -> scanningActivity.goToSuccess() }
                    }
                    Status.LOADING -> {loading_ll.visibility = View.VISIBLE}
                    Status.LOADING -> {loading_ll.visibility = View.GONE}
                }
            })
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment PaymentMethodsFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            PaymentMethodsFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }

        val TAG = "payment_methods"
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.online_payment_btn -> {
                onlinePaymentBTN?.setBackgroundResource(R.drawable.blue_rectangle)
                creditPaymentBTN?.setBackgroundResource(R.drawable.bg)
                cashPaymentBTN?.setBackgroundResource(R.drawable.bg)
                onlinePaymentBTN?.setImageResource(R.drawable.icon_credit_card)
                creditPaymentBTN?.setImageResource(R.drawable.credit_card)
                cashPaymentBTN?.setImageResource(R.drawable.cash)
                paymentType = 1
            }
            R.id.credit_payment_btn -> {
                online_payment_btn.setBackgroundResource(R.drawable.bg)
                credit_payment_btn.setBackgroundResource(R.drawable.blue_rectangle)
                cash_payment_btn.setBackgroundResource(R.drawable.bg)
                onlinePaymentBTN?.setImageResource(R.drawable.icon_online_gray)
                creditPaymentBTN?.setImageResource(R.drawable.credit_card_white)
                cashPaymentBTN?.setImageResource(R.drawable.cash)
                paymentType = 2
            }
            R.id.cash_payment_btn -> {
                online_payment_btn.setBackgroundResource(R.drawable.bg)
                credit_payment_btn.setBackgroundResource(R.drawable.bg)
                cash_payment_btn.setBackgroundResource(R.drawable.blue_rectangle)
                onlinePaymentBTN?.setImageResource(R.drawable.icon_online_gray)
                creditPaymentBTN?.setImageResource(R.drawable.credit_card)
                cashPaymentBTN?.setImageResource(R.drawable.cash_white)
                paymentType = 3
            }
            R.id.next_btn -> {
                if (scanningActivity != null && scanningActivity?.mSession_id != null) {
                    Utils.getDeviceIMEI(scanningActivity!!)?.let { terminal ->
                        scanningActivity?.cachedUser?.getUser()?.let {
                            if (paymentType == 3) {
                                scanningViewModel.makeOrder(
                                    it.lang,
                                    it.api_token,
                                    terminal,
                                    scanningActivity?.mSession_id!!,
                                    "0599855959",
                                    paymentType.toString(),
                                    "",
                                    ""
                                )
                            } else if (paymentType == 2) {
                                scanningActivity?.let { scanningActivity ->
                                    scanningActivity.cart?.let { cart ->
                                        startActivity(
                                            Intent(
                                                scanningActivity,
                                                CreditCardActivity::class.java
                                            ).putExtra(CreditCardActivity.INTENT_DATA, cart)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}