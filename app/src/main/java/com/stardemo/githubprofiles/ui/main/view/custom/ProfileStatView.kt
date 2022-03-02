package com.stardemo.githubprofiles.ui.main.view.custom

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.stardemo.githubprofiles.R
import com.stardemo.githubprofiles.databinding.PartialProfileStatBinding

class ProfileStatView : LinearLayout {
    private val binding =
        PartialProfileStatBinding.inflate(LayoutInflater.from(context), this, true)

    var statName: String = ""
        set(value) {
            field = value
            binding.tvStatName.text = value
        }

    var statValue: String = ""
        set(value) {
            field = value
            binding.tvStatValue.text = value
        }

    constructor(context: Context) : super(context) {
        initLayout(null)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        initLayout(attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        initLayout(attrs)
    }

    private fun initLayout(attrs: AttributeSet?) {
        attrs?.run {
            val styledAttrs =
                context.obtainStyledAttributes(attrs, R.styleable.ProfileStatView)
            statName = styledAttrs.getString(R.styleable.ProfileStatView_statName) ?: ""
            statValue = styledAttrs.getString(R.styleable.ProfileStatView_statValue) ?: ""
            styledAttrs.recycle()
        }
    }
}