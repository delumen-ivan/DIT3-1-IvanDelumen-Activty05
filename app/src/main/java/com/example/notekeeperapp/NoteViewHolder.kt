package com.example.notekeeperapp

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.*

class NoteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val tvTitle: TextView = itemView.findViewById(R.id.tvTitle)
    private val tvContent: TextView = itemView.findViewById(R.id.tvContent)
    private val tvTimestamp: TextView = itemView.findViewById(R.id.tvTimestamp)

    fun bind(note: Note, onNoteClick: (Note) -> Unit, onNoteLongClick: (Note) -> Unit) {
        tvTitle.text = note.title
        tvContent.text = note.content
        tvTimestamp.text = formatTimestamp(note.timestamp)

        // Click to edit
        itemView.setOnClickListener {
            onNoteClick(note)
        }

        // Long click to delete
        itemView.setOnLongClickListener {
            onNoteLongClick(note)
            true
        }
    }

    private fun formatTimestamp(timestamp: Long): String {
        val sdf = SimpleDateFormat("MMM dd, yyyy hh:mm a", Locale.getDefault())
        return sdf.format(Date(timestamp))
    }
}