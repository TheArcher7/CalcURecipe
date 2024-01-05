package com.mmock.calcurecipe.adapter

import androidx.recyclerview.widget.RecyclerView
import com.mmock.calcurecipe.SavedListsActivity
import com.mmock.calcurecipe.model.ListFolder
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.mmock.calcurecipe.FolderContentsActivity
import com.mmock.calcurecipe.FolderManager
import com.mmock.calcurecipe.R

class FolderListAdapter(private val savedListsActivity: SavedListsActivity, private var folderList: ArrayList<ListFolder>)
    : RecyclerView.Adapter<FolderListAdapter.ListViewHolder>() {

    class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val folderImage: ImageView = itemView.findViewById(R.id.fi_folderImage)
        val folderName: TextView = itemView.findViewById(R.id.fi_folderName)
        val itemCount: TextView = itemView.findViewById(R.id.fi_items)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view = LayoutInflater.from(savedListsActivity).inflate(R.layout.folder_item, parent, false)
        return ListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val currentFolder = folderList[position]

        // TODO Set folder image (replace with your actual image loading logic)
        // For example, using a library like Picasso or Glide
        // Picasso.get().load(currentFolder.imagePath).into(holder.folderImage)

        // Set a placeholder image if needed
        holder.folderImage.setImageResource(R.drawable.ic_folder)

        // Set folder name
        holder.folderName.text = currentFolder.folderName

        // Set item count text
        val itemCountText = FolderManager.getNumRecipes(currentFolder.folderID).toString()
        holder.itemCount.text = itemCountText

        // Set click listener to switch to FolderContentsActivity
        holder.itemView.setOnClickListener {
            val intent = Intent(savedListsActivity, FolderContentsActivity::class.java)
            intent.putExtra("position", holder.adapterPosition)
            intent.putExtra("id", currentFolder.folderID)
            savedListsActivity.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return folderList.size
    }

    // Method to update the list when changes occur
    fun updateFolderList(newFolderList: ArrayList<ListFolder>) {
        folderList.clear()
        folderList.addAll(newFolderList)
        notifyDataSetChanged()
    }
}
