package com.xmly.ting.android.webview;

import com.android.build.gradle.AppExtension;
import com.ss.android.ugc.bytex.common.CommonPlugin;
import com.ss.android.ugc.bytex.common.visitor.ClassVisitorChain;
import com.ss.android.ugc.bytex.pluginconfig.anno.PluginConfig;
import com.xmly.ting.android.webview.visitors.WebViewClassVisitor;

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
        if (context != null && !context.isWhiteList(relativePath) && context.extension != null && context.extension.getReplaceWebViewClazz() != null) {
            // 这样会不会效率很低
            if (!relativePath.equals(context.extension.getReplaceWebViewClazz() + ".class")) {
                chain.connect(new WebViewClassVisitor(context));
            }
        }
        return super.transform(relativePath, chain);
    }

}
