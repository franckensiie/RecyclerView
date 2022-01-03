package com.pkg.recyclerview

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageButton
import com.pkg.recyclerview.model.Task
import java.util.*

class FormActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_form)

        val taskToEdit = intent.getSerializableExtra("taskToEdit") as? Task;
        if (taskToEdit != null) {
            findViewById<EditText>(R.id.editTextTitle).setText(taskToEdit.title)
            findViewById<EditText>(R.id.editTextDesc).setText(taskToEdit.description)
        }

        val buttonInsert = findViewById<ImageButton>(R.id.buttonInsert);
        buttonInsert.setOnClickListener {
            val title = findViewById<EditText>(R.id.editTextTitle).text.toString();
            val desc = findViewById<EditText>(R.id.editTextDesc).text.toString();
            val newTask = Task(taskToEdit?.id ?: UUID.randomUUID().toString(), title, desc);
            intent.putExtra("task", newTask);
            setResult(RESULT_OK, intent)
            finish()
        }

    }

}