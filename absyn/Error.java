package absyn;

public class Error {

    public static boolean printError = true;
    private final static String RESET = "\u001B[0m";
    private final static String RED = "\u001B[31m";
    private final static String GREEN = "\u001B[32m";
    private final static String YELLOW = "\u001B[33m";
    private static boolean isValid = true;

    public static void functionDoesNotExit(CallExp exp) {
        if (printError) {
            System.err.println(RED + "ERROR: " + RESET + "Call to undeclared function " + YELLOW
                    + "\'" + exp.func + "\'" + RESET + exp.lineToString());
        }
        isValid = false;
    }

    public static void variableDoesNotExist(VarExp exp) {
        if (printError) {
            System.err.println(RED + "ERROR: " + RESET + "Use of undeclared identifier " + YELLOW
                    + "\'" + exp._var.name + "\'" + RESET + exp.lineToString());
        }
        isValid = false;
    }

    public static void functionRedefinition(FunctionDec dec) {
        if (printError) {
            System.err.println(RED + "ERROR: " + RESET + "Redefinition of " + YELLOW
                    + "\'" + dec.name + "\'" + RESET + dec.lineToString());
        }
        isValid = false;
    }

    public static void prototypeRedefinition(FunctionDec dec) {
        if (printError) {
            System.err.println(RED + "ERROR: " + RESET + "Prototype redefinition of " + YELLOW
                    + "\'" + dec.name + "\'" + RESET + dec.lineToString());
        }
        isValid = false;
    }

    public static void variableRedefinition(Dec dec) {
        if (printError) {
            System.err.println(RED + "ERROR: " + RESET + "Redefinition of " + YELLOW
                    + "\'" + dec.name + "\'" + RESET + dec.lineToString());
        }
        isValid = false;
    }

    public static void invalidAssignExpression(AssignExp exp) {
        if (printError) {
            System.err.println(RED + "ERROR: " + RESET + "Invalid assigning of " + YELLOW
                    + "\'" + exp.left._var.name + "\'" + RESET + exp.right.lineToString());
        }
        isValid = false;
    }

    public static void invalidReturnType(ReturnExp exp, NameTy type) {
        if (printError) {
            System.err.println(RED + "ERROR: " + RESET + "Invalid return type, expected " + YELLOW
                    + "\'" + type.toString() + "\'" + RESET + exp.exp.lineToString());
        }
        isValid = false;
    }

    public static void invalidRelationalOperation(OpExp exp) {
        if (printError) {
            System.err.println(RED + "ERROR: " + RESET + "Type mismatch for relational operation " + YELLOW
                    + "\'" + exp.toString() + "\'" + RESET + exp.lineToString());
        }
        // System.out.println(exp.left.expType + " " + exp.right.expType);
        isValid = false;
    }

    public static void invalidArithmeticOperation(OpExp exp) {
        if (printError) {
            System.err.println(RED + "ERROR: " + RESET + "Type mismatch for arithmetic operation " + YELLOW
                    + "\'" + exp.toString() + "\'" + RESET + exp.lineToString());
        }
        // System.out.println(exp.left.expType + " " + exp.right.expType);
        isValid = false;
    }

    public static void invalidBooleanOperation(OpExp exp) {
        if (printError) {
            System.err.println(RED + "ERROR: " + RESET + "Type mismatch for boolean operation " + YELLOW
                    + "\'" + exp.toString() + "\'" + RESET + exp.lineToString());
        }
        // System.out.println(exp.left.expType + " " + exp.right.expType);
        isValid = false;
    }

    public static void invalidIndexType(IndexVar var) {
        if (printError) {
            System.err.println(RED + "ERROR: " + RESET + "Incompatible index value, expected " + YELLOW
                    + "\'int\'" + RESET + var.exp.lineToString());
        }
        isValid = false;
    }

    public static void invalidConditionType(Exp exp) {
        if (printError) {
            System.err.println(RED + "ERROR: " + RESET + "Invalid condition type" + exp.lineToString());
        }
        isValid = false;
    }

    // public static void invalidCallArgumentCount(CallExp exp, int expected, int actual) {
    //     if (printError) {
    //         System.err.println(
    //                 RED + "ERROR: "
    //                 + RESET + "Invalid number of arguments for call to function "
    //                 + YELLOW + "\'" + exp.func + "\'"
    //                 + RESET + ", expected "
    //                 + YELLOW + expected
    //                 + RESET + ", but received "
    //                 + YELLOW + actual
    //                 + RESET + exp.lineToString()
    //         );
    //     }
    //     isValid = false;
    // }
    public static void invalidCallArgumentType(CallExp exp, String expString, String paramString) {
        if (printError) {
            System.err.println(RED + "ERROR: " + RESET + "Argument mismatch for call to function " + YELLOW
                    + "\'" + exp.func + "\'" + RESET + ", expected " + GREEN + "\'(" + paramString + ")\'" + RESET
                    + ", but received " + GREEN + "\'(" + expString + ")\'" + RESET + exp.lineToString());
        }
        isValid = false;
    }

    public static void indexOutOfBounds(IndexVar var, int size) {
        if (printError) {
            System.err.println(RED + "ERROR: " + RESET + "Index out of bounds for array of size " + size + ": "
                    + YELLOW + "\'" + var.name + "\'" + RESET + var.lineToString()
            );
        }
        isValid = false;
    }

    public static void invalidTypeDeclaration(Dec dec) {
        if (printError) {
            System.err.println(
                    RED + "ERROR: "
                    + RESET + "Type "
                    + YELLOW + "\'" + dec.type + "\' "
                    + RESET + "is not meaningful and will be changed to "
                    + YELLOW + "\'int\'"
                    + RESET + dec.lineToString()
            );
        }
        isValid = false;
    }

    public static boolean getIsValid() {
        return isValid;
    }
}
