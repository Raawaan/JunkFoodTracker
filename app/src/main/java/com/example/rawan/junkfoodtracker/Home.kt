package com.example.rawan.junkfoodtracker

import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.NavigationView
import android.support.v4.app.Fragment
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.example.rawan.junkfoodtracker.R.id.nav_profile
import com.facebook.login.LoginManager
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.home.*
import kotlinx.android.synthetic.main.nav_header_main.*
import android.widget.TextView
import com.google.firebase.auth.UserProfileChangeRequest


/**
 * Created by rawan on 08/09/18.
 */
class Home: AppCompatActivity(){
    var fbAuth= FirebaseAuth.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.home)
        setSupportActionBar(toolbar)
        supportFragmentManager.beginTransaction().add(R.id.fragmentsLayout, HomeFrag.newInstance(), "a").commit()
    if (fbAuth.getCurrentUser()!=null){
        val navigationView = findViewById<View>(R.id.nav_view) as NavigationView
        val headerView = navigationView.getHeaderView(0)
        val navUsername = headerView.findViewById(R.id.nav_name) as TextView
        val navEmail = headerView.findViewById(R.id.nav_email) as TextView
        navUsername.text=fbAuth.currentUser?.displayName
        navEmail.text=fbAuth.currentUser?.email
    }
        fab.setOnClickListener {
        val i = Intent(this@Home,Scanner::class.java)
        startActivity(i)
        }
        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar ,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()
        nav_view.setNavigationItemSelectedListener{
            when (it.itemId) {
                R.id.nav_logout->{
                    fbAuth.signOut()
                    LoginManager.getInstance().logOut()
                    val i= Intent(this,MainActivity::class.java)
                startActivity(i)
                finish()
                }
                R.id.nav_home->{
                    replaceFragments(HomeFrag.newInstance())
                }  R.id.nav_profile->{
                    replaceFragments(ProfileFrag.newInstance())
                } R.id.nav_about->{
                    replaceFragments(AboutFrag.newInstance())
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

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        val id = item.itemId
        return if (id == R.id.action_settings) {
            true
        } else super.onOptionsItemSelected(item)

    }
private fun replaceFragments(fragment: Fragment){
    val fragmentTransaction= supportFragmentManager.beginTransaction()
    fragmentTransaction.replace(R.id.
    fragmentsLayout,fragment).commit()
}
}