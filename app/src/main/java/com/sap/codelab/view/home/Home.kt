package com.sap.codelab.view.home

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.NonNull
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.sap.codelab.R
import com.sap.codelab.model.Memo
import com.sap.codelab.utils.coroutines.ScopeProvider
import com.sap.codelab.view.create.CreateMemo
import com.sap.codelab.view.detail.BUNDLE_MEMO_ID
import com.sap.codelab.view.detail.ViewMemo
import com.sap.codelab.view.home.RecordAudioPermissionRationaleDialog.Companion.REQUEST_CODE_RATIONALE
import com.sap.codelab.view.home.SettingsDialog.Companion.REQUEST_CODE_SETTINGS
import com.sap.codelab.voiceengine.VoiceCommands
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.content_home.*
import kotlinx.coroutines.launch

/**
 * The main activity of the app. Shows a list of recorded memos and lets the user add new memos.
 */
internal class Home : AppCompatActivity() {

    private lateinit var adapter: MemoAdapter
    private lateinit var model: HomeViewModel
    private lateinit var menuItemShowAll: MenuItem
    private lateinit var menuItemShowOpen: MenuItem
    private val ui = ScopeProvider.newScope()

    private val recordAudioPermission =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            when {
                isGranted -> model.launchVoiceRecognizer()
                !shouldShowRequestPermissionRationale(Manifest.permission.RECORD_AUDIO) -> model.onRecordAudioPermissionDenied()
                else -> { }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        //Setup observation of the memo list (that we'll update the adapter with once it changes)
        model = ViewModelProvider(this)[HomeViewModel::class.java]
        setSupportActionBar(toolbar)
        initRecyclerView()
        setDialogResultListeners()
        initClickListeners()
        observeViewModel(model, false)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean =
        menuInflater
            .inflate(R.menu.menu_home, menu)
            .also {
                menuItemShowAll = menu.findItem(R.id.action_show_all)
                menuItemShowOpen = menu.findItem(R.id.action_show_open)
            }
            .let { true }

    /**
     * Handles actionbar interactions.
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_show_all -> showMemos(shouldShowAll = true).let { true }
            R.id.action_show_open -> showMemos(shouldShowAll = false).let { true }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onDestroy() {
        ScopeProvider.cancel(ui)
        super.onDestroy()
    }

    private fun initRecyclerView() {
        //Setup the recycler view and the adapter
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        adapter = MemoAdapter(mutableListOf(), {
            //Implementation for when the user selects a row to show the detail view
                view ->
            val intent = Intent(this@Home, ViewMemo::class.java)
            intent.putExtra(BUNDLE_MEMO_ID, (view.tag as Memo).id)
            startActivity(intent)
        }, {
            //Implementation for when the user marks a memo as completed
                checkbox, isChecked ->
            model.updateMemo(checkbox.tag as Memo, isChecked)
        })
        recyclerView.adapter = adapter
        recyclerView.addItemDecoration(DividerItemDecoration(this,
            (recyclerView.layoutManager as LinearLayoutManager).orientation))
    }

    private fun setDialogResultListeners() {
        supportFragmentManager.setFragmentResultListener(
            REQUEST_CODE_RATIONALE, this)
        { _: String, _: Bundle -> recordAudioPermission.launch(Manifest.permission.RECORD_AUDIO) }

        supportFragmentManager.setFragmentResultListener(
            REQUEST_CODE_SETTINGS, this)
        { _: String, _: Bundle ->
            Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                data = Uri.fromParts(EXTRA_APP_PACKAGE, packageName, null)
                startActivity(this)
            }
        }
    }

    private fun initClickListeners() {
        fab.setOnClickListener {
            //Handles clicks on the FAB button > creates a new Memo
            navigateToCreateMemo()
        }

        fab.setOnLongClickListener {
            if (shouldShowRequestPermissionRationale(Manifest.permission.RECORD_AUDIO)) {
                RecordAudioPermissionRationaleDialog().show(
                    supportFragmentManager,
                    RecordAudioPermissionRationaleDialog.TAG
                )
            } else {
                recordAudioPermission.launch(Manifest.permission.RECORD_AUDIO)
            }
            true
        }
    }

    private fun observeViewModel(@NonNull viewModel: HomeViewModel, showAll: Boolean) {
        ui.launch {
            val observables = if (showAll) viewModel.getAllMemos() else viewModel.getOpenMemos()
            with(viewModel) {
                setMemos(observables, this@Home)
                observables.observe(this@Home) { memoList ->
                    if (memoList != null) adapter.setItems(memoList)
                }

                showPermissionDeniedDialogEvent.observe(this@Home) {
                    SettingsDialog().show(supportFragmentManager, SettingsDialog.TAG)
                }
                voiceCommandEvent.observe(this@Home) { command -> handleResult(command) }
                startListeningEvent.observe(this@Home) { showRecognitionProgress() }
                errorProcessingEvent.observe(this@Home) { showError() }
                progressEvent.observe(this@Home) { visible -> progress.isVisible = visible }
            }
        }
    }

    private fun handleResult(command: VoiceCommands) {
        when (command) {
            VoiceCommands.SHOW_ALL -> showMemos(shouldShowAll = true)
            VoiceCommands.SHOW_OPEN -> showMemos(shouldShowAll = false)
            VoiceCommands.CREATE_MEMO -> navigateToCreateMemo()
            VoiceCommands.UNRECOGNIZED -> showUnrecognizedCommand()
        }
    }

    private fun showMemos(shouldShowAll: Boolean) {
        observeViewModel(model, shouldShowAll)
        //Switch available menu options
        menuItemShowAll.isVisible = !shouldShowAll
        menuItemShowOpen.isVisible = shouldShowAll
    }

    private fun navigateToCreateMemo() {
        startActivity(Intent(this@Home, CreateMemo::class.java))
    }

    private fun showUnrecognizedCommand() {
        Toast.makeText(applicationContext, getString(R.string.unknown_command), Toast.LENGTH_SHORT).show()
    }

    private fun showRecognitionProgress() {
        Toast.makeText(applicationContext, getString(R.string.start_to_speak), Toast.LENGTH_SHORT).show()
    }

    private fun showError() {
        Toast.makeText(applicationContext, getString(R.string.start_to_speak), Toast.LENGTH_SHORT).show()
    }

    companion object {
        private const val EXTRA_APP_PACKAGE = "package"
    }
}