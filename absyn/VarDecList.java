package absyn;

import java.util.List;

public class VarDecList extends AbsynList<VarDec> {

    public VarDec head;
    public VarDecList tail;

    public VarDecList(VarDec head, VarDecList tail) {
        this.head = head;
        this.tail = tail;
    }

    @Override
    public void accept(AbsynVisitor visitor, int level) {
        visitor.visit(this, level);
    }

    @Override
    public String toString() {
        String paramString = "";
        List<VarDec> list = this.getFlattened();

        for (VarDec dec : list) {
            paramString += dec.type.toString() + ", ";
        }
        return paramString.substring(0, paramString.length() - 2);
    }

    @Override
    public VarDec getHead() {
        return head;
    }

    @Override
    public AbsynList<VarDec> getTail() {
        return tail;
    }
}
