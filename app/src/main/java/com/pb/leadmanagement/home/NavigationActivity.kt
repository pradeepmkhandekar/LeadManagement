package com.pb.leadmanagement.home

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.app.Fragment
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import com.pb.leadmanagement.R
import com.pb.leadmanagement.core.facade.UserFacade
import com.pb.leadmanagement.dashboard.DashboardFragment
import com.pb.leadmanagement.login.LoginActivity
import com.pb.leadmanagement.profile.ProfileFragment
import com.pb.leadmanagement.reports.ReportsFragment
import kotlinx.android.synthetic.main.activity_navigation.*
import kotlinx.android.synthetic.main.app_bar_navigation.*

class NavigationActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_navigation)
        setSupportActionBar(toolbar)

        /* fab.setOnClickListener { view ->
             Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                     .setAction("Action", null).show()
         }*/

        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)

        bindHeader(nav_view.getHeaderView(0))

        //by default first item clicked.
        onNavigationItemSelected(nav_view.menu.getItem(0))
    }

    fun bindHeader(header: View) {

        var txtName = header.findViewById<TextView>(R.id.txtName)
        var txtEmail = header.findViewById<TextView>(R.id.txtEmail)
        var txtChainCode = header.findViewById<TextView>(R.id.txtChainCode)


        txtName.setText(UserFacade(this).getUser()?.Name)
        txtEmail.setText(UserFacade(this).getUser()?.EmailID)
        txtChainCode.setText("Chain :" + UserFacade(this).getUser()?.ChainCode)
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }


    private fun switchFragment(switchFragment: Fragment) {
        supportFragmentManager.beginTransaction().replace(R.id.frame, switchFragment).commit()
    }

    private fun alertLogoutDialog() {
        // Initialize a new instance of
        val builder = AlertDialog.Builder(this@NavigationActivity)

        // Set the alert dialog title
        builder.setTitle("Logout")

        // Display a message on alert dialog
        builder.setMessage("Do you want to Logout?")

        // Set a positive button and its click listener on alert dialog
        builder.setPositiveButton("LOGOUT") { dialog, which ->

            if (UserFacade(this@NavigationActivity).clearUser()) {
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
            }
        }


        // Display a negative button on alert dialog
        builder.setNegativeButton("CANCEL") { dialog, which ->
            dialog.dismiss()
        }

        // Finally, make the alert dialog using builder
        val dialog: AlertDialog = builder.create()

        // Display the alert dialog on app interface
        dialog.show()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_dashboard -> {
                // Handle the camera action
                val homeFragment = DashboardFragment()
                /*val bundle = Bundle()
                bundle.putString("TITLE", "CAMERA")
                homeFragment.arguments = bundle*/
                //homeFragment.arguments?.putString("TITLE", "CAMERA")
                switchFragment(homeFragment)
                supportActionBar?.title = "Dashboard"
            }
            R.id.nav_profile -> {
                val updateProfile = ProfileFragment()
                switchFragment(updateProfile)
                supportActionBar?.title = "Profile"
            }

            R.id.nav_report -> {
                val reportFragment = ReportsFragment()
                switchFragment(reportFragment)
                supportActionBar?.title = "Reports"
            }

            R.id.nav_logout -> {
                alertLogoutDialog()
            }

        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }
}
