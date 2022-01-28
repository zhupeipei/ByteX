package com.xmly.ting.android.webview;

import com.android.build.gradle.AppExtension;
import com.custom_base_bytex.common.BaseContextWhitWhiteList;

import org.gradle.api.Project;

/**
 * @author ZhuPeipei
 * @date 2021/11/12 17:47
 */
public class WebViewContext extends BaseContextWhitWhiteList<WebViewExtension> {

    public WebViewContext(Project project, AppExtension android, WebViewExtension extension) {
        super(project, android, extension);
    }
}
