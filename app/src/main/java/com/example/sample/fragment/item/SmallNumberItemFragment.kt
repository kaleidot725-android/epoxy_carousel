package com.example.sample.fragment.item

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import com.example.sample.R

class SmallNumberItemFragment : Fragment(R.layout.fragment_small_number_item) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val position = getBundle(this)
        val textView = view.findViewById<TextView>(R.id.text_view)
        textView.text = "Position $position"
    }

    companion object {
        fun create(position: Int): Fragment {
            return SmallNumberItemFragment().apply {
                arguments = bundleOf("position" to position)
            }
        }

        private fun getBundle(fragment: Fragment): Int {
            return fragment.arguments?.getInt("position") ?: 0
        }
    }
}