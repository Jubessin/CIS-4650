package absyn;

public class NilExp extends Exp {

    public NilExp(int row, int col) {
        super(row, col);
        this.expType = NameTy.VOID;
    }

    public void accept(AbsynVisitor visitor, int level, boolean isAddress) {
        visitor.visit(this, level, isAddress);
    }
}
