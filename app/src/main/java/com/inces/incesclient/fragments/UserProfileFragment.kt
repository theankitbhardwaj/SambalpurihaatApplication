package com.inces.incesclient.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.facebook.login.LoginManager
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.inces.incesclient.R
import com.inces.incesclient.activities.Login
import com.inces.incesclient.activities.MyOrders
import com.inces.incesclient.helpers.DebugLogHelper
import com.inces.incesclient.helpers.SharedPrefManager
import com.inces.incesclient.helpers.UserHelper
import com.inces.incesclient.models.User
import com.inces.incesclient.util.Constants
import com.inces.incesclient.viewmodels.CartViewModel
import com.inces.incesclient.viewmodels.ProductViewModel
import kotlinx.android.synthetic.main.user_profile_fragment.*

class UserProfileFragment : Fragment(R.layout.user_profile_fragment) {

    private lateinit var mGoogleSignInClient: GoogleSignInClient
    private lateinit var user: User
    private lateinit var loginWith: String
    private var imageUrl: Uri? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        if (!UserHelper.isNetworkConnected(requireContext()))
            Toast.makeText(context, Constants.NO_INTERNET, Toast.LENGTH_SHORT).show()

        val gso =
            GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build()
        mGoogleSignInClient = context?.let { GoogleSignIn.getClient(it, gso) }!!
        imageUrl = GoogleSignIn.getLastSignedInAccount(context)?.photoUrl

        val type = object : TypeToken<User>() {}.type
        user = Gson().fromJson<User>(SharedPrefManager.getData(context, Constants.USER_DATA), type)
        loginWith = user.login_with

        userName.text = user.fullname
        userEmail.text = user.email

        logoutLayout.setOnClickListener {
            if (loginWith == "google")
                googleSignOut()
            else if (loginWith == "facebook")
                fbSignOut()
        }


        myOrderLayout.setOnClickListener {
            startActivity(Intent(context, MyOrders::class.java))
        }

        context?.let { context ->
            imageUrl?.let {
                Glide.with(context)
                    .load(it)
                    .into(userImage)
            }
        }

    }

    private fun fbSignOut() {
        LoginManager.getInstance().logOut()
        clearDb()
        val i = Intent(context, Login::class.java)
        i.flags =
            Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(i)
    }

    private fun clearDb() {
        SharedPrefManager.deleteData(context)
        val cartViewModel: CartViewModel by viewModels()
        val productViewModel: ProductViewModel by viewModels()
        cartViewModel.deleteAll()
        productViewModel.deleteAll()
    }

    private fun googleSignOut() {
        mGoogleSignInClient.signOut().addOnCompleteListener {
            SharedPrefManager.deleteData(context)
            DebugLogHelper.Log("TAG", "signOut: ${it.result}")
            val i = Intent(context, Login::class.java)
            i.flags =
                Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(i)
        }
    }
}