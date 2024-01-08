package com.mmock.calcurecipe

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.bumptech.glide.Glide
import com.mmock.calcurecipe.model.Recipe

private const val TAG = "DetailsRecipeActivity"

class DetailsRecipeActivity : AppCompatActivity(){
    var recipe : Recipe? = null
    private lateinit var recipeImage : ImageView
    private var descriptionText : TextView? = null
    private var stepsText : TextView? = null

    private lateinit var bookmark : ImageView
    private lateinit var heart : ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.recipe_details)

        // Get the recipe to show
        val recipeLoc = intent.extras?.getInt("recipe_loc") ?: -1
        recipe = RecipeManager.getRecipeByPosition(recipeLoc)
        Log.d(TAG, "Showing recipe with ID = ${recipe?.recipeID}")

        // Get UI elements
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        recipeImage = findViewById(R.id.rd_recipeImage)
        descriptionText = findViewById(R.id.rd_description)
        stepsText = findViewById(R.id.rd_steps)
        bookmark = findViewById(R.id.rd_saveButton)
        heart = findViewById(R.id.rd_likeButton)

        // Set the UI elements
        setSupportActionBar(toolbar)
        supportActionBar?.title = recipe?.name
        descriptionText?.text = recipe?.description
        stepsText?.text = recipe?.details

        //set the image
        displayImage(recipe?.imagePath)

        // Set the liked icon
        val liked = FolderManager.isRecipeInFolder(recipe!!.recipeID, 0)
        if (liked) {
            heart.setImageResource(R.drawable.ic_heart_liked_true)
        } else {
            heart.setImageResource(R.drawable.ic_heart)
        }

        // Set button clicks
        heart.setOnClickListener {
            toggleLiked()
        }
        bookmark.setOnClickListener {
            switchToSaveRecipeActivity()
        }
    }

    fun displayImage(path: String?){
        if (path == "") {
            //use default image
            recipeImage.setImageResource(R.drawable.default_recipe_image_pancakes)
            return
        }
        Glide.with(this)
            .load(path)
            .into(recipeImage)
    }

    fun toggleLiked(){
        val liked = FolderManager.isRecipeInFolder(recipe!!.recipeID, 0)
        if (liked) {
            //remove from the liked list and send a toast
            Log.d(TAG, "Removed from the liked list a recipe with id = ${recipe!!.recipeID}")
            heart.setImageResource(R.drawable.ic_heart)
            FolderManager.removeMapping(0, recipe!!.recipeID)
            Toast.makeText(this, "Removed", Toast.LENGTH_SHORT).show()
        } else {
            //add to the liked list
            Log.d(TAG, "Added to the liked list a recipe with id = ${recipe!!.recipeID}")
            heart.setImageResource(R.drawable.ic_heart_liked_true)
            FolderManager.addMapping(0, recipe!!.recipeID)
        }
        FolderManager.saveMappingsToFile(applicationContext)
    }

    fun switchToSaveRecipeActivity(){
        val intent = Intent(this, SaveRecipeActivity::class.java)
        intent.putExtra("recipeID", recipe!!.recipeID)
        startActivity(intent)
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

    override fun onPause() {
        super.onPause()
        FolderManager.saveMappingsToFile(applicationContext)
        RecipeManager.saveRecipesToFile(applicationContext)
    }
}