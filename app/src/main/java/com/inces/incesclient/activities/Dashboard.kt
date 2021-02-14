package com.inces.incesclient.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.inces.incesclient.R
import com.inces.incesclient.fragments.DashboardFragment
import com.inces.incesclient.fragments.UserProfileFragment
import kotlinx.android.synthetic.main.activity_dashboard.*

class Dashboard : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        setSupportActionBar(toolbar)

        /*val toggle = ActionBarDrawerToggle(
             this, drawerLayout, toolbar, 0, 0
         )
         drawerLayout.addDrawerListener(toggle)

         supportActionBar?.title = ""
         toggle.syncState()
         toggle.isDrawerIndicatorEnabled = false
         toggle.setHomeAsUpIndicator(R.drawable.ic_menu_icon)

         toggle.setToolbarNavigationClickListener {
             if (drawerLayout.isDrawerVisible(GravityCompat.START)) {
                 drawerLayout.closeDrawer(GravityCompat.START);
             } else {
                 drawerLayout.openDrawer(GravityCompat.START);
             }
         }*/


        search.setOnClickListener {
            startActivity(Intent(this, Search::class.java))
        }

        bottomNav.setupWithNavController(fragmentNavHost.findNavController())

    }
}