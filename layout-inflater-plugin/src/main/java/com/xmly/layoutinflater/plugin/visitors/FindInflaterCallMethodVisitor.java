package com.xmly.layoutinflater.plugin.visitors;

import com.ss.android.ugc.bytex.common.utils.OpcodesUtils;
import com.xmly.layoutinflater.plugin.LayoutInflaterContext;
import com.xmly.layoutinflater.plugin.model.MethodInfo;

import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.IincInsnNode;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.IntInsnNode;
import org.objectweb.asm.tree.JumpInsnNode;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.TypeInsnNode;
import org.objectweb.asm.tree.VarInsnNode;

import java.util.ArrayList;
import java.util.List;

import static com.xmly.layoutinflater.plugin.LayoutInflaterContext.INFLATEDESC_1;
import static com.xmly.layoutinflater.plugin.LayoutInflaterContext.INFLATEDESC_2;


/**
 * Created by luhang on 2021/3/18.
 *
 * @author LuHang
 * Email: luhang@ximalaya.com
 * Tel:15918812121
 */
class FindInflaterCallMethodVisitor extends MethodVisitor implements Opcodes {
    private LayoutInflaterContext context;
    private MethodInfo methodInfo;
    private List<AbstractInsnNode> instructions = new ArrayList<>();// 记录浏览过的指令
    private boolean find = false;
    private List<Integer> targetMethodIndex = new ArrayList<>(2);

    public FindInflaterCallMethodVisitor(int i, MethodVisitor methodVisitor, MethodInfo methodInfo, LayoutInflaterContext context) {
        super(i, methodVisitor);
        this.context = context;
        this.methodInfo = methodInfo;
    }

    @Override
    public void visitCode() {
        super.visitCode();
//        context.getLogger().d("visitMethod===================\n: " + methodInfo.toString());
    }

    public void visitInsn(final int opcode) {
        super.visitInsn(opcode);
        addInsn(new InsnNode(opcode));
//        System.out.println("visitInsn: " + OpcodesUtils.getOpcodeString(opcode));
    }

    @Override
    public void visitIntInsn(final int opcode, final int operand) {
        super.visitIntInsn(opcode, operand);
//        System.out.println("visitIntInsn: " + OpcodesUtils.getOpcodeString(opcode) + " " + operand);
        addInsn(new IntInsnNode(opcode, operand));
    }

    @Override
    public void visitVarInsn(final int opcode, final int var) {
        super.visitVarInsn(opcode, var);
        addInsn(new VarInsnNode(opcode, var));
//        System.out.println("visitVarInsn: " + OpcodesUtils.getOpcodeString(opcode) + " " + var);
    }

    @Override
    public void visitTypeInsn(final int opcode, final String type) {
        super.visitTypeInsn(opcode, type);
        addInsn(new TypeInsnNode(opcode, type));
//        System.out.println("visitTypeInsn: " + OpcodesUtils.getOpcodeString(opcode) + " " + type);
    }

    @Override
    public void visitFieldInsn(final int opcode, final String owner, final String name, final String desc) {
        super.visitFieldInsn(opcode, owner, name, desc);
        addInsn(new FieldInsnNode(opcode, owner, name, desc));
//        System.out.println("visitFieldInsn: " + OpcodesUtils.getOpcodeString(opcode) + " " + owner + name + desc);
    }


    @Override
    public void visitMethodInsn(int opcode, String owner, String name,
                                String desc, boolean itf) {
        if (api < Opcodes.ASM5) {
            super.visitMethodInsn(opcode, owner, name, desc, itf);
            return;
        }
        super.visitMethodInsn(opcode, owner, name, desc, itf);
        int index = addInsn(new MethodInsnNode(opcode, owner, name, desc, itf));
        //    INVOKEVIRTUAL android/view/LayoutInflater.inflate (ILandroid/view/ViewGroup;)Landroid/view/View;
//        System.out.println("visitMethodInsn: " + OpcodesUtils.getOpcodeString(opcode) + " " + owner + " " + name + " " + desc + " " + itf);
        if (opcode == INVOKEVIRTUAL
                && "android/view/LayoutInflater".equals(owner)
                && "inflate".equals(name)
                && (INFLATEDESC_1.equals(desc) || INFLATEDESC_2.equals(desc))) {
            find = true;
            targetMethodIndex.add(index);
//            context.addContainInflaterMethod(methodInfo);
        }
    }

//    @Override
//    public void visitInvokeDynamicInsn(String name, String desc, Handle bsm, Object... bsmArgs) {
//        super.visitInvokeDynamicInsn(name, desc, bsm, bsmArgs);
//        System.out.println("visitInvokeDynamicInsn: " + name + " " + desc);
//    }

    @Override
    public void visitJumpInsn(final int opcode, final Label label) {
        super.visitJumpInsn(opcode, label);
        addInsn(new JumpInsnNode(opcode, getLabelNode(label)));
//        System.out.println("visitJumpInsn: " + OpcodesUtils.getOpcodeString(opcode) + " " + label);
    }

//    @Override
//    public void visitLabel(final Label label) {
//        super.visitLabel(label);
//        System.out.println("visitLabel: " + label + " ");
//    }

    @Override
    public void visitLdcInsn(final Object cst) {
        super.visitLdcInsn(cst);
        addInsn(new LdcInsnNode(cst));
//        System.out.println("visitLdcInsn: " + cst + " ");
    }

    @Override
    public void visitIincInsn(final int var, final int increment) {
        super.visitIincInsn(var, increment);
        addInsn(new IincInsnNode(var, increment));
//        System.out.println("visitIincInsn: " + var + " " + increment);
    }

//    @Override
//    public void visitLocalVariable(String name, String descriptor, String signature, Label start, Label end, int index) {
//        super.visitLocalVariable(name, descriptor, signature, start, end, index);
//        System.out.println("visitLocalVariable: " + name + " " + descriptor + " " + signature + " " + index);
//    }

    @Override
    public void visitEnd() {
        super.visitEnd();

        if (find) {// 发现目标
            context.addContainInflaterMethod(methodInfo, targetMethodIndex);
        }
    }

    private int addInsn(AbstractInsnNode insnNode) {
        instructions.add(insnNode);
        return instructions.size() - 1;
    }

    protected LabelNode getLabelNode(final Label l) {
        if (!(l.info instanceof LabelNode)) {
            l.info = new LabelNode();
        }
        return (LabelNode) l.info;
    }

}
