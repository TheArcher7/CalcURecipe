package com.mmock.calcurecipe

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.mmock.calcurecipe.model.Recipe

private const val TAG = "EditRecipeActivity"

class EditRecipeActivity : AppCompatActivity() {
    var recipe: Recipe? = null
    lateinit var chooseImageButton : Button
    lateinit var recipeImage : ImageView
    lateinit var recipeNameTitle : EditText
    lateinit var recipeDescription : EditText
    lateinit var recipeSteps : EditText
    lateinit var cancelButton : Button
    lateinit var deleteButton : Button
    lateinit var saveButton : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.recipe_editor)

        //get UI elements
        chooseImageButton = findViewById(R.id.re_ChooseImage)
        recipeImage = findViewById(R.id.re_Image)
        recipeNameTitle = findViewById(R.id.re_recipe_name_EditText)
        recipeDescription = findViewById(R.id.re_description_EditTextMultiLine)
        recipeSteps = findViewById(R.id.re_steps_EditTextMultiLine)
        cancelButton = findViewById(R.id.re_cancel_Button)
        deleteButton = findViewById(R.id.re_delete_Button)
        saveButton = findViewById(R.id.re_save_Button)

        //get the recipe to edit
        recipe = intent.extras?.getInt("recipeID")?.let { RecipeManager.getRecipe(it) }
        Log.d(TAG, "Editing recipe with ID = ${recipe?.recipeID} and name = ${recipe?.name}")

        //set UI elements to values in Recipe
        //TODO set the image
        recipeNameTitle.setText(recipe?.name)
        recipeDescription.setText(recipe!!.description)
        recipeSteps.setText(recipe!!.details)

        //set OnClick listeners for buttons
        chooseImageButton.setOnClickListener() {
            //TODO ImageSelectionActivity. set functionality for image selection button
        }
        cancelButton.setOnClickListener() {
            cancel()
        }
        deleteButton.setOnClickListener(){
            delete()
        }
        saveButton.setOnClickListener() {
            save()
        }
    }

    fun cancel(){
        finish()
    }

    fun delete(){
        val idToDelete = recipe?.recipeID
        if (idToDelete != null) {
            RecipeManager.deleteRecipe(idToDelete)
            val numFoldersRecipeRemovedFrom =
                FolderManager.deleteRecipe(idToDelete)

            // Display a Toast notification with the number of folders the recipe was removed from
            Toast.makeText(
                applicationContext,
                "Recipe removed from $numFoldersRecipeRemovedFrom folders",
                Toast.LENGTH_SHORT
            ).show()

            //switch to the MainActivity
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }

    fun save(){
        //create new recipe
        val newRecipe = Recipe()
        newRecipe.recipeID = recipe!!.recipeID

        //TODO save the image

        //set values in recipe = UI values
        newRecipe.name = recipeNameTitle.text.toString()
        newRecipe.description = recipeDescription.text.toString()
        newRecipe.details = recipeSteps.text.toString()

        //save the recipe to RecipeManager
        RecipeManager.updateRecipe(newRecipe.recipeID, newRecipe)
        RecipeManager.saveRecipesToFile()

        //return to previous activity
        finish()
    }

    override fun onPause() {
        super.onPause()
        finish()
    }
}