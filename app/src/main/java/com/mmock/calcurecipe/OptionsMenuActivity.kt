package com.mmock.calcurecipe

import android.content.Intent
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity

open class OptionsMenuActivity : AppCompatActivity() {

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.home_button -> {
                //TODO switch to the MainActivity
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                true
            }
            R.id.saved_lists -> {
                //TODO switch to the SavedListsActivity
                true
            }
            R.id.add_recipe_button -> {
                //TODO switch to the AddRecipeActivity

                val intent = Intent(this, AddRecipeActivity::class.java)
                startActivity(intent)
                true
            }
            R.id.search -> {
                //TODO switch to the SearchActivity
                true
            }
            R.id.settings -> {
                //TODO switch to the SettingsActivity
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}