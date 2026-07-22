package com.jayaram.tasktracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.jayaram.tasktracker.repository.FolderRepository
import com.jayaram.tasktracker.repository.NoteRepository

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        enableEdgeToEdge()

        super.onCreate(savedInstanceState)

        val database = DatabaseModule.provideDatabase(this)

        val folderRepository = FolderRepository(database)

        val noteRepository = NoteRepository(database)

        setContent {
            App(
                folderRepository = folderRepository,
                noteRepository = noteRepository
            )
        }
    }
}

@Preview
@Composable
fun AppAndroidPreview() {
    // Preview won't work with a real database
}