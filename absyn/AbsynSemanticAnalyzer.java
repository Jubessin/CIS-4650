package absyn;

import java.io.*;
import java.util.*;

@SuppressWarnings("StringConcatenationInsideStringBufferAppend")
public class AbsynSemanticAnalyzer implements AbsynVisitor {

    private static final HashMap<String, ArrayList<NodeType>> table = new HashMap<>();
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
        ArrayList<NodeType> list = table.getOrDefault(dec.name, null);
        if (list == null) {
            table.put(dec.name, new ArrayList<>());
        }

        list = table.getOrDefault(dec.name, null);
        list.add(new NodeType(dec.name, dec, level));
    }

    // needs to be completed, I think
    private static void insert(FunctionDec dec, int level) {
        if (dec.name.equals("output") || dec.name.equals("input")) {
            Error.redefinePredefinedFunction(dec);
            return;
        }
        NodeType node = lookup(dec.name);
        if (node != null) {
            FunctionDec functionDec = (FunctionDec) node.dec;
            String paramString = functionDec.params == null ? "" : functionDec.params.toString();
            String decString = dec.params == null ? "" : dec.params.toString();

            if (functionDec.type.type != dec.type.type) {
                Error.prototypeRedefinition(dec);
            }
            if (!paramString.equals(decString)) {
                Error.prototypeRedefinition(dec);
            }
            if (!functionDec.isPrototype && !dec.isPrototype) {
                Error.functionRedefinition(dec);
            }

            node.dec = dec;
            return;
        }
        insert((Dec) dec, level);
    }

    private static NodeType lookup(String id) {
        ArrayList<NodeType> list = table.getOrDefault(id, null);
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
        for (HashMap.Entry<String, ArrayList<NodeType>> list : table.entrySet()) {
            list.getValue().removeIf(node -> node.level == level);
        }
    }

    private static void indent(int level) {
        tableBuilder.append(" ".repeat(level * INDENT));
    }

    public void serialize(String file) throws FileNotFoundException, UnsupportedEncodingException {
        try (PrintWriter writer = new PrintWriter(file.replace(".cm", ".sym"), "UTF-8")) {
            writer.println(tableBuilder.toString());
        }
    }

    public String expListToString(ExpList list, int level) {
        Error.printError = false;
        String expString = "";
        List<Exp> flatList = list.getFlattened();

        for (Exp exp : flatList) {
            exp.accept(this, level);
            expString += NameTy.getString(exp.expType);
            expString += isArrayExp(exp) ? "[]" : "";
            expString += ", ";
        }
        Error.printError = true;
        return expString.substring(0, expString.length() - 2);
    }

    @Override
    public void visit(DecList list, int level) {
        print(level++, "Entering the global scope:");

        for (Dec item : list.getFlattened()) {
            item.accept(this, level);
        }

        for (HashMap.Entry<String, ArrayList<NodeType>> decList : table.entrySet()) {
            for (NodeType node : decList.getValue()) {
                Dec dec = node.dec;
                print(level, dec.toString());
            }
        }

        delete(level);
        print(--level, "Leaving the global scope");
    }

    @Override
    public void visit(SimpleDec dec, int level) {
        if (dec.type.type == NameTy.VOID) {
            Error.invalidTypeDeclaration(dec);
            dec.type.type = NameTy.INT;
        }

        insert(dec, level);
    }

    @Override
    public void visit(ArrayDec dec, int level) {
        if (dec.type.type == NameTy.VOID) {
            Error.invalidTypeDeclaration(dec);
            dec.type.type = NameTy.INT;
        }

        insert(dec, level);
    }

    @Override
    public void visit(FunctionDec dec, int level) {
        currentFunction = dec;
        insert(dec, level);

        if (dec.body instanceof NilExp) {
            return;
        }

        if (dec.params != null) {
            dec.params.accept(this, level + 1);
        }

        print(level, "Entering the scope for function " + dec.name + ":");

        dec.body.accept(this, level + 1);

        delete(level + 1);
        print(level, "Leaving the function scope");
    }

    @Override
    public void visit(CompoundExp exp, int level) {
        if (exp.decs != null) {
            exp.decs.accept(this, level);
        }

        for (Exp item : exp.exps.getFlattened()) {
            item.accept(this, level);
        }

        for (HashMap.Entry<String, ArrayList<NodeType>> list : table.entrySet()) {
            for (NodeType node : list.getValue()) {
                Dec dec = node.dec;
                if (node.level == level) {
                    print(level, dec.toString());
                }
            }
        }

        delete(level);
    }

    @Override
    public void visit(VarDecList list, int level) {
        for (VarDec item : list.getFlattened()) {
            item.accept(this, level);
        }
    }

    public boolean isArrayExp(Exp exp) {
        return exp instanceof VarExp && ((VarExp) exp).isArray;
    }

    @Override
    public void visit(OpExp exp, int level) {
        Exp left = exp.left, right = exp.right;
        left.accept(this, level);
        right.accept(this, level);

        switch (exp.operationType) {
            case OpExp.isRelationalOperation -> {
                exp.expType = NameTy.BOOL;
                if (left.expType != NameTy.INT || right.expType != NameTy.INT) {
                    Error.invalidRelationalOperation(exp);
                }
            }
            case OpExp.isArithmeticOperation -> {
                // need to implement unary
                boolean typeMismatch;
                exp.expType = NameTy.INT;

                typeMismatch = (left.expType != NameTy.INT || right.expType != NameTy.INT)
                        || (isArrayExp(left) || isArrayExp(right));
                if (typeMismatch) {
                    Error.invalidArithmeticOperation(exp);
                }
            }
            case OpExp.isBooleanOperation -> {
                // might need to change later
                exp.expType = NameTy.BOOL;
                if (left.expType == NameTy.VOID || right.expType == NameTy.VOID) {
                    if (!(left.expType == NameTy.VOID && right.expType != NameTy.VOID)) {
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
    public void visit(IntExp exp, int level) {
    }

    @Override
    public void visit(NilExp exp, int level) {
    }

    @Override
    public void visit(VarExp exp, int level) {
        NodeType variable = lookup(exp._var.name);
        if (variable == null) {
            Error.variableDoesNotExist(exp);
            return;
        }

        if (variable.dec instanceof ArrayDec && exp._var instanceof SimpleVar) {
            exp.isArray = true;
        }
        exp.expType = variable.dec.type.type;
        exp._var.accept(this, level);
    }

    @Override
    public void visit(BoolExp exp, int level) {
    }

    @Override
    public void visit(IfExp exp, int level) {
        exp.test.accept(this, level);

        if (exp.test.expType == NameTy.VOID) {
            Error.invalidConditionType(exp);
        }

        print(level, "Entering a new if block:");
        exp.body.accept(this, level + 1);
        print(level, "Leaving the if block");

        if (!(exp._else instanceof NilExp)) {
            print(level, "Entering a new else block:");
            exp._else.accept(this, level + 1);
            print(level, "Leaving the else block");
        }
    }

    @Override
    public void visit(WhileExp exp, int level) {
        exp.test.accept(this, level);

        if (exp.test.expType == NameTy.VOID) {
            Error.invalidConditionType(exp);
        }

        print(level, "Entering a new while block:");
        exp.body.accept(this, level + 1);
        print(level, "Leaving the while block");
    }

    @Override
    public void visit(AssignExp exp, int level) {
        VarExp left = exp.left;
        Exp right = exp.right;

        left.accept(this, level);
        right.accept(this, level);

        if (left.expType != right.expType) {
            Error.invalidAssignExpression(exp);
        }
    }

    @Override
    public void visit(ReturnExp exp, int level) {
        exp.exp.accept(this, level);
        if (currentFunction.type.type != exp.exp.expType) {
            Error.invalidReturnType(exp, currentFunction.type);
        }
    }

    @Override
    public void visit(CallExp exp, int level) {
        String functionName = exp.func;
        NodeType node = lookup(functionName);

        if (functionName.equals("output") || functionName.equals("input")) {
            Error.callPredefinedFunction(exp);
            return;
        }
        if (node == null) {
            Error.functionDoesNotExit(exp);
            return;
        }

        ExpList callParameterList = exp.args;
        FunctionDec functionDec = (FunctionDec) node.dec;
        String paramString = functionDec.params == null ? "" : functionDec.params.toString();
        String argString = callParameterList == null ? "" : expListToString(callParameterList, level);
        exp.expType = functionDec.type.type;

        if (functionDec.isPrototype) {
            Error.functionPrototypeOnly(exp);
        }

        if (!paramString.equals(argString)) {
            Error.invalidCallArgumentType(exp, argString, paramString);

        }

        // for (int i = 0; i < parameterCount; ++i) {
        //     VarDec param = parameters.get(i);
        //     Exp arg = arguments.get(i);
        //     arg.accept(this, level);
        //     if (arg.expType != param.type.type) {
        //         Error.invalidCallArgumentType(exp, exp.args.toString(this, level), functionDec.params.toString());
        //         return;
        //     }
        // }
    }

    @Override
    public void visit(NameTy type, int level) {
    }

    @Override
    public void visit(ExpList list, int level) {
        for (Exp item : list.getFlattened()) {
            item.accept(this, level);
        }
    }

    @Override
    public void visit(IndexVar var, int level) {
        var.exp.accept(this, level);
        if (var.exp.expType != NameTy.INT) {
            Error.invalidIndexType(var);
        }
    }

    @Override
    public void visit(SimpleVar var, int level) {
    }
}
