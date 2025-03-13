package absyn;

import java.io.*;
import java.util.*;

// TODO: add dummy types for int and bool for operation expression comparisons  
@SuppressWarnings("StringConcatenationInsideStringBufferAppend")
public class AbsynSemanticAnalyzer implements AbsynVisitor {

    private static final ArrayList<NodeType> table = new ArrayList<>();
    private static final StringBuilder sb = new StringBuilder();
    private static final int INDENT = 4;

    private boolean isValid = true;

    private static void step(int level, String message) {
        indent(level);
        sb.append(message + "\n");
    }

    private static void insert(Dec dec, int level) {
        table.add(new NodeType(dec.name, dec, level));
    }

    private static NodeType lookup(String id) {
        for (NodeType node : table) {
            if (node.name.equals(id)) {
                return node;
            }
        }
        return null;
    }

    private static void delete(int level) {
        table.removeIf(node -> node.level == level);
    }

    private static void indent(int level) {
        sb.append(" ".repeat(level * INDENT));
    }

    public boolean finish(String file) throws FileNotFoundException, UnsupportedEncodingException {
        if (file == null) {
            System.out.println(sb.toString());
        } else {
            try (PrintWriter writer = new PrintWriter(file.replace(".cm", ".sym"), "UTF-8")) {
                writer.println(sb.toString());
            }
        }

        return isValid;
    }

    @Override
    public void visit(DecList list, int level) {
        step(level, "Entering the global scope:");

        while (list != null) {
            if (list.head != null) {
                list.head.accept(this, level + 1);
            }
            list = list.tail;
        }

        for (NodeType node : table) {
            Dec dec = node.dec;
            step(level + 1, dec.toString());
        }

        delete(level + 1);
        step(level, "Leaving the global scope");
    }

    @Override
    public void visit(SimpleDec dec, int level) {
        insert(dec, level);
    }

    @Override
    public void visit(ArrayDec dec, int level) {
        insert(dec, level);
    }

    @Override
    public void visit(FunctionDec dec, int level) {
        insert(dec, level);

        if (dec.params != null) {
            dec.params.accept(this, level + 1);
        }

        if (dec.body instanceof NilExp) {
            return;
        }

        step(level, "Entering the scope for function " + dec.name + ":");

        dec.body.accept(this, level + 1);

        delete(level + 1);
        step(level, "Leaving the function scope");
    }

    @Override
    public void visit(CompoundExp exp, int level) {
        if (exp.decs != null) {
            exp.decs.accept(this, level);
        }

        ExpList iter = new ExpList(exp.exps.head, exp.exps.tail);
        while (iter != null) {
            if (iter.head != null) {
                iter.head.accept(this, level);
            }
            iter = iter.tail;
        }

        for (NodeType node : table) {
            Dec dec = node.dec;
            if (node.level == level) {
                step(level, dec.toString());
            }
        }

        delete(level);
    }

    @Override
    public void visit(VarDecList list, int level) {
        VarDecList iter = new VarDecList(list.head, list.tail);
        while (iter != null) {
            if (iter.head != null) {
                iter.head.accept(this, level);
            }
            iter = iter.tail;
        }
        // for (NodeType node : table) {
        //     Dec dec = node.dec;
        //     String string = dec.toString();
        //     System.out.println(string);
        // }
    }

    @Override
    public void visit(OpExp exp, int level) {

    }

    @Override
    public void visit(IntExp exp, int level) {

    }

    @Override
    public void visit(NilExp exp, int level) {

    }

    @Override
    public void visit(VarExp exp, int level) {

    }

    @Override
    public void visit(BoolExp exp, int level) {

    }

    @Override
    public void visit(IfExp exp, int level) {
        step(level, "Entering a new if block:");
        exp.body.accept(this, level + 1);
        step(level, "Leaving the if block");

        if (!(exp._else instanceof NilExp)) {
            step(level, "Entering a new else block:");
            exp._else.accept(this, level + 1);
            step(level, "Leaving the else block");
        }
    }

    @Override
    public void visit(WhileExp exp, int level) {
        step(level, "Entering a new while block:");
        exp.body.accept(this, level + 1);
        step(level, "Leaving the while block");
    }

    @Override
    public void visit(AssignExp exp, int level) {

    }

    @Override
    public void visit(ReturnExp exp, int level) {

    }

    @Override
    public void visit(CallExp exp, int level) {

    }

    @Override
    public void visit(NameTy type, int level) {

    }

    @Override
    public void visit(ExpList list, int level) {
        while (list != null) {
            if (list.head != null) {
                list.head.accept(this, level);
            }
            list = list.tail;
        }
    }

    @Override
    public void visit(IndexVar var, int level) {

    }

    @Override
    public void visit(SimpleVar var, int level) {

    }
}
