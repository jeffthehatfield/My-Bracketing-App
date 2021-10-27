package com.example.mybracketapp.view.bracket

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.mybracketapp.R
import com.example.mybracketapp.activity.BaseFragment
import com.example.mybracketapp.databinding.ItemBracketColumnBinding
import com.example.mybracketapp.databinding.ItemBracketMatchBinding
import com.example.mybracketapp.model.ColumnData
import com.example.mybracketapp.model.MatchData

class BracketColumnFragment(private val mActivity: AppCompatActivity, private val columnData1: ColumnData) : BaseFragment<ItemBracketColumnBinding>() {

    lateinit var bracketMatchItemAdapter: BracketMatchItemAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return initView(inflater, container, R.layout.item_bracket_column)
    }

    override fun initDataBindings() {
        bracketMatchItemAdapter = BracketMatchItemAdapter(mActivity, columnData1)

//        mDataBinding.columnRecyclerView.adapter = bracketMatchItemAdapter
//        mDataBinding.columnRecyclerView.layoutManager = LinearLayoutManager(context)
//        mDataBinding.columnRecyclerView.addItemDecoration(VerticalSpaceItemDecoration(140))

    }

    fun updateMatches() {
        bracketMatchItemAdapter.notifyDataSetChanged()
    }

    class BracketMatchItemAdapter(private val mActivity: AppCompatActivity, private val columnData: ColumnData) : RecyclerView.Adapter<BracketMatchItemAdapter.MatchCellItemHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MatchCellItemHolder {
            return MatchCellItemHolder.from(parent)
        }

        override fun onBindViewHolder(holder: MatchCellItemHolder, position: Int) {
            holder.bind(mActivity, columnData.getMatches()[position])
        }

        override fun getItemCount(): Int {
            return columnData.getMatches().size
        }

        class MatchCellItemHolder(private val binding: ItemBracketMatchBinding) : RecyclerView.ViewHolder(binding.root) {

            companion object {
                fun from(parent: ViewGroup) : MatchCellItemHolder {
                    return MatchCellItemHolder(ItemBracketMatchBinding.inflate(LayoutInflater.from(parent.context), parent, false))
                }
            }

            fun bind(mActivity: AppCompatActivity, matchData: MatchData){
                binding.match = matchData
                binding.pendingButton.setOnClickListener {
                    MatchResultAlertDialog(mActivity, matchData).show(mActivity.supportFragmentManager, MatchResultAlertDialog.ALERT_STRING)
                }
                binding.competitorTopLayout.competitorMatchResult.isSelected = true
                binding.competitorBottomLayout.competitorMatchResult.isSelected = true
            }

        }

    }
}