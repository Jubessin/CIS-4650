package absyn;

public class ArrayDec extends VarDec {

    public int size;

    public ArrayDec(int row, int col, NameTy type, String name, int size) {
        super(row, col, type, name);
        this.size = size;
    }

    @Override
    public void accept(AbsynVisitor visitor, int level) {
        visitor.visit(this, level);
    }

    @Override
    public String toString() {
        return name + ": " + type.toString();
    }
}
