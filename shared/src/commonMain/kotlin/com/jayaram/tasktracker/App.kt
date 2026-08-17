package com.jayaram.tasktracker

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import kotlinx.coroutines.launch

import com.jayaram.tasktracker.model.Folder
import com.jayaram.tasktracker.repository.FolderRepository

//for haptics
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import com.jayaram.tasktracker.repository.NoteRepository

//for hiding keyboard
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.runtime.mutableLongStateOf
import com.jayaram.tasktracker.repository.ContactRepository

import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun App(
    folderRepository: FolderRepository,
    noteRepository: NoteRepository,
    contactRepository: ContactRepository
) {
    val haptic = LocalHapticFeedback.current
    var submissionCount by remember { mutableLongStateOf(0L) }

    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    var selectedFolder by remember { mutableStateOf<Folder?>(null) }

    var browserUrl by remember { mutableStateOf<String?>(null) }
    var browserNoteData by remember { mutableStateOf<String?>(null) }

    var folderName by remember { mutableStateOf("") }

    var editingFolder by remember { mutableStateOf<Folder?>(null) }

    val folders = remember { mutableStateListOf<Folder>() }

    val listState = rememberLazyListState()

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    var isRefreshing by remember { mutableStateOf(false) }
    val refreshData = {
        scope.launch {
            isRefreshing = true
            folders.clear()
            folders.addAll(folderRepository.getFolders())
            submissionCount = contactRepository.getSubmissionCount()
            isRefreshing = false
        }
    }

    LaunchedEffect(listState.isScrollInProgress) {
        if (listState.isScrollInProgress) {
            focusManager.clearFocus()
            keyboardController?.hide()
        }
    }

    LaunchedEffect(Unit) {
        folders.clear()
        folders.addAll(folderRepository.getFolders())

        submissionCount = contactRepository.getSubmissionCount()
    }

    MaterialTheme {
        //if browser open, back button closes it
        BackHandler(enabled = browserUrl != null) {
            browserUrl = null
        }

        //if folder selected, back button redirects to all folders page
        BackHandler(enabled = selectedFolder != null && browserUrl == null) {
            selectedFolder = null
        }

        if (browserUrl != null) {
            BrowserScreen(
                initialUrl = browserUrl!!,
                noteData = browserNoteData,
                onBack = {
                    browserUrl = null
                    browserNoteData = null
                }
            )
            return@MaterialTheme
        }

        if (selectedFolder != null) {
            NotesScreen(
                folder = selectedFolder!!,
                noteRepository = noteRepository,
                onBack = {
                    selectedFolder = null
                },
                openBrowser = { url, note ->
                    browserUrl = url
                    browserNoteData = note
                }
            )
            return@MaterialTheme
        }

        Scaffold(
            contentWindowInsets = WindowInsets(0),
            snackbarHost = {
                SnackbarHost(
                    hostState = snackbarHostState
                ) { snackbarData ->
                    Snackbar(
                        snackbarData = snackbarData,
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                        actionColor = Color.Red
                    )
                }
            }
        ) { innerPadding ->
            PullToRefreshBox(
                isRefreshing = isRefreshing,
                onRefresh = { refreshData() },
                modifier = Modifier.padding(innerPadding)
            ) {
                LazyColumn(
                    state = listState,
                    modifier = Modifier.fillMaxSize()
                ) {
                    item {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Spacer(modifier = Modifier.height(12.dp))
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Edit,
                                    contentDescription = null,
                                    modifier = Modifier.size(42.dp)
                                )

                                Text(
                                    text = "My Notes",
                                    style = MaterialTheme.typography.headlineLarge
                                )
                            }
                            Spacer(modifier = Modifier.height(16.dp))
                        }
                    }

                    item {
                        Box(modifier = Modifier.padding(horizontal = 16.dp)) {
                            Card(
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(
                                    modifier = Modifier.padding(16.dp)
                                ) {
                                    Text(
                                        text = "📨 Contact Form",
                                        style = MaterialTheme.typography.titleMedium
                                    )
                                    Spacer(modifier = Modifier.height(6.dp))

                                    Text(
                                        text = "Total Submissions: $submissionCount",
                                        style = MaterialTheme.typography.bodyLarge
                                    )
                                }
                            }
                        }
                        Spacer(modifier = Modifier.height(20.dp))
                    }

                    item {
                        Text(
                            text = "Folder Creation",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(horizontal = 16.dp)
                        )
                    }

                    item {
                        Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                            TextField(
                                value = folderName,
                                onValueChange = {
                                    folderName = it
                                },
                                modifier = Modifier.fillMaxWidth(),
                                label = {
                                    Text("Folder name")
                                }
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            Button(
                                modifier = Modifier.fillMaxWidth(),
                                onClick = {
                                    haptic.performHapticFeedback(
                                        HapticFeedbackType.LongPress
                                    )

                                    if (folderName.isBlank()) {
                                        scope.launch {
                                            snackbarHostState.currentSnackbarData?.dismiss()

                                            snackbarHostState.showSnackbar(
                                                message = "Enter folder name first",
                                                duration = SnackbarDuration.Short
                                            )
                                        }
                                        return@Button
                                    }

                                    if (editingFolder == null) {
                                        folderRepository.addFolder(folderName)
                                    } else {
                                        folderRepository.updateFolder(
                                            editingFolder!!.copy(
                                                name = folderName
                                            )
                                        )
                                        editingFolder = null
                                    }

                                    folders.clear()
                                    folders.addAll(folderRepository.getFolders())
                                    folderName = ""
                                    focusManager.clearFocus()
                                    keyboardController?.hide()
                                }
                            ) {
                                Text(
                                    if (editingFolder == null)
                                        "Create Folder"
                                    else
                                        "Save Changes"
                                )
                            }

                            Spacer(modifier = Modifier.height(30.dp))
                            Text(
                                text = "All Folders",
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                        }
                    }

                    items(folders) { folder ->
                        Box(modifier = Modifier.padding(horizontal = 16.dp)) {
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 6.dp)
                                    .clickable {
                                        haptic.performHapticFeedback(
                                            HapticFeedbackType.TextHandleMove
                                        )
                                        selectedFolder = folder
                                    }
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "📁 ${folder.name}",
                                        modifier = Modifier.weight(1f)
                                    )

                                    IconButton(
                                        onClick = {
                                            folderName = folder.name
                                            editingFolder = folder
                                        }
                                    ) {
                                        Icon(
                                            Icons.Default.Edit,
                                            contentDescription = null
                                        )
                                    }

                                    IconButton(
                                        onClick = {
                                            haptic.performHapticFeedback(
                                                HapticFeedbackType.LongPress
                                            )

                                            val deletedFolder = folder

                                            folderRepository.deleteFolder(deletedFolder.id)

                                            folders.clear()
                                            folders.addAll(folderRepository.getFolders())

                                            scope.launch {
                                                val result = snackbarHostState.showSnackbar(
                                                    message = "Folder Deleted",
                                                    actionLabel = "UNDO",
                                                    duration = SnackbarDuration.Short
                                                )

                                                if (result == SnackbarResult.ActionPerformed) {
                                                    folderRepository.addFolder(
                                                        deletedFolder.name
                                                    )

                                                    folders.clear()
                                                    folders.addAll(folderRepository.getFolders())
                                                }
                                            }
                                        }
                                    ) {
                                        Icon(
                                            Icons.Default.Delete,
                                            contentDescription = null,
                                            tint = Color.Red
                                        )
                                    }
                                }
                            }
                        }
                    }
                    
                    item {
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }
            }
        }
    }
}
