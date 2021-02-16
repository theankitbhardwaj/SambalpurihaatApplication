package com.inces.incesclient.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.text.Html
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import com.google.gson.Gson
import com.inces.incesclient.R
import com.inces.incesclient.helpers.DebugLogHelper
import com.inces.incesclient.helpers.SharedPrefManager
import com.inces.incesclient.helpers.UserHelper
import com.inces.incesclient.models.OtpResponse
import com.inces.incesclient.models.User
import com.inces.incesclient.repo.MainRepo
import com.inces.incesclient.util.Constants
import com.inces.incesclient.util.Resource
import com.inces.incesclient.viewmodels.MainViewModel
import com.inces.incesclient.viewmodels.factory.ViewModelFactory
import kotlinx.android.synthetic.main.activity_phone_login.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.concurrent.TimeUnit
import kotlin.math.min


class PhoneLogin : AppCompatActivity() {
    private var otpPin: String? = null
    private var uid: String? = null
    private var otpSent:Boolean = false
    var phoneRegex = "[0-9]{10}".toRegex()
    private lateinit var mainViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_phone_login)
        mainViewModel = ViewModelProviders.of(
            this,
            ViewModelFactory(MainRepo())
        ).get(MainViewModel::class.java)

        multiBtn.setOnClickListener {
            if(!otpSent){
                if (phoneNumber.text.toString().matches(phoneRegex)) {
                    //send OTP
                    sendOTP()
                    multiBtn.text = "Verify OTP"
                    phoneNumberLayout.visibility = View.GONE
                    otpLayout.visibility = View.VISIBLE
                    otpResendLayout.visibility = View.VISIBLE
                    val htmlText =
                        Html.fromHtml("Enter the OTP sent to <b>${encryptPhoneNumber(phoneNumber.text.toString())}</b>")
                    text2.text = htmlText.toString()

                    object : CountDownTimer(300000, 1000) {
                        override fun onTick(millis: Long) {
                            val minutes: Long = millis / 1000 / 60
                            val seconds: Long = millis / 1000 % 60
                            tv_otp_resend.text = "Waiting for OTP... " +minutes+":"+seconds
                        }

                        override fun onFinish() {
                            tv_otp_resend.text = "Try resending OTP..."
                            tv_btn_resend.visibility = View.VISIBLE
                            tv_btn_resend.setOnClickListener {
                                recreate()
                            }
                        }
                    }.start()
                }
            }else{
                signInWithPhoneNumber()
            }

        }
    }

    private fun encryptPhoneNumber(number: String): String {
        return "+91 - *******${number.removeRange(0, 7)}"
    }

    private fun sendOTP() {
        val phone_no: String = phoneNumber.getText().toString().trim({ it <= ' ' })
        if (phone_no.isEmpty()) {
            phoneNumber.error = "Required Phone Number"
            phoneNumber.requestFocus()
            return
        }
        if (phone_no.length != 10 ||
            !(Character.getNumericValue(phone_no[0]) < 10 && Character.getNumericValue(
                phone_no[0]
            ) > 5)
        ) {
            phoneNumber.error = "Required Valid Phone Number"
            phoneNumber.requestFocus()
            return
        }
        mainViewModel.getOtp("+91$phone_no").observe(this, { response ->
            response?.data?.enqueue(object : Callback<OtpResponse?> {
                override fun onResponse(
                    call: Call<OtpResponse?>,
                    response: Response<OtpResponse?>
                ) {
                    val otpResponse: OtpResponse? = response.body()
                    if (!otpResponse!!.isErr) {
                        otpPin = otpResponse.otp
                        uid = otpResponse.uid
                    } else {
                        multiBtn.text = "Send OTP"
                        phoneNumberLayout.visibility = View.VISIBLE
                        otpLayout.visibility = View.GONE
                        phoneNumber.error = "Sending Failed"
                        phoneNumber.requestFocus()
                    }
                    multiBtn.setOnClickListener {
                        signInWithPhoneNumber()
                    }
                }

                override fun onFailure(
                    call: Call<OtpResponse?>,
                    t: Throwable
                ) {

                }
            })
        })
    }

    private fun signInWithPhoneNumber() {
        val pin: String = inputPin.getText().toString().trim({ it <= ' ' })
        if (pin.isEmpty()) {
            inputPin.error = "Required Pin No."
            inputPin.requestFocus()
            return
        }
        if (pin != otpPin) {
            phoneNumber.setError("Required Valid Pin")
            phoneNumber.requestFocus()
            return
        } else {
            if (uid != null) {
                mainViewModel.getUser(uid!!).observe(this, { response ->
                    when (response) {
                        is Resource.Success -> {
                            response.data?.body()?.let {
                                if (!it.error) {
                                    SharedPrefManager.saveData(
                                        this,
                                        Constants.USER_DATA,
                                        Gson().toJson(it.user)
                                    )
                                    startActivity(Intent(this, Dashboard::class.java))
                                    finish()
                                } else {
                                    DebugLogHelper.Log("TAG", it.message)
                                    SharedPrefManager.saveData(
                                        this,
                                        Constants.USER_DATA,
                                        Gson().toJson(it.user)
                                    )
                                    it.user?.let { it1 -> goToNextActivity(it1) }
                                }
                            }
                        }
                    }
                })
            } else {
                val phone_no: String = phoneNumber.getText().toString().trim({ it <= ' ' })
                val user = User("", "", "mobile", phone_no, UserHelper.uniqueUserId())
                SharedPrefManager.setLoginWith(this, "mobile", phone_no)
                val intent = Intent(this@PhoneLogin, UserProfile::class.java)
                intent.putExtra(Constants.USER_DATA, user)
                startActivity(intent)
                finish()
            }
        }
        otpSent = false
        return
    }

    private fun goToNextActivity(user: User) {
        val i = Intent(this, Dashboard::class.java)
        i.putExtra(Constants.USER_DATA, user)
        startActivity(i)
        finish()
    }

}