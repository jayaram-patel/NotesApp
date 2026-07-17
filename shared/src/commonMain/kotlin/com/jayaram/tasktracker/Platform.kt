package com.jayaram.tasktracker

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform