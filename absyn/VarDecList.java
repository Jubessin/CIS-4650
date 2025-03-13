package absyn;

public class VarDecList extends Absyn {

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
        VarDecList iter = new VarDecList(this.head, this.tail);
        while (iter != null) {
            if (iter.head != null) {
                paramString += iter.head.type.toString() + ",";
            }
            iter = iter.tail;
        }
        return paramString.substring(0, paramString.length() - 1);
    }
}
