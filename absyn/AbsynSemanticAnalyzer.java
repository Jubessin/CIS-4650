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
            Error.variableRedefinition(dec);
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
        NodeType node = lookup(dec.name);

        if (node != null) {
            FunctionDec functionDec = (FunctionDec) node.dec;

            if (!functionDec.params.toString().equals(dec.params.toString())) {
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
        try (var writer = new PrintWriter(file.replace(".cm", ".sym"), "UTF-8")) {
            writer.println(tableBuilder.toString());
        }
    }

    public void addPredefinedFunctions() {
        NameTy nameType = new NameTy(-1, -1, NameTy.INT);
        FunctionDec input = new FunctionDec(-1, -1, nameType, "input", null, new NilExp(-1, -1)),
                output = new FunctionDec(-1, -1, nameType, "output", null, new NilExp(-1, -1));
        insert(input, 1);
        insert(output, 1);
    }

    @Override
    public void visit(DecList list, int level) {
        print(level++, "Entering the global scope:");

        addPredefinedFunctions();

        for (var item : list.getFlattened()) {
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
        insert(dec, level);
    }

    @Override
    public void visit(ArrayDec dec, int level) {
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

        ExpList iter = new ExpList(exp.exps.head, exp.exps.tail);
        while (iter != null) {
            if (iter.head != null) {
                iter.head.accept(this, level);
            }
            iter = iter.tail;
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
        for (var item : list.getFlattened()) {
            item.accept(this, level);
        }
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
                exp.expType = NameTy.INT;
                if (left.expType != NameTy.INT || right.expType != NameTy.INT) {
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
        if (currentFunction.type.type != getResolvedType(exp.exp)) {
            Error.invalidReturnType(exp, currentFunction.type);
        }
    }

    private static int getResolvedType(Exp exp) {
        if (exp instanceof ResolvableExp) {
            var _exp = ((ResolvableExp)exp);

            var node = lookup(_exp.getResolvedName());

            return node.dec.type.type;
        }

        return exp.expType;        
    }

    @Override
    public void visit(CallExp exp, int level) {
        String functionName = exp.func;
        NodeType node = lookup(functionName);

        if (node == null) {
            Error.functionDoesNotExit(exp);
            return;
        }

        var callParameterList = exp.args;
        var functionDec = (FunctionDec)node.dec;
        var functionParameterList = functionDec.params;

        if (callParameterList == null && functionParameterList == null)
            return;

        if (callParameterList == null || functionParameterList == null) {
            Error.invalidCallArgumentType(exp);
            return;
        }

        var arguments = callParameterList.getFlattened();
        var parameters = functionParameterList.getFlattened();

        if (arguments.size() != parameters.size()) {
            Error.invalidCallArgumentCount(exp);
            return;
        }

        for (int i = 0; i < arguments.size(); ++i) {
            var arg = arguments.get(i);
            var param = parameters.get(i);

            if (getResolvedType(arg) != param.type.type) {
                Error.invalidCallArgumentType(exp);
                return;
            }
        }
    }

    @Override
    public void visit(NameTy type, int level) {
    }

    @Override
    public void visit(ExpList list, int level) {
        for (var item : list.getFlattened()) {
            item.accept(this, level);
        }
    }

    @Override
    public void visit(IndexVar var, int level) {
        // TODO: check out of bounds
        var.exp.accept(this, level);
        if (getResolvedType(var.exp) != NameTy.INT) {
            Error.invalidIndexType(var);
        }
    }

    @Override
    public void visit(SimpleVar var, int level) {

    }
}
