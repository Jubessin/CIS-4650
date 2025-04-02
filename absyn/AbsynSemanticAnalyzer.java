package absyn;

import java.io.*;
import java.util.*;

@SuppressWarnings("StringConcatenationInsideStringBufferAppend")
public class AbsynSemanticAnalyzer implements AbsynVisitor {

    private static final HashMap<String, ArrayList<Node>> table = new HashMap<>();
    private static final StringBuilder tableBuilder = new StringBuilder();
    private static final int INDENT = 4;

    private FunctionDec currentFunction;

    private static void print(int level, String message) {
        indent(level);
        tableBuilder.append(message + "\n");
    }

    private static void insert(Dec dec, int level) {
        if (lookup(dec.name) != null && lookup(dec.name).level == level) {
            Error.variableRedeclaration(dec);
            return;
        }
        ArrayList<Node> list = table.getOrDefault(dec.name, null);
        if (list == null) {
            table.put(dec.name, new ArrayList<>());
        }

        list = table.getOrDefault(dec.name, null);
        list.add(new Node(dec.name, dec, level));
    }

    private static void insert(FunctionDec dec, int level) {
        Node node = lookup(dec.name);
        if (node != null) {
            if (node.dec instanceof FunctionDec != true) {
                Error.variableRedeclaration(dec);
                return;
            }
            FunctionDec functionDec = (FunctionDec) node.dec;
            String paramString = functionDec.params == null ? "" : functionDec.params.toString();
            String decString = dec.params == null ? "" : dec.params.toString();

            if ((functionDec.type.type != dec.type.type)
                    || !paramString.equals(decString)) {
                Error.prototypeRedefinition(dec);
                return;
            } else if (!functionDec.isPrototype && !dec.isPrototype) {
                Error.functionRedefinition(dec);
                return;
            }

            node.dec = dec;
            return;
        }
        insert((Dec) dec, level);
    }

    private static Node lookup(String id) {
        ArrayList<Node> list = table.getOrDefault(id, null);
        if (list != null) {
            if (list.isEmpty()) {
                table.remove(id);
                return null;
            }
            return list.get(list.size() - 1);
        }
        return null;
    }

    private static void delete(int level) {
        for (HashMap.Entry<String, ArrayList<Node>> list : table.entrySet()) {
            list.getValue().removeIf(node -> node.level == level);
        }
    }

    private static void indent(int level) {
        tableBuilder.append(" ".repeat(level * INDENT));
    }

    private String expListToString(ExpList list, int level) {
        String expString = "";
        List<Exp> flatList = list.getFlattened();

        for (Exp exp : flatList) {
            exp.accept(this, level, false);
            expString += NameTy.getString(exp.expType);
            expString += isArrayExp(exp) ? "[]" : "";
            expString += ", ";
        }
        return expString.substring(0, expString.length() - 2);
    }

    private void addPredefinedFunctions() {
        VarDecList list = new VarDecList(new SimpleDec(0, 0, new NameTy(0, 0, NameTy.INT), "value"), null);
        insert(new FunctionDec(0, 0, new NameTy(0, 0, NameTy.INT), "input", null, new NilExp(0, 0)), 0);
        insert(new FunctionDec(0, 0, new NameTy(0, 0, NameTy.VOID), "output", list, new NilExp(0, 0)), 0);
    }

    private boolean isArrayExp(Exp exp) {
        return exp instanceof VarExp && ((VarExp) exp).isArray;
    }

    public void serialize(String file) throws FileNotFoundException, UnsupportedEncodingException {
        try (PrintWriter writer = new PrintWriter(file.replace(".cm", ".sym"), "UTF-8")) {
            writer.println(tableBuilder.toString());
        }
    }

    @Override
    public void visit(SimpleDec dec, int level, boolean isAddress) {
        if (dec.type.type == NameTy.VOID) {
            Error.invalidTypeDeclaration(dec);
            dec.type.type = NameTy.INT;
        }

        insert(dec, level);
    }

    @Override
    public void visit(ArrayDec dec, int level, boolean isAddress) {
        if (dec.type.type == NameTy.VOID) {
            Error.invalidTypeDeclaration(dec);
            dec.type.type = NameTy.INT;
        }

        insert(dec, level);
    }

    @Override
    public void visit(FunctionDec dec, int level, boolean isAddress) {
        currentFunction = dec;
        insert(dec, level);

        if (dec.body instanceof NilExp) {
            return;
        }

        if (dec.params != null) {
            dec.params.accept(this, level + 1, false);
        }

        print(level, "Entering the scope for function " + dec.name + ":");

        dec.body.accept(this, level + 1, false);

        delete(level + 1);
        print(level, "Leaving the function scope");
    }

    @Override
    public void visit(CompoundExp exp, int level, boolean isAddress) {
        if (exp.decs != null) {
            exp.decs.accept(this, level, false);
        }

        for (Exp item : exp.exps.getFlattened()) {
            item.accept(this, level, false);
        }

        for (HashMap.Entry<String, ArrayList<Node>> list : table.entrySet()) {
            for (Node node : list.getValue()) {
                Dec dec = node.dec;
                if (node.level == level) {
                    print(level, dec.toString());
                }
            }
        }

        delete(level);
    }

