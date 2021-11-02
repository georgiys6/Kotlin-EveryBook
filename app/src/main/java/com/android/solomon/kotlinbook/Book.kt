package com.android.solomon.kotlinbook

import java.util.*

class Book(val id: UUID) {

    var mId:UUID
        get() {
            return mId
        }
        set(id) {
            mId = id
        }
    var title:String
        get() {
            return title
        }
        set(title) {
            this.title = title
        }
    var date:Date
        get() {
            return date
        }
        set(date) {
            this.date = date
        }
    var solved: Boolean
        get() {
            return solved
        }
        set(solved) {
            this.solved = solved
        }
    var suspect:String
        get() {
            return suspect
        }
        set(suspect) {
            this.suspect = suspect
        }

    init {
        mId = id
        date = Date()
    }
    constructor():this(UUID.randomUUID())
}