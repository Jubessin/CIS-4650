package absyn;

public class BoolExp extends Exp {
    boolean value;

    public BoolExp(int pos, boolean value) {
        super(pos);
        this.value = value;
    }

    public void accept(AbsynVisitor visitor, int level) {
        visitor.visit(this, level);
    }
}