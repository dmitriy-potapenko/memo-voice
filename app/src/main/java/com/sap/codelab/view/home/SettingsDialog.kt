package com.sap.codelab.view.home

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import com.sap.codelab.R

class SettingsDialog : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
        AlertDialog.Builder(requireContext())
            .setMessage(getString(R.string.settings_dialog_message))
            .setPositiveButton(getString(R.string.settings_dialog_ok_button)) { _, _ ->
                setFragmentResult(REQUEST_CODE_SETTINGS, bundleOf())
            }
            .setNegativeButton(getString(R.string.settings_dialog_cancel_button)) { _, _ -> dismiss() }
            .create()

    companion object {
        const val TAG = "SettingsDialog"
        const val REQUEST_CODE_SETTINGS = "REQUEST_CODE_SETTINGS"
    }
}