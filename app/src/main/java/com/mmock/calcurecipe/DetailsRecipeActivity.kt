package com.mmock.calcurecipe

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.mmock.calcurecipe.model.Recipe

private const val TAG = "DetailsRecipeActivity"

class DetailsRecipeActivity : AppCompatActivity(){
    var recipe : Recipe? = null
    var descriptionText : TextView? = null
    var stepsText : TextView? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.recipe_details)

        //get the recipe to show
        val recipeLoc = intent.extras?.getInt("recipe_loc") ?: -1
        recipe = RecipeManager.getRecipeByPosition(recipeLoc)
        Log.d(TAG, "Showing recipe with ID = ${recipe?.recipeID}")

        //set UI elements
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        descriptionText = findViewById(R.id.rd_description)
        stepsText = findViewById(R.id.rd_steps)

        //set the UI elements
        setSupportActionBar(toolbar)
        supportActionBar?.title = recipe?.name
        descriptionText?.text = recipe?.description
        stepsText?.text = recipe?.details
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.details_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.edit_recipe -> {
                //switch to the EditRecipeActivity
                val intent = Intent(this, EditRecipeActivity::class.java)
                intent.putExtra("recipeID", recipe?.recipeID)
                startActivity(intent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}