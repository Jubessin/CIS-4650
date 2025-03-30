package absyn;

public class SimpleDec extends VarDec {

    public SimpleDec(int row, int col, NameTy type, String name) {
        super(row, col, type, name);
    }

    @Override
    public void accept(AbsynVisitor visitor, int level, boolean isAddress) {
        visitor.visit(this, level, isAddress);
    }

    @Override
    public String toString() {
        return name + ": " + type.toString();
    }
}
