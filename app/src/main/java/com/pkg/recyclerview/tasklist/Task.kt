package com.pkg.recyclerview.tasklist

import java.io.Serializable

data class Task(val id : String, val title: String, val description: String = "No description") : Serializable