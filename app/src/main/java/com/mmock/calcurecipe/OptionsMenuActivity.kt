package com.mmock.calcurecipe

import android.content.Intent
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity

open class OptionsMenuActivity : AppCompatActivity() {
    private val TAG = "OptionsMenu"

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.home_button -> {
                Log.d(TAG, "Home button pressed.")
                // Switch to the MainActivity only if not already in MainActivity
                if (this !is MainActivity) {
                    Log.d(TAG, "Switching to MainActivity.")
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                }
                true
            }
            R.id.saved_lists -> {
                Log.d(TAG, "Bookmark button pressed.")
                // Switch to the SavedListsActivity only if not already in SavedListsActivity
                if (this !is SavedListsActivity) {
                    Log.d(TAG, "Switching to SavedListsActivity.")
                    val intent = Intent(this, SavedListsActivity::class.java)
                    startActivity(intent)
                }
                true
            }
            R.id.add_recipe_button -> {
                Log.d(TAG, "Create button pressed.")
                // Switch to the AddRecipeActivity only if not already in AddRecipeActivity
                if (this !is AddRecipeActivity) {
                    Log.d(TAG, "Switching to AddRecipeActivity.")
                    val intent = Intent(this, AddRecipeActivity::class.java)
                    startActivity(intent)
                }
                true
            }
            R.id.search -> {
                Log.d(TAG, "Search button pressed.")
                // Switch to the SearchActivity only if not already in SearchActivity
                if (this !is SearchActivity) {
                    Log.d(TAG, "Switching to SearchActivity.")
                    val intent = Intent(this, SearchActivity::class.java)
                    startActivity(intent)
                }
                true
            }
            R.id.settings -> {
                Log.d(TAG, "Settings button pressed.")
                // Switch to the SettingsActivity only if not already in SettingsActivity
                if (this !is SettingsActivity) {
                    Log.d(TAG, "Switching to SettingsActivity.")
                    val intent = Intent(this, SettingsActivity::class.java)
                    startActivity(intent)
                }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}