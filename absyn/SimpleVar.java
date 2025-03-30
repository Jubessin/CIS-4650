package absyn;

public class SimpleVar extends Var {

    public SimpleVar(int row, int col, String name) {
        super(row, col, name);
    }

    @Override
    public void accept(AbsynVisitor visitor, int level, boolean isAddress) {
        visitor.visit(this, level, isAddress);
    }
}
