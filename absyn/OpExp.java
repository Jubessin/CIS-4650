package absyn;

public class OpExp extends Exp {
    public int op;
    public Exp left;
    public Exp right;

    public static final int PLUS = 1;
    public static final int MINUS = 2;
    public static final int UMINUS = 3;
    public static final int MULTIPLY = 4;
    public static final int DIVIDE = 5;
    public static final int EQ = 6;
    public static final int NE = 7;
    public static final int LT = 8;
    public static final int LE = 9;
    public static final int GT = 10;
    public static final int GE = 11;
    public static final int NOT = 12;
    public static final int AND = 13;
    public static final int OR = 14;

    public OpExp(int row, int col, Exp left, int op, Exp right) {
        super(row, col);
        this.op = op;
        this.left = left;
        this.right = right;
    }

    public void accept(AbsynVisitor visitor, int level) {
        visitor.visit(this, level);
    }
}