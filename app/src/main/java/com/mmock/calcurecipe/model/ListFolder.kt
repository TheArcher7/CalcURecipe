package com.mmock.calcurecipe.model

class ListFolder {
    var folderName: String = ""
    var folderID: Int = -1

    constructor(name: String, id: Int){
        folderName = name
        folderID = id
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