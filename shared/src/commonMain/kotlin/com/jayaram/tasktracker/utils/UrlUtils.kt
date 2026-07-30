package com.jayaram.tasktracker.utils

private val urlRegex = Regex(
    "(https?://[\\w\\-._~:/?#\\[\\]@!$&'()*+,;=%]+)"
)

fun extractUrls(text: String): List<String> {
    return urlRegex.findAll(text)
        .map { it.value }
        .toList()
}