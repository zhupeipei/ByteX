package com.xmly.layoutinflater.plugin.visitors;

import com.ss.android.ugc.bytex.common.Constants;
import com.ss.android.ugc.bytex.common.visitor.BaseClassVisitor;
import com.xmly.layoutinflater.plugin.LayoutInflaterContext;
import com.xmly.layoutinflater.plugin.model.ClassInfo;
import com.xmly.layoutinflater.plugin.model.MethodInfo;

import org.objectweb.asm.MethodVisitor;

/**
 * Created by luhang on 2021/3/17.
 *
 * @author LuHang
 * Email: luhang@ximalaya.com
 * Tel:15918812121
 */
public class FindLayoutInflaterCallClassVisitor extends BaseClassVisitor {
    private LayoutInflaterContext context;
    private ClassInfo classInfo;

    public FindLayoutInflaterCallClassVisitor(LayoutInflaterContext context) {
        this.context = context;
    }

    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        super.visit(version, access, name, signature, superName, interfaces);
        this.classInfo = new ClassInfo(access, name, superName, interfaces);
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
        MethodVisitor methodVisitor = cv.visitMethod(access, name, descriptor, signature, exceptions);
        MethodInfo methodInfo = new MethodInfo(classInfo, access, name, descriptor);
        MethodVisitor mv = new FindInflaterCallMethodVisitor(Constants.ASM_API, methodVisitor, methodInfo, context);
        return mv;
    }

}
