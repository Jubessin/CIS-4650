package absyn;

public class IndexVar extends Var {
    public Exp exp;
    public String name;

    public IndexVar(int row, int col, String name, Exp exp) {
        super(row, col);
        this.name = name;
        this.exp = exp;
    }

    public void accept(AbsynVisitor visitor, int level) {
        visitor.visit(this, level);
    }
}