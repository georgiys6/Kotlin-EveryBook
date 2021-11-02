package com.android.solomon.kotlinbook

import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.CheckBox
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.startActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class BookListFragment : Fragment() {

    private val SAVED_SUBTITLE_VISIBLE = "subtitle"

    private var mBookRecyclerView: RecyclerView? = null
    private var mAdapter: BookAdapter? = null
    private var mSubtitleVisible = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_book_list, container, false)
        mBookRecyclerView = view.findViewById<View>(R.id.book_recycler_view) as RecyclerView
        mBookRecyclerView!!.layoutManager = LinearLayoutManager(activity)
        if (savedInstanceState != null) {
            mSubtitleVisible = savedInstanceState.getBoolean(SAVED_SUBTITLE_VISIBLE)
        }
        updateUI()
        return view
    }

    override fun onResume() {
        super.onResume()
        updateUI()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean(SAVED_SUBTITLE_VISIBLE, mSubtitleVisible)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.fragment_book_list, menu)
        val subtitleItem = menu.findItem(R.id.show_subtitle)
        if (mSubtitleVisible) {
            subtitleItem.setTitle(R.string.hide_subtitle)
        } else {
            subtitleItem.setTitle(R.string.show_subtitle)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.new_book -> {
                val book = Book()
                BookLab[activity!!]!!.addBook(book)
                val intent = BookPagerActivity.newIntent(activity, book.mId)
                startActivity(intent)
                true
            }
            R.id.show_subtitle -> {
                mSubtitleVisible = !mSubtitleVisible
                activity!!.invalidateOptionsMenu()
                updateSubtitle()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun updateSubtitle() {
        val bookLab = BookLab[activity!!]
        val bookCount: Int = bookLab!!.getBooks()!!.size
        var subtitle: String? = getString(R.string.subtitle_format, bookCount)
        if (!mSubtitleVisible) {
            subtitle = null
        }
        val activity = activity as AppCompatActivity?
        activity!!.supportActionBar!!.setSubtitle(subtitle)
    }

    private fun updateUI() {
        val bookLab = BookLab[activity!!]
        val books = bookLab!!.getBooks()
        if (mAdapter == null) {
            mAdapter = BookAdapter(books)
            mBookRecyclerView!!.adapter = mAdapter
        } else {
            mAdapter!!.setBooks(books)
            mAdapter!!.notifyDataSetChanged()
        }
        updateSubtitle()
    }

    private class BookHolder(inflater: LayoutInflater, parent: ViewGroup?) :
        RecyclerView.ViewHolder(inflater.inflate(R.layout.list_item_book, parent, false)),
        View.OnClickListener {
        private var mBook: Book? = null
        private val mTitleTextView: TextView
        private val mDateTextView: TextView
        private val mCheckBox: CheckBox
        fun bind(book: Book?) {
            mBook = book
            val titleTextView = itemView.findViewById<TextView>(R.id.book_title)
            val dateTextView = itemView.findViewById<TextView>(R.id.book_date)
            titleTextView.text = mBook!!.title
            dateTextView.text = mBook!!.date.toString()
            mCheckBox.isChecked = mBook!!.solved
            itemView.setOnClickListener(this)
        }

        override fun onClick(view: View) {
            Toast.makeText(view.context, "${mBook!!.title} clicked!", Toast.LENGTH_SHORT / 2)
                .show()

        }

        init {
            itemView.setOnClickListener(this)
            mTitleTextView = itemView.findViewById<View>(R.id.book_title) as TextView
            mDateTextView = itemView.findViewById<View>(R.id.book_date) as TextView
            mCheckBox = itemView.findViewById<View>(R.id.checkBox) as CheckBox
        }
    }

    private class BookAdapter(private var mBooks: List<Book?>?) :
        RecyclerView.Adapter<BookHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookHolder =
            BookHolder(LayoutInflater.from(parent!!.context),parent)/*{
            val layoutInflater = LayoutInflater.from(activity)
            return BookHolder(layoutInflater, parent)
        }*/

        override fun onBindViewHolder(holder: BookHolder, position: Int) {
            val book = mBooks!![position]
            holder.bind(book)
        }

        override fun getItemCount(): Int {
            return mBooks!!.size
        }

        fun setBooks(books: List<Book?>?) {
            mBooks = books
        }
    }
}