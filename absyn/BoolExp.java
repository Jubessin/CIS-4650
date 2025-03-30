package absyn;

public class BoolExp extends Exp {

    public boolean value;

    public BoolExp(int row, int col, boolean value) {
        super(row, col);
        this.value = value;
        this.expType = NameTy.BOOL;
    }

    @Override
    public void accept(AbsynVisitor visitor, int level, boolean isAddress) {
        visitor.visit(this, level, isAddress);
    }
}
