package absyn;

abstract public class Dec extends Absyn {
    public NameTy type;
    public String name;
    public Dec(int row, int col, NameTy type, String name) {
        this.row = row;
        this.col = col;
        this.type = type;
        this.name = name;
    }
}
