package com.android.solomon.kotlinbook

class BookDbSchema {
    companion object BookTable{
        const val NAME = "books"
        object Cols {
            const val UUID = "uuid"
            const val TITLE = "title"
            const val DATE = "date"
            const val SOLVED = "solved"
            const val SUSPECT = "suspect"
        }
    }
}