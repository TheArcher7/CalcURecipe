package com.mmock.calcurecipe.adapter


import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mmock.calcurecipe.DetailsRecipeActivity
import com.mmock.calcurecipe.FolderContentsActivity
import com.mmock.calcurecipe.R
import com.mmock.calcurecipe.model.Recipe

class RecipeCondensedAdapter(private val context: Context, private val recipeList: ArrayList<Recipe>) :
    RecyclerView.Adapter<RecipeCondensedAdapter.RecipeViewHolder>() {

    class RecipeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val recipeName: TextView = itemView.findViewById(R.id.rsi_recipe_name)
        val recipeImage: ImageView = itemView.findViewById(R.id.rsi_image)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeViewHolder {
        val view =
            LayoutInflater.from(context).inflate(R.layout.recipe_search_item, parent, false)
        return RecipeViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecipeViewHolder, position: Int) {
        val currentRecipe = recipeList[position]

        // Set the recipe name
        holder.recipeName.text = currentRecipe.name

        // Set the recipe image (replace with your actual image loading logic)
        // For example, using a library like Picasso or Glide
        // Picasso.get().load(currentRecipe.imagePath).into(holder.recipeImage)
        displayImage(currentRecipe.imagePath, holder.recipeImage)

        // Set a placeholder image if needed
        holder.recipeImage.setImageResource(R.drawable.default_recipe_image_pancakes)

        // Set click listener to switch to DetailsRecipeActivity
        holder.itemView.setOnClickListener {
            val intent = Intent(context, DetailsRecipeActivity::class.java)
            intent.putExtra("recipe_loc", holder.adapterPosition)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return recipeList.size
    }


    private fun displayImage(path: String?, recipeImage: ImageView){
        if (path == "") {
            //use default image
            recipeImage.setImageResource(R.drawable.default_recipe_image_pancakes)
            return
        }
        Glide.with(context)
            .load(path)
            .into(recipeImage)
    }

}
