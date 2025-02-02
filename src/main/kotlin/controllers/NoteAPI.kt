package controllers

import models.Note
import utils.formatListString
import java.util.ArrayList

class NoteAPI() {

    private var notes = ArrayList<Note>()

    // ----------------------------------------------
    //  For Managing the id internally in the program
    // ----------------------------------------------
    private var lastNoteId = 0
    private fun getNextId() = lastNoteId++

    // ----------------------------------------------
    //  CRUD METHODS FOR NOTE ArrayList
    // ----------------------------------------------
    fun add(note: Note): Boolean {
        note.noteId = getNextId()
        return notes.add(note)
    }

    fun delete(id: Int) = notes.removeIf { note -> note.noteId == id }

    fun update(id: Int, note: Note?): Boolean {
        // find the note object by the index number
        val foundNote = findNote(id)

        // if the note exists, use the note details passed as parameters to update the found note in the ArrayList.
        if ((foundNote != null) && (note != null)) {
            foundNote.noteTitle = note.noteTitle
            foundNote.notePriority = note.notePriority
            foundNote.noteCategory = note.noteCategory
            return true
        }

        // if the note was not found, return false, indicating that the update was not successful
        return false
    }

    fun archiveNote(id: Int): Boolean {
        val foundNote = findNote(id)
        if (( foundNote != null) && (!foundNote.isNoteArchived)
            && ( foundNote.checkNoteCompletionStatus())) {
              foundNote.isNoteArchived = true
              return true
        }
        return false
    }

    // ----------------------------------------------
    //  LISTING METHODS FOR NOTE ArrayList
    // ----------------------------------------------
    fun listAllNotes() =
        if (notes.isEmpty()) "No notes stored"
        else formatListString(notes)

    fun listActiveNotes() =
        if (numberOfActiveNotes() == 0) "No active notes stored"
        else formatListString(notes.filter { note -> !note.isNoteArchived })

    fun listArchivedNotes() =
        if (numberOfArchivedNotes() == 0) "No archived notes stored"
        else formatListString(notes.filter { note -> note.isNoteArchived })

    // ----------------------------------------------
    //  COUNTING METHODS FOR NOTE ArrayList
    // ----------------------------------------------
    fun numberOfNotes() = notes.size
    fun numberOfArchivedNotes(): Int = notes.count { note: Note -> note.isNoteArchived }
    fun numberOfActiveNotes(): Int = notes.count { note: Note -> !note.isNoteArchived }

    // ----------------------------------------------
    //  SEARCHING METHODS
    // ---------------------------------------------
    fun findNote(noteId : Int) =  notes.find{ note -> note.noteId == noteId }

    fun searchNotesByTitle(searchString: String) =
       formatListString(
            notes.filter { note -> note.noteTitle.contains(searchString, ignoreCase = true) }
        )

    fun searchItemByContents(searchString: String): String {
        return if (numberOfNotes() == 0) "No notes stored"
        else {
            var listOfNotes = ""
            for (note in notes) {
                for (item in note.items) {
                    if (item.itemContents.contains(searchString, ignoreCase = true)) {
                        listOfNotes += "${note.noteId}: ${note.noteTitle} \n\t${item}\n"
                    }
                }
            }
            if (listOfNotes == "") "No items found for: $searchString"
            else listOfNotes
        }
    }

    // ----------------------------------------------
    //  LISTING METHODS FOR ITEMS
    // ----------------------------------------------
    fun listTodoItems(): String =
         if (numberOfNotes() == 0) "No notes stored"
         else {
             var listOfTodoItems = ""
             for (note in notes) {
                 for (item in note.items) {
                     if (!item.isItemComplete) {
                         listOfTodoItems += note.noteTitle + ": " + item.itemContents + "\n"
                     }
                 }
             }
             listOfTodoItems
         }

    // ----------------------------------------------
    //  COUNTING METHODS FOR ITEMS
    // ----------------------------------------------
    fun numberOfToDoItems(): Int {
        var numberOfToDoItems = 0
        for (note in notes) {
            for (item in note.items) {
                if (!item.isItemComplete) {
                    numberOfToDoItems++
                }
            }
        }
        return numberOfToDoItems
    }

}
