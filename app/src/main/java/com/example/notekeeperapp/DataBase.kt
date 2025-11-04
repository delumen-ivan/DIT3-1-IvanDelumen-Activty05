package com.example.notekeeperapp

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "NotesDatabase.db"
        private const val TABLE_NOTES = "notes"

        // Column names
        private const val KEY_ID = "id"
        private const val KEY_TITLE = "title"
        private const val KEY_CONTENT = "content"
        private const val KEY_TIMESTAMP = "timestamp"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        // Create notes table
        val createNotesTable = ("CREATE TABLE $TABLE_NOTES ("
                + "$KEY_ID INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "$KEY_TITLE TEXT,"
                + "$KEY_CONTENT TEXT,"
                + "$KEY_TIMESTAMP INTEGER)")
        db?.execSQL(createNotesTable)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        // Drop older table if exists
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_NOTES")
        // Create tables again
        onCreate(db)
    }

    // CREATE - Insert a new note
    fun insertNote(note: Note): Long {
        val db = this.writableDatabase

        val values = ContentValues()
        values.put(KEY_TITLE, note.title)
        values.put(KEY_CONTENT, note.content)
        values.put(KEY_TIMESTAMP, note.timestamp)

        // Insert row
        val id = db.insert(TABLE_NOTES, null, values)
        db.close()

        return id
    }

    // READ - Get a single note
    fun getNote(id: Int): Note? {
        val db = this.readableDatabase

        val cursor: Cursor = db.query(
            TABLE_NOTES,
            arrayOf(KEY_ID, KEY_TITLE, KEY_CONTENT, KEY_TIMESTAMP),
            "$KEY_ID=?",
            arrayOf(id.toString()),
            null,
            null,
            null
        )

        var note: Note? = null
        if (cursor.moveToFirst()) {
            note = Note(
                cursor.getInt(cursor.getColumnIndexOrThrow(KEY_ID)),
                cursor.getString(cursor.getColumnIndexOrThrow(KEY_TITLE)),
                cursor.getString(cursor.getColumnIndexOrThrow(KEY_CONTENT)),
                cursor.getLong(cursor.getColumnIndexOrThrow(KEY_TIMESTAMP))
            )
        }

        cursor.close()
        db.close()

        return note
    }

    // READ - Get all notes
    fun getAllNotes(): List<Note> {
        val notesList = mutableListOf<Note>()

        val selectQuery = "SELECT * FROM $TABLE_NOTES ORDER BY $KEY_TIMESTAMP DESC"
        val db = this.readableDatabase
        val cursor: Cursor = db.rawQuery(selectQuery, null)

        if (cursor.moveToFirst()) {
            do {
                val note = Note(
                    cursor.getInt(cursor.getColumnIndexOrThrow(KEY_ID)),
                    cursor.getString(cursor.getColumnIndexOrThrow(KEY_TITLE)),
                    cursor.getString(cursor.getColumnIndexOrThrow(KEY_CONTENT)),
                    cursor.getLong(cursor.getColumnIndexOrThrow(KEY_TIMESTAMP))
                )
                notesList.add(note)
            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()

        return notesList
    }

    // UPDATE - Update a note
    fun updateNote(note: Note): Int {
        val db = this.writableDatabase

        val values = ContentValues()
        values.put(KEY_TITLE, note.title)
        values.put(KEY_CONTENT, note.content)
        values.put(KEY_TIMESTAMP, note.timestamp)

        // Update row
        val rowsAffected = db.update(
            TABLE_NOTES,
            values,
            "$KEY_ID=?",
            arrayOf(note.id.toString())
        )

        db.close()
        return rowsAffected
    }

    // DELETE - Delete a note
    fun deleteNote(id: Int): Int {
        val db = this.writableDatabase

        val rowsDeleted = db.delete(
            TABLE_NOTES,
            "$KEY_ID=?",
            arrayOf(id.toString())
        )

        db.close()
        return rowsDeleted
    }

    // Get notes count
    fun getNotesCount(): Int {
        val countQuery = "SELECT * FROM $TABLE_NOTES"
        val db = this.readableDatabase
        val cursor: Cursor = db.rawQuery(countQuery, null)
        val count = cursor.count
        cursor.close()
        db.close()
        return count
    }
}