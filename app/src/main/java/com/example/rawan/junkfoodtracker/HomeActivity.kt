package com.example.rawan.junkfoodtracker

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.app.Fragment
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.example.rawan.junkfoodtracker.R.id.nav_profile
import com.facebook.login.LoginManager
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.home.*
import android.widget.TextView


/**
 * Created by rawan on 08/09/18.
 */
class HomeActivity: AppCompatActivity(),View.OnClickListener{
    var fbAuth= FirebaseAuth.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.home)
        setSupportActionBar(toolbar)
        supportFragmentManager.beginTransaction().add(R.id.fragmentPlaceholder, AbstractFrag.newInstance(), "a").commit()
        actionBarScanner.setOnClickListener(this)
        handleNavDrawer()
        navDrawerHeader()
    }
    private fun navDrawerHeader() {
        if (fbAuth.getCurrentUser() != null) {
            val navigationView = findViewById<View>(R.id.nav_view) as NavigationView
            val headerView = navigationView.getHeaderView(0)
            val navUsername = headerView.findViewById(R.id.nav_name) as TextView
            val navEmail = headerView.findViewById(R.id.nav_email) as TextView
            navUsername.text = fbAuth.currentUser?.displayName
            navEmail.text = fbAuth.currentUser?.email
        }
    }
    private fun handleNavDrawer() {
        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()
        nav_view.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.nav_logout -> {
                    fbAuth.signOut()
                    LoginManager.getInstance().logOut()
                    val i = Intent(this, MainActivity::class.java)
                    startActivity(i)
                    finish()
                }
                R.id.nav_home -> {
                    replaceFragments(AbstractFrag.newInstance())
                }
                nav_profile -> {
                    replaceFragments(ProfileFrag.newInstance())
                }
                R.id.nav_about -> {
                    replaceFragments(AboutFrag.newInstance())
                }
               R.id.nav_setting -> {
                 replaceFragments(SettingFrag.newInstance())
               }

            }
            // Close the drawer
            drawer_layout.closeDrawer(GravityCompat.START)
            true
        }
    }
    override fun onBackPressed() {
        val drawer = findViewById<View>(R.id.drawer_layout) as DrawerLayout
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }
    private fun replaceFragments(fragment: Fragment){
        val fragmentTransaction= supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.
        fragmentPlaceholder,fragment).commit()
    }
    override fun onClick(view: View?) {
        when(view!!.id){
            R.id.actionBarScanner->{
                val i = Intent(this@HomeActivity,ScannerActivity::class.java)
                startActivity(i)
            }
        }
    }
}