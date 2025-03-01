package absyn;

public class CompoundExp extends Exp {
    public ExpList exps;
    public VarDecList decs;

    public CompoundExp(int row, int col, VarDecList decs, ExpList exps) {
        super(row, col);
        this.exps = exps;
        this.decs = decs;
    }
    
    public void accept(AbsynVisitor visitor, int level) {
        visitor.visit(this, level);
    }
}
