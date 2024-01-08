package com.mmock.calcurecipe

import android.content.Context
import android.util.Log
import com.mmock.calcurecipe.model.ListFolder
import com.mmock.calcurecipe.model.Recipe
import com.mmock.calcurecipe.model.RecipeFolderMapping
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONTokener
import java.io.BufferedReader
import java.io.FileNotFoundException
import java.io.IOException
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.io.Writer

object FolderManager {
    private const val filename_folders = "CalcURecipe_Folders.json"
    private const val filename_mappings = "CalcURecipe_Mappings.json"
    private const val TAG = "FolderManager"

    private var recipeFolderMappings: ArrayList<RecipeFolderMapping> = ArrayList()
    private var folderList: ArrayList<ListFolder> = ArrayList()
    private var folderCounts: MutableMap<Int, Int> = mutableMapOf()
    private var folderListener: FolderListener? = null

    // Interface for the listener
    interface FolderListener {
        fun onFolderAdded(position: Int)
        fun onFolderDeleted(position: Int)
        fun onFolderUpdated()
    }

    fun setFolderListener(listener: FolderListener) {
        folderListener = listener
    }

    // Add a new folder to folderList and return the assigned ID
    fun addFolder(name: String): Int {
        val newId = generateFolderId() //max ID + 1

        // Create a new folder with the assigned ID
        val folder = ListFolder(name, newId)
        folderList.add(folder)
        folderCounts[newId] = 0

        folderListener?.onFolderAdded(folderList.indexOf(folder))

        return newId
    } // Return the assigned ID

    // Add a mapping between folder and recipe
    fun addMapping(folderID: Int, recipeID: Int) {
        val mapping = RecipeFolderMapping(recipeID, folderID)
        recipeFolderMappings.add(mapping)

        // Update the count for the folder
        folderCounts[folderID] = folderCounts.getOrDefault(folderID, 0) + 1

        folderListener?.onFolderUpdated()
    }

    // Remove a mapping between folder and recipe
    fun removeMapping(folderID: Int, recipeID: Int) {
        val iterator = recipeFolderMappings.iterator()

        while (iterator.hasNext()) {
            val mapping = iterator.next()

            if (mapping.folderID == folderID && mapping.recipeID == recipeID) {
                iterator.remove()

                // Update the count for the folder
                folderCounts[folderID] = folderCounts.getOrDefault(folderID, 0) - 1
                if (folderCounts[folderID] == 0) {
                    folderCounts.remove(folderID)
                }

                return
            }
        }

        folderListener?.onFolderUpdated()
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

            folderListener?.onFolderUpdated()
        }

