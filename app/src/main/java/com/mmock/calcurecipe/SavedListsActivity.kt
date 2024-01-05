package com.mmock.calcurecipe
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.mmock.calcurecipe.adapter.FolderListAdapter


class SavedListsActivity : OptionsMenuActivity() {
    private lateinit var toolbar: androidx.appcompat.widget.Toolbar
    private lateinit var folderListAdapter: FolderListAdapter
    private lateinit var folderRecycler: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_saved_lists)

        // Get UI elements
        toolbar = findViewById(R.id.asl_toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.title = "Saved Lists"

        // Set recycler
        folderRecycler = findViewById(R.id.asl_recyclerViewFolders)
        folderRecycler.layoutManager = LinearLayoutManager(applicationContext)
        folderRecycler.itemAnimator = DefaultItemAnimator()

        // Set adapter
        folderListAdapter = FolderListAdapter(this, FolderManager.sortFolders())
        folderRecycler.adapter = folderListAdapter

        // Set up the Floating Action Button click listener
        findViewById<FloatingActionButton>(R.id.asl_fabAddFolder).setOnClickListener {
            Toast.makeText(this, "Add Folder Clicked", Toast.LENGTH_SHORT).show()
            addNewFolder()
            updateFolderList()
        }
    }

    private fun addNewFolder(){
        // TODO: Handle the click event for adding a folder
    }

    private fun updateFolderList() {
        // Sort folders and update the RecyclerView
        folderListAdapter.updateFolderList(FolderManager.sortFolders())
    }
}
