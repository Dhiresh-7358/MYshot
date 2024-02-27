package com.example.myshot.decorationClass

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class MarginClassHorizontalLinearLayout(
    private val t: Int,
    private val l: Int,
    private val r: Int,
    private val b: Int
) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        with(outRect) {
            if (parent.getChildAdapterPosition(view) == 0) {
                left = l
            }
            top = t
            right = r
            bottom = b
        }
    }

}