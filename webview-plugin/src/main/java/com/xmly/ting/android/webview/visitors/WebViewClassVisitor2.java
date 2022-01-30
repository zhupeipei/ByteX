package com.xmly.ting.android.webview.visitors;

import com.ss.android.ugc.bytex.common.visitor.BaseClassVisitor;
import com.xmly.ting.android.webview.WebViewContext;

import org.objectweb.asm.MethodVisitor;

/**
 * @author ZhuPeipei
 * @date 2022/1/29 11:00
 * 对webview的处理
 * 1. webview替换有两种，1是替换new WebView()，2是替换new OtherWebView()，替换构造方法
 */
public class WebViewClassVisitor2 extends BaseClassVisitor {
    private WebViewContext mContext;

    private String mClazzName;
    private boolean mIsChildWebView; // 是否是webview的子类

    public WebViewClassVisitor2(WebViewContext context) {
        mContext = context;
    }

    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        mClazzName = name;
        // 这里可能出现这个webview被嵌套了好几层，如 A ex B, B ex C, C ex WebView，那么这里只处理C
        mIsChildWebView = "android/webkit/WebView".equals(superName);
        super.visit(version, access, name, signature, superName, interfaces);
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
        MethodVisitor mv = super.visitMethod(access, name, descriptor, signature, exceptions);
        return new WebViewMethodVisitor2(mv, mContext, mClazzName, name, descriptor, mIsChildWebView);
    }
}
