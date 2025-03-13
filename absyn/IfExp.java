package absyn;

public class IfExp extends Exp {

    public Exp test;
    public Exp body;
    public Exp _else;

    public IfExp(int row, int col, Exp test, Exp body, Exp _else) {
        super(row, col);
        this.test = test;
        this.body = body;
        this._else = _else;
    }

    @Override
    public void accept(AbsynVisitor visitor, int level) {
        visitor.visit(this, level);
    }
}
