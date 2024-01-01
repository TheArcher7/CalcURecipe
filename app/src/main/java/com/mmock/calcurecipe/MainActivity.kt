package com.mmock.calcurecipe

//resources
//How to implement a custom menu (Java) https://youtu.be/zOsWCAsG2Xo?si=8QWOjJ3HMTmoX2hq
//Custom menu across multiple activities (Java) https://youtu.be/G9yi92qdxBg?si=PyI5BRYFIaKbZ4kd
//Bottom view navigation bar across multiple fragments (Kotlin) https://youtu.be/L_6poZGNXOo?si=Jfar3kOojvIiXLSy
//Collapsing toolbar with image, which is good for the recipe details (Kotlin) https://youtu.be/6UmHGn076To?si=6g0NVmKOuvhE2Ust


import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import android.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.MaterialToolbar
import com.mmock.calcurecipe.adapter.RecipeExplorerAdapter
import com.mmock.calcurecipe.databinding.ActivityMainBinding
import com.mmock.calcurecipe.model.Recipe

private const val TAG = "MainActivity"

class MainActivity : OptionsMenuActivity() {
    private var recipeExplorerRecycler: RecyclerView? = null
    private var recipeList : ArrayList<Recipe>? = null
    private var adapter : RecipeExplorerAdapter? = null
    private var toolbar : androidx.appcompat.widget.Toolbar? = null

    //binding (set to true in the module-level build.gradle)
    private lateinit var binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //set the toolbar
        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.title = "CalcURecipe"

        //request permissions
        PermissionsManager.requestReadMediaPermission(this, this, TAG)

        //load recipes
        try{
            RecipeManager.uploadRecipesFromFile()
            //see the Contacts Keeper app for example
            recipeList = RecipeManager.getRecipeList()
        } catch(e : Exception) {
            recipeList = ArrayList()
        }

        //define the adapter
        adapter = RecipeExplorerAdapter(this, recipeList!!)
        // Set the listener in RecipeManager
        RecipeManager.setRecipeListener(adapter!!)

        //define the recyclerView
        recipeExplorerRecycler = findViewById<RecyclerView>(R.id.explorerRecyclerView)
        val layoutManager = LinearLayoutManager(applicationContext)
        recipeExplorerRecycler!!.layoutManager = layoutManager
        recipeExplorerRecycler!!.itemAnimator = DefaultItemAnimator()
        recipeExplorerRecycler!!.adapter = adapter

    }



    fun showRecipe(recipeToShow: Int) {
        //TODO send intent with recipeToShow to view object to be displayed by recipe_details.xml
    }

    fun createNewRecipe(recipe : Recipe) {
        RecipeManager.addRecipe(recipe)
    }

    //TODO override onPause() and onResume()
}

