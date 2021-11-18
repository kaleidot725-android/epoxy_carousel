package com.example.sample.view

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.example.sample.R
import com.google.android.material.tabs.TabLayoutMediator
import kotlin.math.atan

class LoopPageView(
    context: Context,
    attributeSet: AttributeSet
) : FrameLayout(context, attributeSet) {
    private val viewPager2: ViewPager2
    private val hiddenViewPager2: ViewPager2

    private var items: List<String> = emptyList()
    private var scrollChangeCallback: ViewPager2.OnPageChangeCallback? = null
    private var positionChangeCallback: ViewPager2.OnPageChangeCallback? = null

    init {
        View.inflate(context, R.layout.layout_loop_view, this)
        viewPager2 = findViewById(R.id.view_pager)
        viewPager2.isSaveEnabled = false

        hiddenViewPager2 = findViewById(R.id.hidden_view_pager)
        hiddenViewPager2.isSaveEnabled = false
    }

    fun setupData(
        items: List<String>,
        fragment: Fragment,
        onCreateFragment: (position: Int) -> Fragment,
        onChangedPosition: (position: Int) -> Unit
    ) {
        this.items = items
        setupViewPager2AndHiddenViewPager2(fragment, onCreateFragment, onChangedPosition)
    }

    fun clearData() {
        this.items = emptyList()
        clearViewPager2AndHiddenViewPager2()
    }

    fun attachIndicatorView(indicatorView: LoopPageIndicatorView) {
        indicatorView.nextButton.setOnClickListener { next() }
        indicatorView.backButton.setOnClickListener { back() }
        TabLayoutMediator(indicatorView.tabLayout, hiddenViewPager2) { _, _ -> }.apply { attach() }
    }

    fun next() {
        viewPager2.currentItem += 1
    }

    fun back() {
        viewPager2.currentItem -= 1
    }

    fun setPosition(position: Int, smoothScroll: Boolean) {
        viewPager2.setCurrentItem(position + 1, smoothScroll)
    }

    private fun setupViewPager2AndHiddenViewPager2(
        fragment: Fragment,
        onCreateFragment: (position: Int) -> Fragment,
        onChangedPosition: (position: Int) -> Unit
    ) {
        viewPager2.adapter = object : FragmentStateAdapter(fragment) {
            override fun getItemCount(): Int {
                val itemCount = items.count()
                return if (itemCount > 1) itemCount + 2 else itemCount
            }

            override fun createFragment(position: Int): Fragment {
                val itemCount = items.count()
                val loopPosition = (position + (itemCount - 1)) % itemCount
                return onCreateFragment(loopPosition)
            }
        }

        scrollChangeCallback = object : ViewPager2.OnPageChangeCallback() {
            override fun onPageScrollStateChanged(state: Int) {
                if (state == ViewPager2.SCROLL_STATE_IDLE) {
                    val currentPosition = viewPager2.currentItem
                    if (currentPosition < 1) {
                        viewPager2.setCurrentItem(items.count(), false)
                    } else if (currentPosition > items.count()) {
                        viewPager2.setCurrentItem(1, false)
                    }
                }
            }

            override fun onPageSelected(position: Int) {
                hiddenViewPager2.currentItem = position - 1
            }
        }
        viewPager2.registerOnPageChangeCallback(scrollChangeCallback!!)

        hiddenViewPager2.adapter = object : FragmentStateAdapter(fragment) {
            override fun getItemCount(): Int {
                return items.count()
            }

            override fun createFragment(position: Int): Fragment {
                return Fragment()
            }
        }

        positionChangeCallback = object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                viewPager2.currentItem = position + 1
                onChangedPosition(position)
            }
        }
        hiddenViewPager2.registerOnPageChangeCallback(positionChangeCallback!!)

        viewPager2.setCurrentItem(1, false)
    }

    private fun clearViewPager2AndHiddenViewPager2() {
        viewPager2.adapter = null
        scrollChangeCallback?.let { this.viewPager2.unregisterOnPageChangeCallback(it) }

        hiddenViewPager2.adapter = null
        positionChangeCallback?.let { this.viewPager2.unregisterOnPageChangeCallback(it) }
    }
}
