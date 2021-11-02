package com.android.solomon.kotlinbook

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import java.util.*

class BookLab(context: Context) {

    companion object BookTable{
        private var sBookLab: BookLab? = null

        operator fun get(context: Context): BookLab? {
            if (sBookLab == null) {
                sBookLab = BookLab(context)
            }
            return sBookLab
        }
    }

    private var mContext: Context? = null
    private var mDatabase: SQLiteDatabase? = null



    init {
        mContext = context.applicationContext
        mDatabase = BookBaseHelper(mContext)
            .writableDatabase
    }

    fun addBook(c: Book) {
        val values = getContentValues(c)
        mDatabase!!.insert(BookDbSchema.BookTable.NAME, null, values)
    }

    fun getBooks(): List<Book?>? {
        val books: MutableList<Book?> = ArrayList()
        val cursor = queryBooks(null, null)
        try {
            cursor.moveToFirst()
            while (!cursor.isAfterLast) {
                books.add(cursor.getBook())
                cursor.moveToNext()
            }
        } finally {
            cursor.close()
        }
        return books
    }

    fun getBook(id: UUID): Book? {
        val cursor = queryBooks("${BookDbSchema.BookTable.Cols.UUID.toString()} = ?",
            arrayOf(id.toString()))
        return try {
            if (cursor.count === 0) {
                return null
            }
            cursor.moveToFirst()
            cursor.getBook()
        } finally {
            cursor.close()
        }
    }

    fun updateBook(book: Book) {
        val uuidString: String = book.mId.toString()
        val values = getContentValues(book)
        mDatabase!!.update(
            BookDbSchema.BookTable.NAME,
            values,
            "${BookDbSchema.BookTable.Cols.UUID.toString()} = ?",
            arrayOf(uuidString)
        )
    }

    private fun queryBooks(whereClause: String?, whereArgs: Array<String>?): BookCursorWrapper {
        val cursor = mDatabase!!.query(
            BookDbSchema.BookTable.NAME,
            null,  // Columns - null selects all columns
            whereClause,
            whereArgs,
            null,  // groupBy
            null,  // having
            null // orderBy
        )
        return BookCursorWrapper(cursor)
    }

    private fun getContentValues(book: Book): ContentValues {
        val values = ContentValues()
        values.put(BookDbSchema.BookTable.Cols.UUID, book.mId.toString())
        values.put(BookDbSchema.BookTable.Cols.TITLE, book.title)
        values.put(BookDbSchema.BookTable.Cols.DATE, book.date.getTime())
        values.put(BookDbSchema.BookTable.Cols.SOLVED, if (book.solved) 1 else 0)
        values.put(BookDbSchema.BookTable.Cols.SUSPECT, book.suspect)
        return values
    }
}