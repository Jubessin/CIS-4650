package absyn;

public class ExpList extends Absyn {
    private Exp head;
    private ExpList tail;
    
    public ExpList(Exp head, ExpList tail) {
        this.head = head;
        this.tail = tail;
    }

    public void accept(AbsynVisitor visitor, int level) {
        visitor.visit(this, level);
    }
}