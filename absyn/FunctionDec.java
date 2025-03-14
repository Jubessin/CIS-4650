package absyn;

public class FunctionDec extends Dec {

    public Exp body;
    public VarDecList params;
    
    public final boolean isPrototype;

    public FunctionDec(int row, int col, NameTy type, String name, VarDecList params, Exp body) {
        super(row, col, type, name);
        this.params = params;
        this.body = body;

        isPrototype = body == null || body instanceof NilExp;
    }

    @Override
    public void accept(AbsynVisitor visitor, int level) {
        visitor.visit(this, level);
    }

    @Override
    public String toString() {
        String paramString = params == null ? "" : params.toString();
        return name + ": (" + paramString + ") -> " + type.toString();
    }
}
