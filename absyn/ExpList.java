package absyn;

import java.util.List;

public class ExpList extends AbsynList<Exp> {

    public Exp head;
    public ExpList tail;

    public ExpList(Exp head, ExpList tail) {
        this.head = head;
        this.tail = tail;
    }

    @Override
    public void accept(AbsynVisitor visitor, int level) {
        visitor.visit(this, level);
    }

    @Override
    public Exp getHead() {
        return head;
    }

    public String toString(AbsynVisitor visitor, int level) {
        Error.printError = false;
        String expString = "";
        List<Exp> list = this.getFlattened();

        for (Exp exp : list) {
            exp.accept(visitor, level);
            expString += NameTy.getString(exp.expType) + ", ";
        }
        Error.printError = true;
        return expString.substring(0, expString.length() - 2);
    }

    @Override
    public AbsynList<Exp> getTail() {
        return tail;
    }
}
