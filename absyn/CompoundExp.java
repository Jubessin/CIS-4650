package absyn;

public class CompoundExp extends Exp {
    public ExpList exps;
    public VarDecList decs;

    public CompoundExp(int row, int col, ExpList exps, VarDecList decs) {
        super(row, col);
        this.exps = exps;
        this.decs = decs;
    }
    
    public void accept(AbsynVisitor visitor, int level) {
        visitor.visit(this, level);
    }
} 