package com.android.solomon.kotlinbook

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.ContactsContract
import android.text.Editable
import android.text.TextWatcher
import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.CompoundButton
import android.widget.EditText
import androidx.fragment.app.Fragment
import java.util.*

class BookFragment : Fragment() {
    private val ARG_BOOK_ID = "book_id"
    private val DIALOG_DATE = "DialogDate"

    private val REQUEST_DATE = 0
    private val REQUEST_CONTACT = 1

    private var mBook: Book? = null
    private var mTitleField: EditText? = null
    private var mDateButton: Button? = null
    private var mSolvedCheckbox: CheckBox? = null
    private var mReportButton: Button? = null
    private var mSuspectButton: Button? = null

    companion object BookFragment{
        private val ARG_BOOK_ID = "book_id"

        fun newInstance(bookId: UUID?): com.android.solomon.kotlinbook.BookFragment {
            val args = Bundle()
            args.putSerializable(ARG_BOOK_ID, bookId)
            val fragment = BookFragment()
            fragment.arguments = args
            return fragment
        }
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val bookId = arguments!!.getSerializable(ARG_BOOK_ID) as UUID?
        mBook = BookLab[activity!!]!!.getBook(bookId!!)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v: View = inflater.inflate(R.layout.fragment_book, container, false)
        mTitleField = v.findViewById<View>(R.id.book_title) as EditText
        mTitleField!!.setText(mBook?.title)
        mTitleField!!.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                mBook!!.title = s.toString()
            }

            override fun afterTextChanged(s: Editable) {}
        })
        mDateButton = v.findViewById<View>(R.id.book_date) as Button
        updateDate()
        mDateButton!!.setOnClickListener {
            val manager = fragmentManager
            val dialog: DatePickerFragment = DatePickerFragment
                .newInstance(mBook!!.date)
            dialog.setTargetFragment(this@BookFragment, REQUEST_DATE)
            dialog.show(manager!!, DIALOG_DATE)
        }
        mSolvedCheckbox = v.findViewById<View>(R.id.book_solved) as CheckBox
        mSolvedCheckbox!!.isChecked = mBook!!.solved
        mSolvedCheckbox!!.setOnCheckedChangeListener { buttonView, isChecked ->
            mBook!!.solved = isChecked
        }
        mReportButton = v.findViewById<View>(R.id.book_report) as Button
        mReportButton!!.setOnClickListener {
            var i = Intent(Intent.ACTION_SEND)
            i.type = "text/plain"
            i.putExtra(Intent.EXTRA_TEXT, getBookReport())
            i.putExtra(
                Intent.EXTRA_SUBJECT,
                getString(R.string.book_report_subject)
            )
            i = Intent.createChooser(i, getString(R.string.send_report))
            startActivity(i)
        }
        val pickContact = Intent(
            Intent.ACTION_PICK,
            ContactsContract.Contacts.CONTENT_URI
        )
        mSuspectButton = v.findViewById<View>(R.id.book_suspect) as Button
        mSuspectButton!!.setOnClickListener { startActivityForResult(pickContact, REQUEST_CONTACT) }
        if (mBook!!.suspect != null) {
            mSuspectButton!!.text = mBook!!.suspect
        }
        val packageManager = activity!!.packageManager
        if (packageManager.resolveActivity(
                pickContact,
                PackageManager.MATCH_DEFAULT_ONLY
            ) == null
        ) {
            mSuspectButton!!.isEnabled = false
        }
        return v
    }

    override fun onPause() {
        super.onPause()
        BookLab[activity!!]
            ?.updateBook(mBook!!)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode != Activity.RESULT_OK) {
            return
        }
        if (requestCode == REQUEST_DATE) {
            val date = data
                ?.getSerializableExtra(DatePickerFragment.EXTRA_DATE) as Date?
            mBook!!.date = date!!
            updateDate()
        } else if (requestCode == REQUEST_CONTACT && data != null) {
            val contactUri = data.data
            // Specify which fields you want your query to return
            // values for.
            val queryFields = arrayOf(
                ContactsContract.Contacts.DISPLAY_NAME
            )
            // Perform your query - the contactUri is like a "where"
            // clause here
            val c = activity!!.contentResolver
                .query(contactUri!!, queryFields, null, null, null)
            try {
                // Double-check that you actually got results
                if (c!!.count == 0) {
                    return
                }
                // Pull out the first column of the first row of data -
                // that is your suspect's name.
                c.moveToFirst()
                val suspect = c.getString(0)
                mBook!!.suspect = suspect
                mSuspectButton!!.text = suspect
            } finally {
                c!!.close()
            }
        }
    }

    private fun updateDate() {
        mDateButton!!.text = mBook!!.date.toString()
    }

    private fun getBookReport(): String? {
        var solvedString: String? = null
        solvedString = if (mBook!!.solved) {
            getString(R.string.book_report_solved)
        } else {
            getString(R.string.book_report_unsolved)
        }
        val dateFormat = "EEE, MMM dd"
        val dateString: String =
            DateFormat.format(dateFormat, mBook!!.date).toString()
        var suspect: String = mBook!!.suspect
        suspect = if (suspect == null) {
            getString(R.string.book_report_no_suspect)
        } else {
            getString(R.string.book_report_suspect, suspect)
        }
        return getString(
            R.string.book_report,
            mBook!!.title, dateString, solvedString, suspect
        )
    }
}