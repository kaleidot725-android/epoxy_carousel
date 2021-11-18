package com.example.sample

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.sample.view.LoopPageIndicatorView
import com.example.sample.view.LoopPageView

class ViewPagerFragment : Fragment(R.layout.fragment_view_pager) {
    private lateinit var loopPageView: LoopPageView
    private lateinit var loopPageIndicatorView: LoopPageIndicatorView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loopPageView = view.findViewById<LoopPageView>(R.id.loop_page_view)
        loopPageIndicatorView = view.findViewById<LoopPageIndicatorView>(
            R.id.loop_page_indicator_view
        )

        loopPageView.setupData(
            items = listOf("A", "B", "C", "D", "E", "F", "G"),
            fragment = this,
            onCreateFragment = { position -> ViewPagerItemFragment.create(position) },
            onChangedPosition = { position ->
                Toast.makeText(context, "No ${position}", Toast.LENGTH_SHORT).show()
            }
        )
        loopPageView.attachIndicatorView(indicatorView = loopPageIndicatorView)
        loopPageView.setPosition(4, false)
    }
}
