package com.android.solomon.kotlinbook

import android.database.Cursor
import android.database.CursorWrapper

import com.android.solomon.kotlinbook.BookDbSchema.BookTable
import java.util.*


class BookCursorWrapper(cursor: Cursor?) : CursorWrapper(cursor) {
    fun getBook(): Book? {
        val uuidString = getString(getColumnIndex(BookTable.Cols.UUID))
        val title = getString(getColumnIndex(BookTable.Cols.TITLE))
        val date = getLong(getColumnIndex(BookTable.Cols.DATE))
        val isSolved = getInt(getColumnIndex(BookTable.Cols.SOLVED))
        val suspect = getString(getColumnIndex(BookTable.Cols.SUSPECT))
        val book = Book(UUID.fromString(uuidString))
        book.title = title
        book.date = Date(date)
        book.solved = isSolved != 0
        book.suspect = suspect
        return book
    }
}