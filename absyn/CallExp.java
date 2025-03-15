package absyn;

public class CallExp extends Exp implements ResolvableExp {

    public String func;
    public ExpList args;

    public CallExp(int row, int col, String func, ExpList args) {
        super(row, col);
        this.func = func;
        this.args = args;
    }

    @Override
    public void accept(AbsynVisitor visitor, int level) {
        visitor.visit(this, level);
    }

    @Override
    public String getResolvedName() {
        return func;
    }
}
