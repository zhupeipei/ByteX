package com.ss.android.ugc.bytex.example.webview;

import android.os.Build;
import android.text.TextUtils;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import org.aire.FixWebViewClient;

import java.util.Locale;

/**
 * @author ZhuPeipei
 * @date 2022/1/29 16:07
 */
public class WebViewAutoSetWebClient {
    public static void setWebClient(WebView webView) {
        if (webView == null) {
            return;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // 这里 webview可能会自动设置webview client
            WebViewClient webClient = webView.getWebViewClient();
            if (webClient == null || webClient.getClass() == WebViewClient.class) {
                webView.setWebViewClient(new FixWebViewClient());
            }
        } else {
            // 可以考虑反射
        }
    }

    private static Boolean isVivoDevice;

    public static boolean isVivoDevice() {
        if (isVivoDevice != null) {
            return isVivoDevice;
        }
        String manufacturer = Build.MANUFACTURER;
        isVivoDevice = !TextUtils.isEmpty(manufacturer)
                && ("vivo".equalsIgnoreCase(manufacturer) || manufacturer.toLowerCase(Locale.CHINA).contains("vivo"));
        return isVivoDevice;
    }
}
