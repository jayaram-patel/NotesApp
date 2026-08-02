package com.jayaram.tasktracker.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

private val urlRegex =
    Regex("(https?://[^\\s]+)")

@Composable
fun NoteContent(
    note: String,
    onLinkClick: (String) -> Unit
) {

    val match = urlRegex.find(note)

    if (match == null) {
        Text(text = note)
        return
    }

    val url = match.value

    val textWithoutUrl = note.replace(url, "").trim()

    Column {

        if (textWithoutUrl.isNotEmpty()) {
            Text(text = textWithoutUrl)
        }

        val displayName = when {
            url.contains("youtube.com") || url.contains("youtu.be") -> "▶ YouTube"
            url.contains("github.com") -> "🐙 GitHub"
            url.contains("developer.android.com") -> "📖 Android Developers"
            url.contains("kotlinlang.org") -> "🟣 Kotlin"
            url.contains("stackoverflow.com") -> "🟠 Stack Overflow"
            else -> {
                url.removePrefix("https://")
                    .removePrefix("http://")
                    .substringBefore("/")
                    .removePrefix("www.")
            }
        }

        Text(
            text = displayName,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.clickable {
                onLinkClick(url)
            }
        )
    }
}