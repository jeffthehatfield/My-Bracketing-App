package com.example.mybracketapp.activity

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment

abstract class BaseFragment<DB : ViewDataBinding> : Fragment() {

    protected lateinit var mDataBinding: DB

    protected open fun initView(inflater: LayoutInflater, container: ViewGroup?, @LayoutRes layoutResourceId: Int): View? {
        mDataBinding = DataBindingUtil.inflate(inflater, layoutResourceId, container, false)
        initViewModels()
        initDataBindings()
        registerObservers()
        return mDataBinding.root
    }

    protected open fun initViewModels() {}

    protected open fun initDataBindings() {}

    protected open fun registerObservers() {}
}