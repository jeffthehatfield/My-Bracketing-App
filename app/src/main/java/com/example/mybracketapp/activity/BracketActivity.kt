package com.example.mybracketapp.activity

import android.os.Bundle
import android.util.Log
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import com.example.mybracketapp.DataCache
import com.example.mybracketapp.R
import com.example.mybracketapp.SLApplication
import com.example.mybracketapp.databinding.ActivityBracketBinding
import com.example.mybracketapp.model.BucketData
import com.example.mybracketapp.view.bracket.BracketFragment
import com.example.mybracketapp.viewmodel.BracketMatchesViewModel


class BracketActivity: BaseActivity<ActivityBracketBinding, BracketMatchesViewModel>() {

    companion object {
        const val INTENT_BUCKET_INDEX = "Bucket"
    }

    lateinit var bucketData: BucketData

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        SLApplication.setContext(applicationContext)
        mDataBinding = DataBindingUtil.setContentView(this, R.layout.activity_bracket)
        mViewModel = ViewModelProvider(this).get(BracketMatchesViewModel::class.java)

        bucketData = DataCache.getInstance().nonEmptyBucketData[intent?.extras?.getInt(INTENT_BUCKET_INDEX)!!]
        Log.d("Jeff", "BracketFragment onCreateView $bucketData")

        val bracketFragment = BracketFragment(this, bucketData)
        val fragments = supportFragmentManager.fragments
        val manager: FragmentManager = if (fragments.size != 0) fragments[0].childFragmentManager else supportFragmentManager
        val transaction = manager.beginTransaction()
        transaction.replace(mDataBinding.bracketContainer.id, bracketFragment, "brackets_home_fragment")
        manager.executePendingTransactions()
        transaction.commit()

    }
}