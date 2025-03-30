package absyn;

public class VarExp extends Exp implements ResolvableExp {

    public Var _var;
    public boolean isArray = false;

    public VarExp(int row, int col, Var _var) {
        super(row, col);
        this._var = _var;
    }

    @Override
    public void accept(AbsynVisitor visitor, int level, boolean isAddress) {
        visitor.visit(this, level, isAddress);
    }

    @Override
    public String getResolvedName() {
        return _var.name;
    }
}
