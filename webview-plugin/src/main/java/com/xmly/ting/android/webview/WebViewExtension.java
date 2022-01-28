package com.xmly.ting.android.webview;

import com.custom_base_bytex.common.BaseExtensionWithWhiteList;

/**
 * @author ZhuPeipei
 * @date 2021/11/16 17:41
 */
public class WebViewExtension extends BaseExtensionWithWhiteList {
    private String replaceWebViewClazz;

    @Override
    public String getName() {
        return "webviewExtension";
    }

    public String getReplaceWebViewClazz() {
        return replaceWebViewClazz;
    }

    public void setReplaceWebViewClazz(String replaceWebViewClazz) {
        this.replaceWebViewClazz = replaceWebViewClazz;
    }
}
