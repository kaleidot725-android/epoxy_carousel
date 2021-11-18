package com.example.sample.view

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.example.sample.R
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

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
        hiddenViewPager2 = findViewById(R.id.hidden_view_pager)
    }

    fun setupData(
        items: List<String>,
        fragment: Fragment,
        onCreateFragment: (position: Int) -> Fragment,
        onChangedPosition: (position: Int) -> Unit
    ) {
        this.items = items
        setupViewPager2(fragment, onCreateFragment)
        setupHiddenViewPager2(fragment, onChangedPosition)
    }

    fun clearData() {
        clearViewPager2()
        clearHiddenViewPager2()
    }

    fun attachIndicatorView(indicatorView: LoopPageIndicatorView) {
        indicatorView.nextButton.setOnClickListener { next() }
        indicatorView.backButton.setOnClickListener { back() }
        TabLayoutMediator(indicatorView.tabLayout, hiddenViewPager2) { _, _ -> }.attach()
    }

    fun next() {
        viewPager2.currentItem += 1
    }

    fun back() {
        viewPager2.currentItem -= 1
    }

    fun setPosition(position: Int) {
        viewPager2.currentItem = position
    }

    private fun setupViewPager2(fragment: Fragment, onCreateFragment: (position: Int) -> Fragment) {
        viewPager2.adapter = object : FragmentStateAdapter(fragment) {
            override fun getItemCount(): Int {
                val itemCount = items.count()
                return if (itemCount > 1) {
                    itemCount + 2
                } else {
                    itemCount
                }
            }

            override fun createFragment(position: Int): Fragment {
                val itemCount = items.count()
                val newPosition = (position + (itemCount - 1)) % itemCount
                return onCreateFragment(newPosition)
            }
        }

        scrollChangeCallback = object : ViewPager2.OnPageChangeCallback() {
            override fun onPageScrollStateChanged(state: Int) {
                super.onPageScrollStateChanged(state)
                if (state == ViewPager2.SCROLL_STATE_IDLE) {
                    val currentPosition = viewPager2.currentItem
                    if (currentPosition < 1) {
                        viewPager2.setCurrentItem(items.count(), false)
                    } else if (currentPosition > items.count()) {
                        viewPager2.setCurrentItem(1, false)
                    }
                    hiddenViewPager2.currentItem = viewPager2.currentItem - 1
                }
            }
        }
        viewPager2.registerOnPageChangeCallback(scrollChangeCallback!!)

        viewPager2.setCurrentItem(1, false)
    }

    private fun clearViewPager2() {
        viewPager2.adapter = null
        scrollChangeCallback?.let { this.viewPager2.unregisterOnPageChangeCallback(it) }
    }

    private fun setupHiddenViewPager2(fragment: Fragment, onChangedPosition: (position: Int) -> Unit) {
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
    }

    private fun clearHiddenViewPager2() {
        hiddenViewPager2.adapter = null
        positionChangeCallback?.let { this.viewPager2.unregisterOnPageChangeCallback(it) }
    }
}
