package com.xmly.layoutinflater.plugin.visitors;

import com.ss.android.ugc.bytex.common.BaseContext;
import com.ss.android.ugc.bytex.common.Constants;
import com.ss.android.ugc.bytex.common.utils.OpcodesUtils;
import com.ss.android.ugc.bytex.common.visitor.BaseClassVisitor;
import com.xmly.layoutinflater.plugin.LayoutInflaterContext;
import com.xmly.layoutinflater.plugin.model.MethodInfo;
import com.xmly.layoutinflater.plugin.model.TargetMethodIndex;

import org.dom4j.tree.AbstractNode;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.commons.LocalVariablesSorter;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.IincInsnNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.IntInsnNode;
import org.objectweb.asm.tree.JumpInsnNode;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.LocalVariableNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.TypeInsnNode;
import org.objectweb.asm.tree.VarInsnNode;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import static com.xmly.layoutinflater.plugin.LayoutInflaterContext.INFLATEDESC_1;
import static com.xmly.layoutinflater.plugin.LayoutInflaterContext.INFLATEDESC_2;

/**
 * Created by luhang on 2021/3/19.
 *
 * @author LuHang
 * Email: luhang@ximalaya.com
 * Tel:15918812121
 */
public class LayoutInflaterHookClassVisitor extends BaseClassVisitor {
    private LayoutInflaterContext context;
    private boolean needHook = false;
    private String className = null;

