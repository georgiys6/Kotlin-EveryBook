package com.android.solomon.kotlinbook

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import java.util.*

class BookActivity : SingleFragmentActivity() {
    private val EXTRA_BOOK_ID = "com.android.solomon.everybook.book_id"

    fun newIntent(packageContext: Context?, bookId: UUID?): Intent {
        val intent = Intent(packageContext, BookActivity::class.java)
        intent.putExtra(EXTRA_BOOK_ID, bookId)
        return intent
    }

    override fun createFragment(): Fragment? {
        val bookId = intent.getSerializableExtra(EXTRA_BOOK_ID) as UUID?
        return BookFragment.newInstance(bookId)
    }
}