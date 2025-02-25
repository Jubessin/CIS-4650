package absyn;

public abstract class Exp extends Absyn {
    public int pos;

    public Exp(int pos) {
        this.pos = pos;
    }
}