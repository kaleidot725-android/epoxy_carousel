package com.example.sample.controller

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2

class LoopViewPagerController(
    val fragment: Fragment,
    val viewPager2: ViewPager2,
    val hiddenViewPager2: ViewPager2,
    val onCreateFragment: (position: Int) -> Fragment,
    val onChangedPosition: (position: Int) -> Unit
) {
    private var items: List<String> = emptyList()
    private var scrollChangeCallback: ViewPager2.OnPageChangeCallback? = null
    private var positionChangeCallback: ViewPager2.OnPageChangeCallback? = null

    fun setData(items: List<String>) {
        this.items = items
        clear()
        setup()
    }

    fun next() {
        viewPager2.currentItem += 1
    }

    fun back() {
        viewPager2.currentItem -= 1
    }

    private fun setup() {
        viewPager2.adapter = object : FragmentStateAdapter(fragment) {
            override fun getItemCount(): Int {
                val itemCount = items.count()
                return if (itemCount > 1) { itemCount + 2 } else { itemCount }
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

    private fun clear() {
        viewPager2.adapter = null
        scrollChangeCallback?.let { this.viewPager2.unregisterOnPageChangeCallback(it) }

        hiddenViewPager2.adapter = null
        positionChangeCallback?.let { this.viewPager2.unregisterOnPageChangeCallback(it) }
    }
}