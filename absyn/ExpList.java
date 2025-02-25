package absyn;

public class ExpList {
    public Exp head;
    public Exp tail;

    public ExpList(Exp head, Exp tail) {
        this.head = head;
        this.tail = tail;
    }
}