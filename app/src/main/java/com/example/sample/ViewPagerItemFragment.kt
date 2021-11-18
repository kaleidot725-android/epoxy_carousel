package com.example.sample

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment

class ViewPagerItemFragment : Fragment(R.layout.fragment_view_pager_item) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val position = getBundle(this)
        val textView = view.findViewById<TextView>(R.id.text_view)
        textView.text = "Position $position"
    }

    companion object {
        fun create(position: Int): Fragment {
            return ViewPagerItemFragment().apply {
                arguments = bundleOf("position" to position)
            }
        }

        private fun getBundle(fragment: Fragment): Int {
            return fragment.arguments?.getInt("position") ?: 0
        }
    }
}