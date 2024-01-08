package com.mmock.calcurecipe

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.DialogFragment
import com.mmock.calcurecipe.model.ListFolder

class DialogEditFolder : DialogFragment() {
    private var folder: ListFolder? = null
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireActivity())
        val inflater = requireActivity().layoutInflater
        val dialogView = inflater.inflate(R.layout.dialog_edit_folder, null)

        val folderName = dialogView.findViewById<EditText>(R.id.def_folderNameEditText)
        val btnDelete = dialogView.findViewById<Button>(R.id.def_deleteButton)
        val btnCancel = dialogView.findViewById<Button>(R.id.def_cancelButton)
        val btnSave = dialogView.findViewById<Button>(R.id.def_saveButton)

        folderName.setText(folder!!.folderName)

        builder.setView(dialogView).setMessage("Edit Folder")

        btnCancel.setOnClickListener {
            dismiss()
        }

        btnDelete.setOnClickListener {
            FolderManager.deleteFolder(folder!!.folderID)
            dismiss()
        }

        btnSave.setOnClickListener {
            FolderManager.editFolder(
                folder!!.folderID,
                folderName.text.toString()
            )
            dismiss()
        }

        Log.d("DialogEditFolder", "Dialog for editing folder created.")
        return builder.create()
    }

    fun sendFolderSelected(folderSelected: ListFolder) {
        folder = folderSelected
    }
}