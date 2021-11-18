package com.example.sample.fragment

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.sample.R
import com.example.sample.fragment.item.BigNumberItemFragment
import com.example.sample.fragment.item.SmallNumberItemFragment
import com.example.sample.view.LoopPageIndicatorView
import com.example.sample.view.LoopPageView

class LoopPageFragment : Fragment(R.layout.fragment_loop_page) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val loopPageView = view.findViewById<LoopPageView>(R.id.loop_page_view)
        val loopPageIndicatorView = view.findViewById<LoopPageIndicatorView>(
            R.id.loop_page_indicator_view
        )

        loopPageView.setupData(
            items = listOf("A", "B", "C", "D", "E", "F", "G"),
            fragment = this,
            onCreateFragment = { position ->
                if (position % 2 == 0) {
                    SmallNumberItemFragment.create(position)
                } else {
                    BigNumberItemFragment.create(position)
                }
            },
            onChangedPosition = { position ->
                Toast.makeText(context, "No ${position}", Toast.LENGTH_SHORT).show()
            }
        )
        loopPageView.attachIndicatorView(indicatorView = loopPageIndicatorView)
        loopPageView.setPosition(4, false)
    }
}
