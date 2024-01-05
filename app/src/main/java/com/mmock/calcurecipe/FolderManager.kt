package com.mmock.calcurecipe

import com.mmock.calcurecipe.model.ListFolder
import com.mmock.calcurecipe.model.Recipe
import com.mmock.calcurecipe.model.RecipeFolderMapping

object FolderManager {
    private val recipeFolderMappings: ArrayList<RecipeFolderMapping> = ArrayList()
    private val folderList: ArrayList<ListFolder> = ArrayList()
    private val folderCounts: MutableMap<Int, Int> = mutableMapOf()
    private var folderListener: FolderListener? = null

    // Interface for the listener
    interface FolderListener {
        fun onFolderAdded(position: Int)
        fun onFolderDeleted(position: Int)
    }

    fun setFolderListener(listener: FolderListener) {
        folderListener = listener
    }

    // Add a new folder to folderList and return the assigned ID
    fun addFolder(name: String): Int {
        // Assign a new ID
        val newId = generateFolderId()

        // Create a new folder with the assigned ID
        val folder = ListFolder(name, newId)
        folderList.add(folder)

        // Initialize the count for the new folder
        folderCounts[newId] = 0

        // Notify the listener that a folder was added
        folderListener?.onFolderAdded(folderList.indexOf(folder))

        // Return the assigned ID
        return newId
    }

    // Add a mapping between folder and recipe
    fun addMapping(folderID: Int, recipeID: Int) {
        val mapping = RecipeFolderMapping(recipeID, folderID)
        recipeFolderMappings.add(mapping)

        // Update the count for the folder
        folderCounts[folderID] = folderCounts.getOrDefault(folderID, 0) + 1
    }

    // Get a folder by its ID
    fun getFolderById(folderID: Int): ListFolder? {
        return folderList.find { it.folderID == folderID }
    }

    // Get a list of recipes in a folder
    fun getRecipes(folderID: Int): ArrayList<Recipe> {
        val recipesInFolder = ArrayList<Recipe>()
        for (mapping in recipeFolderMappings) {
            if (mapping.folderID == folderID) {
                val recipe = findRecipeById(mapping.recipeID)
                recipe?.let { recipesInFolder.add(it) }
            }
        }
        return recipesInFolder
    }
    //optionally there is RecipeManager.getSublist( recipeIdList ) which returns an arraylist
    //and there is RecipeManager.getSortedSublist(sortTypeString, recipeIdList) which returns a sorted arraylist

    // Get the number of recipes in a folder
    fun getNumRecipes(folderID: Int): Int {
        return folderCounts.getOrDefault(folderID, 0)
    }

    // Get a list of folders for a recipe
    fun getFolders(recipeID: Int): ArrayList<ListFolder> {
        val foldersForRecipe = ArrayList<ListFolder>()

        for (mapping in recipeFolderMappings) {
            if (mapping.recipeID == recipeID) {
                val folder = folderList.find { it.folderID == mapping.folderID }
                folder?.let { foldersForRecipe.add(it) }
            }
        }

        return foldersForRecipe
    }

    // Delete a recipe and its mappings
    fun deleteRecipe(recipeID: Int): Int {
        val iterator = recipeFolderMappings.iterator()
        var numElementsRemoved = 0

        while (iterator.hasNext()) {
            val mapping = iterator.next()
            if (mapping.recipeID == recipeID) {
                // Update the count for the folder
                folderCounts[mapping.folderID] = folderCounts.getOrDefault(mapping.folderID, 0) - 1

                iterator.remove()
                numElementsRemoved++
            }
        }

        return numElementsRemoved
    } //returns the number of folders the recipe was removed from

    // Delete a folder and its mappings
    fun deleteFolder(folderID: Int): Int {
        if (folderID == 0) {
            // Prevents deleting folder 0
            return 0
        }

        // Delete folder from folderList
        val folderIndex = folderList.indexOfFirst { it.folderID == folderID }
        if (folderIndex != -1) {
            folderList.removeAt(folderIndex)

            // Notify the listener that a folder was deleted
            folderListener?.onFolderDeleted(folderIndex)
        }

        // Delete mappings associated with the folder
        val iterator = recipeFolderMappings.iterator()
        var numElementsRemoved = 0

        while (iterator.hasNext()) {
            val mapping = iterator.next()
            if (mapping.folderID == folderID) {
                // Update the count for the folder
                folderCounts[mapping.folderID] = folderCounts.getOrDefault(mapping.folderID, 0) - 1

                iterator.remove()
                numElementsRemoved++
            }
        }

        return numElementsRemoved
    } //returns the number of elements removed

    // Edit the name of a folder
    fun editFolder(folderID: Int, newName: String): Int {
        if (folderID == 0) {
            // Prevent modifying folder 0
            return -1
        }

        val folderIndex = folderList.indexOfFirst { it.folderID == folderID }
        if (folderIndex != -1) {
            // Update the name of the folder
            folderList[folderIndex].folderName = newName

            // Notify the listener that a folder was modified
            folderListener?.onFolderAdded(folderIndex)
        }

        return folderIndex
    } //returns the folder index


    private fun findRecipeById(recipeID: Int): Recipe? {
        return RecipeManager.getRecipe(recipeID)
    }

    // Helper function to generate a new folder ID
    private fun generateFolderId(): Int {
        // Find the largest existing folder ID and add one to it
        val largestExistingId = folderList.maxByOrNull { it.folderID }?.folderID ?: -1
        return largestExistingId + 1
    }

    // Sort the folderList by name, move folder with ID 0 to the top, and return the new ArrayList
    fun sortFolders(): ArrayList<ListFolder> {
        // Sort by name
        folderList.sortBy { it.folderName }

        // Move folder with ID 0 to the top
        val folderWithZero = folderList.firstOrNull { it.folderID == 0 }
        folderList.remove(folderWithZero)
        if (folderWithZero != null) {
            folderList.add(0, folderWithZero)
        }

        return ArrayList(folderList)
    }



    //TODO save mappings to file
    //TODO load mappings from file

    //TODO save folders to file


    //TODO load folders from file
    fun loadFoldersFromFile(){
        if(folderList.size > 0) {
            return
        } //only create default folders on startup

        loadDefaultFolders()
    }

    //Load liked list
    private fun loadDefaultFolders(){
        addFolder("Liked")

        //TODO remove the following before deployment
        addFolder("My first list")
        addMapping(1, 0)
        addMapping(1, 1)
        addMapping(1, 2)

        addFolder("Ultimate recipes")
        addMapping(2, 1)
        addMapping(2, 2)
    }
}
