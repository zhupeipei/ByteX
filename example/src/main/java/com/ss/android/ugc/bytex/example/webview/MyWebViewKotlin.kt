package com.ss.android.ugc.bytex.example.webview

import android.content.Context
import android.util.AttributeSet
import android.webkit.WebView

/**
 * @author ZhuPeipei
 * @date 2022/1/28 21:46
 */
class MyWebViewKotlin : WebView {
    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    constructor(
        context: Context?,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        privateBrowsing: Boolean
    ) : super(context, attrs, defStyleAttr, privateBrowsing)
}