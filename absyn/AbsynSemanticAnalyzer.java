package absyn;

import java.util.*;

public class AbsynSemanticAnalyzer implements AbsynVisitor {
    private static final HashMap<String, ArrayList<NodeType>> table = new HashMap<String, ArrayList<NodeType>>();
    private static final NodeType dummyBool = new NodeType(null, new BoolExp(0, 0, false), -1);
    private static final NodeType dummyInt = new NodeType(null, new IntExp(0, 0, 0), -1);
    private static final StringBuilder sb = new StringBuilder();

    private static int scope;

    private boolean isValid = true;
    
    private static addNode(Dec dec) {
        var list = table.getOrDefault(dec.name);

        if (list == null) {
            list = new ArrayList<NodeType>();
        }

        list.add(new NodeType(dec.name, dec, scope));
    }
    
    public boolean finish(String file) {
        return isValid;
    }
    
    public void visit(Dec dec, int level) {
        
    }

    public void visit(SimpleDec dec, int level) {
        addNode(dec);
    }

    public void visit(ArrayDec dec, int level) {
        addNode(dec);
    }

    public void visit(FunctionDec dec, int level) {
        addNode(dec);

        visit(dec.params, level);

        ++scope;

        visit(dec.body, level);
    }

    public void visit(IfExp exp, int level) {
        visit(exp.test, level);
        
        ++scope;

        visit(exp.body, level);
        
        if (exp._else != null) {
            visit(exp._else);
        }

        --scope;
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