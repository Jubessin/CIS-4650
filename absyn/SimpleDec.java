package absyn;

public class SimpleDec extends VarDec {
    public SimpleDec(int row, int col, NameTy type, String name) {
        super(row, col, type, name);
    }

    public void accept(AbsynVisitor visitor, int level) {
        visitor.visit(this, level);
    }
}
