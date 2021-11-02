package com.android.solomon.kotlinbook

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager.widget.ViewPager
import java.util.*

class BookPagerActivity : AppCompatActivity() {

    companion object BookPagerActivity{
        private val EXTRA_BOOK_ID = "com.android.solomon.everybook.book_id"

        fun newIntent(packageContext: Context?, bookId: UUID?): Intent? {
            val intent = Intent(packageContext, BookPagerActivity::class.java)
            intent.putExtra(EXTRA_BOOK_ID, bookId)
            return intent
        }
    }

    private val EXTRA_BOOK_ID = "com.android.solomon.everybook.book_id"

    private var mViewPager: ViewPager? = null
    private var mBooks: List<Book?>? = null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_book_pager)
        val bookId = intent.getSerializableExtra(EXTRA_BOOK_ID) as UUID?
        mViewPager = findViewById<View>(R.id.book_view_pager) as ViewPager
        mBooks = BookLab[this]!!.getBooks()
        val fragmentManager = supportFragmentManager
        mViewPager!!.adapter = object : FragmentStatePagerAdapter(fragmentManager) {
            override fun getItem(position: Int): Fragment {
                val book = mBooks!![position]
                return BookFragment.newInstance(book!!.mId)
            }

            override fun getCount(): Int {
                return mBooks!!.size
            }
        }
        for (i in mBooks!!.indices) {
            if (mBooks!![i]!!.mId.equals(bookId)) {
                mViewPager!!.currentItem = i
                break
            }
        }
    }
}