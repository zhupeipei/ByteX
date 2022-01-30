package com.xmly.ting.android.webview.visitors;

import com.android.annotations.NonNull;
import com.ss.android.ugc.bytex.common.Constants;
import com.xmly.ting.android.webview.WebViewContext;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import java.util.HashSet;
import java.util.Set;

/**
 * @author ZhuPeipei
 * @date 2022/1/29 13:43
 */
public class WebViewMethodVisitor2 extends MethodVisitor {
    @NonNull
    private WebViewContext mContext;

    private boolean mIsChildWebView; // 是否是webview的子类
    private String mName;
    private String mDesc;
    private String mClazzName;

    final static String TO_CLASS_NAME = "com/ss/android/ugc/bytex/example/webview/WebViewAutoSetWebClient";

    final static Set<String> DESCRIPTOR = new HashSet() {
        {
            add("(Landroid/content/Context;)V");
            add("(Landroid/content/Context;Landroid/util/AttributeSet;)V");
            add("(Landroid/content/Context;Landroid/util/AttributeSet;I)V");
            add("(Landroid/content/Context;Landroid/util/AttributeSet;II)V");
            add("(Landroid/content/Context;Landroid/util/AttributeSet;IZ)V");
        }
    };

    public WebViewMethodVisitor2(MethodVisitor methodVisitor, @NonNull WebViewContext context, String clazzName, String name, String desc, boolean childWebView) {
        super(Constants.ASM_API, methodVisitor);
        this.mContext = context;
        this.mClazzName = clazzName;
        this.mName = name;
        this.mDesc = desc;
        this.mIsChildWebView = childWebView;
    }

    @Override
    public void visitMethodInsn(int opcode, String owner, String name, String descriptor, boolean isInterface) {
        super.visitMethodInsn(opcode, owner, name, descriptor, isInterface);
        if (opcode == Opcodes.INVOKESPECIAL && "android/webkit/WebView".equals(owner) && DESCRIPTOR.contains(descriptor)) {
            if (mIsChildWebView) {
                mv.visitVarInsn(Opcodes.ALOAD, 0);
                mv.visitMethodInsn(Opcodes.INVOKESTATIC, TO_CLASS_NAME, "setWebClient", "(Landroid/webkit/WebView;)V", false);
            } else {
                mv.visitInsn(Opcodes.DUP);
                mv.visitMethodInsn(Opcodes.INVOKESTATIC, TO_CLASS_NAME, "setWebClient", "(Landroid/webkit/WebView;)V", false);
            }
            mContext.getLogger().d(mContext.extension.getName() + "-webview",
                    "clazzName: " + mClazzName + ", name: " + this.mName + ", desc: " + this.mDesc
                            + ", isChildWebView: " + mIsChildWebView
                            + ", owner: " + owner + ", name: " + name
                            + ", desc: " + descriptor + ", isInterface: " + isInterface);
        }
    }
}
