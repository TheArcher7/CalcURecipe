package com.mmock.calcurecipe

import android.os.Bundle
import android.os.PersistableBundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mmock.calcurecipe.adapter.SaveRecipeAdapter
import com.mmock.calcurecipe.model.Recipe

class SaveRecipeActivity : AppCompatActivity() {
    private lateinit var btnDone : Button
    private lateinit var recycler : RecyclerView
    private lateinit var adapter : SaveRecipeAdapter
    private lateinit var recipe : Recipe
    private lateinit var btnNewFolder : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_save_recipe)

        // Get recipe and folders
        val recipeID = intent.extras?.getInt("recipeID") ?: -1
        recipe = RecipeManager.getRecipe(recipeID)!!
        val folders = FolderManager.sortFolders()

        // Set recycler
        recycler = findViewById(R.id.asr_recycler)
        recycler.layoutManager = LinearLayoutManager(applicationContext)
        recycler.itemAnimator = DefaultItemAnimator()

        // Set adapter
        adapter = SaveRecipeAdapter(this, folders, recipeID)
        recycler.adapter = adapter

        // Get UI buttons
        btnDone = findViewById(R.id.asr_doneButton)
        btnNewFolder = findViewById(R.id.asr_addFolderButton)

        // Set button clicks
        btnDone.setOnClickListener {
            FolderManager.saveMappingsToFile(applicationContext)
            finish()
        }

        btnNewFolder.setOnClickListener {
            //summon new folder dialog
            addNewFolder()
        }
    }

    private fun addNewFolder(){
        // Handle the click event for adding a folder
        val dialog = DialogNewFolder()
        dialog.show(supportFragmentManager, "")
        FolderManager.saveFoldersToFile(applicationContext)
    }

    override fun onPause() {
        super.onPause()
        FolderManager.saveMappingsToFile(applicationContext)
    }

}