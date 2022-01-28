package com.ss.android.ugc.bytex.example

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import com.ss.android.ugc.bytex.example.coverage.CoverageReportTask

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Handle coverage log info, send to the server
        CoverageReportTask.init()
    }

    fun textClick(view: View) {
        Log.i("haha1", "textClick: ")
        println("haha1 textClick")
        startActivity(Intent(this, MainActivity2::class.java))
    }
}
