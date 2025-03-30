package absyn;

public class AssignExp extends Exp {
    public Exp right;
    public VarExp left;

    public AssignExp(int row, int col, VarExp left, Exp right) {
        super(row, col);
        this.left = left;
        this.right = right;
    }

    public void accept(AbsynVisitor visitor, int level, boolean isAddress) {
        visitor.visit(this, level, isAddress);
    }
}
