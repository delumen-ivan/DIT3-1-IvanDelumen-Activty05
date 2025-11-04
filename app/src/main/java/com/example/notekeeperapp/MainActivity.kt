package com.example.notekeeperapp

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var fab: FloatingActionButton
    private lateinit var databaseHelper: DatabaseHelper
    private lateinit var noteAdapter: NoteAdapter
    private var notesList = mutableListOf<Note>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize views
        recyclerView = findViewById(R.id.recyclerView)
        fab = findViewById(R.id.fab)

        // Initialize database
        databaseHelper = DatabaseHelper(this)

        // Setup RecyclerView
        setupRecyclerView()

        // Load notes from database
        loadNotes()

        // FAB click listener - Add new note
        fab.setOnClickListener {
            val intent = Intent(this, AddEditNoteActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        // Reload notes when returning to this activity
        loadNotes()
    }

    private fun setupRecyclerView() {
        recyclerView.layoutManager = LinearLayoutManager(this)
        noteAdapter = NoteAdapter(
            notesList,
            onNoteClick = { note ->
                // Edit note
                val intent = Intent(this, AddEditNoteActivity::class.java)
                intent.putExtra("NOTE_ID", note.id)
                intent.putExtra("NOTE_TITLE", note.title)
                intent.putExtra("NOTE_CONTENT", note.content)
                startActivity(intent)
            },
            onNoteLongClick = { note ->
                // Delete note
                showDeleteDialog(note)
            }
        )
        recyclerView.adapter = noteAdapter
    }

    private fun loadNotes() {
        notesList.clear()
        notesList.addAll(databaseHelper.getAllNotes())
        noteAdapter.notifyDataSetChanged()

        // Show empty state message if no notes
        if (notesList.isEmpty()) {
            Toast.makeText(this, "No notes yet. Tap + to add one!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun showDeleteDialog(note: Note) {
        AlertDialog.Builder(this)
            .setTitle("Delete Note")
            .setMessage("Are you sure you want to delete '${note.title}'?")
            .setPositiveButton("Delete") { _, _ ->
                deleteNote(note)
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun deleteNote(note: Note) {
        val result = databaseHelper.deleteNote(note.id)
        if (result > 0) {
            Toast.makeText(this, "Note deleted successfully", Toast.LENGTH_SHORT).show()
            loadNotes()
        } else {
            Toast.makeText(this, "Failed to delete note", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        databaseHelper.close()
    }
}