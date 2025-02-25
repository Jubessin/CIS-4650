package absyn;

public interface AbsynVisitor {
    public void visit(ExpList exp, int level);
    public void visit(BoolExp exp, int level);
    public void visit(VarExp exp, int level);
    public void visit(TypeSpecifier exp, int level);
}