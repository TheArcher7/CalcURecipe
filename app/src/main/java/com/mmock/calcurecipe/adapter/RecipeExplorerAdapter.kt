package com.mmock.calcurecipe.adapter


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mmock.calcurecipe.MainActivity
import com.mmock.calcurecipe.R
import com.mmock.calcurecipe.RecipeManager
import com.mmock.calcurecipe.model.Recipe

class RecipeExplorerAdapter(private val mainActivity: MainActivity, private var recipeList: ArrayList<Recipe>)
    : RecyclerView.Adapter<RecipeExplorerAdapter.RecipeViewHolder>()
    , RecipeManager.RecipeListener{

        inner class RecipeViewHolder(recipeView: View) : RecyclerView.ViewHolder(recipeView), View.OnClickListener {
            //variables of the item being displayed by the recyclerView
            //these should all match the variables in the Recipe class with the corresponding UI
            internal var name = recipeView.findViewById<View>(R.id.recipeExplorerItemName) as TextView
            internal var image = recipeView.findViewById<View>(R.id.recipeExplorerItemImage) as ImageView
            internal var descr = recipeView.findViewById<View>(R.id.recipeExplorerItemDescription) as TextView

            init {
                //setting the
                //defining behavior for items
                recipeView.isClickable = true
                recipeView.setOnClickListener(this)
            }

            override fun onClick(v: View?) {
                //clicking on the item in the recyclerView should switch activities to the recipe details
                mainActivity.showRecipe(adapterPosition)
            }

            //other methods for behavior of items
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.recipe_explorer_item, parent, false)

        return RecipeViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return recipeList.size
    }

    override fun onBindViewHolder(holder: RecipeViewHolder, position: Int) {
        val recipe = recipeList[position]

        //set variables
        //set description and title text
        holder.name.text = recipe.name
        holder.descr.text = recipe.description

        //set image
        /*Glide.with(mainActivity)
            .load(recipe.imagePath)
            .apply(RequestOptions().centerCrop())
            .into(holder.image) */

        //optional TODO define behavior onClick for description to expand (see Gallery Display App 2 for example)
    }

    override fun onRecipeAdded(position: Int) {
        // Notify the adapter that a new item has been added
        notifyItemInserted(position)
    }

    override fun onRecipeDeleted(position: Int) {
        // Notify the adapter that an item has been removed
        notifyItemRemoved(position)
    }

}