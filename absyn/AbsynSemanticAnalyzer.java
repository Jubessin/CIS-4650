package absyn;

import java.io.*;
import java.util.*;

// TODO: add dummy types for int and bool for operation expression comparisons  
public class AbsynSemanticAnalyzer implements AbsynVisitor {
    private static final HashMap<String, ArrayList<NodeType>> table = new HashMap<String, ArrayList<NodeType>>();
    private static final StringBuilder sb = new StringBuilder();
    private static final int INDENT = 4;

    private static int scope;

    private boolean isValid = true;
    
    private static void step(int level, String message) {
        indent(level);
        sb.append(message + "\n");
    }
    
    private static void insert(Dec dec) {
        var list = table.getOrDefault(dec.name, null);

        if (list == null) {
            list = new ArrayList<NodeType>();
            table.put(dec.name, list);
        }

        list.add(new NodeType(dec.name, dec, scope));
    }

    private static NodeType lookup(String id) {
        var list = table.getOrDefault(id, null);

        if (list == null)
            return null;

        var size = list.size();

        if (size == 0)
            return null;

        return list.get(size - 1);
    }

    private static void delete() {
        var iter = table.entrySet().iterator();

        while (iter.hasNext()) {
            var pair = (Map.Entry)iter.next();
            var key = pair.getKey();
            var value = (ArrayList)pair.getValue();

            // TODO: Test that this makes sense.
            value.removeIf(n -> ((NodeType)n).level > scope);
        }
    }

    private static void indent(int level) {
        sb.append(" ".repeat(level * INDENT));
    }
    
    public boolean finish(String file) throws FileNotFoundException, UnsupportedEncodingException {
        if (file == null) {
            System.out.println(sb.toString());
        }
        else {
            var writer = new PrintWriter(file.replace(".cm", ".sym"), "UTF-8");
            writer.println(sb.toString());
            writer.close();
        }

        return isValid;
    }
    
    public void visit(Dec dec, int level) {
        
    }

    public void visit(SimpleDec dec, int level) {
        insert(dec);
    }

    public void visit(ArrayDec dec, int level) {
        insert(dec);
    }

    public void visit(FunctionDec dec, int level) {
        insert(dec);

        var isFunctionDeclaration = dec.body instanceof CompoundExp;

        if (!isFunctionDeclaration)
            return;

        indent(level);
        sb.append("Entering the scope for function " + dec.name + ":\n");
        
        dec.body.accept(this, level);

        indent(level);
        sb.append("Leaving the function scope\n");
    }

    public void visit(IfExp exp, int level) {
        step(level++, "Entering a new if block:");
        // visit(exp.test, level);
        
        // ++scope;

        // visit(exp.body, level);
        
        // if (exp._else != null) {
        //     visit(exp._else, level);
        // }

        // --scope;

        step(--level, "Leaving the if block");
    }

    public void visit(OpExp exp, int level) {

    }

    public void visit(IntExp exp, int level) {

    }

    public void visit(NilExp exp, int level) {

    }

    public void visit(VarExp exp, int level) {

    }

    public void visit(BoolExp exp, int level) {

    }

    public void visit(WhileExp exp, int level) {
        step(level++, "Entering a new while block:");

        step(--level, "Leaving the while block");
    }

    public void visit(AssignExp exp, int level) {

    }

    public void visit(ReturnExp exp, int level) {

    }

    public void visit(CompoundExp exp, int level) {
        scope++;
    }

    public void visit(CallExp exp, int level) {

    }

    public void visit(NameTy type, int level) {

    }

    public void visit(DecList list, int level) {
        step(level++, "Entering the global scope:");
        
        scope++;

        while (list != null) {
            if (list.head != null) {
                list.head.accept(this, level);
            }
            list = list.tail;
        }

        var iter = table.entrySet().iterator();

        while (iter.hasNext()) {
            var pair = (Map.Entry)iter.next();
            var key = pair.getKey();
            var value = (ArrayList)pair.getValue();

            if (value.size() == 0)
                continue;

            var nodeType = (NodeType)value.get(0);

            // TODO: Add type information.
            sb.append(nodeType.name + ": \n");
        }

        step(--level, "Leaving the global scope");
    }

    public void visit(ExpList list, int level) {
        while (list != null) {
            if (list.head != null) {
                list.head.accept(this, level);
            }
            list = list.tail;
        }
    }

    public void visit(VarDecList list, int level) {

    }

    public void visit(IndexVar var, int level) {

    }
    
    public void visit(SimpleVar var, int level) {

    }
}