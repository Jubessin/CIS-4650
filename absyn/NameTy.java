package absyn;

public class NameTy extends Absyn {
    public static final int Bool = 1;
    public static final int Int = 2;
    public static final int Void = 3;
    public static final int Invalid = 4;

    public int type;

    public NameTy(int row, int col, int type) {
        this.row = row;
        this.col = col;
        this.type = type;
    }

    public void accept(AbsynVisitor visitor, int level) {
        visitor.visit(this, level);
    }
}