    @Override
    public void visit(OpExp exp, int level, boolean isAddress) {
        Exp left = exp.left, right = exp.right;
        left.accept(this, level, false);
        right.accept(this, level, false);

        switch (exp.operationType) {
            case OpExp.isRelationalOperation -> {
                exp.expType = NameTy.BOOL;
                if (left.expType != NameTy.INT || right.expType != NameTy.INT) {
                    Error.invalidRelationalOperation(exp);
                }
            }
            case OpExp.isArithmeticOperation -> {
                boolean typeMismatch;
                exp.expType = NameTy.INT;

                typeMismatch = (left.expType != NameTy.INT || right.expType != NameTy.INT)
                        || (isArrayExp(left) || isArrayExp(right));
                if (typeMismatch) {
                    Error.invalidArithmeticOperation(exp);
                }
            }
            case OpExp.isBooleanOperation -> {
                exp.expType = NameTy.BOOL;
                if (exp.op != OpExp.MINUS && exp.op != OpExp.UMINUS) {
                    if (left.expType == NameTy.VOID || right.expType == NameTy.VOID) {
                        Error.invalidBooleanOperation(exp);
                    }
                }
            }
            default -> {
                System.err.println("");
            }
        }
    }

    @Override
    public void visit(IntExp exp, int level, boolean isAddress) {
    }

    @Override
    public void visit(NilExp exp, int level, boolean isAddress) {
    }

    @Override
    public void visit(VarExp exp, int level, boolean isAddress) {
        Node variable = lookup(exp._var.name);
        if (variable == null) {
            Error.variableDoesNotExist(exp);
            return;
        }

        if (variable.dec instanceof ArrayDec && exp._var instanceof SimpleVar) {
            exp.isArray = true;
        }
        exp.expType = variable.dec.type.type;
        exp._var.accept(this, level, false);
    }

    @Override
    public void visit(BoolExp exp, int level, boolean isAddress) {
    }

    @Override
    public void visit(IfExp exp, int level, boolean isAddress) {
        exp.test.accept(this, level, false);

        if (exp.test.expType == NameTy.VOID) {
            Error.invalidConditionType(exp);
        }

        print(level, "Entering a new if block:");
        exp.body.accept(this, level + 1, false);
        print(level, "Leaving the if block");

        if (!(exp._else instanceof NilExp)) {
            print(level, "Entering a new else block:");
            exp._else.accept(this, level + 1, false);
            print(level, "Leaving the else block");
        }
    }

    @Override
    public void visit(WhileExp exp, int level, boolean isAddress) {
        exp.test.accept(this, level, false);

        if (exp.test.expType == NameTy.VOID) {
            Error.invalidConditionType(exp);
        }

        print(level, "Entering a new while block:");
        exp.body.accept(this, level + 1, false);
        print(level, "Leaving the while block");
    }

    @Override
    public void visit(AssignExp exp, int level, boolean isAddress) {
        VarExp left = exp.left;
        Exp right = exp.right;

        left.accept(this, level, false);
        right.accept(this, level, false);

        if (left.expType != right.expType) {
            Error.invalidAssignExpression(exp);
        }
    }

    @Override
    public void visit(ReturnExp exp, int level, boolean isAddress) {
        exp.exp.accept(this, level, false);
        if (currentFunction.type.type != exp.exp.expType) {
            Error.invalidReturnType(exp, currentFunction.type);
        }
    }

    @Override
    public void visit(CallExp exp, int level, boolean isAddress) {
        String functionName = exp.func;
        Node node = lookup(functionName);

        if (node == null) {
            Error.functionDoesNotExit(exp);
            return;
        }

        ExpList callParameterList = exp.args;
        FunctionDec functionDec = (FunctionDec) node.dec;
        String paramString = functionDec.params == null ? "" : functionDec.params.toString();
        String argString = callParameterList == null ? "" : expListToString(callParameterList, level);
        exp.expType = functionDec.type.type;

        if (!paramString.equals(argString)) {
            Error.invalidCallArgumentType(exp, argString, paramString);

        }
    }

    @Override
    public void visit(NameTy type, int level, boolean isAddress) {
    }

    @Override
    public void visit(DecList list, int level, boolean isAddress) {
        print(level++, "Entering the global scope:");

        addPredefinedFunctions();
        for (Dec item : list.getFlattened()) {
            item.accept(this, level, false);
        }

        for (HashMap.Entry<String, ArrayList<Node>> decList : table.entrySet()) {
            for (Node node : decList.getValue()) {
                Dec dec = node.dec;
                if (dec instanceof FunctionDec functionDec) {
                    if (functionDec.body instanceof NilExp) {
                        switch (functionDec.name) {
                            case "input" -> {
                            }
                            case "output" -> {
                            }
                            default ->
                                Error.incompleteFunctionDefinition(functionDec);
                        }
                    }
                }
                // print(level, dec.toString());
            }
        }

        if (table.get("main") == null) {
            Error.missingMain();
        }

        delete(level);
        print(--level, "Leaving the global scope");
    }

    @Override
    public void visit(ExpList list, int level, boolean isAddress) {
        for (Exp item : list.getFlattened()) {
            item.accept(this, level, false);
        }
    }

    @Override
    public void visit(VarDecList list, int level, boolean isAddress) {
        for (VarDec item : list.getFlattened()) {
            item.accept(this, level, false);
        }
    }

    @Override
    public void visit(IndexVar var, int level, boolean isAddress) {
        var.exp.accept(this, level, false);
        if (var.exp.expType != NameTy.INT) {
            Error.invalidIndexType(var);
        }
    }

    @Override
    public void visit(SimpleVar var, int level, boolean isAddress) {
    }
}
