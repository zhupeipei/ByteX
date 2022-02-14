package com.ss.android.ugc.bytex.example;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewStub;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;

import com.ss.android.ugc.bytex.example.webview.ChildChildWebView;
import com.ss.android.ugc.bytex.example.webview.MyWebView;
import com.ss.android.ugc.bytex.example.webview.MyWebViewKotlin;
import com.ss.android.ugc.bytex.example.webview.WebViewAutoSetWebClient;

import org.aire.FixWebViewClient;
import org.aire.OtherWebViewClient;

public class MainActivity2 extends AppCompatActivity {
    private FrameLayout mFl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        mFl = findViewById(R.id.webview_fl);
    }

    @Override
    protected void onResume() {
        super.onResume();

        WebViewClient client = new WebViewClient();
        new WebView(this).setWebViewClient(client);

        WebViewClient client1 = new FixWebViewClient();
        new WebView(this).setWebViewClient(client1);

//        WebViewClient client2 = new MyWebViewClient();
//        new WebView(this).setWebViewClient(client2);
    }

    // webview 已经设置原生webclient
    public void webview1(View view) {
        WebView webView = new ChildChildWebView(this);
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl("chrome://crash");
        mFl.addView(webView, lp);
    }

    // webview 已经设置继承的webclient
    public void webview2(View view) {
        WebView webView = new WebView(this);
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
        webView.setWebViewClient(new OtherWebViewClient());
        webView.loadUrl("chrome://crash");
        mFl.addView(webView, lp);
    }

    // webview new出来没有设置webclient
    public void webview3(View view) {
        webview11(view);

        WebView webView = new ChildChildWebView(this);
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
        mFl.addView(webView, lp);

        webView.loadUrl("chrome://crash");
        webview9(view);
        webview10(view);
    }

    // webview new出来（不是原生webview）没有设置webclient
    public void webview4(View view) {
        WebView webView = new MyWebView(this);
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
        webView.loadUrl("chrome://crash");
        mFl.addView(webView, lp);
    }

    // webview 通过findViewById 没有设置webclient
    public void webview5(View view) {
        mFl.removeAllViews();
        LayoutInflater.from(this).inflate(R.layout.webview_layout, mFl, true);
        WebView webView = findViewById(R.id.webview_webview);
        webView.loadUrl("chrome://crash");
    }

    // webview 通过findViewById 没有设置webclient 继承的webview
    public void webview6(View view) {
        mFl.removeAllViews();
        LayoutInflater.from(this).inflate(R.layout.webview_layout_my, mFl, true);
        WebView webView = findViewById(R.id.webview_webview);
        webView.loadUrl("chrome://crash");
    }

    // kotlin中的webview
    public void webview7(View view) {
        WebView webView = new MyWebViewKotlin(this);
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
        webView.loadUrl("chrome://crash");
        mFl.addView(webView, lp);
    }

    // kotlin中的webview 2
    public void webview8(View view) {
        mFl.removeAllViews();
        LayoutInflater.from(this).inflate(R.layout.webview_layout_kotlin, mFl, true);
        WebView webView = findViewById(R.id.webview_webview);
        webView.loadUrl("chrome://crash");
    }

    // new WebView没有引用
    public void webview9(View view) {
        new WebView(this);
    }

    public void webview10(View view) {
        WebView webview = new ChildChildWebView(this);
    }

    public void webview11(View view) {
        WebView webview = new MyWebView(this);
        WebViewAutoSetWebClient.setWebClient(webview);
    }

    public void webview12(View view) {
        mFl.removeAllViews();
        LayoutInflater.from(this).inflate(R.layout.webview_layout_my_viewstub, mFl, true);
        final WebView webView = findViewById(R.id.webview_webview);

        Log.d("haha", "MainActivity2 onCreate1: ");

        Log.e("haha", "webview12: ");
        ViewStub vs = findViewById(R.id.webview_viewstub);
        View vsView = vs.inflate();
        WebView v = vsView.findViewById(R.id.webview_webview);

        webView.postDelayed(new Runnable() {
            @Override
            public void run() {
                webView.loadUrl("chrome://crash");
            }
        }, 5000);
    }

}