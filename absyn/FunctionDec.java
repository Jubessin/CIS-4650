package absyn;

public class FunctionDec extends Dec {
    // Expression body.
    public Exp body;

    // The start address of the function.
    public int address;    

    // Parameters of the function.
    public VarDecList params;

    // Whether the function is defined, or just a prototype.
    public final boolean isPrototype;

    public FunctionDec(int row, int col, NameTy type, String name, VarDecList params, Exp body) {
        super(row, col, type, name);
        this.params = params;
        this.body = body;

        isPrototype = body == null || body instanceof NilExp;
    }

    @Override
    public void accept(AbsynVisitor visitor, int level, boolean isAddress) {
        visitor.visit(this, level, isAddress);
    }

    @Override
    public String toString() {
        String paramString = params == null ? "" : params.toString();
        return name + ": (" + paramString + ") -> " + type.toString();
    }
}
