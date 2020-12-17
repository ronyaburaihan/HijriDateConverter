package com.techdoctorbd.arabicdateconverter

import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import java.text.SimpleDateFormat
import java.util.*
import java.util.Calendar.getInstance

class MainActivity : AppCompatActivity() {

    private var dayOfWeek: String? = null
    private var mDay = 0
    private var mMonth = 0
    private var mYear = 0
    private var maxDateCal = getInstance()
    private var minDateCal = getInstance()
    private var simpleDateFormat: SimpleDateFormat? = null
    private var dateFormatForDay: SimpleDateFormat? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val calendarForCurrent = getInstance()
        simpleDateFormat = SimpleDateFormat("dd MMMM yyyy , EEEE", Locale.getDefault())
        dateFormatForDay = SimpleDateFormat("EEEE", Locale.getDefault())

        //to set custom date in calendar
        calendarForCurrent.set(2020, 2, 12)

        // to get date, month, year, day name from calendar
        val date = calendarForCurrent[Calendar.DAY_OF_MONTH]
        Log.d("Date: ", "$date")
        val month = calendarForCurrent[Calendar.MONTH]
        Log.d("Month: ", "$month")
        val year = calendarForCurrent[Calendar.YEAR]
        Log.d("Year: ", "$year")
        dayOfWeek = dateFormatForDay!!.format(calendarForCurrent.time).toString()
                .toLowerCase(Locale.getDefault())
        Log.d("Day of Week: ", dayOfWeek!!)


        //to set date into text view
        tv_date_english.text = simpleDateFormat!!.format(calendarForCurrent.time)

        //to convert selected date into hijri date
        val resultText = DateConverter.getDateInArabic(date, month + 1, year, dayOfWeek!!, this)
        tv_arabic_date.text = resultText

        //to set range into calendar
        minDateCal.set(2019, 11, 29)
        maxDateCal.set(2020, 11, 16)

        tv_date_english.setOnClickListener {
            openDatePicker()
        }
    }


    private fun openDatePicker() {
        val calendar = getInstance()

        if (mDay == 0) {
            mYear = 2020 //calendar.get(Calendar.YEAR)
            mMonth = 2 //calendar.get(Calendar.MONTH)
            mDay = 12 //calendar.get(Calendar.DAY_OF_MONTH)
        }

        val datePickerDialog = DatePickerDialog(this, { _, year, monthOfYear, dayOfMonth ->
            mDay = dayOfMonth
            mMonth = monthOfYear
            mYear = year
            calendar.set(year, monthOfYear, dayOfMonth)
            val enDate = simpleDateFormat?.format(calendar.time)
            tv_date_english.text = enDate
            //Toast.makeText(this@MainActivity, "$dayOfMonth ${monthOfYear + 1} $year", Toast.LENGTH_SHORT).show()

            dayOfWeek = dateFormatForDay?.format(calendar.time)?.toLowerCase(Locale.ROOT)
            val resultText = DateConverter.getDateInArabic(dayOfMonth, monthOfYear + 1, year, dayOfWeek!!, this)
            tv_arabic_date.text = resultText
        }, mYear, mMonth, mDay)

        datePickerDialog.datePicker.minDate = minDateCal.timeInMillis
        datePickerDialog.datePicker.maxDate = maxDateCal.timeInMillis

        datePickerDialog.show()
    }
}