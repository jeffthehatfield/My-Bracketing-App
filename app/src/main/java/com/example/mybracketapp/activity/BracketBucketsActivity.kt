package com.example.mybracketapp.activity

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.mybracketapp.DataCache
import com.example.mybracketapp.R
import com.example.mybracketapp.SLApplication
import com.example.mybracketapp.databinding.ActivityBracketBucketsBinding
import com.example.mybracketapp.databinding.ItemBracketBucketBinding
import com.example.mybracketapp.model.CompetitorsJson
import com.example.mybracketapp.viewmodel.BracketBucketViewModel
import com.google.gson.Gson

class BracketBucketsActivity : BaseActivity<ActivityBracketBucketsBinding, BracketBucketViewModel>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        SLApplication.setContext(applicationContext)
        mDataBinding = DataBindingUtil.setContentView(this, R.layout.activity_bracket_buckets)
        mViewModel = ViewModelProvider(this).get(BracketBucketViewModel::class.java)
        setSupportActionBar(findViewById(R.id.toolbar))

        DataCache.getInstance().setCompetitors(
            Gson().fromJson(resources.openRawResource(R.raw.competitors).bufferedReader().use { it.readText() },
                CompetitorsJson::class.java).competitors)
        DataCache.getInstance().generateBuckets()

        mDataBinding.bucketRecyclerView.adapter = BracketBucketAdapter(this)

    }

    class BracketBucketAdapter(private val mActivity: AppCompatActivity)
        : RecyclerView.Adapter<BracketBucketAdapter.BucketItemViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BucketItemViewHolder {
            return BucketItemViewHolder.from(mActivity, parent)
        }

        override fun onBindViewHolder(holder: BucketItemViewHolder, position: Int) {
            holder.bind(position)
        }

        override fun getItemCount(): Int {
            return DataCache.getInstance().nonEmptyBucketData.size
        }

        class BucketItemViewHolder(private val mActivity: AppCompatActivity,
                                   private val binding: ItemBracketBucketBinding)
            : RecyclerView.ViewHolder(binding.root) {

            companion object {
                fun from(mActivity: AppCompatActivity, parent: ViewGroup) : BucketItemViewHolder {
                    return BucketItemViewHolder(mActivity, ItemBracketBucketBinding.inflate(LayoutInflater.from(parent.context), parent, false))
                }
            }

            fun bind(position: Int){
                binding.bucket = DataCache.getInstance().nonEmptyBucketData[position]
                binding.blueLayout.setOnClickListener {
                    val intent = Intent(mActivity, BracketActivity::class.java)
                    val bundle = Bundle()
                    bundle.putInt(BracketActivity.INTENT_BUCKET_INDEX, position)
                    intent.putExtras(bundle)
                    mActivity.startActivity(intent)
                }
            }

        }
    }
}