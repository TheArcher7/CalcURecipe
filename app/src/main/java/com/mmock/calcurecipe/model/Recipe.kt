package com.mmock.calcurecipe.model

import org.json.JSONException
import org.json.JSONObject

class Recipe {
    var recipeID: Int = -1 // the saved lists hold a reference to the Recipe id

    // data for the image
    var imagePath: String = ""

    lateinit var name: String
    lateinit var description: String
    lateinit var details: String

    var liked: Boolean = false

    constructor(name: String, description: String, steps: String) {
        this.name = name
        this.description = description
        details = steps
    }

    constructor() {}

    // JSON properties
    private val JSON_ID = "recipeId"
    private val JSON_IMAGE_PATH = "imagePath"
    private val JSON_NAME = "name"
    private val JSON_DESCRIPTION = "description"
    private val JSON_DETAILS = "details"
    private val JSON_LIKED = "liked"

    // Constructor only used when creating from a JSON object
    @Throws(JSONException::class)
    constructor(jo: JSONObject) {
        recipeID = jo.getInt(JSON_ID)
        imagePath = jo.getString(JSON_IMAGE_PATH)
        name = jo.getString(JSON_NAME)
        description = jo.getString(JSON_DESCRIPTION)
        details = jo.getString(JSON_DETAILS)
        liked = jo.getBoolean(JSON_LIKED)
    }

    // Used for converting the recipe to a JSON object to be saved in a file
    @Throws(JSONException::class)
    fun convertToJSON(): JSONObject {
        val jo = JSONObject()
        jo.put(JSON_ID, recipeID)
        jo.put(JSON_IMAGE_PATH, imagePath)
        jo.put(JSON_NAME, name)
        jo.put(JSON_DESCRIPTION, description)
        jo.put(JSON_DETAILS, details)
        jo.put(JSON_LIKED, liked)
        return jo
    }

    // equals() method for proper object comparison
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Recipe

        if (recipeID != other.recipeID) return false
        if (imagePath != other.imagePath) return false
        if (name != other.name) return false
        if (description != other.description) return false
        if (details != other.details) return false
        return liked == other.liked
    }

    // hashCode() method for proper hashing of the object
    override fun hashCode(): Int {
        var result = recipeID
        result = 31 * result + (imagePath?.hashCode() ?: 0)
        result = 31 * result + name.hashCode()
        result = 31 * result + description.hashCode()
        result = 31 * result + details.hashCode()
        result = 31 * result + liked.hashCode()
        return result
    }

    // toString() method for a human-readable string representation
    override fun toString(): String {
        return "Recipe(id=$recipeID, imagePath=$imagePath, name='$name', " +
                "description='$description', liked=$liked)"
    }



    //TODO convert object to format to store in files (see contacts keeper to see JSON object implementation)
    //TODO create object from the file-stored format
}
