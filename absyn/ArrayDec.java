package absyn;

public class ArrayDec extends VarDec {

    public int size;

    public ArrayDec(int row, int col, NameTy type, String name, int size) {
        super(row, col, type, name);
        this.size = size;
    }

    @Override
    public void accept(AbsynVisitor visitor, int level, boolean isAddress) {
        visitor.visit(this, level, isAddress);
    }

    @Override
    public String toString() {
        String number = size > 0 ? Integer.toString(size) : "";
        return name + ": " + type.toString() + "[" + number + "]";
    }
}
