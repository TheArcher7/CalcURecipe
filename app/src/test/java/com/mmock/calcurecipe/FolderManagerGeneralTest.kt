package com.mmock.calcurecipe

import com.mmock.calcurecipe.model.Recipe
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class FolderManagerGeneralTest {

    private lateinit var folderManager: FolderManager
    private lateinit var recipeManager: RecipeManager

    @Before
    fun setUp() {
        folderManager = FolderManager
        folderManager.addFolder("Liked") //the default folder that is always present

        recipeManager = RecipeManager
    }

    @Test
    fun addFolder_Test() {
        // Test adding a new folder
        val folderName = "Test Folder"
        val folderId = folderManager.addFolder(folderName) //folderId should be == 1

        Assert.assertTrue(folderId > 0)
        Assert.assertEquals(folderName, folderManager.getFolderById(folderId)?.folderName)
    }

    @Test
    fun addMapping_Test() {
        // Test adding a mapping between folder and recipe
        val folderId = folderManager.addFolder("Test Folder")
        val recipeId = 1

        folderManager.addMapping(folderId, recipeId)

        Assert.assertTrue(folderManager.isRecipeInFolder(recipeId, folderId))
        Assert.assertEquals(1, folderManager.getNumRecipes(folderId))
    }

    @Test
    fun removeMapping_Test() {
        // Test removing a mapping between folder and recipe
        val folderId = folderManager.addFolder("Test Folder")
        val recipeId = 1

        folderManager.addMapping(folderId, recipeId)
        folderManager.removeMapping(folderId, recipeId)

        Assert.assertFalse(folderManager.isRecipeInFolder(recipeId, folderId))
        Assert.assertEquals(0, folderManager.getNumRecipes(folderId))
    }

    @Test
    fun getFolderById_Test() {
        // Test getting a folder by its ID
        val folderName = "Test Folder"
        val folderId = folderManager.addFolder(folderName)

        val retrievedFolder = folderManager.getFolderById(folderId)

        Assert.assertNotNull(retrievedFolder)
        Assert.assertEquals(folderName, retrievedFolder?.folderName)
        Assert.assertEquals(folderId, retrievedFolder?.folderID)
    }

    @Test
    fun getNumRecipes_Test() {
        // Test getting the number of recipes in a folder
        val folderId = folderManager.addFolder("Test Folder")
        val recipeId = 1

        folderManager.addMapping(folderId, recipeId)

        val numRecipes = folderManager.getNumRecipes(folderId)

        Assert.assertEquals(1, numRecipes)
    }

    @Test
    fun getRecipes_Test() {
        // Initialize default recipes
        val recipe1 = Recipe("Recipe 1", "Description 1", "Steps 1")
        val recipe2 = Recipe("Recipe 2", "Description 2", "Steps 2")
        val recipe3 = Recipe("Recipe 3", "Description 3", "Steps 3")

        // Add recipes to RecipeManager
        recipeManager.addRecipe(recipe1)
        recipeManager.addRecipe(recipe2)
        recipeManager.addRecipe(recipe3)

        // Add recipes to a folder in FolderManager
        val folderId = folderManager.addFolder("Test Folder")
        folderManager.addMapping(folderId, recipe1.recipeID)
        folderManager.addMapping(folderId, recipe2.recipeID)
        folderManager.addMapping(folderId, recipe3.recipeID)

        // Get recipes from the folder
        val recipesInFolder = folderManager.getRecipes(folderId)

        // Verify that the correct recipes are returned
        Assert.assertEquals(3, recipesInFolder.size)
        Assert.assertTrue(recipesInFolder.contains(recipe1))
        Assert.assertTrue(recipesInFolder.contains(recipe2))
        Assert.assertTrue(recipesInFolder.contains(recipe3))
    }

    // TODO Add more test methods and cases

}
