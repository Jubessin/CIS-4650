package absyn;

import java.io.*;

public class AbsynCodeGenerator implements AbsynVisitor {
    public void serialize(String file) throws FileNotFoundException, UnsupportedEncodingException {
        try (PrintWriter writer = new PrintWriter(file.replace(".cm", ".tm"), "UTF-8")) {
            // writer.println(tableBuilder.toString());
        }
    }

    @Override
    public void visit(SimpleDec dec, int level) {

    }

    @Override
    public void visit(ArrayDec dec, int level) {

    }

    @Override
    public void visit(FunctionDec dec, int level) {

    }

    @Override
    public void visit(IfExp exp, int level) {

    }

    @Override
    public void visit(OpExp exp, int level) {

    }

    @Override
    public void visit(IntExp exp, int level) {

    }

    @Override
    public void visit(NilExp exp, int level) {

    }

    @Override
    public void visit(VarExp exp, int level) {

    }

    @Override
    public void visit(BoolExp exp, int level) {

    }

    @Override
    public void visit(WhileExp exp, int level) {

    }

    @Override
    public void visit(AssignExp exp, int level) {

    }

    @Override
    public void visit(ReturnExp exp, int level) {

    }

    @Override
    public void visit(CompoundExp exp, int level) {

    }

    @Override
    public void visit(CallExp exp, int level) {

    }

    @Override
    public void visit(NameTy type, int level) {

    }

    @Override
    public void visit(DecList list, int level) {

    }

    @Override
    public void visit(ExpList list, int level) {

    }

    @Override
    public void visit(VarDecList list, int level) {

    }

    @Override
    public void visit(IndexVar var, int level) {

    }

    @Override
    public void visit(SimpleVar var, int level) { 

    }
}