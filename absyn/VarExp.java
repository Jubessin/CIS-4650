package absyn;

public class VarExp extends Exp {
    public Var _var;

    public VarExp(int row, int col, Var _var) {
        super(row, col);
        this._var = _var;
    }

    public void accept(AbsynVisitor visitor, int level) {
        visitor.visit(this, level);
    }
}