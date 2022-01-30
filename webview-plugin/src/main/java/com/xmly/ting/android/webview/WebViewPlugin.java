package com.xmly.ting.android.webview;

import com.android.build.gradle.AppExtension;
import com.ss.android.ugc.bytex.common.CommonPlugin;
import com.ss.android.ugc.bytex.common.visitor.ClassVisitorChain;
import com.ss.android.ugc.bytex.pluginconfig.anno.PluginConfig;
import com.xmly.ting.android.webview.visitors.WebViewClassVisitor;
import com.xmly.ting.android.webview.visitors.WebViewClassVisitor2;

import org.gradle.api.Project;

import javax.annotation.Nonnull;

/**
 * @author ZhuPeipei
 * @date 2021/11/12 17:44
 */
@PluginConfig(value = "webview-plugin")
public class WebViewPlugin extends CommonPlugin<WebViewExtension, WebViewContext> {
    @Override
    protected WebViewContext getContext(Project project, AppExtension android, WebViewExtension extension) {
        return new WebViewContext(project, android, extension);
    }

    @Override
    public boolean transform(@Nonnull String relativePath, @Nonnull ClassVisitorChain chain) {
        if (context != null && !context.isWhiteList(relativePath) && context.extension != null) {
            // 1. webclient的替换
            if (context.extension.getReplaceWebViewClazz() != null && !relativePath.equals(context.extension.getReplaceWebViewClazz() + ".class")) {
                chain.connect(new WebViewClassVisitor(context));
            }
            // 2. webview的处理
            chain.connect(new WebViewClassVisitor2(context));
        }
        return super.transform(relativePath, chain);
    }

}
