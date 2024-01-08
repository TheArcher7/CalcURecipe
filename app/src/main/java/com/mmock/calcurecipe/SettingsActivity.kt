package com.mmock.calcurecipe

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.widget.SwitchCompat
import androidx.appcompat.widget.Toolbar

private val PREFS_LOCATION_NAME = "CalcURecipePrefs"
private val TAG = "SettingsActivity"

class SettingsActivity : OptionsMenuActivity() {
    private lateinit var toolbar : Toolbar
    private lateinit var switch : SwitchCompat
    private var showDividers: Boolean = true
    private lateinit var btnHelp : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        //initialize prefs
        val prefs = getSharedPreferences(PREFS_LOCATION_NAME, Context.MODE_PRIVATE)
        showDividers = prefs.getBoolean("dividers", true)

        //get UI elements
        toolbar = findViewById(R.id.as_toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.title = "Settings"
        switch = findViewById(R.id.as_dividerSwitch)
        btnHelp = findViewById(R.id.as_helpButton)

        btnHelp.setOnClickListener {
            helpButton()
        }

        //set the switch on or off as appropriate
        switch.isChecked = showDividers
        switch.setOnCheckedChangeListener {
                _, isChecked ->
            showDividers = isChecked
        }
    }

    override fun onPause() {
        super.onPause()

        //save the settings here
        val prefs = getSharedPreferences(PREFS_LOCATION_NAME, Context.MODE_PRIVATE)
        val editor = prefs.edit()

        editor.putBoolean("dividers", showDividers)

        editor.apply()
    }

    fun helpButton() {
        var intent = Intent(this, HelpActivity::class.java)
        startActivity(intent)
    }

}