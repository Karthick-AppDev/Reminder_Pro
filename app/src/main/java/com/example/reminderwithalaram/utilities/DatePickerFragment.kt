package com.example.reminderwithalaram.utilities

import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.widget.DatePicker
import androidx.fragment.app.DialogFragment
import com.example.reminderwithalaram.MainActivity
import java.util.Calendar

class DatePickerFragment : DialogFragment(), DatePickerDialog.OnDateSetListener {
    private  val TAG = "DatePickerFragment"
    lateinit var getDateAndTime: getDateAndTime

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        // Use the current date as the default date in the picker.
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        // Create a new instance of DatePickerDialog and return it.
        return DatePickerDialog(requireContext(), this, year, month, day)

    }

    override fun onDateSet(view: DatePicker, year: Int, month: Int, day: Int) {
        // Do something with the date the user picks.
        Log.d(TAG, "onDateSet: $day/$month/$year")
        getDateAndTime.getDate(year, month, day)
    }
    fun setInterface(getDateAndTime: getDateAndTime){
        this.getDateAndTime = getDateAndTime
    }
}