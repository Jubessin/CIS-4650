package absyn;

public abstract class Var extends Absyn {
    public int pos;

    public Var(int pos) {
        this.pos = pos;    
    }
}