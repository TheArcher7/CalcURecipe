package com.mmock.calcurecipe

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class HelpActivity : AppCompatActivity() {
    private lateinit var btnDismiss : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_help)

        btnDismiss = findViewById(R.id.ah_dismissButton)

        btnDismiss.setOnClickListener {
            dismissButton()
        }
    }

    fun dismissButton(){
        finish()
    }

}