package com.pb.leadmanagement.utility

import android.app.DatePickerDialog
import android.content.Context
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by Nilesh Birhade on 20-09-2018.
 */
open class DateTimePicker {

    internal var simpleDateFormat = SimpleDateFormat("dd-MMM-yyyy")

    companion object {

        fun showTodayOnwardsDatePickerDialog(mContex: Context, callBack: DatePickerDialog.OnDateSetListener) {
            val calendar = Calendar.getInstance()

            val dialog = DatePickerDialog(mContex, callBack, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH))

            dialog.datePicker.minDate = calendar.timeInMillis


            dialog.show()
        }

        fun showDatePickerDialog(mContex: Context, callBack: DatePickerDialog.OnDateSetListener) {
            val calendar = Calendar.getInstance()

            val dialog = DatePickerDialog(mContex, callBack, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH))

            dialog.datePicker.maxDate = calendar.timeInMillis


            dialog.show()
        }

        fun policyExpValidation(context: Context, date: Date, callBack: DatePickerDialog.OnDateSetListener) {
            val calendar = Calendar.getInstance()
            val dialog: DatePickerDialog
            if (date.month <= calendar.get(Calendar.MONTH))
                dialog = DatePickerDialog(context, callBack, calendar.get(Calendar.YEAR), date.month, date.date)
            else
                dialog = DatePickerDialog(context, callBack, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH))

            dialog.datePicker.minDate = calendar.timeInMillis

            calendar.add(Calendar.MONTH, 12)
            dialog.datePicker.maxDate = calendar.timeInMillis

            dialog.show()
        }

        fun mfgYearMonthValidation(context: Context, date: Date, callBack: DatePickerDialog.OnDateSetListener) {
            val calendar = Calendar.getInstance()
            val dialog = DatePickerDialog(context, callBack, calendar.get(Calendar.YEAR), date.month, date.date)

            calendar.add(Calendar.YEAR, -15)
            dialog.datePicker.minDate = calendar.timeInMillis
            calendar.add(Calendar.YEAR, 15)

            val yearDiff = calendar.time.year - date.year
            val monthDif = date.month - calendar.time.month
            //calendar.set(date.getYear(), date.getMonth(), date.getDate());
            calendar.add(Calendar.YEAR, -yearDiff)
            calendar.add(Calendar.MONTH, monthDif)
            dialog.datePicker.maxDate = calendar.timeInMillis


            dialog.show()
        }

    }

}