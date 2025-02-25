package absyn;

public class ArrayDec extends VarDec {
    public int size;
    public ArrayDec(int row, int col, NameTy type, String name, int size) {
        super(row, col, type, name);
        this.size = size;
    }

    public void accept(AbsynVisitor visitor, int level) {
        visitor.visit(this, level);
    }
}
