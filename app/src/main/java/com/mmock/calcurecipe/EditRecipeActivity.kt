package com.mmock.calcurecipe

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.mmock.calcurecipe.model.Recipe
import java.io.FileOutputStream
import java.io.InputStream

private const val TAG = "EditRecipeActivity"

class EditRecipeActivity : AppCompatActivity() {
    var recipe: Recipe? = null
    private lateinit var chooseImageButton : Button
    private lateinit var recipeImage : ImageView
    private lateinit var recipeNameTitle : EditText
    private lateinit var recipeDescription : EditText
    private lateinit var recipeSteps : EditText
    private lateinit var cancelButton : Button
    private lateinit var deleteButton : Button
    private lateinit var saveButton : Button

    private lateinit var pickMedia: ActivityResultLauncher<PickVisualMediaRequest>
    private var imagePath = ""

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
        //set the image
        displayImage(recipe?.imagePath)
        recipeNameTitle.setText(recipe?.name)
        recipeDescription.setText(recipe!!.description)
        recipeSteps.setText(recipe!!.details)


        //set OnClick listeners for buttons
        registerResult()
        chooseImageButton.setOnClickListener() {
            pickImage()
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

            RecipeManager.saveRecipesToFile(applicationContext)
            FolderManager.saveMappingsToFile(applicationContext)

            //switch to the MainActivity
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }

    fun save(){
        //create new recipe
        val newRecipe = Recipe()
        newRecipe.recipeID = recipe!!.recipeID

        //set the image
        newRecipe.imagePath = imagePath

        //set values in recipe = UI values
        newRecipe.name = recipeNameTitle.text.toString()
        newRecipe.description = recipeDescription.text.toString()
        newRecipe.details = recipeSteps.text.toString()

        //save the recipe to RecipeManager
        RecipeManager.updateRecipe(newRecipe.recipeID, newRecipe)
        RecipeManager.saveRecipesToFile(applicationContext)

        //return to previous activity
        finish()
    }

    override fun onPause() {
        super.onPause()
        RecipeManager.saveRecipesToFile(applicationContext)
    }
}