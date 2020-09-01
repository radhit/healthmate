package com.healthmate.common.adapter

import android.app.Activity
import android.graphics.Rect
import android.util.TypedValue
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class GridSpacingItemDecoration(
    private val spanCount: Int,
    private val activity: Activity,
    private val spacing: Int,
    private val includeEdge: Boolean
) :
    RecyclerView.ItemDecoration() {

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        val spacingx = dpToPx(activity,spacing)
        val position = parent.getChildAdapterPosition(view)
        val column = position % this.spanCount
        if (this.includeEdge) {
            outRect.left = spacingx - column * spacingx / this.spanCount
            outRect.right = (column + 1) * spacingx / this.spanCount
            if (position < this.spanCount) {
                outRect.top = spacingx
            }

            outRect.bottom = spacingx
        } else {
            outRect.left = column * spacingx / this.spanCount
            outRect.right = spacingx - (column + 1) * spacingx / this.spanCount
            if (position >= this.spanCount) {
                outRect.top = spacingx
            }
        }
    }

    fun dpToPx(activity: Activity, dp: Int): Int{
        val r = activity.resources
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp.toFloat(), r.displayMetrics))
    }
}