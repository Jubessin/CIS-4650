package absyn;

public class ExpList extends AbsynList<Exp> {

    public Exp head;
    public ExpList tail;

    public ExpList(Exp head, ExpList tail) {
        this.head = head;
        this.tail = tail;
    }

    @Override
    public void accept(AbsynVisitor visitor, int level, boolean isAddress) {
        visitor.visit(this, level, isAddress);
    }

    @Override
    public Exp getHead() {
        return head;
    }

    @Override
    public AbsynList<Exp> getTail() {
        return tail;
    }
}
