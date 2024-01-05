package com.mmock.calcurecipe

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mmock.calcurecipe.adapter.RecipeCondensedAdapter
import com.mmock.calcurecipe.model.ListFolder
import com.mmock.calcurecipe.model.Recipe

class FolderContentsActivity : AppCompatActivity() {
    private lateinit var toolbar : Toolbar
    private lateinit var folderNameTextView: TextView
    private lateinit var backButton: ImageView
    private lateinit var editButton: ImageView
    private lateinit var recycler : RecyclerView
    private lateinit var folder : ListFolder

    private var recipeList : ArrayList<Recipe>? = null
    private var adapter : RecipeCondensedAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_folder_contents)

        // Set up Toolbar
        toolbar = findViewById(R.id.afc_toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.title = ""
        //supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Get UI elements
        folderNameTextView = findViewById(R.id.afc_folderName)
        backButton = findViewById(R.id.afc_backButton)
        editButton = findViewById(R.id.afc_editButton)

        //set recycler
        recycler = findViewById(R.id.afc_recyclerView)
        recycler.layoutManager = LinearLayoutManager(applicationContext)
        recycler.itemAnimator = DefaultItemAnimator()

        // Get recipes in the folder to show
        val id = intent.getIntExtra("id", 0)
        folder = FolderManager.getFolderById(id)!!
        folderNameTextView.text = folder.folderName
        recipeList = FolderManager.getRecipes(id)

        // Set the adapter
        adapter = RecipeCondensedAdapter(this, recipeList!!)
        recycler.adapter = adapter

        // Set edit button click listener
        editButton.setOnClickListener {
            // TODO: Handle edit button click
            // navigate to an edit activity or show an edit dialog
        }

        backButton.setOnClickListener {
            goBack()
        }
    }

    fun goBack(){
        // TODO: Handle back button click
        finish()
    }
}