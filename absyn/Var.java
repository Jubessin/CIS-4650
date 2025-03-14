package absyn;

public abstract class Var extends Absyn {

    String name;

    public Var(int row, int col, String name) {
        this.row = row;
        this.col = col;
        this.name = name;
    }
}
