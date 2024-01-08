package com.mmock.calcurecipe.model

import org.json.JSONException
import org.json.JSONObject

class ListFolder {
    var folderName: String = ""
    var folderID: Int = -1

    constructor(name: String, id: Int){
        folderName = name
        folderID = id
    }


    //JSON properties
    private val JSON_NAME = "folderName"
    private val JSON_ID = "folderId"
    //constructor only used when creating from a JSON object
    @Throws(JSONException::class)
    constructor(jo: JSONObject) {
        folderName = jo.getString(JSON_NAME)
        folderID = jo.getInt(JSON_ID)
    }
    //used for converting the note to a JSON object to be saved in file
    @Throws(JSONException::class)
    fun convertToJSON(): JSONObject {
        val jo = JSONObject()
        jo.put(JSON_NAME, folderName)
        jo.put(JSON_ID, folderID)
        return jo
    }

    override fun toString(): String {
        return "ListFolder(folderName='$folderName', folderID=$folderID)"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ListFolder

        if (folderName != other.folderName) return false
        return folderID == other.folderID
    }

    override fun hashCode(): Int {
        var result = folderName.hashCode()
        result = 31 * result + folderID
        return result
    }
}