    public LayoutInflaterHookClassVisitor(LayoutInflaterContext context) {
        this.context = context;
    }

    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        super.visit(version, access, name, signature, superName, interfaces);
        this.className = name;
        this.needHook = true;
//        if (context.isHookedClass(name)) {
//            needHook = true;
//        }
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
        MethodVisitor methodVisitor = super.visitMethod(access, name, descriptor, signature, exceptions);
        if (!needHook) {
            return methodVisitor;
        } else {
            //        TargetMethodIndex methodInfo = context.getHookedMethod(className, name, descriptor)

//        if (methodInfo != null) {
//            MethodVisitor mv = new MyMethodNodeTest(Constants.ASM_API, methodVisitor, access, name, descriptor, signature, exceptions, methodInfo);
//            MethodVisitor mv = new MyMethodVisitor(Constants.ASM_API, access, name, descriptor, signature, methodVisitor, methodInfo);
//            return mv;
//        }
        }
        return new InflateMethodVisitor(Constants.ASM_API, methodVisitor, name, descriptor);


//        return methodVisitor;
    }

    // 另一种实现方式
    public class InflateMethodVisitor extends MethodVisitor implements Opcodes {
        private static final String TO_CLASS = "com/ximalaya/commonaspectj/LayoutInflaterAgent";
        private String mName, mDesc;
        private MethodInsnNode targetMethodNode = new MethodInsnNode(INVOKEVIRTUAL,
                "android/view/LayoutInflater",
                "inflate",
                INFLATEDESC_1, false);

        private MethodInsnNode viewStubMethodNode = new MethodInsnNode(INVOKEVIRTUAL,
                "android/view/ViewStub", "inflate",
                "()Landroid/view/View;", false);

        public InflateMethodVisitor(int api, MethodVisitor methodVisitor, String name, String desc) {
            super(api, methodVisitor);
            this.mName = name;
            this.mDesc = desc;
        }

        @Override
        public void visitMethodInsn(int opcode, String owner, String name, String descriptor, boolean isInterface) {
            if (owner.equals(targetMethodNode.owner) && name.equals(targetMethodNode.name)) {
                if (descriptor.equals(INFLATEDESC_1)) {
                    mv.visitMethodInsn(INVOKESTATIC, TO_CLASS, "wrapInflate", "(Landroid/view/LayoutInflater;ILandroid/view/ViewGroup;)Landroid/view/View;", false);
                    context.getLogger().d(context.extension.getName() + "_2", className + "#" + mName + mDesc);

                } else if (descriptor.equals(INFLATEDESC_2)) {
                    mv.visitMethodInsn(INVOKESTATIC, TO_CLASS, "wrapInflate", "(Landroid/view/LayoutInflater;ILandroid/view/ViewGroup;Z)Landroid/view/View;", false);
                    context.getLogger().d(context.extension.getName() + "_3", className + "#" + mName + mDesc);
                } else {
                    super.visitMethodInsn(opcode, owner, name, descriptor, isInterface);
                }
            } else if (owner.equals(viewStubMethodNode.owner) && name.equals(viewStubMethodNode.name) && descriptor.equals(viewStubMethodNode.desc)) {
                mv.visitMethodInsn(INVOKESTATIC, TO_CLASS, "wrapInflate", "(Landroid/view/ViewStub;)Landroid/view/View;", false);
                context.getLogger().d(context.extension.getName() + "_3", className + "#" + mName + mDesc);

                context.getLogger().i("hahhaahah12", "className: " + className
                        + ", name: " + name + ", descriptor: " + descriptor
                        + ", owner: " + owner + ", mName: " + mName
                        + ", mDesc: " + mDesc);
            } else {
                super.visitMethodInsn(opcode, owner, name, descriptor, isInterface);
            }

        }
    }

    public class MyMethodNodeTest extends MethodNode implements Opcodes {
        private static final String TO_CLASS = "com/ximalaya/commonaspectj/LayoutInflaterAgent";


        private TargetMethodIndex methodInfo;
        private MethodVisitor methodVisitor;

        int startTimeIndex = -1;
        int lIdIndex = -1;
        int resultIndex = -1;
        int inflateParam2 = -1;
        int attached = -1;

        int currentMaxVarIndex = 0;
        LocalVariableNode indexMaxNode = null;


        private MethodInsnNode targetMethodNode = new MethodInsnNode(INVOKEVIRTUAL,
                "android/view/LayoutInflater",
                "inflate",
                INFLATEDESC_1, false);

        public MyMethodNodeTest(int api, MethodVisitor mv, int access, String name, String descriptor, String signature, String[] exceptions, TargetMethodIndex methodInfo) {
            super(api, access, name, descriptor, signature, exceptions);
            this.methodVisitor = mv;
            this.methodInfo = methodInfo;
            if (localVariables == null) {
                localVariables = new ArrayList<>(5);
            }
        }

        @Override
        public void visitEnd() {
            super.visitEnd();
            ListIterator<AbstractInsnNode> iterator = instructions.iterator();
            MethodInsnNode realNode = null;
            LabelNode begin = new LabelNode();
            LabelNode end = new LabelNode();
            instructions.insertBefore(instructions.getFirst(), begin);
            instructions.insert(instructions.getLast(), end);

            while (iterator.hasNext()) {
                AbstractInsnNode tmpNode = iterator.next();
                if (tmpNode instanceof MethodInsnNode) {
                    MethodInsnNode methodInsnNode = (MethodInsnNode) tmpNode;
                    if (isTargetMethod(methodInsnNode)) {
                        if (currentMaxVarIndex == 0 && indexMaxNode == null) {
                            System.out.println("localVar: all " + className + ": " + name + desc + " var: " + localVariables.size() + " " + maxLocals);
                            for (LocalVariableNode localVariable : localVariables) {
                                System.out.println("localVar: " + className + ": " + name + desc + " var: " + localVariable.name + " " + localVariable.desc + " " + localVariable.index);
                                if (localVariable.index >= currentMaxVarIndex) {
                                    currentMaxVarIndex = localVariable.index;
                                    indexMaxNode = localVariable;
                                }
                            }
                            if (currentMaxVarIndex == 0) {
                                currentMaxVarIndex = maxLocals;
                            }
                            if (indexMaxNode == null) {
                                System.out.println("localVar: " + className + ": " + name + desc + " maxIndex: " + currentMaxVarIndex + " var is null ----------------");
                            } else {
                                System.out.println("localVar: " + className + ": " + name + desc + " maxIndex: " + currentMaxVarIndex + " " + indexMaxNode.name + indexMaxNode.desc + indexMaxNode.index);
                            }

                        }
                        context.getLogger().d(context.extension.getName(), "hook: " + methodInfo.methodInfo.getClassInfo().getName() + "#" + methodInfo.methodInfo.getName() + methodInfo.methodInfo.getDesc());

                        // 从这里开始前后插入目标代码
                        InsnList paramList = new InsnList();
                        InsnList resultList = new InsnList();
                        // 保留第三个参数 （如果有）
                        if (methodInsnNode.desc.equals(INFLATEDESC_2)) {
                            attached = getNextIndex(currentMaxVarIndex);
                            LocalVariableNode param3 = new LocalVariableNode("x_attach_parent", Type.BOOLEAN_TYPE.getDescriptor(), null, begin, end, attached);
                            localVariables.add(param3);
                            indexMaxNode = param3;
                            paramList.add(new VarInsnNode(ISTORE, attached));
                        }

                        // 保留第二个参数
                        inflateParam2 = getNextIndex(currentMaxVarIndex);
                        LocalVariableNode param2 = new LocalVariableNode("x_view_group", Type.getType("Landroid/view/ViewGroup;").getDescriptor(), null, begin, end, inflateParam2);
                        localVariables.add(param2);
                        indexMaxNode = param2;
                        paramList.add(new VarInsnNode(ASTORE, inflateParam2));

                        // 复制第一个参数备份一份

                        paramList.add(new InsnNode(DUP));
                        lIdIndex = getNextIndex(currentMaxVarIndex); // 保存一份到自定义变量中
                        LocalVariableNode layoutId = new LocalVariableNode("x_layout_id", Type.INT_TYPE.getDescriptor(), null, begin, end, lIdIndex);
                        localVariables.add(layoutId);
                        indexMaxNode = layoutId;
                        paramList.add(new VarInsnNode(ISTORE, lIdIndex));
                        // 插入开始时间
                        paramList.add(new MethodInsnNode(INVOKESTATIC, "java/lang/System", "currentTimeMillis", "()J", false));
                        startTimeIndex = getNextIndex(currentMaxVarIndex);
                        LocalVariableNode startTime = new LocalVariableNode("x_start_time", Type.LONG_TYPE.getDescriptor(), null, begin, end, startTimeIndex);
                        localVariables.add(startTime);
                        indexMaxNode = startTime;
                        paramList.add(new VarInsnNode(LSTORE, startTimeIndex));

                        // 恢复第二个参数
                        paramList.add(new VarInsnNode(ALOAD, inflateParam2));
                        // 恢复第三个参数
                        if (methodInsnNode.desc.equals(INFLATEDESC_2) && attached > 0) {
                            paramList.add(new VarInsnNode(ILOAD, attached));
                        }
                        // 调用inflate
                        // 保存结果

                        resultList.add(new InsnNode(DUP));
                        resultIndex = getNextIndex(currentMaxVarIndex);
                        LocalVariableNode result = new LocalVariableNode("x_inflate_result", Type.getType("Landroid/view/View;").getDescriptor(), null, begin, end, resultIndex);
                        localVariables.add(result);
                        indexMaxNode = result;
                        resultList.add(new VarInsnNode(ASTORE, resultIndex));
                        // 计算函数调用时间
                        resultList.add(new MethodInsnNode(INVOKESTATIC, "java/lang/System", "currentTimeMillis", "()J", false));
                        resultList.add(new VarInsnNode(LLOAD, startTimeIndex));
                        resultList.add(new InsnNode(LSUB));
                        int r = getNextIndex(currentMaxVarIndex);
                        LocalVariableNode cost = new LocalVariableNode("x_cost_mills", Type.LONG_TYPE.getDescriptor(), null, begin, end, r);
                        localVariables.add(cost);
                        indexMaxNode = cost;
                        resultList.add(new VarInsnNode(LSTORE, r));
                        // 加载保存的参数
                        resultList.add(new VarInsnNode(ALOAD, resultIndex));
                        resultList.add(new VarInsnNode(ILOAD, lIdIndex));
                        resultList.add(new VarInsnNode(LLOAD, r));
                        resultList.add(new MethodInsnNode(INVOKESTATIC, TO_CLASS, "inflateHook", "(Landroid/view/View;IJ)V", false));
                        // 调用插桩方法

                        instructions.insertBefore(methodInsnNode, paramList);
                        instructions.insert(methodInsnNode, resultList);

                    }
                }
            }
            accept(this.methodVisitor);
        }

        private int getNextIndex(int currMaxIndex) {
            if (indexMaxNode == null) {
                currentMaxVarIndex = maxLocals;
            } else {
                currentMaxVarIndex = currMaxIndex + Type.getType(indexMaxNode.desc).getSize();
            }

            return currentMaxVarIndex;
        }

        private boolean isTargetMethod(MethodInsnNode methodInsnNode) {
            if (methodInsnNode.getOpcode() == targetMethodNode.getOpcode()
                    && targetMethodNode.owner.equals(methodInsnNode.owner)
                    && targetMethodNode.name.equals(methodInsnNode.name)
                    && (INFLATEDESC_1.equals(methodInsnNode.desc) || INFLATEDESC_2.equals(methodInsnNode.desc))) {
                return true;
            }

            return false;
        }
    }


    public class MyMethodNode extends MethodNode implements Opcodes {
        private static final String TO_CLASS = "com/ximalaya/commonaspectj/LayoutInflaterAgent";

        private TargetMethodIndex methodInfo;
        private MethodVisitor methodVisitor;

        private MethodInsnNode targetMethodNode = new MethodInsnNode(INVOKEVIRTUAL,
                "android/view/LayoutInflater",
                "inflate",
                "(ILandroid/view/ViewGroup;)Landroid/view/View;", false);

        public MyMethodNode(int api, MethodVisitor mv, int access, String name, String descriptor, String signature, String[] exceptions, TargetMethodIndex methodInfo) {
            super(api, access, name, descriptor, signature, exceptions);
            this.methodVisitor = mv;
            this.methodInfo = methodInfo;
            if (localVariables == null) {
                localVariables = new ArrayList<>(5);
            }
        }

        @Override
        public void visitEnd() {
            super.visitEnd();


            ListIterator<AbstractInsnNode> iterator = instructions.iterator();
            MethodInsnNode realNode = null;
            LabelNode begin = new LabelNode();
            LabelNode end = new LabelNode();
            instructions.insertBefore(instructions.getFirst(), begin);
            instructions.insert(instructions.getLast(), end);

            while (iterator.hasNext()) {
                AbstractInsnNode tmpNode = iterator.next();
                if (tmpNode instanceof MethodInsnNode) {
                    MethodInsnNode methodInsnNode = (MethodInsnNode) tmpNode;
                    if (isTargetMethod(methodInsnNode)) {
                        // 从这里开始前后插入目标代码
                        AbstractInsnNode preNode = methodInsnNode.getPrevious().getPrevious();
                        AbstractInsnNode nextNode = methodInsnNode.getNext();
                        InsnList startTimeList = new InsnList();
                        InsnList layoutIdList = new InsnList();
                        InsnList saveResultList = new InsnList();
                        boolean findPre = false;
                        while (preNode != null) {
                            if (preNode instanceof MethodInsnNode && isTargetMethod((MethodInsnNode) preNode)) {
                                break;
                            }
                            if (preNode instanceof LdcInsnNode && ((LdcInsnNode) preNode).cst instanceof Integer) { // 认为是加载layout id
                                findPre = true;
                                break;
                            } else if (preNode instanceof FieldInsnNode && preNode.getOpcode() == GETSTATIC && ((FieldInsnNode) preNode).owner.contains("R$")
                                    && ((FieldInsnNode) preNode).desc.equals(Type.INT_TYPE.getDescriptor())) {
                                findPre = true;
                                break;
                            } else if (preNode instanceof FieldInsnNode && preNode.getOpcode() == GETSTATIC
                                    && ((FieldInsnNode) preNode).desc.equals(Type.INT_TYPE.getDescriptor())) {
                                findPre = true;
                                break;
                            } else if (preNode instanceof VarInsnNode && preNode.getOpcode() == ILOAD) {
                                findPre = true;
                                break;
                            } else if (preNode instanceof FieldInsnNode && preNode.getOpcode() == GETFIELD
                                    && ((FieldInsnNode) preNode).desc.equals(Type.INT_TYPE.getDescriptor())) {
                                findPre = true;
                                break;
                            } else if (preNode instanceof MethodInsnNode && ((MethodInsnNode) preNode).desc.endsWith(")I")) {
                                findPre = true;
                                break;
                            } else if (preNode instanceof InsnNode && preNode.getOpcode() == L2I) {
                                findPre = true;
                                break;
                            }
                            preNode = preNode.getPrevious();
                        }
                        if (findPre) {
                            // 插入时间
                            insertStartTime(startTimeList, begin, end);
                            // 保存layout Id
                            insertSaveLayoutId(layoutIdList, begin, end);
                        } else {
                            throw new RuntimeException("没有找到合适的hook点！ " + methodInfo.methodInfo.getClassInfo().getName()
                                    + "#" + methodInfo.methodInfo.getName() + methodInfo.methodInfo.getDesc());
                        }

                        AbstractInsnNode shouldRemoveNode = null;
                        boolean find = false;
                        int originIndex = -1;
                        boolean isReturn = false;
                        boolean isPutToField = false;
                        boolean isMethodParam = false;
                        while (nextNode != null) {
                            if (preNode instanceof MethodInsnNode && isTargetMethod((MethodInsnNode) preNode)) {
                                break;
                            }
                            if (nextNode instanceof VarInsnNode && nextNode.getOpcode() == ASTORE) {
                                find = true;
                                originIndex = ((VarInsnNode) nextNode).var;
                                break;
                            } else if (nextNode instanceof InsnNode && nextNode.getOpcode() == POP) {
                                shouldRemoveNode = nextNode;
                                find = true;
                                break;
                            } else if (nextNode instanceof InsnNode && nextNode.getOpcode() == ARETURN) {
                                find = true;
                                isReturn = true;
                                break;
                            } else if (nextNode instanceof FieldInsnNode && (nextNode.getOpcode() == PUTFIELD || nextNode.getOpcode() == PUTSTATIC)
                                    && ((FieldInsnNode) nextNode).desc.equals("Landroid/view/View;")) {
                                find = true;
                                isPutToField = true;
                                break;
                            } else if (nextNode instanceof MethodInsnNode && ((MethodInsnNode) nextNode).desc.contains("Landroid/view/View;")) {
                                // 函数调用，且函数的参数是inflate 的结果
                                find = true;
                                isPutToField = true;
                                break;
                            }

                            nextNode = nextNode.getNext();
                        }

                        if (shouldRemoveNode != null) {
                            instructions.remove(shouldRemoveNode);
                        }

                        if (find) {
                            insertSaveResult(saveResultList, begin, end, originIndex, isReturn, isPutToField);
                            weave(preNode, nextNode, startTimeList, layoutIdList, saveResultList, isPutToField);
                        } else {
                            throw new RuntimeException("没有找到inflate 的结果！ " + methodInfo.methodInfo.getClassInfo().getName()
                                    + "#" + methodInfo.methodInfo.getName() + methodInfo.methodInfo.getDesc());
                        }

                    }
                }
            }

            accept(this.methodVisitor);
        }

        int startTimeIndex = -1;
        int lIdIndex = -1;
        int resultIndex = -1;

        private void insertStartTime(InsnList startTimeList, LabelNode begin, LabelNode end) {
            startTimeList.add(new MethodInsnNode(INVOKESTATIC, "java/lang/System", "currentTimeMillis", "()J", false));
            startTimeIndex = localVariables.size();
            LocalVariableNode startTime = new LocalVariableNode("x_start_time", Type.LONG_TYPE.getDescriptor(), null, begin, end, startTimeIndex);
            localVariables.add(startTime);
            startTimeList.add(new VarInsnNode(LSTORE, startTimeIndex));
        }

        private void insertSaveLayoutId(InsnList layoutIdList, LabelNode begin, LabelNode end) {
            lIdIndex = localVariables.size();
            LocalVariableNode layoutId = new LocalVariableNode("x_layout_id", Type.INT_TYPE.getDescriptor(), null, begin, end, lIdIndex);
            localVariables.add(layoutId);
            layoutIdList.add(new VarInsnNode(ISTORE, lIdIndex));
            layoutIdList.add(new VarInsnNode(ILOAD, lIdIndex));
        }

        private void insertSaveResult(InsnList resultList, LabelNode begin, LabelNode end, int originIndex, boolean isReturn, boolean isPutField) {
            context.getLogger().d(context.extension.getName(), "hook: " + methodInfo.methodInfo.getClassInfo().getName() + "#" + methodInfo.methodInfo.getName() + methodInfo.methodInfo.getDesc());

            resultIndex = localVariables.size();
            LocalVariableNode result = new LocalVariableNode("x_inflate_result", Type.getType("Landroid/view/View;").getDescriptor(), null, begin, end, resultIndex);
            localVariables.add(result);
            if (originIndex > 0) {
                resultList.add(new VarInsnNode(ALOAD, originIndex));
            }
            resultList.add(new VarInsnNode(ASTORE, resultIndex));


            resultList.add(new MethodInsnNode(INVOKESTATIC, "java/lang/System", "currentTimeMillis", "()J", false));
            resultList.add(new VarInsnNode(LLOAD, startTimeIndex));
            resultList.add(new InsnNode(LSUB));
            int r = localVariables.size();
            LocalVariableNode cost = new LocalVariableNode("x_cost", Type.LONG_TYPE.getDescriptor(), null, begin, end, r);
            localVariables.add(cost);
            resultList.add(new VarInsnNode(LSTORE, r));

            resultList.add(new VarInsnNode(ALOAD, resultIndex));
            resultList.add(new VarInsnNode(ILOAD, lIdIndex));
            resultList.add(new VarInsnNode(LLOAD, r));
            resultList.add(new MethodInsnNode(INVOKESTATIC, TO_CLASS, "inflateHook", "(Landroid/view/View;IJ)V", false));

            if (isReturn || isPutField) {
                resultList.add(new VarInsnNode(ALOAD, resultIndex));
            }
        }

        private void weave(AbstractInsnNode preNode, AbstractInsnNode nextNode, InsnList startTimeList, InsnList layoutIdList, InsnList resultList, boolean isPutField) {
            instructions.insertBefore(preNode, startTimeList);
            instructions.insert(preNode, layoutIdList);
            if (nextNode.getOpcode() >= IRETURN && nextNode.getOpcode() <= RETURN || nextNode.getOpcode() == ATHROW || isPutField) {
                instructions.insertBefore(nextNode, resultList);
            } else {
                instructions.insert(nextNode, resultList);
            }
        }

        private boolean isTargetMethod(MethodInsnNode methodInsnNode) {
            if (methodInsnNode.getOpcode() == targetMethodNode.getOpcode()
                    && targetMethodNode.owner.equals(methodInsnNode.owner)
                    && targetMethodNode.name.equals(methodInsnNode.name)
                    && targetMethodNode.desc.equals(methodInsnNode.desc)) {
                return true;
            }

            return false;
        }
    }

    class MyMethodVisitor extends LocalVariablesSorter implements Opcodes {
        //        private static final String TO_CLASS = "com/ss/android/ugc/bytex/example/LayoutInflaterHelper";
        //com.ximalaya.commonaspectj.LayoutInflaterAgent
        private static final String TO_CLASS = "com/ximalaya/commonaspectj/LayoutInflaterAgent";

        private TargetMethodIndex methodInfo;
        private int startTimeVarIndex;

        private int inflateFirstPmIndex;

        boolean hook = false;

        boolean firstSpecial = false;

        private int index = 0; // 指令浏览指针

        protected MyMethodVisitor(int api, int access, String name, String descriptor, String signature, MethodVisitor methodVisitor, TargetMethodIndex methodInfo) {
            super(api, access, descriptor, methodVisitor);
            this.methodInfo = methodInfo;

            if (methodInfo.targetIndex.get(0) <= 2) {
                firstSpecial = true;
            }
        }


        @Override
        public void visitCode() {
            super.visitCode();
            context.getLogger().d("visitMethod-----------\n: " + methodInfo.toString());
        }

        public void visitInsn(final int opcode) {
            addInsn(new InsnNode(opcode));
            super.visitInsn(opcode);

//            System.out.println("visitInsn: " + OpcodesUtils.getOpcodeString(opcode));
        }

        @Override
        public void visitIntInsn(final int opcode, final int operand) {
            addInsn(new IntInsnNode(opcode, operand));
            super.visitIntInsn(opcode, operand);
        }

        @Override
        public void visitVarInsn(final int opcode, final int var) {
            addInsn(new VarInsnNode(opcode, var));
            super.visitVarInsn(opcode, var);
        }

        @Override
        public void visitTypeInsn(final int opcode, final String type) {
            addInsn(new TypeInsnNode(opcode, type));
            super.visitTypeInsn(opcode, type);
        }

        @Override
        public void visitFieldInsn(final int opcode, final String owner, final String name, final String desc) {
            addInsn(new FieldInsnNode(opcode, owner, name, desc));
            super.visitFieldInsn(opcode, owner, name, desc);
        }


        @Override
        public void visitMethodInsn(int opcode, String owner, String name,
                                    String desc, boolean itf) {
            if (api < Opcodes.ASM5) {
                super.visitMethodInsn(opcode, owner, name, desc, itf);
                return;
            }
            addInsn(new MethodInsnNode(opcode, owner, name, desc, itf));
            super.visitMethodInsn(opcode, owner, name, desc, itf);

            //    INVOKEVIRTUAL android/view/LayoutInflater.inflate (ILandroid/view/ViewGroup;)Landroid/view/View;
//            System.out.println("visitMethodInsn: " + OpcodesUtils.getOpcodeString(opcode) + " " + owner + " " + name + " " + desc + " " + itf);
//            if (opcode == INVOKEVIRTUAL
//                    && "android/view/LayoutInflater".equals(owner)
//                    && "inflate".equals(name)
//                    && "(ILandroid/view/ViewGroup;)Landroid/view/View;".equals(desc)) {
////            context.addContainInflaterMethod(methodInfo);
//            }
        }


        @Override
        public void visitJumpInsn(final int opcode, final Label label) {
            addInsn(new JumpInsnNode(opcode, getLabelNode(label)));
            super.visitJumpInsn(opcode, label);
        }


        @Override
        public void visitLdcInsn(final Object cst) {
            addInsn(new LdcInsnNode(cst));
            super.visitLdcInsn(cst);
        }

        @Override
        public void visitIincInsn(final int var, final int increment) {
            addInsn(new IincInsnNode(var, increment));
            super.visitIincInsn(var, increment);
        }


        @Override
        public void visitEnd() {
            super.visitEnd();
        }


        protected LabelNode getLabelNode(final Label l) {
            if (!(l.info instanceof LabelNode)) {
                l.info = new LabelNode();
            }
            return (LabelNode) l.info;
        }

        private List<AbstractInsnNode> instructions = new ArrayList<>();// 记录浏览过的指令

        private void addInsn(AbstractInsnNode insnNode) {
            instructions.add(insnNode);
            for (int i = 0; i < methodInfo.targetIndex.size(); i++) {
                int targetIndex = methodInfo.targetIndex.get(i);
                if (targetIndex - 2 == index) {// 开始插入时间
                    weaveBeginTime();
                } else if (targetIndex - 1 == index) { // 保留第一个参数
                    saveInflaterFirstParam();
                } else if (targetIndex + 1 == index) {

                    if (insnNode.getOpcode() == ASTORE && insnNode instanceof VarInsnNode) {
                        VarInsnNode varInsnNode = (VarInsnNode) insnNode;
//                        saveView(varInsnNode.var);
                        originViewIndex = varInsnNode.var;
                        storeView = true;
                        inflateNextIsStore = true;
                    } else {
                        inflateNextIsStore = false;
                    }

                } else if (targetIndex + 2 == index) {
                    if (!inflateNextIsStore) {
                        if (insnNode.getOpcode() == ASTORE && insnNode instanceof VarInsnNode) {
                            VarInsnNode varInsnNode = (VarInsnNode) insnNode;
                            originViewIndex = varInsnNode.var;
                            storeView = true;
                        }
                    } else {
                        if (storeView) {
                            storeView = false;
                            saveView(originViewIndex);
//                        useView();
                        }
                        //                    weaveEndTime();
//                    useLayoutId();
                        hookInflate();
                    }

                } else if (targetIndex + 3 == index && !inflateNextIsStore) {
                    if (storeView) {
                        storeView = false;
                        saveView(originViewIndex);
//                        useView();
                    }
                    //                    weaveEndTime();
//                    useLayoutId();
                    hookInflate();
                }
            }
            index++;
        }

        int viewIndex = -1;
        int originViewIndex;
        boolean storeView = false;
        boolean inflateNextIsStore = false;


        private void saveView(int var) {
            viewIndex = newLocal(Type.getType("Landroid/view/View;"));
            mv.visitVarInsn(ALOAD, viewIndex - 1);
            mv.visitVarInsn(ASTORE, viewIndex);
        }

        private void weaveBeginTime() { // 植入开始的时间获取
            mv.visitMethodInsn(Opcodes.INVOKESTATIC, "java/lang/System", "currentTimeMillis", "()J", false);
            startTimeVarIndex = newLocal(Type.LONG_TYPE);
            mv.visitVarInsn(Opcodes.LSTORE, startTimeVarIndex);
        }

        private void weaveEndTime() {
            mv.visitMethodInsn(INVOKESTATIC, "java/lang/System", "currentTimeMillis", "()J", false);
            mv.visitVarInsn(LLOAD, startTimeVarIndex);
            mv.visitInsn(LSUB);
            int index = newLocal(Type.LONG_TYPE);
            mv.visitVarInsn(LSTORE, index);
            mv.visitLdcInsn("methodName");
            mv.visitVarInsn(LLOAD, index);
            mv.visitMethodInsn(INVOKESTATIC, "com/ss/android/ugc/bytex/example/LayoutInflaterHelper", "timingMethod", "(Ljava/lang/String;J)V", false);
        }

        private void saveInflaterFirstParam() {
            inflateFirstPmIndex = newLocal(Type.INT_TYPE);
            mv.visitVarInsn(ISTORE, inflateFirstPmIndex); // 保存
            mv.visitVarInsn(ILOAD, inflateFirstPmIndex); // 加载
        }

        private void useLayoutId() {
            mv.visitVarInsn(ILOAD, inflateFirstPmIndex);
            mv.visitMethodInsn(INVOKESTATIC, "com/ss/android/ugc/bytex/example/LayoutInflaterHelper", "layoutId", "(I)V", false);
        }

        private void useView() {
            mv.visitVarInsn(ALOAD, viewIndex);
            mv.visitMethodInsn(INVOKESTATIC, "com/ss/android/ugc/bytex/example/LayoutInflaterHelper", "view", "(Landroid/view/View;)V", false);
        }

        private void hookInflate() {
            mv.visitMethodInsn(INVOKESTATIC, "java/lang/System", "currentTimeMillis", "()J", false);
            mv.visitVarInsn(LLOAD, startTimeVarIndex);
            mv.visitInsn(LSUB);
            int index = newLocal(Type.LONG_TYPE);
            mv.visitVarInsn(LSTORE, index);

            mv.visitVarInsn(ALOAD, viewIndex); // 参数一
            mv.visitVarInsn(ILOAD, inflateFirstPmIndex); // 参数二
            mv.visitVarInsn(LLOAD, index); // 参数3

            mv.visitMethodInsn(INVOKESTATIC, TO_CLASS, "inflateHook", "(Landroid/view/View;IJ)V", false);
        }
    }

    class HookMethodVisitor extends MethodNode implements Opcodes {

        private BaseContext context;
        private MethodInfo methodInfo;

        private MethodVisitor mv;

        public HookMethodVisitor(int access, String name, String desc, String signature, String[] exceptions, MethodVisitor mv, MethodInfo methodInfo) {
            super(Constants.ASM_API, access, name, desc, signature, exceptions);
            this.context = context;
            this.mv = mv;
            this.methodInfo = methodInfo;
            if (localVariables == null) {
                localVariables = new ArrayList<>(5);
            }
        }

        @Override
        public void visitEnd() {
            super.visitEnd();
            System.out.println("hook inflater: " + methodInfo.getClassInfo().getName() + " " + methodInfo.getName() + " " + methodInfo.getDesc());
            MethodInsnNode insnNode = new MethodInsnNode(INVOKEVIRTUAL,
                    "android/view/LayoutInflater",
                    "inflate",
                    "(ILandroid/view/ViewGroup;)Landroid/view/View;", false);

            ListIterator<AbstractInsnNode> iterator = instructions.iterator();
            MethodInsnNode realNode = null;
            while (iterator.hasNext()) {
                AbstractInsnNode tmpNode = iterator.next();
                if (tmpNode instanceof MethodInsnNode) {
                    MethodInsnNode methodInsnNode = (MethodInsnNode) tmpNode;
                    if (methodInsnNode.getOpcode() == insnNode.getOpcode()
                            && insnNode.owner.equals(methodInsnNode.owner)
                            && insnNode.name.equals(methodInsnNode.name)
                            && insnNode.desc.equals(methodInsnNode.desc)) {
                        realNode = methodInsnNode;
                        break;
                    }
                }
            }
            if (realNode != null) {
                System.out.println("do hook the methond " + instructions.indexOf(realNode));
                AbstractInsnNode startTimeNode = new MethodInsnNode(INVOKESTATIC, "java/lang/System", "currentTimeMillis", "()J", false);
                InsnList il = new InsnList();
                AbstractInsnNode abstractInsnNode = instructions.getFirst();
                int ls = localVariables.size();
//                il.add(startTimeNode);
//                il.add(new VarInsnNode(LSTORE, ls));
//                addLocalJobConf(ls + 1);
                il.add(new LabelNode());
                il.add(new InsnNode(ICONST_1));
                il.add(new VarInsnNode(ISTORE, ls));

                LabelNode begin = new LabelNode();
                LabelNode end = new LabelNode();
                il.insertBefore(instructions.getFirst(), begin);
                il.insert(instructions.getLast(), end);
                LocalVariableNode lv = new LocalVariableNode("layout_id", Type.INT_TYPE.getDescriptor(), null, null, null, ls);
                localVariables.add(lv);


//                localVariables.add(5, new LocalVariableNode())
                instructions.insertBefore(abstractInsnNode, il);
//                maxStack += 2;
//                instructions.insertBefore(instructions.getFirst(), startTimeNode);
//                instructions.insert(instructions.getFirst(), new VarInsnNode(LSTORE, 1));
//                int index = instructions.indexOf(insnNode);
//                AbstractInsnNode realNode = instructions.get(index);
//                instructions.ins
            }
            accept(this.mv);
        }

        private void addLocalJobConf(int varIndex) {
            LabelNode begin = new LabelNode();
            LabelNode end = new LabelNode();
            instructions.insertBefore(instructions.getFirst(), begin);
            instructions.insert(instructions.getLast(), end);
            LocalVariableNode lv = new LocalVariableNode("layout_id", "J", null, begin, end, varIndex);
            localVariables.add(lv);
        }

    }


}
