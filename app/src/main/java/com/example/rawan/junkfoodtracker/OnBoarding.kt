package com.example.rawan.junkfoodtracker

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.PersistableBundle
import com.hololo.tutorial.library.PermissionStep
import com.hololo.tutorial.library.Step
import com.hololo.tutorial.library.TutorialActivity

/**
 * Created by rawan on 27/09/18.
// */
 class OnBoarding: TutorialActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val step = Step.Builder()
        addFragment( step
                .setTitle("Welcome To Junk Food Tracker!")
                .setContent("JFT helps you track your daily food by scanning barcode, " +
                        "counting the total calories and other important minerals")
                .setBackgroundColor(Color.parseColor("#b53f3f")) // int background color
                .setDrawable(R.drawable.food2) // int top drawable
                .build())
        val step2 = Step.Builder()

        addFragment( step2
                .setTitle("So lets start by adding our first food by clicking here")
                .setBackgroundColor(Color.parseColor("#b53f3f")) // int background color
                .setDrawable(R.drawable.food2)
//                 .setView(R.id.actionBarScanner)// int top drawable
                .build())
    }

    override fun finishTutorial() {
        super.finishTutorial()
        val i= Intent(this@OnBoarding,HomeActivity::class.java)
        i.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(i)
    }
}