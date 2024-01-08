package com.mmock.calcurecipe

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresExtension
import com.bumptech.glide.Glide
import com.mmock.calcurecipe.model.Recipe
import java.io.FileOutputStream
import java.io.InputStream

//variables for debugging
private const val TAG = "AddRecipeActivity"

class AddRecipeActivity : OptionsMenuActivity() {
    lateinit var chooseImageButton : Button
    lateinit var recipeImage : ImageView
    lateinit var recipeNameTitle : EditText
    lateinit var recipeDescription : EditText
    lateinit var recipeSteps : EditText
    lateinit var cancelButton : Button
    lateinit var saveButton : Button

    private var toolbar : androidx.appcompat.widget.Toolbar? = null

    // Activity result launcher for the photo picker
    private lateinit var pickMedia: ActivityResultLauncher<PickVisualMediaRequest>
    private lateinit var resultLauncher: ActivityResultLauncher<Intent>
    private var imagePath = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.recipe_editor_create)

        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.title = "Add Recipe"

        //get the UI elements
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
        registerResult()
        chooseImageButton.setOnClickListener() {
            pickImage()
        }

        cancelButton.setOnClickListener() {
            cancel()
        }
        saveButton.setOnClickListener() {
            save()
        }

    }

    private fun pickImage(){
        Log.d(TAG, "Picking Image from Gallery")
        // Launch the photo picker
        val pickMediaRequest = PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
        pickMedia.launch(pickMediaRequest)
    }

    private fun registerResult(){
        // Initialize the photo picker activity result launcher
        pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            try {
                if (uri != null) {
                    // Get the bitmap from the URI
                    val inputStream: InputStream? = contentResolver.openInputStream(uri)
                    val bitmap: Bitmap? = BitmapFactory.decodeStream(inputStream)

                    // Save the bitmap to internal storage
                    val imagePath = saveImageToInternalStorage(bitmap)

                    // Set the imagePath variable to the saved path
                    this.imagePath = imagePath

                    // Set the image in the ImageView
                    recipeImage.setImageURI(uri)

                    Log.d("PhotoPicker", "Selected URI: $uri")
                    Log.d("PhotoPicker", "Image saved to: $imagePath")
                } else {
                    Log.d("PhotoPicker", "No media selected")
                }
            } catch (e: Exception) {
                // Handle exceptions, such as IOException or decoding issues
                Log.e("PhotoPicker", "Error processing image", e)
                Toast.makeText(this, "Error processing image", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun saveImageToInternalStorage(bitmap: Bitmap?): String {
        val filename = "image_${System.currentTimeMillis()}.jpg"
        val fileOutputStream: FileOutputStream = openFileOutput(filename, Context.MODE_PRIVATE)

        bitmap?.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream)
        fileOutputStream.close()

        return getFileStreamPath(filename).absolutePath
    }

    fun displayImage(path: String){
        if (path == "") {
            //use default image
            recipeImage.setImageResource(R.drawable.default_recipe_image_pancakes)
            return
        }
        Glide.with(this)
            .load(path)
            .into(recipeImage)
    }

    fun cancel(){
        finish()
    }

    fun save(){
        //optional TODO fields should not be blank or too large

        //create new recipe
        val recipe = Recipe()

        //set the image
        recipe.imagePath = imagePath

        //set values in recipe = UI values
        recipe.name = recipeNameTitle.text.toString()
        recipe.description = recipeDescription.text.toString()
        recipe.details = recipeSteps.text.toString()

        //save the recipe to RecipeManager
        RecipeManager.addRecipe(recipe)
        RecipeManager.saveRecipesToFile(applicationContext)

        //return to previous activity
        finish()
    }
}