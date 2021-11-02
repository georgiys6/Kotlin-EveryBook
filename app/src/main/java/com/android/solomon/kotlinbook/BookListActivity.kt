package com.android.solomon.kotlinbook

import androidx.fragment.app.Fragment

class BookListActivity : SingleFragmentActivity() {

    override fun createFragment(): Fragment? {
        return BookListFragment()
    }
}