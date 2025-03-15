package absyn;

public class VarExp extends Exp implements ResolvableExp {

    public Var _var;

    public VarExp(int row, int col, Var _var) {
        super(row, col);
        this._var = _var;
    }

    @Override
    public void accept(AbsynVisitor visitor, int level) {
        visitor.visit(this, level);
    }

    @Override 
    public String getResolvedName() {
        return _var.name;
    }
}
