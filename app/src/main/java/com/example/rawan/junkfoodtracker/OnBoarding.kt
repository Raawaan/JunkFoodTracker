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
                .setTitle(getString(R.string.title_welcome))
                .setContent(getString(R.string.desc_welcome))
                .setBackgroundColor(Color.parseColor(getString(R.string.red))) // int background color
                .setDrawable(R.drawable.food2) // int top drawable
                .build())
        val step1 = Step.Builder()
        addFragment( step1
                .setTitle(getString(R.string.title_start))
                .setBackgroundColor(Color.parseColor(getString(R.string.red))) // int background color
                .setDrawable(R.drawable.food2)
                .build())
    }
    override fun finishTutorial() {
        super.finishTutorial()
        val i= Intent(this@OnBoarding,HomeActivity::class.java)
        i.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(i)
    }
}