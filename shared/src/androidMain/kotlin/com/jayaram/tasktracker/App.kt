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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color

import com.jayaram.tasktracker.database.DatabaseDriverFactory
import com.jayaram.tasktracker.model.Folder
import com.jayaram.tasktracker.repository.FolderRepository

import androidx.compose.foundation.Image
import androidx.compose.ui.res.painterResource
import tasktracker.shared.generated.resources.Res
import org.jetbrains.compose.resources.painterResource
import tasktracker.shared.generated.resources.app_logo

import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
@Composable
fun App() {

    val haptic = LocalHapticFeedback.current

    val context = LocalContext.current

    val repository = remember {
        FolderRepository(
            DatabaseDriverFactory(context)
        )
    }

    var selectedFolder by remember {
        mutableStateOf<Folder?>(null)
    }

    var folderName by remember {
        mutableStateOf("")
    }

    var editingFolder by remember {
        mutableStateOf<Folder?>(null)
    }

    val folders = remember {
        mutableStateListOf<Folder>()
    }

    LaunchedEffect(Unit) {
        folders.clear()
        folders.addAll(repository.getFolders())
    }

    MaterialTheme {
        if (selectedFolder != null) {

            NotesScreen(
                folder = selectedFolder!!,
                onBack = {
                    selectedFolder = null
                }
            )

            return@MaterialTheme
        }

        Scaffold { innerPadding ->

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .safeDrawingPadding()
                    .padding(innerPadding)
                    .padding(16.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = null,
                        modifier = Modifier.size(42.dp)
                    )

                    Spacer(modifier = Modifier.width(12.dp))

                    Text(
                        text = "My Notes",
                        style = MaterialTheme.typography.headlineLarge
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                Spacer(modifier = Modifier.height(20.dp))

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

                        if (folderName.isNotBlank()) {

                            if (editingFolder == null) {

                                repository.addFolder(folderName)

                            } else {

                                repository.updateFolder(
                                    editingFolder!!.copy(
                                        name = folderName
                                    )
                                )

                                editingFolder = null
                            }

                            folders.clear()
                            folders.addAll(repository.getFolders())

                            folderName = ""
                        }

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

                LazyColumn {

                    items(folders) { folder ->

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

                                        repository.deleteFolder(folder.id)

                                        folders.clear()
                                        folders.addAll(repository.getFolders())

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
            }
        }
    }
}