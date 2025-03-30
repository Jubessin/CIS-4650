package absyn;

public abstract class VarDec extends Dec {
    // Whether the variable is declared locally or globally.
    public boolean global;

    // The offset within the related stackframe for memory access.
    public int frameOffset;
    
    public VarDec(int row, int col, NameTy type, String name) {
        super(row, col, type, name);
    }
}
