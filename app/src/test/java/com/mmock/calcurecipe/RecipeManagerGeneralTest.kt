package com.mmock.calcurecipe

import android.util.Log
import com.mmock.calcurecipe.model.Recipe
import org.junit.Assert
import org.junit.Before
import org.junit.Ignore
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner
import java.io.File

class RecipeManagerGeneralTest {

    private lateinit var recipeManager: RecipeManager

    @Before
    fun setUp() {
        recipeManager = RecipeManager
    }

    @Test
    fun addRecipe_Test() {
        // Test adding a recipe
        val recipe = Recipe("Test Recipe", "Description", "List of steps: \n 1. This is the first step \n 2. This is step two")
        recipeManager.addRecipe(recipe)

        val retrievedRecipe = recipeManager.getRecipe(recipe.recipeID)

        Assert.assertNotNull(retrievedRecipe)
        Assert.assertEquals(recipe.name, retrievedRecipe?.name)
    }

    @Test
    fun deleteRecipe_Test() {
        // Test deleting a recipe
        val recipe = Recipe("Test Recipe", "Description", "List of steps: \n 1. This is the first step \n 2. This is step two")
        recipeManager.addRecipe(recipe)

        val recipeId = recipe.recipeID

            recipeManager.deleteRecipe(recipeId)

        val deletedRecipe = recipeManager.getRecipe(recipeId)

        Assert.assertNull(deletedRecipe)
    }

    @Ignore
    fun deleteImageFromInternalStorage_Test() {
        // Test deleting an image from internal storage
        val imagePath = "/fake/path/to/image.jpg"

        val file = File(imagePath)
        file.createNewFile()

        Assert.assertTrue(file.exists())

        //recipeManager.deleteImageFromInternalStorage(imagePath)

        Assert.assertFalse(file.exists())
    }

    @Test
    fun getRecipeByPosition_Test() {
        // Test getting a recipe by its position
        val recipe = Recipe("Test Recipe", "Description", "List of steps: \n 1. This is the first step \n 2. This is step two")
        recipeManager.addRecipe(recipe)

        val recipePosition = 0
        val retrievedRecipe = recipeManager.getRecipeByPosition(recipePosition)

        Assert.assertNotNull(retrievedRecipe)
        Assert.assertEquals(recipe.name, retrievedRecipe?.name)
    }



    // TODO Add more test methods for other RecipeManager methods
}
