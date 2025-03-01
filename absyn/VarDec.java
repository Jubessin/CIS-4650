package absyn;

public abstract class VarDec extends Dec {
    public VarDec(int row, int col, NameTy type, String name) {
        super(row, col, type, name);
    }
}