        return folderIndex
    } //returns the folder index

    // Check if a recipe is in a folder
    fun isRecipeInFolder(recipeID: Int, folderID: Int): Boolean {
        return recipeFolderMappings.any { it.recipeID == recipeID && it.folderID == folderID }
    }

    fun getFolderSize() : Int {
        return folderList.size
    }

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

        return folderList
    }

    // Method to clean up mappings and remove duplicates
    private fun cleanUpMappings() {
        // Create a set to keep track of unique mappings
        val uniqueMappings = HashSet<RecipeFolderMapping>()

        // Iterate through the recipeFolderMappings list
        val iterator = recipeFolderMappings.iterator()
        while (iterator.hasNext()) {
            val mapping = iterator.next()

            // If the mapping is not unique, remove it from the list
            if (!uniqueMappings.add(mapping)) {
                iterator.remove()
            }
        }
    }

    fun saveData(context: Context) {
        try {
            saveMappingsToFile(context)
            saveFoldersToFile(context)
        } catch (e: Exception) {
            Log.e(TAG, "Error saving folders and mappings", e)
        }
    }

    @Throws(IOException::class, JSONException::class)
    fun saveMappingsToFile(context: Context){
        cleanUpMappings()

        //make an array in JSON format
        val jArray = JSONArray()

        //load it with mappings
        for (m in recipeFolderMappings)
            jArray.put(m.convertToJSON())

        //write it to the private disk space of the app
        var writer: Writer? = null
        try{
            val out = context.openFileOutput(filename_mappings, Context.MODE_PRIVATE)
            writer = OutputStreamWriter(out)
            writer.write(jArray.toString())
        } finally {
            writer?.close()
        }
    }

    @Throws(IOException::class, JSONException::class)
    fun saveFoldersToFile(context: Context) {
        //make an array in JSON format
        val jArray = JSONArray()

        //load it with folders
        for (f in folderList)
            jArray.put(f.convertToJSON())

        //write it to the private disk space of the app
        var writer: Writer? = null
        try{
            val out = context.openFileOutput(filename_folders, Context.MODE_PRIVATE)
            writer = OutputStreamWriter(out)
            writer.write(jArray.toString())
        } finally {
            writer?.close()
        }
    }


    fun loadData(context: Context) {
        if (folderList.isNotEmpty()) {
            return
        } //will only load files upon app opening

        try {
            loadMappingsFromFile(context)
            loadFoldersFromFile(context)
        } catch (e: Exception) {
            Log.e(TAG, "Error loading data from file. Using default folders. ", e)
        }
        Log.d(TAG, "${folderList.size} folders loaded from file.")


        if(folderList.isEmpty()) {
            loadDefaultFolders()
        } //will only create default folders upon app install
    }


    @Throws(IOException::class, JSONException::class)
    private fun loadMappingsFromFile(context: Context){
        val mappingsList = ArrayList<RecipeFolderMapping>()
        var reader: BufferedReader? = null

        try {
            val `in` = context.openFileInput(filename_mappings)
            reader = BufferedReader(InputStreamReader(`in`))
            val jsonString = StringBuilder()

            for (line in reader.readLine()) {
                jsonString.append(line)
            }

            val jArray = JSONTokener(jsonString.toString()).nextValue() as JSONArray

            for (i in 0 until jArray.length()) {
                val mapping = RecipeFolderMapping(jArray.getJSONObject(i))
                mappingsList.add(mapping)

                // Update folderCounts based on the loaded mappings
                folderCounts[mapping.folderID] = folderCounts.getOrDefault(mapping.folderID, 0) + 1
            }
        } catch (e: FileNotFoundException) {
            //This happens when we start fresh
            Log.e(TAG, "No $filename_mappings file found.")
        } finally {
            reader?.close()
        }

        Log.d(TAG, "${mappingsList.size} mappings loaded from file.")

        recipeFolderMappings = mappingsList
    }

    @Throws(IOException::class, JSONException::class)
    private fun loadFoldersFromFile(context: Context){
        val folderList = ArrayList<ListFolder>()
        var reader: BufferedReader? = null

        try {
            val `in` = context.openFileInput(filename_folders)
            reader = BufferedReader(InputStreamReader(`in`))
            val jsonString = StringBuilder()

            for (line in reader.readLine()) {
                jsonString.append(line)
            }

            val jArray = JSONTokener(jsonString.toString()).nextValue() as JSONArray

            for (i in 0 until jArray.length()) {
                folderList.add(ListFolder(jArray.getJSONObject(i)))
            }
        } catch (e: FileNotFoundException) {
            //This happens when we start fresh
            Log.e(TAG, "No $filename_folders file found.")
        } finally {
            reader?.close()
        }

        this.folderList = folderList
    }

    //Load liked list
    private fun loadDefaultFolders(){
        Log.d(TAG, "Loading default folders.")
        addFolder("Liked")

        //TODO remove the following before deployment. Folder manager cannot handle mappings to wrong ids
        addFolder("My first list")
        addMapping(1, 0)
        addMapping(1, 1)
        addMapping(1, 2)

        addFolder("Ultimate recipes")
        addMapping(2, 1)
        addMapping(2, 2)

        Log.d(TAG, "Folder list size = ${folderList.size}")
    }
}
