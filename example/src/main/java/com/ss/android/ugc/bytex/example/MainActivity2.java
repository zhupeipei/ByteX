package com.ss.android.ugc.bytex.example;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;

import org.aire.OtherWebViewClient;

public class MainActivity2 extends AppCompatActivity {
    private WebView mWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        mWebView = findViewById(R.id.main_webview);

        mWebView.setWebViewClient(new OtherWebViewClient());
        mWebView.loadUrl("chrome://crash");
    }
}