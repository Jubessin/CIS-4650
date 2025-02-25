package absyn;

public abstract class Var extends Absyn {
    public Var(int row, int col) {
        this.row = row;
        this.col = col;    
    }
}