package com.mmock.calcurecipe.model

class Recipe {
    var recipeID: Int = -1 // the saved lists hold a reference to the Recipe id

    // data for the image
    var imagePath: String? = null
    var imageName: String? = null

    lateinit var name: String
    lateinit var description: String
    lateinit var details: String

    var liked: Boolean = false

    constructor(imagePath: String?, imageName: String?) {
        this.imagePath = imagePath
        this.imageName = imageName
    }

    constructor(name: String, description: String, steps: String) {
        this.name = name
        this.description = description
        details = steps
    }

    constructor() {}

    // equals() method for proper object comparison
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Recipe

        if (recipeID != other.recipeID) return false
        if (imagePath != other.imagePath) return false
        if (imageName != other.imageName) return false
        if (name != other.name) return false
        if (description != other.description) return false
        if (details != other.details) return false
        return liked == other.liked
    }

    // hashCode() method for proper hashing of the object
    override fun hashCode(): Int {
        var result = recipeID
        result = 31 * result + (imagePath?.hashCode() ?: 0)
        result = 31 * result + (imageName?.hashCode() ?: 0)
        result = 31 * result + name.hashCode()
        result = 31 * result + description.hashCode()
        result = 31 * result + details.hashCode()
        result = 31 * result + liked.hashCode()
        return result
    }

    // toString() method for a human-readable string representation
    override fun toString(): String {
        return "Recipe(id=$recipeID, imagePath=$imagePath, imageName=$imageName, name='$name', " +
                "description='$description', details='$details', liked=$liked)"
    }



    //TODO convert object to format to store in files (see contacts keeper to see JSON object implementation)
    //TODO create object from the file-stored format
}
