package com.example.mybracketapp.activity

import android.app.DatePickerDialog
import android.content.Context
import android.graphics.Rect
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.mybracketapp.R
import com.example.mybracketapp.SLApplication
import com.example.mybracketapp.databinding.ActivityRegistrationFormBinding
import com.example.mybracketapp.viewmodel.RegistrationFormViewModel
import java.util.*


class RegistrationFormActivity : BaseActivity<ActivityRegistrationFormBinding, RegistrationFormViewModel>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        SLApplication.setContext(applicationContext)
        mDataBinding = DataBindingUtil.setContentView(this, R.layout.activity_registration_form)
        mViewModel = ViewModelProvider(this).get(RegistrationFormViewModel::class.java)
        setSupportActionBar(findViewById(R.id.toolbar))
        mDataBinding.viewModel = mViewModel
        mDataBinding.lifecycleOwner = this

        mDataBinding.birthdateEditText.setOnClickListener(onBirthdateClick())
        mDataBinding.ageEditText.setOnClickListener(onBirthdateClick())

        mDataBinding.bothCheckbox.setOnCheckedChangeListener{ _, isChecked ->
            mDataBinding.redCheckbox.isChecked = isChecked
            mDataBinding.blueCheckbox.isChecked = isChecked
            mDataBinding.neitherCheckbox.isChecked = false
        }

        mDataBinding.neitherCheckbox.setOnCheckedChangeListener{ _, isChecked ->
            if(isChecked) {
                mDataBinding.redCheckbox.isChecked = false
                mDataBinding.blueCheckbox.isChecked = false
                mDataBinding.bothCheckbox.isChecked = false
            }
        }

        mDataBinding.weightEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                try {
                    if(mDataBinding.weightEditText.hasFocus()){
                        mDataBinding.weightSeekbar.progress = s.toString().toInt()
                    }
                } catch (ex: Exception) {
                }
            }
            override fun afterTextChanged(s: Editable) {}
        })

        mDataBinding.weightSeekbar.setOnSeekBarChangeListener(object : OnSeekBarChangeListener{
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                if(!mDataBinding.weightEditText.hasFocus()){
                    mDataBinding.weightEditText.setText(p1.toString())
                }
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {}
            override fun onStopTrackingTouch(p0: SeekBar?) {}
        })

    }

    override fun dispatchTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN) {
            val v: View? = currentFocus
            if (v is EditText) {
                val outRect = Rect()
                v.getGlobalVisibleRect(outRect)
                if (!outRect.contains(event.rawX.toInt(), event.rawY.toInt())) {
                    v.clearFocus()
                    val imm: InputMethodManager =
                        getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0)
                }
            }
        }
        return super.dispatchTouchEvent(event)
    }

    private fun onBirthdateClick(): View.OnClickListener {
        return View.OnClickListener {
            val calendar: Calendar = mViewModel.dateOfBirth.value?.let { mViewModel.dateOfBirth.value } ?: Calendar.getInstance()
            val today = Calendar.getInstance()
            val datePickerDialog = DatePickerDialog(this, R.style.MySpinnerDatePickerStyle,
                { _, year, monthOfYear, dayOfMonth ->
                    mViewModel.dateOfBirth.value = Calendar.getInstance()
                    mViewModel.dateOfBirth.value!!.set(Calendar.YEAR, year)
                    mViewModel.dateOfBirth.value!!.set(Calendar.MONTH, monthOfYear)
                    mViewModel.dateOfBirth.value!!.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                mDataBinding.invalidateAll()},
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            )
            datePickerDialog.datePicker.minDate = today.timeInMillis - 1000L*60*60*24*365*80
            datePickerDialog.datePicker.maxDate = today.timeInMillis
            datePickerDialog.show()
        }
    }

}