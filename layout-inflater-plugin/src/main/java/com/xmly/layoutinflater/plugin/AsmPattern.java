package com.xmly.layoutinflater.plugin;

import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.FrameNode;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.LineNumberNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;

/**
 * Created by luhang on 2021/3/21.
 *
 * @author LuHang
 * Email: luhang@ximalaya.com
 * Tel:15918812121
 */
public class AsmPattern {

    public static final int IGNORE_FRAMES = 1 << 0;
    public static final int IGNORE_LABELS = 1 << 1;
    public static final int IGNORE_LINENUMBERS = 1 << 2;
    public static final int CODE_ONLY = IGNORE_FRAMES | IGNORE_LABELS | IGNORE_LINENUMBERS;

    private final int flags;
    private final ImmutableList<Predicate<AbstractInsnNode>> insnPredicates;

    private AsmPattern(List<Predicate<AbstractInsnNode>> predicates, int flags) {
        this.insnPredicates = ImmutableList.copyOf(predicates);
        this.flags = flags;
    }

    public InsnPattern test(MethodNode main) {
        return test(main.instructions.getFirst());
    }

    public InsnPattern test(AbstractInsnNode start) {
        return AsmHelper.findPattern(
                start,
                insnPredicates.size(),
                // isValidNode
                (node) ->
                        !testFlag(node, FrameNode.class, IGNORE_FRAMES)
                                && !testFlag(node, LabelNode.class, IGNORE_LABELS)
                                && !testFlag(node, LineNumberNode.class, IGNORE_LINENUMBERS),
                // nodePredicate
                (found, node) -> insnPredicates.get(found).test(node),
                InsnPattern::new);
    }

    // returns true if the node is an instance of the given type and the given flag is present
    private boolean testFlag(
            AbstractInsnNode node, Class<? extends AbstractInsnNode> type, int flag) {
        return type.isInstance(node) && (this.flags & flag) != 0;
    }

    public static class Builder {

        private final int flags;
        private final List<Predicate<AbstractInsnNode>> predicates = new ArrayList<>();

        public Builder(final int flags) {
            this.flags = flags;
        }

        public Builder opcode(int opcode) {
            return add(insn -> insn.getOpcode() == opcode);
        }

        public Builder opcodes(int... opcodes) {
            for (int o : opcodes) {
                opcode(o);
            }
            return this;
        }

        public Builder invoke() {
            return add(insn -> insn instanceof MethodInsnNode);
        }

        public Builder any() {
            return add(insn -> true);
        }

        public Builder label() {
            if ((flags & IGNORE_LABELS) != 0) {
                throw new IllegalStateException("Attempting to find a label with flag IGNORE_LABELS");
            }
            return add(insn -> insn instanceof LabelNode);
        }

        public <T extends AbstractInsnNode> Builder custom(Predicate<T> predicate) {
            return add(predicate);
        }

        private Builder add(Predicate<? extends AbstractInsnNode> predicate) {
            predicates.add((Predicate<AbstractInsnNode>) predicate);
            return this;
        }

        public AsmPattern build() {
            return new AsmPattern(predicates, flags);
        }
    }


    public class InsnPattern {

        private final AbstractInsnNode first;
        private final AbstractInsnNode last;

        public InsnPattern(AbstractInsnNode first, AbstractInsnNode last) {
            Objects.requireNonNull(first);
            Objects.requireNonNull(last);
            this.first = first;
            this.last = last;
        }

        public <T extends AbstractInsnNode> T getFirst() {
            return (T) this.first;
        }

        public <T extends AbstractInsnNode> T getLast() {
            return (T) this.last;
        }

        public <T extends AbstractInsnNode> T getIndex(final int index) {
            AbstractInsnNode node = this.first;
            for (int i = 0; i < index; i++) {
                node = node.getNext();
                // if (node == this.last && i < index)
                //    throw new ArrayIndexOutOfBoundsException(String.valueOf(index));
            }
            return (T) node;
        }
    }
}
