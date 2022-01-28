package com.xmly.ting.android.webview.visitors;

import com.ss.android.ugc.bytex.common.visitor.BaseClassVisitor;
import com.xmly.ting.android.webview.WebViewConstants;
import com.xmly.ting.android.webview.WebViewContext;

import org.objectweb.asm.MethodVisitor;

/**
 * @author ZhuPeipei
 * @date 2021/11/12 17:53
 */
public class WebViewClassVisitor extends BaseClassVisitor {
    private WebViewContext mContext;
    private String mReplaceClazz;

    private String mClazzName;

    public WebViewClassVisitor(WebViewContext context) {
        mContext = context;
        mReplaceClazz = context.extension.getReplaceWebViewClazz();
    }

    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        mClazzName = name;
        if (mReplaceClazz != null && !mClazzName.equals(mReplaceClazz)
                && superName != null
                && superName.equals(WebViewConstants.DEFAULT_WEBVIEW)) {
            superName = mReplaceClazz;
            mContext.getLogger().i(mContext.extension.getName(), "extends class replace: className: " + mClazzName);
        }
        super.visit(version, access, name, signature, superName, interfaces);
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
        MethodVisitor mv = super.visitMethod(access, name, descriptor, signature, exceptions);
        return new WebViewMethodVisitor(mv, mContext, mClazzName, name, descriptor);
    }
}
