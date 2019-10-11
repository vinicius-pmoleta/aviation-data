package com.aviationdata.common.core.view

import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Build
import android.provider.Settings
import android.util.AttributeSet
import android.widget.ProgressBar

class ProgressView : ProgressBar {

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    override fun setIndeterminateDrawable(drawable: Drawable) {
        super.setIndeterminateDrawable(if (hideIndeterminateDrawable()) null else drawable)
    }

    private fun hideIndeterminateDrawable(): Boolean {
        val animationSetting = Settings.Global.getFloat(
            context.contentResolver,
            Settings.Global.ANIMATOR_DURATION_SCALE,
            1f
        )
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && animationSetting == 0f
    }

}