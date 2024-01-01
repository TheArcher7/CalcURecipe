package com.mmock.calcurecipe

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import com.mmock.calcurecipe.model.Recipe

//variables for debugging
private const val TAG = "AddRecipeActivity"

class AddRecipeActivity : OptionsMenuActivity() {
    var recipes : ArrayList<Recipe>? = null
    lateinit var chooseImageButton : Button
    lateinit var recipeImage : ImageView
    lateinit var recipeNameTitle : EditText
    lateinit var recipeDescription : EditText
    lateinit var recipeSteps : EditText
    lateinit var cancelButton : Button
    lateinit var saveButton : Button

    private var toolbar : androidx.appcompat.widget.Toolbar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.recipe_editor_create)


        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.title = "Add Recipe"

        //get recipeList
        recipes = RecipeManager.getRecipeList()

        //get elements of UI
        chooseImageButton = findViewById(R.id.rec_ChooseImage)
        recipeImage = findViewById(R.id.rec_Image)
        recipeNameTitle = findViewById(R.id.rec_recipe_name_EditText)
        recipeDescription = findViewById(R.id.rec_description_EditTextMultiLine)
        recipeSteps = findViewById(R.id.rec_steps_EditTextMultiLine)
        cancelButton = findViewById(R.id.rec_cancel_Button)
        saveButton = findViewById(R.id.rec_save_Button)

        //optional TODO textChangeListener
        recipeNameTitle.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {}
        })

        //set button functionality
        chooseImageButton.setOnClickListener() {
            //TODO set functionality for image selection button
        }
        cancelButton.setOnClickListener() {
            cancel()
        }
        saveButton.setOnClickListener() {
            save()
        }

    }

    private fun getStringFromEditText(t: EditText) : String {
        return if (t.text.isEmpty()){
            ""
        } else
            t.text.toString()
    }

    fun cancel(){
        finish()
    }

    fun save(){
        //optional TODO fields should not be blank or too large

        //create new recipe
        val recipe = Recipe()
        //set values in recipe = UI values
        recipe.name = recipeNameTitle.text.toString()
        recipe.description = recipeDescription.text.toString()
        recipe.details = recipeSteps.text.toString()
        //save the recipe to RecipeManager
        RecipeManager.addRecipe(recipe)
        RecipeManager.saveRecipesToFile()
        //return to previous activity
        finish()
    }

    override fun onPause() {
        super.onPause()
        finish()
    }

}