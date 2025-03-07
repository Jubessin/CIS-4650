package absyn;

import java.io.*;

public class AbsynTreeBuilder implements AbsynVisitor {
    private static final int INDENT = 4;

    private static StringBuilder sb;

    public AbsynTreeBuilder() {
        sb = new StringBuilder();
    }
    
    public void flush(String file) throws FileNotFoundException, UnsupportedEncodingException {
        if (file == null) {
            System.out.println(sb.toString());
        }
        else {
            file = file.replace(".cm", ".abs");

            // System.out.println("saving to " + file);
            var writer = new PrintWriter(file, "UTF-8");
            writer.println(sb.toString());
            writer.close();
        }
    }
    
    public void visit(Dec dec, int level) {
        print(level, "Dec: " + dec.name, dec.type);
    }

    public void visit(SimpleDec dec, int level) {
        print(level, "SimpleDec: " + dec.name, dec.type);
    }

    public void visit(ArrayDec dec, int level) {
        print(level, "ArrayDec: " + dec.name, dec.type);
    }

    public void visit(FunctionDec dec, int level) {
        print(level++, "FunctionDec: " + dec.name, dec.type);
        if (dec.params != null) {
            print(level, "Parameters: ", dec.params);
        } else {
            print(level, "Parameters: Void");
        }
        print(level, "Body: ", dec.body);
    }

    public void visit(IfExp exp, int level) {
        print(level++, "IfExp: ");
        print(level, "Test: ", exp.test);
        print(level, "Body: ", exp.body);

        if (exp._else != null) {
            print(level, "Else: ", exp._else);
        }
    }

    public void visit(OpExp exp, int level) {
        print(
            level, 
            "OpExp: " + switch (exp.op) {
                case OpExp.LT -> "<";
                case OpExp.GT -> ">";
                case OpExp.EQ -> "==";
                case OpExp.NE -> "!=";
                case OpExp.LE -> "<=";
                case OpExp.GE -> ">=";
                case OpExp.OR -> "||";
                case OpExp.AND -> "&&";
                case OpExp.PLUS -> "+";
                case OpExp.MINUS -> "-";
                case OpExp.DIVIDE -> "/";
                case OpExp.MULTIPLY -> "*";
                default -> "Unrecognized operator at line " + exp.row + ", column " + exp.col;
            }, 
            exp.left, 
            exp.right
        );
    }

    public void visit(IntExp exp, int level) {
        print(level, "IntExp: " + exp.value);
    }

    public void visit(NilExp exp, int level) {
        print(level, "NilExp");
    }

    public void visit(VarExp exp, int level) {
        print(level, "VarExp: ", exp._var);
    }

    public void visit(BoolExp exp, int level) {
        print(level, "BoolExp: " + exp.value);
    }

    public void visit(WhileExp exp, int level) {
        print(level++, "WhileExp: ");
        print(level, "Test: ", exp.test);
        print(level, "Body: ", exp.body);
    }

    public void visit(AssignExp exp, int level) {
        print(level, "AssignExp: ", exp.left, exp.right);
    }

    public void visit(ReturnExp exp, int level) {
        print(level, "ReturnExp: ", exp.exp);
    }

    public void visit(CompoundExp exp, int level) {
        print(level, "CompoundExp: ", exp.decs, exp.exps);
    }

    public void visit(CallExp exp, int level) {
        print(level, "CallExp: " + exp.func, exp.args);
    }

    public void visit(NameTy type, int level) {
        print(level, "NameTy: " + switch (type.type) {
            case NameTy.Void -> "Void";
            case NameTy.Int -> "Integer";
            case NameTy.Bool -> "Boolean";
            case NameTy.Invalid -> "Invalid";
            default -> "Unrecognized type at line " + type.row + ", column " + type.col;
        });
    }

    public void visit(DecList list, int level) {
        while (list != null) {
            if (list.head != null) {
                list.head.accept(this, level);
            }
            list = list.tail;
        }
    }

    public void visit(ExpList list, int level) {
        while (list != null) {
            if (list.head != null) {
                list.head.accept(this, level);
            }
            list = list.tail;
        }
    }

    public void visit(VarDecList list, int level) {
        while (list != null) {
            if (list.head != null) {
                list.head.accept(this, level);
            }
            list = list.tail;
        }
    }

    public void visit(IndexVar _var, int level) {
        print(level, "IndexVar: " + _var.name, _var.exp);
    }

    public void visit(SimpleVar _var, int level) {
        print(level, "SimpleVar: " + _var.name);
    }

    private void print(int level, String message, Absyn... tree) {
        indent(level);

        sb.append(message + "\n");
        // System.out.println(message);

        for (Absyn node : tree) {
            if (node == null)
                continue;

            node.accept(this, level + 1);
        }
    }

    private static void indent(int level) {
        sb.append(" ".repeat(level * INDENT));
        // System.out.print(" ".repeat(level * INDENT));
    }
}
