package absyn;

public class VarExp extends Exp {
    private boolean value;

    public VarExp(int row, int col, boolean value) {
        this.row = row;
        this.col = col;
        this.value = value;
    }

    public void accept(AbsynVisitor visitor, int level) {
        visitor.visit(this, level);
    }
}