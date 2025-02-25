package absyn;

public class NameTy extends Absyn {
    public static int Bool = 1;
    public static int Int = 2;
    public static int Void = 3;
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