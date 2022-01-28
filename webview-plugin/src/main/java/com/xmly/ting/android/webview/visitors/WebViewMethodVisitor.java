package com.xmly.ting.android.webview.visitors;

import com.android.annotations.NonNull;
import com.ss.android.ugc.bytex.common.Constants;
import com.xmly.ting.android.webview.WebViewConstants;
import com.xmly.ting.android.webview.WebViewContext;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

/**
 * @author ZhuPeipei
 * @date 2021/11/17 14:53
 */
public class WebViewMethodVisitor extends MethodVisitor {
    @NonNull
    private WebViewContext mContext;
    private String mReplaceClazz;

    private String mOwner;
    private String mName;
    private String mDesc;

    public WebViewMethodVisitor(MethodVisitor methodVisitor, @NonNull WebViewContext context, String className, String name, String desc) {
        super(Constants.ASM_API, methodVisitor);
        this.mContext = context;
        this.mReplaceClazz = context.extension.getReplaceWebViewClazz();
        this.mOwner = className;
        this.mName = name;
        this.mDesc = desc;
    }

    @Override
    public void visitMethodInsn(int opcode, String owner, String name, String descriptor, boolean isInterface) {
        // 触发 webview init方法
        // 这里会有bug 先不搞了
//        if (opcode == Opcodes.INVOKESPECIAL && !this.owner.contains(WebViewConstants.DEFAULT_WEBVIEW)) {
//            if (owner.equals("android/webkit/WebViewClient") && name.equals("<init>") && descriptor.equals("()V")) {
//                System.out.println("qqqwww mRelativePath: " + mRelativePath + ", owner: " + this.owner
//                        + ", name: " + this.name + ", desc: " + this.desc
//                        + ", owner: " + owner + ", name: " + name + ", desc: " + descriptor);
//                owner = "org/aire/MyWebViewClient";
//            }
//        }
        super.visitMethodInsn(opcode, owner, name, descriptor, isInterface);
    }

    @Override
    public void visitTypeInsn(int opcode, String type) {
        if (mReplaceClazz != null
                && opcode == Opcodes.NEW
                && type.equals(WebViewConstants.DEFAULT_WEBVIEW)) {
            type = mReplaceClazz;

            mContext.getLogger().i(mContext.extension.getName(),
                    "new webview class replace: className: " + this.mOwner
                            + ", method: " + this.mName + ", desc: " + this.mDesc);
        }
        super.visitTypeInsn(opcode, type);
    }
}
