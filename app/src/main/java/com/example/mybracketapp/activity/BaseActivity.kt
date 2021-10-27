package com.example.mybracketapp.activity

import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModel

abstract class BaseActivity<DB : ViewDataBinding, VM : ViewModel> : AppCompatActivity() {

    protected lateinit var mDataBinding: DB
    protected lateinit var mViewModel: VM
}