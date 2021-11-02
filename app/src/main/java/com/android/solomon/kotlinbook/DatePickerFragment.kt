package com.android.solomon.kotlinbook

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.DatePicker
import androidx.fragment.app.DialogFragment
import java.util.*

class DatePickerFragment : DialogFragment() {


    private var mDatePicker: DatePicker? = null
    companion object DatePickerFragment{
        val EXTRA_DATE = "com.android.solomon.everybook.date"
        private val ARG_DATE = "date"

        fun newInstance(date: Date?): com.android.solomon.kotlinbook.DatePickerFragment {
            val args = Bundle()
            args.putSerializable(ARG_DATE, date)
            val fragment = DatePickerFragment()
            fragment.arguments = args
            return fragment
    }


    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val date = arguments!!.getSerializable(ARG_DATE) as Date?
        val calendar = Calendar.getInstance()
        calendar.time = date
        val year = calendar[Calendar.YEAR]
        val month = calendar[Calendar.MONTH]
        val day = calendar[Calendar.DAY_OF_MONTH]
        val v: View = LayoutInflater.from(activity).inflate(R.layout.dialog_date, null)
        mDatePicker = v.findViewById<View>(R.id.dialog_date_picker) as DatePicker
        mDatePicker!!.init(year, month, day, null)
        return AlertDialog.Builder(activity).setView(v).setTitle(R.string.date_picker_title)
            .setPositiveButton(android.R.string.ok,
                DialogInterface.OnClickListener { dialogInterface, i ->
                    val year = mDatePicker!!.year
                    val month = mDatePicker!!.month
                    val day = mDatePicker!!.dayOfMonth
                    val date = GregorianCalendar(year, month, day).time
                    sendResult(Activity.RESULT_OK, date)
                }).create()
    }

    private fun sendResult(resultCode: Int, date: Date) {
        if (targetFragment == null) {
            return
        }
        val intent = Intent()
        intent.putExtra(EXTRA_DATE, date)
        targetFragment!!.onActivityResult(targetRequestCode, resultCode, intent)
    }
}