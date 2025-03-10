package absyn;

import java.util.*;

// TODO: add dummy types for int and bool for operation expression comparisons  
public class AbsynSemanticAnalyzer implements AbsynVisitor {
    private static final HashMap<String, ArrayList<NodeType>> table = new HashMap<String, ArrayList<NodeType>>();
    private static final StringBuilder sb = new StringBuilder();

    private static int scope;

    private boolean isValid = true;
    
    private static void insert(Dec dec) {
        var list = table.getOrDefault(dec.name, null);

        if (list == null) {
            list = new ArrayList<NodeType>();
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
    
    public boolean finish(String file) {
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

        // visit(dec.params, level);

        // ++scope;

        // visit(dec.body, level);
    }

    public void visit(IfExp exp, int level) {
        // visit(exp.test, level);
        
        // ++scope;

        // visit(exp.body, level);
        
        // if (exp._else != null) {
        //     visit(exp._else, level);
        // }

        // --scope;
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

    }

    public void visit(AssignExp exp, int level) {

    }

    public void visit(ReturnExp exp, int level) {

    }

    public void visit(CompoundExp exp, int level) {

    }

    public void visit(CallExp exp, int level) {

    }

    public void visit(NameTy type, int level) {

    }

    public void visit(DecList list, int level) {

    }

    public void visit(ExpList list, int level) {

    }

    public void visit(VarDecList list, int level) {

    }

    public void visit(IndexVar var, int level) {

    }
    
    public void visit(SimpleVar var, int level) {

    }
}