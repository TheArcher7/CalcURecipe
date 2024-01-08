package com.mmock.calcurecipe.model

import org.json.JSONException
import org.json.JSONObject

data class RecipeFolderMapping(val recipeID: Int, val folderID: Int) {

    // JSON properties
    companion object {
        private const val JSON_RECIPE_ID = "recipeID"
        private const val JSON_FOLDER_ID = "folderID"
    }

    // Constructor only used when creating from a JSON object
    @Throws(JSONException::class)
    constructor(jo: JSONObject) : this(
        jo.getInt(JSON_RECIPE_ID),
        jo.getInt(JSON_FOLDER_ID)
    )

    // Used for converting the mapping to a JSON object to be saved in a file
    @Throws(JSONException::class)
    fun convertToJSON(): JSONObject {
        val jo = JSONObject()
        jo.put(JSON_RECIPE_ID, recipeID)
        jo.put(JSON_FOLDER_ID, folderID)
        return jo
    }
}
