package com.stardemo.githubprofiles.ui.base

import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.stardemo.githubprofiles.R

abstract class BaseActivity : AppCompatActivity() {

    protected open fun observeViewModel(viewModel: BaseViewModel) {
        viewModel.showLoading.observe(this) {
            showLoading(it)
        }

        viewModel.onError.observe(this) {
            Toast.makeText(this, R.string.error_try_again, Toast.LENGTH_SHORT).show()
        }
    }

    open fun showLoading(show: Boolean) { }
}