package com.example.mybracketapp.viewmodel

import android.widget.ArrayAdapter
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mybracketapp.DataCache
import com.example.mybracketapp.R
import com.example.mybracketapp.SLApplication
import com.example.mybracketapp.view.NoPaddingArrayAdapter
import java.util.*

class RegistrationFormViewModel : ViewModel() {

    var schoolNames = arrayOf("Ground King", "Sambo Texas", "West Oak Cliff", "Yo Mama", "Bitch", "Nunya", "What", "Help Me")
    var schoolNameAdapter: ArrayAdapter<String> = ArrayAdapter(SLApplication.getContext(), android.R.layout.select_dialog_item, schoolNames)

    val dateOfBirth: MutableLiveData<Calendar> = MutableLiveData(Calendar.getInstance())

    var skillLevels = DataCache.getInstance().skillLevels
    var skillLevelAdapter: NoPaddingArrayAdapter<String> = NoPaddingArrayAdapter(SLApplication.getContext(), android.R.layout.select_dialog_item, skillLevels.asList())

    var weight: MutableLiveData<Int> = MutableLiveData(150)

    fun dateOfBirthString(): String {
        return "${SLApplication.getContext().getString(R.string.birthdate)}: ${dateOfBirth.value?.get(Calendar.MONTH)}/${dateOfBirth.value?.get(Calendar.DAY_OF_MONTH)}/${dateOfBirth.value?.get(Calendar.YEAR)}"
    }

    fun ageString(): String {
        val today: Calendar = Calendar.getInstance()
        var yearDiff: Int = today.get(Calendar.YEAR) - dateOfBirth.value!!.get(Calendar.YEAR)
        if (dateOfBirth.value!!.get(Calendar.MONTH) > today.get(Calendar.MONTH) ||
            (dateOfBirth.value!!.get(Calendar.MONTH) == today.get(Calendar.MONTH) && dateOfBirth.value!!.get(Calendar.DATE) > today.get(Calendar.DATE))) {
            yearDiff--
        }
        return "${SLApplication.getContext().getString(R.string.age)}: $yearDiff"
    }

}