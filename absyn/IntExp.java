package absyn;

public class IntExp extends Exp {

    public int value;

    public IntExp(int row, int col, int value) {
        super(row, col);
        this.value = value;
        this.expType = NameTy.INT;
    }

    public void accept(AbsynVisitor visitor, int level) {
        visitor.visit(this, level);
    }
}
