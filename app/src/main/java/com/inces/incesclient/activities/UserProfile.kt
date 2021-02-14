package com.inces.incesclient.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.inces.incesclient.R
import com.inces.incesclient.helpers.SharedPrefManager
import com.inces.incesclient.helpers.UserHelper
import com.inces.incesclient.models.User
import com.inces.incesclient.repo.MainRepo
import com.inces.incesclient.util.Constants
import com.inces.incesclient.util.Resource
import com.inces.incesclient.viewmodels.MainViewModel
import com.inces.incesclient.viewmodels.factory.ViewModelFactory
import kotlinx.android.synthetic.main.activity_user_profile.*
import java.lang.Exception

class UserProfile : AppCompatActivity() {

    private var user: User? = null
    private lateinit var mainViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_profile)
        mainViewModel = ViewModelProviders.of(
            this,
            ViewModelFactory(MainRepo())
        ).get(MainViewModel::class.java)
        val bundle = intent.extras
        user = bundle?.get(Constants.USER_DATA) as User?
        val phoneNo = SharedPrefManager.getPhoneNo(this)
        val type = object : TypeToken<User>() {}.type
        try {
            user =
                Gson().fromJson<User>(
                    SharedPrefManager.getData(this, Constants.USER_DATA),
                    type
                )
        }catch (e:Exception){
            Log.e("Data",e.localizedMessage)
        }
        userPh.setText(phoneNo)
        if(user != null){
            userName.setText(user!!.fullname)
            userEmail.setText(user!!.email)

            if(userName.text.isEmpty()){
                    userName.isEnabled = true
            }
            if(userEmail.text.isEmpty()){
                userEmail.isEnabled = true
            }
        }



        save.setOnClickListener {
            val email = userEmail.text.toString().trim()
            val name = userName.text.toString().trim()
            userPh.text?.let {
                val phoneNumber = userPh.text.toString()
                if (phoneNumber.length == 10) {
                    user = User(email,name,"mobile",phoneNumber,UserHelper.uniqueUserId())
                    callApiToCreateUser(user!!)
                } else {
                    userPh.requestFocus()
                    userPh.error = "Invalid Phone Number"
                }
            }
        }
    }


    private fun callApiToCreateUser(user: User) {
        this.user?.let {
            mainViewModel.createUser(it)
                .observe(this, Observer { response ->
                    when (response) {
                        is Resource.Loading -> {

                        }
                        is Resource.Success -> {

                            //201 - User Created Successfully
                            //422 - User Already exists
                            Log.e("TAG", "callApiToCreateUser: ${response.data}")
                            response.data?.let {
                                when {
                                    it.code() == 422 -> {
                                        Toast.makeText(
                                            this,
                                            "User already Exists",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        SharedPrefManager.saveData(
                                            this,
                                            Constants.USER_DATA,
                                            Gson().toJson(user)
                                        )
                                        startActivity(Intent(this, Dashboard::class.java))
                                        finish()
                                    }
                                    it.code() == 201 -> {
                                        it.body()?.let { it1 ->
                                            if (!it1.error) {
                                                SharedPrefManager.saveData(
                                                    this,
                                                    Constants.USER_DATA,
                                                    Gson().toJson(user)
                                                )
                                                startActivity(Intent(this, Dashboard::class.java))
                                                finish()
                                            }
                                        }
                                    }
                                    it.code() == 500 -> Toast.makeText(
                                        this,
                                        "Error - 500 Internal Server Error",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    else -> Toast.makeText(this, it.code(), Toast.LENGTH_SHORT).show()
                                }
                            }

                        }
                        is Resource.Failure -> {
                            Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show()
                        }
                    }
                })
        }
    }

}