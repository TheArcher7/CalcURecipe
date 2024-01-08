package com.mmock.calcurecipe

//Author Micah Mock
//Colorado State University Global
//CSC475: Platform-based Development
//Professor Chintan Thakkar
//Due date: 24 December 2023

//resources
//How to implement a custom menu (Java) https://youtu.be/zOsWCAsG2Xo?si=8QWOjJ3HMTmoX2hq
//Custom menu across multiple activities (Java) https://youtu.be/G9yi92qdxBg?si=PyI5BRYFIaKbZ4kd
//Bottom view navigation bar across multiple fragments (Kotlin) https://youtu.be/L_6poZGNXOo?si=Jfar3kOojvIiXLSy
//Collapsing toolbar with image, which is good for the recipe details (Kotlin) https://youtu.be/6UmHGn076To?si=6g0NVmKOuvhE2Ust
//Android 13 Photo Picker (Java) https://youtu.be/uHX5NB6wHao?si=ONDMvCHnOVMkqN9t


import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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
        RecipeManager.loadRecipesFromFile(applicationContext)
        FolderManager.loadData(applicationContext)
        recipeList = RecipeManager.getRecipeList()

        //define the recyclerView
        recipeExplorerRecycler = findViewById(R.id.explorerRecyclerView)
        val layoutManager = LinearLayoutManager(applicationContext)
        recipeExplorerRecycler!!.layoutManager = layoutManager
        recipeExplorerRecycler!!.itemAnimator = DefaultItemAnimator()

        //add a dividing line between list items
        val prefs = getSharedPreferences("CalcURecipePrefs", Context.MODE_PRIVATE)
        val showDividers = prefs.getBoolean("dividers", true)
        if (showDividers)
            recipeExplorerRecycler!!.addItemDecoration(DividerItemDecoration(this, LinearLayoutManager.VERTICAL))
        else {
            //check there are some dividers or the app will crash
            if(recipeExplorerRecycler!!.itemDecorationCount > 0)
                recipeExplorerRecycler!!.removeItemDecorationAt(0)
        }

        //define the adapter
        adapter = RecipeExplorerAdapter(this, recipeList!!)
        // Set the listener in RecipeManager
        RecipeManager.setRecipeListener(adapter!!)
        recipeExplorerRecycler!!.adapter = adapter
    }

    fun showRecipe(recipeToShow: Int) {
        //send intent with recipeToShow to view object to be displayed by recipe_details.xml
        val intent = Intent(this, DetailsRecipeActivity::class.java)
        intent.putExtra("recipe_loc", recipeToShow)
        startActivity(intent)
    }
}

