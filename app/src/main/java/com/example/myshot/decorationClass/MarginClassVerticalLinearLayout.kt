package com.example.myshot.decorationClass

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class MarginClassVerticalLinearLayout(
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
                top = t
            }
            left =l
            right = r
            bottom = b
        }
    }

}