package absyn;

public interface AbsynVisitor {
    public void visit(Dec exp, int level);
    public void visit(NameTy exp, int level);
    public void visit(NilExp exp, int level);
    public void visit(DecList exp, int level);
    public void visit(SimpleDec exp, int level);
}