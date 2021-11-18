package com.example.sample

import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.example.sample.controller.LoopViewPagerController
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class ViewPagerFragment : Fragment(R.layout.fragment_view_pager) {
    private lateinit var controller : LoopViewPagerController

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val viewPager = view.findViewById<ViewPager2>(R.id.view_pager)
        val hiddenViewPager = view.findViewById<ViewPager2>(R.id.hidden_view_pager)
        controller = LoopViewPagerController(
            fragment = this,
            viewPager2 = viewPager,
            hiddenViewPager2 = hiddenViewPager,
            onCreateFragment = { position ->  ViewPagerItemFragment.create(position) },
            onChangedPosition = { position -> /** Position Changed */ }
        )

        controller.setData(
            listOf("A", "B", "C", "D", "E", "F", "G")
        )

        val nextButton = view.findViewById<ImageButton>(R.id.next_button)
        nextButton.setOnClickListener {
            controller.next()
        }

        val backButton = view.findViewById<ImageButton>(R.id.back_button)
        backButton.setOnClickListener {
            controller.back()
        }

        val tabLayout = view.findViewById<TabLayout>(R.id.tab_layout)
        TabLayoutMediator(tabLayout, hiddenViewPager) { _, _ -> }.attach()
    }
}
