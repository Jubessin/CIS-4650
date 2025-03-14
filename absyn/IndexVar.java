package absyn;

public class IndexVar extends Var {

    public Exp exp;

    public IndexVar(int row, int col, String name, Exp exp) {
        super(row, col, name);
        this.exp = exp;
    }

    @Override
    public void accept(AbsynVisitor visitor, int level) {
        visitor.visit(this, level);
    }
}
