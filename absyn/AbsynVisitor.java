package absyn;

public interface AbsynVisitor {
    public void visit(Dec exp, int level);
    public void visit(IntExp exp, int level);
    public void visit(NameTy exp, int level);
    public void visit(NilExp exp, int level);
    public void visit(BoolExp exp, int level);
    public void visit(DecList exp, int level);
    public void visit(SimpleDec exp, int level);
    
    public void visit(IndexVar var, int level);
    public void visit(SimpleVar var, int level);

    public void visit(ExpList list, int level);
}