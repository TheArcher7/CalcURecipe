package com.mmock.calcurecipe

import com.mmock.calcurecipe.model.Recipe

//optional TODO implement caching
//optional TODO arraylist sorting by different types (I would like to use something like RecipeManager.SORT_BY_ID_DESC when calling the sort method from other classes)
//optional TODO implement multithreading and safety

object RecipeManager {
    private val masterRecipeList : ArrayList<Recipe> = ArrayList()
    private var recipeListener: RecipeListener? = null

    // Interface for the listener
    interface RecipeListener {
        fun onRecipeAdded(position: Int)
        fun onRecipeDeleted(position: Int)
    }

    fun setRecipeListener(listener: RecipeListener) {
        recipeListener = listener
    }

    // Add a recipe to the master list
    fun addRecipe(recipe: Recipe) {
        // Check if the recipe has an ID of -1
        if (recipe.id == -1) {
            // If the list is not empty, assign the next ID
            if (masterRecipeList.isNotEmpty()) {
                val lastRecipeId = masterRecipeList.last().id
                recipe.id = lastRecipeId + 1
            } else {
                // If the list is empty, assign ID 0
                recipe.id = 0
            }
        }
        // Add the recipe to the master list
        val position = masterRecipeList.size
        masterRecipeList.add(recipe)

        // Notify the listener (adapter) about the new recipe
        recipeListener?.onRecipeAdded(position)
    }

    // Delete a recipe from the master list by its ID
    fun deleteRecipe(recipeId: Int) {
        val position = masterRecipeList.indexOfFirst { it.id == recipeId }
        if (position != -1) {
            masterRecipeList.removeAt(position)

            // Notify the listener (adapter) about the deleted recipe
            recipeListener?.onRecipeDeleted(position)
        }
    }

    // Get a recipe by its ID
    fun getRecipe(recipeId: Int): Recipe? {
        return masterRecipeList.find { it.id == recipeId }
    }

    // Clear the entire recipe list
    fun clearRecipeList() {
        masterRecipeList.clear()
    }

    // Get a sublist of recipes by their IDs
    fun getSublist(recipeIds: IntArray): ArrayList<Recipe> {
        //To be used by savedLists activity to get and display recipes

        val sublist = ArrayList<Recipe>()
        for (recipeId in recipeIds) {
            getRecipe(recipeId)?.let { sublist.add(it) }
        }
        return sublist
    }

    // Get the entire recipe list
    fun getRecipeList(): ArrayList<Recipe> {
        return masterRecipeList
    }

    // Get a sorted sublist of recipes by their IDs
    fun getSortedSublist(sortType: String, recipesToGet: IntArray): ArrayList<Recipe> {
        val sublist = getSublist(recipesToGet)
        return when (sortType) {
            "Name" -> sublist.sortedBy { it.name } as ArrayList<Recipe>
            "Description" -> sublist.sortedBy { it.description } as ArrayList<Recipe>
            "ID" -> sublist.sortedBy { it.id } as ArrayList<Recipe>
            // Add more sorting options as needed
            else -> sublist // Default to returning the unsorted sublist
        }
    }

    // Get the entire recipe list sorted
    fun getSortedRecipeList(sortType: String): ArrayList<Recipe> {
        return when (sortType) {
            "Name" -> masterRecipeList.sortedBy { it.name } as ArrayList<Recipe>
            "Description" -> masterRecipeList.sortedBy { it.description } as ArrayList<Recipe>
            "ID" -> masterRecipeList.sortedBy { it.id } as ArrayList<Recipe>
            // Add more sorting options as needed
            else -> masterRecipeList // Default to returning the unsorted master list
        }
    }


    fun uploadRecipesFromFile() {
        // Implementation to load recipes from a file
        // TODO Load recipes from a file
        // TODO Access images from media storage
    }


    fun saveRecipesToFile() {
        // Implementation to save recipes to a file
        // TODO Save recipes to a file
    }

}