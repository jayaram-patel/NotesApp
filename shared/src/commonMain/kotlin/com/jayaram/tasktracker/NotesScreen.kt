package com.jayaram.tasktracker
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.runtime.Composable
import com.jayaram.tasktracker.model.Folder

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.jayaram.tasktracker.model.Note
import com.jayaram.tasktracker.repository.NoteRepository
import kotlinx.coroutines.launch
import androidx.compose.foundation.combinedClickable
import androidx.compose.material.icons.filled.ArrowBack

@Composable
fun NotesScreen(
    folder: Folder,
    noteRepository: NoteRepository,
    onBack: () -> Unit
){

    var noteText by remember { mutableStateOf("") }
    var editingNote by remember { mutableStateOf<Note?>(null) }

    var selectionMode by remember { mutableStateOf(false) }

    val selectedNotes = remember {
        mutableStateListOf<Note>()
    }

    val haptic = LocalHapticFeedback.current

    val notes = remember { mutableStateListOf<Note>() }

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    LaunchedEffect(folder.id) {
        notes.clear()
        notes.addAll(noteRepository.getNotes(folder.id))
    }

    MaterialTheme {

        Scaffold(
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

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(16.dp)
            ) {

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    IconButton(
                        onClick = {

                            haptic.performHapticFeedback(
                                HapticFeedbackType.TextHandleMove
                            )

                            if (selectionMode) {
                                selectionMode = false
                                selectedNotes.clear()
                            } else {
                                onBack()
                            }

                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }

                    if (selectionMode) {

                        Text(
                            text = "${selectedNotes.size} Selected",
                            style = MaterialTheme.typography.titleSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = folder.name,
                    style = MaterialTheme.typography.headlineLarge
                )

                Spacer(modifier = Modifier.height(16.dp))

                Spacer(modifier = Modifier.height(12.dp))

                TextField(
                    value = noteText,
                    onValueChange = {
                        noteText = it
                    },
                    modifier = Modifier.fillMaxWidth(),
                    label = {
                        Text("Enter a note")
                    }
                )

                Spacer(modifier = Modifier.height(12.dp))

                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {

                        haptic.performHapticFeedback(
                            HapticFeedbackType.LongPress
                        )

                        if (noteText.isBlank()) {

                            scope.launch {

                                snackbarHostState.currentSnackbarData?.dismiss()

                                snackbarHostState.showSnackbar(
                                    message = "Note cannot be empty",
                                    duration = SnackbarDuration.Short
                                )
                            }

                            return@Button
                        }

                        if (editingNote == null) {

                            noteRepository.addNote(
                                folder.id,
                                noteText
                            )

                        } else {

                            noteRepository.updateNote(
                                editingNote!!.copy(
                                    text = noteText
                                )
                            )

                            editingNote = null
                        }

                        notes.clear()
                        notes.addAll(
                            noteRepository.getNotes(folder.id)
                        )

                        noteText = ""
                    }
                ) {
                    Text(
                        if (editingNote == null)
                            "Add Note"
                        else
                            "Save Changes"
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                LazyColumn {

                    items(notes) { note ->

                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 6.dp)
                        ) {

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .combinedClickable(

                                        onClick = {

                                            if (selectionMode) {

                                                if (selectedNotes.contains(note)) {

                                                    selectedNotes.remove(note)

                                                    if (selectedNotes.isEmpty()) {
                                                        selectionMode = false
                                                    }

                                                } else {

                                                    selectedNotes.add(note)
                                                }

                                            }

                                        },

                                        onLongClick = {

                                            haptic.performHapticFeedback(
                                                HapticFeedbackType.LongPress
                                            )

                                            selectionMode = true

                                            if (!selectedNotes.contains(note)) {
                                                selectedNotes.add(note)
                                            }

                                        }

                                    )
                                    .padding(16.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {

                                Row(
                                    modifier = Modifier.weight(1f),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {

                                    if (selectionMode) {

                                        Checkbox(
                                            checked = selectedNotes.contains(note),
                                            onCheckedChange = null
                                        )

                                        Spacer(modifier = Modifier.width(8.dp))
                                    }

                                    Text(
                                        text = note.text
                                    )
                                }

                                if (!selectionMode) {

                                    IconButton(
                                    onClick = {
                                        haptic.performHapticFeedback(
                                            HapticFeedbackType.LongPress
                                        )
                                        noteText = note.text
                                        editingNote = note
                                    }
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Edit,
                                        contentDescription = "Edit Note"
                                    )
                                }
                                }

                                IconButton(
                                    onClick = {

                                        haptic.performHapticFeedback(
                                            HapticFeedbackType.LongPress
                                        )

                                        val deletedNote = note

                                        noteRepository.deleteNote(deletedNote.id)

                                        notes.clear()
                                        notes.addAll(
                                            noteRepository.getNotes(folder.id)
                                        )

                                        scope.launch {

                                            val result =
                                                snackbarHostState.showSnackbar(
                                                    message = "Note Deleted",
                                                    actionLabel = "UNDO",
                                                    duration = SnackbarDuration.Short
                                                )

                                            if (result == SnackbarResult.ActionPerformed) {

                                                noteRepository.addNote(
                                                    folder.id,
                                                    deletedNote.text
                                                )

                                                notes.clear()
                                                notes.addAll(
                                                    noteRepository.getNotes(folder.id)
                                                )
                                            }
                                        }
                                    }
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Delete,
                                        contentDescription = "Delete Note",
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