package absyn;

public class ExpList extends Absyn {
    public Exp head;
    public Exp tail;

    public ExpList(Exp head, Exp tail) {
        this.head = head;
        this.tail = tail;
    }

    public void accept(AbsynVisitor visitor, int level) {
        visitor.visit(this, level);
    }
}