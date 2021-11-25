package com.pkg.recyclerview

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.pkg.recyclerview.tasklist.TaskListFragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}