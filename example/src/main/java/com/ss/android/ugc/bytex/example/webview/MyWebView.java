package com.ss.android.ugc.bytex.example.webview;

import android.content.Context;
import android.util.AttributeSet;
import android.webkit.WebView;

/**
 * @author ZhuPeipei
 * @date 2022/1/28 21:45
 */
public class MyWebView extends WebView {
    private String mName;

    public MyWebView(Context context) {
        super(context);
    }

    public MyWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public MyWebView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public MyWebView(Context context, AttributeSet attrs, int defStyleAttr, boolean privateBrowsing) {
        super(context, attrs, defStyleAttr, privateBrowsing);
    }

    public MyWebView(Context context, AttributeSet attrs, int defStyleAttr, String myName) {
        super(context, attrs, defStyleAttr);
        this.mName = myName;
    }

}
