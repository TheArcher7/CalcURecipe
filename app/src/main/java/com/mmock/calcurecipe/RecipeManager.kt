package com.mmock.calcurecipe

import android.content.Context
import android.util.Log
import com.mmock.calcurecipe.model.Recipe
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONTokener
import java.io.BufferedReader
import java.io.File
import java.io.FileNotFoundException
import java.io.IOException
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.io.Writer

//optional TODO implement caching
//optional TODO arraylist sorting by different types (I would like to use something like RecipeManager.SORT_BY_ID_DESC when calling the sort method from other classes)
//optional TODO implement multithreading and safety

object RecipeManager {
    private const val TAG = "RecipeManager"
    private const val filename = "CalcURecipe_Recipes.json"
    private var masterRecipeList : ArrayList<Recipe> = ArrayList()
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
        if (recipe.recipeID == -1) {
            // If the list is not empty, assign the next ID
            if (masterRecipeList.isNotEmpty()) {
                val lastRecipeId = masterRecipeList.last().recipeID
                recipe.recipeID = lastRecipeId + 1
            } else {
                // If the list is empty, assign ID 0
                recipe.recipeID = 0
            }
        }
        // Add the recipe to the master list
        val position = masterRecipeList.size
        masterRecipeList.add(recipe)

        // Notify the listener (adapter) about the new recipe
        recipeListener?.onRecipeAdded(position)

