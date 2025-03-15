package absyn;

abstract public class Absyn {

    public int row, col;

    public abstract void accept(AbsynVisitor visitor, int level);

    public String lineToString() {
        return " - at line " + (this.row + 1) + ", column " + (this.col + 1);
    }
}
