package absyn;

public class CompoundExp extends Exp {

    public ExpList exps;
    public VarDecList decs;

    public CompoundExp(int row, int col, VarDecList decs, ExpList exps) {
        super(row, col);
        this.exps = exps;
        this.decs = decs;
    }

    @Override
    public void accept(AbsynVisitor visitor, int level, boolean isAddress) {
        visitor.visit(this, level, isAddress);
    }
}
