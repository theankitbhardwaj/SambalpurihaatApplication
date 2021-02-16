package com.inces.incesclient.activities

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.gson.Gson
import com.inces.incesclient.R
import com.inces.incesclient.helpers.DebugLogHelper
import com.inces.incesclient.helpers.SharedPrefManager
import com.inces.incesclient.helpers.UserHelper
import com.inces.incesclient.models.User
import com.inces.incesclient.repo.MainRepo
import com.inces.incesclient.util.Constants
import com.inces.incesclient.util.Resource
import com.inces.incesclient.viewmodels.MainViewModel
import com.inces.incesclient.viewmodels.factory.ViewModelFactory
import kotlinx.android.synthetic.main.activity_home.*
import org.json.JSONObject


class Login : AppCompatActivity() {

    private lateinit var mGoogleSignInClient: GoogleSignInClient
    private var GOOGLE_SIGN_IN = 101
    private val TAG = "LoginFragment"
    private lateinit var mainViewModel: MainViewModel
    private lateinit var progressDialog: ProgressDialog
    private lateinit var bundle: Bundle


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)


        progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Loading...")
        progressDialog.setCancelable(false)

        bundle = Bundle()
        bundle.putString("fields", "first_name,last_name,email,id")

        mainViewModel = ViewModelProviders.of(
            this,
            ViewModelFactory(MainRepo())
        ).get(MainViewModel::class.java)

        if (UserHelper.isNetworkConnected(this)) {
            googleLogin()
        } else
            Toast.makeText(this, Constants.NO_INTERNET, Toast.LENGTH_SHORT).show()

        otpLogin.setOnClickListener {
            startActivity(Intent(this@Login, PhoneLogin::class.java))
        }

    }

    private fun googleLogin() {
        val gso =
            GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build()
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)

        val account = GoogleSignIn.getLastSignedInAccount(this)
        account?.let {
            checkIfExistingUser(it)
        }

        gLogin.setOnClickListener {
            googleSignIn()
        }
    }

    private fun googleSignIn() {
        val signInIntent = mGoogleSignInClient.signInIntent
        startActivityForResult(signInIntent, GOOGLE_SIGN_IN)
    }

    private fun checkIfExistingUser(user: User) {
        mainViewModel.getUser(user.uid).observe(this, Observer { response ->
            when (response) {
                is Resource.Loading -> {
                    progressDialog.show()
                }
                is Resource.Success -> {

                    if (progressDialog.isShowing)
                        progressDialog.dismiss()

                    response.data?.body()?.let {
                        if (!it.error) {
                            SharedPrefManager.saveData(
                                this,
                                Constants.USER_DATA,
                                Gson().toJson(it.user)
                            )
                            startActivity(Intent(this, Dashboard::class.java))
                        } else {
                            DebugLogHelper.Log("TAG", it.message)
                            SharedPrefManager.saveData(
                                this,
                                Constants.USER_DATA,
                                Gson().toJson(user)
                            )
                            DebugLogHelper.Log(TAG, "Username: $user")
                            goToNextActivity(user)
                        }
                    }
                }
                is Resource.Failure -> {

                    if (progressDialog.isShowing)
                        progressDialog.dismiss()

                    SharedPrefManager.saveData(
                        this,
                        Constants.USER_DATA,
                        Gson().toJson(user)
                    )
                    DebugLogHelper.Log(TAG, "Username: $user")
//                    goToNextActivity(user)
                    Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    private fun checkIfExistingUser(account: GoogleSignInAccount) {
        mainViewModel.getUser(account.id!!).observe(this, Observer { response ->
            when (response) {
                is Resource.Loading -> {
                    progressDialog.show()
                }
                is Resource.Success -> {

                    if (progressDialog.isShowing)
                        progressDialog.dismiss()

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
                            newUser(account)
                        }
                    }

                }
                is Resource.Failure -> {
                    if (progressDialog.isShowing)
                        progressDialog.dismiss()
//                    newUser(account)
                    Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == GOOGLE_SIGN_IN) {
            val task =
                GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        }
    }

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account = completedTask.getResult(ApiException::class.java)
            account?.let {
                checkIfExistingUser(it)
            }
        } catch (e: ApiException) {
            DebugLogHelper.Log(TAG, "signInResult:failed code= ${e.statusCode}")
            Toast.makeText(this, "Error Logging In. Please Try again", Toast.LENGTH_LONG)
                .show()
        }
    }

    private fun newUser(account: GoogleSignInAccount) {
        val user = User(
            uid = account.id!!,
            email = account.email!!,
            login_with = "google",
            fullname = account.displayName!!,
            phone_no = ""
        )
        SharedPrefManager.saveData(
            this,
            Constants.USER_DATA,
            Gson().toJson(user)
        )
        DebugLogHelper.Log(TAG, "Username: $user")
        goToNextActivity(user)
    }

    private fun goToNextActivity(user: User) {
        val i = Intent(this, UserProfile::class.java)
        i.putExtra(Constants.USER_DATA, user)
        startActivity(i)
        finish()
    }


}