package com.sap.codelab.view.home

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import com.sap.codelab.R

class RecordAudioPermissionRationaleDialog : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
        AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.rationale_dialog_title))
            .setMessage(getString(R.string.rationale_dialog_message))
            .setPositiveButton(getString(R.string.rationale_dialog_ok)) { _, _ ->
                setFragmentResult(REQUEST_CODE_RATIONALE, bundleOf())
            }
            .setNegativeButton(getString(R.string.rationale_dialog_cancel)) { _, _ -> dismiss() }
            .create()

    companion object {
        const val TAG = "RecordAudioPermissionRationaleDialog"
        const val REQUEST_CODE_RATIONALE = "REQUEST_CODE_RATIONALE"
    }
}