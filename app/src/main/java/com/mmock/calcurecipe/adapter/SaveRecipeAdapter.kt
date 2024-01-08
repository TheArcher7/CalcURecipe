package com.mmock.calcurecipe.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.recyclerview.widget.RecyclerView
import com.mmock.calcurecipe.FolderManager
import com.mmock.calcurecipe.R
import com.mmock.calcurecipe.SaveRecipeActivity
import com.mmock.calcurecipe.model.ListFolder

class SaveRecipeAdapter(private val saveRecipeActivity: SaveRecipeActivity, private var folderList: ArrayList<ListFolder>, private var recipeID: Int)
    : RecyclerView.Adapter<SaveRecipeAdapter.ListViewHolder>()
    , FolderManager.FolderListener  {

    class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val folderCheckBox = itemView.findViewById<CheckBox>(R.id.checkBox)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view = LayoutInflater.from(saveRecipeActivity).inflate(R.layout.checkbox_folder_item, parent, false)
        return ListViewHolder(view)
    }

    override fun getItemCount(): Int {
        return folderList.size
    }

    override fun onBindViewHolder(holder: SaveRecipeAdapter.ListViewHolder, position: Int) {
        val currentFolder = folderList[position]

        val folderID = currentFolder.folderID

        val recipeIsInFolder = FolderManager.isRecipeInFolder(recipeID, folderID)

        holder.folderCheckBox.text = currentFolder.folderName
        holder.folderCheckBox.isChecked = recipeIsInFolder

        // Toggle the folderCheckBox
        holder.folderCheckBox.setOnClickListener {
            val isChecked = holder.folderCheckBox.isChecked

            if (isChecked) {
                // Add mapping when checked
                FolderManager.addMapping(folderID, recipeID)
            } else {
                // Remove mapping when unchecked
                FolderManager.removeMapping(folderID, recipeID)
            }
        }
    }

    override fun onFolderUpdated() {
        notifyDataSetChanged()
    }
    override fun onFolderAdded(position: Int) {
        notifyItemInserted(position)
    }

    override fun onFolderDeleted(position: Int) {
        //not necessary, but is here anyway
        notifyItemRemoved(position)
    }

}