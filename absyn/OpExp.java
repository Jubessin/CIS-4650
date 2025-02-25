package absyn;

public class OpExp extends Exp {
    public int op;
    public Exp left;
    public Exp right;

    public OpExp(int row, int col, Exp left, int op, Exp right) {
        super(row, col);
        this.op = op;
        this.left = left;
        this.right = right;
    }

    public void accept(AbsynVisitor visitor, int level) {
        visitor.visit(this, level);
    }
}