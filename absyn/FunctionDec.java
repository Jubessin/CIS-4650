package absyn;

public class FunctionDec extends Dec {

    public Exp body;
    public VarDecList params;

    public FunctionDec(int row, int col, NameTy type, String name, VarDecList params, Exp body) {
        super(row, col, type, name);
        this.params = params;
        this.body = body;
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
