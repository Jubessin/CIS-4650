package absyn;

public class AbsynRenderer implements AbsynVisitor {
    private static final int INDENT = 4;
    
    private static void indent(int level) {
        System.out.print(" ".repeat(level * INDENT));
    }
    
    public void visit(Dec dec, int level) {

    }

    public void visit(SimpleDec dec, int level) {

    }

    public void visit(FunctionDec dec, int level) {

    }

    public void visit(IfExp exp, int level) {

    }

    public void visit(OpExp exp, int level) {
        indent(level);
        
        System.out.print("OpExp");

        switch (exp.op) {
            case OpExp.LT: System.out.print(" < "); break;
            case OpExp.GT: System.out.print(" > "); break;
            case OpExp.EQ: System.out.print(" == "); break;
            case OpExp.NE: System.out.print(" != "); break;
            case OpExp.LE: System.out.print(" <= "); break;
            case OpExp.GE: System.out.print(" >= "); break;
            case OpExp.OR: System.out.print(" || "); break;
            case OpExp.AND: System.out.print(" && "); break;
            case OpExp.PLUS: System.out.println(" + "); break;
            case OpExp.DIVIDE: System.out.print(" / "); break;
            case OpExp.MINUS: System.out.println(" - "); break;
            case OpExp.MULTIPLY: System.out.print(" * "); break;
            default: System.out.println( "Unrecognized operator at line " + exp.row + " and column " + exp.col); break;
        }

        level++;

        if (exp.left != null) {
            exp.left.accept(this, level);
        }

        if (exp.right != null) {
            exp.right.accept(this, level);
        }
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
