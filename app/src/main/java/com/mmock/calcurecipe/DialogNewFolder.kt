package com.mmock.calcurecipe

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.DialogFragment

class DialogNewFolder : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireActivity())
        val inflater = requireActivity().layoutInflater
        val dialogView = inflater.inflate(R.layout.dialog_new_folder, null)

        val folderName = dialogView.findViewById<EditText>(R.id.dnf_folderNameEditText)
        val btnCancel = dialogView.findViewById<Button>(R.id.dnf_cancelButton)
        val btnSave = dialogView.findViewById<Button>(R.id.dnf_saveButton)

        builder.setView(dialogView).setMessage("Create New Folder")

        //handle button clicks
        btnCancel.setOnClickListener {
            dismiss()
        }

        btnSave.setOnClickListener {
            val newFolderName = folderName.text.toString()

            if(newFolderName.isNotEmpty()) {
                val newFolderID = FolderManager.addFolder(newFolderName)
                dismiss()
            }
            Log.d("DialogNewFolder", "Cannot create folder with empty string.")
        }

        return builder.create()
    }
}