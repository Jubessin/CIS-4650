package absyn;

public class NameTy extends Absyn {

    public static final int BOOL = 1;
    public static final int INT = 2;
    public static final int VOID = 3;
    public static final int INVALID = 4;

    public int type;

    public NameTy(int row, int col, int type) {
        this.row = row;
        this.col = col;
        this.type = type;
    }

    @Override
    public void accept(AbsynVisitor visitor, int level) {
        visitor.visit(this, level);
    }

    public static String getString(int type) {
        switch (type) {
            case BOOL -> {
                return "bool";
            }
            case INT -> {
                return "int";
            }
            case VOID -> {
                return "void";
            }
            default -> {
                return "";
            }
        }
    }

    @Override
    public String toString() {
        switch (this.type) {
            case BOOL -> {
                return "bool";
            }
            case INT -> {
                return "int";
            }
            case VOID -> {
                return "void";
            }
            default -> {
                return "";
            }
        }
    }
}
