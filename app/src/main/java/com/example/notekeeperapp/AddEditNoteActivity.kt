package com.example.notekeeperapp

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class AddEditNoteActivity : AppCompatActivity() {

    private lateinit var etTitle: EditText
    private lateinit var etContent: EditText
    private lateinit var btnSave: Button
    private lateinit var btnCancel: Button
    private lateinit var databaseHelper: DatabaseHelper

    private var noteId: Int = -1
    private var isEditMode = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_edit_note)

        // Initialize views
        etTitle = findViewById(R.id.etTitle)
        etContent = findViewById(R.id.etContent)
        btnSave = findViewById(R.id.btnSave)
        btnCancel = findViewById(R.id.btnCancel)

        // Initialize database
        databaseHelper = DatabaseHelper(this)

        // Check if editing existing note
        noteId = intent.getIntExtra("NOTE_ID", -1)
        if (noteId != -1) {
            isEditMode = true
            supportActionBar?.title = "Edit Note"

            // Load existing note data
            val title = intent.getStringExtra("NOTE_TITLE") ?: ""
            val content = intent.getStringExtra("NOTE_CONTENT") ?: ""

            etTitle.setText(title)
            etContent.setText(content)
        } else {
            supportActionBar?.title = "Add Note"
        }

        // Save button click listener
        btnSave.setOnClickListener {
            saveNote()
        }

        // Cancel button click listener
        btnCancel.setOnClickListener {
            finish()
        }
    }

    private fun saveNote() {
        val title = etTitle.text.toString().trim()
        val content = etContent.text.toString().trim()

        // Validation
        if (title.isEmpty()) {
            etTitle.error = "Title is required"
            etTitle.requestFocus()
            return
        }

        if (content.isEmpty()) {
            etContent.error = "Content is required"
            etContent.requestFocus()
            return
        }

        val timestamp = System.currentTimeMillis()

        if (isEditMode) {
            // Update existing note
            val note = Note(noteId, title, content, timestamp)
            val result = databaseHelper.updateNote(note)

            if (result > 0) {
                Toast.makeText(this, "Note updated successfully", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this, "Failed to update note", Toast.LENGTH_SHORT).show()
            }
        } else {
            // Create new note
            val note = Note(0, title, content, timestamp)
            val result = databaseHelper.insertNote(note)

            if (result != -1L) {
                Toast.makeText(this, "Note saved successfully", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this, "Failed to save note", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        databaseHelper.close()
    }
}