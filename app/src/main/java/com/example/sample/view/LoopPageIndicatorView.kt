package com.example.sample.view

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.ImageButton
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.sample.R
import com.google.android.material.tabs.TabLayout

class LoopPageIndicatorView(
    context: Context,
    attributeSet: AttributeSet
) : ConstraintLayout(context, attributeSet) {
    val nextButton : ImageButton
    val backButton : ImageButton
    val tabLayout: TabLayout

    init {
        View.inflate(context, R.layout.layout_indicator_view, this)
        nextButton = findViewById(R.id.next_button)
        backButton = findViewById(R.id.back_button)
        tabLayout = findViewById(R.id.tab_layout)
    }
}