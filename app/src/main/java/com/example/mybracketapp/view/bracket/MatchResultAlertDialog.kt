package com.example.mybracketapp.view.bracket

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import androidx.core.view.children
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import com.example.mybracketapp.R
import com.example.mybracketapp.databinding.AlertMatchResultBinding
import com.example.mybracketapp.model.MatchCompetitor
import com.example.mybracketapp.model.MatchData

class MatchResultAlertDialog(private val mActivity: AppCompatActivity, private val matchData: MatchData) : DialogFragment() {

    companion object {
        const val ALERT_STRING = "MatchResultAlertDialog"
    }

    private lateinit var mDataBinding: AlertMatchResultBinding
    private var isValidSelection: Boolean = false
    private var selectedCompetitor: MatchCompetitor = MatchCompetitor()
    private var selectedWinCondition: String = ""

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        mDataBinding = DataBindingUtil.inflate(inflater, R.layout.alert_match_result, container, false)
        mDataBinding.match = matchData
        return mDataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setBindings()
        setPointsBinding()
        setSubmissionsBinding()
    }

    private fun setBindings() {
        mDataBinding.root.setOnClickListener {
            mDataBinding.layoutPointsWinCondition.competitorTopPoints.clearFocus()
            mDataBinding.layoutPointsWinCondition.competitorBottomPoints.clearFocus()
            val imm: InputMethodManager = mActivity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(it.windowToken, 0)
        }

        mDataBinding.competitorTopSelector.setOnClickListener {
            selectedCompetitor = matchData.getCompetitorTop()
            checkValidSelection()
        }

        mDataBinding.competitorBottomSelector.setOnClickListener {
            selectedCompetitor = matchData.getCompetitorBottom()
            checkValidSelection()
        }

        mDataBinding.winViaSpinner.onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                mDataBinding.winViaMethodBaseLayout.children.forEachIndexed { index, view ->
                    view.visibility = if(index == p2) View.VISIBLE else View.GONE
                }
                selectedWinCondition = MatchData.WIN_COND_ARRAY[p2]
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {}
        }

        mDataBinding.matchSaveButton.setOnClickListener {
            if(isValidSelection) {
                when(selectedWinCondition) {
                    MatchData.WIN_COND_POINTS -> matchData.setPointResult(
                        mDataBinding.layoutPointsWinCondition.competitorTopPoints.text.toString().toInt(),
                        mDataBinding.layoutPointsWinCondition.competitorBottomPoints.text.toString().toInt())
                    MatchData.WIN_COND_SUBMISSION -> matchData.setSubmissionResult(
                        if(mDataBinding.layoutSubmissionWinCondition.submissionsSpinner.selectedItemPosition != 2)
                            mDataBinding.layoutSubmissionWinCondition.submissionsSpinner.selectedItem.toString()
                        else
                            mDataBinding.layoutSubmissionWinCondition.submissionEditText.text.toString(), selectedCompetitor)

                }
                dismiss()
            }
        }
    }

    private fun setPointsBinding() {
        mDataBinding.layoutPointsWinCondition.competitorTopMinusButton.setOnClickListener {
            updatePoints(-1, mDataBinding.layoutPointsWinCondition.competitorTopPoints)
        }

        mDataBinding.layoutPointsWinCondition.competitorTopPlusButton.setOnClickListener {
            updatePoints(1, mDataBinding.layoutPointsWinCondition.competitorTopPoints)
        }

        mDataBinding.layoutPointsWinCondition.competitorBottomMinusButton.setOnClickListener {
            updatePoints(-1, mDataBinding.layoutPointsWinCondition.competitorBottomPoints)
        }

        mDataBinding.layoutPointsWinCondition.competitorBottomPlusButton.setOnClickListener {
            updatePoints(1, mDataBinding.layoutPointsWinCondition.competitorBottomPoints)
        }

        mDataBinding.layoutPointsWinCondition.competitorTopPoints.addTextChangedListener(object: TextWatcher{
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                checkPoints()
            }
            override fun afterTextChanged(s: Editable) {}
        })

        mDataBinding.layoutPointsWinCondition.competitorBottomPoints.addTextChangedListener(object: TextWatcher{
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                checkPoints()
            }
            override fun afterTextChanged(s: Editable) {}
        })
    }

    private fun updatePoints(increment: Int, editText: EditText) {
        try {
            if(editText.text.toString().toInt()+increment < 0){
                throw NumberFormatException()
            }
            editText.setText((editText.text.toString().toInt()+increment).toString())
        } catch (exception: NumberFormatException) {
            editText.setText(if(increment>0)"1" else "0")
        }
    }

    private fun checkPoints() {
        try {
            selectedCompetitor = when {
                mDataBinding.layoutPointsWinCondition.competitorTopPoints.text.toString().toInt()
                        > mDataBinding.layoutPointsWinCondition.competitorBottomPoints.text.toString().toInt() -> {
                    matchData.getCompetitorTop()
                }
                mDataBinding.layoutPointsWinCondition.competitorTopPoints.text.toString().toInt()
                        < mDataBinding.layoutPointsWinCondition.competitorBottomPoints.text.toString().toInt() -> {
                    matchData.getCompetitorBottom()
                }
                else -> {
                    MatchCompetitor()
                }
            }
        } catch (exception: NumberFormatException) {
            selectedCompetitor = MatchCompetitor()
        }
        checkValidSelection()
    }

    private fun checkIfPointsAreValid(): Boolean {
        if(selectedWinCondition != MatchData.WIN_COND_POINTS && selectedWinCondition != MatchData.WIN_COND_TIME)
            return true

        return try {
            val topWinner: Boolean = selectedCompetitor.competitor == matchData.getCompetitorTop().competitor
            val topPoints: Int =  mDataBinding.layoutPointsWinCondition.competitorTopPoints.text.toString().toInt()
            val bottomPoints: Int = mDataBinding.layoutPointsWinCondition.competitorBottomPoints.text.toString().toInt()

            (topWinner && topPoints > bottomPoints) || (!topWinner && topPoints < bottomPoints)
        } catch (exception: NumberFormatException) {
            false
        }
    }

    private fun setSubmissionsBinding() {
        mDataBinding.layoutSubmissionWinCondition.submissionsSpinner.onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                mDataBinding.layoutSubmissionWinCondition.submissionEditText.visibility = if(p2 == 2) View.VISIBLE else View.GONE
                if(p2 != 2) {
                    mDataBinding.layoutSubmissionWinCondition.submissionEditText.text.clear()
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {}
        }

        mDataBinding.layoutSubmissionWinCondition.submissionEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                checkValidSelection()
            }
            override fun afterTextChanged(s: Editable) {}
        })
    }

    private fun checkIfSubmissionIsValid(): Boolean {
        if(selectedWinCondition != MatchData.WIN_COND_SUBMISSION)
            return true
        return mDataBinding.layoutSubmissionWinCondition.submissionsSpinner.selectedItemPosition != 3
                || mDataBinding.layoutSubmissionWinCondition.submissionEditText.text.isNotEmpty()
    }

    private fun checkValidSelection() {

        isValidSelection = !selectedCompetitor.competitor.isPlaceholder()
                    && selectedWinCondition.isNotEmpty()
                    && checkIfPointsAreValid()
                    && checkIfSubmissionIsValid()

        mDataBinding.matchSaveButton.isEnabled = isValidSelection

        when(selectedCompetitor.beltColor) {
            MatchCompetitor.Color.Red -> {
                mDataBinding.matchSaveButton.background = AppCompatResources.getDrawable(mActivity, R.drawable.button_red_background)
                mDataBinding.competitorTopSelector.backgroundTintList = ContextCompat.getColorStateList(mActivity, R.color.sambo_red)
                mDataBinding.competitorBottomSelector.backgroundTintList = ContextCompat.getColorStateList(mActivity, R.color.sambo_blue_25)
            }
            MatchCompetitor.Color.Blue -> {
                mDataBinding.matchSaveButton.background = AppCompatResources.getDrawable(mActivity, R.drawable.button_blue_background)
                mDataBinding.competitorTopSelector.backgroundTintList = ContextCompat.getColorStateList(mActivity, R.color.sambo_red_25)
                mDataBinding.competitorBottomSelector.backgroundTintList = ContextCompat.getColorStateList(mActivity, R.color.sambo_blue)
            }
            else -> {
                mDataBinding.competitorTopSelector.backgroundTintList = ContextCompat.getColorStateList(mActivity, R.color.sambo_red_25)
                mDataBinding.competitorBottomSelector.backgroundTintList = ContextCompat.getColorStateList(mActivity, R.color.sambo_blue_25)
            }
        }
    }
}