package com.example.mybracketapp.view.bracket

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.mybracketapp.R
import com.example.mybracketapp.activity.BaseFragment
import com.example.mybracketapp.databinding.FragmentBracketBinding
import com.example.mybracketapp.databinding.ItemBracketColumnBinding
import com.example.mybracketapp.databinding.ItemBracketMatchBinding
import com.example.mybracketapp.model.BucketData
import com.example.mybracketapp.model.ColumnData
import com.example.mybracketapp.Utils


class BracketFragment(private val mActivity: AppCompatActivity, private val bucketData: BucketData) : BaseFragment<FragmentBracketBinding>(){

    private lateinit var bracketRecyclerView: RecyclerView
    private lateinit var columnAdapter: BracketColumnAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return initView(inflater, container, R.layout.fragment_bracket)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bracketRecyclerView = mDataBinding.bracketsRecyclerView
        columnAdapter = BracketColumnAdapter(mActivity, bucketData.getColumns())
        bracketRecyclerView.adapter = columnAdapter
        bracketRecyclerView.layoutManager = LinearLayoutManager(mActivity, LinearLayoutManager.HORIZONTAL, false)
        bucketData.fragment = this
    }

    fun update() {
        columnAdapter.notifyDataSetChanged()
    }

    class BracketColumnAdapter(private val mActivity: AppCompatActivity, private val columns: ArrayList<ColumnData>) : RecyclerView.Adapter<BracketColumnAdapter.ColumnHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ColumnHolder {
            return ColumnHolder.from(mActivity, parent)
        }

        override fun onBindViewHolder(holder: ColumnHolder, position: Int) {
            holder.bind(mActivity, columns[position])
        }

        override fun getItemCount(): Int {
            return columns.size
        }

        class ColumnHolder(private val columnBinding: ItemBracketColumnBinding) : RecyclerView.ViewHolder(columnBinding.root) {

            private val bracketMatchBindings: ArrayList<ItemBracketMatchBinding> = ArrayList()

            companion object {
                fun from(activity: AppCompatActivity, parent: ViewGroup) : ColumnHolder {
                    val holder = ColumnHolder(ItemBracketColumnBinding.inflate(LayoutInflater.from(parent.context), parent, false))
                    val layoutParams = holder.columnBinding.root.layoutParams
                    layoutParams.width = (Utils.getScreenWidth(activity)/2.2F).toInt()
                    holder.columnBinding.root.layoutParams = layoutParams
                    return holder
                }
            }

            fun bind(mActivity: AppCompatActivity, columnData: ColumnData){
                columnData.getMatches().forEachIndexed { index, matchData ->
                    if(bracketMatchBindings.size > index) {
                        bracketMatchBindings[index].match = matchData
                    }else{
                        val bracketMatch = ItemBracketMatchBinding.inflate(LayoutInflater.from(mActivity), columnBinding.columnLinearLayout, true)
                        bracketMatch.match = matchData
                        bracketMatch.pendingButton.setOnClickListener {
                            MatchResultAlertDialog(mActivity, matchData).show(mActivity.supportFragmentManager, MatchResultAlertDialog.ALERT_STRING)
                        }
                        bracketMatch.competitorTopLayout.competitorMatchResult.isSelected = true
                        bracketMatch.competitorBottomLayout.competitorMatchResult.isSelected = true
                        val layoutParams = bracketMatch.root.layoutParams as LinearLayout.LayoutParams
                        layoutParams.weight = 1F
                        bracketMatch.root.layoutParams = layoutParams
                        bracketMatchBindings.add(bracketMatch)
                    }
                    columnBinding.columnLinearLayout.weightSum = columnBinding.columnLinearLayout.childCount.toFloat()
                }
            }

        }

    }
}