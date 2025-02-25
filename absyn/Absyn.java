package absyn;

abstract public class Absyn {
    public int row, col;
    
    public abstract void accept(AbsynVisitor visitor, int level);
}
