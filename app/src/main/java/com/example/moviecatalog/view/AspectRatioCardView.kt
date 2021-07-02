package com.example.moviecatalog.view

import android.content.Context
import android.util.AttributeSet
import androidx.cardview.widget.CardView
import com.example.moviecatalog.R

/**
 * Created by Iftekhar on 5/16/18.
 */
open class AspectRatioCardView : CardView {

    var aspectRatioX: Int
    var aspectRatioY: Int

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        val a = context.theme.obtainStyledAttributes(attrs, R.styleable.AspectRatioViewGroup, 0, 0)
        try {
            aspectRatioX = a.getInt(R.styleable.AspectRatioViewGroup_aspectX, -1)
            aspectRatioY = a.getInt(R.styleable.AspectRatioViewGroup_aspectY, -1)
        } finally {
            a.recycle()
        }
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : this(context, attrs)

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        if (aspectRatioX > 0 && aspectRatioY > 0) {
            var height = MeasureSpec.getSize(heightMeasureSpec)
            var width = MeasureSpec.getSize(widthMeasureSpec)
            val aspectRatio = aspectRatioX.toDouble() / aspectRatioY.toDouble()
            if (width > height * aspectRatio) {
                width = (height * aspectRatio).toInt()
            } else {
                height = (width / aspectRatio).toInt()
            }
            val measuredWidth = MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY)
            val measuredHeight = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY)
            super.onMeasure(measuredWidth, measuredHeight)
        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        }
    }

    fun setAspectRatio(aspectX: Int, aspectY: Int) {
        aspectRatioX = aspectX
        aspectRatioY = aspectY
        requestLayout()
    }
}