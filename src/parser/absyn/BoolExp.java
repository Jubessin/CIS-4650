package absyn;

public class BoolExp extends Exp {
    private boolean value;

    public BoolExp(boolean value) {
        this.value = value;
    }

    public void accept(AbsynVisitor visitor, int level) {
        visitor.visit(this, level);
    }
}