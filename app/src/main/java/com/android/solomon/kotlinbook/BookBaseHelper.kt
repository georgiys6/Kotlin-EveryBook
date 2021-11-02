package com.android.solomon.kotlinbook

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

import com.android.solomon.kotlinbook.BookDbSchema.*


class BookBaseHelper(
    context: Context?,
) : SQLiteOpenHelper(context, "bookBase.db",null, 1) {



    override fun onCreate(db: SQLiteDatabase?) {
        db!!.execSQL(
            "create table ${BookTable.NAME} (" +
                    " _id integer primary key autoincrement, ${BookTable.Cols.UUID}, " +
                    "${BookTable.Cols.TITLE}, ${BookTable.Cols.DATE} , " +
                    "${BookTable.Cols.SOLVED}, ${BookTable.Cols.SUSPECT})")
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
        TODO("Not yet implemented")
    }


}


