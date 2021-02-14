package com.inces.incesclient.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.inces.incesclient.R
import com.inces.incesclient.helpers.SharedPrefManager
import com.inces.incesclient.models.User
import com.inces.incesclient.util.Constants
import kotlinx.android.synthetic.main.activity_splash.*

class Splash : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)




        Handler().postDelayed({
            val type = object : TypeToken<User>() {}.type
            val user =
                Gson().fromJson<User>(
                    SharedPrefManager.getData(this, Constants.USER_DATA),
                    type
                )
            if (user != null) {
                if (user.fullname.isNotBlank()) {
                    if (user.phone_no.isEmpty()) {
                        startActivity(Intent(this@Splash, UserProfile::class.java))
                        finish()
                    } else {
                        startActivity(Intent(this@Splash, Dashboard::class.java))
                        finish()
                    }
                } else {
                    startActivity(Intent(this@Splash, Login::class.java))
                    finish()
                }
            } else {
                startActivity(Intent(this@Splash, Login::class.java))
                finish()
            }
        }, 2000)


    }
}