        //Log.d(TAG, "Created a new recipe with id = ${recipe.recipeID}")
    }

    // Delete a recipe from the master list by its ID
    fun deleteRecipe(recipeId: Int) {
        val position = masterRecipeList.indexOfFirst { it.recipeID == recipeId }
        if (position != -1) {
            // Delete the associated image from internal storage
            val imagePath = masterRecipeList[position].imagePath
            deleteImageFromInternalStorage(imagePath)

            // Remove the recipe from the master list
            masterRecipeList.removeAt(position)

            // Notify the listener (adapter) about the deleted recipe
            recipeListener?.onRecipeDeleted(position)

            //Log.d(TAG, "Deleted recipe with id = $recipeId")
        }
    }

    // Helper function to delete an image from internal storage
    private fun deleteImageFromInternalStorage(imagePath: String?) {
        if (imagePath != null) {
            try {
                val file = File(imagePath)
                val deleted = file.delete()
                if (deleted) {
                    //Log.d(TAG, "Deleted image from internal storage: $imagePath")
                } else {
                    //Log.e(TAG, "Failed to delete image from internal storage: $imagePath")
                }
            } catch (e: Exception) {
                //Log.e(TAG, "Error deleting image from internal storage", e)
            }
        }
    }

    // Get a recipe by its ID
    fun getRecipe(recipeId: Int): Recipe? {
        return masterRecipeList.find { it.recipeID == recipeId }
    }

    fun getRecipeByPosition(recipeArraylistInt: Int) : Recipe? {
        if ((recipeArraylistInt < 0) or (recipeArraylistInt > masterRecipeList.size)){
            Log.e("RecipeManager", "OutOfBounds. Error getting recipe from arraylist in RecipeManager. Id = $recipeArraylistInt")
        }
        return masterRecipeList[recipeArraylistInt]
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

    // Update a recipe in masterRecipeList by recipe ID. Returns the index number of the recipe that was replaced
    fun updateRecipe(recipeID: Int, newRecipe: Recipe): Int {
        val index = findRecipeIndexById(recipeID)
        if (index != -1) {
            masterRecipeList[index] = newRecipe
        }
        return index
    }

    // Update a recipe in masterRecipeList by position/index
    fun updateRecipeByPosition(recipeIndex: Int, newRecipe: Recipe): Boolean {
        if (recipeIndex in 0 until masterRecipeList.size) {
            masterRecipeList[recipeIndex] = newRecipe
            return true
        }
        return false
    }

    // Helper function to find the index of a recipe by its ID
    private fun findRecipeIndexById(recipeID: Int): Int {
        for (i in masterRecipeList.indices) {
            if (masterRecipeList[i].recipeID == recipeID) {
                return i
            }
        }
        return -1
    }

    // Get a sorted sublist of recipes by their IDs
    fun getSortedSublist(sortType: String, recipesToGet: IntArray): ArrayList<Recipe> {
        val sublist = getSublist(recipesToGet)
        return when (sortType) {
            "Name" -> sublist.sortedBy { it.name } as ArrayList<Recipe>
            "Description" -> sublist.sortedBy { it.description } as ArrayList<Recipe>
            "ID" -> sublist.sortedBy { it.recipeID } as ArrayList<Recipe>
            // Add more sorting options as needed
            else -> sublist // Default to returning the unsorted sublist
        }
    }

    // Get the entire recipe list sorted
    fun getSortedRecipeList(sortType: String): ArrayList<Recipe> {
        return when (sortType) {
            "Name" -> masterRecipeList.sortedBy { it.name } as ArrayList<Recipe>
            "Description" -> masterRecipeList.sortedBy { it.description } as ArrayList<Recipe>
            "ID" -> masterRecipeList.sortedBy { it.recipeID } as ArrayList<Recipe>
            // Add more sorting options as needed
            else -> masterRecipeList // Default to returning the unsorted master list
        }
    }

    // Search recipes by name and return them sorted by name
    fun searchRecipesByName(query: String): ArrayList<Recipe> {
        val matchingRecipes = masterRecipeList.filter { it.name.contains(query, ignoreCase = true) }
        return ArrayList(matchingRecipes.sortedBy { it.name })
    }

    @Throws(IOException::class, JSONException::class)
    fun saveRecipesToFile(context: Context) {
        val jArray = JSONArray()

        for(r in masterRecipeList)
            jArray.put(r.convertToJSON())

        var writer: Writer? = null
        try{
            val out = context.openFileOutput(filename, Context.MODE_PRIVATE)
            writer = OutputStreamWriter(out)
            writer.write(jArray.toString())
        } finally {
            writer?.close()
        }
    }

    @Throws(IOException::class, JSONException::class)
    fun loadRecipesFromFile(context: Context) {
        if (masterRecipeList.isNotEmpty()) {
            return
        } //will only load recipes from file upon app opening

        val recipeList = ArrayList<Recipe>()
        var reader: BufferedReader? = null

        try {
            val `in` = context.openFileInput(filename)
            reader = BufferedReader(InputStreamReader(`in`))
            val jsonString = StringBuilder()

            for (line in reader.readLine()) {
                jsonString.append(line)
            }

            val jArray = JSONTokener(jsonString.toString()).nextValue() as JSONArray

            for (i in 0 until jArray.length()) {
                recipeList.add(Recipe(jArray.getJSONObject(i)))
            }
        } catch (e: FileNotFoundException) {
            //This happens when we start fresh
            Log.e(TAG, "No $filename file found.")
        } finally {
            reader?.close()
        }

        Log.d(TAG, "${recipeList.size} recipes loaded from file.")

        masterRecipeList = recipeList

        if(masterRecipeList.isEmpty()) {
            loadDefaultRecipes()
        } //will only load default recipes for newly downloaded app
    }

    private fun loadDefaultRecipes(){
        Log.d(TAG, "Loading default recipes.")

        var recipe = Recipe(
            "Morning Bread",
            "A very strange kind of bread that most people would call a pan \"cake\" since it was traditionally made in a pan.",
            "1 put together some lettuce \n2 add the grain of choice, in this case oatmeal \n3 mix well in bowl and serve"
        )
        addRecipe(recipe)

        recipe = Recipe(
            "Basic Pancake",
            "A recipe found in Grandma's cookbook",
            "You likely already have everything you need to make this pancake recipe. If not, here's what to add to your grocery list:\n" +
                    "\n" +
                    "· Flour: This homemade pancake recipe starts with all-purpose flour.\n" +
                    "· Baking powder: Baking powder, a leavener, is the secret to fluffy pancakes.\n" +
                    "· Sugar: Just a tablespoon of white sugar is all you'll need for subtly sweet pancakes.\n" +
                    "· Salt: A pinch of salt will enhance the overall flavor without making your pancakes taste salty.\n" +
                    "· Milk and butter: Milk and butter add moisture and richness to the pancakes.\n" +
                    "· Egg: A whole egg lends even more moisture. Plus, it helps bind the pancake batter together."
        )
        addRecipe(recipe)

        recipe = Recipe(
            "Ultimate Pancake Recipe",
            "You and your family are probably not ready for this. The taste of this pancake will melt your mouth and leave you hungry for more.",
            "1. Preheat your good quality, non stick pan or griddle on medium heat first. Once it’s hot, lower the heat down to low-medium heat. Wait about two minutes. This part is crucial and so worth it. Lightly grease the pan with a small amount of butter (yes, even on non stick pans), and wipe away any excess butter with a sheet of paper towel.\n" +
                    "2. Use a 1/4 cup measuring cup to pour your batter so you get perfect, evenly sized pancakes. Start pouring from the middle, then continue pouring slowly in a circular motion so that the batter spreads into perfect round shapes.\n" +
                    "3. Allow the pancakes to cook properly! Don’t rush them on high heat, or flip them too early. Let that under-side cook to a beautiful golden brown colour, and when bubbles start forming on top and around the surface (batter side), they are ready to flip.\n" +
                    "4. How to get that flip perfected without creating lopsided pancakes? Grab a good, plastic spatula and slide it underneath the pancake. Use your WRIST to gently flip them instead of using your whole arm. You’ll know what I mean when you’re in front of your pan ready to flip. THIS makes a HUGE difference."
        )
        addRecipe(recipe)

        Log.d(TAG, "Master recipe list size = ${masterRecipeList.size}")
    }

}