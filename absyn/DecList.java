package absyn;

public class DecList extends AbsynList<Dec> {
    public Dec head;
    public DecList tail;

    public DecList(Dec head, DecList tail) {
        this.head = head;
        this.tail = tail;
    }

    @Override
    public void accept(AbsynVisitor visitor, int level) {
        visitor.visit(this, level);
    }

    @Override
    public Dec getHead() {
        return head;
    }

    public AbsynList<Dec> getTail() {
        return tail;
    }
}
