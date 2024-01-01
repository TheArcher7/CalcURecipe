package com.mmock.calcurecipe

import com.mmock.calcurecipe.model.Recipe
import com.mmock.calcurecipe.model.RecipeFolderMapping

object RecipeFolderMapManager {
    private val recipeFolderMappings: ArrayList<RecipeFolderMapping> = ArrayList()

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

    fun getFolders(recipeID: Int): ArrayList<RecipeFolderMapping> {
        val foldersForRecipe = ArrayList<RecipeFolderMapping>()
        for (mapping in recipeFolderMappings) {
            if (mapping.recipeID == recipeID) {
                foldersForRecipe.add(mapping)
            }
        }
        return foldersForRecipe
    }

    fun addMapping(folderID: Int, recipeID: Int) {
        val mapping = RecipeFolderMapping(recipeID, folderID)
        recipeFolderMappings.add(mapping)
    }

    fun deleteRecipe(recipeID: Int): Int {
        val iterator = recipeFolderMappings.iterator()
        var numElementsRemoved = 0

        while (iterator.hasNext()) {
            val mapping = iterator.next()
            if (mapping.recipeID == recipeID) {
                iterator.remove()
                numElementsRemoved++
            }
        }

        return numElementsRemoved
    }

    fun deleteFolder(folderID: Int): Int {
        if (folderID == 0) {
            // Prevents deleting folder 0
            return 0
        }

        val iterator = recipeFolderMappings.iterator()
        var numElementsRemoved = 0

        while (iterator.hasNext()) {
            val mapping = iterator.next()
            if (mapping.folderID == folderID) {
                iterator.remove()
                numElementsRemoved++
            }
        }

        return numElementsRemoved
    }

    private fun findRecipeById(recipeID: Int): Recipe? {
        return RecipeManager.getRecipe(recipeID)
    }

    //TODO save mappings to file
    //TODO load mappings from file
}
