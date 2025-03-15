package absyn;

public abstract class Exp extends Absyn {

    public int expType;

    public Exp(int row, int col) {
        this.row = row;
        this.col = col;
    }
}
