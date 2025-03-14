package absyn;

public class Error {

    private final static String RESET = "\u001B[0m";
    private final static String RED = "\u001B[31m";
    private final static String GREEN = "\u001B[32m";
    private final static String YELLOW = "\u001B[33m";
    private static boolean isValid = true;

    public static void functionDoesNotExit(CallExp exp) {
        System.err.println(RED + "ERROR: " + RESET + "Call to undeclared function " + YELLOW
                + "\'" + exp.func + "\'" + RESET + exp.lineToString());
        isValid = false;
    }

    public static void variableDoesNotExist(VarExp exp) {

        System.err.println(RED + "ERROR: " + RESET + "Use of undeclared identifier " + YELLOW
                + "\'" + exp._var.name + "\'" + RESET + exp.lineToString());
        isValid = false;
    }

    public static void functionRedefinition(FunctionDec dec) {
        System.err.println(RED + "ERROR: " + RESET + "Redefinition of " + YELLOW
                + "\'" + dec.name + "\'" + RESET + dec.lineToString());
        isValid = false;
    }

    public static void prototypeRedefinition(FunctionDec dec) {
        System.err.println(RED + "ERROR: " + RESET + "Prototype redefinition of " + YELLOW
                + "\'" + dec.name + "\'" + RESET + dec.lineToString());
        isValid = false;
    }

    public static void variableRedefinition(Dec dec) {
        System.err.println(RED + "ERROR: " + RESET + "Redefinition of " + YELLOW
                + "\'" + dec.name + "\'" + RESET + dec.lineToString());
        isValid = false;
    }

    public static void invalidRelationalOperation(OpExp exp) {
        System.err.println(RED + "ERROR: " + RESET + "Type mismatch for relational operation " + YELLOW
                + "\'" + exp.toString() + "\'" + RESET + exp.lineToString());
        isValid = false;
    }

    public static void invalidArithmeticOperation(OpExp exp) {
        System.err.println(RED + "ERROR: " + RESET + "Type mismatch for arithmetic operation " + YELLOW
                + "\'" + exp.toString() + "\'" + RESET + exp.lineToString());
        isValid = false;
    }

    public static void invalidBooleanOperation(OpExp exp) {
        System.err.println(RED + "ERROR: " + RESET + "Type mismatch for boolean operation " + YELLOW
                + "\'" + exp.toString() + "\'" + RESET + exp.lineToString());
        isValid = false;
    }

    public static void invalidIndexType(IndexVar var) {
        System.err.println(RED + "ERROR: " + RESET + "Incompatible index value, expected " + YELLOW
                + "\'int\'" + RESET + var.exp.lineToString());
        isValid = false;
    }

    public static void invalidReturnType(ReturnExp exp) {
        // System.err.println(RED + "ERROR: " + RESET + "Incompatible return type, expected " + YELLOW
        //         + "\'" + dec.name + "\'" + RESET + dec.lineToString());
        // isValid = false;
    }

    public static boolean getIsValid() {
        return isValid;
    }
}
