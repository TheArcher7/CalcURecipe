package com.mmock.calcurecipe

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mmock.calcurecipe.adapter.RecipeCondensedAdapter
import com.mmock.calcurecipe.model.Recipe

class SearchActivity : OptionsMenuActivity() {
    private val TAG = "SearchActivity"
    private lateinit var toolbar : Toolbar
    private lateinit var searchBar : EditText
    private lateinit var resultsTV : TextView
    private lateinit var recycler : RecyclerView

    private var recipeList : ArrayList<Recipe>? = null
    private var adapter : RecipeCondensedAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        //get UI elements
        toolbar = findViewById(R.id.ash_toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.title = "Search"
        searchBar = findViewById(R.id.ash_searchEditText)
        resultsTV = findViewById(R.id.ash_resultsCountTextView)

        //set recycler
        recycler = findViewById(R.id.ash_searchRecyclerView)
        recycler.layoutManager = LinearLayoutManager(applicationContext)
        recycler.itemAnimator = DefaultItemAnimator()

        //get recipes to show
        recipeList = RecipeManager.getRecipeList()

        //set adapter and layout
        adapter = RecipeCondensedAdapter(this, recipeList!!)
        recycler.adapter = adapter

        //set searchBar functionality
        searchBar.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                //get sublist and display in recycler
                val searchQuery = searchBar.text.toString()
                show( RecipeManager.searchRecipesByName(searchQuery) )
            }
        })
    }

    fun show(recipes: ArrayList<Recipe>){
        Log.d(TAG, "Showing new recipes.")
        resultsTV.text = recipes.size.toString()

        val adapter = RecipeCondensedAdapter(this, recipes)
        recycler.adapter = adapter

        // Optionally, notify the adapter of the data change
        adapter.notifyDataSetChanged()
    }

}